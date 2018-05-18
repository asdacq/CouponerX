package com.example.henry.couponer_x;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewCoupons extends AppCompatActivity {

    DatabaseHelper database;
    Information info;
    TwoColumnAdapter couponAdapter;
    ListView couponListView;
    ArrayList<Information> coupons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_data);
        database = new DatabaseHelper(this);
        couponListView = (ListView) findViewById(R.id.couponListID);
        // Retrieve data from database
        Cursor data = database.getListContents();
        int numRows = data.getCount();
        if(numRows == 0){
            Toast.makeText(this,
                    "The Database is empty",
                    Toast.LENGTH_LONG).show();
        }
        else{
            // Adds Information into ArrayList<Information>
            while(data.moveToNext()){
                int i = 0;
                info = new Information(data.getString(1), data.getString(2), data.getString(3));
                coupons.add(i,info);
                i++;
            }
            // Set the adapter
            couponAdapter =  new TwoColumnAdapter(this,R.layout.display_information, coupons);
            couponListView.setAdapter(couponAdapter);
        }
        // Delete the coupons based on user holding down on an entry
        if(coupons != null) {
            couponListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int i, long l) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(ViewCoupons.this);
                    adb.setTitle("Delete?");
                    adb.setMessage("Are you sure you want to delete this coupon?");
                    adb.setPositiveButton("No", null);
                    adb.setNegativeButton("Yes", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String store = coupons.get(i).getStoreName();
                            String exp = coupons.get(i).getExpirationDate();
                            String coup = coupons.get(i).getCouponNumber();
                            // Removes the data from the coupon array
                            coupons.remove(i);
                            database.deleteStore(store, exp, coup);
                            // Changes the data of the adapter
                            couponAdapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Coupon has been deleted", Toast.LENGTH_LONG).show();
                            if(coupons.isEmpty()) {
                                finish();
                            }
                        }
                    });
                    adb.show();
                    return true;
                }
            });
        }
        // Goes to the image
        if(coupons != null){
            couponListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(ViewCoupons.this, Barcode.class);
                    startActivity(intent);
                }
            });
        }
    }
}
