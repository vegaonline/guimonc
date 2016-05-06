// JavaFX cube
package moncgui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;

    public class cubeTest extends Group {

		Rectangle top;
		Rectangle bottom;
		Rectangle left;
		Rectangle right;
		Rectangle front;
		Rectangle back;

        public cubeTest(double size, Color color, double shade) {

                back = RectangleBuilder.create() // back face
                    .width(size).height(size)
                    .fill(color.deriveColor(0.0, 1.0, (1 - 0.5*shade), 1.0))
                    .translateX(-0.5*size)
                    .translateY(-0.5*size)
                    .translateZ(0.5*size)
                    .smooth(true)
                    .build();
                bottom = RectangleBuilder.create() // bottom face
                    .width(size).height(size)
                    .fill(color.deriveColor(0.0, 1.0, (1 - 0.4*shade), 1.0))
                    .translateX(-0.5*size)
                    .translateY(0)
                    .rotationAxis(Rotate.X_AXIS)
                    .rotate(90)
                    .smooth(true)
                    .build();
                right = RectangleBuilder.create() // right face
                    .width(size).height(size)
                    .fill(color.deriveColor(0.0, 1.0, (1 - 0.3*shade), 1.0))
                    .translateX(-1*size)
                    .translateY(-0.5*size)
                    .rotationAxis(Rotate.Y_AXIS)
                    .rotate(90)
                    .smooth(true)
                    .build();
                left = RectangleBuilder.create() // left face
                    .width(size).height(size)
                    .fill(color.deriveColor(0.0, 1.0, (1 - 0.2*shade), 1.0))
                    .translateX(0)
                    .translateY(-0.5*size)
                    .rotationAxis(Rotate.Y_AXIS)
                    .rotate(90)
                    .smooth(true)
                    .build();
                top = RectangleBuilder.create() // top face
                    .width(size).height(size)
                    .fill(color.deriveColor(0.0, 1.0, (1 - 0.1*shade), 1.0))
                    .translateX(-0.5*size)
                    .translateY(-1*size)
                    .rotationAxis(Rotate.X_AXIS)
                    .rotate(90)
                    .smooth(true)
                    .build();
                front = RectangleBuilder.create() // front face
                    .width(size).height(size)
                    .fill(color)
                    .translateX(-0.5*size)
                    .translateY(-0.5*size)
                    .translateZ(-0.5*size)
                    .smooth(true)
                    .build();
            
            getChildren().addAll(top, bottom, left, right, front, back );
        }
        public void setBottomColor(Color c){
        	getChildren().remove(bottom);
        	bottom.setFill(c);
        	getChildren().add(bottom);
        }
        public void setTopColor(Color c){
        	getChildren().remove(top);
        	top.setFill(c);
        	getChildren().add(top);
        }
        public void setFrontColor(Color c){
        	getChildren().remove(front);
        	front.setFill(c);
        	getChildren().add(front);
        }
        public void setLeftColor(Color c){
        	getChildren().remove(left);
        	left.setFill(c);
        	getChildren().add(left);
        }
        public void setRightColor(Color c){
        	getChildren().remove(right);
        	right.setFill(c);
        	getChildren().add(right);
        }
        public void setBackColor(Color c){
        	getChildren().remove(back);
        	back.setFill(c);
        	getChildren().add(back);
        }
    }
