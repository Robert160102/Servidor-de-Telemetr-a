package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
import java.io.IOException;


import ssoo.telemetría.estación.Petición;
import ssoo.telemetría.estación.Receptor;

public class HiloRecepción implements Runnable {
    
    // Atributos
    private Receptor receptor; //Receptor es la calse que se encarga de crear los objetos que realmente reciben las peticiones
    private ColaTrabajos ct;
   private CacheTrabajosActivos cache;
    
    // Constructor
    public HiloRecepción(ColaTrabajos ct , CacheTrabajosActivos cache) throws IOException {
        this.receptor = new Receptor(); //Creamos el receptor de encargos y activamos la escucha
        this.ct=ct;
        this.cache=cache;

    }

    @Override
    public void run() {
    	
    	Petición peticion;
    	
    	
        for(int i=0; true; i++) { 
            try {
                //el receptor recibe la peticion
                peticion = receptor.recibirPetición();
                // Creamos un HiloPeticion para procesar la peticion recibida
                HiloPetición hiloPeticion = new HiloPetición(peticion,i,ct,cache);                
                Thread hilo = new Thread(hiloPeticion);
                //Iniciamos el hilo de peticion
                hilo.start();
                
                
            } catch (IOException e) {
                System.out.println("Error en las comunicaciones: " + e.getMessage());
            }
        }
    }
}
