// Sphere model
package moncgui;

import javafx.geometry.Point3D;

/**
 * Sphere represents a 3D object with all points equi-distance from a center
 * point.
 */
/**
 *
 * @author vega
 */
public class Sphere_SECT extends Mesh {

    private static final long serialVersionUID = 1L;

    protected int zSamples;
    protected int radialSamples;

    /**
     * the distance from the center point each point falls on
     */
    private double innerRadius;
    private double outerRadius;
    private double radius;
    
    /**
     * azimuthal angles
     */
    private double phi0;
    private double phi1;
    private double dTheta;
    
    /**
     * vertical angles
     */
    private double theta0;
    private double theta1;
    private double dPhi;
    
    /**
     * the center of the sphere
     */
    public Vector3D center = new Vector3D (0, 0, 0);

    protected boolean viewInside;

    public Sphere_SECT() {
    }

    /**
     * Constructs a sphere. By default the Sphere has not geometry data or
     * center.
     *
     * @param name The name of the sphere.
     */
    //public Sphere(final String name) {
    //super(name);
    //}

        /**
     *
     * @param name
     * @param outerRadius
     * @param radialSamples
     * @param zSamples
     * @param material
     */
    public Sphere_SECT(final String name, final double outerRadius,
            final int radialSamples, final int zSamples, Material material) {
        this(name, 0.0, outerRadius, 0.0, Math.PI, 0.0, 2.0 * Math.PI,
                radialSamples, zSamples, material);
    }

    /**
     *
     * @param name
     * @param innerRadius
     * @param outerRadius
     * @param radialSamples
     * @param zSamples
     * @param material
     */
    public Sphere_SECT(final String name, final double innerRadius,
            final double outerRadius, final int radialSamples,
            final int zSamples, Material material) {
        this(name, innerRadius, outerRadius, 0.0, Math.PI, 0.0, 2.0 * Math.PI,
                radialSamples, zSamples, material);
    }
    
    
    /**
     *
     * @param name
     * @param innerRadius
     * @param outerRadius
     * @param theta1
     * @param phi1
     * @param radialSamples
     * @param zSamples
     * @param material
     */
    public Sphere_SECT(final String name, final double innerRadius,
            final double outerRadius, final double theta1, final double phi1,
            final int radialSamples, final int zSamples, Material material) {
        this(name, innerRadius, outerRadius, 0.0, theta1, 0.0, phi1,
                radialSamples, zSamples, material);
    }

    /**
     * 
     * @param name
     * @param innerRadius
     * @param outerRadius
     * @param theta0
     * @param theta1
     * @param phi0
     * @param phi1
     * @param radialSamples
     * @param zSamples
     * @param material 
     */
    public Sphere_SECT(final String name, final double innerRadius,
            final double outerRadius,  double theta0,  double theta1,
             double phi0,  double phi1, final int radialSamples,   // put final 
            final int zSamples, Material material) {
        super(material);
/*
        theta0 = -Math.PI * 0.5;
        theta1 = Math.PI * 0.5;
        phi0 = 0.0;
        phi1 = Math.PI * 2.0;
        */ 
        dTheta = Math.abs (theta0 - theta1);
        dPhi = Math.abs (phi0 - phi1);
        this.innerRadius = innerRadius;
        this.outerRadius = outerRadius;
        this.theta0 = theta0;
        this.theta1 = theta1;
        this.phi0 = phi0;
        this.phi1 = phi1;
        this.radialSamples = radialSamples;
        this.zSamples = zSamples;
        this.center = center;
        setGeometryData();
        setIndexData();
        createTriangles();
    }

