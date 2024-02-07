package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;

public class Rook extends Piece{

    public Rook(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.titlesize;
        this.yPos = row* board.titlesize;
        this.isWhite=isWhite;
        this.name = "Rook";
        this.value = 5;

        this.sprite = sheet.getSubimage(4*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.titlesize,board.titlesize, BufferedImage.SCALE_SMOOTH);
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
}
