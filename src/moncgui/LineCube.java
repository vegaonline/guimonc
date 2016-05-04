/**
 * @(#)Cube.java
 *
 *
 * @author 
 * @version 1.00 2011/10/26
 */

import javafx.geometry.Bounds;
import javafx.scene.DepthTest;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.Node;

    public class LineCube extends Group {

        public LineCube(double size, Color color, double shade) {

            getChildren().addAll(
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .translateX(0)
                    .translateY( 0.5*size)
                    .translateZ(-0.5*size)                    	
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .translateX(0)
                    .translateY(-0.5*size)
                    .translateZ(-0.5*size)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .translateX(0)
                    .translateY( 0.5*size)
                    .translateZ( 0.5*size)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .translateX(0)
                    .translateY(-0.5*size)
                    .translateZ( 0.5*size)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Y_AXIS)
                    .rotate(90)                    	
                    .translateX(-0.5*size)
                    .translateY(-0.5*size)
                    .translateZ(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Y_AXIS)
                    .rotate(90)                    	
                    .translateX(-0.5*size)
                    .translateY( 0.5*size)
                    .translateZ(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Y_AXIS)
                    .rotate(90)                    	
                    .translateX( 0.5*size)
                    .translateY(-0.5*size)
                    .translateZ(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Y_AXIS)
                    .rotate(90)                    	
                    .translateX( 0.5*size)
                    .translateY( 0.5*size)
                    .translateZ(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Z_AXIS)
                    .rotate(90)                    	
                    .translateX(-0.5*size)
                    .translateZ(-0.5*size)
                    .translateY(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Z_AXIS)
                    .rotate(90)                    	
                    .translateX(-0.5*size)
                    .translateZ( 0.5*size)
                    .translateY(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Z_AXIS)
                    .rotate(90)                    	
                    .translateX( 0.5*size)
                    .translateZ(-0.5*size)
                    .translateY(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build(),
                LineBuilder.create() // 
                    .startX(-0.5*size)
                    .startY(0)
                    .endX(0.5*size)
                    .endY(0)
                    .rotationAxis(Rotate.Z_AXIS)
                    .rotate(90)                    	
                    .translateX( 0.5*size)
                    .translateZ( 0.5*size)
                    .translateY(0)  
                    .stroke(color)
                    .strokeWidth(0.2)
                    .strokeLineCap(StrokeLineCap.BUTT)
                    .build()                                                                                                                                                               
            );
            //for (Node n: getChildren()){
             //System.out.println("node=" + ((Line)n));
             //System.out.println("node bounds=" + ((Line)n).getBoundsInLocal());
             //System.out.println("node bounds=" + ((Line)n).getBoundsInParent());
             //System.out.println("node points=" + ((Line)n).getStartX() + " " + ((Line)n).getStartY() + " " + ((Line)n).getEndX() + " " + ((Line)n).getEndY());
            //}
        }
    }
