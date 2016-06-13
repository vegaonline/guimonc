/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.*;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class ConfigSetController implements Initializable {

    private MoncGUI myGUI;
    private Scene confScene;
    private Stage confStage;

    private Label projTL = new Label ("Title");
    private TextField projT = new TextField ();
    private Label eventNTL = new Label ("Event Number");
    private TextField eventNT = new TextField ();
    private Label inactNL = new Label ("Inactive Neutron");
    private TextField inactN = new TextField ();
    private Label actNL = new Label ("Active Neutron");
    private TextField actN = new TextField ();
    private Label neut1GenL = new Label ("Number of neutron in 1 generation");
    private TextField neut1Gen = new TextField ();
    private Label wtCutoffL = new Label ("Weight Cutoff");
    private TextField wtCutoff = new TextField ();
    private Label randSeedL = new Label ("Initial Seed Random Number");
    private TextField randSeed = new TextField ();
    private Label chargeZL = new Label ("Charge (Z)");
    private TextField chargeZ = new TextField ();
    private Label massAL = new Label ("Mass Number (A)");
    private TextField massA = new TextField ();
    private Label numBodyL = new Label ("Number of Bodies");
    public TextField numBody = new TextField ();

    private Button saveButton = new Button ("Save");
    private Button cleanButton = new Button ("Clean");

    public Label matL
            = new Label ("Material Description from Material Library ");
    public Button upd = new Button ("Update Material");
    public Label zoneL = new Label ("Zoning Description");
    public Label geoL = new Label (" Geometrical Configuration ");
    public TextArea geoArea = new TextArea ();   // for printing geo object definition
    public TextArea zoneArea = new TextArea ();  // for printing zone definition with vacuum = 0
    public TextArea matArea = new TextArea ();   // for printing material definition
    private VBox vb1 = new VBox ();
    private java.util.List<double[]> eleData
            = new java.util.ArrayList<double[]> ();
    private ObservableList<String> elementName = FXCollections.
            observableArrayList ();

    private final GridPane objPane = new GridPane ();
    private DropShadow shadow = new DropShadow ();
    private ScrollPane sp = new ScrollPane ();

    private String configFName = null;

    @FXML
    private AnchorPane configPane;
    @FXML
    private BorderPane bPane;

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = badGUI;
    }

    public void setMyStage(Stage stage) {
        this.confStage = stage;
    }

    public void setArray(ObservableList<String> eleName, List<double[]> eleData) {
        this.elementName = eleName;
        this.eleData = eleData;
        System.out.println ("In config setArray : size = " + eleName.size ());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        buildScreen ();
        actionButtonProperties ();
    }

    private void buildScreen() {
        configPane.setPrefWidth (850);
        matArea.setMaxHeight (75);
        matArea.autosize ();
        zoneArea.setMaxHeight (75);
        zoneArea.autosize ();
        geoArea.setMaxHeight (75);
        geoArea.autosize ();
        objPane.autosize ();

        projTL.setFont (new Font ("Times New Roman", 12));
        eventNTL.setFont (new Font ("Times New Roman", 12));
        inactNL.setFont (new Font ("Times New Roman", 12));
        actNL.setFont (new Font ("Times New Roman", 12));
        neut1GenL.setFont (new Font ("Times New Roman", 12));
        wtCutoffL.setFont (new Font ("Times New Roman", 12));
        randSeedL.setFont (new Font ("Times New Roman", 12));
        chargeZL.setFont (new Font ("Times New Roman", 12));
        massAL.setFont (new Font ("Times New Roman", 12));
        matL.setFont (new Font ("Times new Roman", 12));
        matL.setAlignment (Pos.CENTER);
        matL.setTranslateX (200);
        zoneL.setFont (new Font ("Times new Roman", 12));
        zoneL.setAlignment (Pos.CENTER);
        zoneL.setTranslateX (200);
        geoL.setFont (new Font ("Times New Roman", 12));
        geoL.setAlignment (Pos.CENTER);
        geoL.setTranslateX (200);

        projT.setMaxSize (600, 1);
        eventNT.setMaxSize (70, 1);
        inactN.setMaxSize (50, 1);
        actN.setMaxSize (50, 1);
        neut1Gen.setMaxSize (50, 1);
        wtCutoff.setMaxSize (60, 1);
        randSeed.setMaxSize (90, 1);
        chargeZ.setMaxSize (40, 1);
        massA.setMaxSize (40, 1);

        HBox hb1 = new HBox (projTL, projT, eventNTL, eventNT);
        hb1.setSpacing (10);
        HBox hb2 = new HBox (inactNL, inactN, actNL, actN, neut1GenL, neut1Gen);
        hb2.setSpacing (10);
        HBox hb3 = new HBox (wtCutoffL, wtCutoff, randSeedL, randSeed);
        hb3.setSpacing (10);
        HBox hb4 = new HBox (chargeZL, chargeZ, massAL, massA);
        hb4.setSpacing (10);
        HBox hb100 = new HBox (zoneL);
        HBox hb101 = new HBox (matL, upd);
        HBox hb102 = new HBox (geoL);
        HBox hb200 = new HBox (saveButton, cleanButton);
        vb1.getChildren ().addAll (hb1, hb2, hb3, hb4, hb100, zoneArea, hb101,
                matArea, hb102, geoArea);
        sp.setContent (vb1);
        hb200.setLayoutY (configPane.getPrefHeight () + 10);

        configPane.getChildren ().addAll (sp, hb200);
        upd.addEventHandler (MouseEvent.MOUSE_CLICKED, evt -> {
            getUpdateMat ();
        });
    }

    private void getUpdateMat() {
        String matStr=null;
        for (int ii = 0; ii < elementName.size (); ii++) {
            matStr = elementName.get (ii);
            matArea.appendText (matStr);
        }
        
        
    }

    private void actionButtonProperties() {
        sp.setHbarPolicy (ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy (ScrollPane.ScrollBarPolicy.ALWAYS);

        BooleanBinding boolBind = projT.textProperty ().isEmpty ()
                .or (eventNT.textProperty ().isEmpty ())
                .or (chargeZ.textProperty ().isEmpty ())
                .or (massA.textProperty ().isEmpty ())
                .or (geoArea.textProperty ().isEmpty ());

        saveButton.disableProperty ().bind (boolBind);
        cleanButton.disableProperty ().bind (boolBind);

        saveButton.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent evt) {
                saveMe ();
            }
        });

        saveButton.addEventHandler (MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent me) {
                saveButton.setEffect (shadow);
            }
        });

        saveButton.addEventHandler (MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent me) {
                saveButton.setEffect (null);
            }
        });

        cleanButton.setOnAction (new EventHandler<ActionEvent> () {
            @Override
            public void handle(ActionEvent evt) {
                cleanMe ();
            }
        });

        cleanButton.addEventHandler (MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent me) {
                cleanButton.setEffect (shadow);
            }
        });

        cleanButton.addEventHandler (MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent> () {
            @Override
            public void handle(MouseEvent me) {
                cleanButton.setEffect (null);
            }
        });
    }

    private void saveMe() {
        FileChooser fileChooser = new FileChooser ();
        File recordsDir = new File ("configSet");
        if ( !recordsDir.exists () ) {
            recordsDir.mkdirs ();
        }
        fileChooser.setInitialDirectory (recordsDir);
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter ("Configuration File (.inp)",
                        "*.inp");
        fileChooser.getExtensionFilters ().add (extFilter);
        fileChooser.setTitle ("Saving the Configuration");
        configFName = fileChooser.showSaveDialog (confStage).getPath ();
        if ( configFName != null ) {
            File file = new File (configFName);
            try ( FileWriter fw = new FileWriter (file) ) {
                fw.write (
                        "C    MONC-2.0       $ Monte Carlo Nucleon Transport Code");
                fw.write (projT.getText () + "$ Project Title \n");
                fw.write (eventNT.getText () + "$ Number of events \n");
                fw.
                        write (inactN.getText () +
                                "$ Inactive Neutron Generation \n");
                fw.write (actN.getText () + "$ Active Neutron Generation \n");
                fw.write (neut1Gen.getText () +
                        "$ Number of neutron in one generation \n");
                fw.write (wtCutoff.getText () + "$ weight cutoff \n");
                fw.write (randSeed.getText () + "$ Initial Random Number \n");
                fw.write (chargeZ.getText () + "$ Charge (Z) \n");
                fw.write (massA.getText () + "$ Mass Number (A) \n");

                fw.write ("C    ------------ GEOMETRY DATA ---------- ");
                fw.write (geoArea.getText () + "\n");
                fw.close ();
            } catch (IOException ex) {
                popupMsg.infoBox ("No file selected",
                        "File Saving Procedure not successful");
                Logger.getLogger (ConfigSetController.class.getName ()).
                        log (Level.SEVERE, null, ex);
            }
        }
    }

    private void cleanMe() {
        projT.clear ();
        eventNT.clear ();
        inactN.clear ();
        actN.clear ();
        neut1Gen.clear ();
        wtCutoff.clear ();
        randSeed.clear ();
        chargeZ.clear ();
        massA.clear ();
    }
}
