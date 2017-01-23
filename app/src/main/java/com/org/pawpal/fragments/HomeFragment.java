package com.org.pawpal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.activities.ConversationActivity;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.activities.PalProfileActivity;
import com.org.pawpal.adapter.FavoritesAdapter;
import com.org.pawpal.adapter.InboxAdapter;
import com.org.pawpal.interfaces.OnInboxListener;
import com.org.pawpal.interfaces.OnItemClickListener;
import com.org.pawpal.model.Favorite;
import com.org.pawpal.model.FavoriteResponse;
import com.org.pawpal.model.GetInboxMessageResponse;
import com.org.pawpal.model.Message;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 06-12-2016.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnItemClickListener, OnInboxListener {
    private View view;
    private DashboardActivity dashboardActivity;
    private RecyclerView rvFavorite, rvConversations;
    private List<Favorite> favorites;
    private ArrayList<Message> messages;
    private LinearLayoutManager linearLayoutManagerFav;
    private LinearLayoutManager linearLayoutManagerInbox;
    private InboxAdapter inboxAdapter;
    private FavoritesAdapter favoritesAdapter;
    private CompositeSubscription compositeSubscription;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        init();
        getInboxMessages();
        setFavAdapter();
        setInboxAdapter();
        return view;
    }

    private void setFavAdapter() {
        favoritesAdapter = new FavoritesAdapter(favorites, getContext(), this);
        rvFavorite.setLayoutManager(linearLayoutManagerFav);
        rvFavorite.setAdapter(favoritesAdapter);
    }

    private void setInboxAdapter() {
        inboxAdapter = new InboxAdapter(getContext(), messages, this);
        rvConversations.setLayoutManager(linearLayoutManagerInbox);
        rvConversations.setAdapter(inboxAdapter);
    }

    private void init() {
        dashboardActivity = (DashboardActivity) getActivity();
        rvFavorite = (RecyclerView) view.findViewById(R.id.rv_fav);
        rvConversations = (RecyclerView) view.findViewById(R.id.rv_conversations);
        favorites = new ArrayList<>();
        messages = new ArrayList<>();
        linearLayoutManagerFav = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerInbox = new LinearLayoutManager(getContext());
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFavorites();
    }

    private void getInboxMessages() {
        rvConversations.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String profileId = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getInboxMessages(profileId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetInboxMessageResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        dashboardActivity.showSnackBar(getString(R.string.wrong), (RelativeLayout) view.findViewById(R.id.parent_view));
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GetInboxMessageResponse getInboxMessageResponse) {
                        progressBar.setVisibility(View.GONE);
                        rvConversations.setVisibility(View.VISIBLE);
                        if (Integer.valueOf(getInboxMessageResponse.getCode()) == Constants.SUCCESS_CODE) {
                            ArrayList<Message> messagesList = getInboxMessageResponse.getInboxResponse().getMessages();
                            if (messagesList != null && messagesList.size() != 0) {
                                messages.clear();
                                messages.add(messagesList.get(0));
                                messages.add(messagesList.get(1));
                                inboxAdapter.notifyDataSetChanged();
                            }


                        } else
                            dashboardActivity.showSnackBar(getString(R.string.wrong), (RelativeLayout) view.findViewById(R.id.parent_view));
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    private void getFavorites() {
        progressBar.setVisibility(View.VISIBLE);
        String profileID = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getFavorites(profileID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FavoriteResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        dashboardActivity.showSnack(getString(R.string.wrong));
                    }

                    @Override
                    public void onNext(FavoriteResponse favoriteResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(favoriteResponse.getCode()) == Constants.SUCCESS_CODE) {
                            favorites.clear();
                            favorites.addAll(favoriteResponse.getFavorites());
                            favoritesAdapter.notifyDataSetChanged();
                        } else
                            dashboardActivity.showSnack(favoriteResponse.getMessage());
                    }
                }));
    }

    @Override
    public void onClicked(int position) {
        Intent intent = new Intent(getContext(), PalProfileActivity.class);
        intent.putExtra("other_user_profile", favorites.get(position).getId());
        startActivity(intent);
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
        intent.putExtra("thread_id", messages.get(position).getThread_id());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
    }
}
