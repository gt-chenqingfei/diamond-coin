package com.lovecoin.ediamond.push;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created on 2017/10/31 0031.
 */

public class MessageService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Logger.d("From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Logger.d("Message data payload: " + remoteMessage.getData());
            handleNow(remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            //Logger.d("Message Notification Body: " + notification.getBody());


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNow(Map<String, String> data) {
        if (data.containsKey(PushHandler.PUSH_KEY)) {
            String action = data.get(PushHandler.PUSH_KEY);
            if (action != null && !PushHandler.PUSH_VALUE_ERROR.equals(action)) {
                PushHandler.routePush(getApplicationContext(), action);
            }
        }
    }

    private void scheduleJob() {
    }

}
