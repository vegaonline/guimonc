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
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class MatGeneratorController implements Initializable {

    private MoncGUI myGUI;
    private Stage matStage;
    private Scene matScene;
    FileChooser fileChooser = new FileChooser ();
    public FileWriter fileWriter;
    String fName = null;
    int selPressed = 0;

    private MenuBar menuBar;
    private Menu matProcess;
    private Menu matSelect;
    private MenuItem newMat;
    private MenuItem delMat;
    private MenuItem seeMat;
    private MenuItem selectIt;

    private Label matNameL = new Label ("Material Name");
    private TextField matName = new TextField ();
    private ListView<String> materials = new ListView ();
    private ListView<String> matSelected = new ListView ();
    private Label selMat = new Label ("Select Element");
    private Button selMe = new Button ("Select");
    private Button delMe = new Button ("Delete");
    private Button clsMe = new Button ("Clear");
    private Button acceptMe = new Button ("Accept");

    private Map<String, Integer> datType = new HashMap<String, Integer>();    
    ArrayList<Double> oneRow = new ArrayList<Double> ();
    ObservableList<Double> eleData1 = FXCollections.observableArrayList (oneRow);
    ObservableList<Double> eleData2 = FXCollections.observableArrayList (oneRow);
    private List<String> elename = new ArrayList<String> ();
    ObservableList<String> elementName = FXCollections.observableArrayList ();
    ObservableList<String> eleSelectName = FXCollections.observableArrayList ();

    @FXML
    private AnchorPane matGenPane;

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = badGUI;
    }

    public void setMyStage(Stage badStage) {
        this.matStage = badStage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuBar = new MenuBar ();
        matProcess = new Menu ("Process material");
        matSelect = new Menu ("Select material for Simulation");
        newMat = new MenuItem ("Create Material");
        delMat = new MenuItem ("Delete Material");
        seeMat = new MenuItem ("Review Material");
        selectIt = new MenuItem ("Select Material");

        matProcess.getItems ().addAll (newMat, delMat, seeMat);
        matSelect.getItems ().add (selectIt);
        menuBar.getMenus ().addAll (matProcess, matSelect);
        matGenPane.getChildren ().add (menuBar);

        newMat.setOnAction (new EventHandler<ActionEvent> () {
            public void handle(ActionEvent ev) {
                System.out.println (" I am in newmat");
                makeNewMat ();
            }
        });

        delMat.setOnAction (new EventHandler<ActionEvent> () {
            public void handle(ActionEvent ev) {
                deleteMat ();
            }
        });

        seeMat.setOnAction (new EventHandler<ActionEvent> () {
            public void handle(ActionEvent ev) {
                reviewMat ();
            }
        });

        selectIt.setOnAction (new EventHandler<ActionEvent> () {
            public void handle(ActionEvent ev) {
                selMat ();
            }
        });
    }

    public String matFileHandler(int act) {
        String fileName = null;
        File dataF;
        File recordsDir = new File ("Materials");
        if ( !recordsDir.exists () ) {
            recordsDir.mkdirs ();
        }
        fileChooser.setInitialDirectory (recordsDir);
        fileChooser.getExtensionFilters ().clear ();
        FileChooser.ExtensionFilter extFilt = new FileChooser.ExtensionFilter (
                "Material Database (.mat)", "*.mat");
        fileChooser.getExtensionFilters ().add (extFilt);

        while (fileName == null && act == 1) { // open file
            fileChooser.setTitle ("Select Material database to Read data");
            fileName = fileChooser.showOpenDialog (matStage).getPath ();
        }
        while (fileName == null && act == 2) { // save file
            fileChooser.setTitle ("Select Material database to save data");
            fileName = fileChooser.showSaveDialog (matStage).getPath ();
        }
        if ( fileName != null ) {
            //dataF = new File(fileName);
            return fileName;
        } else {
            popupMsg.infoBox ("No file selected",
                    "File Saving Procedure not successful");
            return null;
        }
    }

    public void makeNewMat() {
        popupMsg.infoBox (
                "Material management is under development. Please have patience. Thank You",
                "Feature is under development");
        matGenPane.getChildren ().clear ();
        matGenPane.getChildren ().add (menuBar);

    }

    public void deleteMat() {
        popupMsg.infoBox (
                "Material management is under development. Please have patience. Thank You",
                "Feature is under development");
    }

    public void reviewMat() {
        File dataF = null;
        String fileName = null;
        fileName = matFileHandler (1);
        System.out.println (dataF);
        popupMsg.infoBox (
                "Material management is under development. Please have patience. Thank You",
                "Feature is under development");
    }

    public void getdataContents(File fl1, File fl2) {
        String line = null;
        String cvsSplitBy = ",";
        try {
            Reader reader = new FileReader (fl1);
            Closeable resource = reader;
            BufferedReader br = new BufferedReader (reader);
            resource = br;
            line = null;
            int i = 0;
            while ((line = br.readLine ()) != null) {
                String[] data = line.split (cvsSplitBy);
                oneRow.clear ();
                double tstData = 0.0;
                int j = 0;
                if ( !data[0].contains ("#") ) {
                    tstData = Double.parseDouble (data[1].toString ());
                    oneRow.add (tstData);
                    tstData = Double.parseDouble (data[2].toString ());
                    oneRow.add (tstData);
                    tstData = Double.parseDouble (data[3].toString ());
                    oneRow.add (tstData);
                    tstData = Double.parseDouble (data[4].toString ());
                    oneRow.add (tstData);                    
                    eleData1.addAll (oneRow);
                    elename.add (data[0].toString ());
                    elementName.add (data[0].toString ());
                    datType.put (data[0].toString (), 0);
                }
            }
            resource.close ();
        } catch (FileNotFoundException ex) {
            Logger.getLogger (MatGeneratorController.class.getName ()).log (
                    Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger (MatGeneratorController.class.getName ()).log (
                    Level.SEVERE, null, ex);
        }

        //---------------
        try {
            Reader reader = new FileReader (fl2);
            Closeable resource = reader;
            BufferedReader br = new BufferedReader (reader);
            resource = br;
            line = null;
            int i = 0;
            while ((line = br.readLine ()) != null) {
                String[] data = line.split (cvsSplitBy);
                int j = 0;
                double tstData = 0.0;
                oneRow.clear ();
                if ( !data[0].contains ("#") ) {
                    tstData = Double.parseDouble (data[1].toString ());
                    oneRow.add (tstData);
                    tstData = Double.parseDouble (data[2].toString ());
                    oneRow.add (tstData);
                    tstData = Double.parseDouble (data[3].toString ());
                    oneRow.add (tstData);
                    int dLim = (int) (2 * tstData);
                    for ( int jj = 0; jj < dLim; jj++ ) {
                        tstData = Double.parseDouble (data[4 + jj].toString ());
                        oneRow.add (tstData);
                    }
                    eleData2.addAll (oneRow);
                    elename.add (data[0].toString ());
                    elementName.add (data[0].toString ());
                    datType.put (data[0].toString (), 1);
                    System.out.println(elementName.get (elementName.size ()-1));
                }
            }
            resource.close ();
        } catch (FileNotFoundException ex) {
            Logger.getLogger (MatGeneratorController.class.getName ()).log (
                    Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger (MatGeneratorController.class.getName ()).log (
                    Level.SEVERE, null, ex);
        }

    }

    public void selMat() {
        String fileName1 = "Materials/VistaElements.mat";
        String fileName2 = "Materials/VistaCompound.mat";
        int dLen = 0;
        File dataF1 = null;
        File dataF2 = null;
        String line = null;
        String cvsSplitBy = ",";
        File recordsDir = new File ("Materials");
        if ( !recordsDir.exists () ) {
            recordsDir.mkdirs ();
        }

        dataF1 = new File (fileName1);
        dataF2 = new File (fileName2);
        getdataContents (dataF1, dataF2);
        
        materials.getItems ().clear ();
        materials.setMaxWidth (180);
        materials.setMaxHeight (130);
        materials.setLayoutX (30);
        materials.setLayoutY (90);
        materials.setItems (elementName);
        selMat.setLayoutX (30);
        selMat.setLayoutY (60);
        selMe.setLayoutX (225);
        selMe.setLayoutY (155);
        matSelected.getItems ().clear ();
        matSelected.setMaxWidth (60);
        matSelected.setMaxHeight (130);
        matSelected.setLayoutX (300);
        matSelected.setLayoutY (90);
        clsMe.setLayoutX (380);
        clsMe.setLayoutY (110);
        delMe.setLayoutX (380);
        delMe.setLayoutY (140);
        acceptMe.setLayoutX (380);
        acceptMe.setLayoutY (170);
        matGenPane.getChildren ().addAll (selMat, materials, selMe, matSelected,
                clsMe, delMe, acceptMe);

        selMe.addEventHandler (MouseEvent.MOUSE_CLICKED, evt -> {
            materials.getSelectionModel ().selectedItemProperty ().
                    addListener (new ChangeListener<String> () {
                        @Override
                        public void changed(
                                ObservableValue<? extends String> ov,
                                String old_val, String new_val) {
                            eleSelectName.add (new_val);
                            int IDX = materials.getSelectionModel ().
                                    getSelectedIndex ();
                            // double[] oneRow = new double[4];
                            oneRow.clear ();                            
                            matSelected.setItems (eleSelectName);
                        }
                    });
            eleSelectName.clear ();
        });

        clsMe.addEventHandler (MouseEvent.MOUSE_CLICKED, evt -> {
            matSelected.getItems ().clear ();
        });

        delMe.addEventHandler (MouseEvent.MOUSE_CLICKED, evt -> {
            int selIDX = -1;
            selIDX = matSelected.getSelectionModel ().getSelectedIndex ();
            System.out.println (selIDX);
            matSelected.getItems ().remove (selIDX);
        });

        acceptMe.addEventHandler (MouseEvent.MOUSE_CLICKED, evt -> {
            eleSelectName.addAll (matSelected.getSelectionModel ().
                    getSelectedItems ());
            //myGUI.setArray (eleSelectName, eleData2);
            matGenPane.getChildren ().clear ();
        });
    }
}
