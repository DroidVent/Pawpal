package com.org.pawpal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.org.pawpal.R;
import com.org.pawpal.custom.CustomTextView;
import com.org.pawpal.interfaces.OnMessagesListener;
import com.org.pawpal.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 16-01-2017.
 */
public class ArchieveAdapter extends RecyclerView.Adapter<ArchieveAdapter.MyViewHolder> {
    private ArrayList<Message> conversations;
    private OnMessagesListener onMessagesListener;
    public ArchieveAdapter(ArrayList<Message> conversations, OnMessagesListener onMessagesListener) {
        this.conversations = conversations;
        this.onMessagesListener = onMessagesListener;
    }
    @Override
    public ArchieveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archieve_item, parent, false);

        return new ArchieveAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArchieveAdapter.MyViewHolder holder, final int position) {
        Message message = conversations.get(position);
        holder.tvMsg.setText(message.getMessage_text());
        holder.tvUsername.setText(message.getName());
        if (message.getImages() != null)
            if (message.getImages().size() != 0)
            {
                Picasso.with(holder.tvMsg.getContext()).load(message.getImages().get(0).getUrl()).fit().centerCrop().placeholder(R.mipmap.img_default).into(holder.ivProfile);
            }
        holder.unArchieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMessagesListener != null)
                    onMessagesListener.onArchieveClicked(position);
            }
        });
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMessagesListener != null)
                    onMessagesListener.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View item;
        private CircleImageView ivProfile;
        private CustomTextView tvMsg, tvUsername;
        private RelativeLayout unArchieve;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            ivProfile = (CircleImageView) item.findViewById(R.id.profile_image);
            tvMsg = (CustomTextView) item.findViewById(R.id.tv_msg);
            tvUsername = (CustomTextView) item.findViewById(R.id.tv_username);
            unArchieve = (RelativeLayout) item.findViewById(R.id.rl_unarchieve);
        }
    }
}
