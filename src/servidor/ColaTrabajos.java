package servidor;

import ssoo.telemetría.Numerable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Cola de trabajos concurrente y con capacidad limitada.
 * Basada en ArrayBlockingQueue para garantizar seguridad multihilo
 * y bloqueo automático cuando está llena o vacía.
 */
public class ColaTrabajos implements Numerable {

    private final BlockingQueue<Trabajo> cola;

    public ColaTrabajos(int capacidad) {
        this.cola = new ArrayBlockingQueue<>(capacidad);
    }

    /**
     * Inserta un trabajo en la cola. 
     * Si está llena, el hilo se bloquea hasta que haya espacio.
     */
    public void meter(Trabajo trabajo) throws InterruptedException {
        cola.put(trabajo);
        System.out.println("[ColaTrabajos] Trabajo " + trabajo.getTelemetria().getNombre() + " añadido a la cola.");
    }

    /**
     * Extrae un trabajo de la cola. 
     * Si está vacía, el hilo se bloquea hasta que haya un trabajo disponible.
     */
    public Trabajo sacar() throws InterruptedException {
        Trabajo t = cola.take();
        System.out.println("[ColaTrabajos] Trabajo " + t.getTelemetria().getNombre()+ " extraído de la cola.");
        return t;
    }

    /**
     * Devuelve el número de trabajos actualmente en la cola.
     */
    @Override
    public int numTrabajos() {
        return cola.size();
    }
}
