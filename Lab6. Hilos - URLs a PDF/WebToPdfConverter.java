package com.ejemplo.WebToPdfConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class WebToPdfConverter {
    private int numThreads;
    private String outputDir;
    
    public WebToPdfConverter(int numThreads, String outputDir) {
        this.numThreads = numThreads;
        this.outputDir = outputDir;
        
        // Crear directorio de salida si no existe
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public List<String> convertUrlsToPdf(List<String> urls) {
        List<String> pdfPaths = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<String>> futures = new ArrayList<>();
        
        System.out.println("Iniciando conversión con " + numThreads + " hilos para " + urls.size() + " URLs");
        
        // Enviar tareas al executor
        for (String url : urls) {
            PdfConverterTask task = new PdfConverterTask(url, outputDir);
            Future<String> future = executor.submit(task);
            futures.add(future);
        }
        
        // Recoger resultados
        for (Future<String> future : futures) {
            try {
                String pdfPath = future.get();
                pdfPaths.add(pdfPath);
                System.out.println("Convertido: " + pdfPath);
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error al convertir URL: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        // Cerrar el executor
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.err.println("Interrupción al esperar por el executor: " + e.getMessage());
        }
        
        return pdfPaths;
    }
}