// View from camera
package moncgui;

import javafx.geometry.Bounds;
import javafx.scene.*;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.transform.*;
import javafx.stage.Stage;


class CameraView extends CameraTransform {
    final CameraTransform cam = new CameraTransform();
    public Text statusText=new Text(0,0,"");

    Polygon viewAxis0 = new Polygon(0,0,0,0,0,0);
    Polygon viewAxis1 = new Polygon(1,0,1,0,1,0);
    //Line viewAxis = new Line(0,0,1000,0);

    final Translate zt  = new Translate();

    final Rotate zrx = new Rotate(0, Rotate.X_AXIS);
    final Rotate zry = new Rotate(0, Rotate.Y_AXIS);
    final Rotate zrz = new Rotate(0, Rotate.Z_AXIS);

    public CameraView(){
    	//viewAxis.getTransforms().add(new Rotate(-90,Rotate.Y_AXIS));

    viewAxis0.getTransforms().addAll(zt);
    viewAxis1.getTransforms().addAll(zrx, zrz, zry);

        getChildren().add(cam);
        getChildren().add(statusText);
        getChildren().add(viewAxis0);
        getChildren().add(viewAxis1);
        resetCam();
    }

	public void updateStatusText(){
		zrx.setAngle(-getRx());
		zry.setAngle(-90+getRy());
		zrz.setAngle(-getRz());
		zt.setX(-getXTranslate());
		zt.setY(getYTranslate());
		zt.setZ(-getZTranslate());
		//statusText.setText("RX=" + getRx() + " RY=" + getRy() + " RZ=" + getRz() + " TX=" + getXTranslate()  + " TY=" + getYTranslate()  + " TZ=" + getZTranslate() );
	////statusText.setText("X=" + viewAxis0.getBoundsInParent().getMinX() + " Y=" + viewAxis0.getBoundsInParent().getMinY() + " Z=" + viewAxis0.getBoundsInParent().getMinZ() + "\n"   + "X=" + viewAxis1.getBoundsInParent().getMinX() + " Y=" + viewAxis1.getBoundsInParent().getMinY() + " Z=" + viewAxis1.getBoundsInParent().getMinZ() );
	}

	public void add(Group gr){
		cam.getChildren().add(gr);
        updateStatusText();
	}

	public void add(Node no){
		cam.getChildren().add(no);
        updateStatusText();
	}

	public double getRx(){
		 return cam.rx.getAngle();
	}
	public double getRy(){
		 return cam.ry.getAngle();
	}
	public double getRz(){
		 return cam.rz.getAngle();
	}

	public void setRx(double angle){
		 cam.rx.setAngle(angle);
        updateStatusText();
	}
	public void setRy(double angle){
		 cam.ry.setAngle(angle);
        updateStatusText();
	}
	public void setRz(double angle){
		 cam.rz.setAngle(angle);
        updateStatusText();
	}

	public double getXScale(){
		return cam.s.getX();
	}
	public double getYScale(){
		return cam.s.getY();
	}
	public double getZScale(){
		return cam.s.getZ();
	}
	public void setXScale(double dist){
		cam.s.setX(dist);
        updateStatusText();
	}
	public void setYScale(double dist){
		cam.s.setY(dist);
        updateStatusText();
	}
	public void setZScale(double dist){
		cam.s.setZ(dist);
        updateStatusText();
	}

	public double getXTranslate(){
		return cam.t.getX();
	}
	public double getYTranslate(){
		return cam.t.getY();
	}
	public double getZTranslate(){
		return cam.t.getZ();
	}
	public void setXTranslate(double dist){
		cam.t.setX(dist);
        updateStatusText();
	}
	public void setYTranslate(double dist){
		cam.t.setY(dist);
        updateStatusText();
	}
	public void setZTranslate(double dist){
		cam.t.setZ(dist);
        updateStatusText();
	}

    public void setView(double width, double height){
        cam.p.setX(width);
        cam.ip.setX(-width);
        cam.p.setY(height);
        cam.ip.setY(-height);
        updateStatusText();
    }


    //=========================================================================
    // CubeSystem.frameCam
    //=========================================================================
    public void frameCam(final Stage stage, final Scene scene) {
//System.out.println("00 camOffset=" + this);
//System.out.println("00 cam      =" + cam);
        setCamOffset(scene);
        // cam.resetTSP();
//System.out.println("01 camOffset=" + this);
//System.out.println("01 cam      =" + cam);
        setCamPivot();
//System.out.println("02 camOffset=" + this);
//System.out.println("02 cam      =" + cam);
        setCamTranslate();
//System.out.println("03 camOffset=" + this);
//System.out.println("03 cam      =" + cam);
        setCamScale(scene);
        updateStatusText();
    }

