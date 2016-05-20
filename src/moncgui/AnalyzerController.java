/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.*;
import static java.lang.Math.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.*;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.*;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * FXML Controller class
 *
 * @author vega
 */
public class AnalyzerController implements Initializable {

    private MoncGUI myGUI;
    private Stage plotStage;
    private Stage newStage;
    private Scene newScene;
    private ImageView imgView = new ImageView ();

    private List<double[]> dataPlot = new ArrayList<double[]> ();
    private final ObservableList<myDat> myData1 = FXCollections.
            observableArrayList ();

    private double[][] colVal;
    private int dataLen = 0;

    private String plotTitle = null;

    private double maxx = -99999999.99, minx = -maxx, maxy = maxx, miny = minx,
            maxz = maxx, minz = minx, meanx = maxx, meany = maxy, maxmax = 0, minmin
            = 0;
    int colX = 0, colY = 0, colZ = 0;
    private StackPane plotAnchor2Plot = new StackPane ();
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private LineChart<Number, Number> lineChart;
    private AWTChart chart;
    private XYChart.Series series;
    private Coord3d[] plotCoord;
    private Color[] plotColor;
    private List<Coord3d> plotCoordList;
    private TableView<myDat> plotTable;
    

    @FXML
    private AnchorPane plotMainAnchorPane;
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
    private TextField plotChartTitle;

    public void setMainApp(MoncGUI badGUI) {
        this.myGUI = badGUI;
    }

    public void setMyStage(Stage stage) {
        this.plotStage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fixOptionCombos ();
    }

    //**** Fix option for plotting using combobox
    private void fixOptionCombos() {
        plotType.getItems ().setAll ("2D ", "2D + ErrorBar", "3D Surface",
                " 3D Scatter", "3D Contour");
        plotType.setValue ("2D");

        plotStyle.getItems ().setAll ("__", "-o-");
        plotStyle.setValue ("__");

        plotExport.getItems ().setAll ("JPG", "PNG");
        plotExport.setValue ("PNG");

        selectX.setTooltip (new Tooltip (
                "Choose column for X Axis -- to be activated after loading the data file"));
        selectY.setTooltip (new Tooltip (
                "Choose column for Y Axis -- to be activated after loading the data file"));
        selectZ.setTooltip (new Tooltip (
                "Choose column for Z Axis -- to be activated after loading the data file"));
    }

    //****  Data File open and read related files and classes ****
    @FXML
    private void plotFileOpen(ActionEvent event) {
        try {
            openDataFile ();
        } catch (FileNotFoundException ex) {
            Logger.getLogger (AnalyzerController.class.getName ()).
                    log (Level.SEVERE, null, ex);
        }
    }

    private int getDataLen() {
        return dataLen;
    }

    private void setDataLen(int dataLen) {
        this.dataLen = dataLen;
    }

    private void configureFileChooser(
            String mytitle, String dType1, String dType2,
            final FileChooser fileChooser) {
        fileChooser.setTitle (mytitle);
        File recordsDir = new File ("TestData");
        fileChooser.setInitialDirectory (recordsDir);  //new File(System.getProperty("user.home"))
        fileChooser.getExtensionFilters ().addAll (
                new FileChooser.ExtensionFilter (dType1, dType2)
        );
    }

    private double ParseDouble(String strNumber) {
        if ( strNumber != null && strNumber.length () > 0 ) {
            try {
                return Double.parseDouble (strNumber);
            } catch (Exception e) {
                return -99999.99;   // or some value to mark this field is wrong. or make a function validates field first ...
            }
        } else {
            return 0;
        }
    }

