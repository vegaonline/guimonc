/*
 * @(#)Cube.java
 *
 *
 * @vega
 * @version 1.00 2011/10/26
 */
package moncgui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

/**
 *
 * @author vega
 */
public class LineCube extends Group {

    public LineCube(double size, Color color, double shade) {

        getChildren ().addAll (
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .translateX (0)
                .translateY (0.5 * size)
                .translateZ (-0.5 * size)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .translateX (0)
                .translateY (-0.5 * size)
                .translateZ (-0.5 * size)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .translateX (0)
                .translateY (0.5 * size)
                .translateZ (0.5 * size)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .translateX (0)
                .translateY (-0.5 * size)
                .translateZ (0.5 * size)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Y_AXIS)
                .rotate (90)
                .translateX (-0.5 * size)
                .translateY (-0.5 * size)
                .translateZ (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Y_AXIS)
                .rotate (90)
                .translateX (-0.5 * size)
                .translateY (0.5 * size)
                .translateZ (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Y_AXIS)
                .rotate (90)
                .translateX (0.5 * size)
                .translateY (-0.5 * size)
                .translateZ (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Y_AXIS)
                .rotate (90)
                .translateX (0.5 * size)
                .translateY (0.5 * size)
                .translateZ (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Z_AXIS)
                .rotate (90)
                .translateX (-0.5 * size)
                .translateZ (-0.5 * size)
                .translateY (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Z_AXIS)
                .rotate (90)
                .translateX (-0.5 * size)
                .translateZ (0.5 * size)
                .translateY (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Z_AXIS)
                .rotate (90)
                .translateX (0.5 * size)
                .translateZ (-0.5 * size)
                .translateY (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build (),
                LineBuilder.create () // 
                .startX (-0.5 * size)
                .startY (0)
                .endX (0.5 * size)
                .endY (0)
                .rotationAxis (Rotate.Z_AXIS)
                .rotate (90)
                .translateX (0.5 * size)
                .translateZ (0.5 * size)
                .translateY (0)
                .stroke (color)
                .strokeWidth (0.2)
                .strokeLineCap (StrokeLineCap.BUTT)
                .build ()
        );
        //for (Node n: getChildren()){
        //System.out.println("node=" + ((Line)n));
        //System.out.println("node bounds=" + ((Line)n).getBoundsInLocal());
        //System.out.println("node bounds=" + ((Line)n).getBoundsInParent());
        //System.out.println("node points=" + ((Line)n).getStartX() + " " + ((Line)n).getStartY() + " " + ((Line)n).getEndX() + " " + ((Line)n).getEndY());
        //}
    }
}
