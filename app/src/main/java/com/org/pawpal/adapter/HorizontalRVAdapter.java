package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.org.pawpal.R;
import com.org.pawpal.model.PalActivity;

import java.util.List;

/**
 * Created by hp-pc on 31-12-2016.
 */

public class HorizontalRVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PalActivity> mDataList;
    private int mRowIndex = -1;

    public HorizontalRVAdapter() {
    }

    public HorizontalRVAdapter(List<PalActivity> palActivities) {
        mDataList=palActivities;
    }

    public void setData(List<PalActivity> data) {
        if (mDataList != data) {
            mDataList = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivActivity;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ivActivity = (ImageView) itemView.findViewById(R.id.iv_activity);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder) rawHolder;
        PalActivity activity = mDataList.get(position);
        switch (activity.getName())
        {
            case "Exercise":
                holder.ivActivity.setImageResource(R.mipmap.icon1);
                break;
            case "Walk":
                holder.ivActivity.setImageResource(R.mipmap.icon3);
                break;
            case "Grooming":
                holder.ivActivity.setImageResource(R.mipmap.icon2);
                break;
            case "Bath":
                holder.ivActivity.setImageResource(R.mipmap.icon4);
                break;
            case "Feeding":
                holder.ivActivity.setImageResource(R.mipmap.icon3);
                break;
            case "Company":
                holder.ivActivity.setImageResource(R.mipmap.icon3);
                break;
            case "Play":
                holder.ivActivity.setImageResource(R.mipmap.icon4);
                break;
            default:
                break;

        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
