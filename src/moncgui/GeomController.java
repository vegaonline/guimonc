/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @author vega
 */
package moncgui;

import java.io.*;
import static java.lang.Math.*;
import java.util.*;
import java.util.logging.*;
import javafx.collections.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.*;
import javax.imageio.ImageIO;
import moncgui.Material;
import moncgui.Mesh;
import moncgui.Sphere;

/**
 * FXML Controller class
 *
 * @author vega
 */
// public class GeomController implements Initializable extends Mesh {
public class GeomController extends Mesh {

    private double dTORad = Math.PI / 180.0;
    private double RTODeg = 180.0 / Math.PI;
    private int geoSnapCnt = 0;
    private int totObjectNum = 1000;
    private int initialized = 0;
    private int isDrawMePressed = 0;
    private int numGeom = 0;

    private MoncGUI myGUI;
    private Stage geoStage;
    private Scene geoScene;
    private CameraView camV = new CameraView ();
    private Object_Array_List oal1;
    private List<Object_Array_List> objLIST = new ArrayList<Object_Array_List> (
            totObjectNum);
    private int objCnt = 0;
    private Stage newStage = new Stage ();
    private Scene newScene;
    private Axis3D axis;
    private Group objGroup;

    private final ScrollBar scB = new ScrollBar ();
    private final SplitPane paramPane1 = new SplitPane ();
    private final SplitPane drawPane = new SplitPane ();
    private final GridPane paramPane = new GridPane ();
    private final GridPane objPane = new GridPane ();
    private final ToggleGroup inView = new ToggleGroup ();

    FileChooser fileChooser = new FileChooser ();

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
    private double radScale = 6.0;
    private double lenScale = 1.1;

    private ArrayList<Sphere> sphList;
    private ArrayList<Cylinder> cylList;
    private ArrayList<Shape3D> myShapeList;
    private ArrayList<PhongMaterial> MaterialList
            = new ArrayList<PhongMaterial> ();
    private List<String> materialNames = new ArrayList<String> ();
    private List<tubeTest> tubeList = new ArrayList<tubeTest> (totObjectNum);

    private Label inViewT = new Label ("View Inside ");
    private RadioButton inViewYes;
    private RadioButton inViewNo;
    private Label BaseCoord = new Label ("Origin (X, Y, Z) ");
    private TextField baseCX = new TextField ();
    private TextField baseCY = new TextField ();
    private TextField baseCZ = new TextField ();
    private Label radIT = new Label ("Inner Rad ");
    private Label radOT = new Label ("Outer Rad ");
    private Label Theta0T = new Label ("Theta_0 (deg) ");
    private Label Theta1T = new Label ("Theta_1 (deg) ");
    private Label ThtSPH0T = new Label ("Theta_0 (-90 : 0 deg) ");
    private Label ThtSPH1T = new Label ("Theta_1 (0 : 90 deg) ");
    private Label Phi0T = new Label ("Phi_0 (0 : 359 deg) ");
    private Label Phi1T = new Label ("Phi_1 (1 : 359 deg) ");
    private TextField theta0 = new TextField ();
    private TextField theta1 = new TextField ();
    private TextField phi0 = new TextField ();
    private TextField phi1 = new TextField ();
    private TextField radI = new TextField ();
    private TextField radO = new TextField ();
    private Label heightT = new Label ("Height ");
    private TextField ht = new TextField ();
    private Label lenT = new Label ("Length ");
    private Label widT = new Label ("Width ");
    private Label depT = new Label ("Depth ");
    private TextField lenVal = new TextField ();
    private TextField widVal = new TextField ();
    private TextField depVal = new TextField ();
    private Label objAxisT = new Label ("Axis Along X, Y Z ");
    private TextField objAxis = new TextField ();

    private Label willCopyT = new Label (" Copy Along ");
    private Label copyNumTX = new Label ("Number of copies along X ");
    private Label copyNumTY = new Label ("Number of copies along Y ");
    private Label copyNumTZ = new Label ("Number of copies along Z ");
    private Label gapT = new Label ("Gap Length ");
    private TextField gap = new TextField ();
    private TextField cpyX = new TextField ();
    private TextField cpyY = new TextField ();
    private TextField cpyZ = new TextField ();

    private String axisX = "[xX]";
    private String axisY = "[yY]";
    private String axisZ = "[zZ]";
    private Label matT = new Label ("Material ");
    private ComboBox<String> matList = new ComboBox<String> ();
    private Button drawMe = new Button ("Draw ");
    private TextArea geoEntries = new TextArea ();
    private TextArea matEntries = new TextArea ();
    // private TextArea nodeList = new TextArea ();
    private TextField nodeList = new TextField ();
    private String geoTextEntry = null;
    private double maxx = -999999.99, minx = -maxx;
    private double maxy = maxx, miny = minx;
    private double maxz = maxx, minz = minx;

