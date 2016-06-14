// Tube model
package moncgui;

/**
 *
 * @author vega
 */
public class tubeTest extends Mesh {

    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    private int axisSamples;
    private int radialSamples;
    private int verts;
    private double outerRadius;
    private double innerRadius;
    private double height;
    private double theta0;
    private double theta1;
    private boolean viewInside;
    private double centerX;
    private double centerY;
    private double centerZ;

    /**
     * Constructor meant for Savable use only.
     */
    public tubeTest() {
    }

    /**
     *
     * @param name
     * @param cx
     * @param cy
     * @param cz
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param axisSamples
     * @param radialSamples
     * @param material
     */
    public tubeTest(final String name, final double cx, final double cy,
            final double cz, final double outerRadius,
            final double innerRadius, final double height, final int axisSamples,
            final int radialSamples, Material material) {
        this (name, cx, cy, cz, outerRadius, innerRadius, height, 0.0, 2.0 *
                Math.PI, axisSamples, radialSamples, true, material);
    }

    /**
     * 
     * @param name
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param axisSamples
     * @param radialSamples
     * @param material 
     */
    public tubeTest(final String name, final double outerRadius,
            final double innerRadius, final double height, final int axisSamples,
            final int radialSamples, Material material) {
        this (name, 0.0, 0.0, 0.0, outerRadius, innerRadius, height, 0.0, 2.0 *
                Math.PI, axisSamples, radialSamples, true, material);
    }

    /**
     *
     * @param name
     * @param cx
     * @param cy
     * @param cz
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param axisSamples
     * @param radialSamples
     * @param closed
     * @param material
     */
    public tubeTest(final String name, final double cx, final double cy,
            final double cz, final double outerRadius,
            final double innerRadius, final double height, final int axisSamples,
            final int radialSamples, final boolean closed, Material material) {
        this (name, cx, cy, cz, outerRadius, innerRadius, height, 0.0, 2.0 *
                Math.PI, axisSamples, radialSamples, closed, material);
    }

    /**
     *
     * @param name
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param axisSamples
     * @param radialSamples
     * @param closed
     * @param material
     */
    public tubeTest(final String name, final double outerRadius,
            final double innerRadius, final double height, final int axisSamples,
            final int radialSamples, final boolean closed, Material material) {
        this (name, 0.0, 0.0, 0.0, outerRadius, innerRadius, height, 0.0, 2.0 *
                Math.PI, axisSamples, radialSamples, closed, material);
    }

    /**
     *
     * @param name
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param ang0
     * @param ang1
     * @param axisSamples
     * @param radialSamples
     * @param material
     */
    public tubeTest(final String name, final double outerRadius,
            final double innerRadius, final double height, final double ang0,
            final double ang1, final int axisSamples, final int radialSamples,
            Material material) {
        this (name, 0.0, 0.0, 0.0, outerRadius, innerRadius, height, ang0, ang1,
                axisSamples, radialSamples, true, material);
    }

    /**
     *
     * @param name
     * @param cx centerX
     * @param cy centerY
     * @param cz centerZ
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param ang0
     * @param ang1
     * @param axisSamples
     * @param radialSamples
     * @param material
     */
    public tubeTest(final String name, final double cx, final double cy,
            final double cz, final double outerRadius,
            final double innerRadius, final double height, final double ang0,
            final double ang1, final int axisSamples, final int radialSamples,
            Material material) {
        this (name, cx, cy, cz, outerRadius, innerRadius, height, ang0, ang1,
                axisSamples, radialSamples, true, material);
    }

    /**
     *
     * @param name
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param ang0
     * @param ang1
     * @param axisSamples
     * @param radialSamples
     * @param viewInside true means can see inside i.e. not closed
     * @param material
     */
    public tubeTest(final String name, final double cx, final double cy,
            final double cz, final double outerRadius, final double innerRadius,
            final double height, final double ang0, final double ang1,
            final int axisSamples, final int radialSamples, final boolean closed,
            Material material) {
        super (material);
        this.centerX = cx;
        this.centerY = cy;
        this.centerZ = cz;
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.height = height;
        this.axisSamples = axisSamples;
        this.radialSamples = radialSamples;
        this.theta0 = ang0;
        this.theta1 = ang1;
        this.viewInside = !closed;
        allocateVertices ();
        createTriangles ();
    }

    private void allocateVertices() {
        setVerts (axisSamples, radialSamples);
        setVertexCoordsSize (verts);
        setNormalCoordsSize (verts);
        final int tris = (4 * radialSamples * (1 + axisSamples));
        setTriangleIndicesSize (tris);
        setGeometryData ();
        setIndexData ();
    }

