package main;


import artificialPlayer.Evaluations;
import pieces.*;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;


public class Board extends JPanel {
    public int tilesize = 85;
    public static int cols=8;
    public static int rows =8;
    public Player whitePlayer, blackPlayer;
    public ArrayList<Piece> piecesList= new ArrayList<>();
    boolean firstMove = true;
    public Piece selectedPiece;
    Input input = new Input(this);
    public CheckScanner checkScanner = new CheckScanner(this);
    HashMap<String, Method> moveState = new HashMap<>();
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
    public Map<Integer,Piece> pieceMap = new HashMap<>();
    int whiteCap = 0;
    int blackCap = 0;
    int custleMove = 0;
    Move lastMove;
    Piece tempDuckPiece = new Duck(this,0,0);
    public boolean endGame = false;





    public Board()
    {
        whitePlayer = new Player();
        blackPlayer = new Player();
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
    public  boolean isEndgame() {
        int majorPieces = 0;
        int minorPieces = 0;
        for (Piece piece : this.piecesList) {
            if (piece.name.equals("Queen")) {
                majorPieces++;
            } else if (piece.name.equals("Rook")) {
                majorPieces++;
            } else if (piece.name.equals("Bishop") || piece.name.equals("Knight")) {
                minorPieces++;
            }
        }
        // Consider it endgame if there are fewer than 2 major pieces and fewer than 4 minor pieces in total on the board,
        // or other conditions that you might find appropriate for your engine's understanding of an endgame scenario.
        return majorPieces < 2 && minorPieces < 4;
    }
    public boolean checkingIfDuckInterapt(Move DuckMove,Move bestWhiteMove) {
        tempDuckPiece.col = duck.col;
        tempDuckPiece.row = duck.row;
        tempDuckPiece.xPos = duck.xPos;
        tempDuckPiece.yPos = duck.yPos;
        DuckMove.piece.col = DuckMove.newCol;
        DuckMove.piece.row = DuckMove.newRow;
        DuckMove.piece.xPos = DuckMove.newCol * tilesize;
        DuckMove.piece.yPos = DuckMove.newRow * tilesize;

        if (!isValidPossibleMove(bestWhiteMove)){
            duck.col = tempDuckPiece.col;
            duck.row = tempDuckPiece.row;
            duck.xPos = tempDuckPiece.xPos;
            duck.yPos = tempDuckPiece.yPos;
            return true;
        }
        duck.col = tempDuckPiece.col;
        duck.row = tempDuckPiece.row;
        duck.xPos = tempDuckPiece.xPos;
        duck.yPos = tempDuckPiece.yPos;
        return false;
    }
    public void makeAduckMove(Move move){
        duck.youCanPlayWithDuck = false;
        whiteturn = whiteturn *-1;
        duck.isWhite = whiteturn == 1 ? true: false;
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tilesize;
        move.piece.yPos = move.newRow * tilesize;
        custleMove = 0;

        bitBoardChange(move);
    }


    public void artificialMove(Move move){
        ArrayList<Move> allValid;
        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }
        if(move.piece.name.equals("King")) {
            moveKing(move);
        }
        else if(!move.piece.name.equals("Duck")){
            duck.youCanPlayWithDuck = true;
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * tilesize;
            move.piece.yPos = move.newRow * tilesize;
            custleMove = 0;
            move.piece.isFirstMove = false;
            capture(move.captured);
        }
        bitBoardChange(move);
        setPieceMap(move);

        if (!move.piece.isWhite){
            allValid = allValidMoves(whitePlayer);
            if (allValid.size()==0){
                System.out.println("black Won");
                gameEnded(move);
            }
            else {
                Move bestDuckMove = Evaluations.bestDuckMoveEval(allValid);
                int colDuck = bestDuckMove.newCol;
                int rowDuck = bestDuckMove.newRow;
                Move duckMove = new Move(this,duck,colDuck,rowDuck);
                makeAduckMove(duckMove);
            }
        }
    }


