package com.equadrado.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by equadrado on 2016-10-31.
 */

public class Pregnancy {

    private final String TAG = "PregnancyClass";

    private DBConnection dbConn;
    private GeneralMeasure generalMeasure;
    private ObstetricIndex obstetricIndex;
    private FetalWeight fetalWeight;

    private Date dum;
    private Date dpp;
    private int idGestSem = 0;
    private int idGestDias = 0;

    private PreviousExam exam;

    private int bpd = 0;
    private int ofd = 0;
    private int cAbd = 0;
    private int cCef = 0;
    private int fem = 0;
    private int ume = 0;

    private int bpdGA = 0;
    private int ofdGA = 0;
    private int cAbdGA = 0;
    private int cCefGA = 0;
    private int femGA = 0;
    private int umeGA = 0;

    private int gestAgeAver = 0;

    public Pregnancy(DBConnection dbConn) {
        this.dbConn = dbConn;

        generalMeasure = new GeneralMeasure(dbConn);
        obstetricIndex = new ObstetricIndex();
        fetalWeight = new FetalWeight();

        Log.i(TAG, "Pregnancy created");
    }

    public int getBpd() {
        return bpd;
    }

    public int setBpd(int bpd) {
        this.bpd = bpd;

        SQLiteDatabase db = dbConn.getReadableDatabase();
        Cursor myC = dbConn.getGestAge(db, String.valueOf(this.bpd), DBConnection.TABLE_NAME_GO_GERAL,
                                        DBConnection.DBP, DBConnection.IG_CRANIO, generalMeasure.getMIN_DBP2());
        myC.moveToFirst();
        if (myC.getCount() > 0)
            bpdGA = Integer.parseInt(myC.getString(0));
        else
            bpdGA = 0;

        setcCef();

        return bpdGA;
    }

    public int getOfd() {
        return ofd;
    }

    public int setOfd(int ofd) {
        this.ofd = ofd;

        SQLiteDatabase db = dbConn.getReadableDatabase();
        Cursor myC = dbConn.getGestAge(db, String.valueOf(this.ofd), DBConnection.TABLE_NAME_GO_GERAL,
                                        DBConnection.DOF, DBConnection.IG_CRANIO, generalMeasure.getMIN_DOF1());
        myC.moveToFirst();
        if (myC.getCount() > 0)
            ofdGA = Integer.parseInt(myC.getString(0));
        else
            ofdGA = 0;

        setcCef();

        return ofdGA;
    }

    public int getcAbd() {
        return cAbd;
    }

    public int setcAbd(int cAbd) {
        this.cAbd = cAbd;

        SQLiteDatabase db = dbConn.getReadableDatabase();
        Cursor myC = dbConn.getGestAge(db, String.valueOf(this.cAbd), DBConnection.TABLE_NAME_GO_GERAL,
                                        DBConnection.CA, DBConnection.IG_CRANIO, generalMeasure.getMIN_CA1());
        myC.moveToFirst();
        if (myC.getCount() > 0)
            cAbdGA = Integer.parseInt(myC.getString(0));
        else
            cAbdGA = 0;

        return cAbdGA;
    }

    public int getcCef() {
        return cCef;
    }

    public int setcCef() {
        if (this.bpd > 0 && this.ofd > 0) {
            this.cCef = (int) Math.round((this.bpd + this.ofd)*1.57);

            SQLiteDatabase db = dbConn.getReadableDatabase();
            Cursor myC = dbConn.getGestAge(db, String.valueOf(cCef), DBConnection.TABLE_NAME_GO_GERAL,
                                            DBConnection.CC, DBConnection.IG_CRANIO, generalMeasure.getMIN_CC2());
            myC.moveToFirst();
            if (myC.getCount() > 0)
                cCefGA = Integer.parseInt(myC.getString(0));
            else
                cCefGA = 0;

            return cCefGA;
        } else {
            return 0;
        }
    }

    public int getFem() {
        return fem;
    }

    public int setFem(int fem) {
        this.fem = fem;

        SQLiteDatabase db = dbConn.getReadableDatabase();
        Cursor myC = dbConn.getGestAge(db, String.valueOf(this.fem), DBConnection.TABLE_NAME_GO_GERAL,
                                        DBConnection.FEM, DBConnection.IG_CRANIO, generalMeasure.getMIN_FEM1());
        myC.moveToFirst();
        if (myC.getCount() > 0)
            femGA = Integer.parseInt(myC.getString(0));
        else
            femGA = 0;

        return femGA;
    }

    public int getUme() {
        return ume;
    }

    public int setUme(int ume) {
        this.ume = ume;

        SQLiteDatabase db = dbConn.getReadableDatabase();
        Cursor myC = dbConn.getGestAge(db, String.valueOf(this.ume), DBConnection.TABLE_NAME_GO_GERAL,
                                        DBConnection.UMERO, DBConnection.IG_CRANIO, generalMeasure.getMIN_UMERO1());
        myC.moveToFirst();
        if (myC.getCount() > 0)
            umeGA = Integer.parseInt(myC.getString(0));
        else
            umeGA = 0;

        return umeGA;
    }

