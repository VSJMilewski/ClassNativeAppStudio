package com.nativeappstudio.milewski_10529136.four_in_a_row;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Random;

public class MenuActivity extends AppCompatActivity {

    //An int array with a few color numbers stored in it
    private static int[] COLORS = {Color.RED, Color.GREEN, Color.GRAY, Color.BLUE, Color.LTGRAY, Color.WHITE, Color.CYAN};

    //A random generator
    private Random RAND = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //add a listener for both buttons
        Button twoPlyr = (Button) findViewById(R.id.button2ply);
        Button onePc = (Button) findViewById(R.id.button1pc);
        twoPlyr.setOnClickListener(nextActivityListener);
        onePc.setOnClickListener(nextActivityListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
        } else if (id == R.id.action_background) {
            LinearLayout l = (LinearLayout) findViewById(R.id.mainLinear);
            int max = COLORS.length;
            int color = COLORS[RAND.nextInt(max)]; //The color is selected from the array
            l.setBackgroundColor(color);

        }
        return super.onOptionsItemSelected(item);
    }

    //the click listener for both buttons. It starts the game for 2 players ore one player
    //depending on the prest button
    private View.OnClickListener nextActivityListener = new View.OnClickListener() {
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.button2ply:
                    //start game for two players
                    startActivity(new Intent(MenuActivity.this, FourRowActivity.class));
                    break;
                case R.id.button1pc:
                    //start game with an bot
                    Intent intent = new Intent(MenuActivity.this, FourRowActivity.class);
                    intent.putExtra("pc",true);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
}
