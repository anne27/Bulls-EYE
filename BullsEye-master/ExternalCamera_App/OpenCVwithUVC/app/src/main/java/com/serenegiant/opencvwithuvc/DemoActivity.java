package com.serenegiant.opencvwithuvc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        Button start=(Button)findViewById(R.id.button);
        start.setText("Click me!");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(DemoActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }
}