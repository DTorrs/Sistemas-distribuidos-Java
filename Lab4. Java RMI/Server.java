package classes;

/**
 * Clase que encapsula el servidor de la biblioteca de música
 */
public class Server {
    
    private JSocketServer sk;
    
    /**
     * Constructor que inicializa el servidor con un puerto específico
     * @param port Puerto en el que escuchará el servidor
     */
    public Server(int port) {
        this.sk = new JSocketServer(port);
    }
    
    /**
     * Inicia el servidor para escuchar conexiones
     */
    public void run() {
        sk.listening();
    }
}