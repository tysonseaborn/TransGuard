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

    String mes;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    public ArrayList<String> valueList;
//    private Boolean valueBool = true;
//    private int valueCounter = 1;

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
        // Creates an explicit intent for an Activity in your app

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(TransGuard.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0,
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(mId, mBuilder.build());


        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        valueList = new ArrayList();
        for(int i = 1; i < extras.size()+1; ++i) {
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
        //valueList.add("harhar");
        TransGuardPastTransactionList.getValuesFromHandler(valueList);
        //mes = extras.getString("title");

       // TransGuardMainMenu.transactionCheckIn();

        //showToast();
        //Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
            }
        });
    }
}