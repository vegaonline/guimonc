// 3D vector operations
package moncgui;

import javafx.geometry.*;
import javafx.scene.shape.Shape;

/**
 *
 * @author vega
 */
public class Vector3D {

    final static double RAD2DEG = 360.0 / (2 * Math.PI);

    protected double x = 0;
    protected double y = 0;
    protected double z = 0;

    public Vector3D() {
    }

    public Vector3D(Point3D p) {
        x = p.getX ();
        y = p.getY ();
        z = p.getZ ();
    }

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setXYZ(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void set(Vector3D v) {
        this.x = v.getX ();
        this.y = v.getY ();
        this.z = v.getZ ();
    }

    public void setVector(Vector3D v) {
        this.x = v.getX ();
        this.y = v.getY ();
        this.z = v.getZ ();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }

    public Point3D toPoint3D() {
        return new Point3D (x, y, z);
    }

    public void add(Vector3D B) {
        x = x + B.getX ();
        y = y + B.getY ();
        z = z + B.getZ ();
    }

    public Vector3D addThis(Vector3D B) {
        x = x + B.getX ();
        y = y + B.getY ();
        z = z + B.getZ ();
        return this;
    }

    public void add(double d) {
        x = x + d;
        y = y + d;
        z = z + d;
    }

    public Vector3D addThis(double d) {
        x = x + d;
        y = y + d;
        z = z + d;
        return this;
    }

    public void subtract(Vector3D B) {
        x = x - B.getX ();
        y = y - B.getY ();
        z = z - B.getZ ();
    }

    public Vector3D subtractThis(Vector3D B) {
        x = x - B.getX ();
        y = y - B.getY ();
        z = z - B.getZ ();
        return this;
    }

    public void subtract(double d) {
        x = x - d;
        y = y - d;
        z = z - d;
    }

    public Vector3D subtractThis(double d) {
        x = x - d;
        y = y - d;
        z = z - d;
        return this;
    }

    public void multiply(double m) {
        x = x * m;
        y = y * m;
        z = z * m;
    }

    public Vector3D multiplyThis(double m) {
        x = x * m;
        y = y * m;
        z = z * m;
        return this;
    }

    public void multiply(Vector3D v) {
        x = x * v.getX ();
        y = y * v.getY ();
        z = z * v.getZ ();
    }

    public Vector3D multiplyThis(Vector3D v) {
        x = x * v.getX ();
        y = y * v.getY ();
        z = z * v.getZ ();
        return this;
    }

    public void divide(double m) {
        x = x / m;
        y = y / m;
        z = z / m;
    }

    public Vector3D divideThis(double m) {
        x = x / m;
        y = y / m;
        z = z / m;
        return this;
    }

    public void divide(Vector3D v) {
        x = x / v.getX ();
        y = y / v.getY ();
        z = z / v.getZ ();
    }

    public Vector3D divideThis(Vector3D v) {
        x = x / v.getX ();
        y = y / v.getY ();
        z = z / v.getZ ();
        return this;
    }

    public void normalise() {
        double mag = Math.sqrt (x * x + y * y + z * z);
        if ( mag == 0 ) {
            x = 0;
            y = 0;
            z = 0;
        } else {
            x = x / mag;
            y = y / mag;
            z = z / mag;
        }
    }

    public Vector3D normaliseThis() {
        double mag = Math.sqrt (x * x + y * y + z * z);
        if ( mag == 0 ) {
            x = 0;
            y = 0;
            z = 0;
        } else {
            x = x / mag;
            y = y / mag;
            z = z / mag;
        }
        return this;
    }

    public double angleBetween(Vector3D B) {
        Vector3D unitA = normalise (this);
        Vector3D unitB = normalise (B);
        double cosTheta = dotProduct (unitA, unitB);
        return Math.acos (cosTheta);
    }

    public void invert() {
        x = -x;
        y = -y;
        z = -z;
    }

    public Vector3D invertThis() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public double distance(Vector3D B) {
        double dx = x - B.getX ();
        double dy = y - B.getY ();
        double dz = z - B.getZ ();
        return Math.sqrt (dx * dx + dy * dy + dz * dz);
    }

// STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC
// STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC STATIC
// STAIC methods returning value
    public final static Vector3D crossProduct(Vector3D A, Vector3D B) {
        return new Vector3D (A.getY () * B.getZ () - A.getZ () * B.getY (),
                A.getZ () * B.getX () - A.getX () * B.getZ (),
                A.getX () * B.getY () - A.getY () * B.getX ());
    }

    public final static double dotProduct(Vector3D A, Vector3D B) {
        return A.getX () * B.getX () + A.getY () * B.getY () + A.getZ () * B.
                getZ ();
    }

    public final static Vector3D add(Vector3D A, Vector3D B) {
        return new Vector3D (A.getX () + B.getX (), A.getY () + B.getY (), A.
                getZ () + B.getZ ());
    }

    public final static Vector3D multiply(Vector3D A, Vector3D B) {
        return new Vector3D (A.getX () * B.getX (), A.getY () * B.getY (), A.
                getZ () * B.getZ ());
    }

    public final static Vector3D subtract(Vector3D A, Vector3D B) {
        return new Vector3D (A.getX () - B.getX (), A.getY () - B.getY (), A.
                getZ () - B.getZ ());
    }

    public final static Vector3D divide(Vector3D A, Vector3D B) {
        return new Vector3D (A.getX () / B.getX (), A.getY () / B.getY (), A.
                getZ () / B.getZ ());
    }

    public final static Vector3D add(Vector3D A, double C) {
        return new Vector3D (A.getX () + C, A.getY () + C, A.getZ () + C);
    }

    public final static Vector3D multiply(Vector3D A, double C) {
        return new Vector3D (A.getX () * C, A.getY () * C, A.getZ () * C);
    }

    public final static Vector3D subtract(Vector3D A, double C) {
        return new Vector3D (A.getX () - C, A.getY () - C, A.getZ () - C);
    }

    public final static Vector3D divide(Vector3D A, double C) {
        return new Vector3D (A.getX () / C, A.getY () / C, A.getZ () / C);
    }

    public final static double sum(Vector3D A) {
        return A.getX () + A.getY () + A.getZ ();
    }

    public final static Vector3D normalise(Vector3D A) {
        double x = A.getX ();
        double y = A.getY ();
        double z = A.getZ ();
        double mag = Math.sqrt (x * x + y * y + z * z);
        if ( mag == 0 ) {
            return new Vector3D (0, 0, 0);
        } else {
            return new Vector3D (A.getX () / mag, A.getY () / mag, A.getZ () /
                    mag);
        }
    }

    public final static double magnitude(Vector3D A) {
        double x = A.getX ();
        double y = A.getY ();
        double z = A.getZ ();
        return Math.sqrt (x * x + y * y + z * z);
    }

    public final static double vectorLength(Vector3D A) {
        double x = A.getX ();
        double y = A.getY ();
        double z = A.getZ ();
        return Math.sqrt (x * x + y * y + z * z);
    }

    public final static double length(Vector3D A) {
        double x = A.getX ();
        double y = A.getY ();
        double z = A.getZ ();
        return Math.sqrt (x * x + y * y + z * z);
    }

    public final static double angleBetween(Vector3D A, Vector3D B) {
        Vector3D unitA = normalise (A);
        Vector3D unitB = normalise (B);
        double cosTheta = dotProduct (unitA, unitB);
        return Math.acos (cosTheta);
    }

    public final static double radToDeg(double thetaRad) {
        double thetaDeg = thetaRad * RAD2DEG;
        return thetaDeg;
    }

    public final static double angleBetweenDeg(Vector3D A, Vector3D B) {
        return radToDeg (angleBetween (A, B));
    }

    public final static Vector3D normalVector(Vector3D A, Vector3D B) {
        Vector3D cross = crossProduct (A, B);
        return normalise (cross);
    }

    public final static Vector3D getVector(Vector3D A, Vector3D B) {
        return new Vector3D (B.getX () - A.getX (), B.getY () - A.getY (), B.
                getZ () - A.getZ ());
    }
    //public final static Point3D getVector(Point3D A, Point3D B){
    //	return new Point3D(B.getX()-A.getX(), B.getY()-A.getY(), B.getZ()-A.getZ());
    //}

    public final static Vector3D invert(Vector3D A) {
        return new Vector3D (-A.getX (), -A.getY (), -A.getZ ());
    }

    public final static Vector3D getPointFromSingularBounds(Shape shape) {
        Bounds shapeBounds = shape.getBoundsInParent ();
        return new Vector3D (shapeBounds.getMinX (), shapeBounds.getMinY (),
                shapeBounds.getMinZ ());
    }

// STATIC methods involving Point3D
    public final static Vector3D getVector(Point3D A, Point3D B) {
        double xA = A.getX ();
        double yA = A.getY ();
        double zA = A.getZ ();
        double xB = B.getX ();
        double yB = B.getY ();
        double zB = B.getZ ();
        return new Vector3D (xB - xA, yB - yA, zB - zA);
    }

// STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT
// STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT STATIC RESULT
// STATIC methods using result vector
    public final static void crossProduct(Vector3D A, Vector3D B,
            Vector3D result) {
        result.setX (A.getY () * B.getZ () - A.getZ () * B.getY ());
        result.setY (A.getZ () * B.getX () - A.getX () * B.getZ ());
        result.setZ (A.getX () * B.getY () - A.getY () * B.getX ());
    }

    public final static void normalVector(Vector3D A, Vector3D B,
            Vector3D result) {
        Vector3D.crossProduct (A, B, result);
        Vector3D.normalise (result, result);
    }

    public final static void getVector(Vector3D A, Vector3D B, Vector3D result) {
        result.setXYZ (B.getX () - A.getX (), B.getY () - A.getY (), B.getZ () -
                A.getZ ());
    }

    public final static void normalise(Vector3D A, Vector3D result) {
        double x = A.getX ();
        double y = A.getY ();
        double z = A.getZ ();
        double mag = Math.sqrt (x * x + y * y + z * z);
        if ( mag == 0 ) {
            result.setXYZ (0, 0, 0);
        } else {
            result.setXYZ (A.getX () / mag, A.getY () / mag, A.getZ () / mag);
        }
    }

    public final static void add(Vector3D A, double C, Vector3D result) {
        result.setXYZ (A.getX () + C, A.getY () + C, A.getZ () + C);
    }

    public final static void multiply(Vector3D A, double C, Vector3D result) {
        result.setXYZ (A.getX () * C, A.getY () * C, A.getZ () * C);
    }

    public final static void subtract(Vector3D A, double C, Vector3D result) {
        result.setXYZ (A.getX () - C, A.getY () - C, A.getZ () - C);
    }

    public final static void divide(Vector3D A, double C, Vector3D result) {
        result.setXYZ (A.getX () / C, A.getY () / C, A.getZ () / C);
    }

    /*
     * [R] = t*x*x + c t*x*y - z*s t*x*z + y*s t*x*y + z*s t*y*y + c t*y*z - x*s
     * t*x*z - y*s t*y*z + x*s t*z*z + c
     *
     * where, �c =cos(angle) �s = sin(angle) �t =1 - c �x = normalised axis x
     * coordinate �y = normalised axis y coordinate �z = normalised axis z
     * coordinate
     */
    public final static void getRotateAngleAxisMatrix(double theta,
            Vector3D axis, double[][] R) {
        double c = Math.cos (theta); //radians
        double t = 1 - c;
        double s = Math.sin (theta); //radians
        double x = axis.getX ();
        double y = axis.getY ();
        double z = axis.getZ ();
        R[0][0] = t * x * x + c;
        R[0][1] = t * x * y - z * s;
        R[0][2] = t * x * z + y * s;
        R[1][0] = t * x * y + z * s;
        R[1][1] = t * y * y + c;
        R[1][2] = t * y * z - x * s;
        R[2][0] = t * x * z - y * s;
        R[2][1] = t * y * z + x * s;
        R[2][2] = t * z * z + c;
    }

    public final static double[][] getRotateAngleAxisMatrix(double theta,
            Vector3D axis) {
        double c = Math.cos (theta); //radians
        double t = 1 - c;
        double s = Math.sin (theta); //radians
        double x = axis.getX ();
        double y = axis.getY ();
        double z = axis.getZ ();
        double[][] R = new double[3][3];
        R[0][0] = t * x * x + c;
        R[0][1] = t * x * y - z * s;
        R[0][2] = t * x * z + y * s;
        R[1][0] = t * x * y + z * s;
        R[1][1] = t * y * y + c;
        R[1][2] = t * y * z - x * s;
        R[2][0] = t * x * z - y * s;
        R[2][1] = t * y * z + x * s;
        R[2][2] = t * z * z + c;
        return R;
    }

    public final static Vector3D rotateAngleAxis(Vector3D v, double[][] R) {
        double vx = v.getX ();
        double vy = v.getY ();
        double vz = v.getZ ();
        double nx = R[0][0] * vx + R[0][1] * vy + R[0][2] * vz;
        double ny = R[1][0] * vx + R[1][1] * vy + R[1][2] * vz;
        double nz = R[2][0] * vx + R[2][1] * vy + R[2][2] * vz;
        return new Vector3D (nx, ny, nz);
    }

    public final static Vector3D rotateAngleAxis(Vector3D v, double theta,
            Vector3D axis) {
        double c = Math.cos (theta); //radians
        double t = 1 - c;
        double s = Math.sin (theta); //radians
        double x = axis.getX ();
        double y = axis.getY ();
        double z = axis.getZ ();
        double[][] R = new double[3][3];
        R[0][0] = t * x * x + c;
        R[0][1] = t * x * y - z * s;
        R[0][2] = t * x * z + y * s;
        R[1][0] = t * x * y + z * s;
        R[1][1] = t * y * y + c;
        R[1][2] = t * y * z - x * s;
        R[2][0] = t * x * z - y * s;
        R[2][1] = t * y * z + x * s;
        R[2][2] = t * z * z + c;
        double vx = v.getX ();
        double vy = v.getY ();
        double vz = v.getZ ();
        double nx = R[0][0] * vx + R[0][1] * vy + R[0][2] * vz;
        double ny = R[1][0] * vx + R[1][1] * vy + R[1][2] * vz;
        double nz = R[2][0] * vx + R[2][1] * vy + R[2][2] * vz;
        //double nx = R[0][0]*vx + R[1][0]*vy + R[2][0]*vz;
        //double ny = R[0][1]*vx + R[1][1]*vy + R[2][1]*vz;
        //double nz = R[0][2]*vx + R[1][2]*vy + R[2][2]*vz;
        return new Vector3D (nx, ny, nz);
    }

}
