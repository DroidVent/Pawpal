package com.org.pawpal.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.adapter.ActivitiesAdapter;
import com.org.pawpal.interfaces.OnItemCheckBoxListener;
import com.org.pawpal.model.FilterPal;
import com.org.pawpal.model.PalActivitiyResponse;
import com.org.pawpal.model.PalActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 26-12-2016.
 */

public class FilterActivity extends BaseActivity implements OnItemCheckBoxListener, View.OnClickListener {
    private RecyclerView gridView;
    private static final int UPDATE_LOC = 1;
    private static final int SAVE_FILTERS = 2;
    private RecyclerView.LayoutManager gridLayoutManager;
    private ActivitiesAdapter activitiesAdapter;
    private List<PalActivity> activities;
    private ProgressBar progressBar;
    private Spinner spinnerPalSize;
    private Spinner spinnerHostPeriod;
    private String petSize;
    private String period;
    private Spinner spinnerGender;
    private Spinner spinnerFrequency;
    private String petGender;
    private String frequency;
    private Button btnSave;
    private EditText etLocation;
    private ArrayList<String> petSelectedActivitiesList;
    private FilterPal filterPal;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        init();
        getPalActivities();
        setPeriodSpinner();
        setPetSizeSpinner();
        setPetGenderSpinner();
        setFrequencySpinner();
        setActivitiesAdapter();

    }

    private void setFrequencySpinner() {
        String[] frequencyArray = getResources().getStringArray(R.array.pet_frequency_array);
        setSpinnerAdapter(spinnerFrequency, frequencyArray);
    }

    private void setPetGenderSpinner() {
        String[] genderArray = getResources().getStringArray(R.array.pet_gender_array);
        setSpinnerAdapter(spinnerGender, genderArray);
    }

    private void init() {
        compositeSubscription = new CompositeSubscription();
        filterPal = new FilterPal();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.filter));
        activities = new ArrayList<>();
        petSelectedActivitiesList = new ArrayList<>();
        etLocation = (EditText)findViewById(R.id.et_location);
        spinnerPalSize = (Spinner) findViewById(R.id.spinner_size);
        btnSave = (Button) findViewById(R.id.btn_search);
        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        spinnerHostPeriod = (Spinner) findViewById(R.id.spinner_host_period);
        spinnerFrequency = (Spinner) findViewById(R.id.spinner_frequency);
        gridLayoutManager = new GridLayoutManager(this, 3);
        gridView = (RecyclerView) findViewById(R.id.rv_activities);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(gridLayoutManager);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnSave.setOnClickListener(this);
    }

    private void setActivitiesAdapter() {
        activitiesAdapter = new ActivitiesAdapter(activities, this);
        gridView.setAdapter(activitiesAdapter);
    }

    private void setPetSizeSpinner() {

        String[] sizesArray = getResources().getStringArray(R.array.petsize_array);
        setSpinnerAdapter(spinnerPalSize, sizesArray);
    }

    private void setSpinnerAdapter(Spinner spinner, final String[] array) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, R.layout.view_user_type_spinner_item, array);
        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                switch (adapterView.getId()) {
                    case R.id.spinner_gender:
                        if (pos != -1 && pos != 0)
                            petGender = array[pos];
                        break;
                    case R.id.spinner_size:
                        if (pos != -1 && pos != 0)
                        {
                            petSize = array[pos];
                            setSizeValue(petSize);
                        }
                        break;
                    case R.id.spinner_host_period:
                        if (pos != -1 && pos != 0)
                            period = array[pos];
                        break;
                    case R.id.spinner_frequency:
                        if (pos != -1 && pos != 0)
                            frequency = array[pos];
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void setSizeValue(String size) {
        switch (size)
        {
            case Constants.SMALL:
                petSize = "SM";
                break;
            case Constants.X_SMALL:
                petSize = "XS";
                break;
            case Constants.Medium:
                petSize = "MD";
                break;
            case Constants.LARGE:
                petSize = "LG";
                break;
            case Constants.EXTRA_LARGE:
                petSize = "XL";
                break;
            case Constants.VERY_SMALL:
                petSize = "VS";
                break;
        }
    }
    private void setPeriodSpinner() {

        String[] periodsArray = getResources().getStringArray(R.array.pet_host_period_array);
        setSpinnerAdapter(spinnerHostPeriod, periodsArray);
    }
    @Override
    public void onItemCheck(int position) {
        petSelectedActivitiesList.add(activities.get(position).getId());
    }

    @Override
    public void onItemUnCheck(int position) {
        petSelectedActivitiesList.remove(activities.get(position));
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
    private void getPalActivities() {
        progressBar.setVisibility(View.VISIBLE);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getActivities()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PalActivitiyResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        showSnackBar(getString(R.string.wrong), (LinearLayout) findViewById(R.id.parent_view));
                    }

                    @Override
                    public void onNext(PalActivitiyResponse activitiyResponse) {
                        progressBar.setVisibility(View.GONE);
                        ((LinearLayout)findViewById(R.id.parent_view)).setVisibility(View.VISIBLE);
                        if (Integer.valueOf(activitiyResponse.getCode()) == Constants.SUCCESS_CODE) {
                            activities.addAll(activitiyResponse.getPalActivities());
                            activitiesAdapter.notifyDataSetChanged();
                        } else
                            showSnackBar(getString(R.string.wrong), (LinearLayout) findViewById(R.id.parent_view));

                    }
                }));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_search:
                new Thread(new Runnable() {
                    public void run() {
                       String address =  etLocation.getText().toString();
                        if (!address.equals(""))
                            getLocationFromAddress(address);
                        else
                            setFilterValues();
                    }
                }).start();

                break;
        }
    }
    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
            Log.e("Location:", p1.latitude + " " + p1.longitude);
            Message msg = handler.obtainMessage();
            msg.what = UPDATE_LOC;
            msg.obj = p1;
            msg.arg1 = 1;
            handler.sendMessage(msg);


        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_LOC) {
                setMapAddress((LatLng) msg.obj);
            }
            else if (msg.what == SAVE_FILTERS)
            {
                Gson gson = new Gson();
                String filters = gson.toJson((FilterPal)msg.obj);
                PrefManager.store(FilterActivity.this, PrefManager.PersistenceKey.SEARCH_PAL_FILTERS, filters);
            }
            super.handleMessage(msg);
        }
    };
    private void setMapAddress(LatLng latLng) {
        filterPal.setLat(String.valueOf(latLng.latitude));
        filterPal.setLng(String.valueOf(latLng.longitude));
        setFilterValues();

    }

    private void setFilterValues() {
        filterPal.setActivities(petSelectedActivitiesList);
        filterPal.setFrequency(frequency);
        filterPal.setHost_period(period);
        filterPal.setPet_size(petSize);
        saveFilters();
        sendResultBack();
    }

    private void saveFilters() {

        Message msg = handler.obtainMessage();
        msg.what = SAVE_FILTERS;
        msg.obj = filterPal;
        msg.arg1 = 2;
        handler.sendMessage(msg);
    }

    private void sendResultBack() {
        Intent intent=new Intent();
        intent.putExtra("filter",filterPal);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
