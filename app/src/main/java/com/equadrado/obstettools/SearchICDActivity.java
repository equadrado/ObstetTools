package com.equadrado.obstettools;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.equadrado.model.DBConnection;
import com.equadrado.model.ICD10;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchICDActivity extends AppCompatActivity {
    private final String TAG = "SearchICDActivity";

    private EditText editText1;
    private EditText editText2;
    private RadioButton radio0;
    private RadioButton radio3;
    private RadioButton radio4;
    private Button button1;
    private Context context;
    private ListView listResults;
    public static ArrayList<String> ArrayofICD = new ArrayList<String>();

    private DBConnection dbConn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_icd);

        Log.i(TAG, "Eduardo Quadrado N01124078");

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        button1 = (Button) findViewById(R.id.button1);
        radio0 = (RadioButton) findViewById(R.id.radio0);
        radio3 = (RadioButton) findViewById(R.id.radio3);
        radio4 = (RadioButton) findViewById(R.id.radio4);
        //listResults = (ListView) findViewById(R.id.listResults);

        dbConn = new DBConnection(this);

        try {
            dbConn.createDataBase();
            dbConn.openDataBase();
        } catch (IOException e) {
            Log.e("DateCalcActivity", e.toString() + "  UnableToCreateDatabase");
            //throw new Error("UnableToCreateDatabase");
        }

        // just check the connection - TEST
        if (dbConn.checkDataBase()){
            //int val = dbConn.listTables().length;
            //Toast.makeText(this, "Connected !", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Not Connected !", Toast.LENGTH_LONG).show();
        }

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Cursor cursor;
                String pesq = new String(editText1.getText().toString());
                int tipo;
                int sexo = 0;

                // clean result
                editText2.setText("");

                hideKeyboard();

                // check empty data
                if (pesq.equalsIgnoreCase("")) {
                    // campo vazio
                } else {
                    // do search
                    if (radio0.isChecked()) {
                        // by description
                        tipo = 0;
                    } else {
                        // by code
                        tipo = 1;
                    }

                    if (radio3.isChecked()) {
                        // male
                        sexo = 1;
                    } if (radio4.isChecked()) {
                        // female
                        sexo = 2;
                    }

                    cursor = dbConn.searchData(editText1.getText().toString(), tipo, sexo);

                    // show data
                    if (cursor != null && cursor.moveToFirst()) {

                        //List<ICD10> icdList = new ArrayList<ICD10>();

                        int idxID = cursor.getColumnIndex("Descricao"); //  tbl_name
                        int idxCode = cursor.getColumnIndex("CodCID10"); //  tbl_name

                        do {
                            //icdList.add(new ICD10(cursor.getString(idxCode), cursor.getString(idxID)));
                            editText2.append(cursor.getString(idxID)+(char)13+(char)10);
                        } while (cursor.moveToNext());

                    }

                    // null cursor
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            }
        });

    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText1.getWindowToken(), 0);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
        catch (Exception e) {
            Toast.makeText (context, "Erro : " + e.getMessage(), Toast.LENGTH_SHORT).show () ;
        }
    }

}
