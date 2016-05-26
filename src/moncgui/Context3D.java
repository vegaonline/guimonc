// Store globals
package moncgui;

/**
 *
 * @author vega
 */
public final class Context3D {

    protected boolean showNormals = false;
    protected boolean showVertexNormals = false;
    protected boolean showLights = false;
    protected boolean showBorders = false;
    protected boolean showWorking = false;
    protected boolean showTexts = false;
    protected Lighting3D lighting;
    protected static CameraView cameraView;

    private static final Context3D INSTANCE = new Context3D ();

    public static Context3D getInstance(CameraView cameraView) {
        Mesh.context = INSTANCE;
        Context3D.cameraView = cameraView;
        return INSTANCE;
    }

    public Context3D() {
    }

    public void showAxes() {
        Axis3D axis = new Axis3D (50.0);
        cameraView.add (axis);
    }

    public void setShowLight(boolean b) {
        showLights = b;
    }

    public void setShowNormals(boolean b) {
        showNormals = b;
    }

    public void setShowVertexNormals(boolean b) {
        showVertexNormals = b;
    }

    public void setShowBorders(boolean b) {
        showBorders = b;
    }

    public void setShowWorking(boolean b) {
        showWorking = b;
    }

    public void setShowTexts(boolean b) {
        showTexts = b;
    }

    public void showLights() {
        lighting.showLights (cameraView);
    }

}
