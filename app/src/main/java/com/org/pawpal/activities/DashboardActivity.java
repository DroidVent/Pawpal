package com.org.pawpal.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.org.pawpal.R;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.fragments.CompleteProfile01;
import com.org.pawpal.fragments.FindPalFragment;
import com.org.pawpal.fragments.HomeFragment;

/**
 * Created by hp-pc on 03-12-2016.
 */

public class DashboardActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private TextView username;
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setUpNavigationView();


    }

    private void setUsername() {

        String name = PrefManager.retrieve(this, PrefManager.PersistenceKey.USER_NAME);
        if (!name.equals("") && name != null)
            username.setText("Hi " + name);
    }

    private void setUpNavigationView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        username = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username);
        setUsername();
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        int id = item.getItemId();

        if (id == R.id.nav_messages) {
            // Handle the camera action
        } else if (id == R.id.nav_find_pal) {
            Fragment homeFragment = new FindPalFragment();
            toolbar.setTitle(getString(R.string.findpal));
            launchFragment(homeFragment, "findpal");
        } else if (id == R.id.nav_home) {
            Fragment homeFragment = new HomeFragment();
            toolbar.setTitle(getString(R.string.dashboard));
            launchFragment(homeFragment, "home");
        } else if (id == R.id.nav_pawfile) {
            Fragment pawfile = new CompleteProfile01();
            toolbar.setTitle(getString(R.string.pawfile));
            launchFragment(pawfile, "pawfile");
        } else if (id == R.id.signout) {
            PrefManager.clear(this);
            launchMainScreen();
        }

        return true;
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
        return false;
    }

    public void showBackNavigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void hideBackNavigation() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toggle.syncState();
    }
    public void showSnack(String msg)
    {
        Snackbar snackbar = Snackbar
                .make(findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

}
