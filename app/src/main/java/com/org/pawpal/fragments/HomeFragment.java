package com.org.pawpal.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.adapter.FavoritesAdapter;
import com.org.pawpal.model.Favorite;
import com.org.pawpal.model.FavoriteResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 06-12-2016.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;
    private DashboardActivity dashboardActivity;
    private RecyclerView rvFavorite;
    private List<Favorite>  favorites;
    private LinearLayoutManager linearLayoutManagerFav;
    FavoritesAdapter favoritesAdapter;
    private CompositeSubscription  compositeSubscription;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        init();
        setFavAdapter();
        return view;
    }

    private void setFavAdapter() {
        favoritesAdapter = new FavoritesAdapter(favorites, getContext());
        rvFavorite.setLayoutManager(linearLayoutManagerFav);
        rvFavorite.setAdapter(favoritesAdapter);
    }

    private void init() {
        dashboardActivity = (DashboardActivity)getActivity();
        rvFavorite = (RecyclerView)view.findViewById(R.id.rv_fav);
        favorites = new ArrayList<>();
        linearLayoutManagerFav = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        compositeSubscription = new CompositeSubscription();
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
    private void getFavorites()
    {
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
                        }
                        else
                            dashboardActivity.showSnack(favoriteResponse.getMessage());
                    }
                }));
    }
}
