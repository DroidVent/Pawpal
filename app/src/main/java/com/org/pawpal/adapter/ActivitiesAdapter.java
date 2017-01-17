package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.org.pawpal.R;
import com.org.pawpal.interfaces.OnItemCheckBoxListener;
import com.org.pawpal.model.PalActivity;

import java.util.List;

/**
 * Created by hp-pc on 17-12-2016.
 */

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {
    private List<PalActivity> pawActivityList;
    private Context context;
    private int[] preSelectedActivitiesPositions;
    private OnItemCheckBoxListener onItemCheckBoxListener;
    public ActivitiesAdapter(List<PalActivity> pawActivityList, OnItemCheckBoxListener onItemCheckBoxListener) {
        this.pawActivityList = pawActivityList;
        this.onItemCheckBoxListener = onItemCheckBoxListener;
    }

    @Override
    public ActivitiesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActivitiesAdapter.MyViewHolder holder, final int position) {
        PalActivity pawActivity = pawActivityList.get(position);
        holder.checkBox.setText(pawActivity.getName());
        if (pawActivity.isChecked())
            holder.checkBox.setChecked(true);
        final int[] i = {1};
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    onItemCheckBoxListener.onItemCheck(position);
                }
                else
                    onItemCheckBoxListener.onItemUnCheck(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pawActivityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public View item;
        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            checkBox = (CheckBox) item.findViewById(R.id.cb_activity);
        }
    }
    public void setItemCheck(int pos)
    {

    }

}
