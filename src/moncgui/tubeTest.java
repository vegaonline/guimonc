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

    /**
     * Constructor meant for Savable use only.
     */
    public tubeTest() {
    }

    /**
     *
     * @param name name of the Tube
     * @param outerRadius Outer Radius (> Inner Radius)
     * @param innerRadius Inner Radius
     * @param height Height
     * @param axisSamples Sampling number along length for triangularization
     * @param radialSamples Sampling number radially
     * @param material Material
     */
    public tubeTest(final String name, final double outerRadius,
            final double innerRadius, final double height, final int axisSamples,
            final int radialSamples, Material material) {
        this (name, outerRadius, innerRadius, height, 0.0, 2.0 * Math.PI,
                axisSamples, radialSamples, true, material);
    }

    /**
     *
     * @param name
     * @param outerRadius
     * @param innerRadius
     * @param height
     * @param axisSamples
     * @param radialSamples
     * @param viewInside true means can see inside i.e. not closed
     * @param material
     */
    public tubeTest(final String name, final double outerRadius,
            final double innerRadius, final double height, final int axisSamples,
            final int radialSamples, final boolean closed, Material material) {
        this (name, outerRadius, innerRadius, height, 0.0, 2.0 * Math.PI,
                axisSamples, radialSamples, closed, material);
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
        this (name, outerRadius, innerRadius, height, ang0, ang1, axisSamples,
                radialSamples, true, material);
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
    public tubeTest(final String name, final double outerRadius,
            final double innerRadius, final double height, final double ang0,
            final double ang1, final int axisSamples, final int radialSamples,
            final boolean closed, Material material) {
        super (material);
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.height = height;
        this.axisSamples = axisSamples;
        this.radialSamples = radialSamples;
        this.theta0 = ang0;
        this.theta1 = ang1;
        this.viewInside = closed;
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

    public void setHeight(final double height) {
        this.height = height;
        allocateVertices ();
    }

    private void setGeometryData() {

        //theta0 = 0.0;
        // theta1 = 0.5* Math.PI;
        System.out.println ("Can see inside ? " + viewInside);

        final double dA = Math.abs (theta0 - theta1);
        System.out.println ("dA = " + dA);
        final double inverseRadial = 1.0 / (radialSamples - 1) ;
        final double axisStep = height / axisSamples;
        final double axisTextureStep = 1.0 / (axisSamples - 1);
        final double halfHeight = 0.5 * height;
        final double innerOuterRatio = innerRadius / outerRadius;
        final double[] sin = new double[radialSamples ];
        final double[] cos = new double[radialSamples ];

        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
            // final double angle = 2 * Math.PI * inverseRadial * radialCount;
            final double angle = theta0 + dA * inverseRadial * radialCount;
            cos[radialCount] = Math.cos (angle);
            sin[radialCount] = Math.sin (angle);
            System.out.println (radialCount + " angle = " + angle + " sin = " +
                    sin[radialCount] + " cos = " + cos[radialCount]);
        }


        // outer cylinder
        for ( int radialCount = 0; radialCount < radialSamples + 1;
                radialCount++ ) {
            for ( int axisCount = 0; axisCount < axisSamples + 1; axisCount++ ) {
                putVertex ((cos[radialCount % radialSamples] * outerRadius),
                        (axisStep * axisCount - halfHeight),
                        (sin[radialCount % radialSamples] * outerRadius));
                if ( viewInside ) {
                    putNormal (-cos[radialCount % radialSamples],
                            0.0,
                            -sin[radialCount % radialSamples]);
                } else {
                    putNormal (cos[radialCount % radialSamples],
                            0.0,
                            sin[radialCount % radialSamples]);
                }
            }
        }
        // inner cylinder
        for ( int radialCount = 0; radialCount < radialSamples + 1;
                radialCount++ ) {
            for ( int axisCount = 0; axisCount < axisSamples + 1; axisCount++ ) {
                putVertex ((cos[radialCount % radialSamples] * innerRadius),
                        (axisStep * axisCount - halfHeight),
                        (sin[radialCount % radialSamples] * innerRadius));
                if ( viewInside ) {
                    putNormal (cos[radialCount % radialSamples],
                            0.0,
                            sin[radialCount % radialSamples]);
                } else {
                    putNormal (-cos[radialCount % radialSamples],
                            0.0,
                            -sin[radialCount % radialSamples]);
                }
            }
        }
        // bottom edge
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
            putVertex ((cos[radialCount] * outerRadius),
                    -halfHeight,
                    (sin[radialCount] * outerRadius));
            putVertex ((cos[radialCount] * innerRadius),
                    -halfHeight,
                    (sin[radialCount] * innerRadius));
            if ( viewInside ) {
                putNormal (0, 1, 0);
                putNormal (0, 1, 0);
            } else {
                putNormal (0, -1, 0);
                putNormal (0, -1, 0);
            }
        }
        // top edge
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
            putVertex ((cos[radialCount] * outerRadius),
                    halfHeight,
                    (sin[radialCount] * outerRadius));
            putVertex ((cos[radialCount] * innerRadius),
                    halfHeight,
                    (sin[radialCount] * innerRadius));
            if ( viewInside ) {
                putNormal (0, -1, 0);
                putNormal (0, -1, 0);
            } else {
                putNormal (0, 1, 0);
                putNormal (0, 1, 0);
            }
        }

        if ( viewInside ) {
            putVertex (0, 0, -1); //bottom center
            putNormal (0, 0, -1);

            putVertex (0, 0, 1); // top center
            putNormal (0, 0, 1);
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
        for ( int radialCount = 0; radialCount < radialSamples - 1; radialCount++ ) {
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
        for ( int radialCount = 0; radialCount < radialSamples - 1; radialCount++ ) {
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
        for ( int radialCount = 0; radialCount < radialSamples - 1; radialCount++ ) {
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
        for ( int radialCount = 0; radialCount < radialSamples - 1; radialCount++ ) {
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
