/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moncgui;

import javax.swing.JOptionPane;

/**
 *
 * @author vega
 */
public class popupMsg {

    /**
     * @param String infoMesg : Message to be displayed
     *
     * @param String titleBar : The titleBar
     *
     */
    public static void infoBox(String infoMesg, String titleBar) {
        {
            JOptionPane.showMessageDialog (null, infoMesg, "InfoBox: " +
                    titleBar, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
