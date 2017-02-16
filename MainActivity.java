package com.example.joshross.myfirstapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    protected Button play_button;
    protected Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Look for the PLAY button and then store it in this variable
        play_button = (Button) findViewById(R.id.play_button);
        spinner = (Spinner) findViewById(R.id.spinner);
        new SpinnerActivity(spinner);
    }

    //Create a method to handle onClick when the PLAY button is pressed
    public void onButtonPress(View view) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("spinVal", SpinnerActivity.getSpinVal());
        startActivity(i);
    }

}