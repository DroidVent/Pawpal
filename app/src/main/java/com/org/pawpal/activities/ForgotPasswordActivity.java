package com.org.pawpal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.Utility;
import com.org.pawpal.model.ForgotResponse;
import com.org.pawpal.server.ErrorResponse;
import com.org.pawpal.server.PawPalAPI;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp-pc on 10-02-2017.
 */

public class ForgotPasswordActivity extends BaseActivity {
    private EditText etEmail;
    private Button btnSubmit;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.forget_password));
        setContentView(R.layout.activity_forgot_password);
        init();
    }

    private void init() {
        etEmail = (EditText) findViewById(R.id.et_email);
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                hideKeyBoard();
                if (!Utility.isEmptyString(email)) {
                    PawPalAPI pawPalAPI = MyApplication.getInstance().getPawPalAPI();
                    Call<ForgotResponse> responseCall = pawPalAPI.forgotPassword(email);
                    responseCall.enqueue(new Callback<ForgotResponse>() {
                        @Override
                        public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                            progressBar.setVisibility(View.GONE);
                            if (response.code() == Constants.SUCCESS_CODE) {
                                ForgotResponse forgotResponse = response.body();
                                Toast.makeText(ForgotPasswordActivity.this, forgotResponse.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                String errorMsg = null;
                                try {
                                    errorMsg = response.errorBody().string();
                                    Gson gson = new Gson();
                                    ErrorResponse errorResponse = gson.fromJson(errorMsg, ErrorResponse.class);
                                    Toast.makeText(ForgotPasswordActivity.this, errorResponse.getMsg(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<ForgotResponse> call, Throwable t) {
                            progressBar.setVisibility(View.GONE);
                            t.printStackTrace();
                            Toast.makeText(ForgotPasswordActivity.this, R.string.wrong, Toast.LENGTH_LONG).show();
                        }
                    });

                } else
                    Toast.makeText(ForgotPasswordActivity.this, "Email cannot be left empty", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
