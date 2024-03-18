package artificialPlayer;

import main.Board;
import main.Move;
import pieces.Piece;
import java.util.ArrayList;
import java.util.Collections;

public class Evaluations {
    static Board board;
    static int index = -1;
    static int jump = -1;
    public Evaluations(Board board){
        this.board = board;
    }
    public static void picesValueEval(ArrayList<Move> moveList){
        for (Move m :moveList){
            if (m.captured!=null){
                m.score = m.score + m.captured.value;
            }
        }
    }
    public static Move bestMoveEval(ArrayList<Move>moveList){
        //all the function that change the nove score
        picesValueEval(moveList);
        // chose the best move
        if (moveList.size()==0){
            return null;
        }
        Move bestMove = moveList.get((int) (Math.random()*moveList.size()));

        for (Move m:moveList){
            if (m.captured!=null && m.captured.name.equals("King")){
                bestMove = m;
                return bestMove;
            }
        }
        Collections.sort(moveList);
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
