package classes;

import java.util.List;

/**
 * Clase que encapsula el cliente de la biblioteca de música
 */
public class Client implements InterfaceSong {
    
    private static final long serialVersionUID = 1L;
    
    JSocketClient sk;
    
    /**
     * Constructor que inicializa el cliente con una dirección y puerto específicos
     * @param address Dirección IP del servidor
     * @param port Puerto del servidor
     */
    public Client(String address, int port) {
        sk = new JSocketClient(address, port);
    }
    
    /**
     * Envía un comando al servidor
     * @param command Comando a enviar
     * @return Objeto con la respuesta del servidor
     */
    public Object operation(String command) {
        return this.sk.request(command);
    }
    
    /**
     * Cierra la conexión con el servidor
     */
    public void closeConnection() {
        this.sk.closeService();
    }

    /**
     * Implementación del método de la interfaz InterfaceSong
     * Delega la búsqueda al JSocketClient
     */
    @Override
    public List<Song> searchSongs(String title, String genre, String author) {
        return sk.searchSongs(title, genre, author);
    }

    /**
     * Implementación del método de la interfaz InterfaceSong
     * Delega la obtención de todas las canciones al JSocketClient
     */
    @Override
    public List<Song> getAllSongs() {
        return sk.getAllSongs();
    }
}