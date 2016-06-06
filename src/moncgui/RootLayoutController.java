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
    @FXML
    private MenuItem makeReport;
    @FXML
    private Menu HOME;
    @FXML
    private MenuItem help;
    @FXML
    private MenuItem exit;
    @FXML
    private MenuItem about;
    @FXML
    private Menu doMat;
    @FXML
    private MenuItem doManageMat;
    @FXML
    private MenuItem RUNMONC;    
    

    private MoncGUI myGUI;

    private Label welcome1;
    private Label welcome2;
    private Label welcome3;
    private Label welcome4;
    private Label welcome5;


    

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
    private void confNew(ActionEvent event){// throws IOException {
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
    private void doPlot(Event event) { // throws IOException {
        myGUI.callAnalyze ();
    }

    @FXML
    private void GeomInitialize(ActionEvent event) {
        myGUI.GeomStart ();
    }

    @FXML
    private void doRPT(ActionEvent event) throws IOException {
        popupMsg.infoBox("Feature is under development. Please have patience. Thank You.", "Feature is under developemnt");
        //myGUI.makeReport ();
    }

    @FXML
    private void doHELP(ActionEvent event) {
        String mesg = "1. Generate Geometry.\n" +
                "2. Complete geometry by clicking UPDATE\n" +
                "3. Complete Configuration and save\n" +
                "4. Run MONC\n" +
                "5. In Analyzer, plot data\n" +
                "6. Select Data file\n" +
                "7. Select 2/3D plot\n" +
                "8. Select columns to be plotted for X, Y and Z axis\n" +
                "9. Data table may be checked to see any superflous data\n" +
                "10. plot data\n" +
                "11. Plot may be exported to PNG/JPG.\n" +
                "12. An executive summary may be produced.\n" +
                "13. Report file may include image automatically produced\n" +
                " from UPDATE button of geometry production.";
        String titleBar = " HELP";
        popupMsg.infoBox (mesg, titleBar);
    }

    @FXML
    private void doAbout(ActionEvent event) {
        String mesg = "This is code developed using Java JDK 1.8\n" +
                "                        VERSION 1.0 \n" +
                "Contact: Dr. Abhijit Bhattacharyya \n" +
                "Nuclear Physics Division \nBhabha Atomic Resarch Centre \n" +
                "INDIA \n" +
                "EMAIL: vega@barc.gov.in,   abhihere@gmail.com" +
                "\n\n" +
                "Upcoming Features:\n" +
                "1. Part of Sphere and Cylinder specifying angles\n" +
                "2. Boolean operation on geometries\n" +
                "3. Copying of geometry along 1D/2D to make arrays\n" +
                "and much more ........";

        String titleBar = " ABOUT THE CODE ";

        popupMsg.infoBox (mesg, titleBar);
    }

    @FXML
    private void manageMat(ActionEvent event) {
        myGUI.manageMat();
    }

    @FXML
    private void MONCRun(ActionEvent event) {
        popupMsg.infoBox ("MONC code is not included with this distribution", "MONC NOT FOUND");
    }

}
