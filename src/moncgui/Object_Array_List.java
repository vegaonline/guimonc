/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

/**
 *
 * @author vega
 */
public class Object_Array_List extends Mesh {

    private int objID;
    private String ObjName = null;
    private int nVerts = 0;
    private int sample1 = 0;
    private int sample2 = 0;
    private double param1 = 0.0;
    private double param2 = 0.0;
    private double param3 = 0.0;
    private double maxX;
    private double minX;
    private double maxY;
    private double minY;
    private double maxZ;
    private double minZ;
    // private List<Vector3D> vertCoords;
    // private List<Vector3D> colorCoords;

    public Object_Array_List() {
    }

    public Object_Array_List(final int id, final String objName,
            final int sample1, final int sample2,
            final double param1, final double param2, final double param3) {
        this.objID = id;
        this.ObjName = objName;
        this.sample1 = sample1;
        this.sample2 = sample2;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public void setMaxMin(final double maxx, final double minx,
            final double maxy, final double miny, final double maxz,
            final double minz) {
        this.maxX = maxx;
        this.minX = minx;
        this.maxY = maxy;
        this.minY = miny;
        this.maxZ = maxz;
        this.minZ = minz;
    }

    public void setNVerts(final int vertN) {
        this.nVerts = vertN;
        // vertCoords = new ArrayList<>(this.nVerts);
        // colorCoords = new ArrayList<Vector3D>(this.nVerts);
    }

    public int getNVerts() {
        return this.nVerts;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public double getMaxZ() {
        return this.maxZ;
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMinY() {
        return this.minY;
    }

    public double getMinZ() {
        return this.minZ;
    }
}
