package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Bishop extends Piece{

    public Bishop(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.tilesize;
        this.yPos = row* board.tilesize;
        this.isWhite=isWhite;
        this.name = "Bishop";
        this.value = 330;

        this.sprite = sheet.getSubimage(2*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.tilesize,board.tilesize, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col,int row){
        return Math.abs(this.col-col)==Math.abs(this.row-row);
    }

    public boolean movmentCollidWithPiece(int col,int row){
        //up left
        if(this.col>col && this.row>row){
            for(int i=1 ; i< Math.abs(this.col-col);i++){
                if(board.getPiece(this.col-i,this.row-i)!=null){
                    return true;
                }
            }
        }
        //up right
        if(this.col<col && this.row>row){
            for(int i=1 ; i< Math.abs(this.col-col);i++){
                if(board.getPiece(this.col+i,this.row-i)!=null){
                    return true;
                }
            }
        }
        //down left
        if(this.col>col && this.row<row){
            for(int i=1 ; i< Math.abs(this.col-col);i++){
                if(board.getPiece(this.col-i,this.row+i)!=null){
                    return true;
                }
            }
        }
        //down right
        if(this.col<col && this.row<row){
            for(int i=1 ; i< Math.abs(this.col-col);i++){
                if(board.getPiece(this.col+i,this.row+i)!=null){
                    return true;
                }
            }
        }


        return false;
    }
    public ArrayList<Integer> getPossibleMoves(int currentCol, int currentRow) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();

// Calculate possible moves in the top-left direction
        int col = currentCol - 1;
        int row = currentRow - 1;
        while (col >= 0 && row >= 0) {
            possibleMoves.add(row * 8 + col ); // Add tile number
            col--;
            row--;
        }

        // Calculate possible moves in the top-right direction
        col = currentCol + 1;
        row = currentRow - 1;
        while (col < 8 && row >= 0) { // Assuming a standard 8x8 chessboard
            possibleMoves.add(row * 8 + col ); // Add tile number
            col++;
            row--;
        }

        // Calculate possible moves in the bottom-left direction
        col = currentCol - 1;
        row = currentRow + 1;
        while (col >= 0 && row < 8) { // Assuming a standard 8x8 chessboard
            possibleMoves.add(row * 8 + col ); // Add tile number
            col--;
            row++;
        }

        // Calculate possible moves in the bottom-right direction
        col = currentCol + 1;
        row = currentRow + 1;
        while (col < 8 && row < 8) { // Assuming a standard 8x8 chessboard
            possibleMoves.add(row * 8 + col ); // Add tile number
            col++;
            row++;
        }

        return possibleMoves;
    }

}
