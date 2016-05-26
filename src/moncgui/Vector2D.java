// 2D Vector
package moncgui;

import javafx.geometry.Point2D;

/**
 *
 * @author vega
 */
public class Vector2D {

    final static double RAD2DEG = 360.0 / (2 * Math.PI);

    protected double x = 0;
    protected double y = 0;

    public Vector2D() {
    }

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setXY(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }

    public Point2D getPoint2D() {
        return new Point2D (x, y);
    }

    public final static double crossProduct(Vector2D A, Vector2D B) {
        return A.getX () * B.getY () - A.getY () * B.getX ();
    }

    public final static double dotProduct(Vector2D A, Vector2D B) {
        return A.getX () * B.getX () + A.getY () * B.getY ();
    }

    public final static Vector2D vectorAdd(Vector2D A, Vector2D B) {
        return new Vector2D (A.getX () + B.getX (), A.getY () + B.getY ());
    }

    public final static double vectorSum(Vector2D A) {
        return A.getX () + A.getY ();
    }

    public final static Vector2D vectorScale(Vector2D A, double scale) {
        return new Vector2D (scale * A.getX (), scale * A.getY ());
    }

    public final static Vector2D normalise(Vector2D A) {
        double mag = Math.sqrt (Math.pow (A.getX (), 2) + Math.
                pow (A.getY (), 2));
        return new Vector2D (A.getX () / mag, A.getY () / mag);
    }

    public final static double vectorLength(Vector2D A) {
        return Math.sqrt (Math.pow (A.getX (), 2) + Math.pow (A.getY (), 2));
    }

    public final static double angleBetween(Vector2D A, Vector2D B) {
        Vector2D unitA = normalise (A);
        Vector2D unitB = normalise (B);
        double cosTheta = dotProduct (unitA, unitB);
        return Math.acos (cosTheta);
    }

    public final static double radToDeg(double thetaRad) {
        double thetaDeg = thetaRad * RAD2DEG;
        return thetaDeg;
    }

    public final static double angleBetweenDeg(Vector2D A, Vector2D B) {
        return radToDeg (angleBetween (A, B));
    }

    public final static Vector2D getVector(Vector2D A, Vector2D B) {
        return new Vector2D (B.getX () - A.getX (), B.getY () - A.getY ());
    }

    public final static Point2D getVector(Point2D A, Point2D B) {
        return new Point2D (B.getX () - A.getX (), B.getY () - A.getY ());
    }

    public final static Vector2D invertVector(Vector2D A) {
        return new Vector2D (-A.getX (), -A.getY ());
    }
}
