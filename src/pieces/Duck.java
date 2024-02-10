package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Duck extends Piece {

    public boolean youCanPlayWithDuck;
    public Duck(Board board, int col, int row) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.tilesize;
        this.yPos = row * board.tilesize;
        this.name = "Duck";
        this.youCanPlayWithDuck = false;
        this.isWhite = true;


        try {
            // Load the image from the resource file in the constructor
            this.sprite = ImageIO.read(ClassLoader.getSystemResourceAsStream("Duck.png")).getScaledInstance(board.tilesize,board.tilesize, BufferedImage.SCALE_SMOOTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidMovement(int col, int row) {
        Piece p = board.getPiece(col, row);
        return p == null && youCanPlayWithDuck;
    }
}