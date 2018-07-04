package com.equadrado.obstettools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    private final String TAG = "AboutActivity";

    private TextView textViewVersao;
    private Button aboutBut;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Log.i(TAG, "Eduardo Quadrado N01124078");

        String version = "";
        version = getVersionNumber(getApplicationContext());

        textViewVersao = (TextView) findViewById(R.id.textViewVersao);

        textViewVersao.setText("Version: "+version);

        aboutBut = (Button) findViewById(R.id.aboutBut);

        aboutBut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }

    //Get current version number.
    public static String getVersionNumber(Context context) {
        String version = "?";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AboutActivity", "Package name not found", e);
        };
        return version;
    }
}
