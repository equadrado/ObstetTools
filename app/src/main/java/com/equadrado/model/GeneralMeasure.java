package com.equadrado.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by equadrado on 2016-10-31.
 */

public class GeneralMeasure {
    private int DBP2;
    private int DOF1;
    private int CC2;
    private int CA1;
    private int FEM1;
    private int UMERO1;
    private double ARTUMBIR05;
    private double ARTUMBIR90;
    private double ARTUMBIP05;
    private double ARTUMBIP90;
    private double ARTCERMEDIP05;
    private double ARTCERMEDIP90;
    private double RELIPCERUMB05;
    private double RELIPCERUMB90;
    private int ILA05;
    private int ILA95;
    private int PICOSISTACM15MM;

    private int MIN_DBP2;
    private int MIN_DOF1;
    private int MIN_CC2;
    private int MIN_CA1;
    private int MIN_FEM1;
    private int MIN_UMERO1;


    DBConnection dbConn;

    public GeneralMeasure(DBConnection dbConn) {
        this.dbConn = dbConn;

        SQLiteDatabase db = this.dbConn.getReadableDatabase();

        // create minimal values for all parameters
        Cursor myC = dbConn.getMinValMed(db, 0, DBConnection.TABLE_NAME_GO_GERAL, DBConnection.UMERO);
        myC.moveToFirst();
        MIN_UMERO1 = Integer.parseInt(myC.getString(0));

        myC = dbConn.getMinValMed(db, 0, DBConnection.TABLE_NAME_GO_GERAL, DBConnection.FEM);
        myC.moveToFirst();
        MIN_FEM1 = Integer.parseInt(myC.getString(0));

        myC = dbConn.getMinValMed(db, 0, DBConnection.TABLE_NAME_GO_GERAL, DBConnection.DBP);
        myC.moveToFirst();
        MIN_DBP2 = Integer.parseInt(myC.getString(0));

        myC = dbConn.getMinValMed(db, 0, DBConnection.TABLE_NAME_GO_GERAL, DBConnection.DOF);
        myC.moveToFirst();
        MIN_DOF1 = Integer.parseInt(myC.getString(0));

        myC = dbConn.getMinValMed(db, 0, DBConnection.TABLE_NAME_GO_GERAL, DBConnection.CA);
        myC.moveToFirst();
        MIN_CA1 = Integer.parseInt(myC.getString(0));

        myC = dbConn.getMinValMed(db, 0, DBConnection.TABLE_NAME_GO_GERAL, DBConnection.CC);
        myC.moveToFirst();
        MIN_CC2 = Integer.parseInt(myC.getString(0));

    }

    public int getMIN_DBP2() {
        return MIN_DBP2;
    }

    public int getMIN_DOF1() {
        return MIN_DOF1;
    }

    public int getMIN_CC2() {
        return MIN_CC2;
    }

    public int getMIN_CA1() {
        return MIN_CA1;
    }

    public int getMIN_FEM1() {
        return MIN_FEM1;
    }

    public int getMIN_UMERO1() {
        return MIN_UMERO1;
    }

    public int getDBP2() {
        return DBP2;
    }

    public void setDBP2(int DBP2) {
        this.DBP2 = DBP2;
    }

    public int getDOF1() {
        return DOF1;
    }

    public void setDOF1(int DOF1) {
        this.DOF1 = DOF1;
    }

    public int getCC2() {
        return CC2;
    }

    public void setCC2(int CC2) {
        this.CC2 = CC2;
    }

    public int getCA1() {
        return CA1;
    }

    public void setCA1(int CA1) {
        this.CA1 = CA1;
    }

    public int getFEM1() {
        return FEM1;
    }

    public void setFEM1(int FEM1) {
        this.FEM1 = FEM1;
    }

    public int getUMERO1() {
        return UMERO1;
    }

    public void setUMERO1(int UMERO1) {
        this.UMERO1 = UMERO1;
    }

    public double getARTUMBIR05() {
        return ARTUMBIR05;
    }

    public void setARTUMBIR05(double ARTUMBIR05) {
        this.ARTUMBIR05 = ARTUMBIR05;
    }

    public double getARTUMBIR90() {
        return ARTUMBIR90;
    }

    public void setARTUMBIR90(double ARTUMBIR90) {
        this.ARTUMBIR90 = ARTUMBIR90;
    }

    public double getARTUMBIP05() {
        return ARTUMBIP05;
    }

    public void setARTUMBIP05(double ARTUMBIP05) {
        this.ARTUMBIP05 = ARTUMBIP05;
    }

    public double getARTUMBIP90() {
        return ARTUMBIP90;
    }

    public void setARTUMBIP90(double ARTUMBIP90) {
        this.ARTUMBIP90 = ARTUMBIP90;
    }

    public double getARTCERMEDIP05() {
        return ARTCERMEDIP05;
    }

    public void setARTCERMEDIP05(double ARTCERMEDIP05) {
        this.ARTCERMEDIP05 = ARTCERMEDIP05;
    }

    public double getARTCERMEDIP90() {
        return ARTCERMEDIP90;
    }

    public void setARTCERMEDIP90(double ARTCERMEDIP90) {
        this.ARTCERMEDIP90 = ARTCERMEDIP90;
    }

    public double getRELIPCERUMB05() {
        return RELIPCERUMB05;
    }

    public void setRELIPCERUMB05(double RELIPCERUMB05) {
        this.RELIPCERUMB05 = RELIPCERUMB05;
    }

    public double getRELIPCERUMB90() {
        return RELIPCERUMB90;
    }

    public void setRELIPCERUMB90(double RELIPCERUMB90) {
        this.RELIPCERUMB90 = RELIPCERUMB90;
    }

    public int getILA05() { return ILA05; }

    public void setILA05(int ILA05) {
        this.ILA05 = ILA05;
    }

    public int getILA95() {
        return ILA95;
    }

    public void setILA95(int ILA95) {
        this.ILA95 = ILA95;
    }

    public int getPICOSISTACM15MM() {
        return PICOSISTACM15MM;
    }

    public void setPICOSISTACM15MM(int PICOSISTACM15MM) {
        this.PICOSISTACM15MM = PICOSISTACM15MM;
    }

}
