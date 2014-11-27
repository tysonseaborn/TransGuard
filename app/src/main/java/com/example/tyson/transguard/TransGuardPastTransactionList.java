package com.example.tyson.transguard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class TransGuardPastTransactionList extends Activity {

    ListView lv;
    List<String> listArray;

    // Instantiate the parser
    XMLParser xmlParser = new XMLParser();
    InputStream inputStream = null;
    List<XMLParser.Entry> entries = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_past_transaction_list);

        try {
            inputStream = new FileInputStream("C:/Users/Tyson/Desktop/xmlTestFile.xml");
            entries = xmlParser.parse(inputStream);


            listArray =  new ArrayList<String>();

            for (XMLParser.Entry entry : entries) {
                listArray.add(entry.name);
            }

//            listArray.add("August");
//            listArray.add("September");


            lv = (ListView) findViewById(R.id.listViewPastTrans);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(TransGuardPastTransactionList.this, TransGuardPastTransactionSecondList.class);
                    String dateValue = lv.getItemAtPosition(position).toString();
                    intent.putExtra("routeName", dateValue);
                    startActivity(intent);
                }

            });

        }catch(Exception e){

            // if any I/O error occurs
            e.printStackTrace();
        }finally{

            // releases system resources associated with this stream
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trans_guard_past_transaction_list, menu);
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
}
