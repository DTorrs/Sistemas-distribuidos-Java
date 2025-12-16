package main;

import classes.Server;

/**
 * Clase principal para iniciar el servidor de la biblioteca de música
 */
public class MusicLibraryServer {
    
    public static void main(String[] args) {
        System.out.println("Iniciando servidor de biblioteca de música...");
        Server server = new Server(1802);
        server.run();
    }
}