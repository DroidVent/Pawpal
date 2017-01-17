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
import android.widget.ProgressBar;

import com.org.pawpal.R;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.adapter.ArchieveAdapter;
import com.org.pawpal.model.Message;

import java.util.ArrayList;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 16-01-2017.
 */

public class ArchieveFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private View view;
    private RecyclerView recyclerViewArchieve;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Message> messages;
    private ArchieveAdapter archieveAdapter;
    private ProgressBar progressBar;
    private CompositeSubscription compositeSubscription;
    private DashboardActivity dashboardActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public void onRefresh() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_archieve, container, false);
        init();
        return view;
    }

    private void init() {
        dashboardActivity = (DashboardActivity) getActivity();
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        messages = new ArrayList<>();
        Message message = new Message();
        message.setReply("");
        messages.add(message);
        messages.add(message);
        messages.add(message);
        recyclerViewArchieve = (RecyclerView) view.findViewById(R.id.rv_sent);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewArchieve.setLayoutManager(linearLayoutManager);
        archieveAdapter = new ArchieveAdapter(messages);
        recyclerViewArchieve.setAdapter(archieveAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }
}
