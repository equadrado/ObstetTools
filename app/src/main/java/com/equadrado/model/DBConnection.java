package com.equadrado.model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Path;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by equadrado on 2016-10-31.
 */

public class DBConnection extends SQLiteOpenHelper {
    int actualVer = 1;
    private static String TAG = "DBConnection"; // Tag just for the LogCat window

    public static final String TABLE_NAME_GO_GERAL = "TbGOMedidasGerais";
    public static final String IG_CRANIO = "IdadeGestacional";
    public static final String DBP = "DBP2";
    public static final String DOF = "DOF1";
    public static final String CC = "CC2";
    public static final String CA = "CA1";
    public static final String FEM = "FEM1";
    public static final String UMERO = "UMERO1";
    public static final String IRUMB05 = "ArtUmbIR05";
    public static final String IRUMB90 = "ArtUmbIR90";
    public static final String IPACM05 = "ArtCerMedIP05";
    public static final String IPACM90 = "ArtCerMedIP90";
    public static final String RELIPCERUMB05 = "RelIPCerUmb05";
    public static final String RELIPCERUMB90 = "RelIPCerUmb90";
    public static final String PESO10 = "Peso10";
    public static final String PESO50 = "Peso50";
    public static final String PESO90 = "Peso90";
    public static final String ILA05 = "ILA05";
    public static final String ILA95 = "ILA95";
    public static final String PSACM15MM = "PicoSistACM15MM";
    public static final String TABLE_NAME_MF_SISTEMAS = "MFSistema";
    public static final String CODSISTEMA = "CodSistema";
    public static final String NOMESISTEMA = "NomeSistema";

    // Android's folder for all your databases. You only have to change
    private static String DB_PATH = ""; // "/data/data/com.equadrado.obstettools/databases/";
    private static File FILE_PATH;

    // Name of the database file
    private static String DB_NAME = "ObstetDB.sqlite";

    // Internal variables
    private SQLiteDatabase dbHandle;
    private final Context dbContext;
    private Cursor cursor;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DBConnection(Context context) {
        super(context, DB_NAME, null, 1);
        //FILE_PATH = context.getDatabasePath(DBConnection.DB_NAME);
        //DB_PATH = FILE_PATH.getAbsolutePath();
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.dbContext = context.getApplicationContext();

        Log.i(TAG, "Connection created");
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        //If the database does not exist, copy it from the assets.

        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist)
        {
            this.getReadableDatabase();
            this.close();
            try
            {
                //Copy the database from assests
                copyDataBase();
                Log.i(TAG, "Database created");
            }
            catch (IOException mIOException)
            {
                Log.e(TAG, "Error on create Database: "+mIOException.getMessage());
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase(){
        File dbFile = new File(DB_PATH + DB_NAME);

        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
        InputStream mInput = dbContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0)
        {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();

        Log.i(TAG, "Database copied");
    }

    public boolean openDataBase() throws SQLException {
        // Open the database
        String mPath = DB_PATH + DB_NAME;
        try{
            //Log.v("mPath", mPath);
            dbHandle = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        } catch (Exception ex){
            Log.e("DBConnection", ex.getMessage());
            throw new Error("ErrorCopyingDataBase");
        }
        return dbHandle != null;
    }

    @Override
    public synchronized void close() {
        if (dbHandle != null)
            dbHandle.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase dbHandle) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase dbHandle, int oldVersion, int newVersion) {
        // update database
        if (actualVer < newVersion){
            // ...
            Log.i(TAG, "Database updated");

            // update actualVersion
            actualVer = newVersion;
        }
    }

    public Cursor searchData(String constrainct, int type, int gender ) {
        try {
            String constr1 = "";
            String constr2 = "";

            if (type == 0) {
                constr1 = "Descricao";
            } else {
                constr1 = "CodCID10";
            }

            if (gender != 0) {
                if (gender == 1) {
                    constr2 = " AND RestricaoSexo = 1";
                } else {
                    constr2 = " AND RestricaoSexo = 3";
                }
            } else {
                constr2 = "";
            }

            cursor = dbHandle.query("TbCadastroCID10", new String[] {"Descricao"},
                    constr1+" like " + "'%" + constrainct + "%'"+constr2, null, null, null, null);

            int numeroRegistro = cursor.getCount();
            if (numeroRegistro == 0) {
                showMessage("List is empty");
            } else {
                return cursor;
            }

        } catch (Exception e) {
            showMessage("Error in search data" + e.getMessage());
        }
        return cursor;
    }

    private void showMessage (String msg)
    {
        Toast.makeText (dbContext, msg, Toast.LENGTH_SHORT).show ();
    }

    public String[] listTables(){
        Cursor c = dbHandle.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        List<String> result = new ArrayList<>();
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                result.add(c.getString(0));
                //Log.i("Pregnancy",  "Table Name=> "+c.getString(0));
                c.moveToNext();
            }
        }

