package com.ejemplo.WebToPdfConverter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Verificar argumentos
        if (args.length < 2) {
            System.out.println("Uso: java -jar DocumentToPdfConverter.jar <num_hilos> <directorio_salida> <archivo_lista_o_documentos...>");
            System.out.println("Ejemplo 1: java -jar DocumentToPdfConverter.jar 4 ./pdfs lista_archivos.txt");
            System.out.println("Ejemplo 2: java -jar DocumentToPdfConverter.jar 4 ./pdfs C:/documento1.docx C:/documento2.xlsx C:/documento3.pptx");
            return;
        }
        
        int numThreads = Integer.parseInt(args[0]);
        String outputDir = args[1];
        List<String> filePaths = new ArrayList<>();
        
        // Comprobar si el tercer argumento es un archivo de lista o documentos directos
        if (args.length == 3 && new File(args[2]).exists() && !new File(args[2]).isDirectory()) {
            // Es un archivo de lista
            filePaths = loadFilePathsFromFile(args[2]);
        } else if (args.length > 2) {
            // Son rutas de documentos directas
            filePaths = Arrays.asList(Arrays.copyOfRange(args, 2, args.length));
        } else {
            System.out.println("Debe proporcionar rutas de documentos o un archivo con la lista de rutas.");
            return;
        }
        
        if (filePaths.isEmpty()) {
            System.out.println("No se encontraron documentos para procesar.");
            return;
        }
        
        System.out.println("Iniciando conversión con " + numThreads + " hilos...");
        System.out.println("Documentos a procesar: " + filePaths.size());
        
        // Iniciar temporizador
        long startTime = System.currentTimeMillis();
        
        // Crear convertidor y procesar documentos
        DocumentToPdfConverter converter = new DocumentToPdfConverter(numThreads, outputDir);
        List<String> pdfPaths = converter.convertDocumentsToPdf(filePaths);
        
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
        saveResults(numThreads, duration, filePaths.size(), outputDir);
    }
    
    private static List<String> loadFilePathsFromFile(String filePath) {
        List<String> paths = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    paths.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de rutas: " + e.getMessage());
        }
        return paths;
    }
    
    private static void saveResults(int numThreads, long duration, int numFiles, String outputDir) {
        try {
            String logFile = "resultados.csv";
            boolean fileExists = new File(logFile).exists();
            
            java.io.FileWriter writer = new java.io.FileWriter(logFile, true);
            if (!fileExists) {
                writer.write("NumHilos,TiempoMS,NumDocs,FechaHora\n");
            }
            
            String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            writer.write(numThreads + "," + duration + "," + numFiles + "," + timestamp + "\n");
            writer.close();
            
            System.out.println("Resultados guardados en " + logFile);
        } catch (IOException e) {
            System.err.println("Error al guardar resultados: " + e.getMessage());
        }
    }
}