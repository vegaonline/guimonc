// Tube model
package moncgui;

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

    protected boolean viewInside;

    /**
     * Constructor meant for Savable use only.
     */
    public tubeTest() {
    }

    //public tubeTest(final String name, final double outerRadius, final double innerRadius, final double height,
    //        final int axisSamples, final int radialSamples) {
    //	this(name, outerRadius, innerRadius,height,  axisSamples, radialSamples);
    //}
    public tubeTest(final String name, final double outerRadius,
            final double innerRadius, final double height,
            final int axisSamples, final int radialSamples, Material material) {
        super (material);
        this.outerRadius = outerRadius;
        this.innerRadius = innerRadius;
        this.height = height;
        this.axisSamples = axisSamples;
        this.radialSamples = radialSamples;
        allocateVertices ();
        createTriangles ();
    }

    //public tubeTest(final String name, final double outerRadius, final double innerRadius, final double height) {
    //    this(name, outerRadius, innerRadius, height, 2, 20);
    //}
    private void allocateVertices() {
        //viewInside = true;       
        setVerts (axisSamples, radialSamples);
        setVertexCoordsSize (verts);
        setNormalCoordsSize (verts);
        final int tris = (4 * radialSamples * (1 + axisSamples));
        setTriangleIndicesSize (tris);
        setGeometryData ();
        setIndexData ();
    }

    private void setVerts(int axisSamples, int radialSamples) {
        verts
                = (2 * (axisSamples + 1) * (radialSamples + 1) + radialSamples *
                4);
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

        final double inverseRadial = 1.0 / radialSamples;
        final double axisStep = height / axisSamples;
        final double axisTextureStep = 1.0 / axisSamples;
        final double halfHeight = 0.5 * height;
        final double innerOuterRatio = innerRadius / outerRadius;
        final double[] sin = new double[radialSamples];
        final double[] cos = new double[radialSamples];

        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
            final double angle = 2 * Math.PI * inverseRadial * radialCount;
            cos[radialCount] = Math.cos (angle);
            sin[radialCount] = Math.sin (angle);
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
    }

    private void setIndexData() {
        final int outerCylinder = (axisSamples + 1) * (radialSamples + 1);
        final int bottomEdge = 2 * outerCylinder;
        final int topEdge = bottomEdge + 2 * radialSamples;
        // inner cylinder
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
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
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
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
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
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
        for ( int radialCount = 0; radialCount < radialSamples; radialCount++ ) {
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
