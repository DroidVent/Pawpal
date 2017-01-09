package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.interfaces.OnItemClickListener;
import com.org.pawpal.model.SearchPal;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hp-pc on 31-12-2016.
 */

public class FindPalAdapter extends RecyclerView.Adapter<FindPalAdapter.MyViewHolder>  {
    private List<SearchPal> pals;
    private RecyclerView horizontalList;
//    private List<PalActivity> palActivities;
    private OnItemClickListener onItemClickListener;
    private Context context;
    public FindPalAdapter(List<SearchPal> pals, OnItemClickListener onItemClickListener, Context context)
    {
        this.pals = pals;
        this.onItemClickListener = onItemClickListener;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pal_item, parent, false);

        return new FindPalAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        SearchPal searchPal = pals.get(position);
        String name  = searchPal.getName();
        String desc  = searchPal.getDescription();
        String size  = searchPal.getPet_size();
        String period  = searchPal.getPeriod();
        holder.horizontalAdapter.setData(searchPal.getPalActivities());
        if (!name.isEmpty() && name != null)
            holder.tvPalName.setText(name);
        if ( desc != null && !desc.isEmpty())
            holder.tvDescription.setText(desc);
        if ( size != null && !size.isEmpty())
            setSizeValue(holder.tvSize, size);
        if (period != null && !period.isEmpty())
            holder.tvPeriod.setText(searchPal.getPeriod());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onClicked(position);
            }
        });
        if (searchPal.getUserImages() != null && searchPal.getUserImages().size() != 0)
            Picasso.with(context).load(searchPal.getUserImages().get(0).getUrl()).fit().centerCrop().placeholder(R.mipmap.img_default).into(holder.ivPalProfile);
    }
    private void setSizeValue(TextView tvSize, String size) {
        switch (size)
        {
            case "SM":
                tvSize.setText("Small");
                break;
            case "XS":
                tvSize.setText(Constants.X_SMALL);
                break;
            case "MD":
                tvSize.setText(Constants.Medium);
                break;
            case "LG":
                tvSize.setText(Constants.LARGE);
                break;
            case "XL":
                tvSize.setText(Constants.EXTRA_LARGE);
                break;
            case "VS":
                tvSize.setText(Constants.VERY_SMALL);
                break;
            default:
                break;
        }
    }
    @Override
    public int getItemCount() {
        return pals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View item;
        private TextView tvPalName, tvDescription, tvSize, tvPeriod;
        private ImageView ivPalProfile;
        private LinearLayout layoutSize, layoutPeriod;
        private HorizontalRVAdapter horizontalAdapter;
        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            tvPalName = (TextView) item.findViewById(R.id.tv_pal_name);
            tvDescription = (TextView) item.findViewById(R.id.tv_pal_description);
            layoutSize = (LinearLayout) item.findViewById(R.id.ll_size);
            layoutPeriod = (LinearLayout) item.findViewById(R.id.ll_period);
            tvSize = (TextView) item.findViewById(R.id.tv_size);
            tvPeriod = (TextView) item.findViewById(R.id.tv_period);
            ivPalProfile = (ImageView)item.findViewById(R.id.profile_image);
            horizontalList = (RecyclerView) itemView.findViewById(R.id.rv_activities);
            horizontalList.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new HorizontalRVAdapter();
            horizontalList.setAdapter(horizontalAdapter);
        }
    }
}
