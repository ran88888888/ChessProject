package main;

import pieces.Piece;

public class Move implements Comparable<Move>{
    int oldCol;
    int oldRow;
    int newCol;
    int newRow;

    public int score = 0;
    public Piece piece;
    public Piece captured;

    public Move(Board board,Piece piece,int newCol,int newRow){
        this.oldCol = piece.col;
        this.oldRow = piece.row;
        this.newCol = newCol;
        this.newRow = newRow;

        this.piece = piece;
        this.captured = board.getPiece(newCol,newRow);
    }
    public boolean equalMove(Move m2){
        if (m2.piece.name.equals(this.piece.name)&& m2.piece.col == this.piece.col && m2.piece.row == this.piece.row && m2.newCol==this.newCol && m2.newRow == this.newRow){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int compareTo(Move o) {
        return Integer.compare( o.score,this.score);
    }


}
