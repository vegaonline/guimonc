// Cube model
package moncgui;

    public class Brick extends Mesh {

		double l2, b2, d2;

        public Brick(String name, double length, double breadth, double depth, Material material) {
			super(name, material);
			//half = size/2;
                        l2 = 0.5 * length;
                        b2 = 0.5 * breadth;
                        d2 = 0.5 * depth;
            setVertexData();
            setNormalData();
            setIndexData();
            createTriangles();
        }

    private void setVertexData() {

        setVertexCoordsSize(8);

        // top
        putVertex(new Vector3D( l2,  b2,   d2));
        putVertex(new Vector3D(-l2,  b2,   d2));
        putVertex(new Vector3D(-l2,  b2,  -d2));
        putVertex(new Vector3D( l2,  b2,  -d2));
        // bottom
        putVertex(new Vector3D( l2, -b2,   d2));
        putVertex(new Vector3D(-l2, -b2,   d2));
        putVertex(new Vector3D(-l2, -b2,  -d2));
        putVertex(new Vector3D( l2, -b2,  -d2));
        // front
        putVertex(new Vector3D( l2,  b2,   d2));
        putVertex(new Vector3D(-l2,  b2,   d2));
        putVertex(new Vector3D(-l2, -b2,   d2));
        putVertex(new Vector3D( l2, -b2,   d2));
        // back
        putVertex(new Vector3D( l2,  b2,  -d2));
        putVertex(new Vector3D(-l2,  b2,  -d2));
        putVertex(new Vector3D(-l2, -b2,  -d2));
        putVertex(new Vector3D( l2, -b2,  -d2));
        // left
        putVertex(new Vector3D(-l2,  b2,   d2));
        putVertex(new Vector3D(-l2,  b2,  -d2));
        putVertex(new Vector3D(-l2, -b2,  -d2));
        putVertex(new Vector3D(-l2, -b2,   d2));
        // right
        putVertex(new Vector3D( l2,  b2,   d2));
        putVertex(new Vector3D( l2,  b2,  -d2));
        putVertex(new Vector3D( l2, -b2,  -d2));
        putVertex(new Vector3D( l2, -b2,   d2));
    }

    private void setNormalData() {
        setNormalCoordsSize(16);

        // top
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        // bottom
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        // front
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        // back
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        // left
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        // right
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
        putNormal( 0,  0,  1);
    }

    private void setIndexData() {
        setTriangleIndicesSize(6);
        putTriangleIndex(0, 3, 2, 1); //top
        putTriangleIndex(4, 5, 6, 7); //bottom
        putTriangleIndex(8, 9, 10, 11); //front
        putTriangleIndex(12, 15, 14, 13); //back
        putTriangleIndex(16, 17, 18, 19); //left
        putTriangleIndex(20, 23, 22, 21); //left
    }

}
