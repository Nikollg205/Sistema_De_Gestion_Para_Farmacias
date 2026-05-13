package data;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * DAO de roles del sistema (Administrador, Cajero, etc.).
 */
public class RolDAO {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public RolDAO() {
        CON = Conexion.getInstancia();
    }

    // Lista roles filtrando por nombre.
    public List<Object[]> listar(String texto) {
        List<Object[]> registros = new ArrayList<>();
        try {
            String sql = "SELECT id_rol, nombre_rol, descripcion_rol FROM rol WHERE nombre_rol LIKE ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Object[]{
                    rs.getString("id_rol"),
                    rs.getString("nombre_rol"),
                    rs.getString("descripcion_rol")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return registros;
    }

    // Lista compacta de pares id/nombre para combos o selectores.
    public List<String[]> listarNombresIds() {
        List<String[]> lista = new ArrayList<>();
        try {
            String sql = "SELECT id_rol, nombre_rol FROM rol ORDER BY nombre_rol";
            ps = CON.conectar().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{rs.getString("id_rol"), rs.getString("nombre_rol")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return lista;
    }

    // Inserta un nuevo rol en catálogo.
    public boolean insertar(String idRol, String nombre, String descripcion) {
        resp = false;
        try {
            String sql = "INSERT INTO rol (id_rol, nombre_rol, descripcion_rol) VALUES (?,?,?)";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idRol);
            ps.setString(2, nombre);
            ps.setString(3, descripcion);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Actualiza nombre y descripción del rol.
    public boolean actualizar(String idRol, String nombre, String descripcion) {
        resp = false;
        try {
            String sql = "UPDATE rol SET nombre_rol=?, descripcion_rol=? WHERE id_rol=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setString(3, idRol);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Elimina rol por identificador.
    public boolean eliminar(String idRol) {
        resp = false;
        try {
            String sql = "DELETE FROM rol WHERE id_rol=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idRol);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Cuenta total de roles existentes.
    public int total() {
        int total = 0;
        try {
            String sql = "SELECT COUNT(*) FROM rol";
            ps = CON.conectar().prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return total;
    }

    // Verifica si un nombre de rol ya existe.
    public boolean existe(String nombre) {
        boolean resp = false;
        try {
            String sql = "SELECT nombre_rol FROM rol WHERE nombre_rol=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            resp = rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Libera PreparedStatement, ResultSet y conexión.
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
