package pieces;

import main.Board;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Duck extends Piece {
    private BufferedImage sheet; // Declaring BufferedImage as a private field
    public boolean isDuckMoved;
    public boolean youCanPlayWithDuck = false;
    public Duck(Board board, int col, int row) {
        super(board);
        this.col = col;
        this.row = row;
        this.xPos = col * board.titlesize;
        this.yPos = row * board.titlesize;
        this.name = "Duck";
        this.isDuckMoved = false;


        try {
            // Load the image from the resource file in the constructor
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("Duck.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidMovement(int col, int row) {
        Piece p = board.getPiece(col, row);
        return p == null && youCanPlayWithDuck;
    }
}