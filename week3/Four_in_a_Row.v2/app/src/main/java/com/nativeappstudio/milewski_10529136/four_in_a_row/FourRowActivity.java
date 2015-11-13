package com.nativeappstudio.milewski_10529136.four_in_a_row;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Random;

public class FourRowActivity extends AppCompatActivity implements View.OnClickListener {

    public static GridLayout board;
    public TextView turnT1;
    public TextView scoreT1, scoreT2;

    private int cellSize;
    private static boolean PLAYER1 = true;
    private boolean bot = false;

    public int player1wins;
    public int player2wins;

    //the cells are stored in an hashmap so they are easy to retrieve
    public static HashMap<String,CellView>  cells = new HashMap<String, CellView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_row);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
        }

        Intent intent = getIntent();
        player1wins = intent.getIntExtra("wins1",0);
        player2wins = intent.getIntExtra("wins2",0);
        bot = intent.getBooleanExtra("pc",false);

        //prepare the game
        createBoard();
        createButtons();

        //set the textfields
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            turnT1 = (TextView) findViewById(R.id.turn);
            scoreT1 = (TextView) findViewById(R.id.score);

            scoreT1.setText(player1wins + " - " + player2wins);
            scoreT1.setTextColor(Color.DKGRAY);
        } else {
            //in landscape there are two views for the score
            turnT1 = (TextView) findViewById(R.id.turn);
            scoreT1 = (TextView) findViewById(R.id.score1);
            scoreT2 = (TextView) findViewById(R.id.score2);

            scoreT1.setText("Player1: "+player1wins);
            scoreT1.setTextColor(Color.DKGRAY);
            scoreT2.setText("Player2: "+player2wins);
            scoreT2.setTextColor(Color.DKGRAY);
        }

        setTurnText();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("p1wins", player1wins);
        outState.putInt("p2wins", player2wins);
        outState.putBoolean("pc", bot);
        outState.putBoolean("player", PLAYER1);
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        player1wins = inState.getInt("p1wins");
        player2wins = inState.getInt("p2wins");
        bot = inState.getBoolean("pc");
        PLAYER1 = inState.getBoolean("player");
    }

    //every turn the text has to be changed to let the players know who's turn it is
    private void setTurnText() {
        if (PLAYER1) {
            turnT1.setText(R.string.player1);
            turnT1.setTextColor(Color.GREEN);
        } else {
            turnT1.setText(R.string.player2);
            turnT1.setTextColor(Color.RED);
        }
    }

    //the four in a row board is created
    private void createBoard() {
        board = (GridLayout) findViewById(R.id.gameBoard);

        //the number of columns and rows from the gridview
        int cols = board.getColumnCount();
        int rows = board.getRowCount();

        //the size for each cell is set depending on the screen size
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth,margins;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //in portrait size depends on screen width
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            screenWidth = size.x;
            margins = (int) ((R.dimen.activity_horizontal_margin * metrics.density) + 0.5) * 2;
            cellSize = (screenWidth - margins) / cols;
        } else {
            //in landscape the size of the shell depends on the height of the screen
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            screenWidth = size.y;
            margins = (int) ((R.dimen.activity_vertical_margin * metrics.density) + 0.5) * 2;
            cellSize = (screenWidth -margins - 220) / (rows+1);
        }

        //in every cell, a cellview is stored.
        int index = 100;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CellView cv = new CellView(this,i,j);
                String key = i+"-"+j;
                cells.put(key, cv);
                board.addView(cv);
                cv.getLayoutParams().width = cellSize;
                cv.getLayoutParams().height = cellSize;
                cv.setId(index+j);
            }
            index += 10;
        }
    }

    //create a row of buttons, so that in each column a coin can be dropped
    //they must have the id's from 0 zero the number of cols
    private void createButtons() {
        LinearLayout buttonList = (LinearLayout) findViewById(R.id.ButtonList);
        int cols = board.getColumnCount();

        for (int i = 0; i < cols; i++) {
            ImageButton button = new ImageButton(this);
            buttonList.addView(button);
            button.setImageResource(R.drawable.arrow_up);
            button.getLayoutParams().width = cellSize;
            button.getLayoutParams().height = cellSize;
            button.setScaleType(ImageView.ScaleType.FIT_CENTER);
            button.setAdjustViewBounds(true);
            button.setId(i);
            button.setOnClickListener(this);
        }
    }

    //tests on which button is clicked and drops in that column a coin
    public void onClick(View view){
        int id = view.getId();
        //test which column to drop
        for(int i = 0; i < board.getColumnCount(); i++){
            if(id == i) {
                int row = dropCoin(id, -1);
                // if the column was already full
                if (row == -1){
                    Toast.makeText(getApplicationContext(), "Not a valid move",
                            Toast.LENGTH_SHORT).show();
                } else {
                    testWin(id, row);
                    if (PLAYER1) {
                        PLAYER1 = false;
                        if(bot) {
                            pcMove();
                        }
                    } else {
                        PLAYER1 = true;
                    }
                    setTurnText();
                }
                break;
            }
        }
    }

    //drop the coin in the given column
    public static int dropCoin(int id, int row) {
        int lastRow = row;

        //if it is the first test
        if (row == -1) {
            row = 0;
        }

        //if it is not beyond the last row
        if(row < board.getRowCount()) {
            CellView cell = cells.get(row+"-"+id);
            if(cell.isFree()) {
                lastRow = cell.drop(PLAYER1);
            }
        }
        return lastRow;
    }

    //the pc drops in a random column a coin
    private void pcMove() {
        Random random = new Random();
        int col = random.nextInt(board.getColumnCount());

        int row = dropCoin(col, -1);
        // if the column was already full
        if (row == -1){
            pcMove();
        } else {
            testWin(col, row);
            PLAYER1 = true;
        }
    }

    //depending on where the coin landed, test if it was a winning move
    public void testWin(int column, int row) {
        if(horizontalWin(column,row) || verticalWin(column,row) ||
                diag1Win(column,row) || diag2Win(column,row)) {

            //create an popup so the player can choose to play again
            AlertDialog.Builder gameEnd = new AlertDialog.Builder(this);
            gameEnd.setTitle("Play Again?");

            if (PLAYER1) {
                player1wins += 1;
                gameEnd.setMessage("Player 1 has won! \n Play Again?");
            } else {
                player2wins += 1;
                gameEnd.setMessage("Player 2 has won! \n Play Again?");
            }

            //action for ending the game
            gameEnd.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Intent intent = new Intent(FourRowActivity.this, MenuActivity.class);
                    finish();
                }
            });
            //action for starting a new game
            gameEnd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(FourRowActivity.this, FourRowActivity.class);
                    intent.putExtra("wins1",player1wins);
                    intent.putExtra("wins2",player2wins);
                    intent.putExtra("pc",bot);
                    finish();
                    startActivity(intent);
                }
            });

            gameEnd.show();
        }
    }

    //test if there are horizontally four in a row
    public boolean horizontalWin(int column, int row) {
        int testPly = 2;
        if(PLAYER1){
            testPly = 1;
        }

        int count = 1;

        CellView cellMin1 = cells.get(row+"-"+(column-1));
        CellView cellMin2 = cells.get(row+"-"+(column-2));
        CellView cellMin3 = cells.get(row+"-"+(column-3));
        CellView cellPlus1 = cells.get(row+"-"+(column+1));
        CellView cellPlus2 = cells.get(row+"-"+(column+2));
        CellView cellPlus3 = cells.get(row+"-"+(column+3));

        if (cellMin1 != null && cellMin1.getPlayer() == testPly){
            count++;

            if (cellMin2 != null && cellMin2.getPlayer() == testPly) {
                count++;

                if (cellMin3 != null && cellMin3.getPlayer() == testPly) {
                    count++;
                }
            }
        }

        if (cellPlus1 != null && cellPlus1.getPlayer() == testPly){
            count++;

            if (cellPlus2 != null && cellPlus2.getPlayer() == testPly) {
                count++;

                if (cellPlus3 != null && cellPlus3.getPlayer() == testPly) {
                    count++;
                }
            }
        }
        if(count > 3) {
            return true;
        }
        return false;
    }

    //test if there are vertically four in a row
    public boolean verticalWin(int column, int row) {
        int testPly = 2;
        if(PLAYER1){
            testPly = 1;
        }

        int count = 1;

        CellView cellMin1 = cells.get((row-1)+"-"+column);
        CellView cellMin2 = cells.get((row-2)+"-"+column);
        CellView cellMin3 = cells.get((row-3)+"-"+column);
        CellView cellPlus1 = cells.get((row+1)+"-"+column);
        CellView cellPlus2 = cells.get((row+2)+"-"+column);
        CellView cellPlus3 = cells.get((row+3)+"-"+column);

        if (cellMin1 != null && cellMin1.getPlayer() == testPly) {
            count++;

            if (cellMin2 != null && cellMin2.getPlayer() == testPly) {
                count++;

                if (cellMin3 != null && cellMin3.getPlayer() == testPly) {
                    count++;
                }
            }
        }

        if (cellPlus1 != null && cellPlus1.getPlayer() == testPly) {
            count++;

            if (cellPlus2 != null && cellPlus2.getPlayer() == testPly) {
                count++;

                if (cellPlus3 != null && cellPlus3.getPlayer() == testPly) {
                    count++;
                }
            }
        }

        if(count > 3) {
            return true;
        }
        return false;
    }

    //test diagonally from top left to bottom right
    public boolean diag1Win(int column, int row) {
        int testPly = 2;
        if(PLAYER1){
            testPly = 1;
        }

        int count = 1;

        CellView cellMin1 = cells.get((row-1)+"-"+(column-1));
        CellView cellMin2 = cells.get((row-2)+"-"+(column-2));
        CellView cellMin3 = cells.get((row-3)+"-"+(column-3));
        CellView cellPlus1 = cells.get((row+1)+"-"+(column+1));
        CellView cellPlus2 = cells.get((row+2)+"-"+(column+2));
        CellView cellPlus3 = cells.get((row+3)+"-"+(column+3));

        if (cellMin1 != null && cellMin1.getPlayer() == testPly) {
            count++;

            if (cellMin2 != null && cellMin2.getPlayer() == testPly) {
                count++;

                if (cellMin3 != null && cellMin3.getPlayer() == testPly) {
                    count++;
                }
            }
        }

        if (cellPlus1 != null && cellPlus1.getPlayer() == testPly) {
            count++;

            if (cellPlus2 != null && cellPlus2.getPlayer() == testPly) {
                count++;

                if (cellPlus3 != null && cellPlus3.getPlayer() == testPly) {
                    count++;
                }
            }
        }
        if(count > 3) {
            return true;
        }
        return false;
    }

    //test diagonally from bottom left to top right
    public boolean diag2Win(int column, int row) {
        int testPly = 2;
        if(PLAYER1){
            testPly = 1;
        }

        int count = 1;

        CellView cellMin1 = cells.get((row-1)+"-"+(column+1));
        CellView cellMin2 = cells.get((row-2)+"-"+(column+2));
        CellView cellMin3 = cells.get((row-3)+"-"+(column+3));
        CellView cellPlus1 = cells.get((row+1)+"-"+(column-1));
        CellView cellPlus2 = cells.get((row+2)+"-"+(column-2));
        CellView cellPlus3 = cells.get((row+3)+"-"+(column-3));

        if (cellMin1 != null && cellMin1.getPlayer() == testPly) {
            count++;

            if (cellMin2 != null && cellMin2.getPlayer() == testPly) {
                count++;

                if (cellMin3 != null && cellMin3.getPlayer() == testPly) {
                    count++;
                }
            }
        }

        if (cellPlus1 != null && cellPlus1.getPlayer() == testPly) {
            count++;

            if (cellPlus2 != null && cellPlus2.getPlayer() == testPly) {
                count++;

                if (cellPlus3 != null && cellPlus3.getPlayer() == testPly) {
                    count++;
                }
            }
        }
        if(count > 3) {
            return true;
        }
        return false;
    }
}
