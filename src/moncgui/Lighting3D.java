// 3D light sources (Light3D) and light holding class (Lighting3D)
package moncgui;

import java.util.*;
import javafx.scene.paint.Color;

/**
 *
 * @author vega
 */
public class Lighting3D {

    List<Light3D> diffuseLights = new ArrayList ();
    List<Light3D> specularLights = new ArrayList ();
    Light3D ambient;
    //Color ambientColor;

    public Lighting3D() {
        ambient = new Light3D (0.5);
    }

    public void add(Type type, Source source, Vector3D p) {
        Light3D l = new Light3D (source, 1.0, p);
        switch (type) {
            case DIFFUSE:
                diffuseLights.add (l);
                break;
            case SPECULAR:
                specularLights.add (l);
                break;
        }
    }

    public void add(Type type, Source source, double i, Vector3D p) {
        Light3D l = new Light3D (source, i, p);
        switch (type) {
            case DIFFUSE:
                diffuseLights.add (l);
                break;
            case SPECULAR:
                specularLights.add (l);
                break;
        }
    }

    public double getAmbientIntensity() {
        return ambient.intensity;
    }

    public void setAmbientIntensity(double intensity) {
        ambient.intensity = intensity;
    }

    public enum Type {
        DIFFUSE, SPECULAR;
    }

    public enum Source {
        POINT, PARALLEL;
    }

    public List<Light3D> getDiffuseLights() {
        return diffuseLights;
    }

    public List<Light3D> getSpecularLights() {
        return specularLights;
    }

    public void showLights(CameraView cameraView) {
        for ( Light3D l : diffuseLights ) {
            l.showLight (cameraView);
        }
        for ( Light3D l : specularLights ) {
            l.showLight (cameraView);
        }
    }

}

class Light3D {

    Vector3D p;
    //Color color;
    double intensity;
    Lighting3D.Source source;

    public Light3D(Lighting3D.Source s, Vector3D p) {
        this.source = s;
        this.p = p;
    }

    public Light3D(Lighting3D.Source s, double intensity, Vector3D p) {
        this.source = s;
        this.intensity = intensity;
        this.p = p;
    }

    public Light3D(double intensity) {
        this.intensity = intensity;
    }

    public double getIntensity() {
        return intensity;
    }

    public Vector3D getP() {
        return p;
    }

    public Lighting3D.Source getSource() {
        return source;
    }

    public void showLight(CameraView cameraView) {
        Rod3D rod = new Rod3D (new Vector3D (0, 0, 0), p.multiplyThis (100),
                Color.YELLOW, 2);
        cameraView.add (rod);
    }

    public String toString() {
        return "Light [p=" + p + " intensity=" + intensity + " source=" + source +
                "]";
    }
}
