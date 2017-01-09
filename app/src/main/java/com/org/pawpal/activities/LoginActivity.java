package com.org.pawpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.model.Login;
import com.org.pawpal.model.User;
import com.org.pawpal.model.UserData;
import com.org.pawpal.server.PawPalAPI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private String password;
    private String email;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        hideActionBar();
      /*  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.signin));*/
        init();
    }

    private void init() {
        btnLogin = (Button) findViewById(R.id.btn_login);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                if (isEmailValid(email))

                    if (!TextUtils.isEmpty(password)) {
                        if (isNetworkAvailable())
                            doLogin();

                        else
                            showSnackBar(getString(R.string.network_unavailable), (RelativeLayout) findViewById(R.id.parent_view));

                    } else
                        etPassword.setError(getString(R.string.password_invalid));
                else
                    etEmail.setError(getString(R.string.email_invalid));
            }
        });
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

    private void showHideProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }

    private void doLogin() {
        hideKeyBoard();
        showHideProgressBar(View.VISIBLE);
        PawPalAPI pawPalAPI = MyApplication.getInstance().getPawPalAPI();
        Login login = new Login();
        login.setEmail(email);
        login.setPassword(password);
        Call<User> user = pawPalAPI.loginUser(login);
        user.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                showHideProgressBar(View.GONE);
                if (response != null) {
                    if (response.code() == Constants.SUCCESS_CODE) {

                        showSnackBar("Success", (RelativeLayout) findViewById(R.id.parent_view));
                        saveCreadentials(response.body());
                        launchDashboard();

                    } else if (response.code() == Constants.ERROR_INVALID_MISSING_PARAMETER)
                        showSnackBar("Wrong Usename or Password", (RelativeLayout) findViewById(R.id.parent_view));
                    else
                        showSnackBar(response.message(), (RelativeLayout) findViewById(R.id.parent_view));
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                showHideProgressBar(View.GONE);

            }
        });
    }

    private void launchDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
        finish();
    }

    private void saveCreadentials(User user) {
        UserData data  = user.getUserData();
        PrefManager.store(this, PrefManager.PersistenceKey.USER_ID, data.getUser_id());
        PrefManager.store(this, PrefManager.PersistenceKey.PROFILE_ID, data.getProfile_id());
        PrefManager.store(this, PrefManager.PersistenceKey.USER_NAME, data.getName());
    }

    public boolean isEmailValid(String email) {
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
}
