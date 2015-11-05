package com.nativeappstudio.milewski_10529136.four_in_a_row;

import android.app.ActionBar;
import android.graphics.Point;
import android.os.Bundle;
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
import android.widget.Toast;

public class FourRowActivity extends AppCompatActivity implements View.OnClickListener {

    public static GridLayout board;
    private int cellSize;
    private static boolean PLAYER1 = true;

    public static CellView cells[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_row);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createBoard();

        createButtons();
    }

    private void createBoard() {
        board = (GridLayout) findViewById(R.id.gameBoard);

        int cols = board.getColumnCount();
        int rows = board.getRowCount();

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int screenWidth = size.x;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int margins = (int) ((R.dimen.activity_horizontal_margin * metrics.density) + 0.5)*2;
        cellSize = (screenWidth-margins)/7;

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

    public void onClick(View view){
        int id = view.getId();
        for(int i = 0; i < board.getColumnCount(); i++){
            if(id == i) {
                int row = dropCoin(id, 0);
                testWin(id,row);
                if(PLAYER1) {
                    PLAYER1 = false;
                } else {
                    PLAYER1 = true;
                }
            }
        }
    }

    public static int dropCoin(int id, int row) {
        int lastRow = row;
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

    public void testWin(int column, int row) {
        if(horizontalWin(column,row) || verticalWin(column,row) ||
                diag1Win(column,row) || diag2Win(column,row)) {
            Toast.makeText(getApplicationContext(), "you won",
                    Toast.LENGTH_LONG).show();
        }
    }

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
