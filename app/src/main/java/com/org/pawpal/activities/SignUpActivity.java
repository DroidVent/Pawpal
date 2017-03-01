package com.org.pawpal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.org.pawpal.R;
import com.org.pawpal.fragments.SignUpStep1;

/**
 * Created by hp-pc on 29-11-2016.
 */

public class SignUpActivity extends BaseActivity {
    private static final String TAG_FRAGMENT = SignUpActivity.class.getSimpleName();
    private Bundle bundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        bundle = getIntent().getExtras();
        setActionBarTitle();
        launchSignUpStep1Fragment();
    }

    private void launchSignUpStep1Fragment() {
        SignUpStep1 step1Fragment = new SignUpStep1();
        if (bundle != null)
        {

            step1Fragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, step1Fragment, TAG_FRAGMENT).addToBackStack(null)
                .commit();
    }

    private void setActionBarTitle() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getString(R.string.create_account));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