    public void openDataFile() throws FileNotFoundException {
        int i = 0, j = 0;
        int lineCount = 0;
        myData1.clear ();
        dataPlot.clear ();
        dataLen = 0;
        colX = 0;
        colY = 0;
        colZ = 0;
        String dType1 = ".dat", dType2 = "*.dat", cvsSplitBy = ",";
        String line = "";
        FileChooser fileChooser = new FileChooser ();

        configureFileChooser (
                "Select data file to Plot", dType1, dType2, fileChooser
        );
        File dataF = fileChooser.showOpenDialog (plotStage.getScene ().
                getWindow ()); //IT is working commented for testing

        if ( dataF == null ) {
            System.out.println ("No file has been selected.....");
        } else {
            Reader reader = new FileReader (dataF);
            Closeable resource = reader;
            try {
                BufferedReader br = new BufferedReader (reader);
                resource = br;
                line = null;
                while ((line = br.readLine ()) != null) {
                    String[] data = line.split (cvsSplitBy);
                    setDataLen (data.length);
                    dataLen = getDataLen ();
                    double[] oneRow = new double[getDataLen ()];
                    for ( i = 0; i < getDataLen (); i++ ) {
                        oneRow[i] = ParseDouble (data[i]);
                    }
                    switch (dataLen) {
                        case 2: myData1.add (
                                    new myDat (oneRow[0], oneRow[1], 0.0,
                                            0.0, 0.0, 0.0));
                            break;
                        case 3: myData1.add (
                                    new myDat (oneRow[0], oneRow[1],
                                            oneRow[2], 0.0, 0.0, 0.0));
                            break;
                        case 4:
                            myData1.add (new myDat (oneRow[0], oneRow[1],
                                    oneRow[2], oneRow[3], 0.0, 0.0));
                            break;
                        case 5:
                            myData1.add (new myDat (oneRow[0], oneRow[1],
                                    oneRow[2], oneRow[3], oneRow[4], 0.0));
                            break;
                        case 6: myData1.add (
                                    new myDat (oneRow[0], oneRow[1],
                                            oneRow[2], oneRow[3], oneRow[4],
                                            oneRow[5]));
                    }
                    boolean add = dataPlot.add (oneRow); //dataPlot.add(oneRow);
                    if ( dataLen == 1 ) {
                        System.out.println (
                                "Please delete extra blank line in the data file probably at the end.");
                        return;
                    }
                }
                resource.close ();
                lineCount = dataPlot.size ();
                colVal = new double[7][lineCount + 1];
                switch (dataLen) {
                    case 2:
                        selectX.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2"));
                        selectY.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2"));
                        selectZ.setTooltip (new Tooltip (
                                " NO Z value.  Data file has two columns only"));
                        selectX.setValue ("Col 1");
                        selectY.setValue ("Col 2");
                        selectZ.setValue ("");
                        break;
                    case 3:
                        selectX.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3"));
                        selectY.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3"));
                        selectZ.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3"));
                        selectX.setValue ("Col 1");
                        selectY.setValue ("Col 2");
                        selectZ.setValue ("Col 3");
                        break;
                    case 4:
                        selectX.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4"));
                        selectY.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4"));
                        selectZ.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4"));
                        selectX.setValue ("Col 1");
                        selectY.setValue ("Col 2");
                        selectZ.setValue ("Col 3");
                        break;
                    case 5:
                        selectX.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4", "Col 5"));
                        selectY.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4", "Col 5"));
                        selectZ.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4", "Col 5"));
                        selectX.setValue ("Col 1");
                        selectY.setValue ("Col 2");
                        selectZ.setValue ("Col 3");
                        break;
                    case 6:
                        selectX.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4", "Col 5", "Col 6"));
                        selectY.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4", "Col 5", "Col 6"));
                        selectZ.setItems (FXCollections.
                                observableArrayList ("Col 1", "Col 2", "Col 3",
                                        "Col 4", "Col 5", "Col 6"));
                        selectX.setValue ("Col 1");
                        selectY.setValue ("Col 2");
                        selectZ.setValue ("Col 3");
                }
                for ( i = 0; i < lineCount; i++ ) {
                    switch (dataLen) {
                        case 2:
                            colVal[0][i] = dataPlot.get (i)[0];
                            colVal[1][i] = dataPlot.get (i)[1];
                            break;
                        case 3:
                            colVal[0][i] = dataPlot.get (i)[0];
                            colVal[1][i] = dataPlot.get (i)[1];
                            colVal[2][i] = dataPlot.get (i)[2];
                            break;
                        case 4:
                            colVal[0][i] = dataPlot.get (i)[0];
                            colVal[1][i] = dataPlot.get (i)[1];
                            colVal[2][i] = dataPlot.get (i)[2];
                            colVal[3][i] = dataPlot.get (i)[3];
                            break;
                        case 5:
                            colVal[0][i] = dataPlot.get (i)[0];
                            colVal[1][i] = dataPlot.get (i)[1];
                            colVal[2][i] = dataPlot.get (i)[2];
                            colVal[3][i] = dataPlot.get (i)[3];
                            colVal[4][i] = dataPlot.get (i)[4];
                            break;
                        case 6:
                            colVal[0][i] = dataPlot.get (i)[0];
                            colVal[1][i] = dataPlot.get (i)[1];
                            colVal[2][i] = dataPlot.get (i)[2];
                            colVal[3][i] = dataPlot.get (i)[3];
                            colVal[4][i] = dataPlot.get (i)[4];
                            colVal[5][i] = dataPlot.get (i)[5];
                            break;
                    }
                }
                if ( !dataPlot.isEmpty () ) {
                    dataPlot.clear ();
                }
                System.out.
                        println ("Data file " + dataF.getName () + " loaded.");
                plotTitle = dataF.getName ();
            } catch (FileNotFoundException ex) {
                System.out.println ("File " + dataF + " Not found...");
            } catch (IOException ex) {
                System.out.println ("File " + dataF + " Not found...");
            }
        }
    }

