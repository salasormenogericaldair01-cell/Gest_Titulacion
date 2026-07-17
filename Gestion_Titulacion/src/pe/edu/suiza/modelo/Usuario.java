package pe.edu.suiza.modelo;

import java.sql.Timestamp;

/**
 * Modelo de Usuario en el paquete modular pe.edu.suiza.modelo.
 */
public class Usuario {
    
    private int idUsuario;
    private String username;
    private String passwordClaro;
    private String rol;
    private String nombreCompleto;
    private String estado;
    private Timestamp fechaRegistro;
    
    public Usuario() {
    }

    public Usuario(int idUsuario, String username, String passwordClaro, String rol, String nombreCompleto, String estado, Timestamp fechaRegistro) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.passwordClaro = passwordClaro;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.estado = estado;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordClaro() { return passwordClaro; }
    public void setPasswordClaro(String passwordClaro) { this.passwordClaro = passwordClaro; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    private String email;
    private String especialidad;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
}