    public GeneralMeasure getGeneralMeasure() {
        return generalMeasure;
    }

    public void setGeneralMeasure(GeneralMeasure generalMeasure) {
        this.generalMeasure = generalMeasure;
    }

    public ObstetricIndex getObstetricIndex() {
        return obstetricIndex;
    }

    public void setObstetricIndex(ObstetricIndex obstetricIndex) {
        this.obstetricIndex = obstetricIndex;
    }

    public FetalWeight getFetalWeight() {
        return fetalWeight;
    }

    public void setFetalWeight(FetalWeight fetalWeight) {
        this.fetalWeight = fetalWeight;
    }

    public Date getDum() {
        return dum;
    }

    public void setDum(Date dum) {
        this.dum = dum;
        // Calculate the most likely delivery date
        setDpp();
        // Calculate gestacional age
        setIdGestWD();
        // Set Fetal Weight

        // Set General Measure

        // Set Obstetric Index
    }

    public void setDum(Date dexam, int w, int d) throws Exception {
        if ((w > 42) || (d > 6)) {
            throw new Exception("Gestacional age out of range");
        }
        Calendar day = Calendar.getInstance();
        day.setTime(dexam);
        int days = -1 * (7 * w + d);
        day.add(Calendar.DATE, days);

        setDum(day.getTime());
    }

    public Date getDpp() {
        return dpp;
    }

    public void setDpp() {
        Calendar day = Calendar.getInstance();
        day.setTime(dum);
        // increment 280 days on dum
        day.add(Calendar.DATE, 280);
        this.dpp = day.getTime();
    }

    public void setDpp(Date dpp) {
        this.dpp = dpp;
    }

    public int getGestAgeWeek() {
        return idGestSem;
    }

    public int getGestAgeDay() {
        return idGestDias;
    }

    public void setIdGest(int idGestSem, int idGestDias) {
        this.idGestSem = idGestSem;
        this.idGestDias = idGestDias;
    }

    public PreviousExam getExam() {
        return exam;
    }

    public void setExam(Date dtExam, int idGestSemExam, int idGestDiasExam) {
        this.exam = new PreviousExam(dtExam, idGestSemExam, idGestDiasExam);
    }

    private String getIdGestSemDayExam() {
        int nWeek = 0;
        int nDay = 0;

        if (this.exam != null){
            nWeek = this.exam.getGestAgeWeekExame();
            nDay = this.exam.getGestAgeDayExame();
        }

        return IdGest(nWeek, nDay);
    }

    private void setIdGestWD() {
        // calcula idade gestacional
        Date today = new Date();
        int nTotal = intervalDays(today, this.dum);
        int nDays = (nTotal % 7);
        nTotal = (int) (nTotal - nDays) / 7;
        this.setIdGest(nTotal, nDays);
    }

    public String getIdGestLMP() {
        return IdGest(idGestSem, idGestDias);
    }

    private String IdGest(int gaSem, int gaDays) {
        String idgest = String.valueOf(gaSem) + " weeks ";
        if (gaDays > 0) {
            idgest += String.valueOf(gaDays) + "/7";
        }
        return idgest;
    }

    private int intervalDays(Date d1, Date d2) {
        int result = (int) ((d1.getTime() - d2.getTime()) / 86400000L); //
        return result < 0 ? result * -1 : result;
    }