    public static class myDat {

        private final SimpleDoubleProperty d1;
        private final SimpleDoubleProperty d2;
        private final SimpleDoubleProperty d3;
        private final SimpleDoubleProperty d4;
        private final SimpleDoubleProperty d5;
        private final SimpleDoubleProperty d6;

        public myDat(double d1, double d2, double d3, double d4, double d5,
                double d6) {
            this.d1 = new SimpleDoubleProperty (d1);
            this.d2 = new SimpleDoubleProperty (d2);
            this.d3 = new SimpleDoubleProperty (d3);
            this.d4 = new SimpleDoubleProperty (d4);
            this.d5 = new SimpleDoubleProperty (d5);
            this.d6 = new SimpleDoubleProperty (d6);
        }

        public double getD1() {
            return d1.get ();
        }

        public void setD1(double D1) {
            d1.set (D1);
        }

        public double getD2() {
            return d2.get ();
        }

        public void setD2(double D2) {
            d2.set (D2);
        }

        public double getD3() {
            return d3.get ();
        }

        public void setD3(double D3) {
            d3.set (D3);
        }

        public double getD4() {
            return d4.get ();
        }

        public void setD4(double D4) {
            d4.set (D4);
        }

        public double getD5() {
            return d5.get ();
        }

        public void setD5(double D5) {
            d5.set (D5);
        }

        public double getD6() {
            return d6.get ();
        }

        public void setD6(double D6) {
            d6.set (D6);
        }
    }

    //**** Select working columns of the multiple column datafile ****
    int getCol(String scanMe) {
        int i = 0;
        if ( scanMe.contains ("1") == true ) {
            i = 1;
        } else if ( scanMe.contains ("2") == true ) {
            i = 2;
        } else if ( scanMe.contains ("3") == true ) {
            i = 3;
        } else if ( scanMe.contains ("4") == true ) {
            i = 4;
        } else if ( scanMe.contains ("5") == true ) {
            i = 5;
        } else if ( scanMe.contains ("6") == true ) {
            i = 6;
        }
        return i;
    }

    //**** Plotting data ****
    private void getMaxMinData(int cx, int cy, int cz) {
        double x, y, z;
        int datCnt = myData1.size ();
        for ( int ii = 0; ii < datCnt; ii++ ) {
            if ( cx >= 0 ) {
                x = colVal[cx][ii];
            } else {
                x = minx;
            }
            if ( cy >= 0 ) {
                y = colVal[cy][ii];
            } else {
                y = miny;
            }
            if ( cz >= 0 ) {
                z = colVal[cz][ii];
            } else {
                z = minz;
            }
            maxx = max (maxx, x);
            minx = min (minx, x);
            maxy = max (maxy, y);
            miny = min (miny, y);
            maxz = max (maxz, z);
            minz = min (minz, z);
        }
        meanx = 0.5 * (maxx - minx);
        meany = 0.5 * (maxy - miny);
    }

