package io.github.AdmiralSbs.DiceWars;

import java.awt.*;
import java.awt.geom.*;

public class Hex extends Path2D.Double {

	private static final long serialVersionUID = 1L;
	public static final Color DEFAULTCOLOR = Color.black;
	public static final double sqrt3 = Math.sqrt(3);
	private double x;
	private double y;
	private double[] xPD;
	private double[] yPD;
	private Point2D.Double[] points = new Point2D.Double[6];
	private double radius;
	private Color color;
	private int parentID;

	public Hex() {
		radius = 10;
		setCoords(0, 0);
		color = DEFAULTCOLOR;
	}

	public Hex(double x, double y) {
		radius = 10;
		setCoords(x, y);
		color = DEFAULTCOLOR;
	}

	public Hex(double x, double y, double s) {
		radius = s;
		setCoords(x, y);
		color = DEFAULTCOLOR;
		// System.out.println("Made it");
	}

	public Hex(double x, double y, double s, Color c) {
		radius = s;
		setCoords(x, y);
		color = c;
	}
	
	public Hex(double x, double y, double s, int id) {
		radius = s;
		setCoords(x, y);
		color = DEFAULTCOLOR;
		parentID = id;
	}
	
	public Hex(double x, double y, double s, Color c, int id) {
		radius = s;
		setCoords(x, y);
		color = c;
		parentID = id;
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		g2.fill(this);
		g2.setColor(DEFAULTCOLOR);
		g2.draw(this);
		// g.drawString(x + " " + y, x, y);
		// System.out.println("Drew solo");
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRadius() {
		return radius;
	}

	public Color getColor() {
		return color;
	}
	
	public double[] getXPD() {
		return xPD;
	}
	
	public double[] getYPD() {
		return yPD;
	}
	
	public Point2D.Double[] getPoints() {
		return points;
	}
	
	public int getID() {
		return parentID;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	public void setCoords(double x, double y) {
		this.x = x;
		this.y = y;
		setPoints();
	}

	public void setPoints() {
		reset();
		double[] xPD = { x - (0.5 * radius), x + (0.5 * radius), x + radius,
				x + (0.5 * radius), x - (0.5 * radius), x - radius };
		double[] yPD = { y + (sqrt3 * radius / 2), y + (sqrt3 * radius / 2), y,
				y - (sqrt3 * radius / 2), y - (sqrt3 * radius / 2), y };
		moveTo(xPD[0], yPD[0]);
		points[0] = (Point2D.Double) getCurrentPoint();
		for (int i = 1; i < 6; i++) {
			lineTo(xPD[i], yPD[i]);
			points[i] = (Point2D.Double) getCurrentPoint();
		}
		lineTo(xPD[0], yPD[0]);
		this.xPD = xPD;
		this.yPD = yPD;
		//System.out.println(Arrays.toString(xPD));
		//System.out.println(Arrays.toString(yPD));
	}
}
