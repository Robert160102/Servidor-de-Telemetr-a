package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
import java.io.IOException;

import ssoo.telemetría.panel.PanelVisualizador;

public class ServidorTelemetría {

	public static void main(String[] args) throws InterruptedException {
		
		ColaTrabajos ct = new ColaTrabajos(30);
		PanelVisualizador.getPanel().registrarColaTrabajos(ct);
		CacheTrabajosActivos cache= new CacheTrabajosActivos();
		PanelVisualizador.getPanel().registrarCache(cache);
		
		
		System.out.println("SERVIDOR DE TELEMETRIAS");
		try {
			HiloRecepción	HRecepción = new HiloRecepción(ct,cache);
			HiloLiberador HLiberador= new HiloLiberador(cache);
			final Thread hilo = new Thread (HRecepción);
			final Thread hiloL = new Thread(HLiberador);
			hilo.start();
			hiloL.start();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i =0; i <Runtime.getRuntime().availableProcessors(); i++) {
			HiloAnalizador HAnalizador = new HiloAnalizador(ct);
			final Thread hiloA= new Thread(HAnalizador);
			System.out.println("[Hilo de análisis " + i + "] Empiezo");
			hiloA.start();
		}
		
	}

}
