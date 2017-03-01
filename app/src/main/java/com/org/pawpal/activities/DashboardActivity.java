package com.org.pawpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.Utils.Utility;
import com.org.pawpal.fragments.ArchiveFragment;
import com.org.pawpal.fragments.CompleteProfile01;
import com.org.pawpal.fragments.FindPalFragment;
import com.org.pawpal.fragments.HomeFragment;
import com.org.pawpal.fragments.InboxFragment;
import com.org.pawpal.fragments.NavigationDrawerFragment;
import com.org.pawpal.fragments.SentFragment;
import com.org.pawpal.model.RegisterDeviceResponse;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 03-12-2016.
 */

public class DashboardActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String TAG = DashboardActivity.class.getSimpleName();
    Toolbar toolbar;
    /*    private ActionBarDrawerToggle toggle;

        private NavigationView navigationView;*/
    private TextView username;
    private CircleImageView ivPhoto;
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpToolbar();
        compositeSubscription = new CompositeSubscription();
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        username = (TextView) mDrawerLayout.findViewById(R.id.tv_username);
        ivPhoto = (CircleImageView) mDrawerLayout.findViewById(R.id.profile_image);
        setUsername();
        onNavigationDrawerItemSelected(0, -1);
        if (isNetworkAvailable())
            registerDevice();

    }

    private void registerDevice() {
        String profileId = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID, Constants.GENERAL_PREF_NAME);
        String deviceId = getDeviceId();
        String fcmToken = PrefManager.retrieve(this, PrefManager.PersistenceKey.FCM_TOKEN, Constants.FCM_PREF_NAME);
        Log.e(TAG, "Refreshed token: " + fcmToken);
        boolean isEmpty = Utility.isEmptyString(fcmToken);
        if (!isEmpty) {
            compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().registerDevice(profileId, fcmToken, deviceId, "android")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<RegisterDeviceResponse>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            e.printStackTrace();
                        }

                        @Override
                        public void onNext(RegisterDeviceResponse response) {

                            if (Integer.valueOf(response.getCode()) == Constants.SUCCESS_CODE) {
                                Log.e(TAG, "Token sent");

                            }
                        }
                    }));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        String photo = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_IMAGE, Constants.GENERAL_PREF_NAME);
        if (!photo.equals("null") && !photo.equals(""))
            Picasso.with(this).load(photo).fit().centerCrop().placeholder(R.mipmap.img_default).into(ivPhoto);
    }

    private void setUsername() {

        String name = PrefManager.retrieve(this, PrefManager.PersistenceKey.USER_NAME, Constants.GENERAL_PREF_NAME);
        if (!name.equals("") && name != null)
            username.setText("Hi " + name);
    }

    private void setUpToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
        String id = PrefManager.retrieve(this, PrefManager.PersistenceKey.REMEMBER_ME, Constants.GENERAL_PREF_NAME);
        if (id == "null")
            LoginManager.getInstance().logOut();
    }

    public void launchFragment(Fragment fragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_dashboard, fragment, tag)
//                .addToBackStack(null)
                .commit();
    }

    private void launchMainScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
        finish();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                break;
            case R.id.info:
                Intent intentInfo = new Intent(this, ProfileInfoActivity.class);
                startActivity(intentInfo);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;
            case R.id.security:
                Intent intentSecurity = new Intent(this, ChangePassword.class);
                startActivity(intentSecurity);
                overridePendingTransition(R.anim.bottom_up, R.anim.bottom_down);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showBackNavigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideBackNavigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        toggle.syncState();
    }

    public void showSnack(String msg) {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }


    @Override
    public void onNavigationDrawerItemSelected(int groupPosition, int childPosition) {
        if (groupPosition == 0) {
            Fragment homeFragment = new HomeFragment();
            toolbar.setTitle("");
            toolbar.setTitle(getString(R.string.dashboard));
            launchFragment(homeFragment, "home");
        } else if (groupPosition == 1) {
            Fragment homeFragment = new FindPalFragment();
            toolbar.setTitle(getString(R.string.findpal));
            launchFragment(homeFragment, "findpal");
        } else if (groupPosition == 2) {
            toolbar.setTitle(getString(R.string.conversations));
            switch (childPosition) {
                case 0:
                    Fragment inboxFragment = new InboxFragment();
                    launchFragment(inboxFragment, "inbox");
                    break;
                case 1:
                    Fragment sentFragment = new SentFragment();
                    launchFragment(sentFragment, "sent");
                    break;
                case 2:
                    Fragment archieveFragment = new ArchiveFragment();
                    launchFragment(archieveFragment, "archive");
                    break;

            }

        } else if (groupPosition == 3) {
            Fragment pawfile = new CompleteProfile01();
            toolbar.setTitle(getString(R.string.pawfile));
            launchFragment(pawfile, "pawfile");
        } else if (groupPosition == 4) {
            LoginManager.getInstance().logOut();
            PrefManager.clear(this, Constants.GENERAL_PREF_NAME);
            launchMainScreen();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);
        return true;
    }

}
