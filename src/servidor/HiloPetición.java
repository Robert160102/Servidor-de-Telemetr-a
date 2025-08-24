package servidor;

import ssoo.telemetría.Informe;
import ssoo.telemetría.Índice;
import ssoo.telemetría.estación.Petición;
import ssoo.telemetría.Telemetría;

import java.util.ArrayList;
import java.util.List;

public class HiloPetición implements Runnable {

    private final Petición peticion;
    private final int id;
    private final ColaTrabajos colaTrabajos;
    private final CacheTrabajosActivos cache; // NUEVO: referencia a la caché compartida

    public HiloPetición(Petición peticion, int id, ColaTrabajos colaTrabajos, CacheTrabajosActivos cache) {
        this.peticion = peticion;
        this.id = id;
        this.colaTrabajos = colaTrabajos;
        this.cache = cache;
    }

    @Override
    public void run() {
        String nombreEstacion = peticion.getEstación().getNombre();
        System.out.println("[HiloPeticion-" + id + "] Inicio para estación: " + nombreEstacion);

        List<Telemetría> telemetriasEncargo = peticion.getEncargo().getTelemetrías();
        if (telemetriasEncargo.isEmpty()) {
            System.out.println("[HiloPeticion-" + id + "] No hay telemetrías. Terminando.");
            return;
        }

        try {
            List<Telemetría> resultados = procesarTelemetrias(telemetriasEncargo);

            // Generar informe
            Índice indice = new Índice(resultados);
            Informe informe = new Informe("informe-" + peticion.getEncargo().getTítulo(), indice, resultados);

            peticion.getEstación().enviar(informe);
            System.out.println("[HiloPeticion-" + id + "] Informe enviado: " + informe.getTítulo());

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[HiloPeticion-" + id + "] Interrumpido. Cancelando petición.");
        }

        System.out.println("[HiloPeticion-" + id + "] Finalizado para estación: " + nombreEstacion);
    }

    /**
     * Procesa todas las telemetrías del encargo reutilizando la caché cuando sea posible. 
     */
    private List<Telemetría> procesarTelemetrias(List<Telemetría> telemetrias) throws InterruptedException {
        List<Telemetría> resultados = new ArrayList<>();

        for (Telemetría t : telemetrias) {
            Trabajo trabajo = cache.obtener(t.getNombre());

            if (trabajo == null) {
                // No existe en la caché: crear uno nuevo
                trabajo = new Trabajo(t);
                cache.insertar(t.getNombre(), trabajo);
                colaTrabajos.meter(trabajo);
                System.out.println("[HiloPeticion-" + id + "] Trabajo NUEVO encolado para " + t.getNombre());
            } else {
                System.out.println("[HiloPeticion-" + id + "] Reutilizando trabajo en caché para " + t.getNombre());
            }

            // Esperar a que el trabajo termine y recuperar la telemetría analizada
            resultados.add(trabajo.esperarYObtenerResultado());
        }

        return resultados;
    }
}