package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Queen extends Piece{

    public Queen(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.tilesize;
        this.yPos = row* board.tilesize;
        this.isWhite=isWhite;
        this.name = "Queen";
        this.value = 9;

        this.sprite = sheet.getSubimage(1*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.tilesize,board.tilesize, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col,int row){
        return this.col==col|| this.row==row ||Math.abs(this.col-col)==Math.abs(this.row-row);
    }

    public boolean movmentCollidWithPiece(int col,int row){
        if (this.col==col|| this.row==row ){
            //left
            if (this.col>col){
                for(int c = this.col - 1 ;c>col ;c--){
                    if(board.getPiece(c,this.row)!=null){
                        return true;
                    }
                }
            }
            //right
            if (this.col<col){
                for(int c = this.col + 1 ;c<col ;c++){
                    if(board.getPiece(c,this.row)!=null){
                        return true;
                    }
                }
            }
            //up
            if (this.row>row){
                for(int r = this.row - 1 ;r>row ;r--){
                    if(board.getPiece(this.col,r)!=null){
                        return true;
                    }
                }
            }
            //down
            if (this.row<row){
                for(int r = this.row + 1 ;r<row ;r++){
                    if(board.getPiece(this.col,r)!=null){
                        return true;
                    }
                }
            }
        }
        else {
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
        for(int c = 0;c<8;c++){
            possibleMoves.add((currentRow*8)+c);
        }
        for (int r = 0;r<8;r++){
            possibleMoves.add(currentCol+(r*8));
        }
        return possibleMoves;
    }
}
