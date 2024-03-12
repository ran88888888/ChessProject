package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Pawn extends Piece{

    public Pawn(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.tilesize;
        this.yPos = row* board.tilesize;
        this.isWhite=isWhite;
        this.name = "Pawn";
        this.value = 1;

        this.sprite = sheet.getSubimage(5*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.tilesize,board.tilesize, BufferedImage.SCALE_SMOOTH);

    }
    public boolean isValidMovement(int col,int row){
        int colorIndex = isWhite ? 1 : -1;

// Push pawn 1
        if (col == this.col && row == this.row - colorIndex && board.getPiece(col, row) == null)
            return true;

// Push pawn 2
        if (isFirstMove && col == this.col && row == this.row - colorIndex * 2 && board.getPiece(col, row) == null && board.getPiece(col, row+colorIndex) == null)
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
    public ArrayList<Integer> getPossibleMoves(int currentCol, int currentRow) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();

        int colorIndex = isWhite ? 1 : -1;

        // Forward movement
        if (board.getPiece(currentCol, currentRow - colorIndex) == null) {
            possibleMoves.add(((currentRow - colorIndex)*8)+currentCol);

            // Forward two squares (only on first move)
            if (isFirstMove && board.getPiece(currentCol, currentRow - colorIndex * 2) == null) {
                possibleMoves.add(((currentRow - colorIndex * 2)*8)+currentCol);
            }
        }

        // Capture left
        if (currentCol - 1 >= 0 && board.getPiece(currentCol - 1, currentRow - colorIndex) != null) {
            possibleMoves.add(((currentRow - colorIndex)*8)+currentCol-1);
        }

        // Capture right
        if (currentCol + 1 < 8 && board.getPiece(currentCol + 1, currentRow - colorIndex) != null) {
            possibleMoves.add(((currentRow - colorIndex)*8)+currentCol+1);
        }

        // En passant left
        if (board.getTileNum(currentCol - 1, currentRow) == board.enPassantTile &&
                board.getPiece(currentCol - 1, currentRow + colorIndex) != null) {
            possibleMoves.add(((currentRow - colorIndex)*8)+currentCol-1);
        }

        // En passant right
        if (board.getTileNum(currentCol + 1, currentRow) == board.enPassantTile && board.getPiece(currentCol + 1, currentRow + colorIndex) != null) {
            possibleMoves.add(((currentRow - colorIndex)*8)+currentCol+1);
        }
        return possibleMoves;
    }



}
