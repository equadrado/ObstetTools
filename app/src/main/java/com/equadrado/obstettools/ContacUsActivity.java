package com.equadrado.obstettools;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ContacUsActivity extends AppCompatActivity {
    private final String TAG = "ContacUsActivity";
    private Button contactBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contac_us);

        Log.i(TAG, "Eduardo Quadrado N01124078");

        contactBut = (Button) findViewById(R.id.aboutBut);

        contactBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
