// Archivo: Docente.java
public class Docente implements Persona {
    private String nombre;
    private String apellido;
    private int edad;
    private String identificacion;
    private String especialidad;
    private int añosExperiencia;
    
    @Override
    public String obtenerNombre() {
        return nombre;
    }
    
    @Override
    public void establecerNombre(String nombre) {
        this.nombre = nombre;
    }
    
    @Override
    public String obtenerApellido() {
        return apellido;
    }
    
    @Override
    public void establecerApellido(String apellido) {
        this.apellido = apellido;
    }
    
    @Override
    public int obtenerEdad() {
        return edad;
    }
    
    @Override
    public void establecerEdad(int edad) {
        this.edad = edad;
    }
    
    @Override
    public String obtenerIdentificacion() {
        return identificacion;
    }
    
    @Override
    public void establecerIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }
    
    // Métodos adicionales
    public void asignarCurso(String curso) {
        System.out.println("Docente " + nombre + " asignado al curso " + curso);
    }
    
    public void calificarEstudiante(String estudiante, double nota) {
        System.out.println("Estudiante " + estudiante + " calificado con " + nota);
    }
}