package com.example.tyson.transguard;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GcmMessageHandler extends IntentService {

    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    public ArrayList<String> valueList;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        // Notification!!!
        int mId = 0;
        Context context = getApplicationContext();
        Intent resultIntent = new Intent(Intent.ACTION_MAIN);
        resultIntent.setClass(getApplicationContext(), TransGuard.class);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.tg_logo_small)
                        .setAutoCancel(true)
                        .setContentTitle("TransGuard")
                        .setContentText("Check in now to keep your transactions secure!")
                        .setContentIntent(PendingIntent.getActivity(this, 0, resultIntent, 0));
        ;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(TransGuard.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0,
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());

        valueList = new ArrayList<String>();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        int size = Integer.parseInt(extras.getString("transactionSize"));
        for(int i = 1; i < size; ++i) {
            valueList.add(extras.getString("name" + String.valueOf(i)));
            valueList.add(extras.getString("date" + String.valueOf(i)));
            valueList.add(extras.getString("amount" + String.valueOf(i)));
        }
        Intent i = new Intent("trans");
        i.putExtra("rName", valueList.get(0));
        i.putExtra("rDate", valueList.get(1));
        i.putExtra("rAmount", valueList.get(2));
        i.putExtra("method", "checkTransaction");

        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
        TransGuardPastTransactionList.getValuesFromHandler(valueList);
        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }
}