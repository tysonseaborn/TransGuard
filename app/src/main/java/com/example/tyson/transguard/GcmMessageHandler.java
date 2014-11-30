package com.example.tyson.transguard;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
        valueList = new ArrayList();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        try {


        for(int i = 1; i < extras.size()+1; ++i) {
            valueList.add(extras.getString(("name" + Integer.toString(i))));
            valueList.add(extras.getString(("date" + Integer.toString(i))));
            valueList.add(extras.getString(("amount" + Integer.toString(i))));
        }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        TransGuardPastTransactionList.getValuesFromHandler(valueList);

        //mes = extras.getString("title");


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