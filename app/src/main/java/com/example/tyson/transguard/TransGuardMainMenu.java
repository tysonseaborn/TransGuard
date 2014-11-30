package com.example.tyson.transguard;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class TransGuardMainMenu extends TransGuard {
    GoogleCloudMessaging gcm;
    String apiKey = "AIzaSyBWfKLPBvX8P4tm2sI4bKiT4LA2XUyejp4";
    public String regID;
    String PROJECT_NUMBER = "492813484993";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_main_menu);
        getRegId();

        Content content = createContent();
        content.createRegID(regID);
        post(apiKey, content);
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

    public void onButtonClick(View view) {

        GPSService mGPSService = new GPSService(getBaseContext());
        mGPSService.getLocation();
        String address = "";

        switch(view.getId()) {
            case R.id.buttonCheckIn:

                if (mGPSService.isLocationAvailable == false) {

                    // Here you can ask the user to try again, using return; for that
                    Toast.makeText(getBaseContext(), "Your location is not available, please try again.", Toast.LENGTH_SHORT).show();
                    //return;

                    // Or you can continue without getting the location, remove the return; above and uncomment the line given below
                    // address = "Location not available";
                } else {

                    // Getting location co-ordinates
                    double latitude = mGPSService.getLatitude();
                    double longitude = mGPSService.getLongitude();
                    Toast.makeText(getBaseContext(), "Latitude:" + latitude + " | Longitude: " + longitude, Toast.LENGTH_LONG).show();

                    address = mGPSService.getLocationAddress();
                }

                Toast.makeText(getBaseContext(), "Your location is: " + address, Toast.LENGTH_SHORT).show();

                // make sure you close the gps after using it. Save user's battery power
                mGPSService.closeGPS();

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
                                .setContentText("You should check in!")
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

                break;

            case R.id.buttonPastTrans:
                Intent iPastTrans = new Intent(this, TransGuardPastTransactionList.class);
                startActivity(iPastTrans);
                break;
        }
    }

    public void setRegID(String id) {
        regID = id;
    }

    public void getRegId(){
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
                    msg = "Error :" + ex.getMessage();

                }
                return regID;
            }

            @Override
            protected void onPostExecute(String regID) {
                //etRegId.setText(msg + "\n");
                Toast.makeText(getBaseContext(), "Device registered, registration ID=" + regID, Toast.LENGTH_LONG);
                setRegID(regID);
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

                    // 5. Add JSON data into POST request body


                    //`5.1 Use Jackson object mapper to convert Contnet object into JSON
                    ObjectMapper mapper = new ObjectMapper();

                    // 5.2 Get connection output stream
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                    // 5.3 Copy Content "JSON" into
                    mapper.writeValue(wr, content);


                    // 5.4 Send the request
                    wr.flush();

                    // 5.5 close
                    wr.close();
                    // 6. Get the response
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

                    // 7. Print result
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

        c.addRegId("APA91bE7pkJ82PfXhhGWG8zWl5Cl9g0nhLwGmZL0sqJED-SaXYWLmnldbPaUZ90BPEZdquVOknPxvkh5DWkCOfGySCp-hlURrOWst5icMlgnHd-kwWeWlvMd1vnvIddnyX8Q-wKf-Mqub6u_d-BXUyOVr3luIkSZDkwZKdROHGiNwfu57xTwuiM");
        c.createData("Test Title", "Test Message");

        return c;
    }
}
