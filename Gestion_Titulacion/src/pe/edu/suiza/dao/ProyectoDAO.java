package pe.edu.suiza.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import pe.edu.suiza.modelo.Proyecto;

/**
 * DAO para proyectos de titulación en XAMPP MySQL.
 * Contiene registro de proyecto, filtro por fecha (1 mes a 5 años), búsqueda y cambio de estado.
 * Paquete modular: pe.edu.suiza.dao
 */
public class ProyectoDAO {

    public int insertarProyecto(Proyecto p) {
        String sql = "INSERT INTO proyectos (codigo_proyecto, titulo, programa_estudio, modalidad, asesor, estado, fecha_registro, fecha_actualizacion) VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return -1;

            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, p.getCodigoProyecto());
            pstmt.setString(2, p.getTitulo());
            pstmt.setString(3, p.getProgramaEstudio());
            pstmt.setString(4, p.getModalidad());
            pstmt.setString(5, p.getAsesor());
            pstmt.setString(6, "EN_REVISION"); // Por defecto entra al flujo de revisión del Coordinador
            pstmt.setDate(7, p.getFechaRegistro());

            int filas = pstmt.executeUpdate();
            if (filas > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    int genId = rs.getInt(1);
                    if (genId > 0) return genId;
                }
                cerrarRecursos(pstmt, rs);
                // Consulta de respaldo por si el driver MySQL no devuelve la llave generada
                String sqlBusq = "SELECT id_proyecto FROM proyectos WHERE codigo_proyecto = ?";
                pstmt = conn.prepareStatement(sqlBusq);
                pstmt.setString(1, p.getCodigoProyecto());
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id_proyecto");
                }
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en insertarProyecto: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return -1;
    }
    
    public List<Proyecto> listarTodos() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos ORDER BY id_proyecto DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en listarTodos: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public List<Proyecto> buscarPorTexto(String query) {
        List<Proyecto> lista = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            return listarTodos();
        }
        String q = "%" + query.trim() + "%";
        String sql = "SELECT * FROM proyectos WHERE codigo_proyecto LIKE ? OR titulo LIKE ? OR programa_estudio LIKE ? OR asesor LIKE ? OR estado LIKE ? ORDER BY id_proyecto DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, q);
            pstmt.setString(2, q);
            pstmt.setString(3, q);
            pstmt.setString(4, q);
            pstmt.setString(5, q);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en buscarPorTexto: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public List<Proyecto> listarParaAprobacionFinal() {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos WHERE estado IN ('APROBADO_COORDINACION', 'EN_REVISION') ORDER BY id_proyecto DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en listarParaAprobacionFinal: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public List<Proyecto> listarPorRangoTiempo(String fechaDesde, String fechaHasta) {
        List<Proyecto> lista = new ArrayList<>();
        String sql = "SELECT * FROM proyectos WHERE fecha_registro BETWEEN ? AND ? ORDER BY fecha_registro DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaDesde);
            pstmt.setString(2, fechaHasta);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en listarPorRangoTiempo: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public boolean cambiarEstado(String codigoProyecto, String nuevoEstado) {
        String sql = "UPDATE proyectos SET estado = ?, fecha_actualizacion = NOW() WHERE codigo_proyecto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return false;
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nuevoEstado);
            pstmt.setString(2, codigoProyecto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en cambiarEstado: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    private Proyecto mapearProyecto(ResultSet rs) throws SQLException {
        return new Proyecto(
            rs.getInt("id_proyecto"),
            rs.getString("codigo_proyecto"),
            rs.getString("titulo"),
            rs.getString("programa_estudio"),
            rs.getString("modalidad"),
            rs.getString("asesor"),
            rs.getString("estado"),
            rs.getDate("fecha_registro"),
            rs.getTimestamp("fecha_actualizacion")
        );
    }

    private void cerrarRecursos(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al cerrar recursos: " + e.getMessage());
        }
    }
}

