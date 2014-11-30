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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class TransGuardPastTransactionList extends TransGuard {

    ListView lv;
    List<String> listArray;

    // Instantiate the parser
    XMLParser xmlParser = new XMLParser();
    //InputStream inputStream = null;
    //List<XMLParser.Entry> entries = null;
    List<String> entries = null;
    static ArrayList<String> entriesList = null;

    AssetManager am;
    InputStream is;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_past_transaction_list);

        try {

//            am = getBaseContext().getAssets();
//            is = am.open("xmlTestFile.xml");
//            //is = new FileInputStream("C:/Users/Tyson/Desktop/xmlTestFile.xml");
//            entries = xmlParser.parse(is);


            listArray =  new ArrayList<String>();

            for(int i = 0; i < entriesList.size(); i+=3) {
                listArray.add(entriesList.get(i));
            }

//            for (XMLParser.Entry entry : entries) {
//                if(entry.date != null && !listArray.contains(getMonth(entry.date) + ' ' + getYear(entry.date))) {
//                    listArray.add(getMonth(entry.date) + ' ' + getYear(entry.date));
//                }
//            }

//            listArray.add("August");
//            listArray.add("September");


            lv = (ListView) findViewById(R.id.listViewPastTrans);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArray);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(TransGuardPastTransactionList.this, TransGuardPastTransactionSecondList.class);
                    //String nameValue = lv.getItemAtPosition(position).toString();
//                    String date;
//                    String name;
//                    String amount;
                    String month;
                    int monthNumber;
                    String[] values;

                    //XMLParser.Entry entry = entries.get(position);
//                    date = entry.date;
//                    name = entry.name;
//                    amount = entry.amount;
                    month = lv.getItemAtPosition(position).toString();
                    values = month.split(" ");

                    Date date = null;
                    try {
                        date = new SimpleDateFormat("MMMM").parse(values[0]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(date);
                    monthNumber = cal.get(Calendar.MONTH);
                    monthNumber += 1;
                    month = Integer.toString(monthNumber);


//                    for(XMLParser.Entry entry : entries) {
//                        if(position == )
//
//                        }
//                    }

//                    intent.putExtra("date", date);
//                    intent.putExtra("name", name);
//                    intent.putExtra("amount", amount);
                    intent.putExtra("month", month);
                    startActivity(intent);
                }

            });

        }catch(Exception e){

            // if any I/O error occurs
            e.printStackTrace();
        }finally{

            // releases system resources associated with this stream
            if(is != null) {
                try {
                    is.close();
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

    public String getMonth(String date){
        int month;
        String newDate;
        newDate = date.substring(5, 7);
        month = Integer.parseInt(newDate);
        return new DateFormatSymbols().getMonths()[month-1];
    }

    public String getYear(String date) {
        String newDate;
        newDate = date.substring(1, 5);
        return newDate;
    }

    public static void getValuesFromHandler(ArrayList<String> valueList) {
        entriesList = valueList;
    }
}
