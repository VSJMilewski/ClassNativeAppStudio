package com.nativeappstudio.victor_10529136.week1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //An int array with a few color numbers stored in it
    private static int[] COLORS = {Color.RED, Color.GREEN, Color.GRAY, Color.BLUE, Color.LTGRAY, Color.WHITE, Color.CYAN};

    //A random generator used at a few locations
    private Random RAND = new Random();
    //The variable where the number for each game is stored in
    private int NUMBER;
    //Counts the number of guesses the player has made
    private int COUNT = 0;
    //Keeps track of the best score
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
    * changeBackground(View)
    * When this method is called by the button,
    * a random color will be selected an set as
    * new background color
     */
    public void changeBackground(View view) {

        LinearLayout l = (LinearLayout) findViewById(R.id.mainLinear);
        int max = COLORS.length;
        int color = COLORS[RAND.nextInt(max)]; //The color is selected from the array
        l.setBackgroundColor(color);
    }

    /*
    * The Game is prepared. The text fields get filled inm
    * a random number for the game is generated
    * and the counter is set to zero
     */
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

    /*
    * When the button to enter a number is pressed.
    * The number in the edittext is tested if it is the
    * right number. Otherwise the right message is printed
     */
    public void testNumber(View view){
        TextView mainText = (TextView) findViewById(R.id.textBox);
        TextView textCount = (TextView) findViewById(R.id.textCount);
        String startText = getString(R.string.startText);
        String count = getString(R.string.count);
        EditText field = (EditText) findViewById(R.id.textField);

        String text = field.getText().toString();
        //If the textfield is empty a toast is shown to notify the player
        if (text.matches("")) {
            String message = getString(R.string.noNumber);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else { //the number is tested
            int input = Integer.parseInt(text);
            COUNT++;
            String tmp = count + COUNT;
            textCount.setText(tmp);

            if (input < NUMBER) {
                //the number is to low
                mainText.setText(getString(R.string.toLow));
            } else if (input > NUMBER) {
                //the number is to high
                mainText.setText(getString(R.string.toHigh));
            } else {
                //it was the right number
                if (BEST == 0) {
                    BEST = COUNT;
                } else if (COUNT < BEST) {
                    BEST = COUNT;
                }
                prepareGame(); //if you have won, you can play again
                mainText.setText(getString(R.string.end));
            }
        }
    }
}


