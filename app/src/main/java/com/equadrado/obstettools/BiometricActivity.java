package com.equadrado.obstettools;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.equadrado.controler.PregnancyController;
import com.equadrado.controler.TextChangedListener;
import com.equadrado.model.DBConnection;
import com.equadrado.model.Pregnancy;

public class BiometricActivity extends AppCompatActivity {
    private final String TAG = "BiometricActivity";

    private int abdAP, abdTx, abdCA, bpd, ofd, circCef;

    private EditText etDBP;
    private CheckBox checkDBP;

    private EditText etDOF;
    private CheckBox checkDOF;

    private EditText etAbdAP;

    private EditText etAbdT;
    private EditText etAbdCA;
    private CheckBox checkAbdCA;

    private EditText etFem;
    private CheckBox checkFem;
    private Button btFem;

    private EditText etUmero;
    private CheckBox checkUmero;

    private TextView textViewCalcs;

    protected DBConnection dbConn;

    Pregnancy pregnancy;
    PregnancyController pregnancyController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bometric);

        Log.i(TAG, "Eduardo Quadrado N01124078");

        // get pregnancy controller from Main Activity
        Intent i = getIntent();
        pregnancyController = (PregnancyController) i.getSerializableExtra("PregnancyController");

        abdAP = 0;
        abdTx = 0;
        abdCA = 0;
        bpd = 0;
        ofd = 0;
        circCef = 0;

        etDBP = (EditText) findViewById(R.id.edtBPD);
        checkDBP = (CheckBox) findViewById(R.id.chkBoxBPD);

        etDOF = (EditText) findViewById(R.id.edtOFD);
        checkDOF = (CheckBox) findViewById(R.id.chkBoxOFD);

        etAbdAP = (EditText) findViewById(R.id.edtAbAPD);

        etAbdT = (EditText) findViewById(R.id.edtAbTxD);
        etAbdCA = (EditText) findViewById(R.id.edtAbCirc);
        checkAbdCA = (CheckBox) findViewById(R.id.chkBoxCA);

        etFem = (EditText) findViewById(R.id.edtFemur);
        checkFem = (CheckBox) findViewById(R.id.chkBoxFemur);

        etUmero = (EditText) findViewById(R.id.edtUmerus);
        checkUmero = (CheckBox) findViewById(R.id.chkBoxUmerus);

        textViewCalcs = (TextView) findViewById(R.id.textViewCalcs);

        dbConn = new DBConnection(this);

        // create new pregnancy instance
        pregnancy = pregnancyController.addPregnancy(dbConn);

        etDBP.addTextChangedListener(new TextChangedListener<EditText>(etDBP) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                int gaDBP = 0;
                String val = target.getText().toString().trim();
                // disable if empty or less than minimum
                checkDBP.setEnabled(!val.isEmpty() &&
                        (Integer.parseInt(val) >= pregnancy.getGeneralMeasure().getMIN_DBP2()));
                if (checkDBP.isEnabled()) {
                    gaDBP = pregnancy.setBpd(Integer.parseInt(val));
                } else {
                    gaDBP = pregnancy.setBpd(0);
                }

                checkDBP.setChecked(gaDBP > 0);
                checkDBP.setText(String.format("%7s", gaDBP+"weeks").replace(' ', '0'));
            }
        });

        checkDBP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int val = 0;
                if ( isChecked ) {
                    if (!etDBP.getText().toString().isEmpty())
                        val = Integer.parseInt(etDBP.getText().toString().trim());
                }
                int ga = pregnancy.setBpd(val);

                checkDBP.setText(String.format("%7s", ga+"weeks").replace(' ', '0'));

                updateCalcGA();
            }
        });

        etDOF.addTextChangedListener(new TextChangedListener<EditText>(etDOF) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                // calculate gestacional age to DOF
                int gaDOF = 0;
                String val = target.getText().toString().trim();
                // disable if empty or less than minimum
                checkDOF.setEnabled(!val.isEmpty() &&
                        (Integer.parseInt(val) >= pregnancy.getGeneralMeasure().getMIN_DOF1()));
                if (checkDOF.isEnabled())
                    gaDOF = pregnancy.setOfd(Integer.parseInt(val));
                else {
                    gaDOF = pregnancy.setOfd(0);
                }

                checkDOF.setChecked(gaDOF > 0);
                checkDOF.setText(String.format("%7s", gaDOF+"weeks").replace(' ', '0'));
            }
        });

        checkDOF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int val = 0;
                if ( isChecked ) {
                    if (!etDOF.getText().toString().isEmpty())
                        val = Integer.parseInt(etDOF.getText().toString().trim());
                }
                int ga = pregnancy.setOfd(val);

                checkDOF.setText(String.format("%7s", ga+"weeks").replace(' ', '0'));

                updateCalcGA();
            }
        });

        etAbdAP.addTextChangedListener(new TextChangedListener<EditText>(etAbdAP) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (!target.getText().toString().isEmpty()) {
                    abdAP = Integer.parseInt(target.getText().toString().trim());
                } else {
                    abdAP = 0;
                }
                calcCircAbd(abdAP, abdTx);
            }
        });

        etAbdT.addTextChangedListener(new TextChangedListener<EditText>(etAbdT) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (!target.getText().toString().isEmpty()) {
                    abdTx = Integer.parseInt(target.getText().toString().trim());
                } else {
                    abdTx = 0;
                }
                calcCircAbd(abdAP, abdTx);
            }
        });

        checkAbdCA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int val = 0;
                if ( isChecked ) {
                    if (!etAbdCA.getText().toString().isEmpty())
                        val = Integer.parseInt(etAbdCA.getText().toString().trim());
                }
                int ga = pregnancy.setcAbd(val);

                checkAbdCA.setText(String.format("%7s", ga+"weeks").replace(' ', '0'));

                updateCalcGA();
            }
        });

        etFem.addTextChangedListener(new TextChangedListener<EditText>(etFem) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                int gaFem = 0;
                String val = target.getText().toString().trim();
                // disable if empty or less than minimum
                checkFem.setEnabled(!val.isEmpty() &&
                        (Integer.parseInt(val) >= pregnancy.getGeneralMeasure().getMIN_FEM1()));
                if (checkFem.isEnabled()) {
                    gaFem = pregnancy.setFem(Integer.parseInt(val));
                } else {
                    gaFem = pregnancy.setOfd(0);
                }

                checkFem.setChecked(gaFem > 0);
                checkFem.setText(String.format("%7s", gaFem+"weeks").replace(' ', '0'));
            }
        });

        checkFem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int val = 0;
                if ( isChecked ) {
                    if (!etFem.getText().toString().isEmpty())
                        val = Integer.parseInt(etFem.getText().toString().trim());
                }
                int ga = pregnancy.setFem(val);

                checkFem.setText(String.format("%7s", ga+"weeks").replace(' ', '0'));

                updateCalcGA();
            }
        });

        etUmero.addTextChangedListener(new TextChangedListener<EditText>(etUmero) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                int gaUme = 0;
                String val = target.getText().toString().trim();
                // disable if empty or less than minimum
                checkUmero.setEnabled(!val.isEmpty() &&
                        (Integer.parseInt(val) >= pregnancy.getGeneralMeasure().getMIN_UMERO1()));
                if (checkUmero.isEnabled()){
                    gaUme = pregnancy.setUme(Integer.parseInt(val));
                } else {
                    gaUme = pregnancy.setUme(0);
                }

                checkUmero.setChecked(gaUme > 0);
                checkUmero.setText(String.format("%7s", gaUme+"weeks").replace(' ', '0'));
            }
        });

        checkUmero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int val = 0;
                if ( isChecked ) {
                    if (!etUmero.getText().toString().isEmpty())
                        val = Integer.parseInt(etUmero.getText().toString().trim());
                }
                int ga = pregnancy.setUme(val);

                checkUmero.setText(String.format("%7s", ga+"weeks").replace(' ', '0'));

                updateCalcGA();
            }
        });

    }

    public void calcCircAbd (int mdAP, int mdTx) {
        int gaCA = 0;
        int mdCA = 0;
        if (mdAP > 0 && mdTx > 0)
            mdCA = (int) Math.round((mdAP+mdTx)*1.57);

        // disable if empty or less than minimum
        checkAbdCA.setEnabled(mdCA >= pregnancy.getGeneralMeasure().getMIN_CA1());
        if (checkAbdCA.isEnabled()) {
            etAbdCA.setText(String.valueOf(mdCA));
            gaCA = pregnancy.setcAbd(mdCA);
        } else {
            etAbdCA.setText("");
        }

        checkAbdCA.setChecked(gaCA > 0);
        checkAbdCA.setText(String.format("%7s", gaCA+"weeks").replace(" ", "0"));
    }

    private void updateCalcGA(){
        String show = "Gestacional Age: "+String.valueOf(pregnancy.getGestAgeAver())+"weeks";

        pregnancy.getFetalWeight().calculateAll(pregnancy.getcCef(), pregnancy.getcAbd(),
                                                pregnancy.getFem(), pregnancy.getBpd());

        int w1 = pregnancy.getFetalWeight().getWeight1();
        if (w1 > 0)
            show += "\nEstimated weight (HC, AC, FEM):"+w1+"g";
        int w2 = pregnancy.getFetalWeight().getWeight2();
        if (w2 > 0)
            show += "\nEstimated weight (HC, AC, FEM):"+w2+"g";
        int w3 = pregnancy.getFetalWeight().getWeight3();
        if (w3 > 0)
            show += "\nEstimated weight (BPD, AC):"+w3+"g";

        textViewCalcs.setText(show);
    }

}
