package io.github.AdmiralSbs.DiceWars;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class HexDisplay extends JPanel {
	private static final long serialVersionUID = 1L;
	private static int size = 30;
	private int height;
	private int width;
	private Hex[][] hex;
	private Clump[] clumps;
	private BufferedImage myImage;
	private Graphics2D drawer;

	public HexDisplay(File f) throws IOException {
		Scanner key = new Scanner(f);
		int[] temp = commaSplit(key.nextLine());
		width = temp[0];
		height = temp[1];
		hex = new Hex[width][height];
		clumps = new Clump[temp[2] + 1];
		List<List<Hex>> hexAssignments = new ArrayList<List<Hex>>();
		for (int i = 0; i < clumps.length; i++) {
			hexAssignments.add(new ArrayList<Hex>());
		}
		String[] tempS;
		for (int h = 0; h < height; h++) {
			temp = spaceSplit(key.nextLine());
			for (int w = 0; w < width; w++) {
				int clumpID = temp[w];
				if (w % 2 == 0)
					hex[w][h] = new Hex(.75 * size * (w + .75), Hex.sqrt3 * size / 4.0 * (2 * h + 1.5), size / 2.0,
							clumpID);
				else
					hex[w][h] = new Hex(.75 * size * (w + .75), Hex.sqrt3 * size / 4.0 * (2 * h + 2.5), size / 2.0,
							clumpID);
				// System.out.println(hexAssignments.size());
				// System.out.println(hexAssignments.get(clumpID).size());
				hexAssignments.get(clumpID).add(hex[w][h]);
				// System.out.println(hex[w][h].getX() + " " +
				// hex[w][h].getY());
			}
		}
		tempS = key.nextLine().split(",");
		for (int i = 1; i < clumps.length; i++) {
			Hex[] tem = (Hex[]) hexAssignments.get(i).toArray(new Hex[hexAssignments.get(i).size()]);
			System.out.println("Building clump " + i);
			clumps[i] = new Clump(tem, stringToColor(tempS[i-1]));
			System.out.println("Built clump " + i);
		}
		key.close();
		starting();
		ImageIO.write(myImage, "jpg", new File("outPic.jpg"));
	}

	public void starting() {
		setPreferredSize(new Dimension(1280, 720));
		setLayout(new FlowLayout());
		myImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		drawer = myImage.createGraphics(); // Here
		drawer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		drawer.setColor(Color.WHITE);
		drawer.fillRect(0, 0, 1280, 720);
		// drawHex(drawer);
		drawClumps(drawer);
		repaint();
	}

	public int[] commaSplit(String s) {
		String[] str = s.split(",");
		int[] ret = new int[str.length];
		for (int i = 0; i < str.length; i++) {
			ret[i] = Integer.parseInt(str[i]);
		}
		return ret;
	}
	
	public int[] spaceSplit(String s) {
		String[] str = s.split(" ");
		int[] ret = new int[str.length];
		for (int i = 0; i < str.length; i++) {
			ret[i] = Integer.parseInt(str[i]);
		}
		return ret;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		g2.drawImage(myImage, 0, 0, this);
		// System.out.println("Painted");
	}

	public void drawHex(Graphics g) {
		for (int w = 0; w < width; w++) {
			for (int h = 0; h < height; h++) {
				hex[w][h].draw(g);
			}
		}
		repaint();
	}

	public void drawClumps(Graphics g) {
		for (int i = 1; i < clumps.length; i++) {
			clumps[i].draw((Graphics2D) g);
		}
		repaint();
	}

	private Color stringToColor(String s) {
		try {
			return (Color) Color.class.getField(s.toLowerCase()).get(null);
		} catch (Exception e) {
			return Color.black;
		}
	}

	public static int getHSize() {
		return size;
	}
}