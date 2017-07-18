package com.serenegiant.opencvwithuvc;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") public class SearchActivity extends Activity {

    Button search;
    TextView mrns;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final String TAG = "whoa";
        Toast.makeText(SearchActivity.this, R.string.enter_details_search, Toast.LENGTH_SHORT).show();
        search = (Button) findViewById(R.id.searchMRBtn);
        mrns = (EditText) findViewById(R.id.searchMRText);
        db=openOrCreateDatabase("mydbase.db", MODE_PRIVATE, null);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mrns.getText().length()!=0) {
                    Cursor cursor=db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name='pat'",null);
                    int x=cursor.getCount();
                    String y=Integer.toString(x);
                    Log.d(TAG,"value13 is"+y);
                    if (x==0)
                    {
                        Toast.makeText(SearchActivity.this, "No patients in database yet!", Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        Cursor c = db.rawQuery("select count(*) from pat where mr_no=" + mrns.getText(), null);
                        Log.d(TAG,"value12 is"+c);

                        c.moveToFirst();
                        if (c.getInt(0) == 0) {
                            Toast.makeText(SearchActivity.this, "Not in Database", Toast.LENGTH_SHORT).show();
                            Intent it1 = new Intent(SearchActivity.this, SearchActivity.class);
                            startActivity(it1);
                            finish();
                        } else {
                            Intent it2 = new Intent(SearchActivity.this, SuccessActivity.class);
                            it2.putExtra("mr_no", mrns.getText());
                            startActivity(it2);
                            finish();
                        }
                    }
                }
                else {
                    Toast.makeText(SearchActivity.this, "Blank", Toast.LENGTH_SHORT).show();
                    Intent it1 = new Intent(SearchActivity.this, SearchActivity.class);
                    startActivity(it1);
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.suc, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_home) {
            Intent it = new Intent(SearchActivity.this, FirstActivity.class);
            startActivity(it);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        // your code
        Intent it = new Intent(SearchActivity.this, FirstActivity.class);
        startActivity(it);
        finish();
    }
}

