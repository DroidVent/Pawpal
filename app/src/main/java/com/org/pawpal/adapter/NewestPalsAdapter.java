package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.interfaces.OnPalItemClickListener;
import com.org.pawpal.model.NewestPal;
import com.org.pawpal.model.UserImages;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 27-01-2017.
 */

public class NewestPalsAdapter extends RecyclerView.Adapter<NewestPalsAdapter.MyViewHolder> {
    private List<NewestPal> newestPals;
    private Context context;
    private OnPalItemClickListener onItemClickListener;

    public NewestPalsAdapter(List<NewestPal> newestPals, Context context, OnPalItemClickListener onItemClickListener)
    {
        this.context = context;
        this.newestPals = newestPals;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public NewestPalsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_item, parent, false);
        return new NewestPalsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewestPalsAdapter.MyViewHolder holder, final int position) {
        NewestPal newestPal= newestPals.get(position);
        holder.tvUsername.setText(newestPal.getName());
        holder.tvDistance.setText(newestPal.getDistance());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                {
                    onItemClickListener.onPalClicked(position);
                }
            }
        });
        List<UserImages> userImage = newestPal.getImages();
        if (userImage != null && userImage.size() != 0 )
            Picasso.with(context).load(newestPal.getImages().get(0).getUrl()).placeholder(R.mipmap.img_default).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return newestPals.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvUsername;
        TextView tvDistance;
        View itemView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = (CircleImageView)itemView.findViewById(R.id.profile_image);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
        }
    }
}

