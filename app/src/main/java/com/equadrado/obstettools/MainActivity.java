package com.equadrado.obstettools;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.equadrado.controler.PregnancyController;

import java.io.Serializable;

import static android.os.SystemClock.sleep;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = "MainActivity";

    private PregnancyController pregnancyController;
    private ImageView imageViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "Eduardo Quadrado N01124078");

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        setContentView(R.layout.activity_main);

        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);

        final Animation scaleup = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scaleup);
        scaleup.setFillAfter(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        pregnancyController = new PregnancyController(this);

        imageViewBack.setAlpha((float) 0.4);
        imageViewBack.startAnimation(scaleup);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent i) {
        if(requestCode == 0) {
            // getting error here
//            pregnancyController = (PregnancyController)
//                                        i.getSerializableExtra("PregnancyController");
//            btn.setText("Preg "+Integer.toString(pregnancyController.count()));
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    /*
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.main, menu);
           eturn true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calc_bas ) {
            Intent calc = new Intent(this, DateCalcsActivity.class);

            calc.putExtra("PregnancyController", (Serializable) pregnancyController);

            startActivityForResult(calc, 0);
        } else if (id == R.id.nav_biometric) {
            Intent bio = new Intent(this, BiometricActivity.class);

            bio.putExtra("PregnancyController", (Serializable) pregnancyController);

            startActivityForResult(bio, 1);
        } else if (id == R.id.nav_exit) {
            finish();
        } else if (id == R.id.nav_icd10) {
            Intent icd = new Intent(this, SearchICDActivity.class);

            startActivity(icd);
        } else if (id == R.id.nav_contact) {
            Intent cu = new Intent(this, ContacUsActivity.class);

            startActivity(cu);
        } else if (id == R.id.nav_about) {
            Intent ab = new Intent(this, AboutActivity.class);

            startActivity(ab);
        } else if (id == R.id.nav_map) {
            Intent ma = new Intent(this, MapsActivity.class);

            startActivity(ma);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
