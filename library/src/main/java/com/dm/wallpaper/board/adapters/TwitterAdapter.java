package com.dm.wallpaper.board.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.wallpaper.board.R;
import com.dm.wallpaper.board.TwitterStatusModel.Status;
import com.dm.wallpaper.board.TwitterStatusModel.TwitterResponse;
import com.squareup.picasso.Picasso;


import java.util.List;

public class TwitterAdapter extends RecyclerView.Adapter<TwitterAdapter.ViewHolder> {


    private List<Status> items;

    private MyClickListener itemClickListener;

    public TwitterAdapter(List<Status> items) {
        this.items = items;

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.itemClickListener = myClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.twitter_status_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status status = items.get(position);
        holder.tv_status.setText(status.getStatus());
        holder.tv_user_name.setText(status.getUsername());
        holder.tv_twitter_user_description.setText(status.getUserDescription());
        holder.tv_twitter_user_time.setText(status.getStatusTime());
        Picasso.get().load(status.getUserImage()).placeholder(R.drawable.twitter_logo).into(holder.iv_user_image);
        Log.e("Image: ",status.getUserImage()+"");
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tv_status;
        public TextView tv_user_name;
        public TextView tv_twitter_user_description;
        public TextView tv_twitter_user_time;
        public ImageView iv_user_image;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_status = (TextView) itemView.findViewById(R.id.tv_twitter_status_content);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_twitter_user_name);
            tv_twitter_user_time = (TextView) itemView.findViewById(R.id.tv_twitter_user_time);
            tv_twitter_user_description = (TextView) itemView.findViewById(R.id.tv_twitter_user_description);
            iv_user_image = (ImageView) itemView.findViewById(R.id.iv_twitter_image);


            /*itemView.setOnClickListener(this);*/
        }


        public void onClick(View v) {
            /*itemClickListener.onItemClick(getPosition());*/
        }
    }

}
