package com.org.pawpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.org.pawpal.R;
import com.org.pawpal.adapter.ScreenSlidePagerAdapter;

import me.relex.circleindicator.CircleIndicator;

/**
 * Created by hp-pc on 27-11-2016.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private PagerAdapter mPageAdapter;
    private Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideActionBar();
        init();

    }




    private void init() {
        viewPager = (ViewPager) findViewById(R.id.pager);
        btnSignIn = (Button) findViewById(R.id.signin);
        btnSignUp = (Button) findViewById(R.id.create_account);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        mPageAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mPageAdapter);
        indicator.setViewPager(viewPager);
        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.signin:
                Intent signInIntent = new Intent(this, LoginActivity.class);
                startActivity(signInIntent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
            case R.id.create_account:
                Intent signUp = new Intent(this, SignUpActivity.class);
                startActivity(signUp);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
        }
    }
}
