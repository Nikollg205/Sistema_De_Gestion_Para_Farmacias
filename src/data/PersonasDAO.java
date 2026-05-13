package data;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * PersonasDAO: clase del proyecto HealthPharmacy.
 */
public class PersonasDAO {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public PersonasDAO() {
        CON = Conexion.getInstancia();
    }

    public List<Object[]> listar(String texto) {
        List<Object[]> registros = new ArrayList<>();
        try {
            String sql = "SELECT p.id_persona, p.nombre_persona, p.telefono_persona, p.correo_persona, p.fecha_registro, "
                    + "(SELECT COUNT(*) FROM usuario u WHERE u.id_persona = p.id_persona) as tiene_usuario "
                    + "FROM personas p WHERE p.nombre_persona LIKE ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Object[]{
                    rs.getString("id_persona"),
                    rs.getString("nombre_persona"),
                    rs.getString("telefono_persona"),
                    rs.getString("correo_persona"),
                    rs.getTimestamp("fecha_registro"),
                    rs.getInt("tiene_usuario")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return registros;
    }

    public List<String[]> listarNombresIds() {
        List<String[]> lista = new ArrayList<>();
        try {
            String sql = "SELECT id_persona, nombre_persona FROM personas ORDER BY nombre_persona";
            ps = CON.conectar().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new String[]{rs.getString("id_persona"), rs.getString("nombre_persona")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return lista;
    }

    public boolean insertar(String idPersona, String nombre, String telefono, String correo) {
        resp = false;
        try {
            String sql = "INSERT INTO personas (id_persona, nombre_persona, telefono_persona, correo_persona, fecha_registro) VALUES (?,?,?,?,NOW())";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idPersona);
            ps.setString(2, nombre);
            ps.setString(3, telefono);
            ps.setString(4, correo);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    public boolean actualizar(String idPersona, String nombre, String telefono, String correo) {
        resp = false;
        try {
            String sql = "UPDATE personas SET nombre_persona=?, telefono_persona=?, correo_persona=? WHERE id_persona=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setString(3, correo);
            ps.setString(4, idPersona);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    public boolean eliminar(String idPersona) {
        resp = false;
        try {
            String sql = "DELETE FROM personas WHERE id_persona=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idPersona);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    public int total() {
        int total = 0;
        try {
            String sql = "SELECT COUNT(*) FROM personas";
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

    public boolean existe(String texto) {
        boolean resp = false;
        try {
            String sql = "SELECT nombre_persona FROM personas WHERE nombre_persona=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, texto);
            rs = ps.executeQuery();
            resp = rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

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

