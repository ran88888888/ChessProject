package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyJframe implements ActionListener{
    public JFrame frame;
    private Board board;
    private SidePanel sidePanel;
    JButton rookButton = new JButton("Make a Rook");
    JButton bishopButton = new JButton("Make a bishop");
    JButton knightButton = new JButton("Make a knight");
    JButton queenButton = new JButton("Make a queen");
    JLabel promoteMsg = new JLabel("promotion option");


    public MyJframe() {
        this.frame = new JFrame();
        this.board = new Board();
        this.sidePanel = new SidePanel();
        init();
    }

    private void init() {
        frame.getContentPane().setBackground(new Color(193, 154, 107));
        frame.setLayout(new GridBagLayout());
        frame.setMinimumSize(new Dimension(1200, 1200));
        frame.setLocationRelativeTo(null);

        promoteMsg.setVerticalTextPosition(JLabel.TOP);
        sidePanel.add(promoteMsg);

        rookButton.setBounds(700, 500, 200, 200);
        rookButton.addActionListener(this);
        sidePanel.add(rookButton);

        bishopButton.setBounds(700, 700, 200, 200);
        bishopButton.addActionListener(this);
        sidePanel.add(bishopButton);

        knightButton.setBounds(700, 900, 200, 200);
        knightButton.addActionListener(this);
        sidePanel.add(knightButton);

        queenButton.setBounds(700, 1100, 200, 200);
        queenButton.addActionListener(this);
        sidePanel.add(queenButton);

        frame.add(board);
        frame.add(sidePanel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == rookButton) {
            System.out.println("rook");
            this.board.typeOfpro = 1;
        }
        if (e.getSource() == bishopButton) {
            System.out.println("bishop");
            this.board.typeOfpro = 2;
        }
        if (e.getSource() == knightButton) {
            System.out.println("knight");
            this.board.typeOfpro = 3;
        }
        if (e.getSource() == queenButton) {
            System.out.println("queen");
            this.board.typeOfpro = 4;
        }
    }

}
