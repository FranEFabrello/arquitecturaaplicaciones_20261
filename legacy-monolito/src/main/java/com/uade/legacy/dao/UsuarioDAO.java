package com.uade.legacy.dao;

import com.uade.legacy.model.Usuario;
import com.uade.legacy.util.DBConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UsuarioDAO {

    public Usuario autenticar(String username, String password) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashGuardado = rs.getString("password");
                if (BCrypt.checkpw(password, hashGuardado)) {
                    return mapear(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Usuario buscarPorUsername(String username) {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapear(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("nombre"),
            rs.getString("rol")
        );
    }
}
