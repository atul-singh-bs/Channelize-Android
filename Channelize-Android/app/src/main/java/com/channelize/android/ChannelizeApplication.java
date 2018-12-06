/*
 *   Copyright (c) 2018 BigStep Technologies Private Limited.
 *
 *   The distribution of this source code is prohibited.
 */

package com.channelize.android;


import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.channelize.android.pushnotification.MyFcmListenerService;
import com.socialengineaddons.messenger.interfaces.OnPushNotificationClearListener;
import com.socialengineaddons.messenger.utils.Logcat;
import com.socialengineaddons.messenger.utils.MessengerDatabaseUtils;


public class ChannelizeApplication extends MultiDexApplication implements OnPushNotificationClearListener {

    private static Context context;
    private MessengerDatabaseUtils messengerDatabaseUtils;


    public ChannelizeApplication() {
    }

    public static Context getContext() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        // Initializing MessengerDatabase information.
        messengerDatabaseUtils = MessengerDatabaseUtils.getInstance();
        messengerDatabaseUtils.setApplicationContext(context);
        messengerDatabaseUtils.initializeDatabaseUtils(this);
        messengerDatabaseUtils.prepareDefaultCache();

        // Setting must required information.
        MessengerDatabaseUtils.setApiDefaultUrl(Config.API_DEFAULT_URL);
        MessengerDatabaseUtils.setMQTTServerUrl(Config.MQTT_SERVER_URL);
        MessengerDatabaseUtils.setApiKey(Config.API_KEY);
        MessengerDatabaseUtils.setFirebaseProjectNumber(Config.FIREBASE_SENDER_ID);
        MessengerDatabaseUtils.setFirebaseApplicationId(Config.FIREBASE_APPLICATION_ID);
        MessengerDatabaseUtils.setGiphyApiKey(Config.GIPHY_API_KEY);
        MessengerDatabaseUtils.setAwsAPIUrl(Config.AWS_API_URL);
        MessengerDatabaseUtils.setGooglePlacesKey(context.getResources().getString(R.string.places_api_key));
        MessengerDatabaseUtils.setGooglePlacesEnabled(Config.GOOGLE_PLACES_API_KEY);
        messengerDatabaseUtils.initializeMQTTClient();
        messengerDatabaseUtils.initializeMessengerApp(context);
        messengerDatabaseUtils.setAppName(context.getResources().getString(R.string.app_name));
        messengerDatabaseUtils.setPushNotificationClearListener(this);
        Logcat.setLoggingEnabled(true);

        if (messengerDatabaseUtils.getCurrentUserId() != null
                && !messengerDatabaseUtils.getCurrentUserId().isEmpty()) {
            messengerDatabaseUtils.setFirebaseToken();
        }

    }

    @Override
    public void clearPushNotifications() {
        MyFcmListenerService.clearMessengerPushNotification();
    }

}