    //private Scene geoScene = setMyScene(850, 750);
    // MouseHandler mouseHandler = new MouseHandler(geoScene, camV);
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

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = badGUI;
    }

    public void setMyStage(Stage stage) {
        this.geoStage = stage;
    }

    /**
     * Initializes the controller class.
     */
    //@Override
    // public void initialize(URL url, ResourceBundle rb) {
    public void initialize() {
        buildScene ();
        geoScene = setMyScene (drawWidth, drawHeight);
        MouseHandler mouseHandler = new MouseHandler (geoScene, camV);
        buildMaterial ();
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

        drawPane.getItems ().add (objPane);
        geoEntries.setMaxHeight (200);
        nodeList.setMaxHeight (150);
        VBox vbNodes = new VBox (geoEntries, nodeList);
        objPane.add (vbNodes, 0, 0);

        geoMainArea.setLeft (paramPane);
        geoMainArea.setRight (drawPane);

        geoMainArea.autosize ();
        objPane.autosize ();
        drawPane.autosize ();
        paramPane.autosize ();

        // redefined for plot window
        drawWidth = 850.0;
        drawHeight = 750.0;
        nodeList.setFont (new Font ("Times New Roman", 12));
        nodeList.setMaxSize (drawWidth, 200);
    }

    private void buildCamera(Scene scene) {
        scene.setCamera (new PerspectiveCamera ());
    }

    private void buildMaterial() {
        matList.getItems ().add ("Copper");
        matList.getItems ().add ("Rubber");
        matList.getItems ().add ("Brass");
        matList.getItems ().add ("Glass");
        matList.getItems ().add ("Plastic");
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
        context.setShowBorders (true);
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

    private void maxminFunc(final double xx, final double yy, final double zz) {
        maxx = max (maxx, xx);
        minx = min (miny, xx);
        maxy = max (maxy, yy);
        miny = min (miny, yy);
        maxz = max (maxz, zz);
        minz = min (minz, zz);
    }

    private void maxminInit() {
        maxx = -999999.99;
        minx = -maxx;
        maxy = maxx;
        miny = minx;
        maxz = maxx;
        minz = minx;
    }

    public void drawCyl() {
        BaseCoord.setFont (new Font ("Times New Roman", 10));
        radIT.setFont (new Font ("Times New Roman", 10));
        radOT.setFont (new Font ("Times New Roman", 10));
        Phi0T.setFont (new Font ("Times New Roman", 10));
        Phi1T.setFont (new Font ("Times New Roman", 10));
        heightT.setFont (new Font ("Times New Roman", 10));
        matT.setFont (new Font ("Times New Roman", 10));
        objAxisT.setFont (new Font ("Times New Roman", 10));
        willCopyT.setFont (new Font ("Times New Roman", 10));
        copyNumTX.setFont (new Font ("Times New Roman", 10));
        copyNumTY.setFont (new Font ("Times New Roman", 10));
        copyNumTZ.setFont (new Font ("Times New Roman", 10));
        gapT.setFont (new Font ("Times New Roman", 10));
        inViewT.setFont (new Font ("Times New Roman", 10));

        baseCX.setPrefColumnCount (5);
        baseCX.setAlignment (Pos.CENTER_RIGHT);
        baseCX.setMaxSize (40, 1);
        baseCY.setPrefColumnCount (5);
        baseCY.setAlignment (Pos.CENTER_RIGHT);
        baseCY.setMaxSize (40, 1);
        baseCZ.setPrefColumnCount (5);
        baseCZ.setAlignment (Pos.CENTER_RIGHT);
        baseCZ.setMaxSize (40, 1);
        radI.setPrefColumnCount (5);
        radO.setPrefColumnCount (5);
        radI.setAlignment (Pos.CENTER_RIGHT);
        radO.setAlignment (Pos.CENTER_RIGHT);
        radI.setMaxSize (40, 1); // width height
        radO.setMaxSize (40, 1); // width height
        phi0.setMaxSize (40, 1);
        phi1.setMaxSize (40, 1);
        ht.setPrefColumnCount (5);
        ht.setAlignment (Pos.CENTER_RIGHT);
        ht.setMaxSize (40, 1); // width height
        objAxis.setMaxSize (30, 1);
        cpyX.setMaxSize (40, 1);
        cpyY.setMaxSize (40, 1);
        cpyZ.setMaxSize (40, 1);
        gap.setMaxSize (40, 1);
        gap.setAlignment (Pos.CENTER_RIGHT);

        inViewYes = new RadioButton (" Yes ");
        inViewYes.setToggleGroup (inView);
        inViewNo = new RadioButton (" No ");
        inViewNo.setToggleGroup (inView);
        inViewNo.setSelected (true);
        inViewYes.setUserData ("true");
        inViewNo.setUserData ("false");

        HBox hb1 = new HBox (baseCX, baseCY, baseCZ);
        HBox hb2 = new HBox (radIT, radI);
        HBox hb3 = new HBox (radOT, radO);
        HBox hb4 = new HBox (heightT, ht);
        HBox hb5 = new HBox (Phi0T, phi0);
        HBox hb6 = new HBox (Phi1T, phi1);
        HBox hb7 = new HBox (objAxisT, objAxis);
        HBox hb8 = new HBox (copyNumTX, cpyX);
        HBox hb9 = new HBox (copyNumTY, cpyY);
        HBox hb10 = new HBox (copyNumTZ, cpyZ);
        HBox hb11 = new HBox (gapT, gap);
        HBox hb12 = new HBox (matT, matList);
        HBox hb13 = new HBox (inViewT, inViewYes, inViewNo);

        hb1.setSpacing (2); //hb1.setPadding(new Insets(2));
        hb2.setSpacing (2);
        hb3.setSpacing (2);
        hb4.setSpacing (2);
        hb5.setSpacing (2);
        hb6.setSpacing (2);
        hb7.setSpacing (2);
        hb8.setSpacing (2);
        hb9.setSpacing (2);
        hb10.setSpacing (2);
        hb11.setSpacing (2);
        hb12.setSpacing (2);
        hb13.setSpacing (2);

        VBox vb1 = new VBox (BaseCoord, hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8,
                hb9, hb10, hb11, hb12, hb13);

        baseCX.setPromptText ("0.0");
        baseCY.setPromptText ("0.0");
        baseCZ.setPromptText ("0.0");
        radI.setPromptText ("50.0");
        radO.setPromptText ("30.0");
        ht.setPromptText ("100.0");
        objAxis.setPromptText ("X");

        paramPane.getChildren ().clear ();

        paramPane.add (vb1, 0, 0); // col row                 
        paramPane.add (drawMe, 0, 9);

        ObservableList<tubeTest> tubeList = FXCollections.
                observableArrayList ();

        drawMe.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent ev) {
                paramPane.getChildren ().removeAll (vb1, radIT, radOT, radI,
                        radO, heightT, ht, objAxisT, objAxis, drawMe);

                double oRad, iRad, length, tht0, tht1, gapVal = 0.0, oX = 0.0, oY
                        = 0.0, oZ = 0.0;
                double newCX = 0.0, newCY = 0.0, newCZ = 0.0;
                int cpX = 0, cpY = 0, cpZ = 0;
                int xDir = 0, yDir = 0, zDir = 0;
                boolean naked = (inView.getSelectedToggle ().getUserData ().
                        toString ().contains ("true") ? true : false);

                oX = (!baseCX.getText ().isEmpty () ? Double.parseDouble (
                        baseCX.getText ()) : 0.0);
                oY = (!baseCY.getText ().isEmpty () ? Double.parseDouble (
                        baseCY.getText ()) : 0.0);
                oZ = (!baseCZ.getText ().isEmpty () ? Double.parseDouble (
                        baseCZ.getText ()) : 0.0);
                iRad = (!radI.getText ().isEmpty () ? Double.parseDouble (
                        radI.getText ()) : 0.0);
                oRad = (!radO.getText ().isEmpty () ? Double.parseDouble (
                        radO.getText ()) : 0.0);
                length = (!ht.getText ().isEmpty () ? chkNull (Double.
                        parseDouble (ht.getText ())) : 0.0);
                tht0 = (!phi0.getText ().isEmpty () ? Double.parseDouble (
                        phi0.getText ()) * Math.PI / 180.0 : 0.0);
                tht1 = (!phi1.getText ().isEmpty () ? Double.parseDouble (
                        phi1.getText ()) * Math.PI / 180.0 : 2.0 * Math.PI);
                cpX = (!cpyX.getText ().isEmpty () ? Integer.parseInt (cpyX.
                        getText ()) : 0);
                cpY = (!cpyY.getText ().isEmpty () ? Integer.parseInt (cpyY.
                        getText ()) : 0);
                cpZ = (!cpyZ.getText ().isEmpty () ? Integer.parseInt (cpyZ.
                        getText ()) : 0);
                gapVal = (!gap.getText ().isEmpty () ? Double.parseDouble (gap.
                        getText ()) : 0);
                xDir = (!objAxis.getText ().isEmpty () && objAxis.getText ().
                        matches (axisX) ? 1 : 0);
                yDir = (!objAxis.getText ().isEmpty () && objAxis.getText ().
                        matches (axisY) ? 1 : 0);
                zDir = (!objAxis.getText ().isEmpty () && objAxis.getText ().
                        matches (axisZ) ? 1 : 0);

                xDir = ((xDir == 0 && yDir == 0 && zDir == 0) ? 1 : xDir);
                if ( oRad == 0.0 ) {
                    popupMsg.infoBox (
                            "Outer radius cannot be zero : resetting..",
                            "Parameter Error"
                    );
                    resetGeom ();
                }

                radSample = (int) (radScale * Math.sqrt (oRad) + 0.5);
                lenSample = 2; // (int) (lenScale * Math.sqrt (length) + 0.5);    

                objGroup = new Group ();

                objCnt = 0;
                if ( matList.getValue ().contains ("Copper") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                newCX += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);                           
                            } else if ( yDir == 1 ) {
                                newCX += (2.0 * oRad + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                            } else if ( zDir == 1 ) {
                                newCX += (2.0 * oRad + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));

                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                            // objGroup.getChildren ().add (tubeList.get (ii));
                        }
                        nodeList.setText (" Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                newCY += (2.0 * oRad + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);                           
                            } else if ( yDir == 1 ) {
                                newCY += (length + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                            } else if ( zDir == 1 ) {
                                newCY += (2.0 * oRad + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }

                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                            // objGroup.getChildren ().add (tubeList.get (ii));
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {

                            if ( xDir == 1 ) {
                                newCZ += (2.0 * oRad + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);                           
                            } else if ( yDir == 1 ) {
                                newCZ += (2.0 * oRad + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                            } else if ( zDir == 1 ) {
                                newCZ += (length + gapVal);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Copper ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                            // objGroup.getChildren ().add (tubeList.get (ii));
                        }
                        nodeList.setText ("Toal " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    //camV.add (objGroup);
                } else if ( matList.getValue ().contains ("Rubber") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                newCX += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                            } else if ( zDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCY += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                            } else if ( zDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                                tubeList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                            } else if ( zDir == 1 ) {
                                newCZ += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Rubber ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                } else if ( matList.getValue ().contains ("Brass") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                newCX += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                            } else if ( zDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCY += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                            } else if ( zDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                            } else if ( zDir == 1 ) {
                                newCZ += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                } else if ( matList.getValue ().contains ("Glass") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                newCX += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Glass ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                            } else if ( zDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Brass ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Glass ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Glass ()));
                            } else if ( zDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Glass ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Glass ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Glass ()));
                            } else if ( zDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Glass ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                } else {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                newCX += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                            } else if ( zDir == 1 ) {
                                newCX += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCY += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                            } else if ( zDir == 1 ) {
                                newCY += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        newCX = oX;
                        newCY = oY;
                        newCY = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                                tubeList.get (ii).setRotate (90.0);   //  tub2.setRotate(90.0);
                            } else if ( yDir == 1 ) {
                                newCZ += (gapVal + 2.0 * oRad);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                            } else if ( zDir == 1 ) {
                                newCZ += (gapVal + length);
                                tubeList.add (
                                        new tubeTest ("Tube", newCX, newCY,
                                                newCZ, oRad, iRad, length, tht0,
                                                tht1,
                                                lenSample, radSample, naked,
                                                Material.
                                                Plastic ()));
                                tubeList.get (ii).
                                        setRotationAxis (Rotate.X_AXIS);
                                tubeList.get (ii).setRotate (90.0);
                            }
                            camV.add (tubeList.get (ii));
                            geoTextEntry = "TUBE" + "  (" + newCX + ",  " +
                                    newCY +
                                    ",  " + newCZ + ")  " + "  " + iRad + "  " +
                                    oRad + "  " + length + "  " + tht0 * RTODeg +
                                    "  " +
                                    tht1 * RTODeg + "  " + objAxis.getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText ("Total " + objCnt + "Tube added");
                        tubeList.clear ();
                    }
                }
                matEntries.appendText (matList.getValue () + "\n");

                numGeom++;
                paramPane.getChildren ().clear ();
            }
        }
        );
        tubeList.remove (0, tubeList.size ());
    }

    public void drawSPH() {
        BaseCoord.setFont (new Font ("Times New Roman", 10));
        radIT.setFont (new Font ("Times New Roman", 10));
        radOT.setFont (new Font ("Times New Roman", 10));
        radI.setFont (new Font ("Times New Roman", 10));
        radO.setFont (new Font ("Times New Roman", 10));
        ThtSPH0T.setFont (new Font ("Times New Roman", 10));
        theta0.setFont (new Font ("Times New Roman", 10));
        ThtSPH1T.setFont (new Font ("Times New Roman", 10));
        theta1.setFont (new Font ("Times New Roman", 10));
        Phi0T.setFont (new Font ("Times New Roman", 10));
        phi0.setFont (new Font ("Times New Roman", 10));
        Phi1T.setFont (new Font ("Times New Roman", 10));
        phi1.setFont (new Font ("Times New Roman", 10));
        matT.setFont (new Font ("Times New Roman", 10));
        objAxisT.setFont (new Font ("Times New Roman", 10));
        copyNumTX.setFont (new Font ("Times New Roman", 10));
        copyNumTY.setFont (new Font ("Times New Roman", 10));
        copyNumTZ.setFont (new Font ("Times New Roman", 10));
        gapT.setFont (new Font ("Times New Roman", 10));

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

        cpyX.setMaxSize (40, 1);
        cpyY.setMaxSize (40, 1);
        cpyZ.setMaxSize (40, 1);
        gap.setMaxSize (40, 1);
        gap.setAlignment (Pos.CENTER_RIGHT);

        HBox hb1 = new HBox (baseCX, baseCY, baseCZ);
        HBox hb2 = new HBox (radIT, radI, radOT, radO);
        HBox hb3 = new HBox (radOT, radO);
        HBox hb4 = new HBox (ThtSPH0T, theta0);
        HBox hb5 = new HBox (ThtSPH1T, theta1);
        HBox hb6 = new HBox (Phi0T, phi0);
        HBox hb7 = new HBox (Phi1T, phi1);
        HBox hb8 = new HBox (objAxisT, objAxis);
        HBox hb9 = new HBox (matT, matList);
        HBox hb10 = new HBox (copyNumTX, cpyX);
        HBox hb11 = new HBox (copyNumTY, cpyY);
        HBox hb12 = new HBox (copyNumTZ, cpyZ);
        HBox hb13 = new HBox (gapT, gap);

        hb1.setSpacing (2); //hb1.setPadding(new Insets(2));
        hb2.setSpacing (2);
        hb3.setSpacing (2);
        hb4.setSpacing (2);
        hb5.setSpacing (2);
        hb6.setSpacing (2);
        hb7.setSpacing (2);
        hb8.setSpacing (2);
        hb9.setSpacing (2);
        hb10.setSpacing (2);
        hb11.setSpacing (2);
        hb12.setSpacing (2);
        hb13.setSpacing (2);

        // VBox vb1 = new VBox (BaseCoord, hb1, hb2, hb3, hb4, hb5, hb6);
        VBox vb1 = new VBox (BaseCoord, hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8,
                hb9, hb10, hb11, hb12, hb13);

        baseCX.setPromptText ("0.0");
        baseCY.setPromptText ("0.0");
        baseCZ.setPromptText ("0.0");
        radI.setPromptText ("0.0");
        radO.setPromptText ("5.0");
        theta0.setPromptText ("0.0");
        theta1.setPromptText ("360.0");
        phi0.setPromptText ("0.0");
        phi1.setPromptText ("360.0");
        objAxis.setPromptText ("X");

        paramPane.getChildren ().clear ();
        paramPane.add (vb1, 0, 0); // col row 

        paramPane.add (drawMe, 0, 3);  //15

        ObservableList<Sphere_SECT> sphList = FXCollections.
                observableArrayList ();
        drawMe.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent ev) {
                paramPane.getChildren ().removeAll (vb1, radIT, radOT, radI,
                        radO, heightT, ht, objAxisT, objAxis, drawMe);

                double iRad = 0.0, oRad = 0.0, tht0 = 0.0, tht1 = 0.0,
                        fi0 = 0.0, fi1 = 0.0, gapVal = 0.0, oX = 0.0,
                        oY = 0.0, oZ = 0.0, centerX = 0.0, centerY = 0.0,
                        centerZ = 0.0;
                int xDir = 0, yDir = 0, zDir = 0;
                int cpX = 0, cpY = 0, cpZ = 0;

                iRad = (!radI.getText ().isEmpty () ? Double.
                        parseDouble (radI.getText ()) : 0.0);
                oRad = (!radO.getText ().isEmpty () ? Double.
                        parseDouble (radO.getText ()) : 0.0);
                tht0 = (!theta0.getText ().isEmpty () ? Double.
                        parseDouble (theta0.getText ()) * dTORad : -0.5 *
                        Math.PI);
                tht1 = (!theta1.getText ().isEmpty () ? Double.
                        parseDouble (theta1.getText ()) * dTORad : 0.5 * Math.PI);
                fi0 = (!phi0.getText ().isEmpty () ? Double.parseDouble (
                        phi0.getText ()) * dTORad : 0.0);
                fi1 = (!phi1.getText ().isEmpty () ? Double.parseDouble (
                        phi1.getText ()) * dTORad : 2.0 * Math.PI);

                oX = (!baseCX.getText ().isEmpty () ? Double.
                        parseDouble (baseCX.getText ()) : 0.0);
                oY = (!baseCY.getText ().isEmpty () ? Double.
                        parseDouble (baseCY.getText ()) : 0.0);
                oZ = (!baseCZ.getText ().isEmpty () ? Double.
                        parseDouble (baseCZ.getText ()) : 0.0);
                gapVal = (!gap.getText ().isEmpty () ? Double.parseDouble (gap.
                        getText ()) : 0.0);
                cpX = (!cpyX.getText ().isEmpty () ? Integer.parseInt (cpyX.
                        getText ()) : 0);
                cpY = (!cpyY.getText ().isEmpty () ? Integer.parseInt (cpyY.
                        getText ()) : 0);
                cpZ = (!cpyZ.getText ().isEmpty () ? Integer.parseInt (cpyZ.
                        getText ()) : 0);
                xDir = (!objAxis.getText ().isEmpty () && objAxis.getText ().
                        matches (axisX) ? 1 : 0);
                yDir = (!objAxis.getText ().isEmpty () && objAxis.getText ().
                        matches (axisY) ? 1 : 0);
                zDir = (!objAxis.getText ().isEmpty () && objAxis.getText ().
                        matches (axisZ) ? 1 : 0);

                xDir = ((xDir == 0 && yDir == 0 && zDir == 0) ? 1 : xDir);
                if ( oRad == 0.0 ) {
                    popupMsg.infoBox (
                            "Outer radius cannot be zero : resetting..",
                            "Parameter Error"
                    );
                    resetGeom ();
                }

                radSample = (int) (radScale * Math.sqrt (oRad) + 0.5);
                lenSample = 15;

                centerX = cpX;
                centerY = cpY;
                centerZ = cpZ;

                objCnt = 0;

                if ( matList.getValue ().contains ("Copper") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.
                                        Copper ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                } else if ( matList.getValue ().contains ("Rubber") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Rubber ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                } else if ( matList.getValue ().contains ("Brass") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Brass ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }

                } else if ( matList.getValue ().contains ("Glass") ) {
                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                            } else if ( zDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Glass ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                } else {

                    if ( cpX != 0 ) {
                        objCnt += cpX;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpX; ii++ ) {
                            if ( xDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerX += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpY != 0 ) {
                        objCnt += cpY;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpY; ii++ ) {
                            if ( xDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerY += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                    if ( cpZ != 0 ) {
                        objCnt += cpZ;
                        centerX = oX;
                        centerY = oY;
                        centerZ = oZ;
                        for ( int ii = 0; ii < cpZ; ii++ ) {
                            if ( xDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                                sphList.get (ii).setRotationAxis (Rotate.Y_AXIS);
                                sphList.get (ii).setRotate (90.0);
                            } else if ( yDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                                sphList.get (ii).setRotationAxis (Rotate.X_AXIS);
                                sphList.get (ii).setRotate (90);
                            } else if ( zDir == 1 ) {
                                centerZ += gapVal + oRad;
                                sphList.add (new Sphere_SECT ("Sphere", centerX,
                                        centerY, centerZ, iRad, oRad, tht0, tht1,
                                        fi0, fi1, radSample, lenSample,
                                        Material.Plastic ()));
                            }
                            camV.add (sphList.get (ii));
                            geoTextEntry = "Sphere" + "  (" + centerX + ",  " +
                                    centerY + ",  " + centerZ + ")  " + "  " +
                                    iRad + "  " + oRad + "  " + tht0 * RTODeg +
                                    "  " + tht1 * RTODeg + "  " + fi0 * RTODeg +
                                    "  " + fi1 * RTODeg + "  " + objAxis.
                                    getText () +
                                    "\n";
                            geoEntries.appendText (geoTextEntry);
                        }
                        nodeList.setText (" Total " + objCnt + "Sphere added");
                        sphList.clear ();
                    }
                }

                matEntries.appendText (matList.getValue () + "\n");
                numGeom++;
                paramPane.getChildren ().clear ();
            }
        });
        sphList.remove (0, sphList.size ());
    }

    void complete(Stage vegaStage, Scene vegaScene) {

        camV.frameCam (vegaStage, vegaScene);
        //  MouseHandler mouseHandler = new MouseHandler(vegaScene, camV);
        KeyHandler keyHandler = new KeyHandler (vegaStage, vegaScene,
                camV);
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
            // geoScene = setMyScene(drawWidth, drawHeight);
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
            // geoScene = setMyScene(drawWidth, drawHeight);
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
        // It updates geometry specs in configurator and makes a snapshot of geometry
        String fName = null;
        String dirName = "Images";
        File recordsDir = new File (dirName);
        if ( !recordsDir.exists () ) {
            recordsDir.mkdirs ();
        }

        WritableImage fImage = camV.snapshot (new SnapshotParameters (),
                null);

        //*****  Updating config box ******
        String txtList1 = matEntries.getText ();
        String txtList2 = geoEntries.getText ();
        myGUI.setTxt (txtList1, txtList2);
        //****** Updating config box ends ******

        geoSnapCnt++;
        fName = recordsDir + "/" + "geometrySnap" + geoSnapCnt + ".png";

        File iFile = new File (fName);
        try {
            ImageIO.
                    write (SwingFXUtils.fromFXImage (fImage, null),
                            "png", iFile);
        } catch (IOException ex) {
            popupMsg.infoBox (
                    "Problem in saving the Geometry snapshot..",
                    "Geometry saving ERROR");
            Logger
                    .getLogger (GeomController.class
                            .getName ()).
                    log (Level.SEVERE, null, ex);
        }
        nodeList.setText (
                "Config Updated and Geometry snapshot stored in PNG file.");
    }

    @FXML
    private void doGeomReset(ActionEvent event) {
        drawPane.getItems ().clear ();
        camV.resetCam ();
    }

    private void drawBricks() {
        BaseCoord.setFont (new Font ("Times New Roman", 10));
        lenT.setFont (new Font ("Times New Roman", 10));
        depT.setFont (new Font ("Times New Roman", 10));
        widT.setFont (new Font ("Times New Roman", 10));
        objAxisT.setFont (new Font ("Times New Roman", 10));
        copyNumTX.setFont (new Font ("Times New Roman", 10));
        copyNumTY.setFont (new Font ("Times New Roman", 10));
        copyNumTZ.setFont (new Font ("Times New Roman", 10));
        gapT.setFont (new Font ("Times New Roman", 10));

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
        cpyX.setMaxSize (40, 1);
        cpyY.setMaxSize (40, 1);
        cpyZ.setMaxSize (40, 1);
        gap.setMaxSize (40, 1);
        gap.setAlignment (Pos.CENTER_RIGHT);

        HBox hb1 = new HBox (baseCX, baseCY, baseCZ);
        HBox hb2 = new HBox (lenT, lenVal);
        HBox hb3 = new HBox (widT, widVal);
        HBox hb4 = new HBox (depT, depVal);
        HBox hb5 = new HBox (objAxisT, objAxis);
        HBox hb6 = new HBox (copyNumTX, cpyX);
        HBox hb7 = new HBox (copyNumTY, cpyY);
        HBox hb8 = new HBox (copyNumTZ, cpyZ);
        HBox hb9 = new HBox (gapT, gap);
        HBox hb10 = new HBox (matT, matList);

        hb1.setSpacing (1); //hb1.setPadding(new Insets(2));
        hb2.setSpacing (2);
        hb3.setSpacing (2);
        hb4.setSpacing (2);
        hb5.setSpacing (2);
        hb6.setSpacing (2);
        hb7.setSpacing (2);
        hb8.setSpacing (2);
        hb9.setSpacing (2);
        hb10.setSpacing (2);

        VBox vb1 = new VBox (BaseCoord, hb1, hb2, hb3, hb4, hb5, hb6,
                hb7, hb8,hb9, hb10);

        baseCX.setPromptText ("0.0");
        baseCY.setPromptText ("0.0");
        baseCZ.setPromptText ("0.0");
        objAxis.setPromptText ("X");

        paramPane.getChildren ().clear ();
        paramPane.add (vb1, 0, 0); // col row 

        paramPane.add (drawMe, 0, 15);
        ObservableList<Brick> brkList = FXCollections.
                observableArrayList ();

        drawMe.setOnAction (new EventHandler<ActionEvent> () {
                    @Override
                    public void handle(ActionEvent ev) {
                        paramPane.getChildren ().removeAll (vb1, radIT,
                                radOT,
                                radI,
                                radO, heightT, ht, objAxisT, objAxis,
                                drawMe);
                        double newCX, newCY, newCZ, lenV, widV, depV, gapVal;
                        double oX, oY, oZ;
                        int cpX = 0, cpY = 0, cpZ = 0;
                        int xDir = 0, yDir = 0, zDir = 0;

                        oX = (!baseCX.getText ().isEmpty () ? Double.
                                parseDouble (baseCX.getText ()) : 0.0);
                        oY = (!baseCY.getText ().isEmpty () ? Double.
                                parseDouble (baseCY.getText ()) : 0.0);
                        oZ = (!baseCZ.getText ().isEmpty () ? Double.
                                parseDouble (baseCZ.getText ()) : 0.0);
                        lenV = (!lenVal.getText ().isEmpty () ? Double.
                                parseDouble (lenVal.getText ()) : 0.0);
                        widV = (!widVal.getText ().isEmpty () ? Double.
                                parseDouble (widVal.getText ()) : 0.0);
                        depV = (!depVal.getText ().isEmpty () ? Double.
                                parseDouble (depVal.getText ()) : 0.0);
                        cpX = (!cpyX.getText ().isEmpty () ? Integer.
                                parseInt (cpyX.getText ()) : 0);
                        cpY = (!cpyY.getText ().isEmpty () ? Integer.
                                parseInt (cpyY.getText ()) : 0);
                        cpZ = (!cpyZ.getText ().isEmpty () ? Integer.
                                parseInt (cpyZ.getText ()) : 0);
                        gapVal = (!gap.getText ().isEmpty () ? Double.
                                parseDouble (gap.getText ()) : 0.0);
                        xDir = (!objAxis.getText ().isEmpty () &&
                                objAxis.getText ().matches (axisX) ? 1 : 0);
                        yDir = (!objAxis.getText ().isEmpty () &&
                                objAxis.getText ().matches (axisY) ? 1 : 0);
                        zDir = (!objAxis.getText ().isEmpty () &&
                                objAxis.getText ().matches (axisZ) ? 1 : 0);
                        xDir = ((xDir == 0 && yDir == 0 && zDir == 0)
                                        ? 1 : xDir);

                        objCnt = 0;
                        if ( matList.getValue ().contains ("Copper") ) {
                            if ( cpX != 0 ) {
                                objCnt += cpX;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpX; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCX += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Copper ()));
                                    } else if ( yDir == 1 ) {
                                        newCX += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Copper ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCX += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Copper ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpY != 0 ) {
                                objCnt += cpY;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpY; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Copper ()));
                                    } else if ( yDir == 1 ) {
                                        newCY += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Copper ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Copper ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpZ != 0 ) {
                                objCnt += cpZ;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpZ; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Copper ()));
                                    } else if ( yDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Copper ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCZ += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Copper ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }

                        } else if ( matList.getValue ().contains (
                                "Rubber") ) {
                            if ( cpX != 0 ) {
                                objCnt += cpX;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpX; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCX += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Rubber ()));
                                    } else if ( yDir == 1 ) {
                                        newCX += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Rubber ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCX += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Rubber ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpY != 0 ) {
                                objCnt += cpY;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpY; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Rubber ()));
                                    } else if ( yDir == 1 ) {
                                        newCY += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Rubber ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Rubber ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpZ != 0 ) {
                                objCnt += cpZ;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpZ; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Rubber ()));
                                    } else if ( yDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Rubber ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCZ += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Rubber ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }

                        } else if ( matList.getValue ().contains (
                                "Brass") ) {
                            if ( cpX != 0 ) {
                                objCnt += cpX;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpX; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCX += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Brass ()));
                                    } else if ( yDir == 1 ) {
                                        newCX += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Brass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCX += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Brass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpY != 0 ) {
                                objCnt += cpY;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpY; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Brass ()));
                                    } else if ( yDir == 1 ) {
                                        newCY += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Brass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Brass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpZ != 0 ) {
                                objCnt += cpZ;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpZ; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Brass ()));
                                    } else if ( yDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Brass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCZ += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Brass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                        } else if ( matList.getValue ().contains (
                                "Glass") ) {
                            if ( cpX != 0 ) {
                                objCnt += cpX;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpX; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCX += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Glass ()));
                                    } else if ( yDir == 1 ) {
                                        newCX += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Glass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCX += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Glass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpY != 0 ) {
                                objCnt += cpY;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpY; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Glass ()));
                                    } else if ( yDir == 1 ) {
                                        newCY += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Glass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Glass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpZ != 0 ) {
                                objCnt += cpZ;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpZ; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Glass ()));
                                    } else if ( yDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Glass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCZ += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Glass ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                        } else {
                            if ( cpX != 0 ) {
                                objCnt += cpX;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpX; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCX += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Plastic ()));
                                    } else if ( yDir == 1 ) {
                                        newCX += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Plastic ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCX += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ, lenV,
                                                        widV, depV,
                                                        Material.
                                                        Plastic ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpY != 0 ) {
                                objCnt += cpY;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpY; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Plastic ()));
                                    } else if ( yDir == 1 ) {
                                        newCY += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Plastic ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCY += (gapVal + widV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Plastic ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                            if ( cpZ != 0 ) {
                                objCnt += cpZ;
                                newCX = oX;
                                newCY = oY;
                                newCZ = oZ;
                                for ( int ii = 0; ii < cpZ; ii++ ) {
                                    if ( xDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Plastic ()));
                                    } else if ( yDir == 1 ) {
                                        newCZ += (gapVal + depV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Plastic ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Z_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    } else if ( zDir == 1 ) {
                                        newCZ += (gapVal + lenV);
                                        brkList.add (
                                                new Brick ("Brick",
                                                        newCX, newCY,
                                                        newCZ,
                                                        lenV, widV, depV,
                                                        Material.
                                                        Plastic ()));
                                        brkList.get (ii).
                                                setRotationAxis (
                                                        Rotate.Y_AXIS);
                                        brkList.get (ii).
                                                setRotate (90.0);
                                    }
                                    camV.add (brkList.get (ii));
                                    geoTextEntry
                                            = "Brick" + "  " + newCX +
                                            "  " +
                                            newCY + "  " + newCZ +
                                            "  " + "  " + lenV + "  " +
                                            widV +
                                            "  " + depV + "  " +
                                            objAxis.
                                            getText () + "\n";
                                    geoEntries.appendText (geoTextEntry);
                                }
                                nodeList.setText ("Total " + objCnt +
                                        " Brick added");
                                brkList.clear ();
                            }
                        }
                        matEntries.appendText (matList.getValue () +
                                "\n");
                        numGeom++;
                        paramPane.getChildren ().clear ();
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
            // geoScene = setMyScene(drawWidth, drawHeight);
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
}
