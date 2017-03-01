package com.org.pawpal.activities;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.facebook.login.LoginManager;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;

/**
 * Created by hp-pc on 25-11-2016.
 */

public class BaseActivity extends AppCompatActivity {
    protected void showDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(title);
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // positive button logic
                    }
                });


        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showSnackBar(String message, View view) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
    protected void hideActionBar() {
        getSupportActionBar().hide();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

    }
    protected String getDeviceId()
    {
        return Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String id = PrefManager.retrieve(this, PrefManager.PersistenceKey.REMEMBER_ME, Constants.GENERAL_PREF_NAME);
        if (id == "null")
            LoginManager.getInstance().logOut();
    }
}
