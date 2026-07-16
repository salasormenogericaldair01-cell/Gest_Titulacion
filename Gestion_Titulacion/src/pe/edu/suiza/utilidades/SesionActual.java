package pe.edu.suiza.utilidades;

import pe.edu.suiza.modelo.Usuario;

/**
 * Clase utilitaria para mantener en memoria el usuario actualmente logueado.
 * Paquete: pe.edu.suiza.utilidades
 */
public class SesionActual {
    
    private static Usuario usuarioLogueado = null;
    
    public static void setUsuarioLogueado(Usuario user) {
        usuarioLogueado = user;
    }
    
    public static Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
    
    public static void limpiarSesion() {
        usuarioLogueado = null;
    }
    
    public static boolean haySesionActiva() {
        return usuarioLogueado != null;
    }
}
