/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    private MenuBar menuBar;
    private Menu newMat;
    private Menu delMat;
    private Menu seeMat;

    private Label matNameL = new Label ("Material Name");
    private TextField matName = new TextField ();

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
        newMat = new Menu ("Create Material");
        delMat = new Menu ("Delete Material");
        seeMat = new Menu ("Review Material");
        menuBar.getMenus ().addAll (newMat, delMat, seeMat);
        matGenPane.getChildren ().add (menuBar);

        newMat.setOnAction (new EventHandler<ActionEvent> () {
            public void handle(ActionEvent ev) {
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
    }

    public String matFileHandler(int act) {
        String fileName = null;
        File recordsDir = new File ("Materials");
        if ( !recordsDir.exists () ) {
            recordsDir.mkdirs ();
        }
        fileChooser.setInitialDirectory (recordsDir);
        FileChooser.ExtensionFilter extFilt = new FileChooser.ExtensionFilter (
                "Material Database (.MDB)", ".MDB");
        fileChooser.getExtensionFilters ().add (extFilt);
        while (fileName == null && act == 1) { // open file
            fileName = fileChooser.showOpenDialog (matStage).getPath ();
        }
        while (fileName == null && act == 0) { // save file
            fileName = fileChooser.showSaveDialog (matStage).getPath ();
        }
        return fileName;
    }

    public void makeNewMat() {
        matGenPane.getChildren ().clear ();
        matGenPane.getChildren ().add (menuBar);

    }

    public void deleteMat() {

    }

    public void reviewMat() {

    }
}
