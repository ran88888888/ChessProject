package main;

import pieces.Piece;

import java.util.*;

public class Player {
    Map<Integer, Piece> pieces;


    public  Player(){
        pieces = new  HashMap<Integer, Piece>();
    }

    public Piece getPiece(int col, int row) {
        Integer key = row*Board.cols+col;
        return pieces.containsKey(key) ? pieces.get(key) : null;
    }
}
