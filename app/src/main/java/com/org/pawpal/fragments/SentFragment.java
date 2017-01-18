package com.org.pawpal.fragments;

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
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.adapter.SentAdapter;
import com.org.pawpal.model.GetSentMessageResponse;
import com.org.pawpal.model.Message;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 16-01-2017.
 */

public class SentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recyclerViewSent;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Message> messages;
    private SentAdapter sentAdapter;
    private ProgressBar progressBar;
    private CompositeSubscription compositeSubscription;
    private DashboardActivity dashboardActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvNoResult;

    @Override
    public void onRefresh() {
        getSentMessages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sent, container, false);
        init();
        getSentMessages();
        return view;
    }

    private void init() {
        dashboardActivity = (DashboardActivity) getActivity();
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        messages = new ArrayList<>();
        tvNoResult = (TextView)view.findViewById(R.id.tv_no_result);
        recyclerViewSent = (RecyclerView) view.findViewById(R.id.rv_sent);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewSent.setLayoutManager(linearLayoutManager);
        sentAdapter = new SentAdapter(messages, getContext());
        recyclerViewSent.setAdapter(sentAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void getSentMessages() {
        recyclerViewSent.setVisibility(View.GONE);
        if (!swipeRefreshLayout.isRefreshing())
            showHideProgressBar(View.VISIBLE);
        String profileId = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getSentMessages(profileId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetSentMessageResponse>() {
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
                    public void onNext(GetSentMessageResponse response) {
                        showHideProgressBar(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        recyclerViewSent.setVisibility(View.VISIBLE);
                        if (Integer.valueOf(response.getCode()) == Constants.SUCCESS_CODE) {
                            ArrayList<Message> messages = response.getResponse().getMessages();
                            if (messages.size() != 0)
                            {
                                SentFragment.this.messages.clear();
                                tvNoResult.setVisibility(View.GONE);
                                SentFragment.this.messages.addAll(messages);
                                sentAdapter.notifyDataSetChanged();
                            }
                            else
                                tvNoResult.setVisibility(View.VISIBLE);

                        } else
                            dashboardActivity.showSnackBar(getString(R.string.wrong), (LinearLayout) view.findViewById(R.id.parent_view));
                    }
                }));
    }

    private void showHideProgressBar(int visible) {
        progressBar.setVisibility(visible);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
