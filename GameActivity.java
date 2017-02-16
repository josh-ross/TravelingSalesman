package com.example.joshross.myfirstapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView.setSpinVal(getIntent().getIntExtra("spinVal", 4));
        setContentView(new GameView(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tools, menu);
        return true;
    }

    //Make a method to handle a button press for: Undo, Clear, Quit
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_undo:
                if(GameView.undo()) {
                    Toast.makeText(this, "Click Anywhere On The Screen To Undo The Last Line", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "There Are No Lines To Undo", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_clear:
                if(GameView.clear()) {
                    Toast.makeText(this, "Click Anywhere On The Screen To Clear All Lines", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "There Are No Lines To Clear", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.action_quit:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
