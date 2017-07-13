package com.example.anannyauberoi.appfinal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by vedipen on 29/11/16.
 */


@SuppressLint("NewApi") public class FirstActivity extends RuntimePermissionsActivity{
    Button first, second;
    private static final int REQUEST_PERMISSION = 20;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_first);
        first=(Button) findViewById(R.id.FirstActivityFirstButton);
        second=(Button) findViewById(R.id.FirstActivitySecondButton);
        //Three permissions are required for the app to function properly.
        //Shows as two dialogue boxes- Storage and Camera.
        requestAppPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA},
                R.string.msg,REQUEST_PERMISSION);
        first.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FirstActivity.this, R.string.WelcomeToast, Toast.LENGTH_SHORT).show();
                Intent itf1=new Intent(FirstActivity.this,DetailsActivity.class);
                startActivity(itf1);
                finish();
            }
        });
        second.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent itf2=new Intent(FirstActivity.this,SearchActivity.class);
                startActivity(itf2);
                finish();
            }
        });
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
    }
}