package io.github.AdmiralSbs.DiceWars;

        import javax.swing.*;
        import java.awt.*;
        import java.io.*;

public class DiceWars extends JFrame {
    private static final long serialVersionUID = 1L;

    private DiceWars() throws IOException {
        setTitle("Dice Wars");
        setSize(1300,740);
        setLocation(200,200);
        setContentPane(new HexDisplay(new File("Maps" + File.separator + "1.dicewars")));
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
    }

    public static void main(String[] args) throws IOException {
        DiceWars dw = new DiceWars();
    }

}
