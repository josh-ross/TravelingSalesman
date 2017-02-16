package com.example.joshross.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class SpinnerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected static int spinVal;

    public SpinnerActivity(Spinner spinner) {
        spinVal = 4;
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinVal = Integer.parseInt(parent.getItemAtPosition(position).toString());
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static int getSpinVal() {
        return spinVal;
    }
}
