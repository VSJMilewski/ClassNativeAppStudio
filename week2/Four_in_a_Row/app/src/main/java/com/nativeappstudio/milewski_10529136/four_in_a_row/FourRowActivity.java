package com.nativeappstudio.milewski_10529136.four_in_a_row;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FourRowActivity extends AppCompatActivity implements View.OnClickListener {

    public static GridLayout board;
    public TextView turnT;
    public TextView scoreT;

    private int cellSize;
    private static boolean PLAYER1 = true;
    public static int player1wins = 0;
    public static int player2wins = 0;

    public static CellView cells[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_row);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //prepare the game
        createBoard();
        createButtons();

        //set the textfields
        turnT = (TextView) findViewById(R.id.turn);
        scoreT = (TextView) findViewById(R.id.score);

        setTurnText();
        scoreT.setText(player1wins + " - " + player2wins);
        scoreT.setTextColor(Color.DKGRAY);
    }

    //every turn the text has to be changed to let the players know who's turn it is
    private void setTurnText() {
        if (PLAYER1) {
            turnT.setText(R.string.player1);
            turnT.setTextColor(Color.GREEN);
        } else {
            turnT.setText(R.string.player2);
            turnT.setTextColor(Color.RED);
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
        int screenWidth = size.x;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int margins = (int) ((R.dimen.activity_horizontal_margin * metrics.density) + 0.5)*2;
        cellSize = (screenWidth-margins)/7;

        //in every cell, a cellview is stored.
        cells = new CellView[cols*rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                CellView cv = new CellView(this,i,j);
                //cv.getLayoutParams().width = screenWidth/7;
                //cv.getLayoutParams().height = screenWidth/7;
                cells[i*cols + j] = cv;
                board.addView(cv);
                cv.getLayoutParams().width = cellSize;
                cv.getLayoutParams().height = cellSize;
            }
        }
    }

    //create a row of buttons, so that in each column a coin can be dropped
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
            for (CellView cell : cells) {
                int c = cell.getCol();
                int r = cell.getRow();
                if (c == id && r == row) {
                    if(cell.isFree()) {
                        lastRow = cell.drop(PLAYER1);
                    }
                }
            }
        }
        return lastRow;
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
                    startActivity(new Intent(FourRowActivity.this, MenuActivity.class));
                }
            });
            //action for starting a new game
            gameEnd.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(FourRowActivity.this, FourRowActivity.class));
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
        CellView cellMin3 = null,cellMin2 = null,cellMin1 = null;
        CellView cellPlus1 = null,cellPlus2 = null,cellPlus3 = null;

        for(CellView cell : cells){
            if(cell.getRow() == row) {
                if (cell.getCol() == column - 3){
                    cellMin3 = cell;
                }
                if (cell.getCol() == column - 2){
                    cellMin2 = cell;
                }
                if (cell.getCol() == column - 1){
                    cellMin1 = cell;
                }
                if (cell.getCol() == column + 1){
                    cellPlus1 = cell;
                }
                if (cell.getCol() == column + 2){
                    cellPlus2 = cell;
                }
                if (cell.getCol() == column + 3){
                    cellPlus3 = cell;
                }
            }
        }
        if (cellMin1 != null && !cellMin1.isFree()){
            if (cellMin1.getPlayer() == testPly) {
                count++;
            }

            if (cellMin2 != null && !cellMin2.isFree()) {
                if (cellMin2.getPlayer() == testPly) {
                    count++;
                }

                if (cellMin3 != null && !cellMin3.isFree()) {
                    if (cellMin3.getPlayer() == testPly) {
                        count++;
                    }
                }
            }
        }

        if (cellPlus1 != null && !cellPlus1.isFree()){
            if (cellPlus1.getPlayer() == testPly) {
                count++;
            }

            if (cellPlus2 != null && !cellPlus2.isFree()) {
                if (cellPlus2.getPlayer() == testPly) {
                    count++;
                }

                if (cellPlus3 != null && !cellPlus3.isFree()) {
                    if (cellPlus3.getPlayer() == testPly) {
                        count++;
                    }
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
        CellView cellMin3 = null,cellMin2 = null,cellMin1 = null;
        CellView cellPlus1 = null,cellPlus2 = null,cellPlus3 = null;

        for(CellView cell : cells){
            if(cell.getCol() == column) {
                if (cell.getRow() == row - 3){
                    cellMin3 = cell;
                }
                if (cell.getRow() == row - 2){
                    cellMin2 = cell;
                }
                if (cell.getRow() == row - 1){
                    cellMin1 = cell;
                }
                if (cell.getRow() == row + 1){
                    cellPlus1 = cell;
                }
                if (cell.getRow() == row + 2){
                    cellPlus2 = cell;
                }
                if (cell.getRow() == row + 3){
                    cellPlus3 = cell;
                }
            }
        }
        if (cellMin1 != null && !cellMin1.isFree()){
            if (cellMin1.getPlayer() == testPly) {
                count++;
            }

            if (cellMin2 != null && !cellMin2.isFree()) {
                if (cellMin2.getPlayer() == testPly) {
                    count++;
                }

                if (cellMin3 != null && !cellMin3.isFree()) {
                    if (cellMin3.getPlayer() == testPly) {
                        count++;
                    }
                }
            }
        }

        if (cellPlus1 != null && !cellPlus1.isFree()){
            if (cellPlus1.getPlayer() == testPly) {
                count++;
            }

            if (cellPlus2 != null && !cellPlus2.isFree()) {
                if (cellPlus2.getPlayer() == testPly) {
                    count++;
                }

                if (cellPlus3 != null && !cellPlus3.isFree()) {
                    if (cellPlus3.getPlayer() == testPly) {
                        count++;
                    }
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

        CellView cellMin3 = null,cellMin2 = null,cellMin1 = null;
        CellView cellPlus1 = null,cellPlus2 = null,cellPlus3 = null;

        for(CellView cell : cells){
            if (cell.getRow() == row - 3 && cell.getCol() == column -3){
                cellMin3 = cell;
            }
            if (cell.getRow() == row - 2 && cell.getCol() == column -2){
                cellMin2 = cell;
            }
            if (cell.getRow() == row - 1 && cell.getCol() == column -1){
                cellMin1 = cell;
            }
            if (cell.getRow() == row + 1 && cell.getCol() == column +1){
                cellPlus1 = cell;
            }
            if (cell.getRow() == row + 2 && cell.getCol() == column +2){
                cellPlus2 = cell;
            }
            if (cell.getRow() == row + 3 && cell.getCol() == column +3){
                cellPlus3 = cell;
            }
        }
        if (cellMin1 != null && !cellMin1.isFree()){
            if (cellMin1.getPlayer() == testPly) {
                count++;
            }

            if (cellMin2 != null && !cellMin2.isFree()) {
                if (cellMin2.getPlayer() == testPly) {
                    count++;
                }

                if (cellMin3 != null && !cellMin3.isFree()) {
                    if (cellMin3.getPlayer() == testPly) {
                        count++;
                    }
                }
            }
        }

        if (cellPlus1 != null && !cellPlus1.isFree()){
            if (cellPlus1.getPlayer() == testPly) {
                count++;
            }

            if (cellPlus2 != null && !cellPlus2.isFree()) {
                if (cellPlus2.getPlayer() == testPly) {
                    count++;
                }

                if (cellPlus3 != null && !cellPlus3.isFree()) {
                    if (cellPlus3.getPlayer() == testPly) {
                        count++;
                    }
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

        CellView cellMin3 = null,cellMin2 = null,cellMin1 = null;
        CellView cellPlus1 = null,cellPlus2 = null,cellPlus3 = null;

        for(CellView cell : cells){
            if (cell.getRow() == row - 3 && cell.getCol() == column +3){
                cellMin3 = cell;
            }
            if (cell.getRow() == row - 2 && cell.getCol() == column +2){
                cellMin2 = cell;
            }
            if (cell.getRow() == row - 1 && cell.getCol() == column +1){
                cellMin1 = cell;
            }
            if (cell.getRow() == row + 1 && cell.getCol() == column -1){
                cellPlus1 = cell;
            }
            if (cell.getRow() == row + 2 && cell.getCol() == column -2){
                cellPlus2 = cell;
            }
            if (cell.getRow() == row + 3 && cell.getCol() == column -3){
                cellPlus3 = cell;
            }
        }
        if (cellMin1 != null && !cellMin1.isFree()){
            if (cellMin1.getPlayer() == testPly) {
                count++;
            }

            if (cellMin2 != null && !cellMin2.isFree()) {
                if (cellMin2.getPlayer() == testPly) {
                    count++;
                }

                if (cellMin3 != null && !cellMin3.isFree()) {
                    if (cellMin3.getPlayer() == testPly) {
                        count++;
                    }
                }
            }
        }

        if (cellPlus1 != null && !cellPlus1.isFree()){
            if (cellPlus1.getPlayer() == testPly) {
                count++;
            }

            if (cellPlus2 != null && !cellPlus2.isFree()) {
                if (cellPlus2.getPlayer() == testPly) {
                    count++;
                }

                if (cellPlus3 != null && !cellPlus3.isFree()) {
                    if (cellPlus3.getPlayer() == testPly) {
                        count++;
                    }
                }
            }
        }
        if(count > 3) {
            return true;
        }
        return false;
    }
}
