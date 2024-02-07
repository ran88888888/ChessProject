package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;

public class Knight extends Piece{

    public Knight(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.titlesize;
        this.yPos = row* board.titlesize;
        this.isWhite=isWhite;
        this.name = "knight";
        this.value = 3;

        this.sprite = sheet.getSubimage(3*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.titlesize,board.titlesize, BufferedImage.SCALE_SMOOTH);
    }
    public boolean isValidMovement(int col,int row){
        return Math.abs(col-this.col)* Math.abs(row-this.row)==2;
    }

}