    /**
     *
     *
     * /**
     * builds the vertices based on the radius, center and radial and zSamples.
     */
    private void setGeometryData() {
        radius = outerRadius;
        // allocate vertices
        final int verts = (zSamples - 2) * (radialSamples + 1) + 2;
        setVertexCoordsSize (verts);
        setNormalCoordsSize (verts);

        // generate geometry
        final double fInvRS = 1.0 / radialSamples;
        final double fZFactor = 2.0 / (zSamples - 1);

        // Generate points on the unit circle to be used in computing the mesh
        // points on a sphere slice.
        final double[] afSin = new double[(radialSamples + 1)];
        final double[] afCos = new double[(radialSamples + 1)];
        for ( int iR = 0; iR < radialSamples; iR++ ) {
            // final double fAngle = 2 * Math.PI * fInvRS * iR;
            final double fAngle = phi0 + dPhi * fInvRS * iR;
            afCos[iR] = Math.cos (fAngle);
            afSin[iR] = Math.sin (fAngle);
        }
        afSin[radialSamples] = afSin[0];
        afCos[radialSamples] = afCos[0];

        // generate the sphere itself
        int i = 0;
        Vector3D tempVa = new Vector3D (0, 0, 0);
        final Point3D tempVb;
        Vector3D tempVc = new Vector3D (0, 0, 0);

        Vector3D kSliceCenter = new Vector3D (0, 0, 0);

        for ( int iZ = 1; iZ < (zSamples - 1); iZ++ ) {
            final double fAFraction = 0.5 * Math.PI * (-1.0f + fZFactor * iZ); // in (-pi/2, pi/2)
            final double fZFraction = Math.sin (fAFraction); // in (-1,1)
            final double fZ = radius * fZFraction;

            // compute center of slice
            kSliceCenter = new Vector3D (center.getX (), center.getY (), center.
                    getZ () + fZ);

            // compute radius of slice
            final double fSliceRadius = Math.sqrt (Math.abs (radius * radius -
                    fZ * fZ));

            // compute slice vertices with duplication at end point
            Vector3D kNormal;
            final int iSave = i;
            for ( int iR = 0; iR < radialSamples; iR++ ) {
                final double fRadialFraction = iR * fInvRS; // in [0,1)
                final Vector3D kRadial = new Vector3D (afCos[iR], afSin[iR], 0);
                tempVa = new Vector3D (fSliceRadius * kRadial.getX (),
                        fSliceRadius * kRadial.getY (), fSliceRadius * kRadial.
                        getZ ());
                putVertex ((kSliceCenter.getX () + tempVa.getX ()),
                        (kSliceCenter.getY () + tempVa.getY ()),
                        (kSliceCenter.getZ () + tempVa.getZ ()));

                tempVa = getVertexCoord (i);

                kNormal = new Vector3D (tempVa.getX () - center.getX (), tempVa.
                        getY () - center.getY (), tempVa.getZ () - center.
                        getZ ());
                double mag = Math.sqrt (Math.pow (kNormal.getX (), 2) + Math.
                        pow (kNormal.getY (), 2) + Math.pow (kNormal.getZ (), 2));
                kNormal = new Vector3D (kNormal.getX () / mag, kNormal.getY () /
                        mag, kNormal.getZ () / mag);
                if ( !viewInside ) {
                    putNormal (kNormal.getX (), kNormal.getY (), kNormal.getZ ());
                } else {
                    putNormal (-kNormal.getX (), -kNormal.getY (), -kNormal.
                            getZ ());
                }

                i++;
            }

//            setVertexCoord(i, getVertexCoord(iSave));
//            setNormalCoord(i, getNormalCoord(iSave));
            putVertex (getVertexCoord (iSave));
            putNormal (getNormalCoord (iSave));

            i++;
        }

        // south pole
//        setVertexCount(i+1);
        putVertex (new Vector3D (center.getX (), center.getY (),
                (float) (center.getZ () - radius)));

//        setNormalCount(i+1);
        if ( !viewInside ) {
            putNormal (new Vector3D (0, 0, -1));
        } else {
            putNormal (new Vector3D (0, 0, 1));
        }

        i++;

        // north pole
        putVertex (new Vector3D (center.getX (), center.getY (),
                (float) (center.getZ () + radius)));

        if ( !viewInside ) {
            putNormal (new Vector3D (0, 0, 1));
        } else {
            putNormal (new Vector3D (0, 0, -1));
        }

    }

    /**
     * sets the indices for rendering the sphere.
     */
    private void setIndexData() {
        // allocate connectivity
        final int tris = 2 * (zSamples - 2) * radialSamples;
        setTriangleIndicesSize (tris);

        // generate connectivity
        int index = 0;
        for ( int iZ = 0, iZStart = 0; iZ < (zSamples - 3); iZ++ ) {
            int i0 = iZStart;
            int i1 = i0 + 1;
            iZStart += (radialSamples + 1);
            int i2 = iZStart;
            int i3 = i2 + 1;
            for ( int i = 0; i < radialSamples-1; i++, index += 6 ) {
                if ( !viewInside ) {
                    putTriangleIndex (i0++, i1, i2);
                    putTriangleIndex (i1++, i3++, i2++);
                } else // inside view
                {
                    putTriangleIndex (i0++, i2, i1);
                    putTriangleIndex (i1++, i2++, i3++);
                }
            }
        }

        // south pole triangles
        for ( int i = 0; i < radialSamples; i++, index += 3 ) {
            if ( !viewInside ) {
                putTriangleIndex (i, getVertexCount () - 2, i + 1);
            } else // inside view
            {
                putTriangleIndex (i, i + 1, getVertexCount () - 2);
            }
        }

        // north pole triangles
        final int iOffset = (zSamples - 3) * (radialSamples + 1);
        for ( int i = 0; i < radialSamples; i++, index += 3 ) {
            if ( !viewInside ) {
                putTriangleIndex (i + iOffset, i + 1 + iOffset,
                        getVertexCount () - 1);
            } else // inside view
            {
                putTriangleIndex (i + iOffset, getVertexCount () - 1, i + 1 +
                        iOffset);
            }
        }
    }

    /**
     * Returns the center of this sphere.
     *
     * @return The sphere's center.
     */
    public Vector3D getCenter() {
        return center;
    }

    /**
     *
     * @return true if the normals are inverted to point into the sphere so that
     * the face is oriented for a viewer inside the sphere. false (the default)
     * for exterior viewing.
     */
    public boolean isViewFromInside() {
        return viewInside;
    }

    /**
     *
     * @param viewInside if true, the normals are inverted to point into the
     * sphere so that the face is oriented for a viewer inside the sphere.
     * Default is false (for outside viewing)
     */
    public void setViewFromInside(final boolean viewInside) {
        if ( viewInside != viewInside ) {
            this.viewInside = viewInside;
            setGeometryData ();
            setIndexData ();
        }
    }

    public double getRadius() {
        return radius;
    }

}
