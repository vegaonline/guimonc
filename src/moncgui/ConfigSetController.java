/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class ConfigSetController implements Initializable {

    private MoncGUI myGUI;
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
    private Button saveButton = new Button("Save");
    private Button cleanButton = new Button("Clean");

    public Label geoL = new Label (" Geometrical Configuration ");
    public TextArea geoArea = new TextArea ();

    private final GridPane objPane = new GridPane ();

    @FXML
    private AnchorPane configPane;

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = myGUI;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        buildScreen ();
    }

    private void buildScreen() {
        geoArea.setMaxHeight (150);
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
        
        geoL.setFont (new Font ("Times New Roman", 14));
        geoL.setAlignment (Pos.CENTER);
        geoL.setTranslateX (200);

        projT.setMaxSize (600, 1); 
        eventNT.setMaxSize (70, 1);
        inactN.setMaxSize (50, 1);
        actN.setMaxSize (50, 1);
        neut1Gen.setMaxSize (50, 1);
        wtCutoff.setMaxSize (60, 1);
        randSeed.setMaxSize (90, 1);
        chargeZ.setMaxSize(40,1);   massA.setMaxSize(40,1);

        HBox hb1 = new HBox (projTL, projT, eventNTL, eventNT);
        hb1.setSpacing (10);
        HBox hb2 = new HBox (inactNL, inactN, actNL, actN, neut1GenL, neut1Gen);
        hb2.setSpacing (10);
        HBox hb3 = new HBox (wtCutoffL, wtCutoff, randSeedL, randSeed);
        hb3.setSpacing (10);
        HBox hb4 = new HBox(chargeZL,chargeZ, massAL, massA);
        hb4.setSpacing (10);
        HBox hb99 = new HBox(geoL);
        HBox hb100 = new HBox(saveButton, cleanButton);

        //VBox vb1 = new VBox (hb1, hb2, hb3, hb4, hb99, geoArea);
        VBox vb1 = new VBox (hb1, hb2, hb3, hb4, hb99, geoArea, hb100);

        
       // saveButton.setLayoutX(100); saveButton.setLayoutY(350);
        configPane.setPrefWidth (850);
        //configPane.getChildren ().addAll (vb1,saveButton);
        configPane.getChildren ().addAll (vb1);
    }
}
