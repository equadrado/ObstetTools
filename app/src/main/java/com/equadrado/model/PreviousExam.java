package com.equadrado.model;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by equadrado on 2016-11-03.
 */

public class PreviousExam {
    private final String TAG = "PreviousExamClass";

    private Date examDate;

    private int resultWeek;
    private int resultDay;
    private Date lmpExame;
    private int gestAgeWeekExame;
    private int gestAgeDayExame;

    public PreviousExam(Date examDate, int resultWeek, int resultDay){
        setExamDate(examDate, resultWeek, resultDay);

        Log.i(TAG, "PreviousExam created");
    }

    private int intervalDays(Date d1, Date d2) {
        int result = (int) ((d1.getTime() - d2.getTime()) / 86400000L); //
        return result < 0 ? result * -1 : result;
    }


    public Date getExamDate() {
        return examDate;
    }

    public int getResultWeek() {
        return resultWeek;
    }

    public int getResultDay() {
        return resultDay;
    }

    public Date getLmpExame() {
        return lmpExame;
    }

    public int getGestAgeWeekExame() {
        return gestAgeWeekExame;
    }

    public int getGestAgeDayExame() {
        return gestAgeDayExame;
    }

    public void setExamDate(Date examDate, int resultWeek, int resultDay) {
        setResultWeek(resultWeek);

        setResultDay(resultDay);

        this.examDate = examDate;

        setLmpExame();
    }

    private void setResultWeek(int resultWeek) {
        if (resultWeek > 40 ) resultWeek = 40;

        this.resultWeek = resultWeek;
    }

    private void setResultDay(int resultDay) {
        if (resultDay > 6 ) resultDay = 6;

        this.resultDay = resultDay;
    }

    private void setLmpExame() {
        // calculate lpmExame
        Calendar day = Calendar.getInstance();
        day.setTime(examDate);
        int days = -1 * (7 * resultWeek + resultDay);
        day.add(Calendar.DATE, days);

        this.lmpExame = day.getTime();

        Date today = new Date();
        int nTotal = intervalDays(today, this.lmpExame);
        int nDays = (nTotal % 7);
        nTotal = (int) (nTotal - nDays) / 7;

        this.gestAgeWeekExame = nTotal;
        this.gestAgeDayExame = nDays;
    }
}
