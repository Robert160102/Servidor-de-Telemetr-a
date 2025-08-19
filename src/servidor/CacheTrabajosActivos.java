package servidor;
/*
 * so-j10a-04
 *Robert George Petchescu 
 */
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;



public class CacheTrabajosActivos implements ssoo.telemetría.Numerable {
    // Almacenamiento concurrente de trabajos
    private final ConcurrentHashMap<String, Trabajo> cache;
   

    public CacheTrabajosActivos() {
        this.cache = new ConcurrentHashMap<>();
        
       
    }

  
    public Trabajo ComprobarTrabajo(Trabajo trabajo) {
       
        
           Trabajo t=cache.putIfAbsent(trabajo.getTelemetria().getNombre(), trabajo);/*El metodo putIfAbsent
           solo insertara el trabajo en la cache si la telemetria no existe, es decir inserta V si K no existe*/
           
           
           if(t!=null) {//Si devuelve algo que no sea un Null, es decir que ha encontrado esa K en la cache, significa que el tanbajo ya estaba registrado
        	   System.out.println("EL TRABAJO: "+t.getTelemetria().getNombre()+" DEL ENCARGO "+t.getEncargo().getTítulo()+" ENCONTRADO EN LA CACHE");
        	   return t;
           }else {
        	   System.out.println("AÑADIENDO EL TRABAJO: "+trabajo.getTelemetria().getNombre()+" DEL ENCARGO "+trabajo.getEncargo().getTítulo()+" A LA CACHE");
           synchronized (this) {
               if (cache.size() >= 40) {
                   notify();  // Despertar al HiloLiberador
               }
               return trabajo;
           }
         }
    }
    
    public void EliminarTrabajo(int LimiteInf){
    	
    	Iterator<Trabajo> i = cache.values().iterator();
    	int contador=0;
    	
    	while(i.hasNext()&& contador < LimiteInf) {
    		Trabajo trabajo = i.next();
    		synchronized(trabajo) {
    			
    			if(trabajo.getProcesado()==false) { /*En caso de que el trabajo aun no haya sido procesado lo vamos a eliminar de la cache*/
					System.out.println("ELIMINAMOS EL TRABAJO: "+trabajo.getTelemetria().getNombre()+" PERTENECIENTE AL ENCARGO: "+trabajo.getEncargo().getTítulo()+" DE LA CACHE POR DESUSO");
					i.remove();
					contador++;
    			
    			
    			}
    		}
    	}
    	
    }


	@Override
	public int numTrabajos() {
		// TODO Auto-generated method stub
		return cache.size();
	}
}