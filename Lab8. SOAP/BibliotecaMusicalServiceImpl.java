package com.biblioteca.musical.service;

import com.biblioteca.musical.model.Song;
import javax.jws.WebService;
import java.util.*;
import java.util.stream.Collectors;

@WebService(endpointInterface = "com.biblioteca.musical.service.BibliotecaMusicalService")
public class BibliotecaMusicalServiceImpl implements BibliotecaMusicalService {
    
    private List<Song> baseDatosCanciones;
    
    public BibliotecaMusicalServiceImpl() {
        inicializarBaseDatos();
    }
    
    private void inicializarBaseDatos() {
        baseDatosCanciones = new ArrayList<>();
        
        baseDatosCanciones.add(new Song("Bohemian Rhapsody", "Rock", "Queen", "Inglés", 1975));
        baseDatosCanciones.add(new Song("Imagine", "Pop", "John Lennon", "Inglés", 1971));
        baseDatosCanciones.add(new Song("Hotel California", "Rock", "Eagles", "Inglés", 1976));
        baseDatosCanciones.add(new Song("Billie Jean", "Pop", "Michael Jackson", "Inglés", 1983));
        baseDatosCanciones.add(new Song("Stairway to Heaven", "Rock", "Led Zeppelin", "Inglés", 1971));
        baseDatosCanciones.add(new Song("La Bamba", "Rock", "Ritchie Valens", "Español", 1958));
        baseDatosCanciones.add(new Song("Smooth Criminal", "Pop", "Michael Jackson", "Inglés", 1988));
        baseDatosCanciones.add(new Song("Yesterday", "Pop", "The Beatles", "Inglés", 1965));
        baseDatosCanciones.add(new Song("Sweet Child O' Mine", "Rock", "Guns N' Roses", "Inglés", 1987));
        baseDatosCanciones.add(new Song("Thriller", "Pop", "Michael Jackson", "Inglés", 1982));
        baseDatosCanciones.add(new Song("Come As You Are", "Grunge", "Nirvana", "Inglés", 1991));
        baseDatosCanciones.add(new Song("Smells Like Teen Spirit", "Grunge", "Nirvana", "Inglés", 1991));
        baseDatosCanciones.add(new Song("Wonderwall", "Rock", "Oasis", "Inglés", 1995));
        baseDatosCanciones.add(new Song("Shape of You", "Pop", "Ed Sheeran", "Inglés", 2017));
        baseDatosCanciones.add(new Song("Despacito", "Reggaeton", "Luis Fonsi", "Español", 2017));
        baseDatosCanciones.add(new Song("Macarena", "Pop", "Los del Río", "Español", 1993));
        baseDatosCanciones.add(new Song("La Vida Es Una Fiesta", "Pop", "Manu Chao", "Español", 2001));
        baseDatosCanciones.add(new Song("Blue", "Pop", "Eiffel 65", "Inglés", 1998));
        baseDatosCanciones.add(new Song("Gangnam Style", "K-Pop", "PSY", "Coreano", 2012));
        baseDatosCanciones.add(new Song("Mambo No. 5", "Mambo", "Lou Bega", "Inglés", 1999));
    }
    
    @Override
    public List<Song> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return baseDatosCanciones.stream()
                .filter(song -> song.getTitulo().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Song> buscarPorGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return baseDatosCanciones.stream()
                .filter(song -> song.getGenero().toLowerCase().contains(genero.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Song> buscarPorAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return baseDatosCanciones.stream()
                .filter(song -> song.getAutor().toLowerCase().contains(autor.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Song> buscarPorCriterios(String nombre, String genero, String autor) {
        return baseDatosCanciones.stream()
                .filter(song -> {
                    boolean coincideNombre = nombre == null || nombre.trim().isEmpty() || 
                                           song.getTitulo().toLowerCase().contains(nombre.toLowerCase());
                    boolean coincideGenero = genero == null || genero.trim().isEmpty() || 
                                           song.getGenero().toLowerCase().contains(genero.toLowerCase());
                    boolean coincideAutor = autor == null || autor.trim().isEmpty() || 
                                          song.getAutor().toLowerCase().contains(autor.toLowerCase());
                    
                    return coincideNombre && coincideGenero && coincideAutor;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Song> obtenerTodasLasCanciones() {
        return new ArrayList<>(baseDatosCanciones);
    }
}