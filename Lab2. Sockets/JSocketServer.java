package classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase para manejar la comunicación mediante sockets en el servidor
 */
public class JSocketServer implements InterfaceSong {
    
    private static final long serialVersionUID = 1L;
    
    private int port;
    private ServerSocket serverSk;
    private Socket clientSk;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private List<Song> songDatabase;

    /**
     * Constructor para inicializar el servidor con un puerto específico
     * @param port Puerto en el que escuchará el servidor
     */
    public JSocketServer(int port) {
        try {
            this.port = port;
            this.serverSk = new ServerSocket(port, 100);
            this.oos = null;
            this.ois = null;
            initializeDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Inicializa la base de datos de canciones
     */
    private void initializeDatabase() {
        songDatabase = new ArrayList<>();
        
        // Añadir algunas canciones de ejemplo
        songDatabase.add(new Song("Bohemian Rhapsody", "Rock", "Queen", "Inglés", 1975));
        songDatabase.add(new Song("Despacito", "Reggaeton", "Luis Fonsi", "Español", 2017));
        songDatabase.add(new Song("Billie Jean", "Pop", "Michael Jackson", "Inglés", 1983));
        songDatabase.add(new Song("La Bicicleta", "Pop Latino", "Carlos Vives y Shakira", "Español", 2016));
        songDatabase.add(new Song("Imagine", "Pop", "John Lennon", "Inglés", 1971));
        songDatabase.add(new Song("Bailando", "Pop Latino", "Enrique Iglesias", "Español", 2014));
        songDatabase.add(new Song("Thriller", "Pop", "Michael Jackson", "Inglés", 1982));
        songDatabase.add(new Song("Volare", "Pop", "Gipsy Kings", "Italiano", 1989));
        songDatabase.add(new Song("Shape of You", "Pop", "Ed Sheeran", "Inglés", 2017));
        songDatabase.add(new Song("Vivir Mi Vida", "Salsa", "Marc Anthony", "Español", 2013));
    }

    /**
     * Método para escuchar y procesar las peticiones de los clientes
     */
    public void listening() {
        try {
            System.out.println("\n [Server]: Esperando conexiones en puerto " + this.port);
            this.clientSk = this.serverSk.accept();
            this.oos = new ObjectOutputStream(this.clientSk.getOutputStream());
            this.oos.flush();
            this.ois = new ObjectInputStream(this.clientSk.getInputStream());
            System.out.println("\n [Server]: Conexión exitosa.");
            
            while(true) {
                try {
                    // Recibir comando del cliente
                    String command = (String) this.ois.readObject();
                    
                    if (command == null) {
                        closeService();
                        break;
                    } else {
                        processCommand(command);
                    }
                } catch (Exception e) {
                    System.out.println("\n [Server]: No se puede recibir la data.");
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Procesa los comandos recibidos del cliente
     * @param command Comando recibido
     */
    private void processCommand(String command) {
        try {
            System.out.println("\n [Server]: Comando recibido: " + command);
            
            if (command.startsWith("SEARCH")) {
                // Formato esperado: "SEARCH|título|género|autor"
                String[] parts = command.split("\\|");
                String title = parts.length > 1 ? parts[1] : "";
                String genre = parts.length > 2 ? parts[2] : "";
                String author = parts.length > 3 ? parts[3] : "";
                
                List<Song> results = searchSongs(title, genre, author);
                sendSongList(results);
                System.out.println("\n [Server]: Enviados " + results.size() + " resultados de búsqueda.");
                
            } else if (command.equals("GET_ALL")) {
                List<Song> allSongs = getAllSongs();
                sendSongList(allSongs);
                System.out.println("\n [Server]: Enviadas todas las canciones: " + allSongs.size());
            } else {
                send("Comando desconocido: " + command);
            }
        } catch (Exception e) {
            System.out.println("\n [Server]: Error al procesar comando: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Envía una lista de canciones al cliente
     */
    private void sendSongList(List<Song> songs) {
        try {
            this.oos.writeObject(songs);
            this.oos.flush();
        } catch (Exception e) {
            System.out.println("\n [Server]: No se puede enviar la lista de canciones.");
            e.printStackTrace();
        }
    }

    /**
     * Cierra todas las conexiones
     */
    private void closeService() {
        try {
            this.ois.close();
            this.oos.close();
            this.clientSk.close();
            this.serverSk.close();
            System.out.println("\n [Server]: Conexión terminada.");
        } catch (Exception e) {
            System.out.println("\n [Server]: No se puede cerrar la conexión.");
        }
    }
    
    /**
     * Envía un mensaje de texto al cliente
     */
    private void send(String data) {
        try {
            this.oos.writeObject("[Server]: " + data);
            this.oos.flush();
        } catch (Exception e) {
            System.out.println("\n [Server]: No se puede enviar la data.");
        }
    }
    
    /**
     * Getter para el puerto
     */
    public int getPort() {
        return port;
    }

    /**
     * Setter para el puerto
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Implementación del método de la interfaz InterfaceSong
     */
    @Override
    public List<Song> searchSongs(String title, String genre, String author) {
        List<Song> results = new ArrayList<>();
        
        for (Song song : songDatabase) {
            boolean titleMatch = title == null || title.isEmpty() || 
                                song.getTitle().toLowerCase().contains(title.toLowerCase());
            boolean genreMatch = genre == null || genre.isEmpty() || 
                                song.getGenre().toLowerCase().contains(genre.toLowerCase());
            boolean authorMatch = author == null || author.isEmpty() || 
                                song.getAuthor().toLowerCase().contains(author.toLowerCase());
            
            if (titleMatch && genreMatch && authorMatch) {
                results.add(song);
            }
        }
        
        return results;
    }

    /**
     * Implementación del método de la interfaz InterfaceSong
     */
    @Override
    public List<Song> getAllSongs() {
        return new ArrayList<>(songDatabase);
    }
}