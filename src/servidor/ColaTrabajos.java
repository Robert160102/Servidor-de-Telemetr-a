package servidor; // o ssoo.servidor según el enunciado

import ssoo.telemetría.Numerable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Implementación de una cola de trabajos concurrente y con capacidad limitada.
 * Utiliza ArrayBlockingQueue para garantizar la seguridad en entornos multihilo
 * y para manejar el bloqueo de hilos de forma automática y eficiente.
 */
public class ColaTrabajos implements Numerable {

    private final BlockingQueue<Trabajo> cola;

    public ColaTrabajos(int tamaño) {
        super();
        // 2. CONSTRUCTOR SIMPLIFICADO:
        // Solo necesitamos inicializar la ArrayBlockingQueue con su capacidad.
        this.cola = new ArrayBlockingQueue<>(tamaño);
    }
	
    // 3. MÉTODO 'meter' (o 'encolar') REFACTORIZADO:
    // No necesita 'synchronized'. El método put() es atómico y bloqueante.
    public void meter(Trabajo trabajo) throws InterruptedException {
        // Mensaje de log antes de la operación bloqueante
        System.out.println("TRABAJO " + trabajo.getTelemetria().getNombre() + " DEL ENCARGO: " + trabajo.getEncargo().getTítulo() + " A PUNTO DE ENTRAR EN LA COLA");
        
        // put() hará que el hilo espere AUTOMÁTICAMENTE si la cola está llena.
        cola.put(trabajo);
        
        System.out.println("TRABAJO AÑADIDO A LA COLA");
    }
	
    // 4. MÉTODO 'sacar' REFACTORIZADO:
    // Tampoco necesita 'synchronized'. El método take() es atómico y bloqueante.
    public Trabajo sacar() throws InterruptedException {
        // take() hará que el hilo espere AUTOMÁTICAMENTE si la cola está vacía.
        Trabajo t = cola.take(); 
        
        // Mensaje de log después de haber sacado el trabajo
        System.out.println("TRABAJO " + t.getTelemetria().getNombre() + " DEL ENCARGO: " + t.getEncargo().getTítulo() + " EXTRAIDO DE LA COLA");
        
        return t;
    }

    // 5. MÉTODO DE LA INTERFAZ 'Numerable':
    // El enunciado indica que la interfaz Numerable debe ser usada por el panel.
    // El método size() de la cola nos da el número de elementos de forma segura.
    // Nota: He cambiado el nombre de 'numTrabajos' a 'getNumElementos' que es
    // más estándar, ajústalo si tu interfaz se llama diferente.
    @Override
    public int numTrabajos() {
        return cola.size();
    }

}