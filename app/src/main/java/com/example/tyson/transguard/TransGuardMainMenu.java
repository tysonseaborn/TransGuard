package com.example.tyson.transguard;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class TransGuardMainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_main_menu);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                    return;

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
        }
    }
}
