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

    String fileName1 = "Materials/VistaElements.mat";
    String fileName2 = "Materials/VistaCompound.mat";

    private MoncGUI myGUI;
    private Stage matStage;
    private Scene matScene;
    FileChooser fileChooser = new FileChooser();
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

    private Label matNameL = new Label("Material Name");
    private TextField matName = new TextField();
    private ListView<String> materials = new ListView();
    private ListView<String> matSelected = new ListView();
    private Label selMat = new Label("Select Element");
    private Button selMe = new Button("Select");
    private Button delMe = new Button("Delete");
    private Button clsMe = new Button("Clear");
    private Button acceptMe = new Button("Accept");

    private Map<String, Integer> datType = new HashMap<String, Integer>();
    private Map<String, Integer> serialNum = new HashMap<String, Integer>();
    ArrayList<Double> oneRow = new ArrayList<Double>();
    ObservableList<Double> eleData1 = FXCollections.observableArrayList(oneRow);
    ObservableList<Double> eleData2 = FXCollections.observableArrayList(oneRow);
    ObservableList<String> elename = FXCollections.observableArrayList();
    ObservableList<String> elementName = FXCollections.observableArrayList();
    ObservableList<String> eleSelectName = FXCollections.observableArrayList();
    int DLMax = 28;

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
        selMat();
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
            popupMsg.infoBox("No file selected",
                    "File Saving Procedure not successful");
            return null;
        }
    }

    public void getdataContents(File fl1, File fl2) {
        String line = null;
        String cvsSplitBy = ",";
        try {
            Reader reader = new FileReader(fl1);
            Closeable resource = reader;
            BufferedReader br = new BufferedReader(reader);
            resource = br;
            line = null;
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                oneRow.clear();
                double tstData = 0.0;
                int j = 0;
                if (!data[0].contains("#")) {
                    tstData = Double.parseDouble(data[1].toString());
                    oneRow.add(tstData);
                    tstData = Double.parseDouble(data[2].toString());
                    oneRow.add(tstData);
                    tstData = Double.parseDouble(data[3].toString());
                    oneRow.add(tstData);
                    tstData = Double.parseDouble(data[4].toString());
                    oneRow.add(tstData);
                    eleData1.addAll(oneRow);
                    // elename.add(data[0].toString());
                    elementName.add(data[0].toString());
                    datType.put(data[0].toString(), 0);
                    serialNum.put(data[0].toString(), i);
                    i++;
                }
            }
            resource.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MatGeneratorController.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MatGeneratorController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        //---------------
        try {
            Reader reader = new FileReader(fl2);
            Closeable resource = reader;
            BufferedReader br = new BufferedReader(reader);
            resource = br;
            line = null;
            int i = 0;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                int j = 0;
                double tstData = 0.0;
                oneRow.clear();
                if (!data[0].contains("#")) {
                    tstData = Double.parseDouble(data[1].toString());
                    oneRow.add(tstData);
                    tstData = Double.parseDouble(data[2].toString());
                    oneRow.add(tstData);
                    tstData = Double.parseDouble(data[3].toString());
                    oneRow.add(tstData);
                    int dLim = (int) (2 * tstData);
                    for (int jj = 0; jj < dLim; jj++) {
                        tstData = Double.parseDouble(data[4 + jj].toString());
                        oneRow.add(tstData);
                    }
                    for (int jj = dLim; jj < DLMax; jj++) {
                        tstData = 0.0;
                        oneRow.add(tstData);
                    }
                    eleData2.addAll(oneRow);
                    // elename.add(data[0].toString());
                    elementName.add(data[0].toString());
                    datType.put(data[0].toString(), 1);
                    serialNum.put(data[0].toString(), i);
                    i++;
                }
            }
            System.out.println("DLMAX = " + DLMax);
            DLMax += 3;
            resource.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MatGeneratorController.class.getName()).log(
                    Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MatGeneratorController.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

    }

    public void selMat() {
        int dLen = 0;
        File dataF1 = null;
        File dataF2 = null;
        String line = null;
        String cvsSplitBy = ",";
        File recordsDir = new File("Materials");
        if (!recordsDir.exists()) {
            recordsDir.mkdirs();
        }

        dataF1 = new File(fileName1);
        dataF2 = new File(fileName2);
        getdataContents(dataF1, dataF2);

        materials.getItems().clear();
        materials.setMaxWidth(180);
        materials.setMaxHeight(130);
        materials.setLayoutX(30);
        materials.setLayoutY(90);
        materials.setItems(elementName);
        selMat.setLayoutX(30);
        selMat.setLayoutY(60);
        selMe.setLayoutX(225);
        selMe.setLayoutY(155);
        matSelected.getItems().clear();
        matSelected.setMaxWidth(60);
        matSelected.setMaxHeight(130);
        matSelected.setLayoutX(300);
        matSelected.setLayoutY(90);
        clsMe.setLayoutX(380);
        clsMe.setLayoutY(110);
        delMe.setLayoutX(380);
        delMe.setLayoutY(140);
        acceptMe.setLayoutX(380);
        acceptMe.setLayoutY(170);
        matGenPane.getChildren().addAll(selMat, materials, selMe, matSelected,
                clsMe, delMe, acceptMe);

        selMe.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            materials.getSelectionModel().selectedItemProperty().
                    addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(
                                ObservableValue<? extends String> ov,
                                String old_val, String new_val) {
                            eleSelectName.add(new_val);
                            int IDX = materials.getSelectionModel().
                                    getSelectedIndex();
                            // double[] oneRow = new double[4];
                            oneRow.clear();
                            matSelected.setItems(eleSelectName);
                        }
                    });
            eleSelectName.clear();
        });

        clsMe.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            matSelected.getItems().clear();
        });

        delMe.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            int selIDX = -1;
            selIDX = matSelected.getSelectionModel().getSelectedIndex();
            matSelected.getItems().remove(selIDX);
        });

        acceptMe.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            eleSelectName.addAll(matSelected.getSelectionModel().
                    getSelectedItems());
            String matProps = "";
            int key = 0;
            int ind2get = DLMax;
            int iVal = 0;
            double val = 0.0;
            ObservableList<String> tempList = FXCollections.observableArrayList();
            int count = matSelected.getItems().size();
            matSelected.getSelectionModel().selectAll();
            tempList.addAll(matSelected.getItems());
            elename.addAll(tempList);
            for (int ii = 0; ii < count; ii++) {
                matProps += tempList.get(ii);
                int dataArray2Search = (Integer) datType.get(tempList.get(ii)).intValue();
                int srNum = (Integer) serialNum.get(tempList.get(ii)).intValue();
                if (dataArray2Search == 0) {
                    for (int jj = 0; jj < 4; jj++) {
                        val = eleData1.get(srNum * 4 + jj);
                        if (jj == 1 && val != 0.0) {
                            matProps += "   " + (int) val;
                        } else if (val != 0.0) {
                            matProps += "  " + val;
                        }
                    }
                } else if (dataArray2Search == 1) {
                    for (int jj = 0; jj < DLMax; jj++) {
                        val = eleData2.get(srNum * ind2get + jj);
                        if (jj == 2 && val != 0.0) {
                            matProps += "  " + (int) val;
                        } else if (jj > 2 && (jj + 1) % 2 == 0 && val != 0.0) {
                            matProps += "  " + (int) val;
                        } else if (val != 0) {
                            matProps += "  " + val;
                        }
                    }
                }
                matProps += "\n";
            }
            System.out.println("In MatGen " + elename.size());
            myGUI.setMatProp(matProps, elename);
            matGenPane.getChildren().clear();
            eleData1.clear();
            eleData2.clear();
            elename.clear();
            elementName.clear();
            eleSelectName.clear();
            oneRow.clear();
        });
    }
}
