package com.example.tyson.transguard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class TransGuard extends Activity {

    NetworkInfo mWifi;

    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard);
        etUsername = (EditText)findViewById(R.id.usernameInput);
        etPassword = (EditText)findViewById(R.id.passwordInput);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trans_guard, menu);
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

    public void openMenu(String menuItem) {
        if(menuItem.equals("about")) {
            Intent about = new Intent(this, About.class);
            startActivity(about);
        }
    }

    public void onButtonClick(View view) {
        switch(view.getId()) {
            case R.id.buttonSignin:
                if(etUsername.getText().toString().matches("") || etPassword.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Username or password cannot be blank", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!mWifi.isConnected()) {
                    Toast.makeText(getApplicationContext(), "Application needs a wifi network connection", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent iLogin = new Intent(this, TransGuardMainMenu.class);
                    startActivity(iLogin);
                }
                break;
        }
    }

}
