package com.org.pawpal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.activities.ConversationActivity;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.adapter.InboxAdapter;
import com.org.pawpal.interfaces.OnInboxListener;
import com.org.pawpal.model.GetInboxMessageResponse;
import com.org.pawpal.model.Message;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 14-01-2017.
 */

public class InboxFragment extends Fragment implements OnInboxListener, SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recyclerViewInbox;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Message> inboxMessages;
    private InboxAdapter inboxAdapter;
    private ProgressBar progressBar;
    private CompositeSubscription compositeSubscription;
    private DashboardActivity dashboardActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoResult, tvCount;
    private int size;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
        init();
        getInboxMessages();
        return view;
    }

    private void init() {
        dashboardActivity = (DashboardActivity) getActivity();
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        inboxMessages = new ArrayList<>();
        tvNoResult = (TextView)view.findViewById(R.id.tv_no_result);
        tvCount = (TextView)view.findViewById(R.id.tv_count_msg);
        recyclerViewInbox = (RecyclerView) view.findViewById(R.id.rv_inbox);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewInbox.setLayoutManager(linearLayoutManager);
        inboxAdapter = new InboxAdapter(getContext(), inboxMessages, this);
        recyclerViewInbox.setAdapter(inboxAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void getInboxMessages() {
        recyclerViewInbox.setVisibility(View.GONE);
        if (!swipeRefreshLayout.isRefreshing())
            showHideProgressBar(View.VISIBLE);
        String profileId = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getInboxMessages(profileId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetInboxMessageResponse>() {
                    @Override
                    public void onCompleted() {
                        showHideProgressBar(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showHideProgressBar(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        dashboardActivity.showSnackBar(getString(R.string.wrong), (LinearLayout) view.findViewById(R.id.parent_view));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GetInboxMessageResponse getInboxMessageResponse) {
                        showHideProgressBar(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerViewInbox.setVisibility(View.VISIBLE);
                        if (Integer.valueOf(getInboxMessageResponse.getCode()) == Constants.SUCCESS_CODE) {
                            ArrayList<Message> messages = getInboxMessageResponse.getInboxResponse().getMessages();
                            size = messages.size();
                            if (messages != null && size != 0)
                            {
                                tvCount.setText("You have "+ size +" conversation(s)");
                                inboxMessages.clear();
                                inboxMessages.addAll(messages);
                                tvNoResult.setVisibility(View.GONE);
                                inboxAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                tvCount.setVisibility(View.GONE);
                                tvNoResult.setVisibility(View.VISIBLE);
                            }


                        } else
                            dashboardActivity.showSnackBar(getString(R.string.wrong), (LinearLayout) view.findViewById(R.id.parent_view));
                    }
                }));
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
    @Override
    public void onStarClicked(int position) {
    }

    @Override
    public void onArchieveClicked(int position) {
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getContext(), ConversationActivity.class);
        intent.putExtra("thread_id", inboxMessages.get(position).getThread_id());
//        intent.putExtra("profile_id", inboxMessages.get(position).getProfile_id());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
    }

    private void showHideProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onRefresh() {
        getInboxMessages();
    }
}
