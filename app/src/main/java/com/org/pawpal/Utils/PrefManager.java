package com.org.pawpal.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by hp-pc on 06-12-2016.
 */

public class PrefManager {


    public static void clear(Context context, String prefName) {
        SharedPreferences settings = context.getSharedPreferences(prefName, 0);
        settings.edit().clear().commit();
    }

    public static enum PersistenceKey {USER_ID,USER_NAME,PROFILE_ID, SEARCH_PAL_FILTERS, PROFILE_IMAGE, REMEMBER_ME, FCM_TOKEN, IS_SUBSCRIBED}


    public static void store(Context context, PersistenceKey key, String value, String prefName){

        SharedPreferences settings = context.getSharedPreferences(prefName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key.toString(), value);
        editor.commit();
    }

    public static String retrieve(Context context, PersistenceKey key, String prefName){

        SharedPreferences settings = context.getSharedPreferences(prefName, 0);
        return settings.getString(key.toString(), "null");
    }
    public static void removeKeyPreference(Context  context, PersistenceKey key)
    {
        SharedPreferences settings = context.getSharedPreferences(key.toString(), 0);
        settings.edit().clear().commit();
    }

}
