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
import android.widget.Toast;

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
    private ArrayList<Message> messages;
    private InboxAdapter inboxAdapter;
    private ProgressBar progressBar;
    private CompositeSubscription compositeSubscription;
    private DashboardActivity dashboardActivity;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
        init();
        return view;
    }

    private void init() {
        dashboardActivity = (DashboardActivity) getActivity();
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh);
        messages = new ArrayList<>();
        Message message = new Message();
        message.setReply("");
        messages.add(message);
        messages.add(message);
        messages.add(message);
        recyclerViewInbox = (RecyclerView) view.findViewById(R.id.rv_inbox);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewInbox.setLayoutManager(linearLayoutManager);
        inboxAdapter = new InboxAdapter(messages, this);
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
                            messages.clear();
                            messages.addAll(getInboxMessageResponse.getMessages());
                            inboxAdapter.notifyDataSetChanged();
                        } else
                            dashboardActivity.showSnackBar(getString(R.string.wrong), (LinearLayout) view.findViewById(R.id.parent_view));
                    }
                }));
    }

    @Override
    public void onStarClicked(int position) {
        Toast.makeText(getContext(), "star", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArchieveClicked(int position) {
        Toast.makeText(getContext(), "archieve", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getContext(), ConversationActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
    }

    private void showHideProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
    }
}
