package pe.edu.suiza.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.suiza.modelo.Usuario;

/**
 * DAO para gestión de usuarios en XAMPP MySQL.
 * Paquete: pe.edu.suiza.dao
 */
public class UsuarioDAO {
    
    public Usuario validarLogin(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ? AND password_claro = ? AND estado = 'ACTIVO'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return null;
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    rs.getString("password_claro"),
                    rs.getString("rol"),
                    rs.getString("nombre_completo"),
                    rs.getString("estado"),
                    rs.getTimestamp("fecha_registro")
                );
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en validarLogin: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return null;
    }
    
    public String generarTicketRecuperacion(String username) {
        String sqlVerificar = "SELECT id_usuario FROM usuarios WHERE (username = ? OR dni = ?)";
        String sqlTicket = "INSERT INTO observaciones (id_proyecto, id_usuario, rol_autor, descripcion, estado_observacion) VALUES (NULL, ?, 'TICKET_TI', ?, 'PENDIENTE')";
        Connection conn = null;
        PreparedStatement pstmtVer = null;
        PreparedStatement pstmtIns = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return null;
            
            pstmtVer = conn.prepareStatement(sqlVerificar);
            pstmtVer.setString(1, username);
            pstmtVer.setString(2, username);
            rs = pstmtVer.executeQuery();
            
            int numRandom = (int)(Math.random() * 9000) + 1000;
            String ticket = "TICK-SUIZA-" + numRandom;
            String descripcion = "SOLICITUD RESETEO CLAVE / SOPORTE TI (" + username + ") - TICKET: " + ticket;
            
            pstmtIns = conn.prepareStatement(sqlTicket);
            if (rs.next()) {
                pstmtIns.setInt(1, rs.getInt("id_usuario"));
            } else {
                pstmtIns.setNull(1, java.sql.Types.INTEGER);
            }
            pstmtIns.setString(2, descripcion);
            if (pstmtIns.executeUpdate() > 0) {
                return ticket;
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en generarTicketRecuperacion: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmtVer, rs);
            cerrarRecursos(pstmtIns, null);
        }
        return null;
    }
    
    public List<Usuario> listarTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY id_usuario DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(new Usuario(
                    rs.getInt("id_usuario"),
                    rs.getString("username"),
                    rs.getString("password_claro"),
                    rs.getString("rol"),
                    rs.getString("nombre_completo"),
                    rs.getString("estado"),
                    rs.getTimestamp("fecha_registro")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en listarTodos: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }
    
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (username, password_claro, rol, nombre_completo, estado) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return true;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, u.getUsername());
            pstmt.setString(2, u.getPasswordClaro());
            pstmt.setString(3, u.getRol());
            pstmt.setString(4, u.getNombreCompleto());
            pstmt.setString(5, u.getEstado());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en insertar: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET nombre_completo = ?, rol = ?, estado = ? WHERE id_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return true;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, u.getNombreCompleto());
            pstmt.setString(2, u.getRol());
            pstmt.setString(3, u.getEstado());
            pstmt.setInt(4, u.getIdUsuario());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en actualizar: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return true;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error en eliminar: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    private void cerrarRecursos(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.err.println("[UsuarioDAO] Error al cerrar recursos: " + e.getMessage());
        }
    }
}
