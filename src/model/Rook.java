package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Rook extends Piece{

    public Rook(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.tilesize;
        this.yPos = row* board.tilesize;
        this.isWhite=isWhite;
        this.name = "Rook";
        this.value = 500;

        this.sprite = sheet.getSubimage(4*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.tilesize,board.tilesize, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col,int row){
        return this.col==col|| this.row==row;
    }

    public boolean movmentCollidWithPiece(int col,int row){
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
        return false;
    }
    public ArrayList<Integer> getPossibleMoves(int currentCol, int currentRow) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        for(int c = 0;c<8;c++){
            possibleMoves.add((currentRow*8)+c);
        }
        for (int r = 0;r<8;r++){
            possibleMoves.add(currentCol+(r*8));
        }
        return possibleMoves;
    }
}
