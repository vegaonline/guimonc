// Sphere Section refractored from Sphere model
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
public class Sphere_Sect extends Mesh {

    private static final long serialVersionUID = 1L;

    protected int zSamples;

    protected int radialSamples;

    /**
     * the distance from the center point each point falls on
     */
    public double inrad;
    public double outrad;
    public double theta0;
    public double theta1;
    public double phi0;
    public double phi1;
    /**
     * the center of the sphere
     */
    public Vector3D center = new Vector3D (0, 0, 0);

    protected boolean viewInside;

    public Sphere_Sect() {
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
     * Constructs a sphere with center at the origin. For details, see the other
     * constructor.
     *
     * @param name Name of sphere.
     * @param zSamples The samples along the Z.
     * @param radialSamples The samples along the radial.
     * @param radius Radius of the sphere.
     * @see #Sphere(java.lang.String, com.ardor3d.math.Vector3, int, int,
     * double)
     */
    public Sphere_Sect(final String name, final int zSamples,
            final int radialSamples, final double outrad,
            final double theta0, final double theta1, final double phi0,
            final double phi1) {
        this (name, new Vector3D (0, 0, 0), zSamples, radialSamples, 1.0e-6,
                outrad, theta0, theta1, phi0, phi1,
                Material.getShinyMaterial ());
    }

    public Sphere_Sect(final String name, final int zSamples,
            final int radialSamples, final double inrad, final double outrad,
            final double theta0, final double theta1, final double phi0,
            final double phi1) {
        this (name, new Vector3D (0, 0, 0), zSamples, radialSamples, inrad,
                outrad, theta0, theta1, phi0, phi1,
                Material.getShinyMaterial ());
    }

    /**
     * Constructs a sphere. All geometry data buffers are updated automatically.
     * Both zSamples and radialSamples increase the quality of the generated
     * sphere.
     *
     * @param name Name of the sphere.
     * @param center Center of the sphere.
     * @param zSamples The number of samples along the Z.
     * @param radialSamples The number of samples along the radial.
     * @param radius The radius of the sphere.
     */
    public Sphere_Sect(final String name, final Vector3D center,
            final int zSamples, final int radialSamples,
            final double inrad, final double outrad, final double theta0,
            final double theta1, final double phi0, final double phi1,
            Material material) {
        super (material);
        this.inrad = inrad;
        this.outrad = outrad;
        this.theta0 = theta0;
        this.theta1 = theta1;
        this.phi0 = phi0;
        this.phi1 = phi1;
        this.radialSamples = radialSamples;
        this.zSamples = zSamples;
        this.center = center;
        setGeometryData ();
        setIndexData ();
        createTriangles ();
    }

    /**
     *
     *
     * /**
     * builds the vertices based on the radius, center and radial and zSamples.
     */
    private void setGeometryData() {

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
            final double fAngle = 2 * Math.PI * fInvRS * iR;
            afCos[iR] = Math.cos (fAngle);
            afSin[iR] = Math.sin (fAngle);
        }
        afSin[radialSamples] = afSin[0];
        afCos[radialSamples] = afCos[0];

        // generate the sphere itself
        int i = 0;
        Vector3D tempVa1 = new Vector3D (0, 0, 0);
        final Point3D tempVb1;
        Vector3D tempVc1 = new Vector3D (0, 0, 0);

        Vector3D tempVa2 = new Vector3D (0, 0, 0);
        final Point3D tempVb2;
        Vector3D tempVc2 = new Vector3D (0, 0, 0);

        Vector3D kSliceCenter = new Vector3D (0, 0, 0);
        // Vector3D kSliceCenter1 = new Vector3D (0, 0, 0);
        // Vector3D kSliceCenter2 = new Vector3D (0, 0, 0);

