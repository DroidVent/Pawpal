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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.activities.BaseActivity;
import com.org.pawpal.activities.ConversationActivity;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.adapter.ArchieveAdapter;
import com.org.pawpal.interfaces.OnMessagesListener;
import com.org.pawpal.model.GetArchieveMessageResponse;
import com.org.pawpal.model.Message;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 16-01-2017.
 */

public class ArchiveFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, OnMessagesListener {
    private View view;
    private RecyclerView recyclerViewArchieve;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<Message> messages;
    private ArchieveAdapter archieveAdapter;
    private ProgressBar progressBar;
    private CompositeSubscription compositeSubscription;
    private DashboardActivity dashboardActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private BaseActivity baseActivity;
    private String profileId;

    @Override
    public void onRefresh() {
        getArchieveMessages();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_archieve, container, false);
        init();
        getArchieveMessages();

        return view;
    }

    private void init() {
        baseActivity = (BaseActivity) getActivity();
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        messages = new ArrayList<>();

        recyclerViewArchieve = (RecyclerView) view.findViewById(R.id.rv_archieve);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewArchieve.setLayoutManager(linearLayoutManager);
        archieveAdapter = new ArchieveAdapter(messages, this);
        recyclerViewArchieve.setAdapter(archieveAdapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    private void getArchieveMessages() {
        if (baseActivity.isNetworkAvailable()) {
            recyclerViewArchieve.setVisibility(View.GONE);
            if (!swipeRefreshLayout.isRefreshing())
                progressBar.setVisibility(View.VISIBLE);
            profileId = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID,Constants.GENERAL_PREF_NAME);
            compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getArchieveMessages(profileId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GetArchieveMessageResponse>() {
                        @Override
                        public void onCompleted() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressBar.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                            dashboardActivity.showSnackBar(getString(R.string.wrong), (LinearLayout) view.findViewById(R.id.parent_view));
                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(GetArchieveMessageResponse response) {
                            progressBar.setVisibility(View.GONE);
                            swipeRefreshLayout.setRefreshing(false);
                            recyclerViewArchieve.setVisibility(View.VISIBLE);
                            if (Integer.valueOf(response.getCode()) == Constants.SUCCESS_CODE) {
                                ArrayList<Message> messagesList = response.getArchieveMessageResponse().getMessages();
                                int size = messagesList.size();
                                if (messagesList != null && size != 0) {
                                    messages.clear();
                                    messages.addAll(messagesList);
//                                tvNoResult.setVisibility(View.GONE);
                                    archieveAdapter.notifyDataSetChanged();
                                } else {

//                                tvNoResult.setVisibility(View.VISIBLE);
                                }


                            } else
                                dashboardActivity.showSnackBar(getString(R.string.wrong), (LinearLayout) view.findViewById(R.id.parent_view));
                        }
                    }));

        } else
            baseActivity.showSnackBar(getString(R.string.network_unavailable), (LinearLayout) view.findViewById(R.id.parent_view));

    }

    @Override
    public void onStarClicked(int position) {

    }

    @Override
    public void onArchieveClicked(final int position) {
        progressBar.setVisibility(View.VISIBLE);
        String threadId = messages.get(position).getThread_id();
        int isArchive = messages.get(position).getIs_archive();
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().postArchieveMessage(profileId, threadId, isArchive)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetArchieveMessageResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(GetArchieveMessageResponse archieveMessageResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(archieveMessageResponse.getCode()) == Constants.SUCCESS_CODE) {
                            Toast.makeText(getContext(), archieveMessageResponse.getMessage(), Toast.LENGTH_LONG).show();
                            messages.remove(position);
                            archieveAdapter.notifyDataSetChanged();
                        }
                        else
                            dashboardActivity.showSnackBar(archieveMessageResponse.getMessage(), (RelativeLayout) view.findViewById(R.id.parent_view));

                    }
                }));
    }

    @Override
    public void onItemClicked(int position) {
        Intent intent = new Intent(getContext(), ConversationActivity.class);
        intent.putExtra("thread_id", messages.get(position).getThread_id());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
    }
}
