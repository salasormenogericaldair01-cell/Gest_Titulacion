package pe.edu.suiza.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.suiza.modelo.Observacion;

/**
 * DAO para registro e historial de observaciones metodológicas en XAMPP MySQL.
 * Paquete modular: pe.edu.suiza.dao
 */
public class ObservacionDAO {

    public boolean registrarObservacionPorCodigo(String codigoProyecto, int idUsuario, String rolAutor, String descripcion, String estadoObs) {
        String sqlBusqueda = "SELECT id_proyecto FROM proyectos WHERE codigo_proyecto = ?";
        String sqlInsert = "INSERT INTO observaciones (id_proyecto, id_usuario, rol_autor, descripcion, estado_observacion) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmtBusq = null;
        PreparedStatement pstmtIns = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return false;

            pstmtBusq = conn.prepareStatement(sqlBusqueda);
            pstmtBusq.setString(1, codigoProyecto);
            rs = pstmtBusq.executeQuery();

            if (rs.next()) {
                int idProyecto = rs.getInt("id_proyecto");

                pstmtIns = conn.prepareStatement(sqlInsert);
                pstmtIns.setInt(1, idProyecto);
                if (idUsuario > 0) {
                    pstmtIns.setInt(2, idUsuario);
                } else {
                    pstmtIns.setNull(2, java.sql.Types.INTEGER);
                }
                pstmtIns.setString(3, rolAutor);
                pstmtIns.setString(4, descripcion);
                pstmtIns.setString(5, estadoObs);

                return pstmtIns.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.err.println("[ObservacionDAO] Error en registrarObservacionPorCodigo: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmtBusq, rs);
            cerrarRecursos(pstmtIns, null);
        }
        return false;
    }

    public List<Observacion> listarPorRol(String rolAutor) {
        List<Observacion> lista = new ArrayList<>();
        String sql = "SELECT o.*, p.codigo_proyecto, p.titulo "
                   + "FROM observaciones o "
                   + "LEFT JOIN proyectos p ON o.id_proyecto = p.id_proyecto "
                   + "WHERE o.rol_autor = ? "
                   + "ORDER BY o.fecha_observacion DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, rolAutor);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Observacion obs = mapearObservacion(rs);
                obs.setCodigoProyectoAux(rs.getString("codigo_proyecto") != null ? rs.getString("codigo_proyecto") : "GENERAL/TI");
                obs.setTituloProyectoAux(rs.getString("titulo") != null ? rs.getString("titulo") : "Solicitud TI / Otro");
                lista.add(obs);
            }
        } catch (SQLException e) {
            System.err.println("[ObservacionDAO] Error en listarPorRol: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public List<Observacion> listarPorCodigoProyecto(String codigoProyecto) {
        List<Observacion> lista = new ArrayList<>();
        String sql = "SELECT o.*, p.codigo_proyecto, p.titulo "
                   + "FROM observaciones o "
                   + "INNER JOIN proyectos p ON o.id_proyecto = p.id_proyecto "
                   + "WHERE p.codigo_proyecto = ? "
                   + "ORDER BY o.fecha_observacion DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, codigoProyecto);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Observacion obs = mapearObservacion(rs);
                obs.setCodigoProyectoAux(rs.getString("codigo_proyecto"));
                obs.setTituloProyectoAux(rs.getString("titulo"));
                lista.add(obs);
            }
        } catch (SQLException e) {
            System.err.println("[ObservacionDAO] Error en listarPorCodigoProyecto: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public List<Observacion> listarTodas() {
        List<Observacion> lista = new ArrayList<>();
        String sql = "SELECT o.*, p.codigo_proyecto, p.titulo "
                   + "FROM observaciones o "
                   + "LEFT JOIN proyectos p ON o.id_proyecto = p.id_proyecto "
                   + "ORDER BY o.fecha_observacion DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Observacion obs = mapearObservacion(rs);
                obs.setCodigoProyectoAux(rs.getString("codigo_proyecto") != null ? rs.getString("codigo_proyecto") : "GENERAL/TI");
                obs.setTituloProyectoAux(rs.getString("titulo") != null ? rs.getString("titulo") : "Solicitud TI / Otro");
                lista.add(obs);
            }
        } catch (SQLException e) {
            System.err.println("[ObservacionDAO] Error en listarTodas: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public List<Observacion> listarTodos() {
        return listarTodas();
    }

    private Observacion mapearObservacion(ResultSet rs) throws SQLException {
        return new Observacion(
            rs.getInt("id_observacion"),
            rs.getInt("id_proyecto"),
            rs.getInt("id_usuario"),
            rs.getString("rol_autor"),
            rs.getString("descripcion"),
            rs.getTimestamp("fecha_observacion"),
            rs.getString("estado_observacion")
        );
    }

    private void cerrarRecursos(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.err.println("[ObservacionDAO] Error al cerrar recursos: " + e.getMessage());
        }
    }
}
