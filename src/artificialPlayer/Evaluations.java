package artificialPlayer;

import main.Board;
import main.Move;
import pieces.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Evaluations {
    static Board board;
    public Evaluations(Board board){
        this.board = board;
    }
    public static void picesValueEval(ArrayList<Move> moveList){
        for (Move m :moveList){
            if (m.captured!=null){
                if (!m.captured.name.equals("King")) {
                    m.score = m.score+shouldMakeTrade(m);
                }
            }
        }
    }
    public static int shouldMakeTrade(Move move){
        ArrayList<Integer> temp;
        ArrayList<Piece> attackPieces;
        int countScore;
        if (move.captured!=null){
            countScore = move.captured.value;
            if (move.captured.name.equals("King")){
                return countScore;
            }
        }else {
            countScore = 0;
        }
        Piece tempPiece = new Piece(board);
        tempPiece.isWhite = move.piece.isWhite;
        tempPiece.row = move.newRow;
        tempPiece.col = move.newCol;
        tempPiece.value = move.piece.value;

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * board.tilesize;
        move.piece.yPos = move.newRow * board.tilesize;
        board.piecesList.remove(move.captured);


        int found = 0;

        attackPieces = board.checkScanner.isAPieceCanGetHit(move.piece);
        for (Piece p:attackPieces){
            found = 0;
            Move tempMove = new Move(board,p,tempPiece.col,tempPiece.row);
            temp = p.getPossibleMoves(p.col, p.row);
            for (Integer i : temp) {
                int col = i % 8;
                int row = i / 8;
                if (board.getPiece(col, row) == null) {
                    Move tempDuckMove = new Move(board, board.duck, col, row);
                    if (board.checkingIfDuckInterapt(tempDuckMove, tempMove)) {
                        found = 1;
                    }
                }
            }
            if (found==0){

                move.piece.col = move.oldCol;
                move.piece.row = move.oldRow;
                move.piece.xPos = move.oldCol * board.tilesize;
                move.piece.yPos = move.oldRow * board.tilesize;
                board.piecesList.add(move.captured);

                return countScore-move.piece.value;
            }
        }
        move.piece.col = move.oldCol;
        move.piece.row = move.oldRow;
        move.piece.xPos = move.oldCol * board.tilesize;
        move.piece.yPos = move.oldRow * board.tilesize;
        board.piecesList.add(move.captured);
        return countScore;

    }
    public static void defendPiece(ArrayList<Move> moveList){
        ArrayList<Piece>attackPieces ;
        ArrayList<Piece>subAttackPieces ;
        HashMap<Integer,Piece> pieces;
        ArrayList<Integer> moveOp;
        Move opMove = null;
        int moveScore;
        if (moveList.get(0).piece.isWhite){
            pieces = (HashMap<Integer, Piece>) board.whitePlayer.pieces;
        }else {
            pieces = (HashMap<Integer, Piece>) board.blackPlayer.pieces;
        }
        for (Piece p:pieces.values()){
            attackPieces = board.checkScanner.isAPieceCanGetHit(p);
            subAttackPieces = board.checkScanner.isAPieceCanGetHit(p);
            if (attackPieces.size()!=0){
                for (Piece attackP:attackPieces){
                    Move newMove = new Move(board,attackP,p.col,p.row);
                    moveScore = shouldMakeTrade(newMove);
                    if (moveScore>0){
                        moveOp = p.getPossibleMoves(p.col, p.row);
                        while(moveOp.size()>0&&subAttackPieces.size()!=0){
                            opMove = new Move(board,p,moveOp.get(moveOp.size()-1)%8,moveOp.get(moveOp.size()-1)/8);
                            opMove.piece.col = opMove.newCol;
                            opMove.piece.row = opMove.newRow;
                            opMove.piece.xPos = opMove.newCol * board.tilesize;
                            opMove.piece.yPos = opMove.newRow * board.tilesize;
                            subAttackPieces = board.checkScanner.isAPieceCanGetHit(opMove.piece);
                            moveOp.remove(moveOp.size()-1);
                            opMove.piece.col = opMove.oldCol;
                            opMove.piece.row = opMove.oldRow;
                            opMove.piece.xPos = opMove.oldCol * board.tilesize;
                            opMove.piece.yPos = opMove.oldRow * board.tilesize;
                        }

                        if (opMove!=null){
                            if (subAttackPieces.size()==0){
                                int i = 0;
                                while (i<moveList.size()){
                                    if (moveList.get(i).equals(opMove)){
                                        moveList.get(i).score += moveScore;
                                    }
                                    i++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    public static Move bestMoveEval(ArrayList<Move>moveList){
        if (moveList.size()==0){
            return null;
        }
        //all the function that change the nove score
        picesValueEval(moveList);
        defendPiece(moveList);
        // chose the best move

        Move bestMove = moveList.get((int) (Math.random()*moveList.size()));

        for (Move m:moveList){
            if (m.captured!=null && m.captured.name.equals("King")){
                bestMove = m;
                return bestMove;
            }
        }
        Collections.sort(moveList);
        moveList.get(0).toString();
        System.out.println(moveList.get(0).score);
        return moveList.get(0);
    }
    public static Move bestDuckMoveEval(ArrayList<Move>moveList) {
        Move bestDuckMove = null;
        ArrayList<Move> moveListTemp = moveList;
        Move bestWhiteMove;
        ArrayList<Integer> temp;
        while (bestDuckMove == null) {
            bestWhiteMove = bestMoveEval(moveListTemp);
            if (bestWhiteMove==null){
                return null;
            }
            temp = bestWhiteMove.piece.getPossibleMoves(bestWhiteMove.piece.col, bestWhiteMove.piece.row);
            for (Integer i : temp) {
                int col = i % 8;
                int row = i / 8;
                if (board.getPiece(col, row) == null) {
                    Move tempMove = new Move(board, board.duck, col, row);
                    if (board.checkingIfDuckInterapt(tempMove, bestWhiteMove)) {
                        bestDuckMove = tempMove;
                        return bestDuckMove;
                    }
                }
            }
            moveListTemp.remove(0);

        }
        return bestDuckMove;
    }

}
