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

import com.google.gson.Gson;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.activities.BaseActivity;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.activities.FilterActivity;
import com.org.pawpal.activities.PalProfileActivity;
import com.org.pawpal.adapter.FindPalAdapter;
import com.org.pawpal.custom.CustomTextView;
import com.org.pawpal.interfaces.OnItemClickListener;
import com.org.pawpal.model.FilterPal;
import com.org.pawpal.model.SearchPal;
import com.org.pawpal.model.SearchPalResponse;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 22-12-2016.
 */
public class FindPalFragment extends Fragment implements View.OnClickListener, OnItemClickListener {
    private static final int PAGE_LIMIT = 4;
    private View view;
    private TextView tvFilterBy;
    private CompositeSubscription compositeSubscription;
    private BaseActivity baseActivity;
    private DashboardActivity dashboardActivity;
    private RecyclerView recyclerView;
    private FilterPal filterPal;
    private List<SearchPal> palsList;
    private LinearLayoutManager linearLayoutManager;
    private FindPalAdapter findPalAdapter;
    private TextView pawlCount;
    private ProgressBar progressBar;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int countItems;
    private TextView tvNoResult;
    private RelativeLayout rlClearFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.findpal_fragment, container, false);
        init();
        retrieveFilters();
        searchPal(filterPal, 1, PAGE_LIMIT);
        return view;
    }

    private void retrieveFilters() {
        String filters = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.SEARCH_PAL_FILTERS);
        if (!filters.equals("null")) {
            Gson gson = new Gson();
            filterPal = gson.fromJson(filters, FilterPal.class);
        }
    }

    private void init() {
        baseActivity = (BaseActivity) getActivity();
        dashboardActivity = (DashboardActivity) getActivity();
        filterPal = new FilterPal();
        tvFilterBy = (CustomTextView) view.findViewById(R.id.tv_filter_by);
        pawlCount = (CustomTextView) view.findViewById(R.id.tv_pals_found);
        tvNoResult = (TextView)view.findViewById(R.id.tv_no_result);
        tvFilterBy.setOnClickListener(this);
        compositeSubscription = new CompositeSubscription();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_pals);
        palsList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());
        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        rlClearFilter = (RelativeLayout)view.findViewById(R.id.rl_clear_filters);
        rlClearFilter.setOnClickListener(this);
        setAdapter();
    }

    private void setAdapter() {
        findPalAdapter = new FindPalAdapter(palsList, this, getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(findPalAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
      /*          if (dy > 0) {
                    visibleItemCount = linearLayoutManager.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            if (totalItemCount < countItems) {
                                searchPal(filterPal, totalItemCount, PAGE_LIMIT);
                            }
                        }
                    }
                }*/
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_filter_by:
                Intent intent = new Intent(getContext(), FilterActivity.class);
                startActivityForResult(intent, Constants.FILTER_REQUEST);
                getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
            case R.id.rl_clear_filters:
                PrefManager.removeKeyPreference(getContext(), PrefManager.PersistenceKey.SEARCH_PAL_FILTERS);
                filterPal = null;
                filterPal = new FilterPal();
                filterPal.setProfile_id(PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID));
                searchPal(filterPal,1, PAGE_LIMIT);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.FILTER_REQUEST) {
            if (resultCode == -1) {
                filterPal = data.getParcelableExtra("filter");
                filterPal.setProfile_id(PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID));
                searchPal(filterPal, 1, PAGE_LIMIT);
            }
        }

    }

    private void searchPal(FilterPal filterPal,int page, int limit) {
        progressBar.setVisibility(View.VISIBLE);
        if (baseActivity.isNetworkAvailable()) {
            filterPal.setProfile_id(PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID));
            /*if (palsList.size() != 0)
                filterPal.setLast_profile_id(palsList.get(palsList.size()-1).getId());*/
            filterPal.setPage(PAGE_LIMIT);
            compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().searchPal(filterPal).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SearchPalResponse>() {
                        @Override
                        public void onCompleted() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            if (palsList.size() == 0)
                                tvNoResult.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onNext(SearchPalResponse searchPalResponse) {
                            progressBar.setVisibility(View.GONE);
                            loading = true;
                            if (Integer.valueOf(searchPalResponse.getCode()) == Constants.SUCCESS_CODE) {
                                palsList.clear();

                                countItems = Integer.parseInt(searchPalResponse.getPawl_count());
                                pawlCount.setText(searchPalResponse.getPawl_count()+" Pals found");
                                palsList.addAll(searchPalResponse.getSearchPals());
                                findPalAdapter.notifyDataSetChanged();
                            }
                        }
                    }));
        } else
            dashboardActivity.showSnack(getString(R.string.network_unavailable));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    @Override
    public void onClicked(int position) {
        Intent intent = new Intent(getContext(), PalProfileActivity.class);
        intent.putExtra("profile", palsList.get(position));
        startActivity(intent);
    }
}
