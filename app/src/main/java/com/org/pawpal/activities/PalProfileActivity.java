package com.org.pawpal.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.Utils.Utility;
import com.org.pawpal.adapter.HorizontalRVAdapter;
import com.org.pawpal.adapter.PalProfileImagesAdapter;
import com.org.pawpal.fragments.SendMessageDialog;
import com.org.pawpal.interfaces.OnItemCheckBoxListener;
import com.org.pawpal.interfaces.OnSendMessageListener;
import com.org.pawpal.model.AddFavoriteResponse;
import com.org.pawpal.model.PostMessage;
import com.org.pawpal.model.Profile;
import com.org.pawpal.model.SearchPal;
import com.org.pawpal.model.SendMessageResponse;
import com.org.pawpal.model.UserImages;
import com.org.pawpal.model.UserProfileData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 01-01-2017.
 */

public class PalProfileActivity extends BaseActivity implements OnItemCheckBoxListener, OnSendMessageListener {
    private RecyclerView recyclerViewImages;
    private RecyclerView recyclerViewActivities;
    private SearchPal searchPal;
    private ImageView ivProfileImageView;
    private TextView tvUserType, tvMemberSince, tvLoginAt, tvLocation, tvSize, tvPeriod, tvName, tvDescription;
    private LinearLayoutManager imagesLayoutManager;
    private List<UserImages> userImages;
    private HorizontalRVAdapter activitiesAdapter;
    private RelativeLayout addFavorite, sendMessage;
    private ProgressBar progressBar;
    private CompositeSubscription compositeSubscription;
    private SendMessageDialog dialogFragment;
    private String otherUserProfileId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pal_profile);
        retrieveBundleData();
        init();
        //If this activity called for user's own profile
        if (searchPal != null) {

            setData();
            setActivitiesAdapter();
            setImagesAdapter();
        }
        //If this activity called for other user's profile
        else {
            getOtherProfileData();
        }


    }

    private void getOtherProfileData() {

        progressBar.setVisibility(View.VISIBLE);

        String profileID = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().getOtherProfile(profileID, otherUserProfileId)
                .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Profile>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }

                    @Override
                    public void onNext(Profile profile) {

                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(profile.getCode()) == Constants.SUCCESS_CODE) {
                            searchPal = new SearchPal();
                            UserProfileData otherUserProfileData = profile.getUserData();
                            if (otherUserProfileData.getActivities() != null)
                                searchPal.setPalActivities(otherUserProfileData.getActivities());
                            searchPal.setId(otherUserProfileData.getProfile_id());
                            searchPal.setLast_login(otherUserProfileData.getLast_login());
                            searchPal.setDistance(otherUserProfileData.getDistance());
                            searchPal.setName(otherUserProfileData.getName());
                            searchPal.setPeriod(otherUserProfileData.getPeriod());
                            searchPal.setPet_size(otherUserProfileData.getPet_size());
                            searchPal.setProfile_created_at(otherUserProfileData.getProfile_created_at());
                            searchPal.setUserImages(otherUserProfileData.getImages());

                            setData();
                            setActivitiesAdapter();
                            setImagesAdapter();
                        } else
                            showSnackBar(profile.getMessage(), (RelativeLayout) findViewById(R.id.parent_view));

                    }
                }));
    }

    private void setActivitiesAdapter() {
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        activitiesAdapter = new HorizontalRVAdapter(searchPal.getPalActivities());
        recyclerViewActivities.setAdapter(activitiesAdapter);
    }

    private void setData() {
        String createdAt = searchPal.getProfile_created_at();
        String LoginAt = searchPal.getLast_login();
        String Location = searchPal.getDistance();
        String Period = searchPal.getPeriod();
        String UserType = searchPal.getProfile_type();
        String size = searchPal.getPet_size();
        String name = searchPal.getName();
        String description = searchPal.getDescription();
        if (!Utility.isEmptyString(createdAt)) {
            if (Utility.isDateValid(createdAt, "yyyy-MM-dd hh:mm:ss"))
                tvMemberSince.setText(Utility.formatDate(createdAt));
            else
                tvMemberSince.setText(createdAt);
        }

        if (!Utility.isEmptyString(LoginAt))
            tvLoginAt.setText(Utility.formatDate(LoginAt));
        if (!Utility.isEmptyString(Location))

            tvLocation.setText(Location + " from you");

        if (!Utility.isEmptyString(Period))
            tvPeriod.setText(Period);
        if (!Utility.isEmptyString(UserType))
            tvUserType.setText(UserType);
        if (!Utility.isEmptyString(size))
            setSizeValue(size);
        if (!Utility.isEmptyString(name))
            tvName.setText(name);
        if (!Utility.isEmptyString(description))
            tvDescription.setText(description);
        if (searchPal.getUserImages().size() != 0)
            Picasso.with(this).load(searchPal.getUserImages().get(0).getUrl()).fit().centerCrop().placeholder(R.mipmap.img_default).into(ivProfileImageView);
    }


    private void setImagesAdapter() {
        PalProfileImagesAdapter palProfileImagesAdapter = new PalProfileImagesAdapter(userImages, this);
        imagesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewImages.setLayoutManager(imagesLayoutManager);

        recyclerViewImages.setAdapter(palProfileImagesAdapter);
    }

    private void retrieveBundleData() {
        Bundle bundle = getIntent().getExtras();
        userImages = new ArrayList<>();
        if (bundle != null) {
            searchPal = (SearchPal) bundle.get("profile");
            otherUserProfileId = bundle.getString("other_user_profile");
            if (searchPal != null)
                userImages.addAll(searchPal.getUserImages());
        }
    }

    private void setSizeValue(String size) {
        switch (size) {
            case "SM":
                tvSize.setText("Small");
                break;
            case "XS":
                tvSize.setText(Constants.X_SMALL);
                break;
            case "MD":
                tvSize.setText(Constants.Medium);
                break;
            case "LG":
                tvSize.setText(Constants.LARGE);
                break;
            case "XL":
                tvSize.setText(Constants.EXTRA_LARGE);
                break;
            case "VS":
                tvSize.setText(Constants.VERY_SMALL);
                break;
        }
    }

    private void init() {

        setToolBarTitle(getString(R.string.findpal));
        recyclerViewImages = (RecyclerView) findViewById(R.id.rv_images);
        recyclerViewActivities = (RecyclerView) findViewById(R.id.rv_activities);
        ivProfileImageView = (CircleImageView) findViewById(R.id.profile_image);
        addFavorite = (RelativeLayout) findViewById(R.id.rl_add_favorites);
        sendMessage = (RelativeLayout) findViewById(R.id.rl_send_msg);
        compositeSubscription = new CompositeSubscription();
        tvMemberSince = (TextView) findViewById(R.id.tv_member_since_txt);
        tvLoginAt = (TextView) findViewById(R.id.tv_login_txt);
        tvName = (TextView) findViewById(R.id.tv_username);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvSize = (TextView) findViewById(R.id.tv_size);
        tvPeriod = (TextView) findViewById(R.id.tv_period);
        tvDescription = (TextView) findViewById(R.id.tv_description);
        tvUserType = (TextView) findViewById(R.id.tv_usertype);
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAsFavorite();
            }
        });
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
    }
    private void setToolBarTitle(String title){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.findpal));
    }

    void showDialog() {
        // Create the fragment and show it as a dialog.
        dialogFragment = SendMessageDialog.newInstance();
        dialogFragment.show(getSupportFragmentManager(), "dialog");
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

    @Override
    public void onItemCheck(int position) {

    }

    @Override
    public void onItemUnCheck(int position) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeSubscription.unsubscribe();
    }

    private void addAsFavorite() {
        progressBar.setVisibility(View.VISIBLE);
        String profileID = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID);
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().addFavorite(profileID, searchPal.getId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddFavoriteResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        progressBar.setVisibility(View.GONE);
                        showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }

                    @Override
                    public void onNext(AddFavoriteResponse addFavoriteResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(addFavoriteResponse.getCode()) == Constants.SUCCESS_CODE) {
                            Toast.makeText(PalProfileActivity.this, addFavoriteResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else
                            showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }
                }));
    }

    @Override
    public void onMessageSend(String message) {
        progressBar.setVisibility(View.VISIBLE);
        PostMessage postMessage = new PostMessage();
        postMessage.setMessage_text(message);
        postMessage.setUser_profile_id(searchPal.getId());
        postMessage.setProfile_id(PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID));
        compositeSubscription.add(MyApplication.getInstance().getPawPalAPI().sendMessage(postMessage)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SendMessageResponse>() {
                    @Override
                    public void onCompleted() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }

                    @Override
                    public void onNext(SendMessageResponse sendMessageResponse) {
                        progressBar.setVisibility(View.GONE);
                        if (Integer.valueOf(sendMessageResponse.getCode()) == Constants.SUCCESS_CODE) {
                            Toast.makeText(PalProfileActivity.this, sendMessageResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else
                            showSnackBar(getString(R.string.wrong), (RelativeLayout) findViewById(R.id.parent_view));
                    }
                }));
    }
}
