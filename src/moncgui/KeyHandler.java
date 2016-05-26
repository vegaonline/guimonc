// Control camera with: page up key - zoom in
//                      page down key - zoom out
//                      f key - frame object (fill window)
//                      r key - reset camera
//                      space key - full screen
package moncgui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.stage.Stage;

/**
 *
 * @author vega
 */
public class KeyHandler {

    public KeyHandler(final Stage stage, final Scene scene,
            final CameraView cameraView) {

        scene.setOnKeyPressed (new EventHandler<KeyEvent> () {
            public void handle(KeyEvent ke) {
                if ( KeyCode.R.equals (ke.getCode ()) ) {
                    cameraView.resetCam ();
                    //shear.setX(0.0);
                    //shear.setY(0.0);
                }
                if ( KeyCode.F.equals (ke.getCode ()) ) {
                    cameraView.frameCam (stage, scene);
                    //shear.setX(0.0);
                    //shear.setY(0.0);
                }
                if ( KeyCode.PAGE_UP.equals (ke.getCode ()) ) {
                    double scale = cameraView.getXScale ();
                    //System.out.println("Scale=" + scale);
//                    double newScale = scale + mouseDeltaX*0.01;
                    double newScale = scale * 1.1;
                    cameraView.setXScale (newScale);
                    cameraView.setYScale (newScale);
                    cameraView.setZScale (newScale);
                }
                if ( KeyCode.PAGE_DOWN.equals (ke.getCode ()) ) {
                    double scale = cameraView.getXScale ();
                    //System.out.println("Scale=" + scale);
//                    double newScale = scale + mouseDeltaX*0.01;
                    double newScale = scale * 1 / 1.1;
                    cameraView.setXScale (newScale);
                    cameraView.setYScale (newScale);
                    cameraView.setZScale (newScale);
                }
                if ( KeyCode.SPACE.equals (ke.getCode ()) ) {
                    if ( stage.isFullScreen () ) {
                        stage.setFullScreen (false);
                        cameraView.frameCam (stage, scene);
                    } else {
                        stage.setFullScreen (true);
                        cameraView.frameCam (stage, scene);
                    }
                }
            }
        });
    }
}
