// Rod3D - a long thin cuboid
package moncgui;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;

    public class Rod3D extends Group {

		cubeTest cube;
        double size;
        double thick=0.05;

        public Rod3D(double size, Color color) {
			this(size, 0.05, color);
        }

        public Rod3D(double size, double thick, Color color) {
        	this.size = size;
        	this.thick = thick;
        	double shade=1.0;
        	double yzScale=thick/size;
        	cube = new cubeTest(size, color, shade);
        	cube.getTransforms().add(new Scale(1.0, yzScale, yzScale));
        	//cube.getTransforms().add(new Translate(0.5*size, 0, 0));
        	getChildren().addAll(cube);
        }

        public Rod3D(double P1X, double P1Y, double P2X, double P2Y, Color color){
        	this(new Vector3D(P1X, P1Y, 0), new Vector3D(P2X, P2Y, 0), color);
        }

        public Rod3D(double P1X, double P1Y, double P1Z, double P2X, double P2Y, double P2Z, Color color){
        	this(new Vector3D(P1X, P1Y, P1Z), new Vector3D(P2X, P2Y, P2Z), color);
        }

        public Rod3D(Vector3D P1, Vector3D P2, Color color){
        	this(P1, P2, color, 0.05);
        }

        public Rod3D(Vector3D P1, Vector3D P2, Color color, double thick){
        	this.thick=thick;
        	size = P1.distance(P2);
        	double shade=1.0;
        	double yzScale=thick/size;
        	cube = new cubeTest(size, color, shade);
        	cube.getTransforms().add(new Scale(1.0, yzScale, yzScale));
        	cube.getTransforms().add(new Translate(0.5*size, 0, 0));

        	//double tanTheta=(P2.getY()-P1.getY())/(P2.getX()-P1.getX());
        	//double theta=Math.atan(tanTheta);
        	//double thetaDeg = 360*theta/(2*Math.PI);

			Vector3D edge0 = new Vector3D(1,0,0);
			Vector3D edge1 = new Vector3D((P2.getX()-P1.getX())/size, (P2.getY()-P1.getY())/size, (P2.getZ()-P1.getZ())/size);

			//// cos theta from dot product
			//double cosTheta = edge0.getX()*edge1.getX() + edge0.getY()*edge1.getY() ;
			//double theta = Math.acos(cosTheta);
			//double thetaDeg = 360*theta/(2*Math.PI);

			// get angle between the two planes O and P
			double thetaDeg = Vector3D.angleBetweenDeg(edge0, edge1);
			// rotation axis is crossproduct of the two normals
			Vector3D rotAxis = Vector3D.crossProduct(edge0, edge1);
if (Vector3D.vectorLength(rotAxis)<1E-6) rotAxis = new Vector3D(0, 1, 0);
//System.out.println("P1=" + P1);
//System.out.println("P2=" + P2);
//System.out.println("thetaDeg=" + thetaDeg);
//System.out.println("rotAxis=" + rotAxis);
//System.out.println("edge0=" + edge0);
//System.out.println("edge1=" + edge1);

        	////normal from cross product
        	//double product=edge0.getX()*edge1.getY() - edge0.getY()*edge1.getX();

			//int sign = 1;
			//if (product<0) sign = -1;

//System.out.println("cosTheta=" + cosTheta);
//System.out.println("thetaDeg=" + thetaDeg);
//System.out.println("product=" + product);
//        	getTransforms().add(new Translate(P1.getX(), P1.getY(), 0));
        	getTransforms().add(new Translate(P1.getX(), P1.getY(), P1.getZ()));
        	//getTransforms().add(new Rotate(thetaDeg,P1.getX(),P1.getY(),0,Rotate.Z_AXIS));
//        	getTransforms().add(new Rotate(sign*thetaDeg,0,0,0,Rotate.Z_AXIS));
        	getTransforms().add(new Rotate(thetaDeg,0,0,0,rotAxis.toPoint3D()));
//System.out.println("1SCALE=" +  	cube.getTransforms());
        	getChildren().addAll(cube);
        }

        public void setThick(double t){
        	double yzScale=t/thick;
        	thick = t;
//System.out.println("2SCALE=" +  	cube.getTransforms());
//System.out.println("2SCALE Y=" +  	cube.getScaleY());
//System.out.println("2SCALE Z=" +  	cube.getScaleZ());
//        	cube.getTransforms().add(new Scale(1.0, yzScale, yzScale));
        	cube.setScaleY(yzScale);
        	cube.setScaleZ(yzScale);
//System.out.println("3SCALE=" +  	cube.getTransforms());
//System.out.println("3SCALE Y=" +  	cube.getScaleY());
//System.out.println("3SCALE Z=" +  	cube.getScaleZ());
        }
        public double getThick(){
        	return thick;
        }

        public String toString(){
        	Bounds b = getBoundsInParent();
        	return "minX=" + b.getMinX() + " minY=" + b.getMinY() + " minZ=" + b.getMinZ() + "  maxX=" + b.getMaxX() + " maxY=" + b.getMaxY() + " maxZ=" + b.getMaxZ();
        }
    }
