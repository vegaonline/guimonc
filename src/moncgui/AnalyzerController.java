/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.*;
import static java.lang.Math.*;
import java.net.URL;
import java.util.*;
import java.util.logging.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.colors.*;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.javafx.JavaFXChartFactory;
import org.jzy3d.maths.*;
import org.jzy3d.plot3d.primitives.vbo.builders.VBOBuilderListCoord3d;
import org.jzy3d.plot3d.primitives.vbo.drawable.ScatterVBO;
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
    private CameraView camV = new CameraView ();
    private ImageView imgView = new ImageView ();

    private List<double[]> dataPlot = new ArrayList<double[]> ();
    private final ObservableList<double[]> myData = FXCollections.
            observableArrayList ();
    private final ObservableList<myDat> myData1 = FXCollections.
            observableArrayList ();

    private double[][] colVal;
    private int dataLen = 0;

    private String plotTitle = null;

    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series series;
    private Coord3d[] plotCoord;
    private Color[] plotColor;
    private List<Coord3d> plotCoordList;

    @FXML
    private AnchorPane plotMainAnchorPane;
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
    private AnchorPane plotAnchor2Plot;
    @FXML
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
        if ( plotChartTitle.getText () == null ) {
            plotChartTitle.setText (" PLOT TITLE");
        }
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

    public void openDataFile() throws FileNotFoundException {
        int i = 0, j = 0;
        int lineCount = 0;
        myData.clear ();
        myData1.clear ();
        dataPlot.clear ();
        dataLen = 0;
        String dType1 = ".dat", dType2 = "*.dat", cvsSplitBy = ",";
        String line = "";
        FileChooser fileChooser = new FileChooser ();

        configureFileChooser (
                "Select data file to Plot", dType1, dType2, fileChooser
        );
        File dataF = fileChooser.showOpenDialog (plotStage); //IT is working commented for testing
        System.out.println (dataF);

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
                    oneRow[i] = Double.parseDouble (data[i]);
                    switch (dataLen) {
                        case 2:
                            myData1.add (new myDat (oneRow[0], oneRow[1], 0.0,
                                    0.0,
                                    0.0, 0.0));
                            break;
                        case 3:
                            myData1.add (new myDat (oneRow[0], oneRow[1],
                                    oneRow[2],
                                    0.0, 0.0, 0.0));
                            break;
                        case 4:
                            myData1.add (new myDat (oneRow[0], oneRow[1],
                                    oneRow[2],
                                    oneRow[3], 0.0, 0.0));
                            break;
                        case 5:
                            myData1.add (new myDat (oneRow[0], oneRow[1],
                                    oneRow[2],
                                    oneRow[3], oneRow[4], 0.0));
                            break;
                        case 6:
                            myData1.add (new myDat (oneRow[0], oneRow[1],
                                    oneRow[2],
                                    oneRow[3], oneRow[4], oneRow[5]));
                    }
                }
                boolean add = dataPlot.add (oneRow); //dataPlot.add(oneRow);
                myData.addAll (oneRow);
            }
            resource.close ();
            lineCount = dataPlot.size ();
            System.out.println ("j->  " + lineCount);
            //mydata2Plot = new double[lineCount][dataLen];
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
        } catch (FileNotFoundException ex) {
            System.out.println ("File " + dataF + " Not found...");
        } catch (IOException ex) {
            System.out.println ("File " + dataF + " Not found...");
        }
    }

    private static class myDat {

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
    void plot2DRoutine(int colX, int colY) {
        plotType.setValue ("2D");
        xAxis = new NumberAxis ();
        xAxis.setLabel ("X");
        yAxis = new NumberAxis ();
        yAxis.setLabel ("Y");

        lineChart = new LineChart<Number, Number> (xAxis, yAxis);
        series = new XYChart.Series<> ();

        int datCnt = colVal[colX].length;
        for ( int i = 0; i < datCnt; i++ ) {
            series.getData ().add (new XYChart.Data (colVal[colX][i],
                    colVal[colY][i]));
        }
        if ( plotStyle.getItems ().contains ("__") == true ) {  // nosymbol
            lineChart.setCreateSymbols (false);
        }
        lineChart.getData ().add (series);
        lineChart.setTitle (plotTitle);
        plotAnchor2Plot.getChildren ().add (lineChart);
    }

    private void lightSetting(Context3D context) {
        context.lighting = new Lighting3D ();
        context.lighting.add (Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 1.5, new Vector3D (1, 0.8, 0.6));
        context.lighting.add (Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 1.0, new Vector3D (-1, -0.8, 0.6));
        context.lighting.add (Lighting3D.Type.DIFFUSE,
                Lighting3D.Source.PARALLEL, 0.5, new Vector3D (0, -0.2, -0.8));
        // context.showLights    //-----> These generates yellow lights seem to be axes
        // context.setShowBorders(true);  
        // context.setShowTexts(true);  // vertex texts are shown with drawing. Don't use 
    }

    private void plot3dRoutine(int colX, int colY, int colZ, String pltType) {
        newStage = new Stage ();
        newStage.setTitle (
                "Data Analyzer :: Abhijit Bhattacharyya EMAIL: vega@barc.gov.in");
        //Scene newScene = new Scene (camV, 800, 700, true);
        //newScene.setCamera (new PerspectiveCamera ());
        //Context3D context = Context3D.getInstance (camV);
        //lightSetting (context);
        newStage.setResizable (true);
        System.out.println (" In Plot3dRoutine after resizeable");
        JavaFXChartFactory factory = new JavaFXChartFactory ();
        System.out.println (" In Plot3dRoutine after factory decl");

        AWTChart chart1 = getChart3D (factory, colX, colY, colZ, pltType,"offscreen");  // newt
        System.out.println (" In Plot3dRoutine after factory AWTCHART");

        imgView = factory.bindImageView (chart1);
        System.out.println (" In Plot3dRoutine after factory ImageView");
        //camV.add (chart1);
        //camV.frameCam(newStage, newScene);
        StackPane newPane = new StackPane ();
        newPane.getChildren ().add (imgView);
        newScene = new Scene (newPane, 800, 700, true);
        //MouseHandler mouseHandler = new MouseHandler (vegaScene, camV);
        //KeyHandler keyHandler = new KeyHandler (vegaStage, vegaScene, camV);
        newStage.setScene (newScene);
        newStage.show ();
        newStage.setWidth (700);
        newStage.setHeight (650);
        //factory.addSceneSizeChangedListener (chart1, newScene);
    }

    private AWTChart getChart3D(JavaFXChartFactory fac1, int cx, int cy, int cz,
            String pltType, String toolkit) {
        int xstp = 0, ystp = 0;
        double x = -10000.0, y = x, z = x;
        double epsa = 0.25;
        double maxx = -100000.0, minx = -maxx, maxy = maxx, miny = minx, maxz
                = maxx, minz = minx;
        int dlen = colVal[cx].length;
        plotCoordList = new ArrayList<Coord3d>(dlen);
        plotCoord = new Coord3d[dlen];
        plotColor = new Color[dlen];
        if ( pltType.contains ("scatter") == true ) {
            System.out.println ("We are in scatter...   dlen =  "+dlen);
            for ( int i = 0; i < dlen; i++ ) {
                x = colVal[cx][i];
                y = colVal[cy][i];
                z = colVal[cz][i];
                maxx = max (maxx, x);
                minx = min (minx, x);
                maxy = max (maxy, y);
                miny = min (miny, y);
                maxz = max (maxz, z);
                minz = min (minz, z);
                plotCoord[i] = new Coord3d (x, y, z);
                plotCoordList.add (new Coord3d (x, y, z));
                plotColor[i] = new Color ((float) x, (float) y, (float) z,
                        (float) epsa);
            }
            System.out.println(plotCoordList.get (dlen-5));
            Range rngX = new Range ((float) minx, (float) maxx);
            Range rngY = new Range ((float) miny, (float) maxy);
            Range rngZ = new Range ((float) minz, (float) maxz);
            xstp = (int) ((maxx - minx) / 20.0 + 0.5);
            ystp = (int) ((maxy - miny) / 20.0 + 0.5);
            ColorMapper coloring = coloring (plotCoordList, rngZ);
            ScatterVBO drawable = new ScatterVBO (new VBOBuilderListCoord3d (
                    plotCoordList, coloring));
            System.out.println("drawable was prepared");
            Quality quality = Quality.Advanced;
            AWTChart chart1 = (AWTChart) fac1.newChart (quality, toolkit);
            System.out.println (chart1.getScene ().getClass ().getName ());
            System.out.println (" Drawable Barycentre----> " + drawable.getBarycentre ());
            chart1.getScene ().getGraph ().add (drawable);
            return chart1;
        } else {
            AWTChart chart1 = null;
            return chart1;
        }
    }

    public static ColorMapper coloring(List<Coord3d> coords, Range zRng) {
        // Range zrange = Coord3d.getZRange (coords);
        ColorMapper coloring = new ColorMapper (new ColorMapRainbow (),
                zRng.getMin (), zRng.getMax (), new Color (1, 1, 1, .5f));
        return coloring;
    }

    @FXML
    private void plotItNow(ActionEvent event) {
        int colX = 0, colY = 0, colZ = 0;
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

        plotTitle = plotChartTitle.getText ();
        plotTable.setEditable (true);
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
    }

    @FXML
    private void plotPNGJPG(ActionEvent event) {
    }

    @FXML
    private void plotClearArea(ActionEvent event) {
    }

}
