package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;

public class Pawn extends Piece{

    public Pawn(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.titlesize;
        this.yPos = row* board.titlesize;
        this.isWhite=isWhite;
        this.name = "Pawn";
        this.value = 1;

        this.sprite = sheet.getSubimage(5*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.titlesize,board.titlesize, BufferedImage.SCALE_SMOOTH);

    }
    public boolean isValidMovement(int col,int row){
        int colorIndex = isWhite ? 1 : -1;

// Push pawn 1
        if (col == this.col && row == this.row - colorIndex && board.getPiece(col, row) == null)
            return true;

// Push pawn 2
        if (isFirstMove && col == this.col && row == this.row - colorIndex * 2 && board.getPiece(col, row) == null)
            return true;

// Capture left
        if (col == this.col - 1 && row == this.row - colorIndex && board.getPiece(col, row) != null)
            return true;

// Capture right
        if (col == this.col + 1 && row == this.row - colorIndex && board.getPiece(col, row) != null)
            return true;
        //enPassant left
        if (board.getTileNum(col,row)==board.enPassantTile&& col== this.col-1 && row == this.row - colorIndex && board.getPiece(col, row + colorIndex)!= null){
            return true;
        }
        //enPassant right
        if (board.getTileNum(col,row)==board.enPassantTile&& col== this.col+1 && row == this.row - colorIndex && board.getPiece(col, row + colorIndex)!= null){
            return true;
        }


        return false;

    }

}
