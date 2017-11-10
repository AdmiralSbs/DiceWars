package io.github.AdmiralSbs.DiceWars;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Clump extends Path2D.Double {
    private static final long serialVersionUID = 1L;
    private final Hex[] hex;
    private final ArrayList<Line2D.Double> lines = new ArrayList<>();
    private Color color;
    private final ArrayList<Clump> neighbors = new ArrayList<>();
    //private ArrayList<Point2D.Double> points = new ArrayList<>();

    public Clump(Hex[] h, Color c) {
        hex = h;
        color = c;
        setColor(c);
        for (Hex aHex : hex) {
            /*Point2D.Double[] tempP = aHex.getPoints();
            for (int j = 0; j < 6; j++) {
                if (!containsPoint(tempP[j])) {
                    points.add(tempP[j]);
                }
            }*/
            lines.addAll(aHex.getLines());
        }

        System.out.println("Hex: " + hex.length);
        //System.out.println("Points: " + points.size());
        filterLines();
        orderLines();
        makePathFromOrderedLines();
    }

    /*private void printLines() {
        for (Line2D.Double ln : lines) {
            System.out.print("Line " + lines.indexOf(ln) + ": ");
            System.out.print("(" + ln.getP1().getX() + "," + ln.getP1().getY() + ") to ");
            System.out.print("(" + ln.getP2().getX() + "," + ln.getP2().getY() + ")" + "\n");
        }
    }

    private void printLines(LinkedList<Line2D.Double > lns) {
        for (Line2D.Double ln : lns) {
            System.out.print("Line " + lns.indexOf(ln) + ": ");
            System.out.print("(" + ln.getP1().getX() + "," + ln.getP1().getY() + ") to ");
            System.out.print("(" + ln.getP2().getX() + "," + ln.getP2().getY() + ")" + "\n");
        }
    }*/

    private void filterLines() {
        System.out.println("Lines size: " + lines.size());
        ArrayList<Line2D.Double> finalLines = new ArrayList<>();
        for (Line2D.Double ln : lines) {
            boolean notDuplicate = true;
            for (Line2D.Double otherLine : finalLines) {
                if (linesAreSame(ln, otherLine)) {
                    notDuplicate = false;
                    finalLines.remove(otherLine);
                    System.out.println("false");
                    break;
                }
            }
            if (notDuplicate)
                finalLines.add(ln);
        }
        lines.clear();
        lines.addAll(finalLines);
        System.out.println("Lines size: " + lines.size());
        //printLines();
    }

    private boolean linesAreSame(Line2D.Double line1, Line2D.Double line2) {
        Point2D.Double p11 = (Point2D.Double) line1.getP1();
        Point2D.Double p12 = (Point2D.Double) line1.getP2();
        Point2D.Double p21 = (Point2D.Double) line2.getP1();
        Point2D.Double p22 = (Point2D.Double) line2.getP2();
        if (pointsAreSame(p11, p21) && pointsAreSame(p12, p22))
            return true;
        else if (pointsAreSame(p11, p22) && pointsAreSame(p12, p21))
            return true;
        //System.out.println("returned false");
        return false;
    }

    private boolean pointsAreSame(Point2D.Double p1, Point2D.Double p2) {
        double xdif = Math.abs(p1.getX() - p2.getX());
        double ydif = Math.abs(p1.getY() - p2.getY());
        return xdif < 0.1 && ydif < 0.1;
    }

    private void orderLines() {
        //printLines();
        LinkedList<Line2D.Double> finalLines = new LinkedList<>();
        finalLines.addFirst(lines.get(0));
        lines.remove(0);
        while (lines.size() != 0) {
            //System.out.println("run");
            //printLines(finalLines);
            for (Line2D.Double ln : lines) {
                Point2D.Double f1 = (Point2D.Double) finalLines.getFirst().getP1();
                Point2D.Double f2 = (Point2D.Double) finalLines.getFirst().getP2();
                Point2D.Double p1 = (Point2D.Double) ln.getP1();
                Point2D.Double p2 = (Point2D.Double) ln.getP2();
                if (pointsAreSame(f1, p2)) {
                    finalLines.addFirst(ln);
                    lines.remove(ln);
                    break;
                } else if (pointsAreSame(f2, p1)) {
                    finalLines.addLast(ln);
                    lines.remove(ln);
                    break;
                }
            }
        }
        lines.addAll(finalLines);
        //System.out.println("lines length: " + lines.size());
        //printLines();
    }

    private void makePathFromOrderedLines() {

        moveTo(lines.get(0).getP1().getX(), lines.get(0).getP1().getY());
        for (int i = 1; i < lines.size(); i++) {
            lineTo(lines.get(i).getP1().getX(), lines.get(i).getP1().getY());
            //System.out.println(getCurrentPoint().getX() + "," + getCurrentPoint().getY());
        }
        closePath();
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(color);
        g2.fill(this);
        g2.setColor(Hex.DEFAULT_COLOR);
        g2.setStroke(new BasicStroke(2));
        g2.draw(this);
        // g.drawString(x + " " + y, x, y);
        // System.out.println("Drew solo");
    }


    public void setColor(Color c) {
        for (Hex h : hex) {
            h.setColor(c);
        }
    }

    public void addNeighbor(Clump c) {
        if (!neighbors.contains(c))
            neighbors.add(c);
    }

}