    private void setVerts(int axisSamples, int radialSamples) {
        verts = (2 * (axisSamples + 1) * (radialSamples + 1) +
                radialSamples * 4);
    }

    public int getVerts() {
        return verts;
    }

    public int getAxisSamples() {
        return axisSamples;
    }

    public void setAxisSamples(final int axisSamples) {
        this.axisSamples = axisSamples;
        allocateVertices ();
    }

    public int getRadialSamples() {
        return radialSamples;
    }

    public void setRadialSamples(final int radialSamples) {
        this.radialSamples = radialSamples;
        allocateVertices ();
    }

    public double getOuterRadius() {
        return outerRadius;
    }

    public void setOuterRadius(final double outerRadius) {
        this.outerRadius = outerRadius;
        allocateVertices ();
    }

    public double getInnerRadius() {
        return innerRadius;
    }

    public void setInnerRadius(final double innerRadius) {
        this.innerRadius = innerRadius;
        allocateVertices ();
    }

    public double getHeight() {
        return height;
    }

    public Vector3D getCenter() {
        Vector3D center = new Vector3D(0,0,0);
        center.x = this.centerX;
        center.y = this.centerY;
        center.z = this.centerZ;
        return center;
    }
    
    public void setHeight(final double height) {
        this.height = height;
        allocateVertices ();
    }

    private void setGeometryData() {
        final double dA = Math.abs (theta0 - theta1);
        final double inverseRadial = 1.0 / (radialSamples - 1);
        final double axisStep = height / axisSamples;
        final double axisTextureStep = 1.0 / (axisSamples - 1);
        final double halfHeight = 0.5 * height;
        final double innerOuterRatio = innerRadius / outerRadius;
        final double[] sin = new double[radialSamples];
        final double[] cos = new double[radialSamples];

        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
            // final double angle = 2 * Math.PI * inverseRadial * radialCount;
            final double angle = theta0 + dA * inverseRadial * radialCount;
            cos[radialCount] = Math.cos (angle);
            sin[radialCount] = Math.sin (angle);
        }

