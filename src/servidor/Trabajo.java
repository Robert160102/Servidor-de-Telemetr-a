package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
import ssoo.telemetría.Encargo;
import ssoo.telemetría.Telemetría;

public class Trabajo {
	

	private Telemetría telemetria;
	private Encargo eng;
	private boolean procesado;
	
	public Trabajo(Telemetría telemetria, Encargo eng) {
		super();
		this.procesado=false; //por defecto un trabajo NO esta procesado/analizado
		this.telemetria = telemetria;
		this.eng=eng;
	}
	
	public boolean getProcesado() {
		return procesado; 
	}
	
	public void setProcesado(boolean estado) {
		procesado=estado;
	}

	public Telemetría getTelemetria() {
		return telemetria;
	}
	public void setTelemetria(Telemetría telemetria) {
		this.telemetria = telemetria;
	}
	
	public Encargo getEncargo() {
		return eng;
		
	}
	
	
	
	
	
	
	

	

}
