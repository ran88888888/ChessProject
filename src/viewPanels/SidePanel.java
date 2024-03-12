package viewPanels;

import main.Board;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SidePanel extends JPanel  {

    Board board;
    int hight = 8*85;
    int width = 2*85;
    Border border = BorderFactory.createLineBorder(Color.darkGray,5);
    public SidePanel(){
        this.setBorder(border);
        this.setPreferredSize(new Dimension(width,hight));
        //this.setBackground(Color.green);

    }


}
