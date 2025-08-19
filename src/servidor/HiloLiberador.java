package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
public class HiloLiberador implements Runnable {
	
	private CacheTrabajosActivos cache;
	private int LimiteInferior = 38;
	private int LimiteSuperior = 48;
	
	public HiloLiberador(CacheTrabajosActivos cache) {
		this.cache = cache;
	}
	
	@Override
	public void run() {
		while (true) {
			synchronized (cache) {
				// Espera hasta que la caché supere el límite superior
				while (cache.numTrabajos() <= LimiteSuperior) {
					try {
						cache.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						System.err.println("HiloLiberador interrumpido.");
						return;
					}
				}
			}
			
			// Eliminar trabajos hasta alcanzar el límite inferior
			cache.EliminarTrabajo(LimiteInferior);
		}
	}
}
