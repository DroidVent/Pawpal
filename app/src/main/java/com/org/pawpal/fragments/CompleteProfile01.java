package com.org.pawpal.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.Utils.Utility;
import com.org.pawpal.activities.BaseActivity;
import com.org.pawpal.activities.CompleteProfile02Activity;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.adapter.ActivitiesAdapter;
import com.org.pawpal.interfaces.OnItemCheckBoxListener;
import com.org.pawpal.model.PalActivitiyResponse;
import com.org.pawpal.model.PalActivity;
import com.org.pawpal.model.PostProfile;
import com.org.pawpal.model.Profile;
import com.org.pawpal.model.UserImages;
import com.org.pawpal.model.UserProfileData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 12-12-2016.
 */

public class CompleteProfile01 extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, OnItemCheckBoxListener {
    private View view;
    private DashboardActivity dashboardActivity;
    private TextView tvDOB;
    private EditText etDesc;
    private Spinner spinnerSize, spinnerGender, spinnerHostPeriod, spinnerFrequency;
    private BaseActivity baseActivity;
    private String petSize = "", petName = "", petGender = "", petDob = "", period = "", frequency = "", description = "";
    private CompositeSubscription mCompositeSubscription;
    private ProgressBar progressBar;
    private RecyclerView gridView;
    private RecyclerView.LayoutManager gridLayoutManager;
    private List<PalActivity> activities;
    private ActivitiesAdapter activitiesAdapter;
    private List<UserImages> userImages;
    private List<PalActivity> fetchedActivities;
    private HashMap<String, PalActivity> selectedActivities;
    private Button btnContinue;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.complete_profile01, container, false);
            init();
            getPalActivities();
            setActivitiesAdapter();

            getProfileData();
        }

        return view;
    }

    private void getPalActivities() {
        progressBar.setVisibility(View.VISIBLE);

        mCompositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getActivities()
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<PalActivitiyResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        ((DashboardActivity) getActivity()).showSnack(getString(R.string.wrong));
                    }

                    @Override
                    public void onNext(PalActivitiyResponse activitiyResponse) {
                        progressBar.setVisibility(View.GONE);
                        btnContinue.setEnabled(true);
                        if (Integer.valueOf(activitiyResponse.getCode()) == Constants.SUCCESS_CODE) {
                            activities.addAll(activitiyResponse.getPalActivities());
                            if (fetchedActivities != null)
                                setSelectedActivities(fetchedActivities);
                            else
                                activitiesAdapter.notifyDataSetChanged();
                            Log.e("Activities ", "called");
                        } else
                            ((DashboardActivity) getActivity()).showSnack(getString(R.string.wrong));

                    }
                }));
    }

    private void getProfileData() {

        progressBar.setVisibility(View.VISIBLE);

        String profileID = PrefManager.retrieve(getContext(), PrefManager.PersistenceKey.PROFILE_ID);
        mCompositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getProfile(profileID)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Profile>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        baseActivity.showSnackBar(getString(R.string.wrong), (LinearLayout) view.findViewById(R.id.parent_view));
                    }

                    @Override
                    public void onNext(Profile profile) {
                        btnContinue.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Log.e("Profile ", "called");
                        if (Integer.valueOf(profile.getCode()) == Constants.SUCCESS_CODE) {
                            setProfileData(profile);
                        } else
                            baseActivity.showSnackBar(profile.getMessage(), (LinearLayout) view.findViewById(R.id.parent_view));

                    }
                }));
    }

    private void setProfileData(Profile profileData) {
        UserProfileData userProfileData = profileData.getUserData();
        fetchedActivities = new ArrayList<PalActivity>();
        tvDOB.setText(userProfileData.getPet_dob());
        etDesc.setText(userProfileData.getDescription());
        setSizeSpinnerValues(userProfileData.getPet_size());
        setSpinnerSavedValues(userProfileData.getPet_gender(), spinnerGender);
        setSpinnerSavedValues(userProfileData.getPeriod(), spinnerHostPeriod);
        setSpinnerSavedValues(userProfileData.getFrequency(), spinnerFrequency);
        userImages.addAll(profileData.getUserData().getImages());
        fetchedActivities.addAll(userProfileData.getActivities());

        setSelectedActivities(fetchedActivities);
    }

    private void setSizeSpinnerValues(String pet_size) {
        int pos = 0;
        if (!pet_size.equals("")) {
            ArrayAdapter sizeAdapter = (ArrayAdapter) spinnerSize.getAdapter();
            switch (pet_size) {
                case "SM":
                    pos = sizeAdapter.getPosition(Constants.SMALL);
                    break;
                case "XS":
                    pos = sizeAdapter.getPosition(Constants.X_SMALL);
                    break;
                case "MD":
                    pos = sizeAdapter.getPosition(Constants.Medium);
                    break;
                case "LG":
                    pos = sizeAdapter.getPosition(Constants.LARGE);
                    break;
                case "XL":
                    pos = sizeAdapter.getPosition(Constants.EXTRA_LARGE);
                    break;
                case "VS":
                    pos = sizeAdapter.getPosition(Constants.VERY_SMALL);
                    break;
            }

            spinnerSize.setSelection(pos);
        }
    }

    private void setSelectedActivities(List<PalActivity> petSelectedActivitiesList) {
        int len = petSelectedActivitiesList.size();
        if (len != 0) {
            for (int i = 0; i < len; i++) {
                for (int j = 0; j < activities.size(); j++) {
                    if (activities.get(j).getName().equalsIgnoreCase(petSelectedActivitiesList.get(i).getName())) {
                        activities.get(j).setChecked(true);
                        selectedActivities.put(petSelectedActivitiesList.get(i).getName(), petSelectedActivitiesList.get(i));
                        activitiesAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }

    }


    private void setSpinnerSavedValues(String savedValues, Spinner spinner) {
        if (!savedValues.equals("")) {
            ArrayAdapter sizeAdapter = (ArrayAdapter) spinner.getAdapter();
            savedValues = String.valueOf(savedValues.charAt(0)).toUpperCase() + savedValues.substring(1, savedValues.length());
            int pos = sizeAdapter.getPosition(savedValues);
            spinner.setSelection(pos);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();

        if (view != null)
            view = null;


    }

    private void init() {
        mCompositeSubscription = new CompositeSubscription();
        dashboardActivity = (DashboardActivity) getActivity();
        baseActivity = (BaseActivity) dashboardActivity;
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        tvDOB = (TextView) view.findViewById(R.id.tv_dob);
        activities = new ArrayList<PalActivity>();
        selectedActivities = new LinkedHashMap<>();

        userImages = new ArrayList<UserImages>();
        gridView = (RecyclerView) view.findViewById(R.id.grid_activities);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(gridLayoutManager);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        etDesc = (EditText) view.findViewById(R.id.et_desc);
        spinnerSize = (Spinner) view.findViewById(R.id.spinner_size);
        spinnerGender = (Spinner) view.findViewById(R.id.spinner_gender);
        spinnerHostPeriod = (Spinner) view.findViewById(R.id.spinner_host_period);
        spinnerFrequency = (Spinner) view.findViewById(R.id.spinner_frequency);
        tvDOB.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnContinue.setEnabled(false);
        setPetSizeSpinner();
        setPetGenderSpinner();
        setPeriodSpinner();
        setFrequencySpinner();
    }

    private void setActivitiesAdapter() {
        activitiesAdapter = new ActivitiesAdapter(activities, this);
        gridView.setAdapter(activitiesAdapter);
    }

    private void setPetGenderSpinner() {
        String[] genderArray = getResources().getStringArray(R.array.pet_gender_array);
        setSpinnerAdapter(spinnerGender, genderArray);
    }


    private void setPetSizeSpinner() {

        String[] sizesArray = getResources().getStringArray(R.array.petsize_array);
        setSpinnerAdapter(spinnerSize, sizesArray);
    }

    private void setPeriodSpinner() {

        String[] periodsArray = getResources().getStringArray(R.array.pet_host_period_array);
        setSpinnerAdapter(spinnerHostPeriod, periodsArray);
    }

    private void setFrequencySpinner() {

        String[] frequencyArray = getResources().getStringArray(R.array.pet_frequency_array);
        setSpinnerAdapter(spinnerFrequency, frequencyArray);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_pawfile, menu);
    }


    private PostProfile prepareProfile01Data() {
        PostProfile postProfile = new PostProfile();
        postProfile.setpet_dob(tvDOB.getText().toString());
        postProfile.setDescription(etDesc.getText().toString());
        postProfile.setFrequency(frequency);
        postProfile.setPeriod(period);
        postProfile.setpet_gender(petGender);
        postProfile.setPetSize(petSize);
        postProfile.setUserImages(userImages);
        List<PalActivity> valueList = Collections.list(Collections.enumeration(selectedActivities.values()));
        String activitiesList = new Gson().toJson(valueList);
        postProfile.setactivities(activitiesList);
        return postProfile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dob:
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setOnDateSetListener(this);
                datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.btn_continue:
                PostProfile postProfile = prepareProfile01Data();
                if (!Utility.isEmptyString(postProfile.getpet_dob()))
                    if (!Utility.isEmptyString(postProfile.getDescription()))
                        if (!Utility.isEmptyString(postProfile.getPetSize()))
                            if (!Utility.isEmptyString(postProfile.getpet_gender()))
                                if (!Utility.isEmptyString(postProfile.getPeriod()))
                                    if (!Utility.isEmptyString(postProfile.getFrequency()))
                                    {

                                        Intent signInIntent = new Intent(getContext(), CompleteProfile02Activity.class);
                                        signInIntent.putExtra("profile01", postProfile);
                                        startActivity(signInIntent);
                                        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                                    }
                                    else
                                        Toast.makeText(getContext(), "Please select frequency", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(getContext(), "Please select period", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getContext(), "Please select gender", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getContext(), "Please select Pet size", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getContext(), "Please enter description", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getContext(), "Please select Pet's date of birth", Toast.LENGTH_LONG).show();

                break;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear,
                          int dayOfMonth) {
        monthOfYear++;
        tvDOB.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
    }

    private void setSpinnerAdapter(Spinner spinner, final String[] array) {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getContext(), R.layout.view_user_type_spinner_item, array);
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
                        if (pos != -1 && pos != 0) {
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
        switch (size) {
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


    @Override
    public void onItemCheck(int position) {
        selectedActivities.put(activities.get(position).getName(), activities.get(position));
    }

    @Override
    public void onItemUnCheck(int position) {
        selectedActivities.remove(activities.get(position).getName());
    }
}
