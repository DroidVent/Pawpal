package com.org.pawpal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.interfaces.OnInboxListener;
import com.org.pawpal.model.Message;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 14-01-2017.
 */

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.MyViewHolder>  {
    private ArrayList<Message> messages;
    private OnInboxListener onInboxListener;

    public InboxAdapter(ArrayList<Message> messages, OnInboxListener onInboxListener) {
        this.messages = messages;
        this.onInboxListener = onInboxListener;
    }

    @Override
    public InboxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_item, parent, false);

        return new InboxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InboxAdapter.MyViewHolder holder, final int position) {
        Message message = messages.get(position);
        if (message.getIsStar() == 0)
            holder.ivStar.setVisibility(View.GONE);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onInboxListener !=null)
                    onInboxListener.onItemClicked(position);
            }
        });
        holder.starUnstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onInboxListener !=null)
                    onInboxListener.onStarClicked(position);
            }
        });
        holder.archieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onInboxListener !=null)
                    onInboxListener.onArchieveClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View item;
        public RelativeLayout starUnstar, archieve;
        private ImageView ivStar;
        private TextView tvDate;
        private CircleImageView ivProfile;
        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            starUnstar = (RelativeLayout)item.findViewById(R.id.rl_star_unstart);
            archieve = (RelativeLayout)item.findViewById(R.id.rl_archieve);
            ivStar = (ImageView)item.findViewById(R.id.iv_star);
            tvDate = (TextView)item.findViewById(R.id.tv_date);
            ivProfile = (CircleImageView)item.findViewById(R.id.profile_image);
        }
    }
}
