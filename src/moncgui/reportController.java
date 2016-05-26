/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.*;
import javafx.scene.layout.*;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class reportController implements Initializable {
    private MoncGUI myGUI;
    private CreatePDFA myPDF;
    private String str1=null;

    @FXML
    private BorderPane bRPTPane;
    @FXML
    private AnchorPane reportPane;

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = badGUI;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
