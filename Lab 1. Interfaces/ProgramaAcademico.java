// Archivo: ProgramaAcademico.java
public interface ProgramaAcademico {
    String obtenerNombre();
    void establecerNombre(String nombre);
    int obtenerDuracionSemestres();
    void establecerDuracionSemestres(int semestres);
    int obtenerCreditos();
    void establecerCreditos(int creditos);
    void agregarAsignatura(String asignatura);
    void eliminarAsignatura(String asignatura);
}