package com.org.pawpal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.org.pawpal.R;

/**
 * Created by hp-pc on 01-03-2017.
 */

public class ChangePassword extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        getSupportActionBar().setTitle(getString(R.string.change_password));
    }
}
