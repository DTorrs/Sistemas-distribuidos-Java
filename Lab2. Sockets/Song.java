package classes;

import java.io.Serializable;

/**
 * Clase que representa una canción en la biblioteca de música
 * Implementa Serializable para permitir su transmisión a través de sockets
 */
public class Song implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String title;      // Título de la canción
    private String genre;      // Género musical
    private String author;     // Autor/Artista
    private String language;   // Idioma de la canción
    private int releaseYear;   // Año de lanzamiento
    
    /**
     * Constructor de la clase Song
     * @param title Título de la canción
     * @param genre Género musical
     * @param author Autor/Artista
     * @param language Idioma de la canción
     * @param releaseYear Año de lanzamiento
     */
    public Song(String title, String genre, String author, String language, int releaseYear) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.language = language;
        this.releaseYear = releaseYear;
    }
    
    // Getters y setters
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public int getReleaseYear() {
        return releaseYear;
    }
    
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
    
    @Override
    public String toString() {
        return "Título: " + title + " | Autor: " + author + " | Género: " + genre + 
               " | Idioma: " + language + " | Año: " + releaseYear;
    }
}