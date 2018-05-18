package com.example.henry.couponer_x;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewStores extends AppCompatActivity {

    DatabaseHelper database;
    ListView storeListView;
    ArrayAdapter storeAdapter;
    ArrayList<String> allStores = new ArrayList<>();
    ArrayList<String> storeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new DatabaseHelper(this);
        setContentView(R.layout.store_list);
        // Get the ListView from the activity
        storeListView = (ListView) findViewById(R.id.storeListID);
        addCursorData();
        // If the ListView is not empty, allow user to check the coupon(s) of the store
        if(storeListView != null) {
            storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ViewStores.this, ViewCoupons.class);
                    // Flag set to true so retrieve data based on that store name
                    DatabaseHelper.store_flag =  true;
                    database.storeN = storeListView.getItemAtPosition(i).toString();
                    startActivity(intent);
                }
            });
        }
    }
    // This Override method updates the ListView contents if coupon has been deleted
    @Override
    public void onResume(){
        super.onResume();
        if(!storeList.isEmpty()) {
            storeList.clear();
            allStores.clear();
            addCursorData();
            storeAdapter.notifyDataSetChanged();
        }
    }

    // This helper function gets query from database and adds it into the two ArrayList
    // storeList arrayList is then populated into the storeAdapter
    void addCursorData(){
        Cursor data = database.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this, "There are no contents in this list!",Toast.LENGTH_LONG).show();
        }
        else {
            while (data.moveToNext()) {
                // Adds the store names into Arraylist and iterate through to check
                // if there is already store name inside the Arraylist.
                // If true, don't add into adapter. Else add into adapter
                allStores.add(data.getString(data.getColumnIndex("STORE_NAME")));
                boolean found = false;
                for (int i = 0; i < allStores.size() - 1; i++) {
                    if (data.getString(data.getColumnIndex("STORE_NAME")).equals(allStores.get(i))) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    storeList.add(data.getString(data.getColumnIndex("STORE_NAME")));
                    storeAdapter = new  ArrayAdapter<>(this, android.R.layout.simple_list_item_1, storeList);
                    storeListView.setAdapter(storeAdapter);
                }
            }
        }
    }
}
