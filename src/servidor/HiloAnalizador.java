package servidor;

import ssoo.telemetría.Analizador;
import ssoo.telemetría.Telemetría;
import ssoo.telemetría.panel.PanelVisualizador;

public class HiloAnalizador implements Runnable {

    private final ColaTrabajos colaDeTrabajos;
    private final Analizador analizador;

    public HiloAnalizador(ColaTrabajos colaDeTrabajos) {
        this.colaDeTrabajos = colaDeTrabajos;
        this.analizador = new Analizador();
        // Registrar analizador en el panel visualizador
        PanelVisualizador.getPanel().registrarAnalizador(this.analizador);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 1. Obtener trabajo de la cola (bloqueante si está vacía)
                Trabajo trabajo = colaDeTrabajos.sacar();

                System.out.println("[HiloAnalizador] Analizando telemetría: " +
                        trabajo.getTelemetria().getNombre());

                // 2. Analizar la telemetría
                Telemetría telemetriaAnalizada = analizador.analizar(trabajo.getTelemetria());

                // 3. Marcar el trabajo como completado
                trabajo.marcarCompletado(telemetriaAnalizada);

                System.out.println("[HiloAnalizador] Trabajo completado: " +
                        telemetriaAnalizada.getNombre());

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[HiloAnalizador] Interrumpido. Terminando...");
                break;
            }
        }
    }
}