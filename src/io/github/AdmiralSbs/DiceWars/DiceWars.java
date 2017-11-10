package io.github.AdmiralSbs.DiceWars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class DiceWars extends JFrame {
    private static final long serialVersionUID = 1L;
    private static DiceWars dw;


    private DiceWars() throws IOException {
        setTitle("Dice Wars");
        setSize(1300, 740);
        setLocation(200, 200);
        setContentPane(new HexDisplay(new File("Maps" + File.separator + "1.dicewars")));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        addMouseListener(new MouseListen());
    }

    public static void main(String[] args) throws IOException {
        dw = new DiceWars();
        System.out.println(dw.getLocationOnScreen());
        System.out.println(dw.getContentPane().getLocationOnScreen());
    }

    private class MouseListen implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            Point pt = getMousePosition();
            pt.setLocation(pt.getX(), pt.getY() - 22);
            //System.out.println(pt.getX() + ", " + pt.getY());
            int c = findSelectedClump(pt);
            System.out.println(c);
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    private static int findSelectedClump(Point pt) {
        Clump[] clumps = ((HexDisplay) dw.getContentPane()).getClumps();
        //System.out.println(pt.getX() + ", " + pt.getY());
        for (int i = 1; i < clumps.length; i++) {
            //System.out.println(clumps[i]);
            if (clumps[i].contains(pt))
                return i;
        }
        return -1;
    }

}
