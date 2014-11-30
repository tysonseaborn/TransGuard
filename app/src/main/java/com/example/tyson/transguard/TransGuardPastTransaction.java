package com.example.tyson.transguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TransGuardPastTransaction extends TransGuard {

    TextView tvName, tvDate, tvAmount;
    String name, date, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_past_transaction);

        name = getIntent().getExtras().getString("name");
        date = getIntent().getExtras().getString("date");
        amount = getIntent().getExtras().getString("amount");

        tvName = (TextView)findViewById(R.id.textViewName);
        tvDate = (TextView)findViewById(R.id.textViewDate);
        tvAmount = (TextView)findViewById(R.id.textViewAmount);


        tvName.setText(name);
        tvDate.setText(date);
        tvAmount.setText(amount);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trans_guard_past_transaction, menu);
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
}
