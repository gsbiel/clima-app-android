package com.example.climaapp;

import android.os.AsyncTask;

import com.example.climaapp.interfaces.TimeOutDelegate;

public class TimeOut extends AsyncTask<Integer, Integer, Integer> {

    int time = 200; // ms
    private TimeOutDelegate delegate;

    TimeOut(int time, TimeOutDelegate activity){
        this.time = time;
        this.delegate = activity;
    }

    @Override
    protected Integer doInBackground(Integer... integers) {
        try{
            Thread.sleep(this.time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return integers[0];
    }

    @Override
    protected void onPostExecute(Integer aInt) {
        super.onPostExecute(aInt);
        this.delegate.didFinishTimer(aInt);
    }
}
