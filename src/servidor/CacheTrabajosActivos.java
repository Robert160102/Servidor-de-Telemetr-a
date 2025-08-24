package servidor;

import java.util.concurrent.ConcurrentHashMap;
import ssoo.telemetría.Numerable;

/**
 * Caché de trabajos activos. 
 * Almacena trabajos en curso o ya completados, 
 * evitando reprocesar la misma telemetría varias veces.
 */
public class CacheTrabajosActivos implements Numerable {

    private final ConcurrentHashMap<String, Trabajo> cache;

    public CacheTrabajosActivos() {
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Devuelve un trabajo de la caché si existe, o null si no está.
     */
    public Trabajo obtener(String clave) {
        return cache.get(clave);
    }

    /**
     * Inserta un trabajo en la caché si no existía ya.
     * Devuelve el trabajo existente en caso de colisión, 
     * o el nuevo trabajo si se insertó con éxito.
     */
    public Trabajo insertar(String clave, Trabajo nuevo) {
        Trabajo existente = cache.putIfAbsent(clave, nuevo);
        if (existente != null) {
            System.out.println("[Cache] Trabajo ya existente para " + clave + ". Reutilizando.");
            return existente;
        } else {
            System.out.println("[Cache] Trabajo insertado para " + clave);
            return nuevo;
        }
    }

    /**
     * Elimina un trabajo de la caché (pensado para Fase 4 - liberador).
     */
    public void eliminar(String clave) {
        cache.remove(clave);
        System.out.println("[Cache] Trabajo eliminado para " + clave);
    }

    /**
     * Devuelve el número de trabajos activos en la caché.
     */
    @Override
    public int numTrabajos() {
        return cache.size();
    }
}
