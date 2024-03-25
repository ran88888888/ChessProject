package artificialPlayer;

import main.Board;
import main.Move;
import pieces.Pawn;
import pieces.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Evaluations {
    static Board board;
    private static int[][] pawnEval = {
            {0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5, -10,  0,  0, -10, -5,  5},
            {5, 10, 10, -20, -20, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static int[][] knightEval = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20,   0,   0,   0,   0, -20, -40},
            {-30,   0,  10,  15,  15,  10,   0, -30},
            {-30,   5,  15,  20,  20,  15,   5, -30},
            {-30,   0,  15,  20,  20,  15,   0, -30},
            {-30,   5,  10,  15,  15,  10,   5, -30},
            {-40, -20,   0,   5,   5,   0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}
    };

    private static int[][] bishopEval = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10,   0,   5,  10,  10,   5,   0, -10},
            {-10,   5,   5,  10,  10,   5,   5, -10},
            {-10,   0,  10,  10,  10,  10,   0, -10},
            {-10,  10,  10,  10,  10,  10,  10, -10},
            {-10,   5,   0,   0,   0,   0,   5, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}
    };

    private static int[][] rookEval = {
            {0,   0,   0,   0,   0,   0,   0,   0},
            {5,  10,  10,  10,  10,  10,  10,   5},
            {-5,   0,   0,   0,   0,   0,   0,  -5},
            {-5,   0,   0,   0,   0,   0,   0,  -5},
            {-5,   0,   0,   0,   0,   0,   0,  -5},
            {-5,   0,   0,   0,   0,   0,   0,  -5},
            {-5,   0,   0,   0,   0,   0,   0,  -5},
            {0,   0,   0,   5,   5,   0,   0,   0}
    };

    private static int[][] queenEval = {
            {-20, -10, -10,  -5,  -5, -10, -10, -20},
            {-10,   0,   0,   0,   0,   0,   0, -10},
            {-10,   0,   5,   5,   5,   5,   0, -10},
            { -5,   0,   5,   5,   5,   5,   0,  -5},
            {  0,   0,   5,   5,   5,   5,   0,  -5},
            {-10,   5,   5,   5,   5,   5,   0, -10},
            {-10,   0,   5,   0,   0,   0,   0, -10},
            {-20, -10, -10,  -5,  -5, -10, -10, -20}
    };

    private static int[][] kingEval = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            { 20,  20,   0,   0,   0,   0,  20,  20},
            { 20,  30,  10,   0,   0,  10,  30,  20}
    };

    private static int[][] kingEndgameEval = {
            {-50, -40, -30, -20, -20, -30, -40, -50},
            {-30, -20, -10,   0,   0, -10, -20, -30},
            {-30, -10,  20,  30,  30,  20, -10, -30},
            {-30, -10,  30,  40,  40,  30, -10, -30},
            {-30, -10,  30,  40,  40,  30, -10, -30},
            {-30, -10,  20,  30,  30,  20, -10, -30},
            {-30, -30,   0,   0,   0,   0, -30, -30},
            {-50, -30, -30, -30, -30, -30, -30, -50}
    };
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
    public static float shouldMakeTrade(Move move){
        ArrayList<Integer> temp;
        ArrayList<Piece> attackPieces;
        float countScore;
        if (move.captured!=null){
            countScore = move.captured.value;
            if (move.captured.name.equals("King")){
                return countScore;
            }
        }else {
            countScore = 0;
        }

        move.piece.col = move.newCol;
        move.piece.row = move.newRow;
        move.piece.xPos = move.newCol * board.tilesize;
        move.piece.yPos = move.newRow * board.tilesize;
        board.piecesList.remove(move.captured);


        int found = 0;

        attackPieces = board.checkScanner.isAPieceCanGetHit(move.piece);
        for (Piece p:attackPieces){
            found = 0;
            Move tempMove = new Move(board,p,move.newCol,move.newRow);
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
        float moveScore;
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
                        pieceDefebdPiece(moveList,p);
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
    public static void pieceDefebdPiece(ArrayList<Move>moveList,Piece pieceToDefend){
        ArrayList<Piece>attackPieces;
        ArrayList<Integer>posMove;
        float maxScore = 0;
        attackPieces = board.checkScanner.isAPieceCanGetHit(pieceToDefend);
        float moveScore;
        for (Move move:moveList){
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * board.tilesize;
            move.piece.yPos = move.newRow * board.tilesize;
            maxScore = 0;
            for (Piece attackP:attackPieces){
                Move newMove = new Move(board,attackP,pieceToDefend.col,pieceToDefend.row);
                moveScore = shouldMakeTrade(newMove);
                if (moveScore<=0){
                    moveScore = moveScore*-1;
                    if (maxScore<moveScore){
                        maxScore = moveScore;
                    }
                }
            }
            move.score+=maxScore;
            move.piece.col = move.oldCol;
            move.piece.row = move.oldRow;
            move.piece.xPos = move.oldCol * board.tilesize;
            move.piece.yPos = move.oldRow * board.tilesize;
        }
    }
    public static void willBeMatePrevent(ArrayList<Move>moveList){
        HashMap<Integer,Piece> pieces;
        ArrayList<Move> attacMoves;
        ArrayList<Move> defenceMove;
        ArrayList<Move>counterMoves;
        ArrayList<Move>sheredCounterMove = new ArrayList<>();
        if (!moveList.get(0).piece.isWhite){
            attacMoves = board.allValidMoves(board.whitePlayer);
            pieces = (HashMap<Integer, Piece>) board.whitePlayer.pieces;
        }else {
            attacMoves = board.allValidMoves(board.blackPlayer);
            pieces = (HashMap<Integer, Piece>) board.blackPlayer.pieces;
        }
        for (Move aM:attacMoves){
            aM.piece.col = aM.newCol;
            aM.piece.row = aM.newRow;
            aM.piece.xPos = aM.newCol * board.tilesize;
            aM.piece.yPos = aM.newRow * board.tilesize;
            board.setPieceMap(aM);
            if (aM.piece.isWhite){
                defenceMove = board.allValidMoves(board.blackPlayer);
            }
            else{
                defenceMove = board.allValidMoves(board.whitePlayer);
            }
            if (defenceMove.size()==0){
                counterMoves =board.checkScanner.checkMateScanner(moveList,aM);
                if (counterMoves.size()!=0){
                    if (sheredCounterMove.size()!=0){
                        int i = 0;
                        while (i<sheredCounterMove.size()&&i<counterMoves.size()){
                            if (!sheredCounterMove.get(i).equals(counterMoves.get(i))){
                                sheredCounterMove.remove(i);
                            }
                            i++;
                        }
                    }
                    else {
                        sheredCounterMove = counterMoves;
                    }
                }
            }
            Move tempMove = new Move(board,aM.piece,aM.oldCol,aM.oldRow);
            board.setPieceMap(tempMove);
            aM.piece.col = aM.oldCol;
            aM.piece.row = aM.oldRow;
            aM.piece.xPos = aM.oldCol * board.tilesize;
            aM.piece.yPos = aM.oldRow * board.tilesize;


        }
        moveList = sheredCounterMove;

    }
    public static void bestPlaceToBe(ArrayList<Move>moveList){
        for (Move move:moveList){
            switch (move.piece.name){
                case ("Pawn"):{
                    if (move.piece.isWhite){
                        move.score = move.score+ pawnEval[move.newRow][move.newCol];
                    }
                    else {
                        move.score = move.score+ pawnEval[7-move.newRow][move.newCol];
                    }

                    break;
                }
                case ("Knight"):{
                    if (move.piece.isWhite){
                        move.score = move.score+ knightEval[move.newRow][move.newCol];
                    }
                    else {
                        move.score = move.score+ knightEval[7-move.newRow][move.newCol];
                    }

                    break;
                }
                case ("Rook"):{
                    if (move.piece.isWhite){
                        move.score = move.score+ rookEval[move.newRow][move.newCol];
                    }
                    else {
                        move.score = move.score+ rookEval[7-move.newRow][move.newCol];
                    }

                    break;
                }
                case ("Bishop"):{
                    if (move.piece.isWhite){
                        move.score = move.score+ bishopEval[move.newRow][move.newCol];
                    }
                    else {
                        move.score = move.score+ bishopEval[7-move.newRow][move.newCol];
                    }

                    break;
                }
                case ("Queen"):{
                    if (move.piece.isWhite){
                        move.score = move.score+ queenEval[move.newRow][move.newCol];
                    }
                    else {
                        move.score = move.score+ queenEval[7-move.newRow][move.newCol];
                    }

                    break;
                }
                case ("King"):{
                    if (board.endGame){
                        if (move.piece.isWhite){
                            move.score = move.score+ kingEndgameEval[move.newRow][move.newCol];
                        }
                        else {
                            move.score = move.score+ kingEndgameEval[7-move.newRow][move.newCol];
                        }
                    }
                    else{
                        if (move.piece.isWhite){
                            move.score = move.score+ kingEval[move.newRow][move.newCol];
                        }
                        else {
                            move.score = move.score+ kingEval[7-move.newRow][move.newCol];
                        }

                    }
                    break;
                }
            }
        }
    }
    public static void isMovePutDamage(ArrayList<Move>moveList){
        ArrayList<Piece>attackingPieces;
        float scoreCount;
        for (Move move:moveList){
            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * board.tilesize;
            move.piece.yPos = move.newRow * board.tilesize;
            board.setPieceMap(move);

            attackingPieces = board.checkScanner.isAPieceCanGetHit(move.piece);
            if (attackingPieces.size()!=0){
                for (Piece atPiece:attackingPieces){
                    Move atMove = new Move(board,atPiece, move.newCol, move.newRow);
                    scoreCount = shouldMakeTrade(atMove);
                    if (scoreCount>0){
                        move.score = move.score - scoreCount;
                    }
                }
            }
            Move tempMove = new Move(board,move.piece,move.oldCol,move.oldRow);
            board.setPieceMap(tempMove);
            move.piece.col = move.oldCol;
            move.piece.row = move.oldRow;
            move.piece.xPos = move.oldCol * board.tilesize;
            move.piece.yPos = move.oldRow * board.tilesize;
        }
    }
    public static boolean isDoubledPwan(Move move){
        for(int i = 0;i<8;i++) {
            Piece anotherPawn = board.getPiece(move.newCol, i);
            if (anotherPawn != null && anotherPawn.name.equals("Pawn")&&board.sameTeam(move.piece,anotherPawn)) {
                return true;
            }
        }
        return false;
    }
    public static boolean isIsolatePawn(Move move){
        int colorIndex = move.piece.isWhite?1:-1;
        int found = 0;
        Piece anotherPawn;
        for (int i = -1 ;i<2;i=i+2){
            anotherPawn = board.getPiece(move.newCol+i,move.newRow+colorIndex);
            if (anotherPawn!=null&& anotherPawn.name.equals("Pawn")&&board.sameTeam(move.piece,anotherPawn)){
                found = 1;
            }
            anotherPawn = board.getPiece(move.newCol+i,move.newRow+(colorIndex*2));
            if (anotherPawn!=null&& anotherPawn.name.equals("Pawn")&&board.sameTeam(move.piece,anotherPawn)){
                found = 1;
            }
        }
        if (found==0){
            return true;
        }
        return false;
    }
    public static void pieceProtectPiece(ArrayList<Move>moveList){
        float maxScore;

        ArrayList<Piece>attackingPieces;
        ArrayList<Integer>posMovment;
        for (Move move:moveList){
            float beforeScore = 0;
            float newScore = 0;
            posMovment = move.piece.getPossibleMoves(move.oldCol, move.oldRow);
            for (Integer tile:posMovment){
                Piece defendedPiece = board.getPiece(tile%8,tile/8);
                if (defendedPiece!=null&&board.sameTeam(move.piece,defendedPiece)){
                    attackingPieces = board.checkScanner.isAPieceCanGetHit(defendedPiece);
                    for (Piece atP:attackingPieces){
                        Move atMove = new Move(board,atP,defendedPiece.col,defendedPiece.row);
                        beforeScore = beforeScore+shouldMakeTrade(atMove);

                        move.piece.col = move.newCol;
                        move.piece.row = move.newRow;
                        move.piece.xPos = move.newCol * board.tilesize;
                        move.piece.yPos = move.newRow * board.tilesize;
                        board.setPieceMap(move);

                        newScore = newScore+shouldMakeTrade(atMove);

                        Move tempMove = new Move(board,move.piece,move.oldCol,move.oldRow);
                        board.setPieceMap(tempMove);
                        move.piece.col = move.oldCol;
                        move.piece.row = move.oldRow;
                        move.piece.xPos = move.oldCol * board.tilesize;
                        move.piece.yPos = move.oldRow * board.tilesize;
                    }
                }
            }
            move.score = move.score+beforeScore-newScore;
        }
    }
    public static void pawnStructure(ArrayList<Move>moveList){
        for (Move move:moveList){
            if (move.piece.name.equals("Pawn")){
                //double pawns
                if (isDoubledPwan(move)){
                    move.score = move.score-50;
                }
                //isolate pawns
                if (isIsolatePawn(move)){
                    move.score = move.score-50;
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
        willBeMatePrevent(moveList);
        bestPlaceToBe(moveList);
        isMovePutDamage(moveList);
        //pawnStructure(moveList);
        pieceProtectPiece(moveList);
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
