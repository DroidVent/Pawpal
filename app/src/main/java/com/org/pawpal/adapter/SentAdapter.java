package com.org.pawpal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.model.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hp-pc on 16-01-2017.
 */
public class SentAdapter  extends RecyclerView.Adapter<SentAdapter.MyViewHolder> {
    private ArrayList<Message> messages;
    Context context;

    public SentAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public SentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sent_item, parent, false);

        return new SentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SentAdapter.MyViewHolder holder, final int position) {
        Message message = messages.get(position);
        Log.e("Sent",""+message.getMessage_text().trim());
        holder.tvMsg.setText(message.getMessage_text());

        holder.tvDate.setText(message.getCreated_date());
        holder.tvUsername.setText(message.getName());
        String image;
        if (message.getImages() != null)
            if (message.getImages().size() != 0)
            {
                image = message.getImages().get(0).getUrl();
                Picasso.with(context).load(image).fit().centerCrop().placeholder(R.mipmap.img_default).into(holder.ivProfile);
            }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public View item;
        private CircleImageView ivProfile;
        private TextView tvDate, tvMsg, tvUsername;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            ivProfile = (CircleImageView) item.findViewById(R.id.profile_image);
            tvDate = (TextView) item.findViewById(R.id.tv_date);
            tvMsg = (TextView) item.findViewById(R.id.tv_msg);
            tvUsername = (TextView) item.findViewById(R.id.tv_username);
        }
    }
}
