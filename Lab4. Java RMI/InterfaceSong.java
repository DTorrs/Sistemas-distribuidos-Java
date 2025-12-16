package classes;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaz común que implementarán tanto el cliente como el servidor
 * para la biblioteca de música
 */
public interface InterfaceSong extends Serializable {
    
    /**
     * Busca canciones por nombre, género y/o autor
     * @param title El título de la canción (puede ser null o vacío si no se quiere filtrar por título)
     * @param genre El género de la canción (puede ser null o vacío si no se quiere filtrar por género)
     * @param author El autor de la canción (puede ser null o vacío si no se quiere filtrar por autor)
     * @return Lista de canciones que coinciden con los criterios de búsqueda
     */
    public List<Song> searchSongs(String title, String genre, String author);
    
    /**
     * Obtiene todas las canciones disponibles en la biblioteca
     * @return Lista de todas las canciones
     */
    public List<Song> getAllSongs();
}