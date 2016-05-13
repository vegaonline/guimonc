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
	double  shininess;
	double  maxShininess;
	String name;

   	public Material(){
   	}
   	public Material(String name, Vector3D color, double Ka, double Kd, double Ks, float  shinines){
		this.name=name;
		this.color=color;
		this.Ka=Ka;
		this.Kd=Kd;
		this.Ks=Ks;
		this.shininess=shininess;
   	}
   	public static Material getShinyMaterial(){
		Material shiny = new Material();
		shiny.name = "Shiny";
		shiny.color = new Vector3D(1.0, 0.0, 0.0);
		shiny.Ka = 0.4;
		shiny.Kd = 0.5;
		shiny.Ks = 0.8;
		shiny.shininess = 90.0f;
// System.out.println("NEW Material " + shiny.toString());
		return shiny;
   	}

	public void setColor(Vector3D color){
		this.color = color;
// System.out.println(this);
	}
	public Material putColor(Vector3D color){
		this.color = color;
		return this;
	}
	public void setKd(double k){
		this.Kd = k;
// System.out.println(this);
	}
	public Material putKd(double k){
		this.Kd = k;
		return this;
	}
	public double getShininess(){
		return shininess;
	}
	public void setShininess(double s){
		this.shininess= s;
	}
	public void setMaxShininess(double ms){
		this.maxShininess= ms;
	}
	public double getMaxShininess(){
		return maxShininess;
	}

	public String toString(){
		return "Name=" +name + " Color=" + color + " Ka=" + Ka + " Kd=" + Kd + " Ks=" + Ks;
	}

	//Material	Ambient	Diffuse	Specular.	R	G	B
 	//Copper		0.3		0.7		0.6			228	123	87
 	//Rubber		0.3		0.7		0.0			3	139	251
 	//Brass		0.3		0.7		0.7			228	187	34
 	//Glass		0.3		0.7		0.7			199	227	208
 	//Plastic		0.3		0.9		0.9			0	19	252
 	//Pearl		1.5		-0.5	2.0			255	138	138


}




