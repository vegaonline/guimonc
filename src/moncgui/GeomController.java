/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 * FXML Controller class
 *
 * @author vega
 */
public class GeomController implements Initializable {

    private int initialized = 0;

    private MoncGUI myGUI;
    private Scene geoScene;
    private Stage geoStage ;
    private CameraView camV = new CameraView();

    private Stage newStage;
    private Scene newScene;
    private Axis3D axis;

    private final ScrollBar scB = new ScrollBar();
    private final SplitPane paramPane1 = new SplitPane();
    private final SplitPane drawPane = new SplitPane();
    private final GridPane paramPane = new GridPane();
    private final Group axisGroup = new Group();
    private final Group rootGr = new Group();

    double anchorX, anchorY, anchorAngle;
    //private final double cameraDistance = -1000.0;

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
    private Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
    private Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);
    private Rotate rotateZ = new Rotate(0, Rotate.Z_AXIS);
    private int radSample = 10, lenSample = 3;

    private ArrayList<Sphere> sphList;
    private ArrayList<Cylinder> cylList;
    private ArrayList<Shape3D> myShapeList;
    private ArrayList<PhongMaterial> MaterialList
            = new ArrayList<PhongMaterial>();
    private List<String> materialNames = new ArrayList<String>();

    private Label BaseCoord = new Label("Origin");
    private TextField baseCX = new TextField();
    private TextField baseCY = new TextField();
    private TextField baseCZ = new TextField();
    private Label radT = new Label("Radius");
    private TextField rad = new TextField();
    private Label heightT = new Label("Height");
    private TextField ht = new TextField();
    private Label matT = new Label("Material");
    private ComboBox<String> matList = new ComboBox<String>();
    private Button drawMe = new Button("Draw");

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

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = myGUI;
    }

    public void setMyScene(Scene scene) {
        //this.geoScene = scene;
        this.geoScene = scene;
    }

    public void setMyStage(Stage stage) {
        this.geoStage = stage;
        System.out.println("setMyStage :  Height = " + geoStage.getHeight()+"  Width = "+geoStage.getWidth());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        buildScene();
        /*
        geoStage.setTitle ("Testing");
        final Scene geoScene = new Scene (camV, drawWidth, drawHeight, true);
        geoScene.setFill (new RadialGradient (225, 0.85, centerX, centerY,
                drawWidth, false,
                CycleMethod.NO_CYCLE, new Stop[]{new Stop (0f, Color.BLUE),
                    new Stop (1f, Color.LIGHTBLUE)}));
        buildCamera (geoScene);
        Context3D context = Context3D.getInstance (camV);
        lightSetting(context);
        axis = buildAxes ();
        camV.add (axis);
         */
    }

    private void fixSetup() {
        geoStage.setTitle("Testing");
        System.out.println("fixSetup :  Height = " + geoStage.getHeight()+"  Width = "+geoStage.getWidth());
        System.out.println("Scene Height " + geoStage.getScene().getHeight()+" Width "+geoStage.getScene().getWidth());
        newScene = geoStage.getScene();
        
        System.out.println("Scene Height " + geoScene.getHeight()+" Width "+geoScene.getWidth());
        final Scene geoScene = new Scene(camV, drawWidth, drawHeight, true);
        geoScene.setFill(new RadialGradient(225, 0.85, centerX, centerY,
                drawWidth, false,
                CycleMethod.NO_CYCLE, new Stop[]{new Stop(0f, Color.BLUE),
                    new Stop(1f, Color.LIGHTBLUE)}));
        buildCamera(geoScene);
        Context3D context = Context3D.getInstance(camV);
        lightSetting(context);
        axis = buildAxes();
        camV.add(axis);
    }

    private void buildScene() {
        geoWidth = 600;
        geoHeight = 400;
        menuWidth = 100.0;
        menuHeight = geoHeight - yoffset;
        drawWidth = geoWidth - menuWidth - xoffset;
        drawHeight = menuHeight;
        centerX = 0.5 * drawWidth;// - xoffset;
        centerY = 0.5 * drawHeight;// + yoffset;
        centerZ = 0.3;
        scaleX = 0.08;
        scaleY = 0.08;
        scaleZ = 0.08;
        paramPane1.setPrefSize(menuWidth, menuHeight);
        paramPane1.setOrientation(Orientation.VERTICAL);
        paramPane.setGridLinesVisible(false);
        paramPane.setPrefSize(menuWidth, menuHeight);
        paramPane.setHgap(1);
        paramPane.setVgap(3);
        paramPane.setPadding(new Insets(0, 1, 0, 1));
        paramPane1.getItems().add(paramPane);

        drawPane.setPrefSize(drawWidth, drawHeight);

        geoMainArea.setLeft(paramPane);
        geoMainArea.setRight(drawPane);
    }

    private void buildCamera(Scene scene) {
        scene.setCamera(new PerspectiveCamera());
    }

    private void lightSetting(Context3D context) {
        context.lighting = new Lighting3D();
        context.lighting.add(Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 1.5, new Vector3D(1, 0.8, 0.6));
        context.lighting.add(Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 1.0, new Vector3D(-1, -0.8, 0.6));
        context.lighting.add(Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 0.5, new Vector3D(0, -0.2, -0.8));
        context.showLights();
        context.setShowBorders(true);
        context.setShowTexts(true);
    }

    private Axis3D buildAxes() {
        axis = new Axis3D(50.0, Color.AQUAMARINE);
        return axis;
    }

    public void drawCylSolid() {

        BaseCoord.setFont(new Font("Times New Roman", 11));
        radT.setFont(new Font("Times New Roman", 11));
        heightT.setFont(new Font("Times New Roman", 11));
        matT.setFont(new Font("Times New Roman", 11));

        baseCX.setPrefColumnCount(6);
        baseCX.setAlignment(Pos.CENTER_RIGHT);
        baseCY.setPrefColumnCount(6);
        baseCY.setAlignment(Pos.CENTER_RIGHT);
        baseCZ.setPrefColumnCount(6);
        baseCZ.setAlignment(Pos.CENTER_RIGHT);
        rad.setPrefColumnCount(9);
        rad.setAlignment(Pos.CENTER_RIGHT);
        rad.setMaxSize(60, 1); // width height
        ht.setPrefColumnCount(9);
        ht.setAlignment(Pos.CENTER_RIGHT);
        ht.setMaxSize(60, 1); // width height

        HBox hb1 = new HBox(baseCX, baseCY, baseCZ);
        hb1.setSpacing(1); //hb1.setPadding(new Insets(2));
        VBox vb1 = new VBox(BaseCoord, hb1);

        baseCX.setPromptText("0.0");
        baseCY.setPromptText("0.0");
        baseCZ.setPromptText("0.0");
        rad.setPromptText("50.0");
        ht.setPromptText("100.0");
        /*
        paramPane.add (vb1, 0, 0); // col row
        paramPane.add (radT, 0, 1);
        paramPane.add (rad, 0, 2);
        paramPane.add (heightT, 0, 3);
        paramPane.add (ht, 0, 4);
        paramPane.add (matT, 0, 5);
        paramPane.add (matList, 0, 6);
        paramPane.add (drawMe, 0, 7);
         */
        rad0 = 90.0;
        len0 = 130.0;
        radSample = 5;//2 * (int) rad0;
        lenSample = 3;//(int) (len0 / 5.0);

        cylTest cyl
                = new cylTest("cyl", lenSample, radSample, rad0, len0);
        //cyl.getTransforms ().add (new Translate (centerX, -centerY, 10.0));

        // paramPane.getChildren ().removeAll (vb1, radT, rad, heightT, ht, matT, matList, drawMe);
        camV.add(cyl);

        drawMe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent ev) {
                /*
                 * cylTest cyl = new cylTest ("cyl", lenSample, radSample, rad0,
                 * len0);
                 *
                 * paramPane.getChildren ().removeAll (vb1, radT, rad, heightT,
                 * ht, matT, matList, drawMe); camV.add (cyl);
                 */
            }
        });
        complete();
    }

    void complete() {
        camV.frameCam(geoStage, geoScene);
        MouseHandler mouseHandler = new MouseHandler(geoScene, camV);
        KeyHandler keyHandler = new KeyHandler(geoStage, geoScene, camV);
        geoStage.setScene(geoScene);
        geoStage.show();
    }

    @FXML
    void doShapeCyl(ActionEvent event) {
        if (initialized == 0) {
            System.out.println("Processing initialized=0");
            fixSetup();
            initialized = 1;
        }
        drawCylSolid();
    }

    public void drawCircSolid() {
        if (initialized == 0) {
            fixSetup();
            initialized = 1;
        }
    }

    @FXML
    private void doShapeCirc(ActionEvent event) {
        drawCircSolid();
    }

    @FXML
    private void doDrawReset(MouseEvent event) {
        drawPane.getItems().clear();

    }
}
