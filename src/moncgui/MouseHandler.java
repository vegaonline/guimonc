// Control camera with: left button - rotate in orbit
//                     right butron - translate
package moncgui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.*;

/**
 *
 * @author vega
 */
public class MouseHandler {

    //Scene scene;
    //CameraView cameraView;
    double mousePosX;
    double mousePosY;
    double mouseOldX;
    double mouseOldY;
    double mouseDeltaX;
    double mouseDeltaY;

    public MouseHandler(Scene scene, final CameraView cameraView) {

        scene.setOnMousePressed (new EventHandler<MouseEvent> () {
            public void handle(MouseEvent me) {
                mousePosX = me.getX ();
                mousePosY = me.getY ();
                mouseOldX = me.getX ();
                mouseOldY = me.getY ();
                //System.out.println("scene.setOnMousePressed " + me);
            }
        });

        scene.setOnScroll (new EventHandler<ScrollEvent> () {

            public void handle(ScrollEvent sc) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = sc.getX ();
                mousePosY = sc.getY ();
                mouseDeltaX = mousePosX - mouseOldX;
                mouseDeltaY = mousePosY - mouseOldY;
                double newScale = 0;
                double scale = cameraView.getXScale ();
                if ( sc.getDeltaY () > 0 ) {
                    newScale = scale * 1.1;
                    cameraView.setXScale (newScale);
                    cameraView.setYScale (newScale);
                    cameraView.setZScale (newScale);
                } else if ( sc.getDeltaY () < 0 ) {
                    newScale = scale * 1.0 / 1.1;
                    cameraView.setXScale (newScale);
                    cameraView.setYScale (newScale);
                    cameraView.setZScale (newScale);
                }
                cameraView.updateStatusText ();
            }
        }
        );

        scene.setOnMouseDragged (new EventHandler<MouseEvent> () {
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getX ();
                mousePosY = me.getY ();
                mouseDeltaX = mousePosX - mouseOldX;
                mouseDeltaY = mousePosY - mouseOldY;
//                if (me.isAltDown() && me.isShiftDown() && me.isPrimaryButtonDown()) {
                if ( me.isAltDown () && me.isPrimaryButtonDown () ) {
                    double rzAngle = cameraView.getRz ();
                    cameraView.setRz (rzAngle - mouseDeltaX);
                } //                else if (me.isAltDown() && me.isPrimaryButtonDown()) {
                else if ( me.isPrimaryButtonDown () ) {
                    double ryAngle = cameraView.getRy ();
                    cameraView.setRy (ryAngle + mouseDeltaX);
                    double rxAngle = cameraView.getRx ();
                    cameraView.setRx (rxAngle + mouseDeltaY);
                } else if ( me.isSecondaryButtonDown () ) {
                    double tx = cameraView.getXTranslate ();
                    double ty = cameraView.getYTranslate ();
                    cameraView.setXTranslate (tx + mouseDeltaX);
                    cameraView.setYTranslate (ty + mouseDeltaY);
                }
                cameraView.updateStatusText ();
            }
        });
    }
}
