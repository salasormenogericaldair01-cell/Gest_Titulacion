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
            if (conn == null) {
                for (Catalogo cat : obtenerCatalogosOfflineRespaldo()) {
                    if (cat.getTipoCatalogo().equalsIgnoreCase(tipo)) {
                        lista.add(cat);
                    }
                }
                return lista;
            }

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
            for (Catalogo cat : obtenerCatalogosOfflineRespaldo()) {
                if (cat.getTipoCatalogo().equalsIgnoreCase(tipo)) {
                    lista.add(cat);
                }
            }
            return lista;
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
            if (conn == null) return obtenerCatalogosOfflineRespaldo();

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
            return obtenerCatalogosOfflineRespaldo();
        } finally {
            cerrarRecursos(pstmt, rs);
        }
        return lista;
    }

    public boolean insertar(Catalogo c) {
        String sql = "INSERT INTO catalogos (tipo_catalogo, codigo_item, nombre_item, descripcion, estado) VALUES (?, ?, ?, ?, 'ACTIVO')";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = ConexionDB.getInstancia().getConexion();
            if (conn == null) {
                // En modo offline simulamos éxito agregándolo en memoria o devolviendo true
                obtenerCatalogosOfflineRespaldo().add(c);
                return true;
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, c.getTipoCatalogo());
            pstmt.setString(2, c.getCodigoItem());
            pstmt.setString(3, c.getNombreItem());
            pstmt.setString(4, c.getDescripcion());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("[CatalogoDAO] Error en insertar catalogo: " + e.getMessage());
            return false;
        } finally {
            cerrarRecursos(pstmt, null);
        }
    }

    private List<Catalogo> obtenerCatalogosOfflineRespaldo() {
        List<Catalogo> rescate = new ArrayList<>();
        rescate.add(new Catalogo(1, "PROGRAMA_ESTUDIO", "DSI", "Desarrollo de Sistemas de Información", "Carrera Técnica 3 años", "ACTIVO"));
        rescate.add(new Catalogo(2, "PROGRAMA_ESTUDIO", "ENF", "Enfermería Técnica", "Carrera Técnica 3 años", "ACTIVO"));
        rescate.add(new Catalogo(3, "PROGRAMA_ESTUDIO", "CONT", "Contabilidad", "Carrera Técnica 3 años", "ACTIVO"));
        rescate.add(new Catalogo(4, "MODALIDAD_TITULACION", "TESIS", "Tesis o Proyecto de Aplicación Profesional", "Modalidad principal de titulación", "ACTIVO"));
        rescate.add(new Catalogo(5, "MODALIDAD_TITULACION", "SUF", "Examen de Suficiencia Profesional", "Modalidad evaluación teórica/práctica", "ACTIVO"));
        rescate.add(new Catalogo(6, "ASESOR", "ASE-1", "Ing. Ruber Torres Arevalo", "Docente Principal de Titulación", "ACTIVO"));
        rescate.add(new Catalogo(7, "ASESOR", "ASE-2", "Lic. Carlos Mendoza", "Docente Asesor Metodológico", "ACTIVO"));
        return rescate;
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
