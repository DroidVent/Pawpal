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
import android.widget.TextView;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.activities.ConversationActivity;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.activities.PalProfileActivity;
import com.org.pawpal.adapter.FavoritesAdapter;
import com.org.pawpal.adapter.LatestConversationAdapter;
import com.org.pawpal.adapter.NewestPalsAdapter;
import com.org.pawpal.custom.CustomTextView;
import com.org.pawpal.interfaces.OnItemClickListener;
import com.org.pawpal.interfaces.OnMessagesListener;
import com.org.pawpal.interfaces.OnPalItemClickListener;
import com.org.pawpal.model.Favorite;
import com.org.pawpal.model.FavoriteResponse;
import com.org.pawpal.model.GetLatestConversationResponse;
import com.org.pawpal.model.Message;
import com.org.pawpal.model.NewestPal;
import com.org.pawpal.model.NewestPalsResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 06-12-2016.
 */

public class HomeFragment extends Fragment implements View.OnClickListener, OnItemClickListener, OnMessagesListener, OnPalItemClickListener {
    private View view;
    private DashboardActivity dashboardActivity;
    private RecyclerView rvFavorite, rvConversations, rvNewestPals;
    private List<Favorite> favorites;
    private ArrayList<Message> messages;
    private ArrayList<NewestPal> newestPals;
    private LinearLayoutManager linearLayoutManagerFav;
    private LinearLayoutManager linearLayoutManagerInbox;
    private LinearLayoutManager linearLayoutManagerPals;
    private LatestConversationAdapter latestConversationAdapter;
    private NewestPalsAdapter newestPalsAdapter;
    private FavoritesAdapter favoritesAdapter;
    private CompositeSubscription compositeSubscription;
    private ProgressBar progressBar;
    private TextView tvConversationCount, tvFavCount, tvNewestPalsCount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        init();
        setNewestPalsAdapter();
        setFavAdapter();
        setInboxAdapter();
        return view;
    }

    private void setFavAdapter() {
        favoritesAdapter = new FavoritesAdapter(favorites, getContext(), this);
        rvFavorite.setLayoutManager(linearLayoutManagerFav);
        rvFavorite.setAdapter(favoritesAdapter);
    }
    private void setNewestPalsAdapter() {
        newestPalsAdapter = new NewestPalsAdapter(newestPals, getContext(), this);
        rvNewestPals.setLayoutManager(linearLayoutManagerPals);
        rvNewestPals.setAdapter(newestPalsAdapter);
    }

    private void setInboxAdapter() {
        latestConversationAdapter = new LatestConversationAdapter(getContext(), messages, this);
        rvConversations.setLayoutManager(linearLayoutManagerInbox);
        rvConversations.setAdapter(latestConversationAdapter);
    }

    private void init() {
        dashboardActivity = (DashboardActivity) getActivity();
        rvFavorite = (RecyclerView) view.findViewById(R.id.rv_fav);
        rvConversations = (RecyclerView) view.findViewById(R.id.rv_conversations);
        rvNewestPals = (RecyclerView) view.findViewById(R.id.rv_newest_pals);
        favorites = new ArrayList<>();
        messages = new ArrayList<>();
        newestPals = new ArrayList<>();
        linearLayoutManagerFav = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerPals = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerInbox = new LinearLayoutManager(getContext());
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        tvConversationCount = (CustomTextView)view.findViewById(R.id.tv_count_msg);
        tvFavCount = (CustomTextView)view.findViewById(R.id.tv_count_fav);
        tvNewestPalsCount = (CustomTextView)view.findViewById(R.id.tv_count_pals);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getNewestPals();
        getFavorites();
        getLatestMessages();
    }

    private void getLatestMessages() {
        rvConversations.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        String profileId = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID,Constants.GENERAL_PREF_NAME);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getLatestConversation(profileId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetLatestConversationResponse>() {
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
                    public void onNext(GetLatestConversationResponse response) {
                        progressBar.setVisibility(View.GONE);
                        rvConversations.setVisibility(View.VISIBLE);
                        if (Integer.valueOf(response.getCode()) == Constants.SUCCESS_CODE) {
                            ArrayList<Message> messagesList = response.getLatestConversationResponse().getMessages();
                            int conversationSize = messagesList.size();
                            if (messagesList != null && conversationSize != 0) {
                                tvConversationCount.setText("You have "+conversationSize+" message(s)");
                                messages.clear();
                                messages.addAll(messagesList);
                                latestConversationAdapter.notifyDataSetChanged();
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
        String profileID = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID,Constants.GENERAL_PREF_NAME);
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
                            tvFavCount.setText("You have "+favoriteResponse.getFavorites().size()+" favorite(s)");
                            favorites.clear();
                            favorites.addAll(favoriteResponse.getFavorites());
                            favoritesAdapter.notifyDataSetChanged();
                        } else
                            dashboardActivity.showSnack(favoriteResponse.getMessage());
                    }
                }));
    }
    private void getNewestPals() {
        String profileID = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID,Constants.GENERAL_PREF_NAME);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getNewestPals(profileID)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewestPalsResponse>() {
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
                    public void onNext(NewestPalsResponse response) {
                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(response.getCode()) == Constants.SUCCESS_CODE) {
                            tvNewestPalsCount.setText("You have "+response.getNewestPal().size()+" new Pals in your area");
                            newestPals.clear();
                            newestPals.addAll(response.getNewestPal());
                            newestPalsAdapter.notifyDataSetChanged();
                        } else
                            dashboardActivity.showSnack(response.getMessage());
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

    @Override
    public void onPalClicked(int position) {
        Intent intent = new Intent(getContext(), PalProfileActivity.class);
        intent.putExtra("other_user_profile", newestPals.get(position).getId());
        startActivity(intent);
    }
}
