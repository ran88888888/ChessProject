package pieces;

import main.Board;
import main.Move;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Piece implements Comparable<Piece> {
    public int col, row; // Corrected variable names
    public int xPos, yPos; // Corrected variable names
    public boolean isWhite; // Corrected variable name
    public String name;
    public int value;
    public boolean isFirstMove = true;


    BufferedImage sheet;
    {

        try {
            sheet = ImageIO.read(ClassLoader.getSystemResourceAsStream("pieces.png")); // Corrected method call and parameter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected int sheetScale = sheet.getWidth()/6;
    Image sprite;
    Board board;
    public Piece(Board board){
        this.board = board;
    }

    public boolean isValidMovement(int col,int row){
        return true;
    }
    public boolean movmentCollidWithPiece(int col,int row){
        return false;
    }
    public ArrayList<Integer> getPossibleMoves(int currentCol, int currentRow) {
        return null;
    }

    public void paint(Graphics2D g2d){
        g2d.drawImage(sprite,xPos,yPos,null);
    }

    @Override
    public int compareTo(Piece o) {
        return Integer.compare( o.value,this.value);
    }
}
