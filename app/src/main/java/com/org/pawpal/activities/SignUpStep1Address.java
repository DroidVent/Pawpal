package com.org.pawpal.activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.custom.CustomTextView;
import com.org.pawpal.model.Register;
import com.org.pawpal.model.User;
import com.org.pawpal.server.PawPalAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.org.pawpal.R.id.map;

/**
 * Created by hp-pc on 30-11-2016.
 */

public class SignUpStep1Address extends BaseActivity implements OnMapReadyCallback, View.OnClickListener {
    private static final int UPDATE_LOC = 1;
    private static final int ERROR = 0;
    private static final String TAG = SignUpStep1Address.class.getSimpleName();
    private EditText etLat, etLongt, etAddress;
    private Button btnContinue, btnSearch, btnContinueToLogin;
    private GoogleMap mMap;
    private String userType, name, nickname, email, phone, city, country, makaniNum, password, address = "0.0", lat = "0.0", longt;
    private CheckBox cbTerms;
    private ProgressBar progressBar;
    private boolean success;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_step1_location);
        getBundleData();
        init();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getBundleExtra("registration");
        email = bundle.getString("email");
        userType = bundle.getString("user_type");
        name = bundle.getString("name");
        nickname = bundle.getString("nickname");
        phone = bundle.getString("phone");
        city = bundle.getString("city");
        country = bundle.getString("country");
        makaniNum = bundle.getString("makani");
        password = bundle.getString("password");
    }


    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        etLat = (EditText) findViewById(R.id.et_lat);
        etLongt = (EditText) findViewById(R.id.et_longt);
        etAddress = (EditText) findViewById(R.id.et_address);
        btnSearch = (Button) findViewById(R.id.search);
        btnContinue = (Button) findViewById(R.id.bt_continue);
        btnContinueToLogin = (Button) findViewById(R.id.btn_continue);
        cbTerms = (CheckBox) findViewById(R.id.cb_terms);
        btnSearch.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
        btnContinueToLogin.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float maxZoom = 17.0f;
        LatLng latLng = new LatLng(25.2048, 55.2708);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, maxZoom);
        mMap.animateCamera(cameraUpdate);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Dubai"));
        lat = String.valueOf(latLng.latitude);
        longt = String.valueOf(latLng.longitude);
        etLat.setText(lat);
        etLongt.setText(longt);

    }
    private void getCurrentLocation()
    {

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
            Message msg = handler.obtainMessage();
            msg.what = ERROR;
            msg.obj = p1;
            msg.arg1 = 1;
            handler.sendMessage(msg);
        }

        return p1;
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_LOC) {
                setMapAddress((LatLng) msg.obj);
            }else if (msg.what == ERROR)
                progressBar.setVisibility(View.GONE);
            super.handleMessage(msg);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search:
                hideKeyBoard();
                searchAddress();
                break;
            case R.id.bt_continue:
                continueToSecondStep();
                break;
            case R.id.btn_continue:
                Intent loginIntent = new Intent(SignUpStep1Address.this, LoginActivity.class);
                startActivity(loginIntent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                finish();
                break;

        }
    }

    private void searchAddress() {
        progressBar.setVisibility(View.VISIBLE);
        final LatLng[] latLng = new LatLng[1];
        address = etAddress.getText().toString();
        new Thread(new Runnable() {
            public void run() {
                latLng[0] = getLocationFromAddress(address);
            }
        }).start();
    }

    private void setMapAddress(LatLng latLng) {
        progressBar.setVisibility(View.GONE);
        hideKeyBoard();
        updateCamera(latLng);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                updateCamera(latLng);
            }
        });
    }

    private void updateCamera(LatLng latLng) {
        float maxZoom = 17.0f;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, maxZoom);
        mMap.animateCamera(cameraUpdate);
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(address));
        lat = String.valueOf(latLng.latitude);
        longt = String.valueOf(latLng.longitude);
        etLat.setText(lat);
        etLongt.setText(longt);

    }

    private void continueToSecondStep() {
        address = etAddress.getText().toString();
        if (!address.equals(""))
            if (cbTerms.isChecked())
                doRegister();
            /*{
                Intent mIntent = new Intent(this, SignUpStep2.class);
                startActivity(mIntent);
            }*/
            else
                showSnackBar("Please accept terms", (RelativeLayout) findViewById(R.id.parent_view));
        else {
            etAddress.setError("Please enter address");
            etAddress.requestFocus();
        }
    }

    private void showHideProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }

    private void doRegister() {
        hideKeyBoard();
        showHideProgressBar(View.VISIBLE);
        PawPalAPI pawPalAPI = MyApplication.getInstance().getPawPalAPI();
        Register register = new Register(userType, name, nickname, email, phone, city, country, makaniNum, password, address, lat, longt);

        Call<User> user = pawPalAPI.registerUser(register);
        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                showHideProgressBar(View.GONE);
                if (response != null) {
                    if (response.code() == Constants.SUCCESS_CODE) {

                        success = true;
                        ((LinearLayout) findViewById(R.id.step1_view)).setVisibility(View.GONE);
                        String emailMsg = getString(R.string.email_sent, email);
                        ((CustomTextView) findViewById(R.id.email_msg)).setText(emailMsg);
                        ((LinearLayout) findViewById(R.id.ll_step2_view)).setVisibility(View.VISIBLE);
                    } else
                        showSnackBar(response.message(), (RelativeLayout) findViewById(R.id.parent_view));
                    Log.e("SignUp: ","Response: "+response.code()+
                            " usertype:"+userType+" name:"+name+" nickname:"+nickname+" email:"+email+" phone:"+phone+" city:"+city+" country:"+country+" makani:"+makaniNum+" Password:"+password);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showSnackBar(getString(R.string.wrong), (LinearLayout) findViewById(R.id.parent_view));
                showHideProgressBar(View.GONE);

            }
        });
    }
}
