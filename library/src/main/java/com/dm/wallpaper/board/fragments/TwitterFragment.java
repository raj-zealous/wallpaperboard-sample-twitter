package com.dm.wallpaper.board.fragments;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.danimahardhika.android.helpers.core.ViewHelper;
import com.danimahardhika.android.helpers.core.WindowHelper;
import com.danimahardhika.android.helpers.core.utils.LogUtil;
import com.dm.wallpaper.board.R;
import com.dm.wallpaper.board.R2;
import com.dm.wallpaper.board.TwitterStatusModel.Status;
import com.dm.wallpaper.board.TwitterStatusModel.TwitterResponse;
import com.dm.wallpaper.board.adapters.AboutAdapter;
import com.dm.wallpaper.board.adapters.TwitterAdapter;
import com.dm.wallpaper.board.applications.WallpaperBoardApplication;
import com.dm.wallpaper.board.helpers.ConfigurationHelper;
import com.dm.wallpaper.board.preferences.Preferences;
import com.dm.wallpaper.board.utils.listeners.NavigationListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TwitterFragment extends Fragment {


    @BindView(R2.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        if (!Preferences.get(getActivity()).isShadowEnabled()) {
            View shadow = view.findViewById(R.id.shadow);
            if (shadow != null) shadow.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        resetRecyclerViewPadding(getResources().getConfiguration().orientation);
        ViewHelper.setupToolbar(mToolbar);

        TextView textView = getActivity().findViewById(R.id.title);
        textView.setText("Twitter");
        mToolbar.setTitle("");
        mToolbar.setNavigationIcon(ConfigurationHelper.getNavigationIcon(getActivity(),
                WallpaperBoardApplication.getConfig().getNavigationIcon()));
        mToolbar.setNavigationOnClickListener(view -> {
            try {
                NavigationListener listener = (NavigationListener) getActivity();
                listener.onNavigationIconClick();
            } catch (IllegalStateException e) {
                LogUtil.e("Parent activity must implements NavigationListener");
            }
        });


        String twitter_statuses = loadJSONFromAsset();

        List<Status> statusList = new ArrayList<>();

        try {
            JSONObject twitter_object = new JSONObject(twitter_statuses);
            JSONArray twitter_status_array = twitter_object.getJSONArray("statuses");

            for (int status = 0; status < twitter_status_array.length(); status++) {

                Status twitter_status = new Status();

                JSONObject statusObj = twitter_status_array.optJSONObject(status);
                twitter_status.setStatus(statusObj.optString("text"));

                JSONObject userObj = statusObj.optJSONObject("user");
                twitter_status.setUsername(userObj.optString("name"));
                twitter_status.setUserDescription(userObj.optString("description"));
                twitter_status.setUserImage(userObj.optString("profile_background_image_url"));


                twitter_status.setStatusTime(userObj.optString("created_at").split(" ")[2] + " " + userObj.optString("created_at").split(" ")[1]);


                statusList.add(twitter_status);
            }


        } catch (JSONException e) {
            Log.e("Error in Json Retrival", e.toString());
        }

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false
        ));
        mRecyclerView.setAdapter(new TwitterAdapter(statusList));

    }


    private void resetRecyclerViewPadding(int orientation) {
        if (mRecyclerView == null) return;

        int padding = 0;
        int navBar = 0;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            padding = getActivity().getResources().getDimensionPixelSize(R.dimen.content_padding);
            navBar = padding;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean tabletMode = getResources().getBoolean(R.bool.android_helpers_tablet_mode);
            if (tabletMode || orientation == Configuration.ORIENTATION_PORTRAIT) {
                navBar = WindowHelper.getNavigationBarHeight(getActivity());
            }
        }
        mRecyclerView.setPadding(padding, padding, 0, navBar);
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("twitter_status.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}