package pe.edu.suiza.modelo;

import java.sql.Timestamp;

/**
 * Modelo Observación según requerimientos de revisión del IESTP Suiza.
 * Paquete modular: pe.edu.suiza.modelo
 */
public class Observacion {

    private int idObservacion;
    private int idProyecto;
    private int idUsuario;
    private String rolAutor;
    private String descripcion;
    private Timestamp fechaObservacion;
    private String estadoObservacion; // PENDIENTE o SUBSANADO
    
    // Campos auxiliares para visualización directa en tablas JavaFX
    private String codigoProyectoAux;
    private String tituloProyectoAux;

    public Observacion() {
    }

    public Observacion(int idObservacion, int idProyecto, int idUsuario, String rolAutor, String descripcion, Timestamp fechaObservacion, String estadoObservacion) {
        this.idObservacion = idObservacion;
        this.idProyecto = idProyecto;
        this.idUsuario = idUsuario;
        this.rolAutor = rolAutor;
        this.descripcion = descripcion;
        this.fechaObservacion = fechaObservacion;
        this.estadoObservacion = estadoObservacion;
    }

    public int getIdObservacion() { return idObservacion; }
    public void setIdObservacion(int idObservacion) { this.idObservacion = idObservacion; }

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getRolAutor() { return rolAutor; }
    public void setRolAutor(String rolAutor) { this.rolAutor = rolAutor; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Timestamp getFechaObservacion() { return fechaObservacion; }
    public void setFechaObservacion(Timestamp fechaObservacion) { this.fechaObservacion = fechaObservacion; }

    public String getEstadoObservacion() { return estadoObservacion; }
    public void setEstadoObservacion(String estadoObservacion) { this.estadoObservacion = estadoObservacion; }

    public String getCodigoProyectoAux() { return codigoProyectoAux; }
    public void setCodigoProyectoAux(String codigoProyectoAux) { this.codigoProyectoAux = codigoProyectoAux; }

    public String getTituloProyectoAux() { return tituloProyectoAux; }
    public void setTituloProyectoAux(String tituloProyectoAux) { this.tituloProyectoAux = tituloProyectoAux; }
}
