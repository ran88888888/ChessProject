package controller;

import model.Board;
import model.Move;
import model.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {

    Board board;

    public Input(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.tilesize;
        int row = e.getY() / board.tilesize;


        Piece piece = board.getPiece(col, row);
        if (piece != null) {
            board.selectedPiece = piece;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null) {
            board.selectedPiece.xPos = e.getX() - board.tilesize / 2;
            board.selectedPiece.yPos = e.getY() - board.tilesize / 2;

            board.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = e.getX() / board.tilesize;
        int row = e.getY() / board.tilesize;

        if (board.selectedPiece != null) {
            Move move = new Move(board, board.selectedPiece, col, row);


            if (board.isValidMove(move)&&(col>=0&&col<=7)&&(row>=0&&row<=7)){
                board.makeMove(move);
            }
            else
            {
                board.selectedPiece.xPos = board.selectedPiece.col * board.tilesize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tilesize;
            }

            board.selectedPiece = null;
            board.repaint();

        }


    }
}
