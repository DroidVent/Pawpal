package com.org.pawpal.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.model.Message;

import java.util.ArrayList;

/**
 * Created by hp-pc on 14-01-2017.
 */

public class ConversationAdapter extends  RecyclerView.Adapter<ConversationAdapter.MyViewHolder>  {
    private ArrayList<Message> messages;
    public ConversationAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public ConversationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
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
        return messages.get(position).getType();
    }
    @Override
    public void onBindViewHolder(ConversationAdapter.MyViewHolder holder, int position) {
        Message message = messages.get(position);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage;
        public View item;
        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
        }
    }
}
