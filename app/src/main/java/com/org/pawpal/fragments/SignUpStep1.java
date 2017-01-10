package com.org.pawpal.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.org.pawpal.R;
import com.org.pawpal.activities.BaseActivity;
import com.org.pawpal.activities.SignUpStep1Address;
import com.org.pawpal.custom.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hp-pc on 28-11-2016.
 */

public class SignUpStep1 extends Fragment {
    private static final String TAG_FRAGMENT = SignUpStep1.class.getSimpleName();
    private Spinner spinnerType;
    private CustomTextView tvCountry;
    private EditText etName, etNickName, etEmail, etPhone, etCity, etMakani, etPassword, etConfirmPassword;
    private View rootView;
    private Button btnContinue;
    private CheckBox cbAge;
    private String userType, name, nickname, email, phone, city, country, makaniNum, password, confirmPassword;
    private BaseActivity baseActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.signup_step1, container, false);
            init();
        }
        return rootView;
    }

    private void init() {
        baseActivity = (BaseActivity) getActivity();
        spinnerType = (Spinner) rootView.findViewById(R.id.spinner_type);
        tvCountry = (CustomTextView) rootView.findViewById(R.id.tv_country);
        btnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        etName = (EditText) rootView.findViewById(R.id.et_name);
        etNickName = (EditText) rootView.findViewById(R.id.et_nickname);
        etEmail = (EditText) rootView.findViewById(R.id.et_email);
        etPhone = (EditText) rootView.findViewById(R.id.et_phone);
        etCity = (EditText) rootView.findViewById(R.id.et_city);
        etMakani = (EditText) rootView.findViewById(R.id.et_makani_number);
        etPassword = (EditText) rootView.findViewById(R.id.et_password);
        etConfirmPassword = (EditText) rootView.findViewById(R.id.et_confirm_password);
        cbAge = (CheckBox) rootView.findViewById(R.id.cb_age);

        setUserTypeSpinner();

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        name = etName.getText().toString();
                        nickname = etNickName.getText().toString();
                        email = etEmail.getText().toString();
                        phone = etPhone.getText().toString();
                        city = etCity.getText().toString();
                        country = tvCountry.getText().toString();
                        makaniNum = etMakani.getText().toString();
                        password = etPassword.getText().toString();
                        confirmPassword = etConfirmPassword.getText().toString();
                        if (!checkIfEmpty(userType))
                            if (!checkIfEmpty(name))
                                if (!checkIfEmpty(nickname))
                                    if (isEmailValid(email))
                                        if (isValidPhone(phone))
                                            if (!checkIfEmpty(city))
                                                if (!checkIfEmpty(country))
                                                    if (!checkIfEmpty(password))
                                                        if (checkIfPasswordMatch(confirmPassword))
                                                            if (cbAge.isChecked())

                                                                launchSignUpStep1Address();
                                                            else
                                                                Toast.makeText(getContext(), getString(R.string.age_limit_msg), Toast.LENGTH_LONG).show();
                                                        else {
                                                            etConfirmPassword.setError("Password mismatch");
                                                            etConfirmPassword.requestFocus();
                                                        }
                                                    else {
                                                        etPassword.setError("Password cannot be left empty");
                                                        etPassword.requestFocus();
                                                    }

                                                else {
                                                    tvCountry.setError("Country cannot be left empty");
                                                    tvCountry.requestFocus();
                                                }

                                            else {
                                                etCity.setError("City cannot be left empty");
                                                etCity.requestFocus();
                                            }

                                        else {
                                            etPhone.setError("Invalid Phone  Number");
                                            etPhone.requestFocus();
                                        }
                                    else {
                                        etEmail.setError("Email invalid");
                                        etEmail.requestFocus();
                                    }

                                else {
                                    etNickName.setError("NickName cannot be left empty");
                                    etNickName.requestFocus();
                                }

                            else {
                                etName.setError("Name cannot be left empty");
                                etName.requestFocus();
                            }

                        else {
                            spinnerType.requestFocus();
                            Toast.makeText(getContext(), "Please select user type", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });
        tvCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCountrySpinner();
            }
        });
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

    private boolean checkIfPasswordMatch(String confirmPassword) {
        if (password.equals(confirmPassword))
            return true;
        return false;
    }

    private boolean checkIfEmpty(String text) {
        if (TextUtils.isEmpty(text) || text.equals(""))
            return true;
        return false;
    }

    private void launchSignUpStep1Address() {
        baseActivity.hideKeyBoard();
        Intent mIntent = new Intent(getContext(), SignUpStep1Address.class);
        mIntent.putExtra("registration", setBundle());
        startActivity(mIntent);
        getActivity().overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
        getActivity().finish();
    }

    private Bundle setBundle() {
        Bundle bundle = new Bundle();
        bundle.putString("user_type", userType);
        bundle.putString("name", name);
        bundle.putString("nickname", nickname);
        bundle.putString("email", email);
        bundle.putString("phone", phone);
        bundle.putString("city", city);
        bundle.putString("country", country);
        bundle.putString("makani", makaniNum);
        bundle.putString("password", password);
        return bundle;
    }

    private void setCountrySpinner() {
        final CountryPicker picker = CountryPicker.newInstance("Select Country");
        picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                tvCountry.setText(name);
                etMakani.requestFocus();
                picker.dismiss();
            }
        });

    }

    private void setUserTypeSpinner() {
        final List<String> list = new ArrayList<String>();
        list.add("Please select a type");
        list.add("Host");
        list.add("Owner");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getContext(), R.layout.view_user_type_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(dataAdapter);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos != 0)
                    userType = list.get(pos);
                else
                    userType = "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}
