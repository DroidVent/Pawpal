package com.org.pawpal.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by muhammad.mursaleen on 1/23/2017.
 */

public class PawpalFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = PawpalFirebaseInstanceIDService.class.getCanonicalName();
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }
}
