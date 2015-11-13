package com.nativeappstudio.milewski_10529136.four_in_a_row;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.ImageView;


/**
 * Created by victor on 2-11-2015.
 */
public class CellView extends ImageView {

    int COLUMN = 0; //default
    int ROW = 0; //default
    boolean FREE = true;
    int PLAYER = 0;

    /*
    * the row and column of the location in the grid are stored
     */
    public CellView(Context context, int x, int y) {
        super(context);
        super.setImageResource(R.drawable.empty_cell);

        ROW = x;
        COLUMN = y;
    }

    public CellView(Context context) {
        super(context);
    }

    public CellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //returns the row
    public int getRow() {
        return ROW;
    }

    //returns the column
    public int getCol() {
        return COLUMN;
    }

    //returns if there is a coin in this cell
    public boolean isFree() {
        return FREE;
    }

    //returns the player from who the coin is
    public int getPlayer() {
        return PLAYER;
    }

    //places a coin in this cell, and tries to let the coin fall further down
    public int drop(boolean player1) {
        int lastRow = ROW;
        if (player1) {
            PLAYER = 1;
            FREE = false;
            super.setImageResource(R.drawable.player1_cell);
        } else {
            PLAYER = 2;
            FREE = false;
            super.setImageResource(R.drawable.player2_cell);
        }

        //try to let the coin fall in the next row
        if (ROW != FourRowActivity.board.getRowCount()) {
            CellView cell = FourRowActivity.cells.get((ROW+1)+"-"+COLUMN);
            //the coin can still fall further down
            if(cell!= null && cell.isFree()) {
                FREE = true;
                PLAYER = 0;
                super.setImageResource(R.drawable.empty_cell);
                lastRow = FourRowActivity.dropCoin(COLUMN, ROW + 1);
            }
        }
        return lastRow;
    }

    /*
    * store the data into a state
     */
    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState ss = new SavedState(superState);
        //end

        ss.COLUMN = this.COLUMN;
        ss.ROW = this.ROW;
        ss.PLAYER = this.PLAYER;
        ss.FREE = this.FREE;

        return ss;
    }

    /*
    * restore data from the state
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        //begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState ss = (SavedState)state;
        super.onRestoreInstanceState(ss.getSuperState());
        //end

        this.COLUMN = ss.COLUMN;
        this.ROW = ss.ROW;
        this.PLAYER = ss.PLAYER;
        this.FREE = ss.FREE;

        //also set the image for the cell
        if(this.PLAYER == 1){
            super.setImageResource(R.drawable.player1_cell);
        } else if(this.PLAYER == 2) {
            super.setImageResource(R.drawable.player2_cell);
        } else {
            super.setImageResource(R.drawable.empty_cell);
        }
    }

    /*
    * class to save the state of an view
     */
    static class SavedState extends BaseSavedState {
        int COLUMN = 0;
        int ROW = 0;
        boolean FREE = true;
        int PLAYER = 0;


        SavedState(Parcelable superState) {
            super(superState);
        }

        //restore the variables
        private SavedState(Parcel in) {
            super(in);
            this.COLUMN = in.readInt();
            this.ROW = in.readInt();
            this.FREE = in.readByte() != 0;
            this.PLAYER = in.readInt();
        }

        //store the variables
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.COLUMN);
            out.writeInt(this.ROW);
            out.writeByte((byte) (this.FREE ? 1 : 0));
            out.writeInt(this.PLAYER);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
