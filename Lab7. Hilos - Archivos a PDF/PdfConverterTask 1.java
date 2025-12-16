package com.ejemplo.WebToPdfConverter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfConverterTask implements Callable<String> {
    private String inputFilePath;
    private String outputDir;
    
    public PdfConverterTask(String inputFilePath, String outputDir) {
        this.inputFilePath = inputFilePath;
        this.outputDir = outputDir;
    }
    
    @Override
    public String call() throws Exception {
        try {
            // Crear el nombre del archivo de salida (mismo nombre, extensión PDF)
            File inputFile = new File(inputFilePath);
            String fileName = inputFile.getName();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            String outputFilePath = outputDir + File.separator + fileNameWithoutExtension + ".pdf";
            
            // Convertir según el tipo de archivo
            switch (extension) {
                case "docx":
                    convertDocxToPdf(inputFilePath, outputFilePath);
                    break;
                case "xlsx":
                    convertXlsxToPdf(inputFilePath, outputFilePath);
                    break;
                case "pptx":
                    convertPptxToPdf(inputFilePath, outputFilePath);
                    break;
                case "png":
                case "jpg":
                case "jpeg":
                    convertImageToPdf(inputFilePath, outputFilePath);
                    break;
                default:
                    // Si no es un formato soportado, crear un PDF simple con mensaje
                    createSimplePdf(outputFilePath, "Formato no soportado: " + extension);
                    break;
            }
            
            // Verificar que el archivo PDF se haya creado
            File outputFile = new File(outputFilePath);
            if (!outputFile.exists()) {
                createSimplePdf(outputFilePath, "Error al convertir: " + inputFilePath);
            }
            
            return outputFilePath;
        } catch (Exception e) {
            // En caso de error, crear un PDF indicando el error
            String errorPdfPath = outputDir + File.separator + "error_" + 
                    new File(inputFilePath).getName().replace('.', '_') + ".pdf";
            createSimplePdf(errorPdfPath, "Error: " + e.getMessage());
            return errorPdfPath;
        }
    }
    
    private void convertDocxToPdf(String inputPath, String outputPath) throws Exception {
        // Método simplificado usando solo PDFBox
        XWPFDocument document = null;
        PDDocument pdfDoc = null;
        
        try {
            document = new XWPFDocument(new FileInputStream(inputPath));
            pdfDoc = new PDDocument();
            
            PDPage page = new PDPage(PDRectangle.A4);
            pdfDoc.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(50, 700);
            
            // Título
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.showText("Documento: " + new File(inputPath).getName());
            contentStream.newLineAtOffset(0, -20);
            
            // Contenido
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            int lineCount = 0;
            for (org.apache.poi.xwpf.usermodel.XWPFParagraph para : document.getParagraphs()) {
                String text = para.getText();
                if (text != null && !text.trim().isEmpty()) {
                    contentStream.showText(text.substring(0, Math.min(100, text.length())));
                    contentStream.newLineAtOffset(0, -15);
                    if (++lineCount > 30) {
                        contentStream.showText("... (contenido truncado)");
                        break;
                    }
                }
            }
            contentStream.endText();
            contentStream.close();
            
            pdfDoc.save(outputPath);
        } catch (Exception e) {
            // Método alternativo con iText si el principal falla
            Document pdfDocument = new Document();
            try {
                PdfWriter.getInstance(pdfDocument, new FileOutputStream(outputPath));
                pdfDocument.open();
                pdfDocument.add(new Paragraph("Documento: " + new File(inputPath).getName()));
                pdfDocument.add(new Paragraph("No se pudo procesar completamente. Error: " + e.getMessage()));
            } finally {
                if (pdfDocument.isOpen()) {
                    pdfDocument.close();
                }
            }
        } finally {
            if (document != null) {
                document.close();
            }
            if (pdfDoc != null) {
                pdfDoc.close();
            }
        }
    }
    
    private void convertXlsxToPdf(String inputPath, String outputPath) throws Exception {
        Document pdfDocument = new Document();
        XSSFWorkbook workbook = null;
        
        try {
            PdfWriter.getInstance(pdfDocument, new FileOutputStream(outputPath));
            pdfDocument.open();
            
            workbook = new XSSFWorkbook(new FileInputStream(inputPath));
            // Convertir cada hoja a una tabla en el PDF
            for (int i = 0; i < Math.min(workbook.getNumberOfSheets(), 3); i++) {
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(i);
                
                // Añadir título de la hoja
                pdfDocument.add(new Paragraph("Hoja: " + sheet.getSheetName()));
                
                // Obtener número máximo de columnas
                int maxCols = 0;
                for (org.apache.poi.ss.usermodel.Row row : sheet) {
                    maxCols = Math.max(maxCols, row.getLastCellNum());
                }
                
                // Crear tabla iText
                PdfPTable table = new PdfPTable(Math.max(1, maxCols));
                
                // Limitar a 50 filas para no sobrecargar
                int rowCount = 0;
                for (org.apache.poi.ss.usermodel.Row row : sheet) {
                    if (rowCount++ > 50) {
                        break;
                    }
                    for (int j = 0; j < maxCols; j++) {
                        org.apache.poi.ss.usermodel.Cell cell = row.getCell(j);
                        String text = cell != null ? cell.toString() : "";
                        table.addCell(text);
                    }
                }
                
                pdfDocument.add(table);
                pdfDocument.newPage();
            }
        } catch (Exception e) {
            if (pdfDocument.isOpen()) {
                pdfDocument.add(new Paragraph("Error al procesar Excel: " + e.getMessage()));
            }
        } finally {
            if (pdfDocument.isOpen()) {
                pdfDocument.close();
            }
            if (workbook != null) {
                workbook.close();
            }
        }
    }
    
    private void convertPptxToPdf(String inputPath, String outputPath) throws Exception {
        PDDocument pdfDocument = null;
        XMLSlideShow ppt = null;
        
        try {
            pdfDocument = new PDDocument();
            ppt = new XMLSlideShow(new FileInputStream(inputPath));
            
            // Dimensiones de la diapositiva
            java.awt.Dimension pgsize = ppt.getPageSize();
            
            // Limitar a 10 diapositivas para evitar problemas de memoria
            int slideCount = 0;
            for (XSLFSlide slide : ppt.getSlides()) {
                if (slideCount++ > 10) break;
                
                try {
                    // Crear imagen de la diapositiva
                    BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_RGB);
                    java.awt.Graphics2D graphics = img.createGraphics();
                    
                    // Configurar calidad de renderizado
                    graphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, 
                            java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, 
                            java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                    
                    // Fondo blanco
                    graphics.setColor(java.awt.Color.WHITE);
                    graphics.fill(new java.awt.Rectangle(0, 0, pgsize.width, pgsize.height));
                    
                    // Dibujar la diapositiva
                    slide.draw(graphics);
                    
                    // Crear página PDF
                    PDPage page = new PDPage(new PDRectangle(pgsize.width, pgsize.height));
                    pdfDocument.addPage(page);
                    
                    // Insertar imagen en la página
                    PDImageXObject pdImage = PDImageXObject.createFromByteArray(pdfDocument, 
                            bufferedImageToByteArray(img), "slide");
                    
                    PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
                    contentStream.drawImage(pdImage, 0, 0);
                    contentStream.close();
                    
                    // Liberar recursos
                    graphics.dispose();
                } catch (Exception ex) {
                    System.err.println("Error en diapositiva " + slideCount + ": " + ex.getMessage());
                }
            }
            
            pdfDocument.save(outputPath);
        } catch (Exception e) {
            // Si falla la apertura del PPT, crear una página con el error
            if (pdfDocument == null) {
                pdfDocument = new PDDocument();
            }
            
            PDPage errorPage = new PDPage(PDRectangle.A4);
            pdfDocument.addPage(errorPage);
            
            PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, errorPage);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText("Error al procesar presentación: " + e.getMessage());
            contentStream.endText();
            contentStream.close();
            
            pdfDocument.save(outputPath);
        } finally {
            if (ppt != null) {
                ppt.close();
            }
            if (pdfDocument != null) {
                pdfDocument.close();
            }
        }
    }
    
    private void convertImageToPdf(String inputPath, String outputPath) throws Exception {
        PDDocument document = null;
        
        try {
            document = new PDDocument();
            
            // Cargar imagen
            BufferedImage image = ImageIO.read(new File(inputPath));
            
            // Crear página PDF del tamaño de la imagen (limitar tamaño máximo)
            int maxWidth = 842; // A4 width
            int maxHeight = 595; // A4 height
            
            float width = Math.min(image.getWidth(), maxWidth);
            float height = Math.min(image.getHeight(), maxHeight);
            
            PDPage page = new PDPage(new PDRectangle(width, height));
            document.addPage(page);
            
            // Insertar imagen en el PDF
            PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, 
                    bufferedImageToByteArray(image), "image");
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.drawImage(pdImage, 0, 0, width, height);
            contentStream.close();
            
            document.save(outputPath);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
    
    private void createSimplePdf(String outputPath, String message) throws Exception {
        PDDocument document = null;
        
        try {
            document = new PDDocument();
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(50, 700);
            contentStream.showText(message);
            contentStream.endText();
            contentStream.close();
            
            document.save(outputPath);
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
    
    private byte[] bufferedImageToByteArray(BufferedImage image) throws Exception {
        java.io.ByteArrayOutputStream baos = null;
        
        try {
            baos = new java.io.ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                baos.close();
            }
        }
    }
}