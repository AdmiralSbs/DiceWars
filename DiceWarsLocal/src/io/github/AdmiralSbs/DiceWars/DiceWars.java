package io.github.AdmiralSbs.DiceWars;

import javax.swing.JFrame;
import java.awt.*;
import java.io.*;

public class DiceWars extends JFrame {
	private static final long serialVersionUID = 1L;

	public DiceWars() throws IOException {
		setTitle("Dice Wars");
        setSize(1300,740);
        setLocation(200,200);
        setContentPane(new HexDisplay(new File("Maps" + File.separator + "1.dicewars")));
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args) throws IOException {
		DiceWars dw = new DiceWars();
		dw.setLayout(new FlowLayout());
	}

}
