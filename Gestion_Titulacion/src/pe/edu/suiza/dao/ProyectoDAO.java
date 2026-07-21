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

    public ProyectoDAO() {
        verificarEsquemaArchivos();
    }

    public void verificarEsquemaArchivos() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return;
            rs = conn.getMetaData().getColumns(null, null, "proyectos", "archivo_nombre");
            if (!rs.next()) {
                stmt = conn.createStatement();
                stmt.executeUpdate("ALTER TABLE proyectos ADD COLUMN archivo_nombre VARCHAR(255) DEFAULT NULL");
                stmt.executeUpdate("ALTER TABLE proyectos ADD COLUMN archivo_data LONGBLOB DEFAULT NULL");
                System.out.println("[ProyectoDAO] Migración ejecutada: agregadas columnas archivo_nombre y archivo_data.");
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Nota en verificarEsquemaArchivos: " + e.getMessage());
        } finally {
            cerrarRecursos(stmt, rs);
        }
    }

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
                int genId = -1;
                rs = pstmt.getGeneratedKeys();
                if (rs != null && rs.next()) {
                    genId = rs.getInt(1);
                }
                cerrarRecursos(pstmt, rs);
                if (genId <= 0) {
                    // Consulta de respaldo por si el driver MySQL no devuelve la llave generada
                    String sqlBusq = "SELECT id_proyecto FROM proyectos WHERE codigo_proyecto = ?";
                    pstmt = conn.prepareStatement(sqlBusq);
                    pstmt.setString(1, p.getCodigoProyecto());
                    rs = pstmt.executeQuery();
                    if (rs.next()) {
                        genId = rs.getInt("id_proyecto");
                    }
                }
                if (genId > 0 && p.getArchivoNombre() != null && !p.getArchivoNombre().isEmpty() && p.getArchivoData() != null) {
                    subirArchivo(genId, p.getArchivoNombre(), p.getArchivoData());
                }
                return genId;
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
            if (conn == null) return obtenerProyectosOfflineRespaldo();
            
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en listarTodos: " + e.getMessage());
            return obtenerProyectosOfflineRespaldo();
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
        String sql = "SELECT DISTINCT p.* FROM proyectos p LEFT JOIN estudiantes e ON p.id_proyecto = e.id_proyecto " +
                     "WHERE p.codigo_proyecto LIKE ? OR p.titulo LIKE ? OR p.programa_estudio LIKE ? OR p.asesor LIKE ? OR p.estado LIKE ? " +
                     "OR e.dni_codigo LIKE ? OR e.nombres LIKE ? OR e.apellidos LIKE ? ORDER BY p.id_proyecto DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) {
                String qLower = query.trim().toLowerCase();
                for (Proyecto p : obtenerProyectosOfflineRespaldo()) {
                    if (p.getCodigoProyecto().toLowerCase().contains(qLower) ||
                        p.getTitulo().toLowerCase().contains(qLower) ||
                        p.getProgramaEstudio().toLowerCase().contains(qLower) ||
                        p.getEstado().toLowerCase().contains(qLower) ||
                        p.getAsesor().toLowerCase().contains(qLower)) {
                        lista.add(p);
                    }
                }
                return lista;
            }
            
            pstmt = conn.prepareStatement(sql);
            for (int i = 1; i <= 8; i++) {
                pstmt.setString(i, q);
            }
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
            if (conn == null) {
                for (Proyecto p : obtenerProyectosOfflineRespaldo()) {
                    if ("APROBADO_COORDINACION".equals(p.getEstado()) || "EN_REVISION".equals(p.getEstado())) {
                        lista.add(p);
                    }
                }
                return lista;
            }
            
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
        String sql = "SELECT * FROM proyectos WHERE DATE(fecha_registro) >= DATE(?) AND DATE(fecha_registro) <= DATE(?) ORDER BY fecha_registro DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return obtenerProyectosOfflineRespaldo();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, fechaDesde);
            pstmt.setString(2, fechaHasta);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                lista.add(mapearProyecto(rs));
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en listarPorRangoTiempo: " + e.getMessage());
            return obtenerProyectosOfflineRespaldo();
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public Proyecto obtenerPorId(int idProyecto) {
        String sql = "SELECT * FROM proyectos WHERE id_proyecto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) {
                for (Proyecto p : obtenerProyectosOfflineRespaldo()) {
                    if (p.getIdProyecto() == idProyecto) return p;
                }
                return null;
            }
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProyecto);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapearProyecto(rs);
            }
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en obtenerPorId: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return null;
    }

    public boolean subirArchivo(int idProyecto, String nombreArchivo, byte[] dataArchivo) {
        String sql = "UPDATE proyectos SET archivo_nombre = ?, archivo_data = ?, fecha_actualizacion = NOW() WHERE id_proyecto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreArchivo);
            pstmt.setBytes(2, dataArchivo);
            pstmt.setInt(3, idProyecto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en subirArchivo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    public boolean subirArchivoPorCodigo(String codigoProyecto, String nombreArchivo, byte[] dataArchivo) {
        String sql = "UPDATE proyectos SET archivo_nombre = ?, archivo_data = ?, fecha_actualizacion = NOW() WHERE codigo_proyecto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, nombreArchivo);
            pstmt.setBytes(2, dataArchivo);
            pstmt.setString(3, codigoProyecto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en subirArchivoPorCodigo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    public boolean eliminarArchivoPorCodigo(String codigoProyecto) {
        String sql = "UPDATE proyectos SET archivo_nombre = NULL, archivo_data = NULL, fecha_actualizacion = NOW() WHERE codigo_proyecto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoProyecto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en eliminarArchivoPorCodigo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    public boolean eliminarArchivo(int idProyecto) {
        String sql = "UPDATE proyectos SET archivo_nombre = NULL, archivo_data = NULL, fecha_actualizacion = NOW() WHERE id_proyecto = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return false;
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProyecto);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error en eliminarArchivo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    private List<Proyecto> obtenerProyectosOfflineRespaldo() {
        List<Proyecto> rescate = new ArrayList<>();
        rescate.add(new Proyecto(1, "PROY-SUIZA-2026-001", "Sistema Web de Registro de Titulación IESTP Suiza", "Desarrollo de Sistemas de Información", "Tesis o Proyecto de Aplicación Profesional", "Ing. Ruber Torres Arevalo", "APROBADO_COORDINACION", java.sql.Date.valueOf("2026-06-20"), null, "Tesis_IESTP_Suiza_2026.pdf", new byte[]{1,2,3}));
        rescate.add(new Proyecto(2, "PROY-SUIZA-2026-002", "Aplicación Móvil para Triage y Gestión Citas Médicas Pucallpa", "Desarrollo de Sistemas de Información", "Tesis o Proyecto de Aplicación Profesional", "Lic. Carlos Mendoza", "EN_REVISION", java.sql.Date.valueOf("2026-07-10"), null, "Anteproyecto_Triage_Pucallpa.pdf", new byte[]{1,2,3}));
        rescate.add(new Proyecto(3, "PROY-SUIZA-2026-003", "Sistema Contable Automatizado para PYMES de Ucayali", "Contabilidad", "Examen de Suficiencia Profesional", "Mg. Elena Flores", "OBSERVADO", java.sql.Date.valueOf("2026-07-14"), null, null, null));
        return rescate;
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
        Proyecto p = new Proyecto(
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
        try {
            p.setArchivoNombre(rs.getString("archivo_nombre"));
            p.setArchivoData(rs.getBytes("archivo_data"));
        } catch (SQLException ignored) {
            // Compatibilidad si la columna aún se está procesando o migrando
        }
        return p;
    }

    private void cerrarRecursos(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al cerrar recursos: " + e.getMessage());
        }
    }
    
    private void cerrarRecursos(Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("[ProyectoDAO] Error al cerrar recursos: " + e.getMessage());
        }
    }
}


