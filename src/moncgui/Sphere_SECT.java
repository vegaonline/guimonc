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
public class Sphere extends Mesh {

    private static final long serialVersionUID = 1L;

    protected int zSamples;

    protected int radialSamples;

    /**
     * the distance from the center point each point falls on
     */
    public double radius;
    /**
     * the center of the sphere
     */
    public Vector3D center = new Vector3D (0, 0, 0);

    protected boolean viewInside;

    public Sphere() {
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
    public Sphere(final String name, final int zSamples, final int radialSamples,
            final double radius) {
        this (name, new Vector3D (0, 0, 0), zSamples, radialSamples, radius,
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
    public Sphere(final String name, final Vector3D center, final int zSamples,
            final int radialSamples,
            final double radius, Material material) {
        super (material);
        this.radius = radius;
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

    public double getRadius() {
        return radius;
    }

}
