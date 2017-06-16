package io.github.AdmiralSbs.DiceWars;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Clump extends Path2D.Double {
	private static final long serialVersionUID = 1L;
	private Hex[] hex;
	private ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
	private ArrayList<Point2D.Double> finalPoints = new ArrayList<Point2D.Double>();

	public Clump(Hex[] h) {
		hex = h;
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
			for (int i = 0; i < points.size(); i++) { // Finds lines per points
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

			for (int i = 0; i < lessLines.size(); i++) { // Removes excess lines
															// from lessLines
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
			for (int i = 0; i < points.size(); i++) {
				for (int j = 0; j < lessLines.size(); j++) {
					if (((Point2D.Double) lessLines.get(j).getP1()).equals(points.get(i)))
						linesPerPoint[i]++;
					else if (((Point2D.Double) lessLines.get(j).getP2()).equals(points.get(i)))
						linesPerPoint[i]++;
				}
			}
			System.out.println(Arrays.toString(linesPerPoint));

			for (int i = 0; i < lines.size(); i++) {
				Point2D.Double p1 = (Point2D.Double) lines.get(i).getP1();
				Point2D.Double p2 = (Point2D.Double) lines.get(i).getP2();
				if (linesPerPoint[points.indexOf(p1)] == 1 && linesPerPoint[points.indexOf(p2)] == 1) {
					lessLines.add(lines.get(i));
				}
			}
			System.out.println("LessLines size:" + lessLines.size());
			lines = copyArrayListLine(lessLines);
			System.out.println("Lines size:" + lessLines.size());

			ArrayList<Point2D.Double> miniP = copyArrayList(points);
			for (int i = 0; i < linesPerPoint.length; i++) {
				if (linesPerPoint[i] == 0) {
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
			if (ijk > 5) {
				System.exit(0);
			}
		} while (lines.size() != points.size());
		System.out.println("Suceeded");
		/*
		 * int i = 0; System.out.println("Orig # of points: " + points.size());
		 * while (i < points.size()) { ArrayList<Point2D.Double> miniP =
		 * copyArrayList(points); miniP.remove(points.get(i)); Path2D.Double q =
		 * constructPath2D(miniP); if (q.contains(points.get(i))) {
		 * System.out.println("Removed " + points.get(i).toString());
		 * points.remove(points.get(i)); } else { i++; } }
		 */
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
		/*
		 * int num = 0; while (num + 1 < lines.size()) { int num2 = num + 1;
		 * boolean shrunk = false; while (num2 < lines.size()) { if
		 * (linesIntersect(lines.get(num), lines.get(num2))) { if (lines.size()
		 * == points.size() + 2) lines.remove(lines.get(num2));
		 * lines.remove(lines.get(num)); shrunk = true; break; } else { num2++;
		 * } } if (!shrunk) num++; }
		 */
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

	/*
	 * private void findOutline2() { int i = 0; while (true) { //
	 * System.out.println(i); // System.out.println(points.size());
	 * Point2D.Double startP = points.get(i); ArrayList<Point2D.Double> miniP =
	 * copyArrayList(points); miniP.remove(points.get(i));
	 * ArrayList<Line2D.Double> lines = new ArrayList<Line2D.Double>(); //if
	 * (checkNextPoint(miniP, startP, lines)) { //finalPoints.add(startP);
	 * break; //} //i++; } points = finalPoints; moveTo(points.get(0).getX(),
	 * points.get(0).getY()); for (int j = 0; j < points.size(); j++) {
	 * lineTo(points.get(j).getX(), points.get(j).getY()); }
	 * lineTo(points.get(0).getX(), points.get(0).getY()); }
	 * 
	 * private boolean checkNextPoint(ArrayList<Point2D.Double> pointsLeft,
	 * Point2D.Double cur, ArrayList<Line2D.Double> lines) { for (int i = 0; i <
	 * pointsLeft.size(); i++) { // System.out.println(i); Line2D.Double newLine
	 * = new Line2D.Double(cur, pointsLeft.get(i)); boolean okay = true; for
	 * (int j = 0; j < lines.size(); j++) { if (linesIntersect(newLine,
	 * lines.get(j))) { okay = false; } } if (okay) { ArrayList<Point2D.Double>
	 * newPointsLeft = copyArrayList(pointsLeft);
	 * newPointsLeft.remove(pointsLeft.get(i)); ArrayList<Line2D.Double>
	 * newLines = new ArrayList<Line2D.Double>(); newLines.add(newLine); if
	 * (checkNextPoint(newPointsLeft, pointsLeft.get(i), newLines)) {
	 * finalPoints.add(pointsLeft.get(i)); return true; } } } return false; }
	 */

	/*
	 * private Path2D.Double constructPath2D(ArrayList<Point2D.Double> p) {
	 * Path2D.Double q = new Path2D.Double(); q.moveTo(p.get(0).getX(),
	 * p.get(0).getY()); for (int i = 0; i < p.size(); i++) {
	 * q.lineTo(p.get(i).getX(), p.get(i).getY()); } q.lineTo(p.get(0).getX(),
	 * p.get(0).getY()); return q; }
	 * 
	 * private boolean linesIntersect(Line2D.Double l1, Line2D.Double l2) {
	 * boolean intersectsAtAll = l1.intersectsLine(l2); boolean sharesCoords =
	 * (l1.getP1().equals(l2.getP2()) || l1.getP2().equals(l2.getP1()) ||
	 * l1.getP1().equals(l2.getP1()) || l1.getP2().equals( l2.getP2())); if
	 * (intersectsAtAll && !sharesCoords) return true; else return false; }
	 */

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(hex[0].getColor());
		g2.fill(this);
		g2.setColor(Hex.DEFAULTCOLOR);
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
}
