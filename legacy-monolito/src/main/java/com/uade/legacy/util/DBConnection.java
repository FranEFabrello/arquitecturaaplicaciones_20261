package com.uade.legacy.util;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String URL = "jdbc:h2:mem:legacydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static boolean initialized = false;

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        if (!initialized) {
            initDB(conn);
            initialized = true;
        }
        return conn;
    }

    private static void initDB(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS usuarios (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  username VARCHAR(50) NOT NULL UNIQUE," +
                "  password VARCHAR(255) NOT NULL," +
                "  nombre VARCHAR(100) NOT NULL," +
                "  rol VARCHAR(20) NOT NULL DEFAULT 'USER'" +
                ")"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS productos (" +
                "  id INT AUTO_INCREMENT PRIMARY KEY," +
                "  nombre VARCHAR(100) NOT NULL," +
                "  descripcion VARCHAR(500)," +
                "  precio DOUBLE NOT NULL," +
                "  stock INT NOT NULL DEFAULT 0," +
                "  categoria VARCHAR(50)" +
                ")"
            );

            String hashAdmin = BCrypt.hashpw("admin123", BCrypt.gensalt());
            String hashUser  = BCrypt.hashpw("user123",  BCrypt.gensalt());

            stmt.executeUpdate(
                "MERGE INTO usuarios (username, password, nombre, rol) KEY(username) VALUES " +
                "('admin', '" + hashAdmin + "', 'Administrador', 'ADMIN')"
            );
            stmt.executeUpdate(
                "MERGE INTO usuarios (username, password, nombre, rol) KEY(username) VALUES " +
                "('usuario', '" + hashUser + "', 'Usuario Demo', 'USER')"
            );

            stmt.executeUpdate(
                "MERGE INTO productos (id, nombre, descripcion, precio, stock, categoria) KEY(id) VALUES " +
                "(1, 'Laptop HP', 'Laptop HP 15 pulgadas Intel Core i5', 850.00, 10, 'Electronica')"
            );
            stmt.executeUpdate(
                "MERGE INTO productos (id, nombre, descripcion, precio, stock, categoria) KEY(id) VALUES " +
                "(2, 'Mouse Inalambrico', 'Mouse optico inalambrico USB', 25.00, 50, 'Perifericos')"
            );
            stmt.executeUpdate(
                "MERGE INTO productos (id, nombre, descripcion, precio, stock, categoria) KEY(id) VALUES " +
                "(3, 'Teclado Mecanico', 'Teclado mecanico RGB retroiluminado', 75.00, 30, 'Perifericos')"
            );

            // Resetear la secuencia para que AUTO_INCREMENT no colisione con los IDs manuales
            stmt.executeUpdate("ALTER TABLE productos ALTER COLUMN id RESTART WITH 4");
            stmt.executeUpdate("ALTER TABLE usuarios ALTER COLUMN id RESTART WITH 10");
        }
    }
}
