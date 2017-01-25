package com.org.pawpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.adapter.ConversationAdapter;
import com.org.pawpal.model.GetThreadMessageResponse;
import com.org.pawpal.model.PostMessage;
import com.org.pawpal.model.SendMessageResponse;
import com.org.pawpal.model.ThreadMessage;
import com.org.pawpal.model.ThreadMessageResponse;
import com.org.pawpal.model.ThreadProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.org.pawpal.R.string.conversations;

/**
 * Created by hp-pc on 14-01-2017.
 */

public class ConversationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener  {
    private RecyclerView recyclerViewConversation;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ThreadMessage> threadMessages;
    private ConversationAdapter conversationAdapter;
    private CompositeSubscription compositeSubscription;
    private String threadId, otherUserProfileId;
    private ProgressBar progressBar;
    private CircleImageView ivProfileImage;
    private TextView tvCOnversationUser,tvConversationWith, tvMemberSince, tvDistance, tvViewProfile;
    private RelativeLayout sendMessage;
    private EditText etMsg;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_activity);
        getBundleData();
        init();
        getMessages();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            threadId = bundle.getString("thread_id");
        }
    }

    private void init() {
        compositeSubscription = new CompositeSubscription();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(conversations));
        threadMessages = new ArrayList<>();
        tvCOnversationUser = (TextView)findViewById(R.id.tv_username);
        sendMessage = (RelativeLayout)findViewById(R.id.rl_send_msg);
        etMsg = (EditText)findViewById(R.id.et_msg);
        ivProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        tvMemberSince = (TextView)findViewById(R.id.tv_member_txt);
        tvDistance = (TextView)findViewById(R.id.tv_location);
        tvViewProfile = (TextView)findViewById(R.id.tv_view_profile);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        tvConversationWith = (TextView)findViewById(R.id.tv_conversation_with_user);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        recyclerViewConversation = (RecyclerView) findViewById(R.id.rv_messages);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewConversation.setLayoutManager(linearLayoutManager);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etMsg.getText().toString().isEmpty())
                    sendReply(etMsg.getText().toString());
            }
        });
        swipeRefreshLayout.setOnRefreshListener(this);
        tvViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ConversationActivity.this, PalProfileActivity.class);
                intent.putExtra("other_user_profile", otherUserProfileId);
                startActivity(intent);
            }
        });

    }
    private void sendReply(final String message)
    {
        progressBar.setVisibility(View.VISIBLE);
        PostMessage postMessage = new PostMessage();
        postMessage.setMessage_text(message);
        postMessage.setUser_profile_id(otherUserProfileId);
        postMessage.setThread_id(threadId);
        postMessage.setProfile_id(PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID));
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().sendMessage(postMessage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendMessageResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }

                    @Override
                    public void onNext(SendMessageResponse sendMessageResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(sendMessageResponse.getCode()) == Constants.SUCCESS_CODE) {
                            ThreadMessage threadMessage = sendMessageResponse.getThreadMessage();
                            threadMessage.setIs_own_msg(1);
                            threadMessages.add(threadMessage);
                            conversationAdapter.notifyDataSetChanged();
                            recyclerViewConversation.smoothScrollToPosition(conversationAdapter.getItemCount()-1);
                            etMsg.setText("");
                            hideKeyBoard();
                        } else
                            showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
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

    private void getMessages() {
        if (!swipeRefreshLayout.isRefreshing())
            showHideProgressBar(View.VISIBLE);
//        recyclerViewConversation.setVisibility(View.GONE);
        String profileId = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getThreadMessages(profileId, threadId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetThreadMessageResponse>() {
                    @Override
                    public void onCompleted() {
                        showHideProgressBar(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showHideProgressBar(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GetThreadMessageResponse response) {
                        showHideProgressBar(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        tvViewProfile.setEnabled(true);
                        tvViewProfile.setClickable(true);
                        recyclerViewConversation.setVisibility(View.VISIBLE);
                        if (Integer.valueOf(response.getCode()) == Constants.SUCCESS_CODE) {
                            ArrayList<ThreadMessage> messages = response.getResponse().getMessages();

                            if (messages != null && messages.size()!= 0) {
                                setData(response.getResponse());
                                threadMessages.clear();
                                threadMessages.addAll(messages);
                                conversationAdapter = new ConversationAdapter(ConversationActivity.this, threadMessages, response.getResponse().getProfile().getName());
                                recyclerViewConversation.setAdapter(conversationAdapter);
                                otherUserProfileId = response.getResponse().getProfile().getProfile_id();
//                                conversationAdapter.notifyDataSetChanged();
                            }


                        } else
                            showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }
                }));
    }

    private void setData(ThreadMessageResponse response) {
        ThreadProfile threadProfile = response.getProfile();
        tvCOnversationUser.setText(threadProfile.getName());
        tvMemberSince.setText(threadProfile.getProfile_created_at());
        tvConversationWith.setText("Conversation with "+threadProfile.getName());
        tvDistance.setText(threadProfile.getDistance()+" from you");
        if (threadProfile.getUserImages() != null && threadProfile.getUserImages().size() != 0)
        {
            String imageUrl = threadProfile.getUserImages().get(0).getUrl();
            Picasso.with(this).load(imageUrl).fit().centerCrop().placeholder(R.mipmap.img_default).into(ivProfileImage);
        }

    }

    private void showHideProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onRefresh() {
        getMessages();
    }
}
