package com.org.pawpal.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.Utils.Utility;
import com.org.pawpal.model.ProfileInfo;
import com.org.pawpal.model.ProfileInfoResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 01-03-2017.
 */

public class ProfileInfoActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName, etNickName, etEmail, etPhone, etCity, etMakaniNum, etAddress, etLat, etLongt;
    private TextView tvCountry;
    private Button btnSave;
    private String name, nickname, email, phone, city, country, makaniNum, address, lat, longt;
    private CompositeSubscription compositeSubscription;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getString(R.string.profile_info));
        setContentView(R.layout.activity_info);
        init();
        if (isNetworkAvailable())
            getProfileInfo();
        else
            showSnackBar(getString(R.string.network_unavailable), (LinearLayout) findViewById(R.id.parent_view));

    }

    private void getProfileInfo() {
        progressBar.setVisibility(View.VISIBLE);
        String profileId = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID, Constants.GENERAL_PREF_NAME);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getProfilInfo(profileId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ProfileInfoResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ProfileInfoResponse response) {
                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(response.getCode()) == Constants.SUCCESS_CODE) {
                            ProfileInfo profileInfo = response.getProfileInfo();
                            setProfileInfoData(profileInfo);
                        } else
                            showSnackBar(response.getMessage(), (LinearLayout) findViewById(R.id.parent_view));

                    }
                }));
    }

    private void setProfileInfoData(ProfileInfo profileInfo) {
        etName.setText(profileInfo.getName());
        etNickName.setText(profileInfo.getMakani_number());
        etEmail.setText(profileInfo.getEmail());
        etPhone.setText(profileInfo.getPhone());
        etCity.setText(profileInfo.getCity());
        tvCountry.setText(profileInfo.getCountry());
        etMakaniNum.setText(profileInfo.getMakani_number());
        etAddress.setText(profileInfo.getAddress());
        etLongt.setText(profileInfo.getLng());
        etLat.setText(profileInfo.getLat());
    }

    private void init() {
        compositeSubscription = new CompositeSubscription();
        etAddress = (EditText) findViewById(R.id.et_address);
        etCity = (EditText) findViewById(R.id.et_city);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        etName = (EditText) findViewById(R.id.et_name);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCity = (EditText) findViewById(R.id.et_city);
        etMakaniNum = (EditText) findViewById(R.id.et_makani_number);
        etAddress = (EditText) findViewById(R.id.et_address);
        etLat = (EditText) findViewById(R.id.et_lat);
        etLongt = (EditText) findViewById(R.id.et_longt);
        tvCountry = (TextView) findViewById(R.id.tv_country);
        btnSave = (Button) findViewById(R.id.save);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnSave.setOnClickListener(this);
        tvCountry.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                saveProfile();
                break;
            case R.id.tv_country:
                setCountrySpinner();
                break;
        }
    }

    private void setCountrySpinner() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                tvCountry.setText(name);
                etMakaniNum.requestFocus();
                picker.dismiss();
            }
        });
    }

    private void saveProfile() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                name = etName.getText().toString();
                nickname = etNickName.getText().toString();
                email = etEmail.getText().toString();
                phone = etPhone.getText().toString();
                city = etCity.getText().toString();
                country = tvCountry.getText().toString();
                makaniNum = etMakaniNum.getText().toString();
                address = etAddress.getText().toString();
                lat = etLat.getText().toString();
                longt = etLongt.getText().toString();
                if (!Utility.isEmptyString(name))
                    if (!Utility.isEmptyString(nickname))
                        if (isEmailValid(email))
                            if (isValidPhone(phone))
                                if (!Utility.isEmptyString(city))
                                    if (!Utility.isEmptyString(country))
                                        if (!Utility.isEmptyString(makaniNum))
                                            if (!Utility.isEmptyString(address))
                                                if (!Utility.isEmptyString(lat))
                                                    if (!Utility.isEmptyString(longt))
                                                        updateProfile();
                                                    else {
                                                        etLongt.setError("Longitude required");
                                                        etLongt.requestFocus();
                                                    }
                                                else {
                                                    etLat.setError("Latitude required");
                                                    etLat.requestFocus();
                                                }
                                            else {
                                                etAddress.setError("Address required");
                                                etAddress.requestFocus();
                                            }
                                        else {
                                            etMakaniNum.setError("Makani Number required");
                                            etMakaniNum.requestFocus();
                                        }

                                    else {
                                        tvCountry.setError("Country required");
                                        tvCountry.requestFocus();
                                    }

                                else {
                                    etCity.setError("City required");
                                    etCity.requestFocus();
                                }
                            else {
                                etPhone.setError("Invalid Phone Number");
                                etPhone.requestFocus();
                            }
                        else {
                            etEmail.setError("Email invalid");
                            etEmail.requestFocus();
                        }
                    else {
                        etNickName.setError("NickName required");
                        etNickName.requestFocus();
                    }

                else {
                    etName.setError("Name required");
                    etName.requestFocus();
                }
            }
        });


    }

    private void updateProfile() {

    }

    private boolean isValidPhone(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean isEmailValid(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }
}
