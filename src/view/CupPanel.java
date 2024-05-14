package view;

import model.Board;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class CupPanel extends JPanel {
    Board board;
    int hight = 8 * 85;
    int width = 2 * 85;
    Border border = BorderFactory.createLineBorder(Color.darkGray, 5);

    public CupPanel() {
        this.setBorder(border);
        this.setPreferredSize(new Dimension(width, hight));
        //this.setBackground(Color.green);

    }
}
