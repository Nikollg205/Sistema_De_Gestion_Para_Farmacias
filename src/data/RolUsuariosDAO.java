package data;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * DAO de relación usuario-rol (tabla puente rol_usuarios).
 */
public class RolUsuariosDAO {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public RolUsuariosDAO() {
        CON = Conexion.getInstancia();
    }

    // Devuelve todos los roles asignados a un usuario.
    public List<Object[]> listarPorUsuario(String idUsuario) {
        List<Object[]> registros = new ArrayList<>();
        try {
            String sql = "SELECT ru.id_rol, r.nombre_rol FROM rol_usuarios ru INNER JOIN rol r ON ru.id_rol = r.id_rol WHERE ru.id_usuario = ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Object[]{
                    rs.getString("id_rol"),
                    rs.getString("nombre_rol")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return registros;
    }

    // Asigna un rol específico a un usuario.
    public boolean asignarRol(String idUsuario, String idRol) {
        resp = false;
        try {
            String sql = "INSERT INTO rol_usuarios (id_usuario, id_rol) VALUES (?,?)";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.setString(2, idRol);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Remueve una asignación puntual de rol.
    public boolean removerRol(String idUsuario, String idRol) {
        resp = false;
        try {
            String sql = "DELETE FROM rol_usuarios WHERE id_usuario=? AND id_rol=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.setString(2, idRol);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Limpia todas las asignaciones de rol del usuario.
    public boolean removerTodosRoles(String idUsuario) {
        resp = false;
        try {
            String sql = "DELETE FROM rol_usuarios WHERE id_usuario=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idUsuario);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Indica si el usuario ya posee un rol determinado.
    public boolean tieneRol(String idUsuario, String idRol) {
        boolean resp = false;
        try {
            String sql = "SELECT id_rol FROM rol_usuarios WHERE id_usuario=? AND id_rol=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.setString(2, idRol);
            rs = ps.executeQuery();
            resp = rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Cierre de recursos JDBC usados internamente.
    private void cerrar() {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
            CON.desconectar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}
