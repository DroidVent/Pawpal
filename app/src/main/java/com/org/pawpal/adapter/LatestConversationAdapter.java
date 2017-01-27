package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.custom.CustomTextView;
import com.org.pawpal.interfaces.OnMessagesListener;
import com.org.pawpal.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 27-01-2017.
 */

public class LatestConversationAdapter extends RecyclerView.Adapter<LatestConversationAdapter.MyViewHolder>  {
    private ArrayList<Message> messages;
    private OnMessagesListener onMessagesListener;
    private Context context;
    public LatestConversationAdapter(Context context, ArrayList<Message> conversations, OnMessagesListener onMessagesListener) {
        this.messages = conversations;
        this.onMessagesListener = onMessagesListener;
        this.context = context;
    }

    @Override
    public LatestConversationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inbox_item, parent, false);

        return new LatestConversationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LatestConversationAdapter.MyViewHolder holder, final int position) {
        Message message = messages.get(position);
        Integer isStar = message.getIsFav();
        int isArchieve = message.getIs_archive();
        if (isStar == null || isStar == 0)
        {
            holder.ivStar.setVisibility(View.GONE);
            holder.tvStarUnStar.setText("Star");
            holder.ivStarUnstar.setImageResource(R.mipmap.star_dark);
        }
        else
        {
            holder.ivStar.setVisibility(View.VISIBLE);
            holder.tvStarUnStar.setText("Unstar");
            holder.ivStarUnstar.setImageResource(R.mipmap.unstar);
        }
        if (isArchieve == 1)
        {
            holder.tvArchieve.setText("Archive");
        }
        else
            holder.tvArchieve.setText("Unarchive");
        holder.tvMessage.setText(message.getMessage_text());
        holder.tvDate.setText(message.getCreated_date());
        holder.tvUsername.setText(message.getName());
        String image;
        if (message.getImages() != null)
            if (message.getImages().size() != 0)
            {
                image = message.getImages().get(0).getUrl();
                Picasso.with(context).load(image).fit().centerCrop().placeholder(R.mipmap.img_default).into(holder.ivProfile);
            }

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMessagesListener !=null)
                    onMessagesListener.onItemClicked(position);
            }
        });
        holder.starUnstar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMessagesListener !=null)
                    onMessagesListener.onStarClicked(position);
            }
        });
        holder.archieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onMessagesListener !=null)
                    onMessagesListener.onArchieveClicked(position);
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
        private ImageView ivStar, ivStarUnstar;
        private TextView tvDate, tvMessage;
        private CircleImageView ivProfile;
        public TextView tvUsername;
        public TextView tvStarUnStar,tvArchieve;
        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            starUnstar = (RelativeLayout)item.findViewById(R.id.rl_star_unstart);
            archieve = (RelativeLayout)item.findViewById(R.id.rl_archieve);
            ivStar = (ImageView)item.findViewById(R.id.iv_star);
            tvDate = (TextView)item.findViewById(R.id.tv_date);
            tvMessage = (TextView)item.findViewById(R.id.tv_msg);
            tvUsername = (TextView)item.findViewById(R.id.tv_username);
            ivProfile = (CircleImageView)item.findViewById(R.id.profile_image);
            tvStarUnStar = (CustomTextView)item.findViewById(R.id.tv_star_unstar);
            tvArchieve = (CustomTextView)item.findViewById(R.id.tv_archieve);
            ivStarUnstar = (ImageView)item.findViewById(R.id.iv_star_unstar);
        }
    }
}

