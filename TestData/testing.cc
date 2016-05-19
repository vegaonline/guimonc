#include <iostream>
#include <cmath>
#include <ctime>
#include <fstream>

using namespace std;
int main() {
    long int dlen = 100000;
    double x, y, z, s;

    ofstream myfile;
    myfile.open("Testing3D.dat");
    srand((unsigned int)time(0));
    for (long int  i = 0; i < dlen; ++i) {
        x = 2.0 * (rand()/(float)RAND_MAX);
        y = 2.0 * (rand()/(float)RAND_MAX);
        s = rand()/(float)RAND_MAX;
        if ( s > 0.75) {
            x *= -1;
            y *= -1;
        } else if (s > 0.5 && s <= 0.75 ) {
            x *= -1;
        } else if ( s > 0.25 && s <= 0.5) {
            y *= -1;
        }
        double f1 = cos(2.0 * (x*x + y*y));
        double f2 = cos(x/4.0)*sin(y);
        double f3 = x*x + y*y;
        z = f1 * f2 * exp(-f3);
        myfile << x << "," << y << "," << z << std::endl;
    }
    myfile.close();
    return 0;
}
