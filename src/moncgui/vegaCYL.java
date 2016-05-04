/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.util.*;
import javafx.collections.ObservableFloatArray;
import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;

/**
 *
 * @author vega
 */
public class vegaCYL extends TriangleMesh {

    private String name;
    private boolean close;
    private Material material;
    private int axialSamples;
    private int radialSamples;
    private int lenSamp = 10;
    private int radSamp = 5;
    private double radius;
    private double height;
    private boolean closed;

    List<Point3D> listVertices = new ArrayList<> ();
    
    /*
    float[] texCoord = {1, 1, // t0
                       1, 0, // t1
                       0, 1, // t2
                       0, 0  // t3
    };
*/
    
    float pointX, pointY, pointZ;
    float dr, dz, dtheta, theta;
    int ntheta = 8;
    float pi = (float) Math.PI;

    public vegaCYL() {
    }

    /**
     * Creates a new cylinder. By default, its center is at the origin. Usually,
     * a higher sample number creates better cylinder
     *
     * @param name The name of the cylinder
     * @param radius The radius of the cylinder
     * @param height The height of the cylinder
     */
    public vegaCYL(final String name, final double radius, final double height) {
        this (name, radius, height, 8, 12);
    }

    /**
     * Creates a new cylinder. By default, its center is at the origin. Usually,
     * a higher sample number creates better cylinder
     *
     * @param name The name of the cylinder
     * @param radius The radius of the cylinder
     * @param height The height of the cylinder
     * @param radialSamples Number of triangles along the radius
     * @param axialSamples Number of triangles along the length
     */
    public vegaCYL(final String name, final double radius, final double height,
            final int radialSamples, final int axialSamples) {
        this (name, radius, height, radialSamples, axialSamples, false);
        throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Creates a new cylinder. By default, its center is at the origin. Usually,
     * a higher sample number creates better cylinder
     *
     * @param name The name of the cylinder
     * @param radius The radius of the cylinder
     * @param height The height of the cylinder
     * @param radialSamples The number of divisions radially
     * @param axialSamples The number of divisions axially
     * @param closed True to create a cylinder with top and bottom surface
     */
    public vegaCYL(final String name, final double radius, final double height,
            final int radialSamples, final int axialSamples,
            final boolean closed) {
        //     Material material) {
        this.name = name;
        //this.material = material;
        this.axialSamples = axialSamples;
        this.radialSamples = radialSamples;
        this.radius = radius;
        this.height = height;
        this.closed = closed;

        int index = 0;
        dr = (float) (radius / (float) radialSamples);
        dtheta = (float) ((2.0 * pi) / (float) ntheta);
        dz = (float) (height / (float) axialSamples);
        int wid = radialSamples;
        int ht = axialSamples;
        float[] texCoord = new float[(wid + 1) * (ht + 1) * 2];

        for ( int nZ = 0; nZ < axialSamples; nZ++ ) {
            for ( int itht = 0; itht < ntheta; itht++ ) {
                theta = itht * dtheta;
                pointX = (float) (radius * Math.cos (theta));
                pointY = (float) (radius * Math.sin (theta));
                pointZ = nZ * dz;
                listVertices.add (new Point3D (pointX, pointY, pointZ));
            }
        }

        int totPoints = axialSamples * ntheta;
        int[] listFaces = new int[totPoints];
        
        /*
        for ( int v = 0; v <= ht; v++ ) {
            float dv = (float) v / ((float) ht);
            for ( int u = 0; u <= wid; u++ ) {
                texCoord[index] = (float) u / (float) wid;
                texCoord[index + 1] = dv;
                index += 2;
            }
        }
        */
        int ii=0;
        for (int nZ = 0; nZ < axialSamples; nZ++) {
            for (int itht=0; itht < ntheta; itht++) {
                listFaces[ii] = ii;
                listFaces[ii+1] = 0;
                ++ii;
            }
        }
        
        this.getTexCoords ().addAll (0,0); 
        // This is for testing as I do not need texture important at this moment
 
        this.getFaces ().addAll (listFaces);
        
        this.getPoints ().setAll ((ObservableFloatArray) listVertices);
        
        //triangleMesh.getTexCoords().setAll(textureCoords);
        throw new UnsupportedOperationException ("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
