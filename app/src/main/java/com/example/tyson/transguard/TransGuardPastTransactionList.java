package com.example.tyson.transguard;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;

public class TransGuardPastTransactionList extends TransGuard {

    ListView lv;
    ArrayList<String> singleList;
    ArrayList<String> secondList;
    ArrayList<ArrayList<String>> listArray;

    // Instantiate the parser
    XMLParser xmlParser = new XMLParser();
    List<String> entries = null;
    static ArrayList<String> entriesList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_guard_past_transaction_list);

        try {

            listArray =  new ArrayList<ArrayList<String>>();
            singleList = new ArrayList<String>();

            for(int i = 0; i < entriesList.size(); i+=3) {
                secondList = new ArrayList<String>();
                singleList.add(entriesList.get(i));
                for(int j = 0; j < 3; j++) {
                    secondList.add(entriesList.get(i+j));
                }

                listArray.add(secondList);
            }

            lv = (ListView) findViewById(R.id.listViewPastTrans);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, singleList);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Intent intent = new Intent(TransGuardPastTransactionList.this, TransGuardPastTransaction.class);
                    String date = null;
                    String name = null;
                    String amount = null;
                    int count = 0;

                    for(int i = 0; i < singleList.size(); i++) {
                        if(position == i) {
                            name = listArray.get(i).get(count);
                            date = listArray.get(i).get(count+1);
                            amount = listArray.get(i).get(count+2);
                        }
                    }

                    intent.putExtra("date", date);
                    intent.putExtra("name", name);
                    intent.putExtra("amount", amount);
                    startActivity(intent);
                }
            });
        }catch(Exception e){
            // if any I/O error occurs
            e.printStackTrace();
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

    public static void getValuesFromHandler(ArrayList<String> valueList) {
        entriesList = valueList;
    }
}
