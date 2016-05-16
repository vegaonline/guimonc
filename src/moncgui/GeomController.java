/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @author vega
 */
package moncgui;

import java.net.URL;
import java.util.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import moncgui.Material;
import moncgui.Sphere;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class GeomController implements Initializable {

    private int initialized = 0;
    private MoncGUI myGUI;
    private Scene geoScene;
    private Stage geoStage;
    private CameraView camV = new CameraView ();

    private Stage newStage = new Stage ();
    private Scene newScene;
    private Axis3D axis;

    private final ScrollBar scB = new ScrollBar ();
    private final SplitPane paramPane1 = new SplitPane ();
    private final SplitPane drawPane = new SplitPane ();
    private final GridPane paramPane = new GridPane ();

    private double cx = 0.0, cy = 0.0, cz = 0.0;
    private double xoffset = 20.0, yoffset = 20.0;
    private double geoWidth = 0.0, geoHeight = 0.0;
    private double menuWidth;
    private double menuHeight;
    private double drawWidth;
    private double drawHeight;
    private double centerX;
    private double centerY;
    private double centerZ;
    private int radSample, lenSample;
    private double radScale = 3.0;
    private double lenScale = 1.1;

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
    private Label radIT = new Label ("Inner Rad ");
    private Label radOT = new Label ("Outer Rad ");
    private Label Theta0T = new Label ("Theta_0 (deg) ");
    private Label Theta1T = new Label ("Theta_1 (deg) ");
    private Label Phi0T = new Label ("Phi_0 (deg) ");
    private Label Phi1T = new Label ("Phi_1 (deg) ");
    private TextField theta0 = new TextField ();
    private TextField theta1 = new TextField ();
    private TextField phi0 = new TextField ();
    private TextField phi1 = new TextField ();
    private TextField radI = new TextField ();
    private TextField radO = new TextField ();
    private Label heightT = new Label ("Height");
    private TextField ht = new TextField ();
    private Label lenT = new Label ("Length");
    private Label widT = new Label ("Width");
    private Label depT = new Label ("Depth");
    private TextField lenVal = new TextField ();
    private TextField widVal = new TextField ();
    private TextField depVal = new TextField ();
    private Label objAxisT = new Label ("Axis Along X, Y Z");
    private TextField objAxis = new TextField ();
    private String axisX = "[xX]";
    private String axisY = "[yY]";
    private String axisZ = "[zZ]";
    private Label matT = new Label ("Material");
    private ComboBox<String> matList = new ComboBox<String> ();
    private Button drawMe = new Button ("Draw");
    private TextArea geoEntries = new TextArea ();
    private String geoTextEntry = null;

    @FXML
    private Button shapeEllip;
    @FXML
    private Button shapeCYL;
    @FXML
    private Button shapeUpd;
    @FXML
    private BorderPane geoMainArea;
    @FXML
    private Button shapeRST;
    @FXML
    private Button geomClear;
    @FXML
    private Button shapeBricks;
    @FXML
    private MenuButton shapeRepli;
    @FXML
    private MenuItem shapeCopy;
    @FXML
    private MenuItem shapeMirror;

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = myGUI;
    }

    public void setMyStage(Stage stage) {
        this.geoStage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildScene ();
    }

    private Scene setMyScene(double wid, double ht) {
        Scene sc = new Scene (camV, wid, ht, true);
        sc.setFill (new RadialGradient (230, 0.85, centerX, centerY,
                wid, false,
                CycleMethod.NO_CYCLE, new Stop[]{
                    new Stop (0f, Color.DARKBLUE),
                    new Stop (1f, Color.CADETBLUE)}));   // AQUAMARINE
        return sc;
    }

    private void buildScene() {
        geoWidth = 600;
        geoHeight = 400;
        menuWidth = 200.0;
        menuHeight = geoHeight - yoffset;
        drawWidth = geoWidth - menuWidth - xoffset;
        drawHeight = menuHeight;
        centerX = 0.5 * drawWidth;// - xoffset;
        centerY = 0.5 * drawHeight;// + yoffset;
        centerZ = 0.3;
        paramPane1.setPrefSize (menuWidth, menuHeight);
        paramPane1.setOrientation (Orientation.VERTICAL);
        paramPane.setGridLinesVisible (false);
        paramPane.setPrefSize (menuWidth, menuHeight);
        paramPane.setHgap (1);
        paramPane.setVgap (3);
        paramPane.setPadding (new Insets (0, 1, 0, 1));
        paramPane1.getItems ().add (paramPane);

        drawPane.setPrefSize (drawWidth, drawHeight);
        drawPane.getItems ().add (geoEntries);

        geoMainArea.setLeft (paramPane);
        geoMainArea.setRight (drawPane);

        // redefined for plot window
        drawWidth = 850.0;
        drawHeight = 750.0;
    }

    private void buildCamera(Scene scene) {
        scene.setCamera (new PerspectiveCamera ());
    }

    private void lightSetting(Context3D context) {
        context.lighting = new Lighting3D ();
        context.lighting.add (Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 1.5, new Vector3D (1, 0.8, 0.6));
        context.lighting.add (Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 1.0, new Vector3D (-1, -0.8, 0.6));
        context.lighting.add (Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 0.5, new Vector3D (0, -0.2, -0.8));
        // context.showLights    //-----> These generates yellow lights seem to be axes
        // context.setShowBorders(true);  
        // context.setShowTexts(true);  // vertex texts are shown with drawing. Don't use 
    }

    private double chkNull(double a) {
        if ( a == 0.0 ) {
            a = 1.0e-8;
        }
        return a;
    }

    private float chkNull(float a) {
        if ( a == 0.0 ) {
            a = 0.0000001f;
        }
        return a;
    }

    private Axis3D buildAxes() {
        axis = new Axis3D (60.0, Color.AQUAMARINE);
        return axis;
    }

    public void drawCyl() {
        BaseCoord.setFont (new Font ("Times New Roman", 11));
        radIT.setFont (new Font ("Times New Roman", 11));
        radOT.setFont (new Font ("Times New Roman", 11));
        heightT.setFont (new Font ("Times New Roman", 11));
        matT.setFont (new Font ("Times New Roman", 11));
        objAxisT.setFont (new Font ("Times New Roman", 9));

        baseCX.setPrefColumnCount (6);
        baseCX.setAlignment (Pos.CENTER_RIGHT);
        baseCY.setPrefColumnCount (6);
        baseCY.setAlignment (Pos.CENTER_RIGHT);
        baseCZ.setPrefColumnCount (6);
        baseCZ.setAlignment (Pos.CENTER_RIGHT);
        radI.setPrefColumnCount (9);
        radO.setPrefColumnCount (9);
        radI.setAlignment (Pos.CENTER_RIGHT);
        radO.setAlignment (Pos.CENTER_RIGHT);
        radI.setMaxSize (60, 1); // width height
        radO.setMaxSize (60, 1); // width height
        ht.setPrefColumnCount (9);
        ht.setAlignment (Pos.CENTER_RIGHT);
        ht.setMaxSize (60, 1); // width height
        objAxis.setMaxSize (30, 1);

        HBox hb1 = new HBox (baseCX, baseCY, baseCZ);
        HBox hb2 = new HBox (radIT, radI);
        HBox hb3 = new HBox (radOT, radO);
        HBox hb4 = new HBox (heightT, ht);
        HBox hb5 = new HBox (objAxisT, objAxis);
        hb1.setSpacing (1); //hb1.setPadding(new Insets(2));

        hb2.setSpacing (1);
        hb3.setSpacing (1);
        hb4.setSpacing (1);
        hb5.setSpacing (1);
        VBox vb1 = new VBox (BaseCoord, hb1, hb2, hb3, hb4, hb5);

        baseCX.setPromptText ("0.0");
        baseCY.setPromptText ("0.0");
        baseCZ.setPromptText ("0.0");
        radI.setPromptText ("50.0");
        radO.setPromptText ("30.0");
        ht.setPromptText ("100.0");
        objAxis.setPromptText ("X");

        paramPane.add (vb1, 0, 0); // col row 
        //paramPane.add(matT, 0, 5);
        // paramPane.add(matList, 0, 6);
        paramPane.add (drawMe, 0, 9);

        drawMe.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent ev) {
                double oRad = Double.parseDouble (radO.getText ());
                double length = chkNull (Double.parseDouble (ht.getText ()));
                double iRad = Double.parseDouble (radI.getText ());
                if ( oRad == 0.0 ) {
                    resetGeom ();
                }
                if ( iRad != 0.0 ) {
                    radSample = (int) (radScale * Math.sqrt (iRad) + 0.5);
                } else {
                    radSample = 15;
                }
                lenSample = (int) (lenScale * Math.sqrt (length) + 0.5);

                if ( iRad != 0.0 ) {
                    tubeTest tub1 = new tubeTest ("Tube_1", oRad, iRad, length,
                            lenSample, radSample, Material.getShinyMaterial ().
                            putKd (0.8));
                    if ( objAxis.getText ().matches (axisX) ) {
                        tub1.setRotate (90.0);
                    } else if ( objAxis.getText ().matches (axisY) ) {
                    } else if ( objAxis.getText ().matches (axisZ) ) {
                        tub1.setRotationAxis (Rotate.X_AXIS);
                        tub1.setRotate (90.0);
                    }
                    double oX = Double.parseDouble (baseCX.getText ());
                    double oY = Double.parseDouble (baseCY.getText ());
                    double oZ = Double.parseDouble (baseCZ.getText ());

                    if ( oX != 0.0 ) {
                        tub1.setTranslateX (Double.parseDouble (baseCX.
                                getText ()));
                    }
                    if ( oY != 0.0 ) {
                        tub1.setTranslateY (Double.parseDouble (baseCY.
                                getText ()));
                    }
                    if ( oZ != 0.0 ) {
                        tub1.setTranslateZ (Double.parseDouble (baseCZ.
                                getText ()));
                    }
                    geoTextEntry = "TUBE " + "ORIGIN :: (" + baseCX.getText () +
                            ", " +
                            baseCY.getText () + ", " + baseCZ.getText () + ") " +
                            " Outer Rad " + oRad + " Inner Rad " + iRad +
                            "  Length " + length +
                            " Axis along   " + objAxis.getText () + "\n";
                    geoEntries.appendText (geoTextEntry);
                    paramPane.getChildren ().removeAll (vb1, radIT, radOT, radI,
                            radO, heightT, ht, objAxisT, objAxis, drawMe);
                    camV.add (tub1);
                } else {
                    cylTest tub1 = new cylTest ("Tube_1", lenSample, radSample,
                            oRad, length, true, false, Material.
                            getShinyMaterial ().putKd (0.8));
                    if ( objAxis.getText ().matches (axisX) ) {
                        tub1.setRotationAxis (Rotate.Y_AXIS);
                        tub1.setRotate (90);
                    } else if ( objAxis.getText ().matches (axisY) ) {
                        tub1.setRotationAxis (Rotate.X_AXIS);
                        tub1.setRotate (90);
                    } else if ( objAxis.getText ().matches (axisZ) ) {
                        tub1.setRotationAxis (Rotate.Z_AXIS);
                        tub1.setRotate (90);
                    }
                    double oX = Double.parseDouble (baseCX.getText ());
                    double oY = Double.parseDouble (baseCY.getText ());
                    double oZ = Double.parseDouble (baseCZ.getText ());

                    if ( oX != 0.0 ) {
                        tub1.setTranslateX (Double.parseDouble (baseCX.
                                getText ()));
                    }
                    if ( oY != 0.0 ) {
                        tub1.setTranslateY (Double.parseDouble (baseCY.
                                getText ()));
                    }
                    if ( oZ != 0.0 ) {
                        tub1.setTranslateZ (Double.parseDouble (baseCZ.
                                getText ()));
                    }

                    geoTextEntry = "TUBE " + "ORIGIN :: (" + baseCX.getText () +
                            ", " +
                            baseCY.getText () + ", " + baseCZ.getText () + ") " +
                            " Outer Rad " + oRad + " Inner Rad " + iRad +
                            "  Length " + length +
                            " Axis along   " + objAxis.getText () + "\n";
                    geoEntries.appendText (geoTextEntry);
                    paramPane.getChildren ().removeAll (vb1, radIT, radOT, radI,
                            radO, heightT, ht, objAxisT, objAxis, drawMe);
                    camV.add (tub1);
                }

            }
        }
        );
    }

    public void drawSPH() {
        BaseCoord.setFont (new Font ("Times New Roman", 9));
        radIT.setFont (new Font ("Times New Roman", 9));
        radOT.setFont (new Font ("Times New Roman", 9));
        radI.setFont (new Font ("Times New Roman", 9));
        radO.setFont (new Font ("Times New Roman", 9));
        Theta0T.setFont (new Font ("Times New Roman", 9));
        theta0.setFont (new Font ("Times New Roman", 9));
        Theta1T.setFont (new Font ("Times New Roman", 9));
        theta1.setFont (new Font ("Times New Roman", 9));
        Phi0T.setFont (new Font ("Times New Roman", 9));
        phi0.setFont (new Font ("Times New Roman", 9));
        Phi1T.setFont (new Font ("Times New Roman", 9));
        phi1.setFont (new Font ("Times New Roman", 9));
        matT.setFont (new Font ("Times New Roman", 9));
        objAxisT.setFont (new Font ("Times New Roman", 9));

        baseCX.setPrefColumnCount (5);
        baseCX.setAlignment (Pos.CENTER_RIGHT);
        baseCY.setPrefColumnCount (5);
        baseCY.setAlignment (Pos.CENTER_RIGHT);
        baseCZ.setPrefColumnCount (5);
        baseCZ.setAlignment (Pos.CENTER_RIGHT);

        radO.setPrefColumnCount (5);
        radO.setAlignment (Pos.CENTER_RIGHT);
        radO.setMaxSize (40, 1); // width height
        radI.setAlignment (Pos.CENTER_RIGHT);
        radI.setPrefColumnCount (5);
        radI.setMaxSize (40, 1); // width height

        theta0.setPrefColumnCount (5);
        theta0.setAlignment (Pos.CENTER_RIGHT);
        theta0.setMaxSize (40, 1); // width height
        theta1.setAlignment (Pos.CENTER_RIGHT);
        theta1.setPrefColumnCount (5);
        theta1.setMaxSize (40, 1); // width height

        phi0.setPrefColumnCount (5);
        phi0.setAlignment (Pos.CENTER_RIGHT);
        phi0.setMaxSize (40, 1); // width height
        phi1.setAlignment (Pos.CENTER_RIGHT);
        phi1.setPrefColumnCount (5);
        phi1.setMaxSize (40, 1); // width height

        objAxis.setMaxSize (30, 1);

        HBox hb1 = new HBox (baseCX, baseCY, baseCZ);
        HBox hb2 = new HBox (radIT, radI, radOT, radO);
        HBox hb3 = new HBox (Theta0T, theta0);
        HBox hb4 = new HBox (Theta1T, theta1);
        HBox hb5 = new HBox (Phi0T, phi0);
        HBox hb6 = new HBox (Phi1T, phi1);
        hb1.setSpacing (1); //hb1.setPadding(new Insets(2));
        hb2.setSpacing (2);
        hb3.setSpacing (2);
        hb4.setSpacing (2);
        hb5.setSpacing (2);
        hb6.setSpacing (2);

        VBox vb1 = new VBox (BaseCoord, hb1, hb2, hb3, hb4, hb5, hb6);

        baseCX.setPromptText ("0.0");
        baseCY.setPromptText ("0.0");
        baseCZ.setPromptText ("0.0");

        radI.setPromptText ("50.0");
        radO.setPromptText ("30.0");
        theta0.setPromptText ("0.0");
        theta1.setPromptText ("360.0");
        phi0.setPromptText ("0.0");
        phi1.setPromptText ("360.0");
        objAxis.setPromptText ("X");

        paramPane.add (vb1, 0, 0); // col row 
        paramPane.add (objAxisT, 0, 13);
        paramPane.add (objAxis, 0, 14);
        //paramPane.add(matT, 0, 5);
        // paramPane.add(matList, 0, 6);
        paramPane.add (drawMe, 0, 15);

        drawMe.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent ev) {
                System.out.println ("RAD_O=  " + radO.getText ());
                double oRad = Double.parseDouble (radO.getText ());
                double iRad = Double.parseDouble (radI.getText ());
                double tht0 = Double.parseDouble (theta0.getText ());
                double tht1 = Double.parseDouble (theta1.getText ());
                double fi0 = Double.parseDouble (phi0.getText ());
                double fi1 = Double.parseDouble (phi1.getText ());
                radSample = (int) (radScale * Math.sqrt (iRad) + 0.5);
                lenSample = 5;

                Sphere_Sect sph1 = new Sphere_Sect ("Spherical_1", lenSample,
                        radSample, iRad, oRad, tht0, tht1, fi0, fi1);

                if ( objAxis.getText ().matches (axisX) ) {
                    sph1.setRotate (90.0);
                }
                sph1.setTranslateX (Double.parseDouble (baseCX.getText ()));
                sph1.setTranslateY (Double.parseDouble (baseCY.getText ()));
                sph1.setTranslateZ (Double.parseDouble (baseCZ.getText ()));

                geoTextEntry = "Sphere " + "(" + baseCX.getText () + ", " +
                        baseCY.getText () + ", " + baseCZ.getText () + ") " +
                        " Outer Rad " + oRad + " Inner Rad   " + iRad +
                        " Theta 1 " + tht0 + " Theta 2  " + tht1 + " Phi 1   " +
                        fi0 + "  Phi 2  " + fi1 + " Axis along   " + objAxis.
                        getText () + "\n";
                geoEntries.appendText (geoTextEntry);
                paramPane.getChildren ().removeAll (vb1, radIT, radOT, radI,
                        radO, heightT, ht, objAxisT, objAxis, drawMe);
                camV.add (sph1);
            }
        });
    }

    void complete(Stage vegaStage, Scene vegaScene) {
        camV.frameCam (vegaStage, vegaScene);
        MouseHandler mouseHandler = new MouseHandler (vegaScene, camV);
        KeyHandler keyHandler = new KeyHandler (vegaStage, vegaScene, camV);
        vegaStage.setScene (vegaScene);
        vegaStage.show ();
    }

    @FXML
    void doShapeCyl(ActionEvent event) {
        if ( initialized == 0 ) {
            newStage = new Stage ();
            newStage.setTitle (
                    "Geometry Editor :: Abhijit Bhattacharyya EMAIL: vega@barc.gov.in");
            newStage.setResizable (true);
            geoScene = setMyScene (drawWidth, drawHeight);
            buildCamera (geoScene);
            Context3D context = Context3D.getInstance (camV);
            lightSetting (context);
            axis = buildAxes ();  // Use this but before that remove yellow axes
            camV.add (axis);
            initialized = 1;
        }
        drawCyl ();
        complete (newStage, geoScene);
    }

    @FXML
    private void doShapeCirc(ActionEvent event) {
        if ( initialized == 0 ) {
            newStage = new Stage ();
            newStage.setTitle (
                    "Geometry Editor :: Abhijit Bhattacharyya EMAIL: vega@barc.gov.in");
            newStage.setResizable (true);
            geoScene = setMyScene (drawWidth, drawHeight);
            buildCamera (geoScene);
            Context3D context = Context3D.getInstance (camV);
            lightSetting (context);
            axis = buildAxes ();
            camV.add (axis);
            initialized = 1;
        }
        drawSPH ();
        complete (newStage, geoScene);
    }

    private void resetGeom() {
        drawPane.getItems ().clear ();
        camV.resetCam ();
    }

    @FXML
    private void doDrawReset(MouseEvent event) {
        resetGeom ();
    }

    @FXML
    private void doCLS(ActionEvent event) {
    }

    @FXML
    private void doUpdate(ActionEvent event) {
    }

    @FXML
    private void doGeomReset(ActionEvent event) {
        drawPane.getItems ().clear ();
        camV.resetCam ();
    }

    private void drawBricks() {
        BaseCoord.setFont (new Font ("Times New Roman", 9));
        lenT.setFont (new Font ("Times New Roman", 9));
        depT.setFont (new Font ("Times New Roman", 9));
        widT.setFont (new Font ("Times New Roman", 9));
        objAxisT.setFont (new Font ("Times New Roman", 9));

        baseCX.setPrefColumnCount (5);
        baseCX.setAlignment (Pos.CENTER_RIGHT);
        baseCY.setPrefColumnCount (5);
        baseCY.setAlignment (Pos.CENTER_RIGHT);
        baseCZ.setPrefColumnCount (5);
        baseCZ.setAlignment (Pos.CENTER_RIGHT);

        lenVal.setPrefColumnCount (5);
        lenVal.setAlignment (Pos.CENTER_RIGHT);
        lenVal.setMaxSize (40, 1); // width height
        widVal.setPrefColumnCount (5);
        widVal.setAlignment (Pos.CENTER_RIGHT);
        widVal.setMaxSize (40, 1); // width height
        depVal.setPrefColumnCount (5);
        depVal.setAlignment (Pos.CENTER_RIGHT);
        depVal.setMaxSize (40, 1); // width height
        objAxis.setPrefColumnCount (5);
        objAxis.setAlignment (Pos.CENTER_RIGHT);
        objAxis.setMaxSize (40, 1); // width height

        HBox hb1 = new HBox (baseCX, baseCY, baseCZ);
        HBox hb2 = new HBox (lenT, lenVal);
        HBox hb3 = new HBox (widT, widVal);
        HBox hb4 = new HBox (depT, depVal);
        HBox hb5 = new HBox (objAxisT, objAxis);

        hb1.setSpacing (1); //hb1.setPadding(new Insets(2));
        hb2.setSpacing (2);
        hb3.setSpacing (2);
        hb4.setSpacing (2);

        VBox vb1 = new VBox (BaseCoord, hb1, hb2, hb3, hb4, hb5);

        baseCX.setPromptText ("0.0");
        baseCY.setPromptText ("0.0");
        baseCZ.setPromptText ("0.0");
        objAxis.setPromptText ("X");

        paramPane.add (vb1, 0, 0); // col row 

        //paramPane.add(matT, 0, 5);
        // paramPane.add(matList, 0, 6);
        paramPane.add (drawMe, 0, 15);

        drawMe.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent ev) {
                double lenV = Double.parseDouble (lenVal.getText ());
                double widV = Double.parseDouble (widVal.getText ());
                double depV = Double.parseDouble (depVal.getText ());
                Brick brk = new Brick ("Brick", lenV, widV, depV, Material.
                        getShinyMaterial ().putKd (0.57));
                if ( objAxis.getText ().matches (axisX) ) {
                    brk.setRotate (90.0);
                }
                brk.setTranslateX (Double.parseDouble (baseCX.getText ()));
                brk.setTranslateY (Double.parseDouble (baseCY.getText ()));
                brk.setTranslateZ (Double.parseDouble (baseCZ.getText ()));

                geoTextEntry = "Brick " + "(" + baseCX.getText () + ", " +
                        baseCY.getText () + ", " + baseCZ.getText () + ") " +
                        " Length " + lenV + " Width   " + widV +
                        " Depth " + depV + " Axis along   " + objAxis.
                        getText () + "\n";
                geoEntries.appendText (geoTextEntry);
                paramPane.getChildren ().removeAll (vb1, radIT, radOT, radI,
                        radO, heightT, ht, objAxisT, objAxis, drawMe);
                camV.add (brk);
            }
        });
    }

    @FXML
    private void doBrick(ActionEvent event) {
        if ( initialized == 0 ) {
            newStage = new Stage ();
            newStage.setTitle (
                    "Geometry Editor :: Abhijit Bhattacharyya EMAIL: vega@barc.gov.in");
            newStage.setResizable (true);
            geoScene = setMyScene (drawWidth, drawHeight);
            buildCamera (geoScene);
            Context3D context = Context3D.getInstance (camV);
            lightSetting (context);
            axis = buildAxes ();
            camV.add (axis);
            initialized = 1;
        }
        drawBricks ();
        complete (newStage, geoScene);
    }

    @FXML
    private void doCopy(ActionEvent event) {
        if ( initialized == 0 ) {
            newStage = new Stage ();
            newStage.setTitle (
                    "Geometry Editor :: Abhijit Bhattacharyya EMAIL: vega@barc.gov.in");
            newStage.setResizable (true);
            geoScene = setMyScene (drawWidth, drawHeight);
            buildCamera (geoScene);
            Context3D context = Context3D.getInstance (camV);
            lightSetting (context);
            axis = buildAxes ();
            camV.add (axis);
            initialized = 1;
        }
        System.out.println ("Copy operation selected");
    }

    @FXML
    private void doMirror(ActionEvent event) {
        if ( initialized == 0 ) {
            newStage = new Stage ();
            newStage.setTitle (
                    "Geometry Editor :: Abhijit Bhattacharyya EMAIL: vega@barc.gov.in");
            newStage.setResizable (true);
            geoScene = setMyScene (drawWidth, drawHeight);
            buildCamera (geoScene);
            Context3D context = Context3D.getInstance (camV);
            lightSetting (context);
            axis = buildAxes ();
            camV.add (axis);
            initialized = 1;
        }
        System.out.println ("Mirror operation selected");
    }
}
