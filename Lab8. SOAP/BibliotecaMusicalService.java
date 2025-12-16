package com.biblioteca.musical.service;

import com.biblioteca.musical.model.Song;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface BibliotecaMusicalService {
    
    @WebMethod
    List<Song> buscarPorNombre(String nombre);
    
    @WebMethod
    List<Song> buscarPorGenero(String genero);
    
    @WebMethod
    List<Song> buscarPorAutor(String autor);
    
    @WebMethod
    List<Song> buscarPorCriterios(String nombre, String genero, String autor);
    
    @WebMethod
    List<Song> obtenerTodasLasCanciones();
}