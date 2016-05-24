// store of material characteristics
package moncgui;

public class Material {

//	Vector3D Ke; //emmissive color,
//	Vector3D Ka; //ambient reflectance
//	Vector3D Kd; //diffuse color,
//	Vector3D Ks; //specular color
    Vector3D color; //emmissive color,
    double Ka; //ambient reflectance
    double Kd; //diffuse reflectance,
    double Ks; //specular reflectance
    double shininess;
    double maxShininess;
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

    public static Material getShinyMaterial() {
        Material shiny = new Material ();
        shiny.name = "Shiny";
        shiny.color = new Vector3D (1.0, 0.0, 0.0);
        shiny.Ka = 0.4;
        shiny.Kd = 0.5;
        shiny.Ks = 0.8;
        shiny.shininess = 90.0f;
// System.out.println("NEW Material " + shiny.toString());
        return shiny;
    }

    public static Material Copper() {
        Material copper = new Material ();
        copper.name = "Copper";
        copper.color = new Vector3D (0.8672, 0.3047, 0.1016);
        copper.Ka = 0.3;
        copper.Kd = 0.7;
        copper.Ks = 0.6;
        copper.shininess = 90.0f;
        return copper;
    }

    public static Material Rubber() {
        Material Rubber = new Material ();
        Rubber.name = "Rubber";
        Rubber.color = new Vector3D (0.4336, 0.3750, 0.3516);
        Rubber.Ka = 0.3;
        Rubber.Kd = 0.7;
        Rubber.Ks = 0.0;
        Rubber.shininess = 10.0f;
        return Rubber;
    }

    public static Material Brass() {
        Material Brass = new Material ();
        Brass.name = "Brass";
        Brass.color = new Vector3D (0.8789, 0.7148, 0.2695);
        Brass.Ka = 0.3;
        Brass.Kd = 0.7;
        Brass.Ks = 0.7;
        Brass.shininess = 90.0f;
        return Brass;
    }

    public static Material Glass() {
        Material Glass = new Material ();
        Glass.name = "Glass";
        Glass.color = new Vector3D (0.8203, 0.9023, 0.9180);
        Glass.Ka = 0.3;
        Glass.Kd = 0.7;
        Glass.Ks = 0.7;
        Glass.shininess = 90.0f;
        return Glass;
    }

    public static Material Plastic() {
        Material Glass = new Material ();
        Glass.name = "Glass";
        Glass.color = new Vector3D (0.7305, 0.7383, 0.6914);
        Glass.Ka = 0.3;
        Glass.Kd = 0.9;
        Glass.Ks = 0.9;
        Glass.shininess = 10.0f;
        return Glass;
    }

    public void setColor(Vector3D color) {
        this.color = color;
        System.out.println (this);
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
        return "Name=" + name + " Color=" + color + " Ka=" + Ka + " Kd=" + Kd +
                " Ks=" + Ks;
    }

}
