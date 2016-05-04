// Control camera with: left button - rotate in orbit
//                     right butron - translate
package moncgui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

public class MouseHandler{

	//Scene scene;
	//CameraView cameraView;
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

	public MouseHandler(Scene scene, final CameraView cameraView){

        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                mousePosX = me.getX();
                mousePosY = me.getY();
                mouseOldX = me.getX();
                mouseOldY = me.getY();
                //System.out.println("scene.setOnMousePressed " + me);
            }
        });

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getX();
                mousePosY = me.getY();
                mouseDeltaX = mousePosX - mouseOldX;
                mouseDeltaY = mousePosY - mouseOldY;
//                if (me.isAltDown() && me.isShiftDown() && me.isPrimaryButtonDown()) {
                if (me.isAltDown() &&  me.isPrimaryButtonDown()) {
                    double rzAngle = cameraView.getRz();
                    cameraView.setRz(rzAngle - mouseDeltaX);
                }
//                else if (me.isAltDown() && me.isPrimaryButtonDown()) {
                else if (me.isPrimaryButtonDown()) {
                    double ryAngle = cameraView.getRy();
                    cameraView.setRy(ryAngle + mouseDeltaX);
                    double rxAngle = cameraView.getRx();
                    cameraView.setRx(rxAngle + mouseDeltaY);
                    //System.out.println("ROTATE rx=" + cameraView.getRx() + " ry=" + cameraView.getRy());

                }
                //else if (me.isShiftDown() && me.isPrimaryButtonDown()) {
                //    double yShear = shear.getY();
                //    shear.setY(yShear + mouseDeltaY/1000.0);
                //    double xShear = shear.getX();
                //    shear.setX(xShear + mouseDeltaX/1000.0);
                //}
//                else if (me.isAltDown() && me.isSecondaryButtonDown()) {

                else if (me.isMiddleButtonDown()) {
                    double scale = cameraView.getXScale();
                    //System.out.println("Scale=" + scale);
//                    double newScale = scale + mouseDeltaX*0.01;
                    double newScale = scale + mouseDeltaX*1.0;
                    cameraView.setXScale(newScale);
                    cameraView.setYScale(newScale);
                    cameraView.setZScale(newScale);
                    //cameraView.setScale(newScale);
                }
//                else if (me.isAltDown() && me.isMiddleButtonDown()) {
                else if (me.isSecondaryButtonDown()) {
                    double tx = cameraView.getXTranslate();
                    double ty = cameraView.getYTranslate();
                    //System.out.println("TX=" + tx + " TY=" + ty);
                    cameraView.setXTranslate(tx + mouseDeltaX);
                    cameraView.setYTranslate(ty + mouseDeltaY);
                }
                /*
                System.out.println("t = (" +
                                   cam.t.getX() + ", " +
                                   cam.t.getY() + ", " +
                                   cam.t.getZ() + ") " +
                                   "r = (" +
                                   cam.rx.getAngle() + ", " +
                                   cam.ry.getAngle() + ", " +
                                   cam.rz.getAngle() + ") " +
                                   "s = (" +
                                   cam.s.getX() + ", " +
                                   cam.s.getY() + ", " +
                                   cam.s.getZ() + ")");
                */
                //cameraView.updateStatusText();
            }
        });
	}
}