        for ( int iZ = 1; iZ < (zSamples - 1); iZ++ ) {
            final double fAFraction = 0.5 * Math.PI * (-1.0f + fZFactor * iZ); // in (-pi/2, pi/2)
            final double fZFraction = Math.sin (fAFraction); // in (-1,1)
            final double fZ1 = inrad * fZFraction;
            final double fZ2 = outrad * fZFraction;

            // compute center of slice
            kSliceCenter = new Vector3D (center.getX (), center.getY (), center.
                    getZ () + fZ1);
            // kSliceCenter1 = new Vector3D (center.getX (), center.getY (),center.getZ () + fZ1);
            //kSliceCenter2 = new Vector3D (center.getX (), center.getY (),center.getZ () + fZ2);

            // compute radius of slice
            final double fSliceRadius1 = Math.sqrt (Math.abs (inrad * inrad -
                    fZ1 * fZ1));
            final double fSliceRadius2 = Math.sqrt (Math.abs (outrad * outrad -
                    fZ2 * fZ2));
            final double fSliceRadius = Math.abs (fSliceRadius1 - fSliceRadius2);

            // compute slice vertices with duplication at end point
            Vector3D kNormal1;
            Vector3D kNormal2;
            final int iSave = i;
            for ( int iR = 0; iR < radialSamples; iR++ ) {
                final double fRadialFraction = iR * fInvRS; // in [0,1)
                final Vector3D kRadial = new Vector3D (afCos[iR], afSin[iR], 0);
                tempVa1 = new Vector3D (fSliceRadius1 * kRadial.getX (),
                        fSliceRadius1 * kRadial.getY (), fSliceRadius1 *
                        kRadial.getZ ());
                putVertex ((kSliceCenter.getX () + tempVa1.getX ()),
                        (kSliceCenter.getY () + tempVa1.getY ()),
                        (kSliceCenter.getZ () + tempVa1.getZ ()));
                tempVa1 = getVertexCoord (i);

                tempVa2 = new Vector3D (fSliceRadius2 * kRadial.getX (),
                        fSliceRadius2 * kRadial.getY (), fSliceRadius2 *
                        kRadial.getZ ());
                putVertex ((kSliceCenter.getX () + tempVa2.getX ()),
                        (kSliceCenter.getY () + tempVa2.getY ()),
                        (kSliceCenter.getZ () + tempVa2.getZ ()));
                tempVa2 = getVertexCoord (i);

                kNormal1 = new Vector3D (center.getX () - tempVa1.getX (),
                        center.getY () - tempVa1.getY (), center.getZ () -
                        tempVa1.getZ ());
                double mag = Math.sqrt (Math.pow (kNormal1.getX (), 2) + Math.
                        pow (kNormal1.getY (), 2) + Math.pow (kNormal1.getZ (),
                        2));
                kNormal1 = new Vector3D (kNormal1.getX () / mag, kNormal1.
                        getY () / mag, kNormal1.getZ () / mag);
                if ( !viewInside ) {
                    putNormal (kNormal1.getX (), kNormal1.getY (), kNormal1.
                            getZ ());
                } else {
                    putNormal (-kNormal1.getX (), -kNormal1.getY (), -kNormal1.
                            getZ ());
                }

                kNormal2 = new Vector3D (tempVa2.getX () - center.getX (),
                        tempVa2.
                        getY () - center.getY (), tempVa2.getZ () - center.
                        getZ ());
                mag = Math.sqrt (Math.pow (kNormal2.getX (), 2) + Math.
                        pow (kNormal2.getY (), 2) + Math.pow (kNormal2.getZ (),
                        2));
                kNormal2 = new Vector3D (kNormal2.getX () / mag, kNormal2.
                        getY () /
                        mag, kNormal2.getZ () / mag);
                if ( !viewInside ) {
                    putNormal (kNormal2.getX (), kNormal2.getY (), kNormal2.
                            getZ ());
                } else {
                    putNormal (-kNormal2.getX (), -kNormal2.getY (), -kNormal2.
                            getZ ());
                }
                System.out.println (" kNormal1 " + kNormal1.getX () + "  " +
                        kNormal1.
                        getY () + "   " + kNormal1.getZ ());
                System.out.println (" kNormal2 " + kNormal2.getX () + "  " +
                        kNormal2.
                        getY () + "   " + kNormal2.getZ ());
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
                (float) (center.getZ () - inrad)));
        putVertex (new Vector3D (center.getX (), center.getY (),
                (float) (center.getZ () - outrad)));

//        setNormalCount(i+1);
        if ( !viewInside ) {
            putNormal (new Vector3D (0, 0, -1));
        } else {
            putNormal (new Vector3D (0, 0, 1));
        }

        i++;

        // north pole
        putVertex (new Vector3D (center.getX (), center.getY (),
                (float) (center.getZ () + inrad)));
        putVertex (new Vector3D (center.getX (), center.getY (),
                (float) (center.getZ () + outrad)));

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
            for ( int i = 0; i < radialSamples; i++, index += 6 ) {
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

    public double getINRadius() {
        return inrad;
    }

    public double getOUTRadius() {
        return outrad;
    }

}
