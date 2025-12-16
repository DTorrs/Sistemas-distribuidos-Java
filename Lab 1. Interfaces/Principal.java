// Archivo: Principal.java
public class Principal {
    public static void main(String[] args) {
        // Crear instancias de Estudiante y Docente
        Estudiante estudiante1 = new Estudiante();
        estudiante1.establecerNombre("Carlos");
        estudiante1.establecerApellido("Rodríguez");
        estudiante1.establecerEdad(20);
        estudiante1.establecerIdentificacion("E12345");
        estudiante1.matricular("Cálculo Diferencial");
        
        double[] notas = {4.5, 3.8, 4.2, 3.9};
        System.out.println("Promedio de " + estudiante1.obtenerNombre() + ": " + 
                           estudiante1.calcularPromedio(notas));
        
        Docente docente1 = new Docente();
        docente1.establecerNombre("Ana");
        docente1.establecerApellido("Martínez");
        docente1.establecerEdad(45);
        docente1.establecerIdentificacion("D98765");
        docente1.asignarCurso("Derecho Civil");
        docente1.calificarEstudiante("Carlos Rodríguez", 4.2);
        
        // Crear instancias de los programas académicos
        Derecho programaDerecho = new Derecho();
        programaDerecho.establecerNombre("Derecho");
        programaDerecho.establecerDuracionSemestres(10);
        programaDerecho.establecerCreditos(180);
        programaDerecho.agregarAsignatura("Derecho Constitucional");
        programaDerecho.agregarAsignatura("Derecho Civil");
        programaDerecho.ofrecerEspecializacion("Derecho Penal");
        programaDerecho.asignarJuezPracticas("Juan Pérez");
        
        IngenieriaMecanica programaIngenieria = new IngenieriaMecanica();
        programaIngenieria.establecerNombre("Ingeniería Mecánica");
        programaIngenieria.establecerDuracionSemestres(10);
        programaIngenieria.establecerCreditos(190);
        programaIngenieria.agregarAsignatura("Termodinámica");
        programaIngenieria.agregarAsignatura("Resistencia de Materiales");
        programaIngenieria.agregarLaboratorio("Laboratorio de Mecánica de Fluidos");
        programaIngenieria.realizarVisitaTecnica("Industrias Metalmecánicas S.A.");
        
        // Mostrar información
        System.out.println("\n--- Información del Estudiante ---");
        System.out.println("Nombre: " + estudiante1.obtenerNombre() + " " + estudiante1.obtenerApellido());
        System.out.println("Edad: " + estudiante1.obtenerEdad());
        System.out.println("ID: " + estudiante1.obtenerIdentificacion());
        
        System.out.println("\n--- Información del Docente ---");
        System.out.println("Nombre: " + docente1.obtenerNombre() + " " + docente1.obtenerApellido());
        System.out.println("Edad: " + docente1.obtenerEdad());
        System.out.println("ID: " + docente1.obtenerIdentificacion());
        
        System.out.println("\n--- Información de Programas Académicos ---");
        System.out.println("Programa: " + programaDerecho.obtenerNombre());
        System.out.println("Duración: " + programaDerecho.obtenerDuracionSemestres() + " semestres");
        System.out.println("Créditos: " + programaDerecho.obtenerCreditos());
        
        System.out.println("\nPrograma: " + programaIngenieria.obtenerNombre());
        System.out.println("Duración: " + programaIngenieria.obtenerDuracionSemestres() + " semestres");
        System.out.println("Créditos: " + programaIngenieria.obtenerCreditos());
    }
}