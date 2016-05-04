// basic transforms for camera view
package moncgui;

import javafx.scene.Group;
import javafx.scene.transform.*;

class CameraTransform extends Group {
    Translate t  = new Translate();
    Translate p  = new Translate();
    Translate ip = new Translate();

    final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    final Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    final Rotate rz = new Rotate(0, Rotate.Z_AXIS);
            
    Scale s = new Scale();
    public void setScale(double scaleFactor) {
        s.setX(scaleFactor);
        s.setY(scaleFactor);
        s.setZ(scaleFactor);
    }
    
    public CameraTransform() { 
    	super(); 
    		getTransforms().addAll(t, p, rx, rz, ry, s, ip); 
    }
    
   public String toString(){
   		String s = "t=" + t + " p=" + p + " ip=" + ip;
   		return s;
   }
}