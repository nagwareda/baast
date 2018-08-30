package com.tec77.bsatahalk.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tec77.bsatahalk.R;
import com.tec77.bsatahalk.database.SharedPref;
import com.tec77.bsatahalk.view.activity.HomeActivity;
import com.tec77.bsatahalk.view.activity.LoginActivity;

import java.util.Random;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;
import static android.support.v4.app.NotificationCompat.VISIBILITY_PUBLIC;
import static com.tec77.bsatahalk.database.SharedPref.editor;

/**
 * Created by Nagwa on 05/10/2017.
 */

/**
 *
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();
        Log.d("from", from);

        if (remoteMessage.getData() != null) {


            int count = 0;
            count = SharedPref.getInstance(this).getInt("numberNotification");
            SharedPref pref = new SharedPref(this);
            if (!pref.loggeedIn())
                notifyMe(this, new Random().nextInt(), new Intent(this, LoginActivity.class),
                        remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
            else
                notifyMe(this, new Random().nextInt(), new Intent(this, HomeActivity.class),
                        remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
            count++;
            editor.putInt("numberNotification", count);
            editor.commit();
            Intent intent = new Intent("numberNotification");
            intent.putExtra("count", count);
            broadcaster.sendBroadcast(intent);
            //}
        }
    }



    /**
     * @param context     the context of the application
     * @param requestCode to handle in the activity in onActivityResult method  if is it a constant
     *                    it will be one notification and if it variable number it will be multiple notification
     * @param intent      the intent of the activity which will go to it after click on notification
     * @param title       the title of notification
     * @param body        the message of the notification
     */
    //TODO:Add This Part
    public static void notifyMe(Context context, int requestCode, Intent intent, String title, String body) {
        intent.putExtra("from", "notification");
        if(title.contains("قاعده") || title.contains("القاعده") || title.contains("قاعدة") || title.contains("القاعدة")){

            intent.putExtra("ka3da",title);
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setVisibility(VISIBILITY_PUBLIC)
                        .setPriority(PRIORITY_HIGH);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Random r = new Random();
        notificationManager.notify(r.nextInt(), mBuilder.build());
    }
}