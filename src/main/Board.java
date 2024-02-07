package main;


import pieces.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class Board extends JPanel {
    public int titlesize = 85;
    int cols=8;
    int rows = 8;
    ArrayList<Piece> piecesList= new ArrayList<>();
    
    public Piece selectedPiece;
    Input input = new Input(this);
    CheckScanner checkScanner = new CheckScanner(this);
    public int enPassantTile = -1;
    public int whiteturn = 1;
    protected int typeOfpro;
    public int gameOver = 0;
    public Duck duck = new Duck(this,4,4);



    public Board()
    {
        this.setPreferredSize(new Dimension(cols*titlesize,rows*titlesize));
        this.typeOfpro = 4;
        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    public Piece getPiece(int col,int row){
        for(Piece p :piecesList){
            if(p.col==col && p.row==row){
                return p;
            }
        }
        return null;
    }

    public void makeMove(Move move){
        isDuckMovedThisTurn();
        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }
        if (move.piece.name.equals("Duck")){
            duck.isDuckMoved = true;
        }
        if (!move.piece.name.equals("Duck")){
            duck.youCanPlayWithDuck = true;
            duck.isDuckMoved = false;
        }
        else if(move.piece.name.equals("King")) {
            moveKing(move);
        }

        else {
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * titlesize;
            move.piece.yPos = move.newRow * titlesize;

            move.piece.isFirstMove = false;
            capture(move.captured);
        }

    }
    private void moveKing(Move move) {
        if(Math.abs(move.newCol-move.piece.col)==2) {
            Piece rook;
            if(move.piece.col< move.newCol) {
                rook = this.getPiece(7, move.piece.row);
                rook.col =5;
            }
            else {
                rook = this.getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col* titlesize;
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * titlesize;
        move.piece.yPos = move.newRow * titlesize;

        move.piece.isFirstMove = false;

        capture(move.captured);
    }

    private void movePawn(Move move) {
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.captured = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }
        //premotion
        colorIndex = move.piece.isWhite ? 0:7;
        if (move.newRow==colorIndex){
            promotionPawn(move);
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * titlesize;
        move.piece.yPos = move.newRow * titlesize;

        move.piece.isFirstMove = false;

        capture(move.captured);

    }

    private void promotionPawn(Move move) {
       switch (this.typeOfpro){
           case 4:
               piecesList.add(new Queen(this,move.newCol,move.newRow,move.piece.isWhite));
               break;
           case 3:
               piecesList.add(new Knight(this,move.newCol,move.newRow,move.piece.isWhite));
               break;
           case 2:
               piecesList.add(new Bishop(this,move.newCol,move.newRow,move.piece.isWhite));
               break;
           case 1:
               piecesList.add(new Rook(this,move.newCol,move.newRow,move.piece.isWhite));
               break;
       }
        capture(move.piece);
    }
    public void isDuckMovedThisTurn(){
        if (duck.isDuckMoved){
            whiteturn = whiteturn *-1;
            duck.isDuckMoved = false;
            duck.youCanPlayWithDuck = false;
        }
    }

    public void capture(Piece piece){
        piecesList.remove(piece);
    }
    public boolean isValidMove(Move move){
        if(!move.piece.isValidMovement(move.newCol, move.newRow)){
            return false;
        }
        if(sameTeam(move.piece,move.captured)){
            return false;
        }
        if(move.piece.movmentCollidWithPiece(move.newCol, move.newRow)){
            return false;
        }
        if (!isYourTurn(move)){
            return false;
        }
        if (checkScanner.isKingChecked(move)){
            return false;
        }
        return true;
    }
    public  boolean isYourTurn(Move move){
        if (move.piece.isWhite){
            if (whiteturn ==1){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            if (whiteturn ==-1){
                return true;
            }
            else{
                return false;
            }
        }
    }
    public boolean sameTeam(Piece p1,Piece p2){
        if ((p1==null)||(p2==null)){
            return false;
        }
        return p1.isWhite==p2.isWhite;
    }
    public int getTileNum(int col,int row){
        return  row * rows +col;
    }
    public Piece findKing(boolean isWhite){
        for(Piece piece: piecesList){
            if (piece.isWhite == isWhite && piece.name.equals("King")){
                return piece;
            }
        }
        return null;
    }
    public boolean hasValMove(Piece piece){
        if (piece.name.equals("Rook")){

        }

        return false;
    }

    public void addPieces(){

        //black pieces
        piecesList.add(new Rook(this,0,0,false));
        piecesList.add(new Knight(this,1,0,false));
        piecesList.add(new Bishop(this,2,0,false));
        piecesList.add(new Queen(this,3,0,false));
        piecesList.add(new King(this,4,0,false));
        piecesList.add(new Bishop(this,5,0,false));
        piecesList.add(new Knight(this,6,0,false));
        piecesList.add(new Rook(this,7,0,false));
        //black pawns
        piecesList.add(new Pawn(this,0,1,false));
        piecesList.add(new Pawn(this,1,1,false));
        piecesList.add(new Pawn(this,2,1,false));
        piecesList.add(new Pawn(this,3,1,false));
        piecesList.add(new Pawn(this,4,1,false));
        piecesList.add(new Pawn(this,5,1,false));
        piecesList.add(new Pawn(this,6,1,false));
        piecesList.add(new Pawn(this,7,1,false));
        //white pieces
        piecesList.add(new Rook(this,0,7,true));
        piecesList.add(new Knight(this,1,7,true));
        piecesList.add(new Bishop(this,2,7,true));
        piecesList.add(new Queen(this,3,7,true));
        piecesList.add(new King(this,4,7,true));
        piecesList.add(new Bishop(this,5,7,true));
        piecesList.add(new Knight(this,6,7,true));
        piecesList.add(new Rook(this,7,7,true));
        //white pawns
        piecesList.add(new Pawn(this,0,6,true));
        piecesList.add(new Pawn(this,1,6,true));
        piecesList.add(new Pawn(this,2,6,true));
        piecesList.add(new Pawn(this,3,6,true));
        piecesList.add(new Pawn(this,4,6,true));
        piecesList.add(new Pawn(this,5,6,true));
        piecesList.add(new Pawn(this,6,6,true));
        piecesList.add(new Pawn(this,7,6,true));

        //duck



    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        //paint board
        for (int r=0;r<rows;r++){
            for (int c=0;c<cols;c++){
                g2d.setColor((c+r)%2==0? new Color(229, 191, 163):new Color(161, 96, 21));
                g2d.fillRect(c*titlesize,r*titlesize,titlesize,titlesize);
            }
        }
        //paint where you can go with the valid moves
        if(selectedPiece!=null){
            for (int r=0;r<rows;r++){
                for (int c=0;c<cols;c++){
                    if(isValidMove(new Move(this,selectedPiece,c,r))){
                        g2d.setColor(new Color(41, 201, 77,120));
                        g2d.fillRect(c*titlesize,r*titlesize,titlesize,titlesize);
                    }

                }
            }
        }
        //paint pieces
        for (Piece p: piecesList){
            p.paint(g2d);
        }
    }

}
