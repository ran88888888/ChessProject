package model;

import java.util.Objects;

public class Move implements Comparable<Move> {
    public int oldCol;
    public int oldRow;
    public int newCol;
    public int newRow;

    public float score = 0;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return oldCol == move.oldCol && oldRow == move.oldRow && newCol == move.newCol && newRow == move.newRow &&  Objects.equals(piece, move.piece) && Objects.equals(captured, move.captured);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldCol, oldRow, newCol, newRow, score, piece, captured);
    }

    public Piece piece;
    public Piece captured;

    public Move(Board board, Piece piece, int newCol, int newRow){
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
    public String toString() {
        return "Move{" +
                "oldCol=" + oldCol +
                ", oldRow=" + oldRow +
                ", newCol=" + newCol +
                ", newRow=" + newRow +
                ", score=" + score +
                ", piece=" + piece +
                ", captured=" + captured +
                '}';
    }

    @Override
    public int compareTo(Move o) {
        return (int) (o.score-this.score);
    }





}
