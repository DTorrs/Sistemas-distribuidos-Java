package com.ejemplo.WebToPdfConverter;

import java.io.File;
import java.util.concurrent.Callable;

import org.openqa.selenium.Pdf;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.print.PrintOptions;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.bonigarcia.wdm.config.DriverManagerType;

public class PdfConverterTask implements Callable<String> {
    private String url;
    private String outputDir;
    
    public PdfConverterTask(String url, String outputDir) {
        this.url = url;
        this.outputDir = outputDir;
    }
    
    @Override
    public String call() throws Exception {
        ChromeDriver driver = null;
        try {
            // Configuración mejorada de WebDriverManager para Chrome
            WebDriverManager.chromedriver()
                .clearDriverCache()
                .clearResolutionCache()
                .browserVersion("")  // Vacío para usar la versión actual del navegador
                .setup();
            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");  // Nuevo modo headless
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--no-sandbox");
            
            // Iniciar ChromeDriver
            driver = new ChromeDriver(options);
            
            // Cargar la página
            driver.get(url);
            
            // Esperar a que la página se cargue completamente
            Thread.sleep(3000);  // Aumentado el tiempo de espera
            
            // Generar nombre de archivo basado en la URL
            String fileName = url.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf";
            String filePath = outputDir + File.separator + fileName;
            
            // Imprimir a PDF
            PrintOptions printOptions = new PrintOptions();
            printOptions.setPageRanges("1-");
            Pdf pdf = driver.print(printOptions);
            
            // Guardar el PDF
            java.nio.file.Files.write(new File(filePath).toPath(), 
                    java.util.Base64.getDecoder().decode(pdf.getContent()));
            
            return filePath;
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}