    public void makeMove(Move move){
        if (!endGame){
            endGame = isEndgame();
        }
        ArrayList<Move> allValid;
        if (!move.piece.name.equals("Duck")){
            duck.youCanPlayWithDuck = true;
        }
        if (move.piece.name.equals("Duck")){
            makeAduckMove(move);
            if (!duck.isWhite){
                allValid = allValidMoves(blackPlayer);
                System.out.println(allValid);
                if (allValid.size()==0){
                    System.out.println("game ended");
                    gameEnded(move);
                }
                Move bestMove = Evaluations.bestMoveEval(allValid);
                if (bestMove!=null){
                    artificialMove(bestMove);
                }

            }
        }
        if(move.piece.name.equals("Pawn")){
            movePawn(move);
        }
        if(move.piece.name.equals("King")) {
            moveKing(move);
            setPieceMap(move);
        }
        else if(!move.piece.name.equals("Duck")){
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * tilesize;
            move.piece.yPos = move.newRow * tilesize;
            custleMove = 0;
            move.piece.isFirstMove = false;
            capture(move.captured);
            bitBoardChange(move);
            setPieceMap(move);
        }


    }
    private void moveKing(Move move) {
        if(Math.abs(move.newCol-move.piece.col)==2) {
            custleMove = 0;
            Piece rook;
            if(move.piece.col< move.newCol) {
                rook = this.getPiece(7, move.piece.row);
                rook.col =5;
                custleMove = 1;
            }
            else {
                rook = this.getPiece(0, move.piece.row);
                rook.col = 3;
                custleMove = 1;
            }
            rook.xPos = rook.col* tilesize;
        }
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tilesize;
        move.piece.yPos = move.newRow * tilesize;

        move.piece.isFirstMove = false;
        capture(move.captured);
        //setPieceMap(move);

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
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * tilesize;
        move.piece.yPos = move.newRow * tilesize;

        bitBoardChange(move);
        //setPieceMap(move);
        //premotion
        colorIndex = move.piece.isWhite ? 0:7;
        if (move.newRow==colorIndex){
            promotionPawn(move);
        }

        move.piece.isFirstMove = false;
        capture(move.captured);
        //setPieceMap(move);

    }
    public void unMakeMove(Move move){

    }
    private void promotionPawn(Move move) {
       int key = move.newRow*8+move.newCol;
        switch (this.typeOfpro){
           case 4:
               piecesList.add(new Queen(this,move.newCol,move.newRow,move.piece.isWhite));
               if (move.piece.isWhite) {
                   whitePlayer.pieces.put(key, new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
               } else {
                   blackPlayer.pieces.put(key, new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
               }
               break;
           case 3:
               piecesList.add(new Knight(this,move.newCol,move.newRow,move.piece.isWhite));
               if (move.piece.isWhite) {
                   whitePlayer.pieces.put(key, new Knight(this,move.newCol,move.newRow,move.piece.isWhite));
               } else {
                   blackPlayer.pieces.put(key, new Knight(this,move.newCol,move.newRow,move.piece.isWhite));
               }
               break;
           case 2:
               piecesList.add(new Bishop(this,move.newCol,move.newRow,move.piece.isWhite));
               if (move.piece.isWhite) {
                   whitePlayer.pieces.put(key, new Bishop(this,move.newCol,move.newRow,move.piece.isWhite));
               } else {
                   blackPlayer.pieces.put(key, new Bishop(this,move.newCol,move.newRow,move.piece.isWhite));
               }
               break;
           case 1:
               piecesList.add(new Rook(this,move.newCol,move.newRow,move.piece.isWhite));
               if (move.piece.isWhite) {
                   whitePlayer.pieces.put(key, new Rook(this,move.newCol,move.newRow,move.piece.isWhite));
               } else {
                   blackPlayer.pieces.put(key, new Rook(this,move.newCol,move.newRow,move.piece.isWhite));
               }
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
        System.out.println(allBitBoard.toBinaryString());

    }
    public void setPieceMap(Move move){
        int key = ((move.oldRow*8)+move.oldCol);
        int whereToGo = ((move.newRow*8)+move.newCol);
        Piece p;
        ArrayList<Move> allValid;
        if(!move.piece.name.equals("Duck")){

                if (move.piece.isWhite){
                    if (custleMove==1){
                        int rookKey = whereToGo==62?63:56;
                        int rookWhereToGo = rookKey==63?61:59;
                        Piece rook = whitePlayer.pieces.get(rookKey);
                        whitePlayer.pieces.remove(rookKey);
                        whitePlayer.pieces.put(rookWhereToGo,rook);
                    }
                    p = whitePlayer.pieces.get(key);
                    whitePlayer.pieces.remove(key);
                    whitePlayer.pieces.put(whereToGo,p);
                    //allValid = allValidMoves(blackPlayer);
                }
                else {
                    if (custleMove==1){
                        int rookKey = whereToGo==6?7:0;
                        int rookWhereToGo = rookKey==7?5:3;
                        Piece rook = blackPlayer.pieces.get(rookKey);
                        blackPlayer.pieces.remove(rookKey);
                        blackPlayer.pieces.put(rookWhereToGo,rook);
                    }
                    p = blackPlayer.pieces.get(key);
                    blackPlayer.pieces.remove(key);
                    blackPlayer.pieces.put(whereToGo,p);
                    //allValid = allValidMoves(whitePlayer);
                }
        }


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


    public ArrayList<Move> allValidMoves(Player player) {
        ArrayList<Move> movelist = new ArrayList<>();
        ArrayList<Integer> subMoves ;
        int currentCol;
        int currentRow;
        Piece cup;

        for (Piece set:player.pieces.values()){
            subMoves = set.getPossibleMoves(set.col,set.row);
            for (int index:subMoves){
                currentCol = index%8;
                currentRow = index/8;
                cup = getPiece(currentCol,currentRow);
                Move currentMove =new Move(this,set,currentCol,currentRow);
                currentMove.piece = set;
                currentMove.oldCol = set.col;
                currentMove.oldRow = set.row;
                currentMove.newCol = currentCol;
                currentMove.newRow = currentRow;
                currentMove.captured = cup;
                if (isValidPossibleMove(currentMove)){
                    movelist.add(currentMove);
                }
            }
            //System.out.println(subMoves);
        }
          return movelist;
    }

    public boolean isValidPossibleMove(Move move){
        if (gameOver == 1){
            return false;
        }
        if(getPiece(move.newCol,move.newRow)!=null){
            if(getPiece(move.newCol,move.newRow).name.equals("Duck")){
                return false;
            }
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
        if (checkScanner.isKingChecked(move)){
            return false;
        }
        return true;
    }
    public void capture(Piece piece){
        if (piece!=null){
            if (piece.name.equals("King")){
                if (!piece.isWhite){
                    System.out.println("WHITE WON BY MATE");
                    gameOver = 1;
                }
                else{
                    System.out.println("BLACK WON BY MATE");
                    gameOver = 1;
                }
            }
            piecesList.remove(piece);
            int key = ((piece.row*8)+piece.col);
            if (piece.isWhite){
                whitePlayer.pieces.remove(key);
                blackCap = blackCap +piece.value;
            }
            else{
                blackPlayer.pieces.remove(key);
                whiteCap = whiteCap +piece.value;
            }
        }
    }
    public boolean isValidMove(Move move){
        if (gameOver == 1){
            return false;
        }
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
    public void gameEnded(Move move) {

        String message = "";


        // Check for checkmate
        if (checkScanner.isKingChecked(move)){
            message = move.piece.isWhite ? "checkMate! Black player wins the game." : "checkMate! White player wins the game.";
            JOptionPane.showMessageDialog(null, message);
            System.exit(0);
        }
        // Check for stalemate
        else if (!checkScanner.isKingChecked(move)) {
            message = "StaleMate! you both noobs :(";
            JOptionPane.showMessageDialog(null, message);
            System.exit(0);
        }
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
        //white pawns
        piecesList.add(new Pawn(this,0,6,true));
        piecesList.add(new Pawn(this,1,6,true));
        piecesList.add(new Pawn(this,2,6,true));
        piecesList.add(new Pawn(this,3,6,true));
        piecesList.add(new Pawn(this,4,6,true));
        piecesList.add(new Pawn(this,5,6,true));
        piecesList.add(new Pawn(this,6,6,true));
        piecesList.add(new Pawn(this,7,6,true));
        //white pieces
        piecesList.add(new Rook(this,0,7,true));
        piecesList.add(new Knight(this,1,7,true));
        piecesList.add(new Bishop(this,2,7,true));
        piecesList.add(new Queen(this,3,7,true));
        piecesList.add(new King(this,4,7,true));
        piecesList.add(new Bishop(this,5,7,true));
        piecesList.add(new Knight(this,6,7,true));
        piecesList.add(new Rook(this,7,7,true));

//        //demo mate
//        Piece k = new King(this,1,0,false);
//        k.isFirstMove = false;
//        piecesList.add(k);
//        piecesList.add(new King(this,0,7,true));
//        piecesList.add(new Queen(this,3,7,false));
//        piecesList.add(new Pawn(this,7,1,false));
//        blackPlayer.pieces.put(1,piecesList.get(0));
//        whitePlayer.pieces.put(56,piecesList.get(1));
//        blackPlayer.pieces.put(60,piecesList.get(2));
//        blackPlayer.pieces.put(15,piecesList.get(3));


        //duck
        piecesList.add(duck);
//        //hashMap
        for(int i = 0;i<16;i++){
            blackPlayer.pieces.put(i, piecesList.get(i));
        }
        for (int j =48 ;j<64;j++){
            whitePlayer.pieces.put(j,piecesList.get(j-32));

        }

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
