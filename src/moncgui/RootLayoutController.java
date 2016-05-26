/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.IOException;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class RootLayoutController { // implements Initializable 
    

    @FXML
    private Menu doConfig;
    @FXML
    private MenuItem doConfigNew;
    @FXML
    private MenuItem doQuit;
    @FXML
    private Menu doGeom;
    @FXML
    private Menu doRun;
    @FXML
    private Menu anaLyzePlot;
    @FXML
    private MenuItem analyzePlot;
    @FXML
    private MenuItem doGeomStart;
    @FXML
    private AnchorPane rootConfigPane;

    private Label welcome1;
    private Label welcome2;
    private Label welcome3;
    private Label welcome4;
    private Label welcome5;

    private MoncGUI myGUI;
    @FXML
    private MenuItem makeReport;

    public void setMainApp(MoncGUI myGUI) {
        this.myGUI = myGUI;
    }

    private void buildWelcome() {
        welcome1.setFont (new Font ("Times New Roman", 22));
        welcome2.setFont (new Font ("Times New Roman", 18));
        welcome3.setFont (new Font ("Times New Roman", 15));
        welcome4.setFont (new Font ("Times New Roman", 15));
        welcome5.setFont (new Font ("Times New Roman", 12));

        welcome1 = new Label ("Welcome to MONCGUI");
        welcome2 = new Label ("Developed by Abhijit Bhattacharyya");
        welcome3 = new Label (
                "Nuclear Physics Division. Bhabha Atomic Research Centre");
        welcome4 = new Label ("Mumbai INDIA");
        welcome5 = new Label ("EMAIL: abhihere@gmail.com");
        VBox vb1 = new VBox (welcome1, welcome2, welcome3, welcome4, welcome5);

        rootConfigPane.getChildren ().addAll (vb1);
    }

    /**
     * Initializes the controller class.
     */
    //@Override
    public void initialize() {//URL url, ResourceBundle rb
        //buildWelcome();
    }

    @FXML
    private void confNew(ActionEvent event) throws IOException {
        myGUI.callConfig ();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit (0);
    }

    //private void doPlot(ActionEvent event) throws IOException {
    //    myGUI.callAnalyze();
    //}
    @FXML
    private void doPlot(Event event) throws IOException {
        myGUI.callAnalyze ();
    }

    @FXML
    private void GeomInitialize(ActionEvent event) {
        myGUI.GeomStart ();
    }

    @FXML
    private void doRPT(ActionEvent event) throws IOException {
        myGUI.makeReport ();
    }
}