    public String getDivsMedGestAge(String type) throws Exception {
        int gestAgeCalc = idGestSem;
        String result = "";

        if (type.equals("exam")) {
            result = "Values estimated using Exam Results\n"+
                    "Exam Date: "+formatDate(getExam().getExamDate())+
                    " G.A.:"+getIdGestSemDayExam()+
                    "\nLMP estimated by exam results: "+formatDate(this.exam.getLmpExame());
            gestAgeCalc = this.exam.getGestAgeWeekExame();
        } else {
            result = "Values estimated using LMP\n"+
                    "L.M.P.: "+formatDate(dum)+" Gest.Age.: "+getIdGestLMP();
        }

        String[] fieldList = {DBConnection.PESO10, DBConnection.PESO50, DBConnection.PESO90,
                DBConnection.PSACM15MM, DBConnection.ILA05, DBConnection.ILA95, DBConnection.IRUMB05,
                DBConnection.IRUMB90, DBConnection.IPACM05, DBConnection.IPACM90};

        SQLiteDatabase db = dbConn.getReadableDatabase();

        if(gestAgeCalc > 0) {

            try{
                Cursor myC = db.query(DBConnection.TABLE_NAME_GO_GERAL, fieldList, DBConnection.IG_CRANIO + " >= " +
                        gestAgeCalc, null, null, null, null);
                myC.moveToFirst();

                fetalWeight.setPESO1_P1(Integer.parseInt(myC.getString(0)));
                fetalWeight.setPESO1_P5(Integer.parseInt(myC.getString(1)));
                fetalWeight.setPESO1_P9(Integer.parseInt(myC.getString(2)));

                generalMeasure.setPICOSISTACM15MM(Integer.parseInt(myC.getString(3)));
                generalMeasure.setILA05(Integer.parseInt(myC.getString(4)));
                generalMeasure.setILA95(Integer.parseInt(myC.getString(5)));
                generalMeasure.setARTUMBIP05(Float.parseFloat(myC.getString(6)));
                generalMeasure.setARTUMBIP90(Float.parseFloat(myC.getString(7)));
                generalMeasure.setARTCERMEDIP05(Float.parseFloat(myC.getString(8)));
                generalMeasure.setARTCERMEDIP90(Float.parseFloat(myC.getString(9)));

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            if (gestAgeCalc > 17) {
                result += "\nEstimated weight:" + fetalWeight.getPESO1_P5() + "g ("
                        + fetalWeight.getPESO1_P1() + "g to " + fetalWeight.getPESO1_P9() + "g) ";
            }
            if (gestAgeCalc > 15) {
                result += "\n1.5 MM Peak Sist ACM: " + generalMeasure.getPICOSISTACM15MM() + "\n"+
                          "Amniotic Liquid Index: " + generalMeasure.getILA05()+
                          " to " + generalMeasure.getILA95();
            }
            if (gestAgeCalc > 20) {
                result += "\nRI A.Umb: " + formatFloat(generalMeasure.getARTUMBIR05())+
                          " to " + formatFloat(generalMeasure.getARTUMBIR90())+ " \n"+
                          "PI ACM: " + formatFloat(generalMeasure.getARTCERMEDIP05())+
                          " to " + formatFloat(generalMeasure.getARTCERMEDIP90());
            }
            result += "\n";
        } else if (gestAgeCalc < 5){
            result = "";
        }

        return result;
    }

    public int getGestAgeAver(){
        setGestAgeAver();

        return gestAgeAver;
    }

    private void setGestAgeAver () {
        gestAgeAver = 0;
        int cnt = 0;
        if (bpd > 0) {
            cnt ++;
            gestAgeAver += bpdGA;
        };
        if (ofd > 0) {
            cnt ++;
            gestAgeAver += ofdGA;
        };
        if (cCef > 0) {
            cnt ++;
            gestAgeAver += cCefGA;
        };
        if (cAbd > 0) {
            cnt ++;
            gestAgeAver += cAbdGA;
        };
        if (fem > 0) {
            cnt ++;
            gestAgeAver += femGA;
        };
        if (ume > 0) {
            cnt ++;
            gestAgeAver += umeGA;
        };
        if (cnt > 0) {
            gestAgeAver = Math.round(gestAgeAver / cnt);
        }
    }

    private String formatDate(Date day){
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        return df.format("dd-MM-yyyy", day).toString();
    }

    private String formatFloat(float val){
        return String.format("%.2f", val);
    }

    private String formatFloat(double val){
        return String.format("%.2f", val);
    }

/*
    public Cursor getAgeGest (SQLiteDatabase db, String valorMed, String tableName, String fieldConstr, String fieldSelect ){
        String[] select = {fieldSelect};
        Cursor cursorIG = db.query(tableName, select, fieldConstr + " >= " +
                valorMed, null,null, null, null);
//         cursorIG.moveToFirst();
        return cursorIG;
    }

    public Cursor getMedGestAge(SQLiteDatabase db, int valorMed, String tableName, String fieldConstr, String[] fieldSelect ){
        Cursor cursorIG = db.query(tableName, fieldSelect, fieldConstr + " >= " +
                valorMed+ " AND "+fieldSelect+" >= "+, null,null, null, null);
//         cursorIG.moveToFirst();
        return cursorIG;
    }

    public String CheckILA(int idGest) throws Exception {
        String result = "";
        Pregnancy item = new Pregnancy();
        item.setCode(idGest);

        item = select(item);
        if(item.getObstetricIndex().getCode() > 0) {
            result = "Amniotic Liquid Index: " + item.getObstetricIndex().getILA50()
                    + " (" + item.getObstetricIndex().getILA5() + " to " + item.getObstetricIndex().getILA95() + ") \n";
        }
        return result;
    }

    public String CheckWeight(int idGest) throws Exception {
        String result = "";
        Pregnancy item = new Pregnancy();
        item.setCode(idGest);

        item = select(item);
        if(item.getFetalWeight().getCode() > 0) {
            result = "Estimated weight:" + item.getFetalWeight().getPESO1_P5()
                    + "g (" + item.getFetalWeight().getPESO1_P1() + "g to "
                    + item.getFetalWeight().getPESO1_P9() + "g) \n";
        }
        return result;
    }

    public String CheckACMPeak(int idGest) throws Exception {
        String result = "";
        Pregnancy item = new Pregnancy();
        item.setCode(idGest);

        item = select(item);
        if(item.getGeneralMeasure().getCode() > 0) {
            result = "Cerebral Median Artery (1.5x peak):" + item.getGeneralMeasure().getPICOSISTACM15MM() + "\n";
        }

        return result;
    }
*/
}
