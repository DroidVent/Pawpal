package com.org.pawpal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.org.pawpal.R;
import com.org.pawpal.adapter.ConversationAdapter;
import com.org.pawpal.model.Message;

import java.util.ArrayList;

/**
 * Created by hp-pc on 14-01-2017.
 */

public class ConversationActivity extends BaseActivity {
    private RecyclerView recyclerViewConversation;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Message> messages;
    private ConversationAdapter conversationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);
        init();
    }

    private void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.messages));
        messages = new ArrayList<>();
        Message message1 = new Message();
        message1.setReply("");
        message1.setType(0);

        Message message2 = new Message();
        message2.setReply("");
        message2.setType(1);

        Message message3 = new Message();
        message3.setReply("");
        message3.setType(0);

        messages.add(message1);
        messages.add(message2);
        messages.add(message3);
        recyclerViewConversation = (RecyclerView)findViewById(R.id.rv_messages);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewConversation.setLayoutManager(linearLayoutManager);
        conversationAdapter = new ConversationAdapter(messages);
        recyclerViewConversation.setAdapter(conversationAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
