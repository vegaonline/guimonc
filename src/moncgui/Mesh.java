// Mesh of triangles
package moncgui;

import java.util.*;
import javafx.collections.ObservableList;
import javafx.scene.Group;


public class Mesh extends Group {

    /** Number of vertices represented by this data. */
    protected int vertexCount=0;
    protected int indexCount=0;
    protected int normalCount=0;
    protected int triangleCount=0;
    protected int pointCount=0;
    protected int lineCount=0;
    protected int lineIndiceCount=0;

    /** Number of primitives represented by this data. */
    //protected transient int[] primitiveCounts = new int[1];

    /** Buffer data holding buffers and number of coordinates per vertex */
    //protected Vector3D[] vertexCoords;
    //protected Vector3D[] normalCoords;
    //protected Vector3D[] colorCoords;
    //protected Vector3D[] tangentCoords;
    protected List<Vector3D> vertexCoords = new ArrayList<Vector3D>();
    protected List<Vector3D> normalCoords = new ArrayList<Vector3D>();
    protected List<Vector3D> colorCoords = new ArrayList<Vector3D>();
    protected List<Vector3D> tangentCoords = new ArrayList<Vector3D>();

	//protected Triangle[] tri;
	//protected Triangle3D[] tri;
	protected Polygon3D[] tri;

    /** Index data. */
    //protected int[][] triangleIndices;
    protected List<Integer[]> triangleIndices = new ArrayList<Integer[]>();;
    protected List<Integer> triangleLengths = new ArrayList<Integer>();

	protected List<Integer> pointIndices = new ArrayList<Integer>();

    protected List<Integer> lineIndices = new ArrayList<Integer>();
    protected List<Integer> lineLengths = new ArrayList<Integer>();

	protected List tempList = new ArrayList();

	static Context3D context;

	//protected Vector3D lightVector;
	protected Vector3D lightVector = new Vector3D(1, 1, 1);
	protected String name;

	Material material;


	long startTime;
	long stopTime;

	public Mesh(){

	}
	public Mesh(String name){
		this.name=name;
	}
	public Mesh(String name, Material material){
		this.name=name;
		this.material=material;
	}
	public Mesh(Material material){
		this.material=material;
	}
	public Material getMaterial(){
		return material;
	}
	public void setMaterial(Material material){
		this.material=material;
	}
	public void setDefaultMaterial(){
		this.material=Material.getShinyMaterial();
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}

    /**
     * Gets the vertex count.
     *
     * @return the vertex count
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Sets the vertex buffer.
     *
     * @param vertexBuffer
     *            the new vertex buffer
     */
    public void setVertexCoordsSize(int verts) {
		if (vertexCoords==null){
			vertexCoords = new ArrayList<Vector3D>(verts);
		} else {
			vertexCoords = null;
			vertexCount = 0;
			vertexCoords = new ArrayList<Vector3D>(verts);
		}
    }

    /**
     * Gets the vertex coords.
     *
     * @return the vertex coords
     */
    public Vector3D[] getVertexCoords() {
        return vertexCoords.toArray(new Vector3D[vertexCoords.size()]);
    }

    public Vector3D getVertexCoord(int i) {
        return vertexCoords.get(i);
    }

    public void setVertexCoord(int i, Vector3D p) {
        vertexCoords.set(i, p);
//System.out.println("VC=" + vertexCount);
    }

	public void putVertex(double x, double y, double z){
		vertexCoords.add(new Vector3D(x, y, z));
		vertexCount=vertexCoords.size();
//System.out.println("VC=" + vertexCount);
	}

	public void putVertex(Vector3D p){
		vertexCoords.add(p);
		vertexCount=vertexCoords.size();
//System.out.println("VC=" + vertexCount);
	}

	public void setVertexCount(int i){
		vertexCount = i;
//System.out.println("VC=" + vertexCount);
	}

	public void putNormal(double x, double y, double z){
		normalCoords.add(new Vector3D(x, y, z));
		normalCount=normalCoords.size();
	}

	public void putNormal(Vector3D p){
		normalCoords.add(p);
		normalCount=normalCoords.size();
	}

    /**
     * Gets the normal buffer.
     *
     * @return the normal buffer
     */
    public Vector3D[] getNormalCoords() {
        return normalCoords.toArray(new Vector3D[normalCoords.size()]);
    }

    public Vector3D getNormalCoord(int i) {
        return normalCoords.get(i);
    }

    public void setNormalCoord(int i, Vector3D p) {
        normalCoords.set(i, p);
    }


	public void setNormalCount(int i){
		normalCount = i;
	}

