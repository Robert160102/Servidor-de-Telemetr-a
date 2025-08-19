package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
import ssoo.telemetría.Analizador;
import ssoo.telemetría.Telemetría;

public class HiloAnalizador implements Runnable {
	
	ColaTrabajos ct;
	private Analizador	a= new Analizador();
	
    
	
	/*Va a ser necesario pasarle al hilo analizador la cola de la cual debe
	 * sacar lo trabajos para su posterior análisis*/
	public HiloAnalizador(ColaTrabajos ct) {
		super();
		this.ct = ct;
		
		
	}

	@Override
	public void run() {
	    while(true) {
	    
	        try {
	            Trabajo t = ct.sacar(); // Obtiene el trabajo de la cola
	            if (t!= null) { 
	                synchronized (t) {
	                	System.out.println("ANALIZANDO TRABAJO: "+t.getTelemetria().getNombre()+" DEL ENCARGO: "+t.getEncargo().getTítulo());
	                    Telemetría tel = a.analizar(t.getTelemetria());
	                    t.setTelemetria(tel);
	                    System.out.println("ANALISIS DEL TRABAJO "+t.getTelemetria().getNombre()+" DEL ENCARGO: "+t.getEncargo().getTítulo()+" FINALIZADO");
	                    t.setProcesado(true);//cambiamos el estado del trabajo a procesado/analizado
	                    t.notifyAll();  // Notifica que el trabajo ha sido analizado
	                   
	                }
	            }
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}

}
