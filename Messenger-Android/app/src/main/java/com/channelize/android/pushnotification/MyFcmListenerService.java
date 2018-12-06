/*
 *   Copyright (c) 2018 BigStep Technologies Private Limited.
 *
 *   The distribution of this source code is prohibited.
 */
package com.channelize.android.pushnotification;

import android.app.NotificationManager;
import android.content.Context;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.socialengineaddons.messenger.Constants;
import com.socialengineaddons.messenger.cache.CacheManager;
import com.socialengineaddons.messenger.cache.Result;
import com.socialengineaddons.messenger.interfaces.OnPushNotificationClearListener;
import com.socialengineaddons.messenger.utils.Logcat;
import com.socialengineaddons.messenger.utils.MessengerDatabaseUtils;

import java.util.Map;


public class MyFcmListenerService extends FirebaseMessagingService implements OnPushNotificationClearListener {

    // Member variables.
    public static NotificationManager notificationsManager;
    private String chatRoomId;
    private MessengerDatabaseUtils messengerDatabaseUtils;


    @Override
    public void onNewToken(String token) {
        registerRefreshedToken(token);
        super.onNewToken(token);
    }

    /**
     * Method to Update FCM token on server when the token is refreshed.
     *
     * @param token Updated FCM token.
     */
    public void registerRefreshedToken(final String token) {

    }

    /**
     * Called when message is received.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Map data = remoteMessage.getData();
        Logcat.d(MyFcmListenerService.class, "onMessageReceived, data: " + data);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        messengerDatabaseUtils = MessengerDatabaseUtils.getInstance();
        chatRoomId = data.containsKey("chatId") ? (String) data.get("chatId") : null;

        if (notification != null) {
            CacheManager cacheManager = messengerDatabaseUtils.getCacheManagerInstance();
            Result<String> chatId = cacheManager.get(Constants.CURRENT_CHAT_ROOM_ID + messengerDatabaseUtils.getCurrentUserId(),
                    String.class);
            String currentChatRoomScreenId = "";
            if (chatId != null && chatId.getCachedObject() != null) {
                currentChatRoomScreenId = chatId.getCachedObject();
            }

            // Checking if the there is any chat room is already opened up when push notification has received.
            // Do not showing it when the received push notification belong to the same chat room which is opened up currently.
            if (chatRoomId != null && !chatRoomId.isEmpty() &&
                    !chatRoomId.equals(currentChatRoomScreenId)) {
                messengerDatabaseUtils.setPushNotificationClearListener(this);

                generateCustomNotificationForMessenger(getApplicationContext(), notification.getBody(), notification.getTitle());
            }
        }
    }

    private void generateCustomNotificationForMessenger(Context context, String notificationBody, String notificationTitle) {
        // You can generate a notification from the data.
    }


    /**
     * Method to clear messenger's push notification when clicking on it.
     */
    public static void clearMessengerPushNotification() {

    }

    public static void cancelNotification(int notificationId) {

    }

    @Override
    public void clearPushNotifications() {
        clearMessengerPushNotification();
    }
}
