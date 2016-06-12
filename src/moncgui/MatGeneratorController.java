/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    FileChooser fileChooser = new FileChooser();
    public FileWriter fileWriter;
    String fName = null;

    private MenuBar menuBar;
    private Menu matProcess;
    private Menu matSelect;
    private MenuItem newMat;
    private MenuItem delMat;
    private MenuItem seeMat;
    private MenuItem selectIt;

    private Label matNameL = new Label("Material Name");
    private TextField matName = new TextField();

    private List<double[]> eleData = new ArrayList<double[]>();
    private List<String> elename = new ArrayList<String>();

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
        menuBar = new MenuBar();
        matProcess = new Menu("Process material");
        matSelect = new Menu("Select material for Simulation");
        newMat = new MenuItem("Create Material");
        delMat = new MenuItem("Delete Material");
        seeMat = new MenuItem("Review Material");
        selectIt = new MenuItem("Select Material");

        matProcess.getItems().addAll(newMat, delMat, seeMat);
        matSelect.getItems().add(selectIt);
        menuBar.getMenus().addAll(matProcess, matSelect);
        matGenPane.getChildren().add(menuBar);

        newMat.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ev) {
                System.out.println(" I am in newmat");
                makeNewMat();
            }
        });

        delMat.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ev) {
                deleteMat();
            }
        });

        seeMat.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ev) {
                reviewMat();
            }
        });

        selectIt.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent ev) {
                selMat();
            }
        });
    }

    public String matFileHandler(int act) {
        String fileName = null;
        File dataF;
        File recordsDir = new File("Materials");
        if (!recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        fileChooser.setInitialDirectory(recordsDir);
        fileChooser.getExtensionFilters().clear();
        FileChooser.ExtensionFilter extFilt = new FileChooser.ExtensionFilter(
                "Material Database (.mat)", "*.mat");
        fileChooser.getExtensionFilters().add(extFilt);

        while (fileName == null && act == 1) { // open file
            fileChooser.setTitle("Select Material database to Read data");
            fileName = fileChooser.showOpenDialog(matStage).getPath();
        }
        while (fileName == null && act == 2) { // save file
            fileChooser.setTitle("Select Material database to save data");
            fileName = fileChooser.showSaveDialog(matStage).getPath();
        }
        if (fileName != null) {
            //dataF = new File(fileName);
            return fileName;
        } else {
            popupMsg.infoBox("No file selected", "File Saving Procedure not successful");
            return null;
        }
    }

    public void makeNewMat() {
        popupMsg.infoBox("Material management is under development. Please have patience. Thank You", "Feature is under development");
        matGenPane.getChildren().clear();
        matGenPane.getChildren().add(menuBar);

    }

    public void deleteMat() {
        popupMsg.infoBox("Material management is under development. Please have patience. Thank You", "Feature is under development");
    }

    public void reviewMat() {
        File dataF = null;
        String fileName = null;
        fileName = matFileHandler(1);
        System.out.println(dataF);
        popupMsg.infoBox("Material management is under development. Please have patience. Thank You", "Feature is under development");
    }

    public void selMat() {
        String fileName = null;
        int dLen = 0;
        File dataF = null;
        String line = null;
        String cvsSplitBy = ",";
        fileName = matFileHandler(1); // open file
        dataF = new File(fileName);

        try {
            Reader reader = new FileReader(dataF);
            Closeable resource = reader;
            BufferedReader br = new BufferedReader(reader);
            resource = br;
            line = null;
            int i = 0;
            while ((line = br.readLine()) != null) {                
                String[] data = line.split(cvsSplitBy);
                double[] oneRow = new double[data.length];
                int j = 0;
                if (data.length >4 && data.length < 6) {
                    oneRow[0] = Double.parseDouble(data[0].toString());
                    oneRow[1] = Double.parseDouble(data[2].toString());
                    oneRow[2] = Double.parseDouble(data[3].toString());
                    oneRow[3] = Double.parseDouble(data[4].toString());                    
                    eleData.add(oneRow);
                    elename.add(data[1].toString());                    
                }
                /*
                if (line.charAt(0) != '#') {
                    String[] data = line.split(cvsSplitBy);
                    dLen = data.length;
                    System.out.println("length = " + dLen);
                    double[] oneRow = new double[dLen];
                    for (int ii = 0; ii < dLen; ii++) {
                        // oneRow[ii] = ParseDouble(data[ii]);
                        int jj = ii % 2;
                        System.out.println(data[jj].toString());
                    }
                }
*/
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(MatGeneratorController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MatGeneratorController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
