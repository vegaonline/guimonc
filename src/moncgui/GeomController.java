/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.net.URL;
import java.util.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class GeomController implements Initializable {

    private MoncGUI myGUI;
    private Scene geoScene;
    private Stage geoStage;

    private Stage newStage;
    private Scene newScene;

    private TriangleMesh cylMesh;

    private final ScrollBar scB = new ScrollBar ();
    private final SplitPane paramPane1 = new SplitPane ();
    private final SplitPane drawPane = new SplitPane ();
    private final GridPane paramPane = new GridPane ();
    private final Group axisGroup = new Group ();
    private final Group rootGr = new Group ();

    double anchorX, anchorY, anchorAngle;
    private final double cameraDistance = -1000.0;

    private double cx = 0.0, cy = 0.0, cz = 0.0, len0 = 0.0, len1 = 0.0,
            len2 = 0.0;
    private double rad0 = 0.0, rad1 = 0.0, rad2 = 0.0, rad3 = 0.0;
    private double ang0 = 0.0, ang1 = 0.0, ang2 = 0.0, ang3 = 0.0;

    private double geoWidth = 0.0, geoHeight = 0.0;
    private final double xoffset = 5.0, yoffset = 5.0, zoffset = 0.0;
    private double menuWidth;
    private double menuHeight;
    private double drawWidth;
    private double drawHeight;
    private double centerX;
    private double centerY;
    private double centerZ;
    private double scaleX = 0.08, scaleY = 0.08, scaleZ = 0.08;
    private Rotate rotateX = new Rotate (0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate (0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate (0, Rotate.Z_AXIS);
    private int radSample = 10, lenSample = 3;

    private ArrayList<Sphere> sphList;
    private ArrayList<Cylinder> cylList;
    private ArrayList<Shape3D> myShapeList;
    private ArrayList<PhongMaterial> MaterialList
            = new ArrayList<PhongMaterial> ();
    private List<String> materialNames = new ArrayList<String> ();

    private Label BaseCoord = new Label ("Origin");
    private TextField baseCX = new TextField ();
    private TextField baseCY = new TextField ();
    private TextField baseCZ = new TextField ();
    private Label radT = new Label ("Radius");
    private TextField rad = new TextField ();
    private Label heightT = new Label ("Height");
    private TextField ht = new TextField ();
    private Label matT = new Label ("Material");
    private ComboBox<String> matList = new ComboBox<String> ();
    private Button drawMe = new Button ("Draw");

    @FXML
    private Button shapeCLS;
    @FXML
    private Button shapeLine;
    @FXML
    private Button shapeCirc;
    @FXML
    private Button shapeEllip;
    @FXML
    private Button shapeCYL;
    @FXML
    private Button shapeUpd;
    @FXML
    private BorderPane geoMainArea;

    public void setMainApp(MoncGUI myGUI) {
        this.myGUI = myGUI;
    }

    public void setMyScene(Scene geoScene) {
        //this.geoScene = geoScene;
        this.geoScene = geoStage.getScene ();
    }

    public void setMyStage(Stage geoStage) {
        this.geoStage = geoStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildScene ();
        setMaterial ();
        //buildAxes ();

        geoScene = new Scene (rootGr, geoWidth, geoHeight, true);
        //geoScene = new Scene (cameraView, geoWidth, geoHeight, true);

        geoScene.setFill (new RadialGradient (225, 0.85, centerX, centerY,
                drawWidth, false,
                CycleMethod.NO_CYCLE, new Stop[]{new Stop (0f, Color.BLUE),
                    new Stop (1f, Color.LIGHTBLUE)}));
        
        buildCamera (geoScene);
//        geoStage.setScene (geoScene);
        
    }

    private void buildScene() {
        geoWidth = 600;
        geoHeight = 400;
        menuWidth = 100.0;
        menuHeight = geoHeight - yoffset;
        drawWidth = geoWidth - menuWidth - xoffset;
        drawHeight = menuHeight;
        centerX = 0.5 * drawWidth - xoffset;
        centerY = 0.5 * drawHeight + yoffset;
        centerZ = 0.3;
        scaleX = 0.08;
        scaleY = 0.08;
        scaleZ = 0.08;
        paramPane1.setPrefSize (menuWidth, menuHeight);
        paramPane1.setOrientation (Orientation.VERTICAL);
        paramPane.setGridLinesVisible (false);
        paramPane.setPrefSize (menuWidth, menuHeight);
        paramPane.setHgap (1);
        paramPane.setVgap (3);
        paramPane.setPadding (new Insets (0, 1, 0, 1));
        paramPane1.getItems ().add (paramPane);

        drawPane.setPrefSize (drawWidth, drawHeight);

        geoMainArea.setLeft (paramPane);
        geoMainArea.setRight (drawPane);
        scB.setLayoutX (drawWidth - scB.getWidth ());
        scB.setMin (0);
        scB.setOrientation (Orientation.VERTICAL);
        scB.setPrefHeight (drawHeight - 10);
        scB.setMax (400);

        scB.valueProperty ().addListener (new ChangeListener<Number> () {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number new_val) {
                drawPane.setLayoutY (-new_val.doubleValue ());
            }
        });
        cylMesh = new TriangleMesh ();
    }

    private PerspectiveCamera buildCamera(Scene scene) {
        PerspectiveCamera cam = new PerspectiveCamera (true);
        cam.setNearClip (0.1);
        cam.setFarClip (10000.0);
        cam.setTranslateX (250);
        cam.setTranslateY (250);
        cam.setTranslateZ (-cameraDistance);
        scene.setCamera (cam);
        return cam;
    }

    private void buildAxes() {
        /*
         * axis = new Axis3D (50); axis.setTranslateX (20); axis.setTranslateY
         * (10); axis.setTranslateZ (10); cameraView.add (axis);
         * drawPane.getItems ().add (cameraView);
         */
    }

    private void buildAxesOld() {
        PhongMaterial XAxisMat = null;
        PhongMaterial YAxisMat = null;
        PhongMaterial ZAxisMat = null;
        String XMat = "redMaterial", YMat = "blueMaterial", ZMat
                = "greenMaterial";

        int queVal = 0, Xque = 0, Yque = 0, Zque = 0, net = 3;
        for ( String s : materialNames ) {
            if ( s == XMat ) {
                Xque = queVal;
                net--;
            } else if ( s == YMat ) {
                Yque = queVal;
                net--;
            } else if ( s == ZMat ) {
                Zque = queVal;
                net--;
            }
            if ( net == 0 ) {
                break;
            }
            queVal++;
        }
        XAxisMat = MaterialList.get (Xque);
        YAxisMat = MaterialList.get (Yque);
        ZAxisMat = MaterialList.get (Zque);

        //final Box xAxis = new Box (lineLength, lineHeight, lineDepth);
        //final Box yAxis = new Box (lineHeight, lineLength, lineDepth); // Box(1, 240, 1);
        //final Box zAxis = new Box (lineHeight, lineDepth, lineLength); // Box(1, 1, 240.0);

        // axisGroup.getChildren ().addAll (xAxis, yAxis, zAxis);
        //boolean addAll = worldForm.getChildren ().addAll (axisGroup);
        // drawPane.getItems ().addAll (xAxis, yAxis, zAxis);
    }

    private void setMaterial() {
        final PhongMaterial redMaterial = new PhongMaterial ();
        redMaterial.setDiffuseColor (Color.DARKRED);
        redMaterial.setSpecularColor (Color.RED);
        MaterialList.add (redMaterial);
        materialNames.add ("redMaterial");

        final PhongMaterial greenMaterial = new PhongMaterial ();
        greenMaterial.setDiffuseColor (Color.DARKGREEN);
        greenMaterial.setSpecularColor (Color.GREEN);
        MaterialList.add (greenMaterial);
        materialNames.add ("greenMaterial");

        final PhongMaterial blueMaterial = new PhongMaterial ();
        blueMaterial.setDiffuseColor (Color.DARKBLUE);
        blueMaterial.setSpecularColor (Color.BLUE);
        MaterialList.add (blueMaterial);
        materialNames.add ("blueMaterial");

        final PhongMaterial PE = new PhongMaterial ();
        PE.setDiffuseColor (Color.DARKRED);
        PE.setSpecularColor (Color.RED);
        MaterialList.add (PE);
        materialNames.add ("PE");

        final PhongMaterial Steel = new PhongMaterial ();
        Steel.setDiffuseColor (Color.WHITE);
        Steel.setSpecularColor (Color.LIGHTBLUE);
        MaterialList.add (Steel);
        materialNames.add ("Steel");

        final PhongMaterial Lead = new PhongMaterial ();
        Lead.setDiffuseColor (Color.DARKGREY);
        Lead.setSpecularColor (Color.GREY);
        MaterialList.add (Lead);
        materialNames.add ("Lead");

        matList.getItems ().setAll ("PE", "Steel", "Lead");
        matList.setValue ("PE");
    }

    public void drawCylSolid() {

        BaseCoord.setFont (new Font ("Times New Roman", 11));
        radT.setFont (new Font ("Times New Roman", 11));
        heightT.setFont (new Font ("Times New Roman", 11));
        matT.setFont (new Font ("Times New Roman", 11));

        baseCX.setPrefColumnCount (6);
        baseCX.setAlignment (Pos.CENTER_RIGHT);
        baseCY.setPrefColumnCount (6);
        baseCY.setAlignment (Pos.CENTER_RIGHT);
        baseCZ.setPrefColumnCount (6);
        baseCZ.setAlignment (Pos.CENTER_RIGHT);
        rad.setPrefColumnCount (9);
        rad.setAlignment (Pos.CENTER_RIGHT);
        rad.setMaxSize (60, 1); // width height
        ht.setPrefColumnCount (9);
        ht.setAlignment (Pos.CENTER_RIGHT);
        ht.setMaxSize (60, 1); // width height

        HBox hb1 = new HBox (baseCX, baseCY, baseCZ);
        hb1.setSpacing (1); //hb1.setPadding(new Insets(2));
        VBox vb1 = new VBox (BaseCoord, hb1);

        baseCX.setPromptText ("0.0");
        baseCY.setPromptText ("0.0");
        baseCZ.setPromptText ("0.0");
        rad.setPromptText ("50.0");
        ht.setPromptText ("100.0");

        paramPane.add (vb1, 0, 0); // col row
        paramPane.add (radT, 0, 1);
        paramPane.add (rad, 0, 2);
        paramPane.add (heightT, 0, 3);
        paramPane.add (ht, 0, 4);
        paramPane.add (matT, 0, 5);
        paramPane.add (matList, 0, 6);
        paramPane.add (drawMe, 0, 7);
        rad0 = 8.0;
        len0 = 50.0;
        radSample = 2 * (int) rad0;

        drawMe.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent ev) {                
                final MeshView cyl = new MeshView(new vegaCYL(
                        "CYL1",rad0, len0, radSample, lenSample));
                cyl.setCullFace (CullFace.NONE);
                PhongMaterial myMat = null;
                String chkMat;
                int queVal = 0;
                chkMat = matList.getValue ();
                for ( String s : materialNames ) {
                    if ( s == chkMat ) {
                        break;
                    }
                    ++queVal;
                }
                myMat = MaterialList.get (queVal);
                cx = Double.parseDouble (baseCX.getText ());
                cy = Double.parseDouble (baseCY.getText ());
                cz = Double.parseDouble (baseCZ.getText ());
                rad0 = Double.parseDouble (rad.getText ());
                len0 = Double.parseDouble (ht.getText ());
                radSample = 2 * (int) rad0;
                cyl.setMaterial (myMat);
                
                geoScene.setOnMousePressed (new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent event) {
                        anchorX = event.getSceneX ();
                        anchorY = event.getSceneY ();
                        anchorAngle = cyl.getRotate ();
                    }
                });
                
                geoScene.setOnMouseDragged (new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent event) {
                        cyl.setRotate (anchorAngle + anchorX - event.getSceneX ());
                    }
                });
                
                paramPane.getChildren ().removeAll (vb1, radT, rad, heightT, ht,
                        matT, matList, drawMe);
            }
        });
    }

    public void addMouseScrolling(Node node) {
        node.setOnScroll ((ScrollEvent event) -> {
            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY ();
            if ( deltaY < 0 ) {
                zoomFactor = 2.0 - zoomFactor;
            }
            node.setScaleX (node.getScaleX () * zoomFactor);
            node.setScaleY (node.getScaleY () * zoomFactor);
        });
    }

    @FXML
    void doShapeCyl(ActionEvent event) {
        drawCylSolid ();
    }

    public void drawCircSolid() {

    }

    @FXML
    private void doShapeCirc(ActionEvent event) {
        drawCircSolid ();
    }

    @FXML
    private void doDrawReset(MouseEvent event) {
        drawPane.getItems ().clear ();

    }
}
