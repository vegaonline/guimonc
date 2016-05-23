// cylTest model
package moncgui;

/**
 * <code>cylTest</code> provides an extension of <code>Mesh</code>. A <code>cylTest</code> is defined by a height and
 * radius. The center of the cylTest is the origin.
 */
public class cylTest extends Mesh {

    private static final long serialVersionUID = 1L;

    private int axisSamples;

    private int radialSamples;
    
    private int verts;

    private double radius;
    private double radius2;

    private double height;
    private boolean closed;
    private boolean inverted;

    public cylTest() {}

    /**
     * Creates a new cylTest. By default its center is the origin. Usually, a higher sample number creates a better
     * looking cylinder, but at the cost of more vertex information.
     *
     * @param name
     *            The name of this cylTest.
     * @param axisSamples
     *            Number of triangle samples along the axis.
     * @param radialSamples
     *            Number of triangle samples along the radial.
     * @param radius
     *            The radius of the cylinder.
     * @param height
     *            The cylinder's height.
     */
    public cylTest(final String name, final int axisSamples, final int radialSamples, final double radius,
            final double height) {
        this(name, axisSamples, radialSamples, radius, height, false);
    }

    /**
     * Creates a new cylTest. By default its center is the origin. Usually, a higher sample number creates a better
     * looking cylinder, but at the cost of more vertex information. <br>
     * If the cylinder is closed the texture is split into axisSamples parts: top most and bottom most part is used for
     * top and bottom of the cylinder, rest of the texture for the cylinder wall. The middle of the top is mapped to
     * texture coordinates (0.5, 1), bottom to (0.5, 0). Thus you need a suited distorted texture.
     *
     * @param name
     *            The name of this cylTest.
     * @param axisSamples
     *            Number of triangle samples along the axis.
     * @param radialSamples
     *            Number of triangle samples along the radial.
     * @param radius
     *            The radius of the cylinder.
     * @param height
     *            The cylinder's height.
     * @param closed
     *            true to create a cylinder with top and bottom surface
     */
    public cylTest(final String name, final int axisSamples, final int radialSamples, final double radius,
            final double height, final boolean closed) {
        this(name, axisSamples, radialSamples, radius, height, closed, false);
    }

    /**
     * Creates a new cylTest. By default its center is the origin. Usually, a higher sample number creates a better
     * looking cylinder, but at the cost of more vertex information. <br>
     * If the cylinder is closed the texture is split into axisSamples parts: top most and bottom most part is used for
     * top and bottom of the cylinder, rest of the texture for the cylinder wall. The middle of the top is mapped to
     * texture coordinates (0.5, 1), bottom to (0.5, 0). Thus you need a suited distorted texture.
     *
     * @param name
     *            The name of this cylTest.
     * @param axisSamples
     *            Number of triangle samples along the axis.
     * @param radialSamples
     *            Number of triangle samples along the radial.
     * @param radius
     *            The radius of the cylinder.
     * @param height
     *            The cylinder's height.
     * @param closed
     *            true to create a cylinder with top and bottom surface
     * @param inverted
     *            true to create a cylinder that is meant to be viewed from the interior.
     */
    public cylTest(final String name, final int axisSamples, final int radialSamples, final double radius,
            final double height, final boolean closed, final boolean inverted) {
         this(name, axisSamples, radialSamples, radius, height, closed, inverted, Material.getShinyMaterial());
    }

    public cylTest(final String name, final int axisSamples, final int radialSamples, final double radius,
            final double height, final boolean closed, final boolean inverted, Material material) {

        super(name, material);

        this.axisSamples = axisSamples + (closed ? 2 : 0);
        this.radialSamples = radialSamples;
        this.radius = radius;
        this.radius2 = radius;
        this.height = height;
        this.closed = closed;
        this.inverted = inverted;

        allocateVertices();
    }

    /**
     * @return Returns the height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * @param height
     *            The height to set.
     */
    public void setHeight(final double height) {
        this.height = height;
        allocateVertices();
    }

    /**
     * @return Returns the radius.
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Change the radius of this cylinder. This resets any second radius.
     *
     * @param radius
     *            The radius to set.
     */
    public void setRadius(final double radius) {
        this.radius = radius;
        this.radius2 = radius;
        allocateVertices();
    }

    /**
     * Set the top radius of the 'cylinder' to differ from the bottom radius.
     *
     * @param radius
     *            The first radius to set.
     * @see com.ardor3d.extension.shape.Cone
     */
    public void setRadius1(final double radius) {
        this.radius = radius;
        allocateVertices();
    }

    /**
     * Set the bottom radius of the 'cylinder' to differ from the top radius. This makes the Mesh be a frustum of
     * pyramid, or if set to 0, a cone.
     *
     * @param radius
     *            The second radius to set.
     * @see com.ardor3d.extension.shape.Cone
     */
    public void setRadius2(final double radius) {
        radius2 = radius;

        allocateVertices();
    }

    /**
     * @return the number of samples along the cylinder axis
     */
    public int getAxisSamples() {
        return axisSamples;
    }

    /**
     * @return true if end caps are used.
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @return true if normals and uvs are created for interior use
     */
    public boolean isInverted() {
        return inverted;
    }

    /**
     * @return number of samples around cylinder
     */
    public int getRadialSamples() {
        return radialSamples;
    }

    
    private void setVerts(int axisSamples, int radialSamples) {
        verts = (2 * (axisSamples + 1) * (radialSamples + 1) + radialSamples * 4);
    }

