package pe.edu.suiza.modelo;

/**
 * Modelo Estudiante (integrante de proyecto de titulación) en pe.edu.suiza.modelo.
 * Reglamento institucional: Máximo 5 estudiantes por proyecto.
 */
public class Estudiante {

    private int idEstudiante;
    private int idProyecto;
    private String dniCodigo;
    private String nombres;
    private String apellidos;

    public Estudiante() {
    }

    public Estudiante(int idEstudiante, int idProyecto, String dniCodigo, String nombres, String apellidos) {
        this.idEstudiante = idEstudiante;
        this.idProyecto = idProyecto;
        this.dniCodigo = dniCodigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
    }

    public int getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(int idEstudiante) { this.idEstudiante = idEstudiante; }

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public String getDniCodigo() { return dniCodigo; }
    public void setDniCodigo(String dniCodigo) { this.dniCodigo = dniCodigo; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }
}
