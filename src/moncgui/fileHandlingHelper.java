/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import java.io.File;
import javafx.stage.*;

/**
 *
 * @author vega
 */
public class fileHandlingHelper {

    /**
     * @param myDir The Directory where the operation is to be done
     *
     * @param extDescr Description of extension e.g. "Configuration File (.inp)"
     *
     * @param ext Extension of the file e.g. ".inp"
     *
     * @param op operation to be done e.g. "open" or "save"
     *
     * @return String 
     */
    public String fileSelection(String myDir, String extDescr, String ext,
            String op) {
        String fName = null;
        Stage thisStage = new Stage();
        FileChooser fileChooser = new FileChooser ();
        File recordsDir = new File (myDir);
        if ( !recordsDir.exists () ) {
            recordsDir.mkdirs ();
        }
        fileChooser.setInitialDirectory (recordsDir);
        FileChooser.ExtensionFilter extFilter
                = new FileChooser.ExtensionFilter (extDescr, ext);
        fileChooser.getExtensionFilters ().add (extFilter);
        fileChooser.setTitle ("GUI File Handling OPTION" + extDescr.replace (
                ext, ""));
        if ( op.contains ("open") == true ) {
            fName = fileChooser.showOpenDialog (thisStage).getPath ();
        } else if ( op.contains ("save") == true ) {
            fName = fileChooser.showSaveDialog (thisStage).getPath ();
        }
        return fName;
    }
}
