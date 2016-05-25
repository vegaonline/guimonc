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
import javafx.scene.layout.AnchorPane;

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
    private AnchorPane configPane;    
    
    
    private MoncGUI myGUI;        

    public void setMainApp(MoncGUI myGUI){
        this.myGUI = myGUI;
    }
        
    /**
     * Initializes the controller class.
     */
    //@Override
    public void initialize() {//URL url, ResourceBundle rb
        // TODO
        
    }    

    public static void main(String[] args) {
        //launch(args);
    }
    
    
    @FXML
    private void confNew(ActionEvent event) throws IOException {  
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
    private void doPlot(Event event) throws IOException{
        myGUI.callAnalyze();
    }

    @FXML
    private void GeomInitialize(ActionEvent event) {
        myGUI.GeomStart();
    }
}
