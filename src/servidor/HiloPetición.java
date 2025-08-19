package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
import ssoo.telemetría.Informe;
import ssoo.telemetría.Índice;
import ssoo.telemetría.estación.Petición;

public class HiloPetición implements Runnable {
    
    // Atributos
    private Petición peticion;
    private int id;
    private ColaTrabajos fifo;
    private Trabajo TrabajoNuevo;
    private Trabajo TrabajoEnCache;
    private CacheTrabajosActivos cache;
    

    // Constructor
    public HiloPetición(Petición peticion, int id, ColaTrabajos fifo, CacheTrabajosActivos cache) {
        this.peticion = peticion;
        this.id = id;
        this.fifo = fifo;
        this.cache=cache;
    }

    @Override
    public void run() {
        System.out.println("[Hilo de petición " + id + "] Empiezo");
        System.out.println("[Hilo de petición " + id + "] He recibido una petición: " +
                           "Nombre de estación: " + peticion.getEstación().getNombre() +
                           ", Telemetrías del encargo: " + peticion.getEncargo().getTelemetrías().size());
        System.out.println("[Hilo de petición " + id + "] Encargo: " + peticion.getEncargo().getTítulo());
      

        for (int i = 0; i < peticion.getEncargo().getTelemetrías().size(); i++) {//El hilo de peticion va a crear tantos trabajos como telemetrias haya en el encargo de la peticion
             TrabajoNuevo = new Trabajo(peticion.getEncargo().getTelemetrías().get(i), peticion.getEncargo());
             TrabajoEnCache = cache.ComprobarTrabajo(TrabajoNuevo);
         
            try {
            	if(TrabajoEnCache.equals(TrabajoNuevo)) {//si es igual significa que es nuevo y lo metemos en la cola de trabajos
            			fifo.meter(TrabajoEnCache);
            		
            	}
 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[Hilo de petición " + id + "] Interrumpido al meter trabajo.");
                return;
            }

            // Esperar hasta que el trabajo sea analizado
            synchronized (TrabajoEnCache) {
            	while(!TrabajoEnCache.getProcesado()){ //mientras mi trabajo no est procesado
            		try {
                        System.out.println("[Hilo de petición " + id + "] Esperando a que se procese el trabajo...");
                        TrabajoEnCache.wait();
                        
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("[Hilo de petición " + id + "] Interrumpido durante la espera.");
                        return;
                    }

                }
                peticion.getEncargo().getTelemetrías().set(i, TrabajoEnCache.getTelemetria());  //Remmplazamos la telemetria antigua del trabajo por la ya analizada
                
            	}
            		
           }
        
     // Crear el informe una vez procesado
        Índice indice = new Índice(peticion.getEncargo().getTelemetrías());
        Informe informe = new Informe("informe-" + peticion.getEncargo().getTítulo(), indice, peticion.getEncargo().getTelemetrías());
        peticion.getEstación().enviar(informe);
        System.out.println("[Hilo de petición " + id + "] Informe enviado: " + informe.getTítulo());
        System.out.println("[Hilo de petición " + id + "] Procesamiento completado para " + peticion.getEstación().getNombre());
    }
    
}
