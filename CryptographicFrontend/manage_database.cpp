
/**
 * Token Database management tool
 * 
 * La idea es que esta herramienta es capaz de crear una nueva
 * base de datos para el Tcb o mostrar los datos guardados...
 */
#include <string>
#include <iostream>
#include <unistd.h>

using std::string;

int main (int argc, char **argv) {
    
    string filename;
    
    int o;
    while ((c = getopt(argc, argv, "f:")) != -1) {
        switch(c)
        {
            case 'f':
                filename = optarg;
                break;
            case '?':
                std::cerr << "Unknown option `-" << optopt << "'." << std::endl;
                break;
            default:
                abort();                
        }
    }
}