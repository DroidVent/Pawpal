package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.model.Favorite;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 02-01-2017.
 */

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.MyViewHolder> {
    private List<Favorite> favorites;
    private Context context;

    public FavoritesAdapter(List<Favorite> favorites, Context context)
    {
        this.context = context;
        this.favorites = favorites;
    }

    @Override
    public FavoritesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fav_item, parent, false);
        return new FavoritesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoritesAdapter.MyViewHolder holder, int position) {
        Favorite favorite = favorites.get(position);
        holder.tvUsername.setText(favorite.getName());
        holder.tvDistance.setText(favorite.getDistance());
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvUsername;
        TextView tvDistance;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView)itemView.findViewById(R.id.profile_image);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            tvDistance = (TextView) itemView.findViewById(R.id.tv_distance);
        }
    }
}
