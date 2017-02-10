package com.org.pawpal.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.org.pawpal.R;
import com.org.pawpal.Utils.Constants;
import com.org.pawpal.Utils.PrefManager;
import com.org.pawpal.activities.ConversationActivity;
import com.org.pawpal.activities.DashboardActivity;
import com.org.pawpal.model.PushNotification;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by muhammad.mursaleen on 1/23/2017.
 */

public class PawpalMessageService extends FirebaseMessagingService {
    private static final String TAG = PawpalMessageService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "Received");
        String profileId = PrefManager.retrieve(this, PrefManager.PersistenceKey.PROFILE_ID, Constants.GENERAL_PREF_NAME);
        if (profileId != "null")
        {
            Map<String, String> params = remoteMessage.getData();
            PushNotification pushNotification = new PushNotification();
            pushNotification.setMessage(params.get("message"));
            pushNotification.setProfile_id(params.get("profile_id"));
            pushNotification.setThread_id(params.get("thread_id"));
            pushNotification.setTitle(params.get("title"));
            sendNotification(pushNotification);
        }

    }
    private void sendNotification(PushNotification notificationResponse) {
 /*       Bitmap bitmap;
        if (notificationResponse.getUser().getImagePath() != null || !notificationResponse.getUser().getImagePath().equals(""))
            bitmap = getBitmapFromURL(notificationResponse.getUser().getImagePath());
        else
            bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.app_icon);*/
        Intent dashboardintent = new Intent (this, DashboardActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(DashboardActivity.class);
        stackBuilder.addNextIntent(dashboardintent);

        Intent intent = new Intent(this, ConversationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle messageFeedBundle = new Bundle();
        messageFeedBundle.putParcelable(Constants.BUNDLE_NOTIFICATION_DATA, notificationResponse);
        intent.putExtras(messageFeedBundle);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri notificationSound = Uri.parse(SharedPreferencesHelper.retrieve(this, SharedPreferencesHelper.PersistenceKey.NOTIFICATION_TONE));
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/notification");
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo))
                //  .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(notificationResponse.getTitle())
                .setContentText(notificationResponse.getMessage())
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
