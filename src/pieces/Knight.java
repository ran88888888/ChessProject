package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Knight extends Piece{

    public Knight(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.tilesize;
        this.yPos = row* board.tilesize;
        this.isWhite=isWhite;
        this.name = "Knight";
        this.value = 3;

        this.sprite = sheet.getSubimage(3*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.tilesize,board.tilesize, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col,int row){
        return Math.abs(col-this.col)* Math.abs(row-this.row)==2;
    }
    public ArrayList<Integer> getPossibleMoves(int currentCol, int currentRow) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();

        int[][] moves = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};
        if((currentRow+2)<8 && (currentCol+1)<8){
            possibleMoves.add(((currentRow+2)*8)+currentCol+1);
        }
        if((currentRow+2)<8 && (currentCol-1)>-1){
            possibleMoves.add(((currentRow+2)*8)+currentCol-1);
        }
        if((currentRow+1)<8 && (currentCol-2)>-1){
            possibleMoves.add(((currentRow+1)*8)+currentCol-2);
        }
        if((currentRow+1)<8 && (currentCol+2)<8){
            possibleMoves.add(((currentRow+1)*8)+currentCol+2);
        }
        if((currentRow-2)>-1 && (currentCol+1)<8){
            possibleMoves.add(((currentRow-2)*8)+currentCol+1);
        }
        if((currentRow-2)>-1 && (currentCol-1)>-1){
            possibleMoves.add(((currentRow-2)*8)+currentCol-1);
        }
        if((currentRow-1)>-1 && (currentCol+2)<8){
            possibleMoves.add(((currentRow-1)*8)+currentCol+2);
        }
        if((currentRow-1)>-1 && (currentCol-2)>-1){
            possibleMoves.add(((currentRow-1)*8)+currentCol-2);
        }
        return possibleMoves;
    }


}
