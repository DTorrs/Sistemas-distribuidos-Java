package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para manejar la comunicación mediante sockets en el cliente
 */
public class JSocketClient implements InterfaceSong {
    
    private static final long serialVersionUID = 1L;
    
    private InetAddress address;
    private int port;
    private Socket clientSk;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private boolean connected;
    private Object responseData;

    /**
     * Constructor para inicializar el cliente con una dirección y puerto específicos
     * @param address Dirección IP del servidor
     * @param port Puerto del servidor
     */
    public JSocketClient(String address, int port) {
        try {
            this.port = port;
            this.address = InetAddress.getByName(address);
            this.oos = null;
            this.ois = null;
            this.connected = false;
            this.responseData = null;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establece la conexión con el servidor y envía un comando
     * @param command Comando a enviar
     * @return Objeto con la respuesta del servidor
     */
    public Object request(String command) {
        try {
            if (!connected) {
                this.clientSk = new Socket(this.address, this.port);
                this.oos = new ObjectOutputStream(this.clientSk.getOutputStream());
                this.oos.flush();
                this.ois = new ObjectInputStream(this.clientSk.getInputStream());
                this.connected = true;
                System.out.println("\n [Client]: Conexión exitosa.");
            }
            
            send(command);
            
            try {
                this.responseData = this.ois.readObject();
                return this.responseData;
            } catch (Exception e) {
                System.out.println("\n [Client]: No se puede recibir la data.");
                e.printStackTrace();
                return null;
            }
            
        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        }
    }

    /**
     * Envía un comando al servidor
     * @param command Comando a enviar
     */
    private void send(String command) {
        try {
            this.oos.writeObject(command);
            this.oos.flush();
            System.out.println("\n [Client]: Comando enviado: " + command);
        } catch (Exception e) {
            System.out.println("\n [Client]: No se puede enviar la data.");
            e.printStackTrace();
        }
    }
    
    /**
     * Cierra la conexión con el servidor
     */
    public void closeService() {
        try {
            if (this.ois != null) this.ois.close();
            if (this.oos != null) this.oos.close();
            if (this.clientSk != null) this.clientSk.close();
            this.connected = false;
            System.out.println("\n [Client]: Conexión terminada.");
        } catch (Exception e) {
            System.out.println("\n [Client]: No se puede cerrar la conexión.");
        }
    }

    /**
     * Implementación del método de la interfaz InterfaceSong
     * Busca canciones en el servidor según los criterios
     */
    @Override
    public List<Song> searchSongs(String title, String genre, String author) {
        String command = "SEARCH|" + (title != null ? title : "") + "|" + 
                         (genre != null ? genre : "") + "|" + 
                         (author != null ? author : "");
        
        Object response = request(command);
        
        if (response instanceof List<?>) {
            List<?> list = (List<?>) response;
            if (!list.isEmpty() && list.get(0) instanceof Song) {
                @SuppressWarnings("unchecked")
                List<Song> songList = (List<Song>) list;
                return songList;
            }
        }
        
        return new ArrayList<>();
    }

    /**
     * Implementación del método de la interfaz InterfaceSong
     * Obtiene todas las canciones del servidor
     */
    @Override
    public List<Song> getAllSongs() {
        Object response = request("GET_ALL");
        
        if (response instanceof List<?>) {
            List<?> list = (List<?>) response;
            if (!list.isEmpty() && list.get(0) instanceof Song) {
                @SuppressWarnings("unchecked")
                List<Song> songList = (List<Song>) list;
                return songList;
            }
        }
        
        return new ArrayList<>();
    }
}