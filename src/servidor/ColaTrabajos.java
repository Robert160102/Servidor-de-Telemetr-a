package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
import ssoo.telemetría.Numerable;



public class ColaTrabajos implements Numerable {
	
	private final Trabajo [] cola;
	private int inicio;
	private int fin;
	private final int tamaño;
	private int nelem;

	
	public ColaTrabajos(int tamaño) {
		super();
		cola= new Trabajo [tamaño];
		this.tamaño = tamaño;
		inicio=fin=0;
		nelem=0;
	
		
	}
	

	
	public synchronized  void  meter (Trabajo trabajo) throws InterruptedException {
/*puesto que es una cola fifo, el trabajo que vayamos a meter se introducirá
 * en el último sitio de nuestra cola*/
		
		while (nelem == tamaño) {
	        System.out.println("La cola está llena, esperando...");
	       wait();
	    }
	    cola[fin] = trabajo; // Inserta el elemento en la posición final.
	    System.out.println("TRABAJO "+trabajo.getTelemetria().getNombre()+" DEL ENCARGO: "+trabajo.getEncargo().getTítulo()+" AÑADIDO A LA COLA");
	    fin = (fin + 1) % tamaño; // Avanza la posición de fin.
	    nelem++;

	    notify();
	}
	
	
	public synchronized  Trabajo sacar() throws InterruptedException {
		
		 while (nelem == 0) {
		        System.out.println("No hay elementos en la cola, esperando...");
		        wait();
		    }

		    Trabajo t = cola[inicio]; // Extrae el primer elemento.
		    System.out.println("TRABAJO "+t.getTelemetria().getNombre()+" DEL ENCARGO: "+t.getEncargo().getTítulo()+" EXTRAIDO DE LA COLA");
		    inicio = (inicio + 1) % tamaño; // Avanza la posición de inicio.
		    nelem--;
		    
		    notify();
		    
		    return t;
	}



	@Override
	public int numTrabajos() {
		// TODO Auto-generated method stub
		return nelem;
	}

}
