package model;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class King extends Piece{

    public King(Board board,int col,int row,Boolean isWhite) {
        super(board);
        this.col=col;
        this.row=row;
        this.xPos = col* board.tilesize;
        this.yPos = row* board.tilesize;
        this.isWhite=isWhite;
        this.name = "King";
        this.value = 20000;

        this.sprite = sheet.getSubimage(0*sheetScale,isWhite ? 0 : sheetScale,sheetScale,sheetScale).getScaledInstance(board.tilesize,board.tilesize, BufferedImage.SCALE_SMOOTH);

    }
    public boolean isValidMovement(int col,int row){
        return Math.abs((col-this.col)*(row-this.row))==1 ||Math.abs(col-this.col)+Math.abs(row-this.row)==1 || canCustle(col, row);
    }
    public boolean canCustle(int col, int row) {
        if(this.row == row) {
            if(col == 6) {
                Piece rook = board.getPiece(7, row);
                if(rook!=null) {
                    {
                        return board.getPiece(6, row)==null &&
                                board.getPiece(5, row)==null;
                    }
                }
            }
            if(col == 2) {
                Piece rook = board.getPiece(0, row);
                if(rook != null) {
                    if (rook.name.equals("Rook")){
                        return board.getPiece(1, row)==null &&
                                board.getPiece(2, row)==null &&
                                board.getPiece(3, row)==null ;
                    }
                }
            }
        }
        return false;
    }
    public ArrayList<Integer> getPossibleMoves(int currentCol, int currentRow) {
        ArrayList<Integer> possibleMoves = new ArrayList<>();
        // Calculate possible moves in horizontal and vertical directions
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                if (dRow == 0 && dCol == 0) continue; // Skip current position

                int newRow = currentRow + dRow;
                int newCol = currentCol + dCol;

                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) { // Assuming a standard 8x8 chessboard
                    possibleMoves.add(newRow * 8 + newCol); // Add tile number
                }
            }
        }
        if (canCustle(currentCol + 2, currentRow)){
            possibleMoves.add((currentRow * 8) + currentCol + 2);
        }
        if (canCustle(currentCol - 2, currentRow)){
            possibleMoves.add((currentRow * 8) + currentCol - 2);
        }
        return possibleMoves;
    }


}
