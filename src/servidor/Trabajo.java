package servidor;

import ssoo.telemetría.Telemetría;

public class Trabajo {

    private final Telemetría telemetriaOriginal;
    private Telemetría telemetriaAnalizada;
    private boolean completado = false;

    public Trabajo(Telemetría telemetriaOriginal) {
        this.telemetriaOriginal = telemetriaOriginal;
    }

    /**
     * Devuelve la telemetría original (antes del análisis).
     */
    public Telemetría getTelemetria() {
        return telemetriaOriginal;
    }

    /**
     * Método llamado por el HiloAnalizador cuando termina el análisis.
     * Marca el trabajo como completado y despierta a todos los hilos
     * que estaban esperando.
     */
    public synchronized void marcarCompletado(Telemetría analizada) {
        if (!completado) {
            this.telemetriaAnalizada = analizada;
            this.completado = true;
            notifyAll();
        }
    }

    /**
     * Método llamado por los HiloPeticion.
     * Si el trabajo ya está completo devuelve la telemetría analizada.
     * Si no, espera hasta que el análisis se complete.
     */
    public synchronized Telemetría esperarYObtenerResultado() throws InterruptedException {
        while (!completado) {
            wait();
        }
        return telemetriaAnalizada;
    }

    /**
     * Indica si el trabajo ya está finalizado.
     */
    public synchronized boolean isCompletado() {
        return completado;
    }
}