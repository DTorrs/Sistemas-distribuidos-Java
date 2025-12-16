// Archivo: Derecho.java
public class Derecho implements ProgramaAcademico {
    private String nombre;
    private int duracionSemestres;
    private int creditos;
    private String[] asignaturas = new String[50];
    private int cantidadAsignaturas = 0;
    
    @Override
    public String obtenerNombre() {
        return nombre;
    }
    
    @Override
    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public int obtenerDuracionSemestres() {
        return duracionSemestres;
    }
    
    @Override
    public void establecerDuracionSemestres(int semestres) {
        this.duracionSemestres = semestres;
    }
    
    @Override
    public int obtenerCreditos() {
        return creditos;
    }
    
    @Override
    public void establecerCreditos(int creditos) {
        this.creditos = creditos;
    }
    
    @Override
    public void agregarAsignatura(String asignatura) {
        if (cantidadAsignaturas < asignaturas.length) {
            asignaturas[cantidadAsignaturas] = asignatura;
            cantidadAsignaturas++;
            System.out.println("Asignatura " + asignatura + " agregada al programa de Derecho");
        }
    }
    
    @Override
    public void eliminarAsignatura(String asignatura) {
        for (int i = 0; i < cantidadAsignaturas; i++) {
            if (asignaturas[i].equals(asignatura)) {
                for (int j = i; j < cantidadAsignaturas - 1; j++) {
                    asignaturas[j] = asignaturas[j + 1];
                }
                cantidadAsignaturas--;
                System.out.println("Asignatura " + asignatura + " eliminada del programa de Derecho");
                return;
            }
        }
    }
    
    // Métodos adicionales
    public void ofrecerEspecializacion(String especializacion) {
        System.out.println("Programa de Derecho ofrece especialización en " + especializacion);
    }
    
    public void asignarJuezPracticas(String juez) {
        System.out.println("Juez " + juez + " asignado para prácticas profesionales");
    }
}