package com.example.tyson.transguard;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class TransGuardPastTransactionSecondList extends TransGuard {

    ListView lv;
    List<String> listArray;
    String dateValue;
    String nameValue;
    String amountValue;
    String monthValue;


    // Instantiate the parser
    XMLParser xmlParser = new XMLParser();
    //InputStream inputStream = null;
    List<XMLParser.Entry> entries = null;
    AssetManager am;
    InputStream is;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_past_transaction_second_list);

//        dateValue = getIntent().getExtras().getString("date");
//        nameValue = getIntent().getExtras().getString("name");
//        amountValue = getIntent().getExtras().getString("amount");
        monthValue = getIntent().getExtras().getString("month");

        am = getBaseContext().getAssets();

        try {
            is = am.open("xmlTestFile.xml");
            //is = new FileInputStream("C:/Users/Tyson/Desktop/xmlTestFile.xml");
            entries = xmlParser.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        listArray =  new ArrayList<String>();

        for (XMLParser.Entry entry : entries) {
            if(entry.date != null) {
                String month = entry.date.substring(6, 7);

                if(monthValue.equals(month)) {
                    listArray.add(entry.name.trim());
                }
            }
        }


        //listArray.add(nameValue);
//        listArray.add("Wal-Mart");
//        listArray.add("Target");


        lv = (ListView) findViewById(R.id.listViewSecondList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(TransGuardPastTransactionSecondList.this, TransGuardPastTransaction.class);

                String date;
                String name;
                String amount;

                XMLParser.Entry entry = entries.get(position);
                date = entry.date;
                name = entry.name;
                amount = entry.amount;

                intent.putExtra("date", date);
                intent.putExtra("name", name);
                intent.putExtra("amount", amount);
                startActivity(intent);
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trans_guard_past_transaction_second_list, menu);
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
