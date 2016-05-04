// Axes made of Rod3Ds
package moncgui;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.transform.*;

    public class Axis3D extends Group {

        public Axis3D(double size) {

			double half=0.5*size;

			Rod3D x = new Rod3D(size, size/50, Color.RED);
			x.getTransforms().add(new Translate(half, 0, 0));

			Rod3D y = new Rod3D(size, size/50, Color.GREEN);
			y.getTransforms().add(new Translate(0, half, 0));
			y.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));

			Rod3D z = new Rod3D(size, size/50, Color.BLUE);
			z.getTransforms().add(new Translate(0, 0, half));
			z.getTransforms().add(new Rotate(-90, Rotate.Y_AXIS));

            getChildren().addAll(z, y, x);

        }
        public Axis3D(double size, Color color) {

			double half=0.5*size;

			Rod3D x = new Rod3D(size, size/50, Color.RED);
			x.getTransforms().add(new Translate(half, 0, 0));

			Rod3D y = new Rod3D(size, size/50, Color.GREEN);
			y.getTransforms().add(new Translate(0, half, 0));
			y.getTransforms().add(new Rotate(90, Rotate.Z_AXIS));

			Rod3D z = new Rod3D(size, size/50, Color.BLUE);
			z.getTransforms().add(new Translate(0, 0, half));
			z.getTransforms().add(new Rotate(-90, Rotate.Y_AXIS));

            getChildren().addAll(z, y, x);

        }
    }
