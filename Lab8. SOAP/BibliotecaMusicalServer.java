package com.biblioteca.musical.server;

import com.biblioteca.musical.service.BibliotecaMusicalServiceImpl;
import javax.xml.ws.Endpoint;

public class BibliotecaMusicalServer {
    
    private static final String URL = "http://localhost:8080/BibliotecaMusical";
    
    public static void main(String[] args) {
        try {
            BibliotecaMusicalServiceImpl implementacion = new BibliotecaMusicalServiceImpl();
            
            Endpoint endpoint = Endpoint.publish(URL, implementacion);
            
            if (endpoint.isPublished()) {
                System.out.println("==============================================");
                System.out.println("  SERVIDOR BIBLIOTECA MUSICAL SOAP INICIADO");
                System.out.println("==============================================");
                System.out.println("URL del servicio: " + URL);
                System.out.println("WSDL disponible en: " + URL + "?wsdl");
                System.out.println("");
                System.out.println("Presiona Enter para detener el servidor...");
                System.out.println("==============================================");
                
                System.in.read();
                
                endpoint.stop();
                System.out.println("Servidor detenido correctamente.");
            } else {
                System.err.println("Error: No se pudo iniciar el servidor SOAP");
            }
            
        } catch (Exception e) {
            System.err.println("Error al iniciar el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}