    //=========================================================================
    // CubeSystem.setCamOffset
    //=========================================================================
    public void setCamOffset(final Scene scene) {
        double width = scene.getWidth();
        double height = scene.getHeight();
        setView(width, height); // moved to here from main app.
        t.setX(width/2.0);
        t.setY(height/2.0);
        updateStatusText();
    }

    //=========================================================================
    // setCamScale
    //=========================================================================
    public void setCamScale(final Scene scene) {
        final Bounds bounds = cam.getBoundsInLocal();
        final double pivotX = bounds.getMinX() + bounds.getWidth()/2;
        final double pivotY = bounds.getMinY() + bounds.getHeight()/2;
        final double pivotZ = bounds.getMinZ() + bounds.getDepth()/2;

        double width = scene.getWidth();
        double height = scene.getHeight();

        double scaleFactor = 1.0;
        double scaleFactorY = 1.0;
        double scaleFactorX = 1.0;
        if (bounds.getWidth() > 0.0001) {
            scaleFactorX = width / bounds.getWidth() / 2.0;
        }
        if (bounds.getHeight() > 0.0001) {
            scaleFactorY = height / bounds.getHeight() / 2.0;
        }
        if (scaleFactorX > scaleFactorY) {
            scaleFactor = scaleFactorY;
        } else {
            scaleFactor = scaleFactorX;
        }
        cam.s.setX(scaleFactor);
        cam.s.setY(scaleFactor);
        cam.s.setZ(scaleFactor);
        updateStatusText();
    }

    //=========================================================================
    // setCamPivot
    //=========================================================================
    public void setCamPivot() {
        final Bounds bounds = cam.getBoundsInLocal();
//System.out.println("bounds=" + bounds);
        final double pivotX = bounds.getMinX() + bounds.getWidth()/2;
        final double pivotY = bounds.getMinY() + bounds.getHeight()/2;
        final double pivotZ = bounds.getMinZ() + bounds.getDepth()/2;
        cam.p.setX(pivotX);
        cam.p.setY(pivotY);
        cam.p.setZ(pivotZ);
        cam.ip.setX(-pivotX);
        cam.ip.setY(-pivotY);
        cam.ip.setZ(-pivotZ);
        updateStatusText();
    }

    //=========================================================================
    // setCamTranslate
    //=========================================================================
    public void setCamTranslate() {
        final Bounds bounds = cam.getBoundsInLocal();
        final double pivotX = bounds.getMinX() + bounds.getWidth()/2;
        final double pivotY = bounds.getMinY() + bounds.getHeight()/2;
        final double pivotZ = bounds.getMinZ() + bounds.getDepth()/2;
        cam.t.setX(-pivotX);
        cam.t.setY(-pivotY);
        updateStatusText();
    }

    public void resetCam() {
        // t = (45.840667724609375, -25.478256225585938, 0.0)
        // r = (-44.180908203125, -33.4022216796875, 0.0)
        // s = (1.2595635986328126, 1.2595635986328126, 1.2595635986328126)

        /*
        cam.t.setX(45.84);
        cam.t.setY(-25.47);
        cam.t.setZ(0.0);
        cam.rx.setAngle(-44.18);
        cam.ry.setAngle(-33.40);
        cam.rz.setAngle(0.0);
        cam.s.setX(1.25);
        cam.s.setY(1.25);
        cam.s.setZ(1.25);
        */
        cam.t.setX(0.0);
        cam.t.setY(0.0);
        cam.t.setZ(0.0);

////        cam.rx.setAngle(45.0);
////        cam.ry.setAngle(-7.0);
////        cam.rz.setAngle(0.0);
////        cam.s.setX(1.25);
////        cam.s.setY(1.25);
////        cam.s.setZ(1.25);

        cam.rx.setAngle(180.0);
        cam.ry.setAngle(0.0);
        cam.rz.setAngle(0.0);
        cam.s.setX(1.0);
        cam.s.setY(1.0);
        cam.s.setZ(1.0);

// translate and rotate group so that origin is center and +Y is up
//root.setTranslateX(400/2); root.setTranslateY(150/2);
//root.getTransforms().add(new Rotate(180,Rotate.X_AXIS));

        cam.p.setX(0.0);
        cam.p.setY(0.0);
        cam.p.setZ(0.0);

        cam.ip.setX(0.0);
        cam.ip.setY(0.0);
        cam.ip.setZ(0.0);

        final Bounds bounds = cam.getBoundsInLocal();
//System.out.println("0 bounds=" + bounds);
        final double pivotX = bounds.getMinX() + bounds.getWidth() / 2;
        final double pivotY = bounds.getMinY() + bounds.getHeight() / 2;
        final double pivotZ = bounds.getMinZ() + bounds.getDepth() / 2;

        cam.p.setX(pivotX);
        cam.p.setY(pivotY);
        cam.p.setZ(pivotZ);

        cam.ip.setX(-pivotX);
        cam.ip.setY(-pivotY);
        cam.ip.setZ(-pivotZ);

        updateStatusText();
    }

}