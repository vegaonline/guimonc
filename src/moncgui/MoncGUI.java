/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.IOException;
import java.util.logging.*;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.javafx.JavaFXChartFactory;

public class MoncGUI extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    public Scene rootScene;
    public AnchorPane thisNode;
    public double rootWidth = 600.0;
    public double rootHeight = 480.0;

    public JavaFXChartFactory factory;
    public AWTChart chart;
    public ImageView imageView;

    //final CameraView cameraView = new CameraView();
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle ("MONC GUI :: Abhijit Bhattacharyya");
        FXMLLoader loader = new FXMLLoader (MoncGUI.class.getResource (
                "RootLayout.fxml"));
        rootLayout = (BorderPane) loader.load ();
        //Scene scene = new Scene(rootLayout);
        rootScene = new Scene (rootLayout);
        primaryStage.setScene (rootScene);
        RootLayoutController controller = loader.getController ();
        controller.setMainApp (this);
        primaryStage.show ();
    }

    /**
     * Returns the main stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public double getWidth() {
        return rootWidth;
    }

    public double getHeight() {
        return rootHeight;
    }

    public AnchorPane getMyPane() {
        return thisNode;
    }

    public void setMyPane(AnchorPane myPane) {
        this.thisNode = myPane;
    }

    public void callConfig() {
        try {
            FXMLLoader loader = new FXMLLoader (MoncGUI.class.getResource (
                    "configSet.fxml"));
            AnchorPane confPage = (AnchorPane) loader.load ();
            rootLayout.setCenter (confPage);
        } catch (IOException ex) {
            System.out.println (" Problem in loading config set");
            Logger.getLogger (MoncGUI.class.getName ()).log (Level.SEVERE, null,
                    ex);
        }
    }

    public void QUIT() {
        System.out.println ("Control came here.......");
        System.exit (0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch (args);
    }

    public void GeomStart() {
        try {
            FXMLLoader loader = new FXMLLoader (MoncGUI.class.getResource (
                    "Geom.fxml"));
            AnchorPane geomPage = (AnchorPane) loader.load ();
            rootLayout.setCenter (geomPage);
            primaryStage.setScene (rootScene);
            GeomController controller = loader.getController ();
            controller.setMainApp (this);            
            controller.setMyStage (this.primaryStage);
           // controller.setMyScene (rootScene);
        } catch (IOException ex) {
            System.out.println (" Problem in loading geometry set");
            Logger.getLogger (MoncGUI.class.getName ()).log (Level.SEVERE, null,
                    ex);
        }
    }

    public void callAnalyze() {

        try {
            FXMLLoader loader = new FXMLLoader (MoncGUI.class.getResource (
                    "Analyzer.fxml"));
            AnchorPane plotPage = (AnchorPane) loader.load ();
            rootLayout.setCenter (plotPage);
            AnalyzerController controller = loader.getController ();
            controller.setMainApp (this);
            controller.setMyScene (rootScene);
            //System.out.println("Here in Call Analyze plot page routine");
        } catch (IOException ex) {
            System.out.println (" Problem in loading Plotting set");
            Logger.getLogger (MoncGUI.class.getName ()).log (Level.SEVERE, null,
                    ex);
        }
    }
}
