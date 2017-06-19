package io.github.AdmiralSbs.DiceWars;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Clump extends Path2D.Double {
	private static final long serialVersionUID = 1L;
	private Hex[] hex;
	private ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
	private ArrayList<Point2D.Double> finalPoints = new ArrayList<Point2D.Double>();
	private Color color;

	public Clump(Hex[] h, Color c) {
		hex = h;
		color = c;
		setColor(c);
		// moveTo(hex[0].getXPD()[0], hex[0].getYPD()[0]);
		for (int i = 0; i < hex.length; i++) {
			Point2D.Double[] tempP = hex[i].getPoints();
			for (int j = 0; j < 6; j++) {
				if (!containsPoint(tempP[j])) {
					points.add(tempP[j]);
				}
			}
		}
		System.out.println("Hex: " + hex.length);
		System.out.println("Points: " + points.size());
		// for (int i = 0; i < points.size(); i++)
		// System.out.println(points.get(i).toString());
		filterPoints();
	}

	private boolean containsPoint(Point2D.Double point) {
		double sizish = Math.round(10000.0 * (point.getX() + point.getY())) / 10000.0;
		for (int i = 0; i < points.size(); i++) {
			double sizish2 = Math.round(10000.0 * (points.get(i).getX() + points.get(i).getY())) / 10000.0;
			if (sizish == sizish2)
				return true;
		}
		return false;
	}

	private void filterPoints() {
		ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>();
		int ijk = 0;
		do {
			if (lines.size() == 0) {
				for (int i = 0; i < points.size(); i++) { // Creates lines
					for (int j = i + 1; j < points.size(); j++) {
						Line2D.Double testLine = new Line2D.Double(points.get(i), points.get(j));
						double length = Math.sqrt(Math.pow(testLine.getX2() - testLine.getX1(), 2)
								+ Math.pow(testLine.getY2() - testLine.getY1(), 2));
						if (Math.abs(length - HexDisplay.getHSize() / 2.0) < 0.1)
							lines.add(testLine);
					}
				}
			}
			// Point2D.Double[] thePoints = points.toArray(new
			// Point2D.Double[points.size()]);

			int[] linesPerPoint = new int[points.size()];
			for (int i = 0; i < points.size(); i++) { // Finds lines per point
														// based of lines
				for (int j = 0; j < lines.size(); j++) {
					if (((Point2D.Double) lines.get(j).getP1()).equals(points.get(i)))
						linesPerPoint[i]++;
					else if (((Point2D.Double) lines.get(j).getP2()).equals(points.get(i)))
						linesPerPoint[i]++;
				}
			}

			System.out.println(Arrays.toString(linesPerPoint));
			System.out.println("Lines size:" + lines.size());
			ArrayList<Line2D.Double> lessLines = copyArrayListLine(lines);

			for (int i = 0; i < lessLines.size(); i++) { // Removes lines
															// connecting points
															// of 3
				Point2D.Double p1 = (Point2D.Double) lessLines.get(i).getP1();
				Point2D.Double p2 = (Point2D.Double) lessLines.get(i).getP2();
				if (linesPerPoint[points.indexOf(p1)] == 3 && linesPerPoint[points.indexOf(p2)] == 3) {
					// System.out.println(i);
					lessLines.remove(lessLines.get(i));
					i--;
				}
			}
			System.out.println("LessLines size:" + lessLines.size());

			linesPerPoint = new int[points.size()];
			for (int i = 0; i < points.size(); i++) { // Finds lines per point
														// based off lessLines
				for (int j = 0; j < lessLines.size(); j++) {
					if (((Point2D.Double) lessLines.get(j).getP1()).equals(points.get(i)))
						linesPerPoint[i]++;
					else if (((Point2D.Double) lessLines.get(j).getP2()).equals(points.get(i)))
						linesPerPoint[i]++;
				}
			}
			System.out.println(Arrays.toString(linesPerPoint));

			for (int i = 0; i < lines.size(); i++) { // Adds to lessLines any
														// lines connecting
														// points of 1
				Point2D.Double p1 = (Point2D.Double) lines.get(i).getP1();
				Point2D.Double p2 = (Point2D.Double) lines.get(i).getP2();
				if (linesPerPoint[points.indexOf(p1)] == 1 && linesPerPoint[points.indexOf(p2)] == 1) {
					lessLines.add(lines.get(i));
				}
			}
			System.out.println("LessLines size:" + lessLines.size());

			linesPerPoint = new int[points.size()];
			for (int i = 0; i < points.size(); i++) { // Finds lines per point
														// based off lessLines
				for (int j = 0; j < lessLines.size(); j++) {
					if (((Point2D.Double) lessLines.get(j).getP1()).equals(points.get(i)))
						linesPerPoint[i]++;
					else if (((Point2D.Double) lessLines.get(j).getP2()).equals(points.get(i)))
						linesPerPoint[i]++;
				}
			}
			System.out.println(Arrays.toString(linesPerPoint));

			ArrayList<ArrayList<Line2D.Double>> linesP = new ArrayList<ArrayList<Line2D.Double>>();
			ArrayList<ArrayList<Point2D.Double>> extraP = new ArrayList<ArrayList<Point2D.Double>>();
			ArrayList<Point2D.Double> miniP = new ArrayList<Point2D.Double>();
			for (int i = 0; i < points.size(); i++) { // Fills miniP with all
														// points of 1
				if (linesPerPoint[i] == 1) {
					miniP.add(points.get(i));
					linesP.add(new ArrayList<Line2D.Double>());
					extraP.add(new ArrayList<Point2D.Double>());
					for (int q = 0; q < lines.size(); q++) {
						if (((Point2D.Double) lines.get(q).getP1()).equals(points.get(i))) {
							linesP.get(linesP.size() - 1).add(lines.get(q));
							extraP.get(extraP.size() - 1).add((Point2D.Double) lines.get(q).getP2());
						} else if (((Point2D.Double) lines.get(q).getP2()).equals(points.get(i))) {
							linesP.get(linesP.size() - 1).add(lines.get(q));
							extraP.get(extraP.size() - 1).add((Point2D.Double) lines.get(q).getP1());
						}
					}
				}
			}

			for (int i = 0; i < miniP.size() - 1; i++) {
				Point2D zeroPoint = null;
				for (int k = 0; k < extraP.get(i).size(); k++) {
					if (linesPerPoint[points.indexOf(extraP.get(i).get(k))] == 0) {
						zeroPoint = extraP.get(i).get(k);
						for (int j = i + 1; j < miniP.size(); j++) {
							if (extraP.get(j).contains(zeroPoint)) {
								lessLines.add(linesP.get(i).get(k));
								lessLines.add(linesP.get(j).get(extraP.get(j).indexOf(zeroPoint)));
							}
						}
					}
				}
			}

			lines = copyArrayListLine(lessLines);
			System.out.println("Lines size:" + lines.size());

			linesPerPoint = new int[points.size()];
			for (int i = 0; i < points.size(); i++) { // Finds lines per point
														// based off lessLines
				for (int j = 0; j < lessLines.size(); j++) {
					if (((Point2D.Double) lessLines.get(j).getP1()).equals(points.get(i)))
						linesPerPoint[i]++;
					else if (((Point2D.Double) lessLines.get(j).getP2()).equals(points.get(i)))
						linesPerPoint[i]++;
				}
			}
			System.out.println(Arrays.toString(linesPerPoint));

			miniP = copyArrayList(points);
			for (int i = 0; i < linesPerPoint.length; i++) {
				if (linesPerPoint[i] == 0) { // Removes points of 0
					miniP.remove(points.get(i));
				}
			}
			points = copyArrayList(miniP);

			if (lines.size() != points.size()) {
				ijk++;
				System.out.println("Failed");
				System.out.println("Lines size: " + lines.size());
				System.out.println("Points size: " + points.size());
			}
			// System.out.println(ijk);
			if (ijk > 1) {
				System.exit(0);
			}
		} while (lines.size() != points.size());
		System.out.println("Suceeded");
		findOutline(lines);
	}

	private ArrayList<Point2D.Double> copyArrayList(ArrayList<Point2D.Double> orig) {
		ArrayList<Point2D.Double> newGuy = new ArrayList<Point2D.Double>();
		for (int j = 0; j < orig.size(); j++) {
			newGuy.add(orig.get(j));
		}
		return newGuy;
	}

	private ArrayList<Line2D.Double> copyArrayListLine(ArrayList<Line2D.Double> orig) {
		ArrayList<Line2D.Double> newGuy = new ArrayList<Line2D.Double>();
		for (int j = 0; j < orig.size(); j++) {
			newGuy.add(orig.get(j));
		}
		return newGuy;
	}

	private void findOutline(ArrayList<Line2D.Double> lines) {
		if (lines.size() != points.size()) {
			System.out.println("Failed");
			System.out.println("Lines size: " + lines.size());
			System.out.println("Points size: " + points.size());
			System.exit(0);
			for (int i = 0; i < lines.size(); i++) {
				System.out.print(lines.get(i).getP1().toString());
				System.out.println(lines.get(i).getP2().toString());
			}

		}
		// Order points

		finalPoints = new ArrayList<Point2D.Double>();

		Line2D.Double curLine = lines.get(0);
		lines.remove(0);
		finalPoints.add((Point2D.Double) curLine.getP1());
		Point2D.Double nextPoint = (Point2D.Double) curLine.getP2();
		for (int i = 0; i < lines.size(); i += 0) {
			finalPoints.add(nextPoint);
			curLine = findLineWithPoint(lines, nextPoint);

			if (curLine.getP1().equals(nextPoint)) {
				nextPoint = (Point2D.Double) curLine.getP2();
			} else if (curLine.getP2().equals(nextPoint)) {
				nextPoint = (Point2D.Double) curLine.getP1();
			}

			lines.remove(curLine);
		}

		points = finalPoints;
		moveTo(points.get(0).getX(), points.get(0).getY());
		for (int j = 0; j < points.size(); j++) {
			lineTo(points.get(j).getX(), points.get(j).getY());
		}
		closePath();
	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(color);
		g2.fill(this);
		g2.setColor(Hex.DEFAULTCOLOR);
		g2.setStroke(new BasicStroke(2));
		g2.draw(this);
		// g.drawString(x + " " + y, x, y);
		// System.out.println("Drew solo");
	}

	private Line2D.Double findLineWithPoint(ArrayList<Line2D.Double> lines, Point2D.Double p) {
		for (int i = 0; i < lines.size(); i++) {
			if (((Point2D.Double) lines.get(i).getP1()).equals(p))
				return lines.get(i);
			else if (((Point2D.Double) lines.get(i).getP2()).equals(p))
				return lines.get(i);
		}
		return null;
	}
	
	public void setColor(Color c) {
		for (Hex h : hex) {
			h.setColor(c);
		}
	}
}
