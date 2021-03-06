package com.example.tyson.transguard;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class TransGuardMainMenu extends TransGuard {
    GoogleCloudMessaging gcm;
    String apiKey = "AIzaSyBWfKLPBvX8P4tm2sI4bKiT4LA2XUyejp4";
    public static String regID;
    String PROJECT_NUMBER = "492813484993";
    public static Button checkinButton;

    //Required for displaying the latest incoming transaction
    public static String rName;
    public static String rDate;
    public static String rAmount;
    public static boolean isTrans = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_main_menu);

        checkinButton = (Button)findViewById(R.id.buttonCheckIn);
        try {
            getRegId();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trans_guard_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            openMenu("about");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Open the selected menu item
    public void openMenu(String menuItem) {
        if(menuItem.equals("about")) {
            Intent about = new Intent(this, About.class);
            startActivity(about);
        }
    }

    public void confirmCheckIn() {
        DialogFragment newFragment = new CheckInFragment();
        newFragment.show(getFragmentManager(), "checkin");
    }

    public void transactionCheckIn() {
        DialogFragment newFragment = new TransactionFragment();
        newFragment.show(getFragmentManager(), "transaction");
    }


    public void onButtonClick(View view) {

        GPSService mGPSService = new GPSService(getBaseContext());
        mGPSService.getLocation();

        switch(view.getId()) {
            case R.id.buttonCheckIn:

                if (mGPSService.isLocationAvailable == false) {
                    Toast.makeText(getBaseContext(), "Your location is not available. Please make sure you have location services enabled.", Toast.LENGTH_SHORT).show();

                    if (isTrans = true) {
                        // Popup!
                        confirmCheckIn();
                        isTrans = false;
                    }
                } else {
                    if (isTrans = true) {
                        // Getting location co-ordinates
                        double latitude = mGPSService.getLatitude();
                        double longitude = mGPSService.getLongitude();
                        Content content = createContent();
                        content.createCoords(Double.toString(latitude), Double.toString(longitude));
                        post(apiKey, content);
                        isTrans = false;
                        checkinButton.setVisibility(View.INVISIBLE);
                        Toast.makeText(getBaseContext(), "Transaction and Location Verified!", Toast.LENGTH_LONG);
                    }
                }
                mGPSService.closeGPS();
                break;

            case R.id.buttonPastTrans:
                Intent iPastTrans = new Intent(this, TransGuardPastTransactionList.class);
                startActivity(iPastTrans);
                break;
        }
    }

    public void getRegId() throws ExecutionException, InterruptedException {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }

                    regID = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regID;
                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    Log.i("Error: ", ex.getMessage());
                }
                return regID;
            }

            @Override
            protected void onPostExecute(String regID) {
                //etRegId.setText(msg + "\n");
                Log.i("REGID", "Device registered, registration ID=" + regID);
                Toast.makeText(getBaseContext(), "Device registered, registration ID=" + regID, Toast.LENGTH_LONG);
                Content content = createContent();
                content.createRegID(regID);
                post(apiKey, content);

            }
        }.execute(null, null, null);
    }

    public static void post(final String apiKey, final Content content) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    // 1. URL
                    URL url = new URL("https://android.googleapis.com/gcm/send");

                    // 2. Open connection
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // 3. Specify POST method
                    conn.setRequestMethod("POST");

                    // 4. Set the headers
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Authorization", "key=" + apiKey);

                    conn.setDoOutput(true);

                    //Use Jackson to create a JSON object
                    ObjectMapper mapper = new ObjectMapper();
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    // Copy JSON
                    mapper.writeValue(wr, content);
                    //Send the request
                    wr.flush();
                    wr.close();

                    // Get response code
                    int responseCode = conn.getResponseCode();
                    Log.i("CONNECTION:", "\nSending 'POST' request to URL : " + url);
                    Log.i("CONNECTION:", "Response Code : " + responseCode);

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    Log.i("RESPONSE:", response.toString());

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(null, null, null);
    }

    //content for the android server
    public static Content createContent(){

        Content c = new Content();

        c.addRegId("APA91bEZn9zCQGNYko7JKSBETYojO57ErEQWUV-14sAvU49IuOhCqhpcCVQtzS4GKZdLdmindSOMws_F6dUqr3eoPiA1vqMT9CHOluCwZu8eWvetQZ08AzYoQOcvPbjVTQ-2nprA6nnh97pH-979VgwvXfzNo1EQ_0FrVOqtjI7E3cImMHtQqW4");
        c.createData("Test Title", "Test Message");

        return c;
    }

    //Broadcast logic in order to get Intent values from GCM
    @Override
    public void onResume() {
        super.onResume();

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("trans"));
    }

    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            rName = intent.getStringExtra("rName");
            rDate = intent.getStringExtra("rDate");
            rAmount = intent.getStringExtra("rAmount");
            if (intent.getStringExtra("method").equals("checkTransaction")) {
                transactionCheckIn();
            }
        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
}