    void plot2DRoutine(int cx, int cy) {
        DecimalFormat newFormat = new DecimalFormat("0.###E0");        
        plotAnchor2Plot.getChildren().removeAll(lineChart, xAxis, yAxis);        
        plotMainAnchorPane.getChildren ().remove (plotAnchor2Plot);
        plotTable = new TableView ();
        plotTable.setEditable (true);
        plotTable.setMaxHeight (250);
        plotTable.setMaxWidth (190);
        plotTable.setLayoutX (10);
        plotTable.setLayoutY (185);
        plotTable.autosize ();

        TableColumn col1 = new TableColumn<myDat, Double> ("C1");
        col1.setMinWidth (85);
        col1.setCellValueFactory (
                new PropertyValueFactory<myDat, Double> ("d1"));
        TableColumn col2 = new TableColumn<myDat, Double> ("C2");
        col2.setMinWidth (85);
        col2.setCellValueFactory (
                new PropertyValueFactory<myDat, Double> ("d2"));
        TableColumn col3 = new TableColumn<myDat, Double> ("C3");
        col3.setMinWidth (85);
        col3.setCellValueFactory (
                new PropertyValueFactory<myDat, Double> ("d3"));
        TableColumn col4 = new TableColumn<myDat, Double> ("C4");
        col4.setMinWidth (85);
        col4.setCellValueFactory (
                new PropertyValueFactory<myDat, Double> ("d4"));
        TableColumn col5 = new TableColumn<myDat, Double> ("C5");
        col5.setMinWidth (85);
        col5.setCellValueFactory (
                new PropertyValueFactory<myDat, Double> ("d5"));
        TableColumn col6 = new TableColumn<myDat, Double> ("C6");
        col6.setMinWidth (85);
        col6.setCellValueFactory (
                new PropertyValueFactory<myDat, Double> ("d6"));
        TableColumn col7 = new TableColumn<myDat, Double> ("C7");
        col7.setMinWidth (85);
        col7.setCellValueFactory (
                new PropertyValueFactory<myDat, Double> ("d7"));
        int datCnt = myData1.size ();

        plotTable.setItems (myData1);
        plotTable.getColumns ().
                addAll (col1, col2, col3, col4, col5, col6, col7);

        plotType.setValue ("2D");
        xAxis = new NumberAxis ();
        xAxis.setLabel ("X * "+newFormat.format(meanx));
        yAxis = new NumberAxis ();
        yAxis.setLabel ("Y * "+newFormat.format(meany));
        lineChart = new LineChart<Number, Number> (xAxis, yAxis);
        series = new XYChart.Series<> ();

        for ( int i = 0; i < datCnt; i++ ) {
            series.getData ().add (new XYChart.Data (colVal[cx][i]/meanx,
                    colVal[cy][i]/meany)); // CHANGE AXES SCALE and UNDO THIS DIVISION
            // THIS IS ILLEGAL AND WRONG:: Abhijit Bhattacharyya
        }
        if ( plotStyle.getItems ().contains ("__") == true ) {  // nosymbol
            lineChart.setCreateSymbols (false);
        } else {
            lineChart.setCreateSymbols (true);
        }                
        lineChart.setScaleShape (true);
        lineChart.getData ().add (series);
        lineChart.setTitle (plotTitle+" (col "+(cx+1)+"  :  col "+(cy+1)+") ");

        plotAnchor2Plot.setLayoutX (210);
        plotAnchor2Plot.setLayoutY (20);

        plotMainAnchorPane.getChildren ().add (plotAnchor2Plot);
        plotAnchor2Plot.setMaxSize (360, 280);
        plotAnchor2Plot.getChildren ().add (lineChart);
        plotMainAnchorPane.getChildren ().add (plotTable);
    }

    private void plot3dRoutine(int colX, int colY, int colZ, String pltType) {
        newStage = new Stage ();
        newStage.setTitle (
                "Data Analyzer :: Abhijit Bhattacharyya EMAIL: vega@barc.gov.in");
        newStage.setResizable (true);
        JavaFXChartFactory factory = new JavaFXChartFactory ();
        //AWTChart chart = getChart3D (factory, colX, colY, colZ, pltType,
        chart = getChart3D (factory, colX, colY, colZ, pltType,
                "offscreen");  // Use offscreen.  "newt" gives error for factory
        imgView = factory.bindImageView (chart);

        StackPane newPane = new StackPane ();
        newScene = new Scene (newPane, 600, 470);
        newStage.setScene (newScene);
        newStage.show ();
        newPane.getChildren ().add (imgView);
        factory.addSceneSizeChangedListener (chart, newScene);
    }

