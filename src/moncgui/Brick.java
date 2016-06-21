// Cube model
package moncgui;

/**
 *
 * @author vega
 */
public class Brick extends Mesh {

    private double l2, b2, d2;
    private int verts;
    private double centerX;
    private double centerY;
    private double centerZ;
    public double xMin;
    public double xMax;
    public double yMin;
    public double yMax;
    public double zMin;
    public double zMax;

    public Brick(String name, double cx, double cy, double cz, double length,
            double breadth, double depth,
            Material material) {
        super (name, material);
        //half = size/2;
        this.centerX = cx;
        this.centerY = cy;
        this.centerZ = cz;
        l2 = 0.5 * length;
        b2 = 0.5 * breadth;
        d2 = 0.5 * depth;
        setVertexData ();
        setNormalData ();
        setIndexData ();
        createTriangles ();
    }

    public void getExtent(int xDir, int yDir, int zDir) {
        xMin = centerX - xDir * 0.5 * l2 - yDir * b2;
        xMax = centerX + xDir * 0.5 * l2 + yDir * b2;
        yMin = centerY - yDir * 0.5 * l2 - xDir * d2;
        yMax = centerY + yDir * 0.5 * l2 + xDir * d2;
        zMin = centerZ - zDir * 0.5 * l2 - yDir * b2;
        zMax = centerZ + zDir * 0.5 * l2 + yDir * b2;
    }

    private void setVertexData() {
        setVertexCoordsSize (8);
        // top
        putVertex (new Vector3D (centerX + l2, centerY + b2, centerZ + d2));
        putVertex (new Vector3D (centerX - l2, centerY + b2, centerZ + d2));
        putVertex (new Vector3D (centerX - l2, centerY + b2, centerZ - d2));
        putVertex (new Vector3D (centerX + l2, centerY + b2, centerZ - d2));
        // bottom
        putVertex (new Vector3D (centerX + l2, centerY - b2, centerZ + d2));
        putVertex (new Vector3D (centerX - l2, centerY - b2, centerZ + d2));
        putVertex (new Vector3D (centerX - l2, centerY - b2, centerZ - d2));
        putVertex (new Vector3D (centerX + l2, centerY - b2, centerZ - d2));
        // front
        putVertex (new Vector3D (centerX + l2, centerY + b2, centerZ + d2));
        putVertex (new Vector3D (centerX - l2, centerY + b2, centerZ + d2));
        putVertex (new Vector3D (centerX - l2, centerY - b2, centerZ + d2));
        putVertex (new Vector3D (centerX + l2, centerY - b2, centerZ + d2));
        // back
        putVertex (new Vector3D (centerX + l2, centerY + b2, centerZ - d2));
        putVertex (new Vector3D (centerX - l2, centerY + b2, centerZ - d2));
        putVertex (new Vector3D (centerX - l2, centerY - b2, centerZ - d2));
        putVertex (new Vector3D (centerX + l2, centerY - b2, centerZ - d2));
        // left
        putVertex (new Vector3D (centerX - l2, centerY + b2, centerZ + d2));
        putVertex (new Vector3D (centerX - l2, centerY + b2, centerZ - d2));
        putVertex (new Vector3D (centerX - l2, centerY - b2, centerZ - d2));
        putVertex (new Vector3D (centerX - l2, centerY - b2, centerZ + d2));
        // right
        putVertex (new Vector3D (centerX + l2, centerY + b2, centerZ + d2));
        putVertex (new Vector3D (centerX + l2, centerY + b2, centerZ - d2));
        putVertex (new Vector3D (centerX + l2, centerY - b2, centerZ - d2));
        putVertex (new Vector3D (centerX + l2, centerY - b2, centerZ + d2));
    }

    private void setNormalData() {
        setNormalCoordsSize (16);
        // top
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        // bottom
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        // front
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        // back
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        // left
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        // right
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
        putNormal (centerX + 0, centerY + 0, centerZ + 1);
    }

    public void setVerts() {
        this.verts = getVertexCount ();
    }

    public int getVerts() {
        return this.verts;
    }

    private void setIndexData() {
        setTriangleIndicesSize (6);
        putTriangleIndex (0, 3, 2, 1); //top
        putTriangleIndex (4, 5, 6, 7); //bottom
        putTriangleIndex (8, 9, 10, 11); //front
        putTriangleIndex (12, 15, 14, 13); //back
        putTriangleIndex (16, 17, 18, 19); //left
        putTriangleIndex (20, 23, 22, 21); //left
    }

}
