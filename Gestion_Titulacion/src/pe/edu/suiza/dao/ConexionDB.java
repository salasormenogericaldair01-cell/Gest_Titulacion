package pe.edu.suiza.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton para gestión de conexión JDBC a XAMPP MySQL.
 * Base de Datos: gestion_titulacion_suiza
 * Paquete modular: pe.edu.suiza.dao
 */
public class ConexionDB {
    
    private static ConexionDB instancia = null;
    private Connection conexion = null;
    
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_titulacion_suiza?useSSL=false&serverTimezone=America/Lima&allowPublicKeyRetrieval=true&characterEncoding=UTF-8&useUnicode=true&autoReconnect=true&maxAllowedPacket=67108864";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";
    
    private ConexionDB() {
        conectar();
    }
    
    private synchronized void conectar() {
        try {
            if (conexion != null) {
                try { conexion.close(); } catch (Exception ignored) {}
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
            System.out.println("[ConexionDB] Conexión establecida con MySQL XAMPP exitosamente.");
            verificarYMigrarEsquema(conexion);
        } catch (ClassNotFoundException e) {
            System.err.println("[ConexionDB] Error: Driver JDBC MySQL no encontrado. " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error al conectar a la BD XAMPP (gestion_titulacion_suiza): " + e.getMessage());
        }
    }

    private void verificarYMigrarEsquema(Connection conn) {
        if (conn == null) return;
        try (java.sql.Statement stmt = conn.createStatement()) {
            // Migración automática de columnas de archivo para documentación (PDF, Word, etc.) en proyectos
            try {
                stmt.execute("ALTER TABLE proyectos ADD COLUMN archivo_nombre VARCHAR(255) NULL");
            } catch (SQLException ignored) {}
            try {
                stmt.execute("ALTER TABLE proyectos ADD COLUMN archivo_data LONGBLOB NULL");
            } catch (SQLException ignored) {}
            // Asegurar que la tabla observaciones acepte TICKET_TI y nulos adecuadamente
            try {
                stmt.execute("ALTER TABLE observaciones MODIFY COLUMN id_proyecto INT NULL");
            } catch (SQLException ignored) {}
        } catch (Exception e) {
            System.err.println("[ConexionDB] Aviso en verificación de esquema: " + e.getMessage());
        }
    }
    
    public static synchronized ConexionDB getInstancia() {
        if (instancia == null) {
            instancia = new ConexionDB();
        }
        return instancia;
    }
    
    public synchronized Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed() || !conexion.isValid(2)) {
                System.out.println("[ConexionDB] La conexión estaba cerrada, nula o inactiva. Intentando reconectar a XAMPP...");
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error verificando el estado de la conexión: " + e.getMessage());
            conectar();
        }
        return conexion;
    }
    
    public synchronized void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("[ConexionDB] Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("[ConexionDB] Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

