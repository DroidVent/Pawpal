package com.org.pawpal.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.org.pawpal.MyApplication;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.Utils.Utility;
import com.org.pawpal.model.PostProfile;
import com.org.pawpal.model.UpdateProfile;
import com.org.pawpal.model.UserImages;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by hp-pc on 20-12-2016.
 */

public class CompleteProfile02Activity extends BaseActivity implements View.OnClickListener {
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    public static final int REQUEST_CODE_SAVE_PIC = 4;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;
    private ImageView ivPhoto1, ivPhoto2, ivPhoto3, ivPhoto4, ivPhoto5;
    private Button btnSave, btnPrevious;
    private String userTask;
    //    private File photoFile;
    private int imageNum = -1;
    private PostProfile postProfile;
    private ArrayList<UserImages> userImages;
    @SerializedName("profileImages")
    private ArrayList<UserImages> userImagesToSend;
    private ProgressBar progressBar;
    private Uri imageUri;
    private Bitmap imageBitmap;
    private String path;

    private CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.complete_profile02);
        getBundle();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.pawfile));
        init();
    }

    private void getBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            postProfile = bundle.getParcelable("profile01");
    }

    private void init() {
        mCompositeSubscription = new CompositeSubscription();
        ivPhoto1 = (ImageView) findViewById(R.id.iv_photo1);
        ivPhoto2 = (ImageView) findViewById(R.id.iv_photo2);
        ivPhoto3 = (ImageView) findViewById(R.id.iv_photo3);
        ivPhoto4 = (ImageView) findViewById(R.id.iv_photo4);
        ivPhoto5 = (ImageView) findViewById(R.id.iv_photo5);
        btnSave = (Button) findViewById(R.id.save);
        btnPrevious = (Button) findViewById(R.id.previous);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        userImages = new ArrayList<>();
        userImagesToSend = new ArrayList<>();
        setData();
        btnSave.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        ivPhoto1.setOnClickListener(this);
        ivPhoto2.setOnClickListener(this);
        ivPhoto3.setOnClickListener(this);
        ivPhoto4.setOnClickListener(this);
        ivPhoto5.setOnClickListener(this);
    }

    private void setData() {
        userImages.addAll(postProfile.getProfileImages());
        switch (userImages.size()) {
            case 0:
                break;
            case 1:
                Picasso.with(this).load(userImages.get(0).getUrl()).fit().centerInside().into(ivPhoto1);
                break;
            case 2:
                Picasso.with(this).load(userImages.get(0).getUrl()).fit().centerInside().into(ivPhoto1);
                Picasso.with(this).load(userImages.get(1).getUrl()).fit().centerCrop().into(ivPhoto2);
                break;
            case 3:
                Picasso.with(this).load(userImages.get(0).getUrl()).fit().centerInside().into(ivPhoto1);
                Picasso.with(this).load(userImages.get(1).getUrl()).fit().centerCrop().into(ivPhoto2);
                Picasso.with(this).load(userImages.get(2).getUrl()).fit().centerCrop().into(ivPhoto3);

                break;
            case 4:
                Picasso.with(this).load(userImages.get(0).getUrl()).fit().centerInside().into(ivPhoto1);
                Picasso.with(this).load(userImages.get(1).getUrl()).fit().centerCrop().into(ivPhoto2);
                Picasso.with(this).load(userImages.get(2).getUrl()).fit().centerCrop().into(ivPhoto3);
                Picasso.with(this).load(userImages.get(3).getUrl()).fit().centerCrop().into(ivPhoto4);
                break;
            case 5:
                Picasso.with(this).load(userImages.get(0).getUrl()).fit().centerInside().into(ivPhoto1);
                Picasso.with(this).load(userImages.get(1).getUrl()).fit().centerCrop().into(ivPhoto2);
                Picasso.with(this).load(userImages.get(2).getUrl()).fit().centerCrop().into(ivPhoto3);
                Picasso.with(this).load(userImages.get(3).getUrl()).fit().centerCrop().into(ivPhoto4);
                Picasso.with(this).load(userImages.get(4).getUrl()).fit().centerCrop().into(ivPhoto5);
                break;
            default:
                Picasso.with(this).load(userImages.get(0).getUrl()).fit().centerInside().into(ivPhoto1);
                Picasso.with(this).load(userImages.get(1).getUrl()).fit().centerCrop().into(ivPhoto2);
                Picasso.with(this).load(userImages.get(2).getUrl()).fit().centerCrop().into(ivPhoto3);
                Picasso.with(this).load(userImages.get(3).getUrl()).fit().centerCrop().into(ivPhoto4);
                Picasso.with(this).load(userImages.get(4).getUrl()).fit().centerCrop().into(ivPhoto5);
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null)
            mCompositeSubscription.unsubscribe();
    }

    private String convertToByte(String path) {
        File imagefile = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_photo1:
                imageNum = 1;
                selectImage();
                break;
            case R.id.iv_photo2:
                imageNum = 2;
                selectImage();
                break;
            case R.id.iv_photo3:
                imageNum = 3;
                selectImage();
                break;
            case R.id.iv_photo4:
                imageNum = 4;
                selectImage();
                break;
            case R.id.iv_photo5:
                imageNum = 5;
                selectImage();
                break;
            case R.id.save:
                saveProfile();
                break;
            case R.id.previous:
                finish();
                break;

        }
    }

    private void saveProfile() {
        progressBar.setVisibility(View.VISIBLE);
        String profileID = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID);
        postProfile.setProfileId(profileID);
        try {
            String json = new Gson().toJson(userImagesToSend);
            postProfile.setUserImages(json);
            mCompositeSubscription.add(MyApplication.getInstance().getPawPalAPI().saveProfile(postProfile)
                    .subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<UpdateProfile>() {
                        @Override
                        public void onCompleted() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressBar.setVisibility(View.GONE);

                            e.printStackTrace();
                            Toast.makeText(CompleteProfile02Activity.this, getString(R.string.wrong), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onNext(UpdateProfile updateProfile) {
                            progressBar.setVisibility(View.GONE);
                            if (Integer.valueOf(updateProfile.getCode()) == Constants.SUCCESS_CODE) {
                                if (updateProfile.getUserData().getImages() != null && updateProfile.getUserData().getImages().size() != 0)
                                    PrefManager.store(CompleteProfile02Activity.this, PrefManager.PersistenceKey.PROFILE_IMAGE, updateProfile.getUserData().getImages().get(0).getUrl());
                                Toast.makeText(CompleteProfile02Activity.this, "Profile has been updated", Toast.LENGTH_LONG).show();
                            } else {

                                Toast.makeText(CompleteProfile02Activity.this, getString(R.string.wrong), Toast.LENGTH_LONG).show();
                            }

                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                userTask = (String) items[item];
                if (items[item].equals("Camera")) {
                    checkForWritePermission();
                } else if (items[item].equals("Gallery")) {
                    checkForGalleryPermission();
                }
            }
        });
        builder.show();
    }

    private void checkForGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, SELECT_FILE);
            else
                galleryIntent();
        else
            galleryIntent();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.text_camera_usage), Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case SELECT_FILE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.text_camera_usage), Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_SAVE_PIC: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checksCameraPermission();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.text_write_usage), Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }

    private void cameraIntent() {
        Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        photoFile = createImageFile();
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture for profile");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Profile Pic From your Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(photoPickerIntent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), imageUri);
            path = Utility.saveBitmap(imageBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (imageNum) {
            case 1:
                setImage(ivPhoto1, path);
                break;
            case 2:
                setImage(ivPhoto2, path);
                break;
            case 3:
                setImage(ivPhoto3, path);
                break;
            case 4:
                setImage(ivPhoto4, path);
                break;
            case 5:
                setImage(ivPhoto5, path);
                break;
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri uri = data.getData();
        String path = Utility.getPath(this, uri);
        switch (imageNum) {
            case 1:
                setImage(path, uri, ivPhoto1);
                break;
            case 2:
                setImage(path, uri, ivPhoto2);
                break;
            case 3:
                setImage(path, uri, ivPhoto3);
                break;
            case 4:
                setImage(path, uri, ivPhoto4);
                break;
            case 5:
                setImage(path, uri, ivPhoto5);
                break;
        }
    }

    private void setImage(ImageView ivPhoto, String path) {

        Picasso.with(this).load(imageUri).fit().centerCrop().into(ivPhoto);
        UserImages pawImage = new UserImages();
        pawImage.setUrl(convertToByte(path));
        userImagesToSend.add(pawImage);

    }

    private void setImage(String path, Uri uri, ImageView ivPhoto) {

        Picasso.with(this).load(uri).fit().centerCrop().into(ivPhoto);
        UserImages pawImage = new UserImages();
        pawImage.setUrl(convertToByte(path));
        userImagesToSend.add(pawImage);

    }

    private void checkForWritePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_SAVE_PIC);
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_SAVE_PIC);
                }
            } else {
                checksCameraPermission();
            }
        } else {
            checksCameraPermission();
        }
    }

    public void checksCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
                }
            } else {
                cameraIntent();
            }
        } else {
            cameraIntent();
        }
    }


    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }
}
