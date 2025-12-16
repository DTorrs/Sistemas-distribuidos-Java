package com.ejemplo.WebToPdfConverter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Verificar argumentos
        if (args.length < 2) {
            System.out.println("Uso: java -jar WebToPdfConverter.jar <num_hilos> <directorio_salida>");
            System.out.println("Ejemplo: java -jar WebToPdfConverter.jar 4 ./pdfs");
            return;
        }
        
        int numThreads = Integer.parseInt(args[0]);
        String outputDir = args[1];
        
        // Lista predefinida de 32 URLs
        List<String> urls = Arrays.asList(
            "https://www.wikipedia.org",
            "https://www.bbc.com",
            "https://www.cnn.com",
            "https://www.nytimes.com",
            "https://www.theguardian.com",
            "https://www.washingtonpost.com",
            "https://www.nasa.gov",
            "https://www.weather.gov",
            "https://www.nationalgeographic.com",
            "https://www.who.int",
            "https://www.un.org",
            "https://www.mit.edu",
            "https://www.stanford.edu",
            "https://www.harvard.edu",
            "https://developer.mozilla.org",
            "https://docs.oracle.com/en/java",
            "https://www.python.org",
            "https://nodejs.org",
            "https://www.linux.org",
            "https://www.apache.org",
            "https://www.gnu.org",
            "https://www.wikihow.com",
            "https://www.ted.com",
            "https://www.spotify.com",
            "https://www.imdb.com",
            "https://www.rottentomatoes.com",
            "https://www.webmd.com",
            "https://www.mayoclinic.org",
            "https://www.chess.com",
            "https://www.worldometers.info",
            "https://archive.org",
            "https://www.gutenberg.org"
        );
        
        System.out.println("Iniciando conversión con " + numThreads + " hilos...");
        System.out.println("URLs a procesar: " + urls.size());
        
        // Iniciar temporizador
        long startTime = System.currentTimeMillis();
        
        // Crear convertidor y procesar URLs
        WebToPdfConverter converter = new WebToPdfConverter(numThreads, outputDir);
        List<String> pdfPaths = converter.convertUrlsToPdf(urls);
        
        // Calcular tiempo transcurrido
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        // Mostrar resultados
        System.out.println("\n=== Resultados ===");
        System.out.println("Tiempo total: " + duration + " ms");
        System.out.println("Archivos PDF generados: " + pdfPaths.size());
        System.out.println("Hilos utilizados: " + numThreads);
        System.out.println("Directorio de salida: " + outputDir);
        
        // Guardar resultados en archivo de log
        saveResults(numThreads, duration, urls.size(), outputDir);
    }
    
    private static void saveResults(int numThreads, long duration, int numUrls, String outputDir) {
        try {
            String logFile = "resultados.csv";
            boolean fileExists = new java.io.File(logFile).exists();
            
            java.io.FileWriter writer = new java.io.FileWriter(logFile, true);
            if (!fileExists) {
                writer.write("NumHilos,TiempoMS,NumURLs,FechaHora\n");
            }
            
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            writer.write(numThreads + "," + duration + "," + numUrls + "," + timestamp + "\n");
            writer.close();
            
            System.out.println("Resultados guardados en " + logFile);
        } catch (IOException e) {
            System.err.println("Error al guardar resultados: " + e.getMessage());
        }
    }
}