package controller;

import model.Board;
import model.Move;
import model.Piece;

import java.util.ArrayList;

public class CheckScanner {
    private static final int BOARD_SIZE = 8;
    private Board board;
    int oldCol;
    int oldRow;

    public CheckScanner(Board board) {
        this.board = board;
    }

    public boolean isKingChecked(Move move) {
        Piece king = board.findKing(move.piece.isWhite);
        assert king != null;

        oldCol = move.oldCol;
        oldRow = move.oldRow;

        int kingCol = king.col;
        int kingRow = king.row;

        if ((board.selectedPiece != null && board.selectedPiece.name.equals("King")) || move.piece.name.equals("King")) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        return hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, 1) || // up
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 1, 0) || // right
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, 0, -1) || // down
                hitByRook(move.newCol, move.newRow, king, kingCol, kingRow, -1, 0) || // left

                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, -1) || // up left
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, -1) || // up right
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, 1, 1) || // down right
                hitByBishop(move.newCol, move.newRow, king, kingCol, kingRow, -1, 1) || // down left

                hitByKnight(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByPawn(move.newCol, move.newRow, king, kingCol, kingRow) ||
                hitByKing(king, kingCol, kingRow);
    }


    public boolean hitByRook(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {
        //int i;
        for (int i = 1; i < 8; i++) {
            if (kingCol + (i * colVal) == col && kingRow + (i * rowVal) == row) {
                break;
            }
            Piece piece = board.getPiece(kingCol + (i * colVal), kingRow + (i * rowVal));
            if (piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece, king) && (piece.name.equals("Rook") || piece.name.equals("Queen"))) {
                    return true;
                }
                if (!(piece.col == oldCol && piece.row == oldRow)) {
                    break;
                }
            }
        }
        return false;
    }

    public boolean hitByBishop(int col, int row, Piece king, int kingCol, int kingRow, int colVal, int rowVal) {

        for (int i = 1; i < 8; i++) {
            if (kingCol - (i * colVal) == col && kingRow - (i * rowVal) == row) {
                break;
            }
            Piece piece = board.getPiece(kingCol - (i * colVal), kingRow - (i * rowVal));
            if (piece != null && piece != board.selectedPiece) {
                if (!board.sameTeam(piece, king) && (piece.name.equals("Bishop") || piece.name.equals("Queen"))) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public boolean hitByKnight(int col, int row, Piece king, int kingCol, int kingRow) {
        return checkKnight(board.getPiece(kingCol - 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow - 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow - 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol + 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 1, kingRow + 2), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow + 1), king, col, row) ||
                checkKnight(board.getPiece(kingCol - 2, kingRow - 1), king, col, row);
    }

    public boolean checkKnight(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Knight") && !(p.col == col && p.row == row);
    }

    public boolean hitByKing(Piece king, int kingCol, int kingRow) {
        return checkKing(board.getPiece(kingCol - 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow - 1), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow), king) ||
                checkKing(board.getPiece(kingCol - 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol + 1, kingRow + 1), king) ||
                checkKing(board.getPiece(kingCol, kingRow + 1), king);
    }

    public boolean checkKing(Piece p, Piece k) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("King");
    }

    public boolean hitByPawn(int col, int row, Piece king, int kingCol, int kingRow) {
        int colorVar = king.isWhite ? -1 : 1;
        return checkPawn(board.getPiece(kingCol + 1, kingRow + colorVar), king, col, row) ||
                checkPawn(board.getPiece(kingCol - 1, kingRow + colorVar), king, col, row);
    }

    public boolean checkPawn(Piece p, Piece k, int col, int row) {
        return p != null && !board.sameTeam(p, k) && p.name.equals("Pawn") && !(p.col == col && p.row == row);
    }
    public ArrayList<Piece> isAPieceCanGetHit(Piece piece){
        ArrayList<Piece> piecesAttack = new ArrayList<>();

        Piece attackingPiece;

        int currentRow = piece.row;
        int currentCol = piece.col;
        //check rook up
        // rook move start
        for (int i = 0;i<8;i++){
             attackingPiece = board.getPiece(piece.col,i);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Rook")||attackingPiece.name.equals("Queen")) ){
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        //check rook side
        for (int i = 0;i<8;i++){
             attackingPiece = board.getPiece(i,piece.row);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Rook")||attackingPiece.name.equals("Queen")) ){
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        //rook move end
        //bishop move start
        // Calculate possible moves in the top-left direction
        int col = piece.col - 1;
        int row = piece.row - 1;
        while (col >= 0 && row >= 0) {
            attackingPiece = board.getPiece(col,row);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Bishop")||attackingPiece.name.equals("Queen")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
            col--;
            row--;
        }

        // Calculate possible moves in the top-right direction
        col = piece.col + 1;
        row = piece.row - 1;
        while (col < 8 && row >= 0) { // Assuming a standard 8x8 chessboard
            attackingPiece = board.getPiece(col,row);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Bishop")||attackingPiece.name.equals("Queen")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
            col++;
            row--;
        }

        // Calculate possible moves in the bottom-left direction
        col = piece.col - 1;
        row = piece.row + 1;
        while (col >= 0 && row < 8) { // Assuming a standard 8x8 chessboard
            attackingPiece = board.getPiece(col,row);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Bishop")||attackingPiece.name.equals("Queen")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
            col--;
            row++;
        }

        // Calculate possible moves in the bottom-right direction
        col = piece.col + 1;
        row = piece.row + 1;
        while (col < 8 && row < 8) { // Assuming a standard 8x8 chessboard
            attackingPiece = board.getPiece(col,row);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Bishop")||attackingPiece.name.equals("Queen")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
            col++;
            row++;
        }
        //bishop move end
        //pawn movment start
        int colorIndex = piece.isWhite ? 1 : -1;

        // Capture left
        if (currentCol - 1 >= 0 && board.getPiece(currentCol - 1, currentRow - colorIndex) != null) {
            attackingPiece = board.getPiece(currentCol-1,currentRow-colorIndex);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Pawn")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }

        // Capture right
        if (currentCol + 1 < 8 && board.getPiece(currentCol + 1, currentRow - colorIndex) != null) {
            attackingPiece = board.getPiece(currentCol+1,currentRow-colorIndex);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Pawn")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }

        // En passant left
        if (board.getTileNum(currentCol - 1, currentRow) == board.enPassantTile && board.getPiece(currentCol - 1, currentRow + colorIndex) != null) {
            attackingPiece = board.getPiece(currentCol-1,currentRow-colorIndex);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Pawn")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }

        // En passant right
        if (board.getTileNum(currentCol + 1, currentRow) == board.enPassantTile && board.getPiece(currentCol + 1, currentRow + colorIndex) != null) {
            attackingPiece = board.getPiece(currentCol+1,currentRow-colorIndex);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Pawn")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        //pawn move end
        //knight move start
        //int[][] moves = {{-2, -1}, {-1, -2}, {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}};
        if((currentRow+2)<8 && (currentCol+1)<8){
            attackingPiece = board.getPiece(currentCol+1,currentRow+2);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        if((currentRow+2)<8 && (currentCol-1)>-1){
            attackingPiece = board.getPiece(currentCol-1,currentRow+2);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        if((currentRow+1)<8 && (currentCol-2)>-1){
            attackingPiece = board.getPiece(currentCol-2,currentRow+1);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        if((currentRow+1)<8 && (currentCol+2)<8){
            attackingPiece = board.getPiece(currentCol+2,currentRow+1);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        if((currentRow-2)>-1 && (currentCol+1)<8){
            attackingPiece = board.getPiece(currentCol+1,currentRow-2);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        if((currentRow-2)>-1 && (currentCol-1)>-1){
            attackingPiece = board.getPiece(currentCol-1,currentRow-2);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        if((currentRow-1)>-1 && (currentCol+2)<8){
            attackingPiece = board.getPiece(currentCol+2,currentRow-1);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        if((currentRow-1)>-1 && (currentCol-2)>-1){
            attackingPiece = board.getPiece(currentCol-2,currentRow-1);
            if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("Knight")) ) {
                Move move = new Move(board,attackingPiece,piece.col,piece.row);
                if (board.isValidPossibleMove(move)){
                    piecesAttack.add(attackingPiece);
                }
            }
        }
        //knight move end
        // king move start
        // Calculate possible moves in horizontal and vertical directions
        for (int dRow = -1; dRow <= 1; dRow++) {
            for (int dCol = -1; dCol <= 1; dCol++) {
                int newRow = currentRow + dRow;
                int newCol = currentCol + dCol;
                if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) { // Assuming a standard 8x8 chessboard
                    attackingPiece = board.getPiece(newCol,newRow);
                    if (attackingPiece!=null && !board.sameTeam(attackingPiece,piece) && (attackingPiece.name.equals("King")) ) {
                        Move move = new Move(board,attackingPiece,piece.col,piece.row);
                        if (board.isValidPossibleMove(move)){
                            piecesAttack.add(attackingPiece);
                        }
                    }
                }
            }
        }
        return piecesAttack;

    }
    public ArrayList<Move> checkMateScanner(ArrayList<Move>moveList,Move enemyMove){
        ArrayList<Move>posMoves;
        ArrayList<Move>safeMoves = new ArrayList<>();



        for (Move move:moveList){
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * board.tilesize;
            move.piece.yPos = move.newRow * board.tilesize;
            board.setPieceMap(move);

            if (move.piece.isWhite){
                posMoves = board.allValidMoves(board.whitePlayer);
            }else {
                posMoves = board.allValidMoves(board.blackPlayer);
            }
            if (posMoves.size()!=0){
                safeMoves.add(move);
            }
            Move tempMove = new Move(board,move.piece, move.oldCol,move.oldRow);
            board.setPieceMap(tempMove);
            move.piece.col = move.oldCol;
            move.piece.row = move.oldRow;
            move.piece.xPos = move.oldCol * board.tilesize;
            move.piece.yPos = move.oldRow * board.tilesize;


        }


        return safeMoves;
    }

}
