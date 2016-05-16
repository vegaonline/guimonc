/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.*;
import static java.lang.Math.*;
import java.util.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.*;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class AnalyzerController { // implements Initializable

    private Coord3d[] plotCoord;
    private Color[] plotColor;
    private List<double[]> dataPlot = new ArrayList<double[]>();
    private final ObservableList<double[]> myData = FXCollections.observableArrayList();
    private final ObservableList<myDat> myData1 = FXCollections.observableArrayList();
    private double[][] mydata2Plot;

    private double[] xDat;
    private double[] yDat;
    private double[] zDat;
    private double minX = 99999.9, maxX = -minX;
    private double minY = minX, maxY = maxX;
    private double minZ = minX, maxZ = maxX;
    private double[][] colVal;

    private int dataLen;
    private String plotTitle;

    private Scene plotScene;

    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series series;

    private Group group;
    private JavaFXChartFactory factory1;
    private AWTChartComponentFactory factory2;
    private AWTChart chart1;
    private org.jzy3d.chart.Chart chart2;
    private ImageView imageView;

    @FXML
    private Button plotFileOpener;
    @FXML
    private ComboBox<String> plotType;
    @FXML
    private ComboBox<String> plotStyle;
    @FXML
    private Button plotIt;
    @FXML
    private ComboBox<String> plotExport;
    @FXML
    private Button plotClear;
    @FXML
    private ChoiceBox<String> selectX;
    @FXML
    private ChoiceBox<String> selectY;
    @FXML
    private ChoiceBox<String> selectZ;
    @FXML
    private AnchorPane plotMainAnchorPane;
    @FXML
    private AnchorPane plotAnchor2Plot;
    @FXML
    private TextField plotChartTitle;
    @FXML
    private TableView<myDat> plotTable;
    @FXML
    private TableColumn<myDat, ?> plotTabC1;
    @FXML
    private TableColumn<myDat, ?> plotTabC2;
    @FXML
    private TableColumn<myDat, ?> plotTabC3;
    @FXML
    private TableColumn<myDat, ?> plotTabC4;
    @FXML
    private TableColumn<myDat, ?> plotTabC5;
    @FXML
    private TableColumn<myDat, ?> plotTabC6;

    private MoncGUI myGUI;
    private Stage myStage;
    private Stage baseStage;

    public void setMainApp(MoncGUI myGUI) {
        this.myGUI = myGUI;
    }

    public void setMyScene(Scene myScne) {
        this.plotScene = myScne;
    }

    /**
     * Initializes the controller class.
     */
    //@Override
    public void initialize() {//URL url, ResourceBundle rb
        // TODO
        plotScene = plotMainAnchorPane.getScene();
        fixOptionCombos(); // fix Plot Type and Styles
    }

    public static void main(String[] args) {
        //launch(args);
    }

    @FXML
    private void plotFileOpen(ActionEvent event) throws FileNotFoundException {
        openDataFile(); // Opens and read a dataFile
    }

    int getCol(String scanMe) {
        int i = 0;

        if (scanMe.contains("1") == true) {
            i = 1;
        } else if (scanMe.contains("2") == true) {
            i = 2;
        } else if (scanMe.contains("3") == true) {
            i = 3;
        } else if (scanMe.contains("4") == true) {
            i = 4;
        } else if (scanMe.contains("5") == true) {
            i = 5;
        } else if (scanMe.contains("6") == true) {
            i = 6;
        }
        return i;
    }

    //@SuppressWarnings("unchecked")
    void plot2DRoutine(int colX, int colY) {
        plotType.setValue("2D");
        xAxis = new NumberAxis();
        xAxis.setLabel("X");
        yAxis = new NumberAxis();
        yAxis.setLabel("Y");

        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        series = new XYChart.Series<>();

        int datCnt = colVal[colX].length;
        for (int i = 0; i < datCnt; i++) {
            series.getData().add(new XYChart.Data(colVal[colX][i], colVal[colY][i]));
        }
        if (plotStyle.getItems().contains("__") == true) {  // nosymbol
            lineChart.setCreateSymbols(false);
        }
        lineChart.getData().add(series);
        lineChart.setTitle(plotTitle);
        plotAnchor2Plot.getChildren().add(lineChart);
    }

    private AWTChart AWTgetChart3D(
            int colX, int colY, int colZ,
            String pltType, String toolkit) {

        int xsteps = 0, ysteps = 0;
        double x, y, z;
        double epsa = 0.25;

        int dLen = colVal[colX].length; // length of each colVal;
        plotCoord = new Coord3d[dLen];
        plotColor = new Color[dLen];
        if (pltType.contains("scatter") == true) {
            for (int i = 0; i < dLen; i++) {
                x = colVal[colX][i];
                y = colVal[colY][i];
                z = colVal[colZ][i];
                plotCoord[i] = new Coord3d(x, y, z);
                plotColor[i] = new Color((float) x, (float) y, (float) z, (float) epsa);
            }
            Scatter scatter = new Scatter(plotCoord, plotColor);
            //chart = new AWTChart(Quality.Advanced, "newt");
            chart1 = new org.jzy3d.chart.AWTChart(Quality.Advanced, toolkit);
            System.out.println(" I am in plot3dRoutine");
            chart1.getScene().add(scatter);
        }
        System.out.println(" I am returning after generating AWTgetChart3D.......");
        return chart1;
    }

    private org.jzy3d.chart.Chart getChart3D(
            int colX, int colY, int colZ,
            String pltType) {
        org.jzy3d.chart.Chart thisChart = new org.jzy3d.chart.Chart();
        int xsteps = 0, ysteps = 0;
        double x, y, z;
        double epsa = 0.25;

        int dLen = colVal[colX].length; // length of each colVal;
        plotCoord = new Coord3d[dLen];
        plotColor = new Color[dLen];
        if (pltType.contains("surf") == true) {
            for (int i = 0; i < dLen; i++) {
                x = colVal[colX][i];
                y = colVal[colY][i];
                z = colVal[colZ][i];
                maxX = max(maxX, x);
                minX = min(minX, x);
                maxY = max(maxY, y);
                minY = min(minY, y);
                maxZ = max(maxZ, z);
                minZ = min(minZ, z);
                plotCoord[i] = new Coord3d(x, y, z);
                plotColor[i] = new Color((float) x, (float) y, (float) z, (float) epsa);
            }
            xsteps = (int) ((maxX - minX) / 20.0 + 0.5);
            ysteps = (int) ((maxY - minY) / 20.0 + 0.5);
            Range rangex = new Range(minX, maxX);
            Range rangey = new Range(minY, maxY);
        } else if (pltType.contains("cont") == true) {
            for (int i = 0; i < dLen; i++) {
                x = colVal[colX][i];
                y = colVal[colY][i];
                z = colVal[colZ][i];
                maxX = max(maxX, x);
                minX = min(minX, x);
                maxY = max(maxY, y);
                minY = min(minY, y);
                maxZ = max(maxZ, z);
                minZ = min(minZ, z);
                plotCoord[i] = new Coord3d(x, y, z);
                plotColor[i] = new Color((float) x, (float) y, (float) z, (float) epsa);
            }
            xsteps = (int) ((maxX - minX) / 20.0 + 0.5);
            ysteps = (int) ((maxY - minY) / 20.0 + 0.5);
            Range rangex = new Range(minX, maxX);
            Range rangey = new Range(minY, maxY);
        } else if (pltType.contains("scat") == true) {
            for (int i = 0; i < dLen; i++) {
                x = colVal[colX][i];
                y = colVal[colY][i];
                z = colVal[colZ][i];
                plotCoord[i] = new Coord3d(x, y, z);
                plotColor[i] = new Color((float) x, (float) y, (float) z, (float) epsa);
            }
            Scatter scatter = new Scatter(plotCoord, plotColor);
            System.out.println(" I am in plot3dRoutine");
            thisChart.getScene().add(scatter);
        }
        System.out.println(" I am returning after generating getChart3D.......");
        return thisChart;
    }

    private void plot3dRoutine(int colX, int colY, int colZ, String pltType) {
        chart1 = new AWTChart();
        chart2 = new org.jzy3d.chart.Chart();
        chart2 = getChart3D(colX, colY, colZ, pltType);
        chart1 = AWTgetChart3D(colX, colY, colZ, pltType, "newt");
        plotAnchor2Plot.getChildren().contains(chart2);
    }

    @FXML
    private void plotItNow(ActionEvent ev) {
        int colX = 0, colY = 0, colZ = 0;
        boolean found2d;
        boolean found3d;
        boolean surf3d;
        boolean scat3d;
        boolean cont3d;
        found2d = plotType.getValue().contains("2D");
        found3d = plotType.getValue().contains("3D");
        surf3d = plotType.getValue().contains("Surface");
        scat3d = plotType.getValue().contains("Scatter");
        cont3d = plotType.getValue().contains("Contour");
        colX = getCol(selectX.getValue());
        colY = getCol(selectY.getValue());
        colZ = getCol(selectZ.getValue());

        plotTitle = plotChartTitle.getText();
        plotTable.setEditable(true);
        if (dataLen == 2) {
            plot2DRoutine(colX, colY);
        } else if (found2d == true) {
            plot2DRoutine(colX, colY);
        } else if (surf3d == true) {
            plot3dRoutine(colX, colY, colZ, "surf");
        } else if (scat3d == true) {
            plot3dRoutine(colX, colY, colZ, "scat");
        } else if (cont3d == true) {
            plot3dRoutine(colX, colY, colZ, "cont");
        }
    }

    @FXML
    private void plotPNGJPG(ActionEvent event) {
    }

    @FXML
    private void plotClearArea(ActionEvent event) {
        plotAnchor2Plot.getChildren().clear();
    }

    private void fixOptionCombos() {
        plotType.getItems().setAll(
                "2D ", "2D + ErrorBar",
                "3D Surface", " 3D Scatter", "3D Contour");
        plotType.setValue("2D");

        plotStyle.getItems().setAll("__", "-o-");
        plotStyle.setValue("__");

        plotExport.getItems().setAll("JPG", "PNG");
        plotExport.setValue("PNG");

        selectX.setTooltip(new Tooltip("Choose column for X Axis -- to be activated after loading the data file"));
        selectY.setTooltip(new Tooltip("Choose column for Y Axis -- to be activated after loading the data file"));
        selectZ.setTooltip(new Tooltip("Choose column for Z Axis -- to be activated after loading the data file"));
        if (plotChartTitle.getText() == null) {
            plotChartTitle.setText(" PLOT TITLE");
        }
    }

    private void configureFileChooser(
            String mytitle, String dType1, String dType2, final FileChooser fileChooser) {
        fileChooser.setTitle(mytitle);
        File recordsDir = new File("TestData");
        fileChooser.setInitialDirectory(
                //new File(System.getProperty("user.home"))
                recordsDir);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(dType1, dType2)
        );
    }

    public void openDataFile() throws FileNotFoundException {
        int i = 0, j = 0;
        int lineCount = 0;
        myData.clear();
        myData1.clear();
        dataPlot.clear();
        dataLen = 0;
        String dType1 = ".dat", dType2 = "*.dat", cvsSplitBy = ",";
        String line = "";
        FileChooser fileChooser = new FileChooser();

        configureFileChooser(
                "Select data file to Plot", dType1, dType2, fileChooser
        );
        File dataF = fileChooser.showOpenDialog(baseStage); //IT is working commented for testing
        System.out.println(dataF);

        Reader reader = new FileReader(dataF);
        Closeable resource = reader;
        try {
            BufferedReader br = new BufferedReader(reader);
            resource = br;
            line = null;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);
                setDataLen(data.length);
                dataLen = getDataLen();
                double[] oneRow = new double[getDataLen()];
                for (i = 0; i < getDataLen(); i++) {
                    oneRow[i] = Double.parseDouble(data[i]);
                    switch (dataLen) {
                        case 2:
                            myData1.add(new myDat(oneRow[0], oneRow[1], 0.0, 0.0,
                                    0.0, 0.0));
                            break;
                        case 3:
                            myData1.add(new myDat(oneRow[0], oneRow[1], oneRow[2],
                                    0.0, 0.0, 0.0));
                            break;
                        case 4:
                            myData1.add(new myDat(oneRow[0], oneRow[1], oneRow[2],
                                    oneRow[3], 0.0, 0.0));
                            break;
                        case 5:
                            myData1.add(new myDat(oneRow[0], oneRow[1], oneRow[2],
                                    oneRow[3], oneRow[4], 0.0));
                            break;
                        case 6:
                            myData1.add(new myDat(oneRow[0], oneRow[1], oneRow[2],
                                    oneRow[3], oneRow[4], oneRow[5]));
                    }
                }
                boolean add = dataPlot.add(oneRow); //dataPlot.add(oneRow);
                myData.addAll(oneRow);
            }
            resource.close();
            lineCount = dataPlot.size();
            System.out.println("j->  " + lineCount);
            //mydata2Plot = new double[lineCount][dataLen];
            colVal = new double[7][lineCount + 1];
            switch (dataLen) {
                case 2:
                    selectX.setItems(FXCollections.observableArrayList("Col 1", "Col 2"));
                    selectY.setItems(FXCollections.observableArrayList("Col 1", "Col 2"));
                    selectZ.setTooltip(new Tooltip(" NO Z value.  Data file has two columns only"));
                    selectX.setValue("Col 1");
                    selectY.setValue("Col 2");
                    selectZ.setValue("");
                    break;
                case 3:
                    selectX.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3"));
                    selectY.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3"));
                    selectZ.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3"));
                    selectX.setValue("Col 1");
                    selectY.setValue("Col 2");
                    selectZ.setValue("Col 3");
                    break;
                case 4:
                    selectX.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4"));
                    selectY.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4"));
                    selectZ.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4"));
                    selectX.setValue("Col 1");
                    selectY.setValue("Col 2");
                    selectZ.setValue("Col 3");
                    break;
                case 5:
                    selectX.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4", "Col 5"));
                    selectY.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4", "Col 5"));
                    selectZ.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4", "Col 5"));
                    selectX.setValue("Col 1");
                    selectY.setValue("Col 2");
                    selectZ.setValue("Col 3");
                    break;
                case 6:
                    selectX.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4", "Col 5", "Col 6"));
                    selectY.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4", "Col 5", "Col 6"));
                    selectZ.setItems(FXCollections.observableArrayList("Col 1", "Col 2", "Col 3", "Col 4", "Col 5", "Col 6"));
                    selectX.setValue("Col 1");
                    selectY.setValue("Col 2");
                    selectZ.setValue("Col 3");
            }
            for (i = 0; i < lineCount; i++) {
                switch (dataLen) {
                    case 2:
                        colVal[0][i] = dataPlot.get(i)[0];
                        colVal[1][i] = dataPlot.get(i)[1];
                        break;
                    case 3:
                        colVal[0][i] = dataPlot.get(i)[0];
                        colVal[1][i] = dataPlot.get(i)[1];
                        colVal[2][i] = dataPlot.get(i)[2];
                        break;
                    case 4:
                        colVal[0][i] = dataPlot.get(i)[0];
                        colVal[1][i] = dataPlot.get(i)[1];
                        colVal[2][i] = dataPlot.get(i)[2];
                        colVal[3][i] = dataPlot.get(i)[3];
                        break;
                    case 5:
                        colVal[0][i] = dataPlot.get(i)[0];
                        colVal[1][i] = dataPlot.get(i)[1];
                        colVal[2][i] = dataPlot.get(i)[2];
                        colVal[3][i] = dataPlot.get(i)[3];
                        colVal[4][i] = dataPlot.get(i)[4];
                        break;
                    case 6:
                        colVal[0][i] = dataPlot.get(i)[0];
                        colVal[1][i] = dataPlot.get(i)[1];
                        colVal[2][i] = dataPlot.get(i)[2];
                        colVal[3][i] = dataPlot.get(i)[3];
                        colVal[4][i] = dataPlot.get(i)[4];
                        colVal[5][i] = dataPlot.get(i)[5];
                        break;
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File " + dataF + " Not found...");
        } catch (IOException ex) {
            System.out.println("File " + dataF + " Not found...");
        }
    }

    /**
     * @return the dataLen
     */
    public int getDataLen() {
        return dataLen;
    }

    /**
     * @param dataLen the dataLen to set
     */
    public void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    /**
     * @return the dataPlot
     */
    public List<double[]> getDataPlot() {
        return dataPlot;
    }

    /**
     * @param dataPlot the dataPlot to set
     */
    public void setDataPlot(List<double[]> dataPlot) {
        this.dataPlot = dataPlot;

    }

    /**
     * @return the chart
     */
    //public org.jzy3d.chart.AWTChart getChart() {
    //    return chart;
    //}
    /**
     * @param chart the chart to set
     */
    //public void setChart(org.jzy3d.chart.AWTChart chart) {
    //    this.chart = chart;
    //}
    private static class myDat {

        private final SimpleDoubleProperty d1;
        private final SimpleDoubleProperty d2;
        private final SimpleDoubleProperty d3;
        private final SimpleDoubleProperty d4;
        private final SimpleDoubleProperty d5;
        private final SimpleDoubleProperty d6;

        public myDat(double d1, double d2, double d3, double d4, double d5, double d6) {
            this.d1 = new SimpleDoubleProperty(d1);
            this.d2 = new SimpleDoubleProperty(d2);
            this.d3 = new SimpleDoubleProperty(d3);
            this.d4 = new SimpleDoubleProperty(d4);
            this.d5 = new SimpleDoubleProperty(d5);
            this.d6 = new SimpleDoubleProperty(d6);
        }

        public double getD1() {
            return d1.get();
        }

        public void setD1(double D1) {
            d1.set(D1);
        }

        public double getD2() {
            return d2.get();
        }

        public void setD2(double D2) {
            d2.set(D2);
        }

        public double getD3() {
            return d3.get();
        }

        public void setD3(double D3) {
            d3.set(D3);
        }

        public double getD4() {
            return d4.get();
        }

        public void setD4(double D4) {
            d4.set(D4);
        }

        public double getD5() {
            return d5.get();
        }

        public void setD5(double D5) {
            d5.set(D5);
        }

        public double getD6() {
            return d6.get();
        }

        public void setD6(double D6) {
            d6.set(D6);
        }
    }
}
