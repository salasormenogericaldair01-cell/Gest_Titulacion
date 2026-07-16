package pe.edu.suiza.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import pe.edu.suiza.modelo.Catalogo;

/**
 * DAO para gestión de catálogos (carreras, modalidades, asesores) en XAMPP MySQL.
 * Paquete modular: pe.edu.suiza.dao
 */
public class CatalogoDAO {

    public List<Catalogo> listarPorTipo(String tipo) {
        List<Catalogo> lista = new ArrayList<>();
        String sql = "SELECT * FROM catalogos WHERE tipo_catalogo = ? AND estado = 'ACTIVO' ORDER BY id_catalogo ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipo);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Catalogo(
                    rs.getInt("id_catalogo"),
                    rs.getString("tipo_catalogo"),
                    rs.getString("codigo_item"),
                    rs.getString("nombre_item"),
                    rs.getString("descripcion"),
                    rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[CatalogoDAO] Error en listarPorTipo: " + e.getMessage());
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public List<Catalogo> listarTodos() {
        List<Catalogo> lista = new ArrayList<>();
        String sql = "SELECT * FROM catalogos ORDER BY tipo_catalogo ASC, id_catalogo ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) return lista;

            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                lista.add(new Catalogo(
                    rs.getInt("id_catalogo"),
                    rs.getString("tipo_catalogo"),
                    rs.getString("codigo_item"),
                    rs.getString("nombre_item"),
                    rs.getString("descripcion"),
                    rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[CatalogoDAO] Error en listarTodos: " + e.getMessage());
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
            System.err.println("[CatalogoDAO] Error al cerrar recursos: " + e.getMessage());
        }
    }
}
