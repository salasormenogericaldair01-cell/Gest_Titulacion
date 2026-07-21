package pe.edu.suiza.modelo;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Modelo Proyecto de Titulación según requerimiento de la documentación IESTP Suiza.
 * Paquete modular: pe.edu.suiza.modelo
 */
public class Proyecto {
    
    private int idProyecto;
    private String codigoProyecto;
    private String titulo;
    private String programaEstudio;
    private String modalidad;
    private String asesor;
    private String estado;
    private Date fechaRegistro;
    private Timestamp fechaActualizacion;
    private String archivoNombre;
    private byte[] archivoData;

    public Proyecto() {
    }

    public Proyecto(int idProyecto, String codigoProyecto, String titulo, String programaEstudio, String modalidad, String asesor, String estado, Date fechaRegistro, Timestamp fechaActualizacion) {
        this.idProyecto = idProyecto;
        this.codigoProyecto = codigoProyecto;
        this.titulo = titulo;
        this.programaEstudio = programaEstudio;
        this.modalidad = modalidad;
        this.asesor = asesor;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
    }

    public Proyecto(int idProyecto, String codigoProyecto, String titulo, String programaEstudio, String modalidad, String asesor, String estado, Date fechaRegistro, Timestamp fechaActualizacion, String archivoNombre, byte[] archivoData) {
        this.idProyecto = idProyecto;
        this.codigoProyecto = codigoProyecto;
        this.titulo = titulo;
        this.programaEstudio = programaEstudio;
        this.modalidad = modalidad;
        this.asesor = asesor;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
        this.fechaActualizacion = fechaActualizacion;
        this.archivoNombre = archivoNombre;
        this.archivoData = archivoData;
    }

    public int getIdProyecto() { return idProyecto; }
    public void setIdProyecto(int idProyecto) { this.idProyecto = idProyecto; }

    public String getCodigoProyecto() { return codigoProyecto; }
    public void setCodigoProyecto(String codigoProyecto) { this.codigoProyecto = codigoProyecto; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getProgramaEstudio() { return programaEstudio; }
    public void setProgramaEstudio(String programaEstudio) { this.programaEstudio = programaEstudio; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getAsesor() { return asesor; }
    public void setAsesor(String asesor) { this.asesor = asesor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public Timestamp getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(Timestamp fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }

    public String getArchivoNombre() { return archivoNombre; }
    public void setArchivoNombre(String archivoNombre) { this.archivoNombre = archivoNombre; }

    public byte[] getArchivoData() { return archivoData; }
    public void setArchivoData(byte[] archivoData) { this.archivoData = archivoData; }
}

