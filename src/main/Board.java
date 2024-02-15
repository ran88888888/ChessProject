package main;


import artificialPlayer.Evaluations;
import pieces.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Board extends JPanel {
    public int tilesize = 85;
    int cols=8;
    int rows = 8;
    ArrayList<Piece> piecesList= new ArrayList<>();
    boolean firstMove = true;
    public Piece selectedPiece;
    Input input = new Input(this);
    CheckScanner checkScanner = new CheckScanner(this);
    public int enPassantTile = -1;
    public int whiteturn = 1;
    protected int typeOfpro;
    public int gameOver = 0;
    public Duck duck =new Duck(this,4,3);
    public BitBoard rookBitBoard;
    public BitBoard knightBitBoard;
    public BitBoard bishopBitBoard;
    public BitBoard queenBitBoard ;
    public BitBoard kingBitBoard ;
    public BitBoard pawnBitBoard;
    public BitBoard whiteBitBoard;
    public BitBoard blackBitBoard ;
    public BitBoard allBitBoard ;
    public BitBoard duckBitBoard ;
    public static Map<String, BitBoard> bitboardMap = new HashMap<>();






    public Board()
    {
        new Evaluations(this);
        this.setPreferredSize(new Dimension(cols* tilesize,rows* tilesize));
        this.typeOfpro = 4;
        this.addMouseListener(input);
        this.addMouseMotionListener(input);
        rookBitBoard = new BitBoard(0b1000000100000000000000000000000000000000000000000000000010000001L);
        knightBitBoard = new BitBoard(0b0100001000000000000000000000000000000000000000000000000001000010L);
        bishopBitBoard = new BitBoard(0b0010010000000000000000000000000000000000000000000000000000100100L);
        queenBitBoard = new BitBoard(0b0000100000000000000000000000000000000000000000000000000000001000L);
        kingBitBoard = new BitBoard(0b0001000000000000000000000000000000000000000000000000000000010000L);
        pawnBitBoard = new BitBoard(0b0000000011111111000000000000000000000000000000001111111100000000L);
        whiteBitBoard = new BitBoard(0b1111111111111111000000000000000000000000000000000000000000000000L);
        blackBitBoard = new BitBoard(0b0000000000000000000000000000000000000000000000001111111111111111L);
        allBitBoard = new BitBoard(0b1111111111111111000000000000000000010000000000001111111111111111L);
        duckBitBoard = new BitBoard(0b0000000000000000000000000000000000010000000000000000000000000000L);

        addPieces();

        bitboardMap.put("Rook",rookBitBoard);
        bitboardMap.put("Knight",knightBitBoard);
        bitboardMap.put("Bishop",bishopBitBoard);
        bitboardMap.put("Queen",queenBitBoard);
        bitboardMap.put("King",kingBitBoard);
        bitboardMap.put("Pawn",pawnBitBoard);
        bitboardMap.put("White",whiteBitBoard);
        bitboardMap.put("Black",blackBitBoard);
        bitboardMap.put("All",allBitBoard);
        bitboardMap.put("Duck",duckBitBoard);



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
        if (!move.piece.name.equals("Duck")){
            duck.youCanPlayWithDuck = true;
        }
        if (move.piece.name.equals("Duck")){
            duck.youCanPlayWithDuck = false;
            whiteturn = whiteturn *-1;
            duck.isWhite = whiteturn == 1 ? true: false;
        }
        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }
        if(move.piece.name.equals("King")) {
            moveKing(move);
        }
        else{
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * tilesize;
            move.piece.yPos = move.newRow * tilesize;

            move.piece.isFirstMove = false;
            capture(move.captured);
        }
        bitBoardChange(move);



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
            rook.xPos = rook.col* tilesize;
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tilesize;
        move.piece.yPos = move.newRow * tilesize;

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
        move.piece.xPos = move.newCol * tilesize;
        move.piece.yPos = move.newRow * tilesize;

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
    public void bitBoardChange(Move move){
        int clearPos = ((move.oldRow*8)+move.oldCol);
        int setPos = ((move.newRow*8)+move.newCol);
        int capturedClearPos ;

        BitBoard bitToChange = bitboardMap.get(move.piece.name);
        bitToChange.clearBit(clearPos);
        bitToChange.toggleBit(setPos);
        allBitBoard.clearBit(clearPos);
        allBitBoard.toggleBit(setPos);
        if (move.piece.isWhite){
            whiteBitBoard.clearBit(clearPos);
            whiteBitBoard.toggleBit(setPos);
        }else{
            blackBitBoard.clearBit(clearPos);
            blackBitBoard.toggleBit(setPos);
        }
        if (move.captured!=null){
            capturedClearPos = ((move.captured.row*8)+move.captured.col);
            BitBoard capturedBitBoard = bitboardMap.get(move.captured.name);
            capturedBitBoard.clearBit(capturedClearPos);
            allBitBoard.clearBit(capturedClearPos);
            bitboardMap.put(move.captured.name,capturedBitBoard);
            if (move.captured.isWhite){
                whiteBitBoard.clearBit(capturedClearPos);
            }else{
                blackBitBoard.clearBit(capturedClearPos);
            }
        }
        bitboardMap.put(move.piece.name,bitToChange);
        BitBoard printBitBoard = bitboardMap.get(move.piece.name);
        System.out.println(printBitBoard.toBinaryString());

    }


    public ArrayList<Piece> pieceGiveCheck(Move move) {
        ArrayList<Piece> checkPieces = new ArrayList<>();
        Piece king = findKing(!move.piece.isWhite);
        Piece p;
        //up down
        for(int r = 0;r<8;r++) {
            p = getPiece(king.col, r);
            if(!sameTeam(p,king) && p!=null&&(p.name.equals("Rook")||p.name.equals("Queen"))) {
                if(!p.movmentCollidWithPiece(king.col, king.row)){
                    checkPieces.add(p);
                }

            }
        }
        //left right
        for(int c = 0;c<8;c++) {
            p = getPiece(c, king.row);
            if(!sameTeam(p,king) && p!=null&&(p.name.equals("Rook")||p.name.equals("Queen"))) {
                if(!p.movmentCollidWithPiece(king.col, king.row)){
                    checkPieces.add(p);
                }
            }
        }
        //up left down left
        for(int c =0,r=0;c<8&&r<8;c++,r++) {
            p = getPiece(c, r);
            if(!sameTeam(p,king) && p!=null&&(p.name.equals("Bishop")||p.name.equals("Queen"))) {
                if(!p.movmentCollidWithPiece(king.col, king.row)){
                    checkPieces.add(p);
                }
            }
        }
        //up right up left
        for(int c = 7,r=0;c>=0&&r<8;c--,r++) {
            p = getPiece(c, r);
            if(!sameTeam(p,king) && p!=null&&(p.name.equals("Bishop")||p.name.equals("Queen"))) {
                if(!p.movmentCollidWithPiece(king.col, king.row)){
                    checkPieces.add(p);
                }
            }
        }
        //Pawn
        if(move.piece.name.equals("Pawn")) {
            int colorVar = king.isWhite ? -1: 1;
            p = getPiece(king.col + 1,king.row + colorVar);
            if(!sameTeam(p,king) && p!=null) {
                checkPieces.add(p);
            }
            p = getPiece(king.col - 1,king.row + colorVar);
            if(!sameTeam(p,king) && p!=null) {
                checkPieces.add(p);
            }
        }
        //knight
        if(move.piece.name.equals("Knight")) {
            p = getPiece(king.col - 1,king.row - 2);
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
            p = getPiece(king.col + 1,king.row - 2);//getPiece(kingCol + 1,kingRow - 2)
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
            p = getPiece(king.col + 2,king.row - 1);//getPiece(kingCol + 2,kingRow - 1)
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
            p = getPiece(king.col + 2,king.row +1);//getPiece(kingCol + 2,kingRow + 1)
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
            p = getPiece(king.col + 1,king.row + 2);//getPiece(kingCol + 1,kingRow + 2)
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
            p = getPiece(king.col - 1,king.row + 2);//getPiece(kingCol - 1,kingRow + 2)
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
            p = getPiece(king.col - 2,king.row + 1);//getPiece(kingCol - 2,kingRow + 1)
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
            p = getPiece(king.col - 2,king.row - 1);//getPiece(kingCol - 2,kingRow - 1)
            if(p!=null && !sameTeam(p,king)) {
                checkPieces.add(p);
            }
        }
        return checkPieces;
    }

    public boolean isMate(Move move){
        Piece king = findKing(!move.piece.isWhite);
        ArrayList<Piece> piecesGiveChack = pieceGiveCheck(move);

        for (Piece attackPiece:piecesGiveChack){
            if(attackPiece.name.equals("Queen")){

            }
            for (Piece dp:piecesList){
                if (dp.isWhite== king.isWhite){

                }
            }
        }
        return true;
    }


    public void capture(Piece piece){
        piecesList.remove(piece);
    }
    public boolean isValidMove(Move move){
        if(duck.youCanPlayWithDuck && !move.piece.name.equals("Duck")){
            return false;
        }
        if (move.captured == duck){
            return false;
        }
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
        piecesList.add(duck);

    }

    public void paint(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        //paint board
        for (int r=0;r<rows;r++){
            for (int c=0;c<cols;c++){
                g2d.setColor((c+r)%2==0? new Color(229, 191, 163):new Color(161, 96, 21));
                g2d.fillRect(c* tilesize,r* tilesize, tilesize, tilesize);
            }
        }
        //paint where you can go with the valid moves
        if(selectedPiece!=null){
            for (int r=0;r<rows;r++){
                for (int c=0;c<cols;c++){
                    if(isValidMove(new Move(this,selectedPiece,c,r))){
                        g2d.setColor(new Color(41, 201, 77,120));
                        g2d.fillRect(c* tilesize,r* tilesize, tilesize, tilesize);
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