    /**
     * Sets the normal buffer.
     *
     * @param normalBuffer
     *            the new normal buffer
     */
    public void setNormalCoordsSize(int verts) {
//System.out.println("before normalCoords=" + normalCoords + " verts=" + verts);		
		if (normalCoords==null){
			normalCoords = new ArrayList<Vector3D>(verts);
		} else {
			normalCoords = null;
			normalCount = 0;
			normalCoords = new ArrayList<Vector3D>(verts);
		}
//System.out.println("after normalCoords=" + normalCoords);		
    }

    /**
     * Gets the tangent buffer.
     *
     * @return the tangent buffer
     */
    public Vector3D[] getTangentCoords() {
        return tangentCoords.toArray(new Vector3D[tangentCoords.size()]);
    }


    /**
     * Gets the index buffer.
     *
     * @return the index buffer
     */
    public Integer[] getTriangleIndices() {
        return triangleIndices.toArray(new Integer[triangleIndices.size()]);
    }

    /**
     * Sets the index buffer.
     *
     * @param indices
     *            the new index buffer
     */
    public void putTriangleIndex(int i, int j, int k) {
		Integer[] tempIndices= new Integer[] {i, j, k};
		triangleIndices.add(tempIndices);
		triangleLengths.add(3);
		triangleCount=triangleIndices.size();
    }

    public void putTriangleIndex(int i, int j, int k, int l) {
		Integer[] tempIndices= new Integer[] {i, j, k, l};
		triangleIndices.add(tempIndices);
		triangleLengths.add(4);
		triangleCount=triangleIndices.size();
    }

    public void putPolygonIndices(Integer ... indices) {
		triangleIndices.add(indices);
		triangleCount=triangleIndices.size();
    }

    public void putPointIndex(int i) {
		pointIndices.add(i);
		pointCount = pointIndices.size();
    }

    public void putLineIndex(int i) {
		lineIndices.add(i);
		lineCount = lineIndices.size();
    }
    public void putLineLength(int i) {
		lineLengths.add(i);
		lineCount = lineCount + 1;
		lineCount = lineIndices.size();
    }


    /**
     * Sets the triangleIndices
     *
     * @param bufferData
     *            the new triangleIndices
     */
    public void setTriangleIndicesSize(int tris) {
        if (triangleIndices==null){
	        triangleIndices  = new ArrayList<Integer[]>(tris);
        } else {
        	triangleIndices = null;
	        triangleIndices = new ArrayList<Integer[]>(tris);
        }
//       	triangleCount = tris;
    }
    /**
     * Gets the total primitive count.
     *
     * @return the sum of the primitive counts on all sections of this mesh data.
     */
    public int getTriangleCount() {
        return triangleCount;
    }

	public void makeNormals(){
		int first = 0;
		int second = 1;
		int last;
		int len;
		Vector3D firstV;
		Vector3D secondV;
		Vector3D lastV;
		Vector3D firstEdge = new Vector3D(0, 0, 0);
		Vector3D lastEdge = new Vector3D(0, 0, 0);
		Vector3D normal = new Vector3D(0, 0, 0);
//System.out.println("vertexCount=" + vertexCount);
		setNormalCoordsSize(vertexCount);
		for(int k=0; k<vertexCount; k++){
			normalCoords.add(new Vector3D(0, 0, 0));
		}
		normalCount=normalCoords.size();
		for(int i=0; i<triangleCount; i++){
			len = triangleLengths.get(i);
//System.out.println("i=" + i + "  len=" + len);
			last = len-1;
			firstV = vertexCoords.get(triangleIndices.get(i)[first]);
			secondV = vertexCoords.get(triangleIndices.get(i)[second]);
			lastV = vertexCoords.get(triangleIndices.get(i)[last]);
			Vector3D.getVector(firstV, secondV, firstEdge);
			Vector3D.getVector(firstV, lastV, lastEdge);
			Vector3D.normalVector(firstEdge, lastEdge, normal);
			for (int j=0; j<len; j++){
//System.out.println("triangleIndices.get(i)[j]=" + triangleIndices.get(i)[j]);
				int normalIndex = triangleIndices.get(i)[j];
				normalCoords.get(normalIndex).addThis(normal);
			}			
		}
		for(int k=0; k<normalCount; k++){
			normalCoords.get(k).normaliseThis();
		}
	}

