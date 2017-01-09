package com.org.pawpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.org.pawpal.R;
import com.org.pawpal.Utils.PrefManager;

/**
 * Created by muhammad.mursaleen on 1/4/2017.
 */

public class LandingActivity extends BaseActivity {
    private Button btnTry;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        hideActionBar();
        init();
        checkIfLoggedIn();
    }
    private void checkIfLoggedIn() {
        String userId = PrefManager.retrieve(this, PrefManager.PersistenceKey.USER_ID);
        if (!userId.equals("null") && !userId.equals(""))
        {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            finish();
        }

    }
    private void init() {
        btnTry = (Button)findViewById(R.id.btn_try);
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
            }
        });
    }

}
