/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.IOException;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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

    public void makeVistaEffect() {
        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0);

        Text text1 = new Text("Vista");
        Text text2 = new Text(" -- An user-friendly Graphical User Interface \n    to Computation and Analysis.");
        Text welcome1 = new Text("Welcome to VISTA");
        Text welcome2 = new Text("Developed by Abhijit Bhattacharyya");
        Text welcome3 = new Text(
                "Nuclear Physics Division. \nBhabha Atomic Research Centre");
        Text welcome4 = new Text("Mumbai INDIA");
        Text welcome5 = new Text("EMAIL: abhihere@gmail.com");
        
        text1.setFill(Color.STEELBLUE);
        text1.setFont(Font.font(null, FontWeight.BOLD, 60));
        text1.setX(10.0);
        text1.setY(10.0);
        text1.setTextOrigin(VPos.TOP);
        text1.setEffect(lighting);
        text1.setLayoutX(220);
        text1.setLayoutY(160);

        text2.setFill(Color.STEELBLUE);
        text2.setFont(Font.font(null, FontWeight.BOLD, 15));
        text2.setX(10.0);
        text2.setY(10.0);
        text2.setTextOrigin(VPos.TOP);
        text2.setEffect(lighting);
        text2.setLayoutX(220);
        text2.setLayoutY(220);
        
        welcome1.setFill(Color.BLUEVIOLET);
        welcome2.setFill(Color.CRIMSON);
        welcome3.setFill(Color.CRIMSON);
        welcome4.setFill(Color.CRIMSON);
        welcome5.setFill(Color.CRIMSON);
        
        welcome1.setFont(Font.font(null, FontWeight.BOLD, 24));
        welcome1.setEffect(lighting);
        welcome2.setFont(Font.font(null, FontWeight.BOLD, 15));
        welcome2.setEffect(lighting);
        welcome3.setFont(Font.font(null, FontWeight.BOLD, 12));
        welcome3.setEffect(lighting);
        welcome4.setFont(Font.font(null, FontWeight.BOLD, 12));
        welcome4.setEffect(lighting);
        welcome5.setFont(Font.font(null, FontWeight.BOLD, 10));
        welcome5.setEffect(lighting);
        
        VBox vb1 = new VBox(welcome1, welcome2, welcome3, welcome4, welcome5);
        vb1.setLayoutX(340);
        vb1.setLayoutY(350);
        rootConfigPane.getChildren().addAll(text1,text2,  vb1);
    }


    /**
     * Initializes the controller class.
     */
    //@Override
    public void initialize() {//URL url, ResourceBundle rb
        makeVistaEffect();
    }

    @FXML
    private void confNew(ActionEvent event) {// throws IOException {
        myGUI.callConfig();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }

    //private void doPlot(ActionEvent event) throws IOException {
    //    myGUI.callAnalyze();
    //}
    @FXML
    private void doPlot(Event event) { // throws IOException {
        myGUI.callAnalyze();
    }

    @FXML
    private void GeomInitialize(ActionEvent event) {
        myGUI.GeomStart();
    }

    @FXML
    private void doRPT(ActionEvent event) throws IOException {
        popupMsg.infoBox("Feature is under development. Please have patience. Thank You.", "Feature is under developemnt");
        //myGUI.makeReport ();
    }

    @FXML
    private void doHELP(ActionEvent event) {
        String mesg = "1. Generate Geometry.\n"
                + "2. Complete geometry by clicking UPDATE\n"
                + "3. Complete Configuration and save\n"
                + "4. Run MONC\n"
                + "5. In Analyzer, plot data\n"
                + "6. Select Data file\n"
                + "7. Select 2/3D plot\n"
                + "8. Select columns to be plotted for X, Y and Z axis\n"
                + "9. Data table may be checked to see any superflous data\n"
                + "10. plot data\n"
                + "11. Plot may be exported to PNG/JPG.\n"
                + "12. An executive summary may be produced.\n"
                + "13. Report file may include image automatically produced\n"
                + " from UPDATE button of geometry production.";
        String titleBar = " HELP";
        popupMsg.infoBox(mesg, titleBar);
    }

    @FXML
    private void doAbout(ActionEvent event) {
        String mesg = "Vista developed using Java JDK 1.8\n"
                + "                        VERSION 1.0 \n"
                + "Contact: Dr. Abhijit Bhattacharyya \n"
                + "Nuclear Physics Division \nBhabha Atomic Resarch Centre \n"
                + "INDIA \n"
                + "EMAIL: vega@barc.gov.in,   abhihere@gmail.com"
                + "\n\n"
                + "Upcoming Features:\n"              
                + "1. Boolean operation on geometries\n"
                + "2. Copying of geometry along 1D/2D to make arrays\n"
                + "and much more ........";

        String titleBar = " ABOUT THE CODE ";

        popupMsg.infoBox(mesg, titleBar);
    }

    @FXML
    private void manageMat(ActionEvent event) {
        myGUI.manageMat();
    }

    @FXML
    private void MONCRun(ActionEvent event) {
        popupMsg.infoBox("MONC code is not included with this distribution", "MONC NOT FOUND");
    }

}
