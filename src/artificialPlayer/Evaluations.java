package artificialPlayer;

import main.Board;
import main.Move;
import pieces.Piece;
import java.util.ArrayList;

public class Evaluations {
    static Board board;
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
        // coose the best move
        if (moveList.size()==0){
            return null;
        }
        Move bestMove = moveList.get((int) (Math.random()*moveList.size()));

        for (Move m:moveList){
            if (m.captured!=null && m.captured.name.equals("King")){
                bestMove = m;
                return bestMove;
            }
            if (m.score>bestMove.score){
                bestMove = m;
            }
        }

        return bestMove;
    }
    public static Move bestDuckMoveEval(ArrayList<Move>moveList){
        Move bestWhiteMove = bestMoveEval(moveList);
        ArrayList<Integer> temp = bestWhiteMove.piece.getPossibleMoves(bestWhiteMove.piece.col,bestWhiteMove.piece.row);
        Move bestDuckMove = null;
        for (Integer i:temp){
            int col = i%8;
            int row = i/8;
            if (board.getPiece(col,row)==null){
                Move tempMove = new Move(board,board.duck,col,row);
                if(board.checkingIfDuckInterapt(tempMove,bestWhiteMove)){
                    bestDuckMove = tempMove;
                }
            }
        }
        if (bestDuckMove==null){
            ArrayList<Move> temp2 = new ArrayList<>();
            for (Move m:moveList){
                if (m.captured==null){
                    temp2.add(m);
                }
            }
            bestDuckMove = bestMoveEval(temp2);
        }
        return bestDuckMove;

    }

    
}