    public int getVerts() {
        return verts;
    }
    
    
    private void allocateVertices() {
        // allocate vertices
        final int verts = axisSamples * (radialSamples + 1) + (closed ? 2 : 0);
        setVertexCoordsSize(verts);
        setNormalCoordsSize(verts);

        final int count = ((closed ? 2 : 0) + 2 * (axisSamples - 1)) * radialSamples;
        setTriangleIndicesSize(count);

        setGeometryData();
        setIndexData();
        createTriangles();

        for (int k=0;k<verts;k++){
// System.out.println("V=" + getVertexCoord(k));
// System.out.println("N=" + getNormalCoord(k));
// System.out.println("");
        }

    }

    private void setGeometryData() {
        // generate geometry
        final double inverseRadial = 1.0 / radialSamples;
        final double inverseAxisLess = 1.0 / (closed ? axisSamples - 3 : axisSamples - 1);
        final double inverseAxisLessTexture = 1.0 / (axisSamples - 1);
        final double halfHeight = 0.5 * height;

        // Generate points on the unit circle to be used in computing the mesh
        // points on a cylinder slice.
        final double[] sin = new double[radialSamples + 1];
        final double[] cos = new double[radialSamples + 1];

        for (int radialCount = 0; radialCount < radialSamples; radialCount++) {
            final double angle = 2*Math.PI * inverseRadial * radialCount;
            cos[radialCount] = Math.cos(angle);
            sin[radialCount] = Math.sin(angle);
        }
        sin[radialSamples] = sin[0];
        cos[radialSamples] = cos[0];

        // generate the cylinder itself
        Vector3D tempNormal;
        for (int axisCount = 0, i = 0; axisCount < axisSamples; axisCount++) {
            double axisFraction;
            double axisFractionTexture;
            int topBottom = 0;
            if (!closed) {
                axisFraction = axisCount * inverseAxisLess; // in [0,1]
                axisFractionTexture = axisFraction;
            } else {
                if (axisCount == 0) {
                    topBottom = -1; // bottom
                    axisFraction = 0;
                    axisFractionTexture = inverseAxisLessTexture;
                } else if (axisCount == axisSamples - 1) {
                    topBottom = 1; // top
                    axisFraction = 1;
                    axisFractionTexture = 1 - inverseAxisLessTexture;
                } else {
                    axisFraction = (axisCount - 1) * inverseAxisLess;
                    axisFractionTexture = axisCount * inverseAxisLessTexture;
                }
            }
            final double z = -halfHeight + height * axisFraction;

            // compute center of slice
            final Vector3D sliceCenter = new Vector3D(0, 0, z);

            // compute slice vertices with duplication at end point
            final int save = i;
            for (int radialCount = 0; radialCount < radialSamples; radialCount++) {
                final double radialFraction = radialCount * inverseRadial; // in [0,1)
                tempNormal = new Vector3D(cos[radialCount], sin[radialCount], 0);
                if (topBottom == 0) {
                    if (!inverted) {
                        putNormal(tempNormal.getX(), tempNormal.getY(), tempNormal.getZ());
                    } else {
                        putNormal(-tempNormal.getX(), -tempNormal.getY(), -tempNormal.getZ());
                    }
                } else {
                    putNormal(0, 0, topBottom * (inverted ? -1 : 1));
                }

                double factor = (radius - radius2) * axisFraction + radius2;
                putVertex(tempNormal.getX()*factor+sliceCenter.getX(), tempNormal.getY()*factor+sliceCenter.getY(), tempNormal.getZ()*factor+sliceCenter.getZ());

                i++;
            }

            putVertex(getVertexCoord(save));
            putNormal(getNormalCoord(save));

            i++;
        }

        if (closed) {
            putVertex(0, 0, -halfHeight); // bottom center
            putNormal(0, 0, -1 * (inverted ? -1 : 1));

            putVertex(0, 0, halfHeight); // top center
            putNormal(0, 0, 1 * (inverted ? -1 : 1));

        }
    }

    private void setIndexData() {
        // generate connectivity
        for (int axisCount = 0, axisStart = 0; axisCount < axisSamples - 1; axisCount++) {
            int i0 = axisStart;
            int i1 = i0 + 1;
            axisStart += radialSamples + 1;
            int i2 = axisStart;
            int i3 = i2 + 1;
            for (int i = 0; i < radialSamples; i++) {
                if (closed && axisCount == 0) {
                    if (!inverted) {
                        putTriangleIndex(i0++, getVertexCount() - 2, i1++);
                    } else {
                        putTriangleIndex(i0++, i1++, getVertexCount() - 2);
                    }
                } else if (closed && axisCount == axisSamples - 2) {
                    if (!inverted) {
                        putTriangleIndex(i2++, i3++, getVertexCount() - 1);
                    } else {
                        putTriangleIndex(i2++, getVertexCount() - 1, i3++);
                    }
                } else {
                    if (!inverted) {
                        putTriangleIndex(i0++, i1, i2);
                        putTriangleIndex(i1++, i3++, i2++);
                    } else {
                        putTriangleIndex(i0++, i2, i1);
                        putTriangleIndex(i1++, i2++, i3++);
                    }
                }
            }
        }
    }

}
