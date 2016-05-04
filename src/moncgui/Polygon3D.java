// Polygon in 3D
package moncgui;

import java.util.*;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.*;
import moncgui.Material;


//import odk.lang.*;


    public class Polygon3D extends Group {

    	final Polygon polygon;

		Polygon P1;
		Polygon P2;
		Polygon P3;

		final Line[] borders;
//		final Rod3D[] bordersR;
		javafx.scene.text.Text text;

    	Vector3D[] O;
    	Point3D[] P;
    	Point3D   cp;

		int NUM;

		final List pointsXY = new ArrayList();
		double pX;
		double pY;
		double[] PX;
		double[] PY;

    	Vector3D[] vertices;
    	//Vector3D   ct;
    	//Vector3D   normal;

		double[] edgeDist;

    	Group root = new Group();
    	Group nonTransformedItems = new Group();
    	Group decoration = new Group();
    	Group perimeter = new Group();

    	Cube centroidNormal;

		int id;
		double normalScale=0.25;

		final static double RAD2DEG = 360.0/(2*Math.PI);

		final static double[][] R = new double[3][3];

		final static Vector3D normalZ = new Vector3D(0,0,1);
		final static Vector3D normal = new Vector3D(0,0,0);
		final static Vector3D Raxis   = new Vector3D(0,0,0);
		final static Vector3D axisR   = new Vector3D(0,0,0);

		final static Vector3D OfirstEdge = new Vector3D(0,0,0);
		final static Vector3D OlastEdge = new Vector3D(0,0,0);


        public Polygon3D(Vector3D[] O) {
        	this(O, false);
        }

        public Polygon3D(Vector3D O1, Vector3D O2, Vector3D O3) {
        	this(new Vector3D[]{O1, O2, O3}, false);
        }

        public Polygon3D(Vector3D O1, Vector3D O2, Vector3D O3, boolean debug) {
        	this(new Vector3D[]{O1, O2, O3}, debug);
        }

        public Polygon3D(Vector3D[] O, boolean debug) {

			this.O = O;

        	//if (b) showWorking=true;
			NUM = O.length;
        	int first = 0;
        	int second = 1;
        	int last = NUM-1;

			vertices = new Vector3D[NUM];
			P 		 = new Point3D[NUM];
			PX		 = new double[NUM];
			PY		 = new double[NUM];
			borders  = new Line[NUM];

			//Vector3D OfirstEdge = Vector3D.getVector(O[first], O[second]);
			//Vector3D OlastEdge = Vector3D.getVector(O[first], O[last]);
			Vector3D.getVector(O[first], O[second], OfirstEdge);
			Vector3D.getVector(O[first], O[last], OlastEdge);
			//Vector3D Onormal = Vector3D.crossProduct(OfirstEdge, OlastEdge);
                Vector3D cross = Vector3D.normalVector(OfirstEdge, OlastEdge);
                Vector3D crossNormal = Vector3D.normalise(cross);

			Vector3D.normalVector(OfirstEdge, OlastEdge, normal);
			//Vector3D OnormalUnit = Vector3D.normalise(Onormal);


			double Rangle = Vector3D.angleBetween(normalZ, normal);
			Vector3D.normalVector(normal, normalZ, Raxis);
			if (Vector3D.length(Raxis)<0.001){
				Raxis.setXYZ(1, 0, 0);
			}
			Vector3D.getRotateAngleAxisMatrix(Rangle, Raxis, R);


if (debug){
	System.out.println();
	System.out.println();
	System.out.println("id=" + id);
	System.out.println("NORMAL very small");
	System.out.println("NORMAL=" + normal);
	System.out.println("OfirstEdge =" + OfirstEdge);
	System.out.println("OlastEdge =" + OlastEdge);
	System.out.println("cross =" + cross);
	System.out.println("cromal=" + crossNormal);
	System.out.println("crossl=" + Vector3D.length(cross));
	System.out.println("Rangle=" + Rangle);
	System.out.println("Raxis=" + Raxis);
	System.out.println("normal=" + normal);
	System.out.println("normalZ=" + normalZ);
}


			for (int i=0; i<NUM; i++){
				vertices[i] = Vector3D.rotateAngleAxis(Vector3D.subtract(O[i],O[0]), R);
				pX = vertices[i].getX();
				pY = vertices[i].getY();

				if (debug){
					double ppX=pX;
					double ppY=pY;
				}

				P[i] = new Point3D(pX, pY, 0);
				pointsXY.add(pX);
				pointsXY.add(pY);

				if (i>0){
//					bordersR[i-1] = new Rod3D(P[i-1].getX(), P[i-1].getY(), P[i].getX(), P[i].getY());
					borders[i-1] = new Line(P[i-1].getX(), P[i-1].getY(), P[i].getX(), P[i].getY());
					borders[i-1].setStrokeWidth(0.2);
				}
				if (i==last){
//					bordersR[i] = new Rod3D(P[i].getX(), P[i].getY(), P[0].getX(), P[0].getY());
					borders[i] = new Line(P[i].getX(), P[i].getY(), P[0].getX(), P[0].getY());
					borders[i].setStrokeWidth(0.2);


				}

				if (debug){
					if (i==0) P1 = new Polygon(pX, pY, pX, pY, pX, pY);
					if (i==1) P2 = new Polygon(pX, pY, pX, pY, pX, pY);
					if (i==2) P3 = new Polygon(pX, pY, pX, pY, pX, pY);

					System.out.println();
					System.out.println("i=" + i);
					System.out.println("O[i]" + O[i]);
					System.out.println("vertices[i]" + vertices[i]);
				}
			}


			if (debug){
				System.out.println("vertices" + vertices);
			}

			polygon = PolygonBuilder.create().points(pointsXY).build();
			polygon.setSmooth(true);
			//perimeter.getChildren().addAll(borders);


			cp = setCentroid(P);

			// cube at centroid to scale into rod to act as normal vector
			centroidNormal = new Cube(1.0, Color.BLUE, 1.0);
			Vector3D ct = new Vector3D(cp.getX(), cp.getY(), 0);
			centroidNormal.getTransforms().add(new Translate(ct.getX(),ct.getY(),ct.getZ()));

			centroidNormal.getTransforms().add(new Scale(normalScale, normalScale, 40*normalScale));
			centroidNormal.getTransforms().add(new Translate(0, 0, 0.5));
			centroidNormal.setFrontColor(Color.BLACK);

			perimeter.getTransforms().add(new Translate(0, 0, 0.01));

			root.getChildren().addAll(polygon);
			root.getChildren().addAll(perimeter);
			root.getChildren().addAll(decoration);

			Translate t = new Translate(O[0].getX(), O[0].getY(), O[0].getZ());
			root.getTransforms().add(t);
			Rotate r1 = new Rotate(Vector3D.radToDeg(-Rangle),0,0,0,Raxis.toPoint3D());
			root.getTransforms().add(r1);

			getChildren().addAll(root);
			getChildren().addAll(nonTransformedItems);

			polygon.setFill(Color.GREEN);

			if (debug){
				P1.getTransforms().add(t);
				P2.getTransforms().add(t);
				P3.getTransforms().add(t);
				P1.getTransforms().add(r1);
				P2.getTransforms().add(r1);
				P3.getTransforms().add(r1);

				Vector3D S1 = getVectorFromSingularBounds(P1);
				Vector3D S2 = getVectorFromSingularBounds(P2);
				Vector3D S3 = getVectorFromSingularBounds(P3);

				// get difference between final calculated point and desired input
				double diff1X=Math.abs(O[0].getX()-S1.getX());
				double diff2X=Math.abs(O[1].getX()-S2.getX());
				double diff3X=Math.abs(O[2].getX()-S3.getX());
				double diff1Y=Math.abs(O[0].getY()-S1.getY());
				double diff2Y=Math.abs(O[1].getY()-S2.getY());
				double diff3Y=Math.abs(O[2].getY()-S3.getY());
				double diff1Z=Math.abs(O[0].getZ()-S1.getZ());
				double diff2Z=Math.abs(O[1].getZ()-S2.getZ());
				double diff3Z=Math.abs(O[2].getZ()-S3.getZ());

				// NEED TO ACCOUNT FOR ROUNDOFF ERRORS
				// if final points are not same as input then we must have rotated phi the wrong way
				double tolerance = 0.00001;
				//if (diff1X>tolerance || diff1Y>tolerance || diff1Z>tolerance ||
				//	diff2X>tolerance || diff2Y>tolerance || diff2Z>tolerance ||
				//	diff3X>tolerance || diff3Y>tolerance || diff3Z>tolerance ){
				if (debug){
				System.out.println(" O1=" + O[0] + " O2=" + O[1] + " O3=" + O[2]);
				System.out.println(" S1=" + S1 + " S2=" + S2 + " S3=" + S3);
				//System.out.println(" Polygon=" + polygon.getPoints());
				//System.out.println();
				//System.out.println("P2 boundsP=" + P2.getBoundsInParent());
				//System.out.println("P2 boundsL=" + P2.getBoundsInLocal());
				System.out.println(" axisR=" + axisR);
				//System.out.println(" AxisUnit=" + axisUnit);
				System.out.println(" Raxis=" + Raxis);
				System.out.println(" Length=" + Vector3D.length(axisR));
				System.out.println(" Rangle=" + Rangle);
				System.out.println();
				polygon.setFill(Color.YELLOW);
				//System.out.println(" diff1X=" + diff1X + " diff1Y=" + diff1Y + " diff1Z=" + diff1Z);
				//System.out.println(" diff2X=" + diff2X + " diff2Y=" + diff2Y + " diff2Z=" + diff2Z);
				//System.out.println(" diff3X=" + diff3X + " diff3Y=" + diff3Y + " diff3Z=" + diff3Z);
				//System.out.println("");

			}

			//polygon.setFill(Color.GREEN);
        }

    }


	public Point3D setCentroid(Point3D[] P){
		double centroidX = 0;
		double centroidY = 0;
		double centroidZ = 0;
		double num = P.length;
		for (int i=0;i<num;i++){
			centroidX = centroidX + P[i].getX();
			centroidY = centroidY + P[i].getY();
			centroidZ = centroidZ + P[i].getZ();
		}
		return new Point3D(centroidX/num, centroidY/num, centroidZ/num);
	}

	public void showNormal(){
		decoration.getChildren().add(centroidNormal);
	}
	//public void showVertexNormals(Vector3D normalCoord0, Vector3D normalCoord1, Vector3D normalCoord2){
	public void showVertexNormals(Vector3D[] normalCoords){

//System.out.println("normalCoord0=" + normalCoord0 + "  normalCoord1=" + normalCoord1 + "  normalCoord2=" + normalCoord2);
		Rod3D[] rods = new Rod3D[NUM];
		for (int i=0; i<NUM; i++){
			rods[i] = new Rod3D(O[i], Vector3D.add(O[i], Vector3D.multiply(normalCoords[i], 5)), Color.MAGENTA, 0.25);
			nonTransformedItems.getChildren().add(rods[i]);
//System.out.println("normalCoords[i]=" + normalCoords[i]);
//System.out.println("rods[i]=" + rods[i]);
		}
//System.out.println("rn1=" + rn1);
//System.out.println("");
		//Rod3D rn2 = new Rod3D(vertices[1], Vector3D.add(vertices[1], Vector3D.multiply(normalCoord1, 5)), Color.CYAN, 0.25);
//System.out.println("rn2=" + rn2);
//System.out.println("");
		//Rod3D rn3 = new Rod3D(vertices[2], Vector3D.add(vertices[2], Vector3D.multiply(normalCoord2, 5)), Color.YELLOW, 0.25);
//System.out.println("rn3=" + rn3);
//System.out.println("");
//System.out.println("");
//System.out.println("");
		//nonTransformedItems.getChildren().addAll(rn1, rn2, rn3);
//		decoration.getChildren().addAll(rn1, rn2, rn3);
//		getChildren().add(decoration);
	}
	public void removeNormal(){
		decoration.getChildren().remove(centroidNormal);
	}
	public void setNormalScale(double s){
		normalScale = s;
		centroidNormal.getTransforms().add(new Translate(0, 0, -0.5));
		centroidNormal.getTransforms().add(new Scale(s, s, s));
		centroidNormal.getTransforms().add(new Translate(0, 0, 0.5));
	}
	public void showBorder(){
		perimeter.getChildren().addAll(borders);

	}
	public void removeBorder(){
		perimeter.getChildren().remove(borders);
	}

	//public void setShowWorking(boolean b){
	//	showWorking = b;
	//}

	public void showText(){
		setText("" + id);
		decoration.getChildren().add(text);
	}

	public void setText(String s){
		text = new javafx.scene.text.Text(cp.getX(),cp.getY(),s);
		text.setFont(new Font(4));
		text.setRotate(180);
		text.setRotationAxis(Rotate.X_AXIS);
		double width = text.getBoundsInLocal().getMaxX()-text.getBoundsInLocal().getMinX();
		text.getTransforms().add(new Translate(-width/2,0,0));
		text.setTextOrigin(VPos.CENTER);
		text.toFront();

	}

	public void setIdent(int i){
		id = i;
	}


	public double calculateDiffuse(Vector3D normalCoord, Vector3D lightVector, double Kd){
		//double theta = Vector3D.angleBetween(normalCoord, lightVector);
		double dotP = Vector3D.dotProduct(normalCoord.normaliseThis(), lightVector.normaliseThis());
//System.out.println("DIFFUSE lightVector=" + lightVector);
//System.out.println("DIFFUSE normalCoord=" + normalCoord);
//System.out.println("DIFFUSE thetaDeg=" + radToDeg(theta));
//System.out.println("cos  =" + Math.cos(theta));
//System.out.println("shade=" + (0.6+0.4*Math.cos(theta)) + " " + 256*(0.6+0.4*Math.cos(theta)));
//System.out.println();
		//return (0.6+0.4*Math.cos(theta));
//System.out.println("lightVectorKd=" + lightVectorKd);
//System.out.println("ans=" + Vector3D.multiply(lightVectorKd, Math.max(dotP,0)));
//System.out.println("");
		//return Vector3D.multiply(lightVector, Math.max(dotP,0));
		return Kd*Math.max(dotP,0);
	}


	public void calculateAverageGradientShade(Lighting3D lighting, Material material, Vector3D normalCoord0, Vector3D normalCoord1, Vector3D normalCoord2){
//System.out.println("TRI calculateGradientShade");

		//Point3D[]  normalCoords = {normalCoord0, normalCoord1, normalCoord2};
		//double[] shades = {calculateShade(normalCoords[0], lightVector), calculateShade(normalCoords[1], lightVector), calculateShade(normalCoords[2], lightVector)};
		//Point3D[]  normalCoords = {normalCoord0, normalCoord1, normalCoord2};

		Vector3D lightVector=null;
		double lightIntensity=0;
		double diffuseIntensity=0;
		double[] shades=new double[3];

		double[] vColor = {0, 0, 0};

		double ambientIntensity = lighting.getAmbientIntensity();

		vColor[0] = vColor[0] + material.Ka*ambientIntensity;
		vColor[1] = vColor[1] + material.Ka*ambientIntensity;
		vColor[2] = vColor[2] + material.Ka*ambientIntensity;

		for (Light3D l: lighting.getDiffuseLights()){
//System.out.println("TRI light=" + l);
			if (l.getSource()==Lighting3D.Source.PARALLEL){
//System.out.println("TRI PARALLEL light=" + l);
				lightVector = l.getP();
				lightIntensity = l.getIntensity();
				diffuseIntensity = lightIntensity*material.Kd;
//System.out.println("material.Kd=" + material.Kd);
//System.out.println("lightVector=" + lightVector);

//System.out.println("SHADES 0=" + calculateDiffuse(normalCoord0, lightVector));
				vColor[0] = vColor[0] + (calculateDiffuse(normalCoord0, lightVector, diffuseIntensity));
				vColor[1] = vColor[1] + (calculateDiffuse(normalCoord1, lightVector, diffuseIntensity));
				vColor[2] = vColor[2] + (calculateDiffuse(normalCoord2, lightVector, diffuseIntensity));
			}
		}

		if (vColor[0]>1.0) vColor[0]=1.0;
		if (vColor[1]>1.0) vColor[1]=1.0;
		if (vColor[2]>1.0) vColor[2]=1.0;

		shades[0] = vColor[0];
		shades[1] = vColor[1];
		shades[2] = vColor[2];

		double maxShade = shades[0];
		int maxShadeIndex = 0;
//if(id==3||id==4){
//System.out.println("normalCoord0=" + normalCoord0 + " normalCoord1=" + normalCoord1 + " normalCoord2=" + normalCoord2);
//System.out.println("shades0=" + shades[0] + " shades=" + shades[1]  + " shades2=" + shades[2] );
//}
		if (shades[2]>shades[0]){
			maxShade = shades[2];
			maxShadeIndex=2;
			if (shades[1]>shades[2]){
				maxShade = shades[1];
				maxShadeIndex=1;
			}
		} else {
			if (shades[1]>shades[0]){
				maxShade = shades[1];
				maxShadeIndex=1;
			}
		}

//System.out.println("maxShade=" + maxShade);
//System.out.println("maxShadeIndex=" + maxShadeIndex);
//System.out.println("minShadeIndexA=" + minShadeIndexA);
//System.out.println("minShadeIndexB=" + minShadeIndexB);

// USE STARTING POINT AS LARGEST VALUE
// Length of triangle sides
		Point2D[] verticesFlatT = new Point2D[3];
		List<java.lang.Double> pointsFlatT = polygon.getPoints();
		verticesFlatT[0] = new Point2D(pointsFlatT.get(0) , pointsFlatT.get(1));
		verticesFlatT[1] = new Point2D(pointsFlatT.get(2) , pointsFlatT.get(3));
		verticesFlatT[2] = new Point2D(pointsFlatT.get(4) , pointsFlatT.get(5));

		Point3D[] verticesShadeT = new Point3D[3];
		verticesShadeT[0] = new Point3D(verticesFlatT[0].getX(), verticesFlatT[0].getY(), shades[0]);
		verticesShadeT[1] = new Point3D(verticesFlatT[1].getX(), verticesFlatT[1].getY(), shades[1]);
		verticesShadeT[2] = new Point3D(verticesFlatT[2].getX(), verticesFlatT[2].getY(), shades[2]);

		Point3D shadeV1=null;
		Point3D shadeV2=null;
		if (maxShadeIndex==0){
			shadeV1 = Vector3D.normalise(Vector3D.getVector(verticesShadeT[0], verticesShadeT[1])).toPoint3D();
			shadeV2 = Vector3D.normalise(Vector3D.getVector(verticesShadeT[0], verticesShadeT[2])).toPoint3D();
		}
		if (maxShadeIndex==1){
			shadeV1 = Vector3D.normalise(Vector3D.getVector(verticesShadeT[1], verticesShadeT[2])).toPoint3D();
			shadeV2 = Vector3D.normalise(Vector3D.getVector(verticesShadeT[1], verticesShadeT[0])).toPoint3D();
		}
		if (maxShadeIndex==2){
			shadeV1 = Vector3D.normalise(Vector3D.getVector(verticesShadeT[2], verticesShadeT[0])).toPoint3D();
			shadeV2 = Vector3D.normalise(Vector3D.getVector(verticesShadeT[2], verticesShadeT[1])).toPoint3D();
		}

//System.out.println("shadeV1=" + shadeV1);
//System.out.println("shadeV2=" + shadeV2);

		Vector3D normalT  = Vector3D.normalise(Vector3D.crossProduct(new Vector3D(shadeV1), new Vector3D(shadeV2)));
		Vector3D normalUP = new Vector3D(0, 0, 1);
//System.out.println("normalT=" + normalT);
//System.out.println("normalUP=" + normalUP);

		double theta    = Vector3D.angleBetween(normalT, normalUP);
		//double thetaDeg = Triangle.angleBetweenDeg(normalT, normalUP);
//System.out.println("thetaDeg=" + thetaDeg);

		Vector3D rotAxis = Vector3D.normalise(Vector3D.crossProduct(normalUP, normalT));

//		Point3D fallAxis = new Point3D(rotAxis.getY(), -rotAxis.getX(), 0.0);
		double fallAxisX = rotAxis.getY();
		double fallAxisY = -rotAxis.getX();
//System.out.println("fallaxis=" + fallAxis);

		double gradLength = shades[maxShadeIndex]/Math.tan(theta);
//System.out.println("gradLength=" + gradLength);
//		Point2D gradStop = new Point2D(gradLength*fallAxis.getX()+verticesFlatT[maxShadeIndex].getX(), gradLength*fallAxis.getY()+verticesFlatT[maxShadeIndex].getY());
		double gradStopX = gradLength*fallAxisX+verticesFlatT[maxShadeIndex].getX();
		double gradStopY = gradLength*fallAxisY+verticesFlatT[maxShadeIndex].getY();

//if(id==3||id==4){
//System.out.println("SHADE rotAxis=" + rotAxis);
//System.out.println("SHADE gradStop=" + gradStop);
//}

		Vector3D gradColor = Vector3D.multiply(material.color, (maxShade));
//System.out.println("gradColor=" + gradColor + "material.color=" + material.color + "maxShade=" + maxShade);
		//Stop[] stopTred = new Stop[] { new Stop(0, new Color(maxShade,0.0,0.0,1.0)), new Stop(1, new Color(0.0,0.0,0.0,1.0))};
		Stop[] stopTred = new Stop[] { new Stop(0, new Color(gradColor.x,gradColor.y,gradColor.z,1.0)), new Stop(1, new Color(0.0,0.0,0.0,1.0))};
		//LinearGradient lgTred = new LinearGradient(verticesFlatT[maxShadeIndex].getX(),  verticesFlatT[maxShadeIndex].getY(), gradStop.getX(), gradStop.getY(), false, CycleMethod.NO_CYCLE, stopTred);
		LinearGradient lgTred = new LinearGradient(verticesFlatT[maxShadeIndex].getX(),  verticesFlatT[maxShadeIndex].getY(), gradStopX, gradStopY, false, CycleMethod.NO_CYCLE, stopTred);
		polygon.setFill(lgTred);
	}

/////////////////////////
// FLAT SHADE
////////////////////////
	public void calculateAverageShade(Lighting3D lighting, Material material){
//System.out.println("TRI calculateGradientShade");

		Vector3D lightVector=null;
		double lightIntensity=0;
		double diffuseIntensity=0;
		double[] shades=new double[3];

		double[] vColor = {0};

		double ambientIntensity = lighting.getAmbientIntensity();

		vColor[0] = vColor[0] + material.Ka*ambientIntensity;

		for (Light3D l: lighting.getDiffuseLights()){
//System.out.println("TRI light=" + l);
			if (l.getSource()==Lighting3D.Source.PARALLEL){
//System.out.println("TRI PARALLEL light=" + l);
				lightVector = l.getP();
				lightIntensity = l.getIntensity();
				diffuseIntensity = lightIntensity*material.Kd;
//System.out.println("material.Kd=" + material.Kd);
//System.out.println("lightVector=" + lightVector);

//System.out.println("SHADES 0=" + calculateDiffuse(normalCoord0, lightVector));
				vColor[0] = vColor[0] + (calculateDiffuse(normal, lightVector, diffuseIntensity));
			}
		}

		if (vColor[0]>1.0) vColor[0]=1.0;

		Vector3D gradColor = Vector3D.multiply(material.color, vColor[0]);
		polygon.setFill(new Color(gradColor.x,gradColor.y,gradColor.z,1.0));
	}

	public final static Point3D getPointFromSingularBounds(Shape shape){
		Bounds shapeBounds=shape.getBoundsInParent();
		return new Point3D(shapeBounds.getMinX(), shapeBounds.getMinY(), shapeBounds.getMinZ());
	}
	public final static Vector3D getVectorFromSingularBounds(Shape shape){
		Bounds shapeBounds=shape.getBoundsInParent();
		return new Vector3D(shapeBounds.getMinX(), shapeBounds.getMinY(), shapeBounds.getMinZ());
	}

}
