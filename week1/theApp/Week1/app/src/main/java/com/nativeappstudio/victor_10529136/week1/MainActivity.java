package com.nativeappstudio.victor_10529136.week1;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private static int[] COLORS = {Color.RED, Color.GREEN, Color.GRAY, Color.BLUE, Color.LTGRAY, Color.WHITE, Color.CYAN};

    private Random RAND = new Random();
    private int NUMBER;
    private int COUNT = 0;
    private int BEST = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prepareGame();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public void changeBackground(View view) {

        LinearLayout l = (LinearLayout) findViewById(R.id.mainLinear);
        int max = COLORS.length;
        int color = COLORS[RAND.nextInt(max)];
        l.setBackgroundColor(color);
    }

    public void prepareGame(){
        TextView mainText = (TextView) findViewById(R.id.textBox);
        TextView textCount = (TextView) findViewById(R.id.textCount);
        TextView textBest = (TextView) findViewById(R.id.textBest);
        String startText = getString(R.string.startText);
        String count = getString(R.string.count);
        String best = getString(R.string.best);
        mainText.setText(startText);
        String tmp = count+COUNT;
        textCount.setText(tmp);
        NUMBER = RAND.nextInt(100);
        COUNT = 0;
        tmp = best+BEST;
        textBest.setText(tmp);
    }

    public void testNumber(View view){
        TextView mainText = (TextView) findViewById(R.id.textBox);
        TextView textCount = (TextView) findViewById(R.id.textCount);
        String startText = getString(R.string.startText);
        String count = getString(R.string.count);
        EditText field = (EditText) findViewById(R.id.textField);

        String text = field.getText().toString();
        if (text.matches("")) {
            Toast.makeText(this, "You did not enter a number", Toast.LENGTH_LONG).show();
        } else {
            int input = Integer.parseInt(text);
            COUNT++;
            String tmp = count + COUNT;
            textCount.setText(tmp);
            if (input < NUMBER) {
                mainText.setText(getString(R.string.toLow));
            } else if (input > NUMBER) {
                mainText.setText(getString(R.string.toHigh));
            } else {
                if (BEST == 0) {
                    BEST = COUNT;
                } else if (COUNT < BEST) {
                    BEST = COUNT;
                }
                prepareGame();
                mainText.setText(getString(R.string.end));
            }
        }
    }
}


