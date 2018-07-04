package com.equadrado.controler;

import android.content.Context;
import android.util.Log;

import com.equadrado.model.DBConnection;
import com.equadrado.model.Pregnancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by equadrado on 2016-10-31.
 */

public class PregnancyController implements Serializable {
    private final String TAG = "PregnancyControllerClas";

    private List<Pregnancy> data;

    public PregnancyController(Context ctx) {
        this.data = new ArrayList<>();

        Log.i(TAG, "PregnancyController created");
    }

    public Pregnancy addPregnancy(DBConnection db) {
        Pregnancy preg = new Pregnancy(db);

        if (this.data.size() > 10 ) {
            this.data.remove(0);
        }
        this.data.add(preg);

        return preg;
    }

    public int count(){
        return this.data.size();
    }


    // Controller Methods
    public List<Pregnancy> list() throws Exception {
        return this.data;
    }

}