        return result.toArray(new String[result.size()]);
    }

    public Cursor getGestAge(SQLiteDatabase db, String valueOfConstric, String tableName, String fieldNameConstric, String fieldListNames, int minValue ){
        String[] listOfFields = {fieldListNames};
        String query;
        Cursor resCursor = null;
        try {
            if (Double.parseDouble(valueOfConstric) >= minValue)
                query = fieldNameConstric + " >= " + valueOfConstric+" AND "+fieldNameConstric+" >= "+minValue;
            else
                query = fieldNameConstric + " >= " + valueOfConstric+" AND "+fieldNameConstric+" < 0";

            resCursor = db.query(tableName, listOfFields, query, null, null, null, null);
        } catch (Exception e){
            Log.e("DBConnection", e.getMessage());
        }

        return resCursor;
    }

    public Cursor getMedGestAge (SQLiteDatabase db, int valueOfConstric, String tableName, String fieldNameConstric, String[] listOfFields ){
        Cursor resCursor = null;
        try {
            resCursor = db.query(tableName, listOfFields, fieldNameConstric + " >= " +
                    valueOfConstric, null, null, null, null);
        } catch (Exception e){
            Log.e("DBConnection", e.getMessage());
        }
        return resCursor;
    }

    public Cursor getMinValMed (SQLiteDatabase db, int valueOfConstric, String tableName, String fieldNameConstric){
        String[] fieldConstr = {fieldNameConstric};
        Cursor resCursor = null;
        try {
            resCursor = db.query(tableName, fieldConstr, fieldNameConstric + " > " +
                    valueOfConstric, null, null, null, null);
        } catch (Exception e) {
            Log.e("DBConnection", e.getMessage());
        }
        return resCursor;
    }


    private String sqlCreateDB(){
        String result = "DROP TABLE DHImages CASCADE CONSTRAINTS;" +
                "DROP TABLE TbGOIndicesObstetricos CASCADE CONSTRAINTS;" +
                "DROP TABLE TbGOPesoFetal CASCADE CONSTRAINTS;" +
                "DROP TABLE TbGOMedidasGerais CASCADE CONSTRAINTS;" +
                "CREATE TABLE TbGOIndicesObstetricos (" +
                "    IdadeGestacional NUMBER(5) NOT NULL PRIMARY KEY," +
                "    DBPDOF           NUMBER(5)," +
                "    FEMDBP           NUMBER(5)," +
                "    FEMCC            NUMBER(5)," +
                "    FEMCA            NUMBER(5)," +
                "    ILA2             NUMBER(5)," +
                "    ILA5             NUMBER(5)," +
                "    ILA50            NUMBER(5)," +
                "    ILA95            NUMBER(5)," +
                "    ILA98            NUMBER(5)" +
                ");" +
                "CREATE TABLE TbGOMedidasGerais (" +
                "    IdadeGestacional NUMBER(5) NOT NULL PRIMARY KEY," +
                "    DBP2             NUMBER(5)," +
                "    DOF1             NUMBER(5)," +
                "    CC2              NUMBER(5)," +
                "    CA1              NUMBER(5)," +
                "    FEM1             NUMBER(5)," +
                "    UMERO1           NUMBER(5)," +
                "    ArtUmbIR05       NUMBER(5,2)," +
                "    ArtUmbIR90       NUMBER(5,2)," +
                "    ArtUmbIP05       NUMBER(5,2)," +
                "    ArtUmbIP90       NUMBER(5,2)," +
                "    ArtCerMedIP05    NUMBER(5,2)," +
                "    ArtCerMedIP90    NUMBER(5,2)," +
                "    RelIPCerUmb05    NUMBER(5,2)," +
                "    RelIPCerUmb90    NUMBER(5,2)," +
                "    Peso10           NUMBER(5)," +
                "    Peso50           NUMBER(5)," +
                "    Peso90           NUMBER(5)," +
                "    ILA05            NUMBER(5)," +
                "    ILA95            NUMBER(5)," +
                "    PicoSistACM15MM  NUMBER(5)" +
                ");" +
                "CREATE TABLE TbGOPesoFetal (" +
                "    IdadeGestacional NUMBER(5) NOT NULL PRIMARY KEY," +
                "    PESO1_P1         NUMBER(5)," +
                "    PESO1_P5         NUMBER(5)," +
                "    PESO1_P9         NUMBER(5)" +
                ");" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (14,82,58,15,19, null, null, null, null,null);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (15,81,59,16,19, null, null, null, null,null);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (16,80,61,16,20,73,79,121,185,201);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (17,80,63,17,20,77,83,127,194,211);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (18,80,65,18,21,80,87,133,202,220);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (19,79,67,18,21,83,90,137,207,225);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (20,79,69,18,21,86,93,141,212,230);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (21,79,70,19,22,88,95,143,214,233);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (22,78,77,19,22,89,97,145,216,235);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (23,78,78,19,22,90,98,146,218,237);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (24,78,78,19,22,90,98,147,219,238);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (25,78,78,19,22,89,97,147,221,240);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (26,78,78,19,22,89,97,147,223,242);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (27,78,78,20,22,85,95,146,226,245);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (28,78,79,20,22,86,94,146,228,249);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (29,78,79,20,22,84,92,145,231,254);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (30,78,79,20,22,82,90,145,234,258);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (31,78,79,20,22,79,88,144,238,263);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (32,78,79,21,22,77,86,144,242,269);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (33,78,80,21,22,74,83,143,245,274);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (34,78,80,21,22,72,81,142,248,278);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (35,78,80,21,22,70,79,140,249,279);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (36,78,80,22,22,68,77,138,249,279);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (37,78,80,22,22,66,75,135,244,275);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (38,78,81,22,22,65,73,132,239,269);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (39,78,81,22,22,64,72,127,226,255);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (40,78,81,22,22,63,71,123,214,240);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (41,null,null,null,null,63,70,116,194,216);" +
                "INSERT INTO TbGOIndicesObstetricos VALUES (42,null,null,null,null,63,69,110,175,192);" +
                "COMMIT;" +
                "INSERT INTO TbGOPesoFetal VALUES (18, 185, 223, 261);" +
                "INSERT INTO TbGOPesoFetal VALUES (19, 227, 273, 319);" +
                "INSERT INTO TbGOPesoFetal VALUES (20, 275, 331, 387);" +
                "INSERT INTO TbGOPesoFetal VALUES (21, 331, 399, 467);" +
                "INSERT INTO TbGOPesoFetal VALUES (22, 398, 478, 559);" +
                "INSERT INTO TbGOPesoFetal VALUES (23, 471, 568, 665);" +
                "INSERT INTO TbGOPesoFetal VALUES (24, 556, 670, 784);" +
                "INSERT INTO TbGOPesoFetal VALUES (25, 652, 785, 918);" +
                "INSERT INTO TbGOPesoFetal VALUES (26, 758, 913, 1068);" +
                "INSERT INTO TbGOPesoFetal VALUES (27, 876, 1055, 1234);" +
                "INSERT INTO TbGOPesoFetal VALUES (28, 1004, 1210, 1416);" +
                "INSERT INTO TbGOPesoFetal VALUES (29, 1145, 1379, 1613);" +
                "INSERT INTO TbGOPesoFetal VALUES (30, 1294, 1559, 1824);" +
                "INSERT INTO TbGOPesoFetal VALUES (31, 1453, 1751, 2049);" +
                "INSERT INTO TbGOPesoFetal VALUES (32, 1621, 1953, 2285);" +
                "INSERT INTO TbGOPesoFetal VALUES (33, 1794, 2162, 2530);" +
                "INSERT INTO TbGOPesoFetal VALUES (34, 1973, 2377, 2781);" +
                "INSERT INTO TbGOPesoFetal VALUES (35, 2154, 2595, 3036);" +
                "INSERT INTO TbGOPesoFetal VALUES (36, 2335, 2813, 3291);" +
                "INSERT INTO TbGOPesoFetal VALUES (37, 2513, 3028, 3543);" +
                "INSERT INTO TbGOPesoFetal VALUES (38, 2686, 3236, 3786);" +
                "INSERT INTO TbGOPesoFetal VALUES (39, 2851, 3435, 4019);" +
                "COMMIT;" +
                "INSERT INTO TbGOMedidasGerais VALUES (12,20,38,70,46,7,9,0,0,0,0,0,0,0,0,0,0,0,0,0,0);" +
                "INSERT INTO TbGOMedidasGerais VALUES (13,23,38,89,60,11,11,0,0,0,0,0,0,0,0,0,0,0,0,0,0);" +
                "INSERT INTO TbGOMedidasGerais VALUES (14,27,39,98,73,14,14,0,0,0,0,0,0,0,0,0,0,0,0,0,0);" +
                "INSERT INTO TbGOMedidasGerais VALUES (15,30,42,111,86,17,17,0,0,0,0,0,0,0,0,0,0,0,0,0,0);" +
                "INSERT INTO TbGOMedidasGerais VALUES (16,34,46,124,99,20,20,0,0,0,0,0,0,0,0,0,0,0,79,185,32);" +
                "INSERT INTO TbGOMedidasGerais VALUES (17,38,50,137,112,24,22,0,0,0,0,0,0,0,0,0,0,0,83,194,33);" +
                "INSERT INTO TbGOMedidasGerais VALUES (18,41,54,150,125,27,25,0,0,0,0,0,0,0,0,185,223,261,87,202,35);" +
                "INSERT INTO TbGOMedidasGerais VALUES (19,45,58,163,137,30,28,0,0,0,0,0,0,0,0,227,273,319,90,207,36);" +
                "INSERT INTO TbGOMedidasGerais VALUES (20,48,62,175,150,33,30,0.66,0.86,1.04,2.03,1.36,2.31,4.17,0.9,275,331,387,93,212,38);" +
                "INSERT INTO TbGOMedidasGerais VALUES (21,51,67,187,162,35,33,0.65,0.85,0.98,1.96,1.4,2.34,4.35,0.91,331,399,467,95,214,40);" +
                "INSERT INTO TbGOMedidasGerais VALUES (22,54,71,199,174,38,35,0.64,0.84,0.92,1.9,1.44,2.37,4.55,0.92,398,478,559,97,216,42);" +
                "INSERT INTO TbGOMedidasGerais VALUES (23,58,75,210,185,41,38,0.63,0.83,0.86,1.85,1.47,2.4,4.76,0.93,471,568,665,98,218,44);" +
                "INSERT INTO TbGOMedidasGerais VALUES (24,60,79,221,197,44,40,0.62,0.82,0.81,1.79,1.49,2.42,5,0.93,556,670,784,98,219,46);" +
                "INSERT INTO TbGOMedidasGerais VALUES (25,63,83,232,208,46,42,0.61,0.81,0.76,1.74,1.51,2.44,5,0.93,652,785,918,97,221,48);" +
                "INSERT INTO TbGOMedidasGerais VALUES (26,66,87,242,219,49,44,0.6,0.8,0.67,1.69,1.52,2.45,5.26,0.94,758,913,1068,97,223,50);" +
                "INSERT INTO TbGOMedidasGerais VALUES (27,69,91,252,230,51,46,0.59,0.79,0.63,1.65,1.53,2.45,5.56,0.95,876,1055,1234,95,226,53);" +
                "INSERT INTO TbGOMedidasGerais VALUES (28,71,95,262,240,54,48,0.58,0.78,0.59,1.61,1.53,2.46,5.88,0.96,1004,1210,1416,94,228,55);" +
                "INSERT INTO TbGOMedidasGerais VALUES (29,74,98,271,251,56,50,0.57,0.77,0.56,1.57,1.53,2.45,6.25,0.97,1145,1379,1613,92,231,58);" +
                "INSERT INTO TbGOMedidasGerais VALUES (30,76,102,280,261,58,51,0.56,0.76,0.53,1.54,1.52,2.44,6.67,0.98,1294,1559,1824,90,234,61);" +
                "INSERT INTO TbGOMedidasGerais VALUES (31,79,105,289,271,60,53,0.55,0.75,0.5,1.51,1.51,2.43,6.67,0.98,1453,1751,2049,88,238,64);" +
                "INSERT INTO TbGOMedidasGerais VALUES (32,81,107,297,281,62,55,0.54,0.74,0.48,1.48,1.49,2.41,7.14,0.99,1621,1953,2285,86,242,67);" +
                "INSERT INTO TbGOMedidasGerais VALUES (33,83,110,304,291,64,56,0.53,0.73,0.46,1.46,1.46,2.39,7.69,1,1794,2162,2530,83,245,70);" +
                "INSERT INTO TbGOMedidasGerais VALUES (34,85,112,312,300,66,58,0.52,0.72,0.44,1.44,1.43,2.36,8.33,1.01,1973,2377,2781,81,248,73);" +
                "INSERT INTO TbGOMedidasGerais VALUES (35,87,113,318,309,68,59,0.51,0.71,0.43,1.43,1.4,2.32,9.09,1.02,2154,2595,3036,79,249,77);" +
                "INSERT INTO TbGOMedidasGerais VALUES (36,89,115,325,318,70,61,0.5,0.7,0.42,1.42,1.36,2.28,10,1.03,2335,2813,3291,77,249,80);" +
                "INSERT INTO TbGOMedidasGerais VALUES (37,91,116,330,327,72,62,0.49,0.69,0.42,1.41,1.32,2.24,10,1.04,2513,3028,3543,75,244,84);" +
                "INSERT INTO TbGOMedidasGerais VALUES (38,92,117,336,336,74,63,0.47,0.67,0.42,1.4,1.27,2.19,11.11,1.04,2686,3236,3786,73,239,88);" +
                "INSERT INTO TbGOMedidasGerais VALUES (39,94,118,341,344,75,65,0.46,0.66,0.42,1.4,1.21,2.14,12.5,1.05,2851,3435,4019,72,226,92);" +
                "INSERT INTO TbGOMedidasGerais VALUES (40,95,119,345,353,77,66,0.45,0.65,0.42,1.4,1.12,2.08,14.29,1.06,3004,3619,4524,71,214,96);" +
                "COMMIT;";

        return result;
    }
}
