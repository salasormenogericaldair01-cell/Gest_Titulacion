package pe.edu.suiza.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.suiza.modelo.Estudiante;

/**
 * DAO para gestión de estudiantes integrantes en XAMPP MySQL.
 * Paquete modular: pe.edu.suiza.dao
 */
public class EstudianteDAO {

    public boolean insertarEstudiante(Estudiante est) {
        String sql = "INSERT INTO estudiantes (id_proyecto, dni_codigo, nombres, apellidos) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return false;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, est.getIdProyecto());
            pstmt.setString(2, est.getDniCodigo());
            pstmt.setString(3, est.getNombres());
            pstmt.setString(4, est.getApellidos());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] Error en insertarEstudiante: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    public List<Estudiante> listarPorProyecto(int idProyecto) {
        List<Estudiante> lista = new ArrayList<>();
        String sql = "SELECT * FROM estudiantes WHERE id_proyecto = ? ORDER BY id_estudiante ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, idProyecto);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Estudiante(
                    rs.getInt("id_estudiante"),
                    rs.getInt("id_proyecto"),
                    rs.getString("dni_codigo"),
                    rs.getString("nombres"),
                    rs.getString("apellidos")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] Error en listarPorProyecto: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    private void cerrarRecursos(PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            System.err.println("[EstudianteDAO] Error al cerrar recursos: " + e.getMessage());
        }
    }
}
