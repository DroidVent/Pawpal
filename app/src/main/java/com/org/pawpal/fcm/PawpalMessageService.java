package com.org.pawpal.fcm;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by muhammad.mursaleen on 1/23/2017.
 */

public class PawpalMessageService extends FirebaseMessagingService {
    private static final String TAG = PawpalMessageService.class.getSimpleName();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.e(TAG, "From: " + remoteMessage.getFrom());
    }
}
