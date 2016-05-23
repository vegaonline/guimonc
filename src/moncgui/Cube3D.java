// Cube model
package moncgui;

public class Cube3D extends Mesh {

    //double half;
    double side1;
    double side2;
    double side3;

    public Cube3D(String name, double lenV, double widV, double htV,
            Material material) {
        // public Cube3D(String name, double size, Material material) {
        super (name, material);
        //half = size/2;
        side1 = lenV * 0.5;
        side2 = widV * 0.5;
        side3 = htV * 0.5;
        setVertexData ();
        setNormalData ();
        setIndexData ();
        createTriangles ();
    }

    private void setVertexData() {

        setVertexCoordsSize (8);

        // top
        putVertex (new Vector3D (side1, side2, side3));
        putVertex (new Vector3D (-side1, side2, side3));
        putVertex (new Vector3D (-side1, side2, -side3));
        putVertex (new Vector3D (side1, side2, -side3));
        // bottom
        putVertex (new Vector3D (side1, -side2, side3));
        putVertex (new Vector3D (-side1, -side2, side3));
        putVertex (new Vector3D (-side1, -side2, -side3));
        putVertex (new Vector3D (side1, -side2, -side3));
        // front
        putVertex (new Vector3D (side1, side2, side3));
        putVertex (new Vector3D (-side1, side2, side3));
        putVertex (new Vector3D (-side1, -side2, side3));
        putVertex (new Vector3D (side1, -side2, side3));
        // back
        putVertex (new Vector3D (side1, side2, -side3));
        putVertex (new Vector3D (-side1, side2, -side3));
        putVertex (new Vector3D (-side1, -side2, -side3));
        putVertex (new Vector3D (side1, -side2, -side3));
        // left
        putVertex (new Vector3D (-side1, side2, side3));
        putVertex (new Vector3D (-side1, side2, -side3));
        putVertex (new Vector3D (-side1, -side2, -side3));
        putVertex (new Vector3D (-side1, -side2, side3));
        // right
        putVertex (new Vector3D (side1, side2, side3));
        putVertex (new Vector3D (side1, side2, -side3));
        putVertex (new Vector3D (side1, -side2, -side3));
        putVertex (new Vector3D (side1, -side2, side3));
    }

    private void setNormalData() {
        setNormalCoordsSize (16);

        // top
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        // bottom
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        // front
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        // back
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        // left
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        // right
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
        putNormal (0, 0, 1);
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
