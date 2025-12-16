// Archivo: Estudiante.java
public class Estudiante implements Persona {
    private String nombre;
    private String apellido;
    private int edad;
    private String identificacion;
    private String carrera;
    private double promedio;
    
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
    public void matricular(String asignatura) {
        System.out.println("Estudiante " + nombre + " matriculado en " + asignatura);
    }
    
    public double calcularPromedio(double[] notas) {
        double suma = 0;
        for (double nota : notas) {
            suma += nota;
        }
        this.promedio = suma / notas.length;
        return this.promedio;
    }
}