    public static ColorMapper coloring(List<Coord3d> coords) {
        Range zRange = Coord3d.getZRange (coords);
        ColorMapper coloring = new ColorMapper (new ColorMapRainbow (),
                zRange.getMin (), zRange.getMax (), new Color (1, 1, 1, .5f));
        return coloring;
    }

    private AWTChart getChart3D(JavaFXChartFactory fac1, int cx, int cy, int cz,
            String pltType, String toolkit) {;
        double x, y, z;
        float a = 0.25f;
        int steps = 20;
        int dlen = colVal[cx].length;
        plotCoordList = new ArrayList<Coord3d> (dlen);
        plotCoord = new Coord3d[dlen];
        plotColor = new Color[dlen];
        for ( int i = 0; i < dlen; i++ ) {
            x = colVal[cx][i];
            y = colVal[cy][i];
            z = colVal[cz][i];
            plotCoord[i] = new Coord3d (x, y, z);
            plotColor[i] = new Color ((float) x, (float) y, (float) z, a);
        }
        Range rngx = new Range ((float) minx, (float) maxx);
        Range rngy = new Range ((float) miny, (float) maxy);

        //ColorMapper coloring = coloring (plotCoordList);
        if ( pltType.contains ("scatter") == true ) {
            //ScatterVBO drawable = new ScatterVBO (new VBOBuilderListCoord3d (plotCoordList, coloring));
            Scatter scatter = new Scatter (plotCoord, plotColor);
            Quality quality = Quality.Nicest;
            AWTChart chart = (AWTChart) fac1.newChart (quality, toolkit);
            chart.getScene ().getGraph ().add (scatter);
            return chart;
        } else if ( pltType.contains ("surface") == true ) {
            //Shape surface = Builder.buildOrthonormal (rngx, steps, rngy, steps);
            //surface.setColorMapper (new ColorMapper (new ColorMapRainbow (), surface.getBounds ().getZmin (), surface.getBounds ().getZmax (), new Color (1, 1, 1, .5f)));
            //surface.setFaceDisplayed (true);
            //surface.setWireframeDisplayed (false);

            // Create a chart
            AWTChart chart = (AWTChart) fac1.
                    newChart (Quality.Advanced, toolkit);
            //chart.getScene ().getGraph ().add (surface);
            return chart;
        } else {
            AWTChart chart1 = null;
            return chart1;
        }
    }

    @FXML
    private void plotItNow(ActionEvent event) {
        if ( dataLen == 0 ) {
            System.out.println ("No Data Selected......");
            return;
        }

        boolean found2d;
        boolean found3d;
        boolean surf3d;
        boolean scat3d;
        boolean cont3d;

        found2d = plotType.getValue ().contains ("2D");
        found3d = plotType.getValue ().contains ("3D");
        surf3d = plotType.getValue ().contains ("Surface");
        scat3d = plotType.getValue ().contains ("Scatter");
        cont3d = plotType.getValue ().contains ("Contour");
        colX = getCol (selectX.getValue ());
        colY = getCol (selectY.getValue ());
        colZ = getCol (selectZ.getValue ());
        --colX;
        --colY;
        --colZ;
        System.out.println (colX + "  " + colY + "   " + colZ);
        getMaxMinData (colX, colY, colZ);
        if ( dataLen == 2 ) {
            plot2DRoutine (colX, colY);
        } else if ( found2d == true ) {
            plot2DRoutine (colX, colY);
        } else if ( surf3d == true ) {
            plot3dRoutine (colX, colY, colZ, "surface");
        } else if ( scat3d == true ) {
            plot3dRoutine (colX, colY, colZ, "scatter");
        } else if ( cont3d == true ) {
            plot3dRoutine (colX, colY, colZ, "contour");
        }
        colX = 0;
        colY = 0;
        colZ = 0;
    }

    @FXML
    private void plotPNGJPG(ActionEvent event) {

    }

    @FXML
    private void plotClearArea(ActionEvent event) {
        plotMainAnchorPane.getChildren ().clear();
        fixOptionCombos();
    }

}
