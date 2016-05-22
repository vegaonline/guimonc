/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.util.ArrayList;
import java.util.List;

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
    private List<Vector3D> vertCoords;
    private List<Vector3D> colorCoords;

    public Object_Array_List() {
    }

    public Object_Array_List(final int id, final String objName, final int sample1, final int sample2,
            final double param1, final double param2, final double param3) {
        this.objID = id;
        this.ObjName = objName;
        this.sample1 = sample1;
        this.sample2 = sample2;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public void setNVerts(final int vertN) {
        this.nVerts = vertN;
        vertCoords = new ArrayList<>(this.nVerts);
        colorCoords = new ArrayList<Vector3D>(this.nVerts);
    }
    
    public int getNVerts(){
        return this.nVerts;
    }

    public void setVertCoord(final Vector3D vecInput) {
        this.vertCoords.add(vecInput);
    }

    public Vector3D getVertCoord(int kk) {
        return this.vertCoords.get(kk);
    }

    public void setColorCoord(final Vector3D vecInput) {
        this.colorCoords.add(vecInput);
    }

}
