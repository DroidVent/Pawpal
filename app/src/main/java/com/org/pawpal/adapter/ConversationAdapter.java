package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.model.ThreadMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 14-01-2017.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.MyViewHolder> {
    private ArrayList<ThreadMessage> threadMessages;
    private Context context;
    private String username;
    private String ownPhoto;

    public ConversationAdapter(Context context, ArrayList<ThreadMessage> conversations, String name) {
        this.threadMessages = conversations;
        this.context = context;
        this.username = name;
        ownPhoto = PrefManager.retrieve(context, PrefManager.PersistenceKey.PROFILE_IMAGE,Constants.GENERAL_PREF_NAME);
    }

    @Override
    public ConversationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        /*if (viewType == LOGGED_IN_PROFILE)
            layout = R.layout.sender_view;
        else
            layout = R.layout.receiver_view;*/
        switch (viewType) {
            case Constants.MINE_MESSAGE:
                layout = R.layout.sender_view;
                break;
            case Constants.OTHER_USER_MESSAGE:
                layout = R.layout.receiver_view;
                break;
        }
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        return new ConversationAdapter.MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        return threadMessages.get(position).getIs_own_msg();
    }

    @Override
    public void onBindViewHolder(ConversationAdapter.MyViewHolder holder, int position) {
        ThreadMessage message = threadMessages.get(position);
        holder.tvMessage.setText(message.getMessage_text());
        holder.tvDate.setText(message.getCreated_date());
        String image;
        if (message.getIs_own_msg() == 0)
        {
            holder.tvName.setText(username);
            if (message.getProfile_image() != null) {
                image = message.getProfile_image();
                Picasso.with(context).load(image).fit().centerCrop().placeholder(R.mipmap.img_default).into(holder.ivProfile);
            }
        }
        else
            Picasso.with(context).load(ownPhoto).fit().centerCrop().placeholder(R.mipmap.img_default).into(holder.ivProfile);

    }

    @Override
    public int getItemCount() {
        return threadMessages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage, tvDate, tvName;
        public View item;
        private ImageView ivProfile;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            ivProfile = (CircleImageView) item.findViewById(R.id.profile_image);
            tvDate = (TextView) item.findViewById(R.id.tv_date);
            tvMessage = (TextView) item.findViewById(R.id.tv_msg);
            tvName = (TextView) item.findViewById(R.id.tv_username);
        }
    }
}
