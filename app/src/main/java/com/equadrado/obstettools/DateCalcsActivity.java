package com.equadrado.obstettools;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.equadrado.controler.TextChangedListener;
import com.equadrado.model.DBConnection;
import com.equadrado.model.Pregnancy;
import com.equadrado.controler.PregnancyController;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateCalcsActivity extends AppCompatActivity {
    private final String TAG = "DateCalcsActivity";

    DatePicker picker;
    EditText edtTxtWeek, edtTxtDay;
    TextView txtViewResults;
    RadioButton rbLMP, rbExamDate;
    RadioGroup rgCalcType;

    private int CALC_TYPE_LMP;
    private int CALC_TYPE_EXAM;

    private int actualCalcType;

    Pregnancy pregnancy;
    PregnancyController pregnancyController;
    Date lpm, lpmExam;

    private DBConnection dbConn;

    @Override
    public void onDestroy() {
        super.onDestroy();

        int resultCode = 0;
        Intent resultIntent = new Intent();
        resultIntent.putExtra("PregnancyController", (Serializable) pregnancyController);
        setResult(resultCode, resultIntent);

/*      // getting error
        Intent i = new Intent();

        if (getParent() == null) {
            setResult(AppCompatActivity.RESULT_OK, i);
        } else {
            getParent().setResult(AppCompatActivity.RESULT_OK, i);
        }
        finish();
*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_calcs);

        Log.i(TAG, "Eduardo Quadrado N01124078");

        // get pregnancy controller from Main Activity
        Intent i = getIntent();
        pregnancyController = (PregnancyController) i.getSerializableExtra("PregnancyController");

        // create reference to view objects
        picker = (DatePicker) findViewById(R.id.dpMainDate);
        edtTxtWeek = (EditText) findViewById(R.id.edtTxtWeek);
        edtTxtDay = (EditText) findViewById(R.id.edtTxtDay);
        txtViewResults = (TextView) findViewById(R.id.txtViewResults);
        rbLMP = (RadioButton) findViewById(R.id.rbLPM);
        rbExamDate = (RadioButton) findViewById(R.id.rbExamDate);
        rgCalcType = (RadioGroup) findViewById(R.id.rgCalcType);

        CALC_TYPE_LMP = R.id.rbLPM;
        CALC_TYPE_EXAM = R.id.rbExamDate;
        lpmExam = new Date(0);
        lpm = new Date(0);

        // initialize DatePicker with current date
        Calendar cal = Calendar.getInstance();
        picker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                // update IdGest Spiners
                updateIdGestFields();

                // update Text size
                ViewGroup childpicker = (ViewGroup)picker.findViewById(Resources.getSystem().getIdentifier("year", "id", "android"));
                EditText edTemp = (EditText)childpicker.getChildAt(0);
                edTemp.setTextSize(30);
                childpicker = (ViewGroup)picker.findViewById(Resources.getSystem().getIdentifier("month", "id", "android"));
                edTemp = (EditText)childpicker.getChildAt(0);
                edTemp.setTextSize(30);
                childpicker = (ViewGroup)picker.findViewById(Resources.getSystem().getIdentifier("day", "id", "android"));
                edTemp = (EditText)childpicker.getChildAt(0);
                edTemp.setTextSize(30);
            }
        });

        // set minimum date to DatePicker to 1 YEAR ago
        long minDate = txDateToLong(picker.getYear()-1, picker.getMonth(), picker.getDayOfMonth());
        picker.setMinDate(minDate);
        // set maximum date to DatePicker to 1 year ahead
        long maxDate = txDateToLong(picker.getYear()+1, picker.getMonth(), picker.getDayOfMonth());
        picker.setMaxDate(maxDate);

        // listen change on radio group
        rgCalcType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                actualCalcType = checkedId;
            }
        });

        // listen change on values gestacional age
        edtTxtWeek.addTextChangedListener(new TextChangedListener<EditText>(edtTxtWeek) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (actualCalcType == CALC_TYPE_EXAM){
                    // update calculation
                    updateIdGestFields();
                }
            }
        });

        dbConn = new DBConnection(this);

        try {
            dbConn.createDataBase();
            dbConn.openDataBase();
        } catch (IOException e) {
            Log.e("DateCalcActivity", e.toString() + "  UnableToCreateDatabase");
            //throw new Error("UnableToCreateDatabase");
        }
        // create new pregnancy instance
        pregnancy = pregnancyController.addPregnancy(dbConn);

        // just check the connection - TEST
        if (dbConn.checkDataBase()){
            //int val = dbConn.listTables().length;
            //Toast.makeText(this, "Connected !", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not Connected !", Toast.LENGTH_LONG).show();
        }
    }

    private void updateIdGestFields(){
        String calcType;

        if (rgCalcType.getCheckedRadioButtonId() == CALC_TYPE_LMP) {
            lpm = new Date(txDateToLong(picker.getYear(), picker.getMonth(), picker.getDayOfMonth()));
            // set lpm to this pregnancy
            pregnancy.setDum(lpm);
            // update gestacional age fields
            edtTxtDay.setText(Integer.toString(pregnancy.getGestAgeDay()));
            edtTxtWeek.setText(Integer.toString(pregnancy.getGestAgeWeek()));
            calcType = "lpm";
        } else {
            calcType = "exam";
            lpmExam = new Date(txDateToLong(picker.getYear(), picker.getMonth(), picker.getDayOfMonth()));

            String sem = new String(edtTxtWeek.getText().toString());
            String dias = new String(edtTxtDay.getText().toString());

            if (sem.equalsIgnoreCase("")) {
                sem = "0";
            }
            if (dias.equalsIgnoreCase("")) {
                dias = "0";
            }

            int nsem = Integer.parseInt(sem);
            int ndias = Integer.parseInt(dias);

            if (nsem > 40 || ndias > 6) {
                Toast.makeText(this, "Gestacional age invalid !\n"+
                        "Week must be less than or equal 40\n"+
                        "Days must be less than or equal 6", Toast.LENGTH_LONG).show();
            }
            // set lpm to this pregnancy
            pregnancy.setExam(lpmExam, nsem, ndias);
        }

        try {
            txtViewResults.setText(pregnancy.getDivsMedGestAge(calcType));
        } catch (Exception e){
            Log.e("This activity", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // oculta teclado
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(edtTxtWeek.getWindowToken(), 0);

    }

    public long txDateToLong(int year, int month, int day){
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }
}