        // outer cylinder
        for ( int radialCount = 0; radialCount < radialSamples + 1;
                radialCount++ ) {
            for ( int axisCount = 0; axisCount < axisSamples + 1; axisCount++ ) {
                putVertex (centerX + (cos[radialCount % radialSamples] *
                        outerRadius),
                        centerY + (axisStep * axisCount - halfHeight),
                        centerZ + (sin[radialCount % radialSamples] *
                        outerRadius));
                if ( viewInside ) {
                    putNormal (centerX + -cos[radialCount % radialSamples],
                            centerY + 0.0,
                            centerZ + -sin[radialCount % radialSamples]);
                } else {
                    putNormal (centerX + cos[radialCount % radialSamples],
                            centerY + 0.0,
                            centerZ + sin[radialCount % radialSamples]);
                }
            }
        }
        // inner cylinder
        for ( int radialCount = 0; radialCount < radialSamples + 1;
                radialCount++ ) {
            for ( int axisCount = 0; axisCount < axisSamples + 1; axisCount++ ) {
                putVertex (centerX + (cos[radialCount % radialSamples] *
                        innerRadius),
                        centerY + (axisStep * axisCount - halfHeight),
                        centerZ + (sin[radialCount % radialSamples] *
                        innerRadius));
                if ( viewInside ) {
                    putNormal (centerX + cos[radialCount % radialSamples],
                            centerY + 0.0,
                            centerZ + sin[radialCount % radialSamples]);
                } else {
                    putNormal (centerX - cos[radialCount % radialSamples],
                            centerY + 0.0,
                            centerZ - sin[radialCount % radialSamples]);
                }
            }
        }
        // bottom edge
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
            putVertex (centerX + (cos[radialCount] * outerRadius),
                    centerY - halfHeight,
                    centerZ + (sin[radialCount] * outerRadius));
            putVertex (centerX + (cos[radialCount] * innerRadius),
                    centerY - halfHeight,
                    centerZ + (sin[radialCount] * innerRadius));
            if ( viewInside ) {
                putNormal (centerX + 0, centerY + 1, centerZ + 0);
                putNormal (centerX + 0, centerY + 1, centerZ + 0);
            } else {
                putNormal (centerX + 0, centerY - 1, centerZ + 0);
                putNormal (centerX + 0, centerY - 1, centerZ + 0);
            }
        }
        // top edge
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
            putVertex (centerX + (cos[radialCount] * outerRadius),
                    centerY + halfHeight,
                    centerZ + (sin[radialCount] * outerRadius));
            putVertex (centerX + (cos[radialCount] * innerRadius),
                    centerY + halfHeight,
                    centerZ + (sin[radialCount] * innerRadius));
            if ( viewInside ) {
                putNormal (centerX + 0, centerY - 1, centerZ + 0);
                putNormal (centerX + 0, centerY - 1, centerZ + 0);
            } else {
                putNormal (centerX + 0, centerY + 1, centerZ + 0);
                putNormal (centerX + 0, centerY + 1, centerZ + 0);
            }
        }

        if ( viewInside ) {
            putVertex (centerX + 0, centerY + 0, centerZ - 1); //bottom center
            putNormal (centerX + 0, centerY + 0, centerZ - 1);

            putVertex (centerX + 0, centerY + 0, centerZ + 1); // top center
            putNormal (centerX + 0, centerY + 0, centerZ + 1);
        }

        for ( int ii = 0; ii < sin.length; ii++ ) {
            sin[ii] = 0.0;
            cos[ii] = 0.0;
        }
    }

    private void setIndexData() {
        final int outerCylinder = (axisSamples + 1) * (radialSamples + 1);
        final int bottomEdge = 2 * outerCylinder;
        final int topEdge = bottomEdge + 2 * radialSamples;

        // inner cylinder
        for ( int radialCount = 0; radialCount < radialSamples - 1;
                radialCount++ ) {
            for ( int axisCount = 0; axisCount < axisSamples; axisCount++ ) {
                final int index0 = axisCount + (axisSamples + 1) * radialCount;
                final int index1 = index0 + 1;
                final int index2 = index0 + (axisSamples + 1);
                final int index3 = index2 + 1;
                if ( viewInside ) {
                    putTriangleIndex (index0, index1, index2);
                    putTriangleIndex (index1, index3, index2);
                } else {
                    putTriangleIndex (index0, index2, index1);
                    putTriangleIndex (index1, index2, index3);
                }
            }
        }

        // outer cylinder
        for ( int radialCount = 0; radialCount < radialSamples - 1;
                radialCount++ ) {
            for ( int axisCount = 0; axisCount < axisSamples; axisCount++ ) {
                final int index0 = outerCylinder + axisCount +
                        (axisSamples + 1) * radialCount;
                final int index1 = index0 + 1;
                final int index2 = index0 + (axisSamples + 1);
                final int index3 = index2 + 1;
                if ( viewInside ) {
                    putTriangleIndex (index0, index2, index1);
                    putTriangleIndex (index1, index2, index3);
                } else {
                    putTriangleIndex (index0, index1, index2);
                    putTriangleIndex (index1, index3, index2);
                }
            }
        }

        // bottom edge
        for ( int radialCount = 0; radialCount < radialSamples - 1;
                radialCount++ ) {
            final int index0 = bottomEdge + 2 * radialCount;
            final int index1 = index0 + 1;
            final int index2 = bottomEdge + 2 * ((radialCount + 1) %
                    radialSamples);
            final int index3 = index2 + 1;
            if ( viewInside ) {
                putTriangleIndex (index0, index2, index1);
                putTriangleIndex (index1, index2, index3);
            } else {
                putTriangleIndex (index0, index1, index2);
                putTriangleIndex (index1, index3, index2);
            }
        }

        // top edge
        for ( int radialCount = 0; radialCount < radialSamples - 1;
                radialCount++ ) {
            final int index0 = topEdge + 2 * radialCount;
            final int index1 = index0 + 1;
            final int index2 = topEdge + 2 * ((radialCount + 1) % radialSamples);
            final int index3 = index2 + 1;
            if ( viewInside ) {
                putTriangleIndex (index0, index1, index2);
                putTriangleIndex (index1, index3, index2);
            } else {
                putTriangleIndex (index0, index2, index1);
                putTriangleIndex (index1, index2, index3);
            }
        }

    }

    /**
     *
     * @return true if the normals are inverted to point into the torus so that
     * the face is oriented for a viewer inside the torus. false (the default)
     * for exterior viewing.
     */
    public boolean isViewFromInside() {
        return viewInside;
    }

    /**
     *
     * @param viewInside if true, the normals are inverted to point into the
     * torus so that the face is oriented for a viewer inside the torus. Default
     * is false (for outside viewing)
     */
    public void setViewFromInside(final boolean viewInside) {
        if ( viewInside != viewInside ) {
            this.viewInside = viewInside;
            setGeometryData ();
            setIndexData ();
        }
    }
}
