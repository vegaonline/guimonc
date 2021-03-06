// store of material characteristics
package moncgui;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author vega
 */
public class Material {

//	Vector3D Ke; //emmissive color,
//	Vector3D Ka; //ambient reflectance
//	Vector3D Kd; //diffuse color,
//	Vector3D Ks; //specular color
    int matID;
    private static Map<String, Double> matCol = new HashMap<String, Double>();
    // matID must conform to MONC code. Periodic table related elements 
    // should come from MONC. Compounds are indexed from ID::300
    // ID 1   onwards ---> periodic table elements
    // ID 200 onwards ---> non metals
    // ID 600 onwards ---> metals
    // ID 1111 onwards ---> ODD items

    Vector3D color; //emmissive color,
    double Ka; //ambient reflectance
    double Kd; //diffuse reflectance,
    double Ks; //specular reflectance
    double shininess;
    double maxShininess;
    static double colA, colB, colC;
    static String colorA, colorB, colorC;
    // These properties are to be taken from the MONC code 
    // material dictionary for usefulness.
    // Let us put a Material ID number that should conform to the material
    // ID of the MONC code
    // I feel placing the property here is REQUIRED  :: DISCUSS HARPHOOL

    /*
    double atNum;
    double massNum;
    double density;
     */
    String name;
    

    public Material() {
    }

    public Material(String name, Vector3D color, double Ka, double Kd, double Ks,
            float shinines) {
        this.name = name;
        this.color = color;
        this.Ka = Ka;
        this.Kd = Kd;
        this.Ks = Ks;
        this.shininess = shininess;
        
    }

    public static Material matProcess(String matN) {
        Material matName = new Material();
        double colF = 1.0 / 26.0;
        matCol.put("A", colF);
        matCol.put("B", 2 * colF);
        matCol.put("C", 3 * colF);
        matCol.put("D", 4 * colF);
        matCol.put("E", 5 * colF);
        matCol.put("F", 6 * colF);
        matCol.put("G", 7 * colF);
        matCol.put("H", 8 * colF);
        matCol.put("I", 9 * colF);
        matCol.put("J", 10 * colF);
        matCol.put("K", 11 * colF);
        matCol.put("L", 12 * colF);
        matCol.put("M", 13 * colF);
        matCol.put("N", 14 * colF);
        matCol.put("N", 15 * colF);
        matCol.put("O", 16 * colF);
        matCol.put("P", 17 * colF);
        matCol.put("Q", 18 * colF);
        matCol.put("R", 19 * colF);
        matCol.put("S", 20 * colF);
        matCol.put("T", 21 * colF);
        matCol.put("U", 21 * colF);
        matCol.put("V", 22 * colF);
        matCol.put("W", 23 * colF);
        matCol.put("X", 24 * colF);
        matCol.put("Y", 25 * colF);
        matCol.put("Z", 26 * colF);
        matCol.put("",1.0);
        matName.name = matN;
        matN = matN.toUpperCase();
        colorA = matN.substring(0, 1);
        colorB = (matN.length() >= 2 ? matN.substring(1, 2) : "");
        colorC = (matN.length() >= 3 ? matN.substring(2, 3) : "");
        colA = matCol.get(colorA).doubleValue();
        colB = matCol.get(colorB).doubleValue();
        colC = matCol.get(colorC).doubleValue();
        matName.color = new Vector3D(colA, colB, colC);
        new java.util.Random();
        matName.Ka = Math.random();
        matName.Kd = Math.random();
        matName.Ks = Math.random();
        matName.shininess = (1.0 + 254.0 * Math.random());
        return matName;
    }

    public static Material getShinyMaterial() {
        Material shiny = new Material();
        shiny.matID = 1111;
        shiny.name = "Shiny";
        shiny.color = new Vector3D(1.0, 0.0, 0.0);
        shiny.Ka = 0.4;
        shiny.Kd = 0.5;
        shiny.Ks = 0.8;
        shiny.shininess = 90.0f;
// System.out.println("NEW Material " + shiny.toString());
        return shiny;
    }

    public static Material Copper() {
        Material copper = new Material();
        copper.matID = 600;
        copper.name = "Copper";
        copper.color = new Vector3D(0.8672, 0.3047, 0.1016);
        copper.Ka = 0.3;
        copper.Kd = 0.7;
        copper.Ks = 0.6;
        copper.shininess = 90.0f;
        return copper;
    }

    public static Material Rubber() {
        Material Rubber = new Material();
        Rubber.matID = 200;
        Rubber.name = "Rubber";
        Rubber.color = new Vector3D(0.4336, 0.3750, 0.3516);
        Rubber.Ka = 0.3;
        Rubber.Kd = 0.7;
        Rubber.Ks = 0.0;
        Rubber.shininess = 10.0f;
        return Rubber;
    }

    public static Material Brass() {
        Material Brass = new Material();
        Brass.matID = 601;
        Brass.name = "Brass";
        Brass.color = new Vector3D(0.8789, 0.7148, 0.2695);
        Brass.Ka = 0.3;
        Brass.Kd = 0.7;
        Brass.Ks = 0.7;
        Brass.shininess = 90.0f;
        return Brass;
    }

    public static Material Glass() {
        Material Glass = new Material();
        Glass.matID = 201;
        Glass.name = "Glass";
        Glass.color = new Vector3D(0.8203, 0.9023, 0.9180);
        Glass.Ka = 0.3;
        Glass.Kd = 0.7;
        Glass.Ks = 0.7;
        Glass.shininess = 90.0f;
        return Glass;
    }

    public static Material Plastic() {
        Material Plastic = new Material();
        Plastic.matID = 202;
        Plastic.name = "Plastic";
        Plastic.color = new Vector3D(0.7305, 0.7383, 0.6914);
        Plastic.Ka = 0.3;
        Plastic.Kd = 0.9;
        Plastic.Ks = 0.9;
        Plastic.shininess = 10.0f;
        return Plastic;
    }

    public static Material Hydrogen() {
        Material Hydrogen = new Material();
        Hydrogen.matID = 1;
        Hydrogen.name = "Hydrogen";
        Hydrogen.color = new Vector3D(0.94, 0.82, 0.92);
        Hydrogen.Ka = 0.1;
        Hydrogen.Kd = 0.6;
        Hydrogen.Ks = 0.5;
        Hydrogen.shininess = 4.0f;
        return Hydrogen;
    }

    public static Material Deuterium() {
        Material Deuterium = new Material();
        Deuterium.matID = 2;
        Deuterium.name = "Deuterium";
        Deuterium.color = new Vector3D(0.94, 0.82, 0.92);
        Deuterium.Ka = 0.1;
        Deuterium.Kd = 0.6;
        Deuterium.Ks = 0.5;
        Deuterium.shininess = 4.0f;
        return Deuterium;
    }

    public void setColor(Vector3D color) {
        this.color = color;
        System.out.println(this);
    }

    public Material putColor(Vector3D color) {
        this.color = color;
        return this;
    }

    public void setKd(double k) {
        this.Kd = k;
// System.out.println(this);
    }

    public Material putKd(double k) {
        this.Kd = k;
        return this;
    }

    public double getShininess() {
        return shininess;
    }

    public void setShininess(double s) {
        this.shininess = s;
    }

    public void setMaxShininess(double ms) {
        this.maxShininess = ms;
    }

    public double getMaxShininess() {
        return maxShininess;
    }

    public String toString() {
        return "Name=" + name + " Color=" + color + " Ka=" + Ka + " Kd=" + Kd
                + " Ks=" + Ks;
    }

}
