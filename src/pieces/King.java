package pieces;

import main.Board;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;

public class King extends Piece{

    public King(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.titlesize;
        this.yPos = row* board.titlesize;
        this.isWhite=isWhite;
        this.name = "King";

        this.sprite = sheet.getSubimage(0*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.titlesize,board.titlesize, BufferedImage.SCALE_SMOOTH);

    }
    public boolean isValidMovement(int col,int row){
        return Math.abs((col-this.col)*(row-this.row))==1 ||Math.abs(col-this.col)+Math.abs(row-this.row)==1 || canCustle(col, row);
    }
    public boolean canCustle(int col, int row) {
        if(this.row == row) {
            if(col == 6) {
                Piece rook = board.getPiece(7, row);
                if(rook!=null) {
                    return board.getPiece(6, row)==null &&
                            board.getPiece(5, row)==null;
                }
            }
            if(col == 2) {
                Piece rook = board.getPiece(0, row);
                if(rook != null) {
                    return board.getPiece(1, row)==null &&
                            board.getPiece(2, row)==null &&
                            board.getPiece(3, row)==null ;
                }
            }
        }
        return false;
    }


}