	public void createTriangles(){

		if (!getChildren().isEmpty()) {
			getChildren().removeAll();
			tempList.clear();
		}
		//tri = new Triangle[indexCount];
////		tri = new Triangle3D[triangleCount];
		tri = new Polygon3D[triangleCount];
		ObservableList list = getChildren();
System.out.println();
System.out.println("Triangle=");
System.out.println("==========");
System.out.println("vertexCount=" + vertexCount);
System.out.println("normalCount=" + normalCount);
System.out.println("triangleCount=" + triangleCount);

		for (int i=0; i<triangleCount; i++){
//System.out.println("POLY no. " + i);
//if(i==3||i==4){
//System.out.println("i0=" + triangleIndices.get(i)[0] + " i1=" + triangleIndices.get(i)[1] + " i2=" + triangleIndices.get(i)[2]);
//System.out.println("v0=" + vertexCoords.get(triangleIndices.get(i)[0]) + " v1=" + vertexCoords.get(triangleIndices.get(i)[1]) + " v2=" + vertexCoords.get(triangleIndices.get(i)[2]));
//System.out.println("n0=" + normalCoords.get(triangleIndices.get(i)[0]) + " n1=" + normalCoords.get(triangleIndices.get(i)[1]) + " n2=" + normalCoords.get(triangleIndices.get(i)[2]));
//}
			//tri[i]=new Triangle(vertexCoords[triangleIndices[0][i]], vertexCoords[triangleIndices[1][i]], vertexCoords[triangleIndices[2][i]]);
			//tri[i]=new Triangle3D(vertexCoords[triangleIndices[0][i]], vertexCoords[triangleIndices[1][i]], vertexCoords[triangleIndices[2][i]]);
			////tri[i]=new Triangle3D(vertexCoords.get(triangleIndices.get(i)[0]), vertexCoords.get(triangleIndices.get(i)[1]), vertexCoords.get(triangleIndices.get(i)[2]));

			//if (i==65 || i==64){
			//tri[i]=new Polygon3D(vertexCoords.get(triangleIndices.get(i)[0]), vertexCoords.get(triangleIndices.get(i)[1]), vertexCoords.get(triangleIndices.get(i)[2]), true);
			//} else {
			//tri[i]=new Polygon3D(vertexCoords.get(triangleIndices.get(i)[0]), vertexCoords.get(triangleIndices.get(i)[1]), vertexCoords.get(triangleIndices.get(i)[2]));
			//}
			int len = triangleLengths.get(i);
			Vector3D[] tempVerts = new Vector3D[len];
			Vector3D[] tempNorms = new Vector3D[len];
			for (int j=0; j<len; j++){
				tempVerts[j] = vertexCoords.get(triangleIndices.get(i)[j]);
				if (normalCount>0) tempNorms[j] = normalCoords.get(triangleIndices.get(i)[j]);
			}
			tri[i]=new Polygon3D(tempVerts, false);
			if (normalCount>0)  if (context.showVertexNormals)  tri[i].showVertexNormals(tempNorms);
			tri[i].setIdent(i);
			//if (i==5) tri[i].polygon.setFill(Color.PINK);
			//if (i==15) tri[i].polygon.setFill(Color.PINK);
			//if (context.showWorking) tri[i].setShowWorking(true);
			if (context.showNormals)  tri[i].showNormal();
			//if (context.showVertexNormals)  tri[i].showVertexNormals(normalCoords[triangleIndices[0][i]], normalCoords[triangleIndices[1][i]], normalCoords[triangleIndices[2][i]]);
			//if (context.showVertexNormals)  tri[i].showVertexNormals(normalCoords.get(triangleIndices.get(i)[0]), normalCoords.get(triangleIndices.get(i)[1]), normalCoords.get(triangleIndices.get(i)[2]));
			if (context.showBorders)  tri[i].showBorder();
			if (context.showTexts)  tri[i].showText();
// ???	//if (context.showLight)   tri[i].calculateShade(lightVector);
			//tri[i].calculateGradientShade(lightVector, normalCoords[triangleIndices[0][i]], normalCoords[triangleIndices[1][i]], normalCoords[triangleIndices[2][i]]);
			//tri[i].calculateGradientShade(context.lighting, normalCoords[triangleIndices[0][i]], normalCoords[triangleIndices[1][i]], normalCoords[triangleIndices[2][i]]);
			//tri[i].calculateAverageGradientShade(context.lighting, material, normalCoords[triangleIndices[0][i]], normalCoords[triangleIndices[1][i]], normalCoords[triangleIndices[2][i]]);

//tri[i].calculateAverageShade(context.lighting, material);
//tri[i].calculateAverageGradientShade(context.lighting, material, normalCoords.get(triangleIndices.get(i)[0]), normalCoords.get(triangleIndices.get(i)[1]), normalCoords.get(triangleIndices.get(i)[2]));


if (normalCount==0){
tri[i].calculateAverageShade(context.lighting, material);
} else {
tri[i].calculateAverageGradientShade(context.lighting, material, normalCoords.get(triangleIndices.get(i)[0]), normalCoords.get(triangleIndices.get(i)[1]), normalCoords.get(triangleIndices.get(i)[2]));
}
			//tri[i].calculateGradientShadeV3(lightVector, normalCoords[triangleIndices[0][i]], normalCoords[triangleIndices[1][i]], normalCoords[triangleIndices[2][i]]);
			tempList.add(tri[i]);
		}
		list.addAll(tempList);
	}


}