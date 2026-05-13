package data;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * DAO de usuario: operaciones CRUD, autenticación y utilidades de consulta.
 */
public class UsuarioDAO {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public UsuarioDAO() {
        CON = Conexion.getInstancia();
    }

    // Lista usuarios filtrados por nombre con datos de persona asociados.
    public List<Object[]> listar(String texto) {
        List<Object[]> registros = new ArrayList<>();
        try {
            String sql = "SELECT u.id_usuario, u.nombre_usuario, u.activo, u.ultimo_acceso, p.nombre_persona, p.correo_persona, p.telefono_persona "
                    + "FROM usuario u LEFT JOIN personas p ON u.id_persona = p.id_persona "
                    + "WHERE u.nombre_usuario LIKE ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Object[]{
                    rs.getString("id_usuario"),
                    rs.getString("nombre_usuario"),
                    rs.getInt("activo"),
                    rs.getTimestamp("ultimo_acceso"),
                    rs.getString("nombre_persona"),
                    rs.getString("correo_persona"),
                    rs.getString("telefono_persona")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return registros;
    }

    // Inserta un usuario activo en la tabla usuario.
    public boolean insertar(String idUsuario, String idPersona, String nombreUsuario, String contrasena) {
        resp = false;
        try {
            String sql = "INSERT INTO usuario (id_usuario, id_persona, nombre_usuario, contraseña_usuario, activo) VALUES (?,?,?,?,1)";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idUsuario);
            ps.setString(2, idPersona);
            ps.setString(3, nombreUsuario);
            ps.setString(4, contrasena);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Actualiza datos base del usuario y su estado.
    public boolean actualizar(String idUsuario, String idPersona, String nombreUsuario, String contrasena, int activo) {
        resp = false;
        try {
            String sql = "UPDATE usuario SET id_persona=?, nombre_usuario=?, contraseña_usuario=?, activo=? WHERE id_usuario=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idPersona);
            ps.setString(2, nombreUsuario);
            ps.setString(3, contrasena);
            ps.setInt(4, activo);
            ps.setString(5, idUsuario);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Activa/inactiva un usuario por ID.
    public boolean toggleEstado(String idUsuario, int nuevoEstado) {
        resp = false;
        try {
            String sql = "UPDATE usuario SET activo=? WHERE id_usuario=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setInt(1, nuevoEstado);
            ps.setString(2, idUsuario);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Guarda fecha/hora de último acceso para auditoría básica.
    public boolean actualizarUltimoAcceso(String idUsuario) {
        resp = false;
        try {
            String sql = "UPDATE usuario SET ultimo_acceso=NOW() WHERE id_usuario=?";
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

    // Elimina físicamente un usuario por ID.
    public boolean eliminar(String idUsuario) {
        resp = false;
        try {
            String sql = "DELETE FROM usuario WHERE id_usuario=?";
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

    // Valida si credenciales existen para un usuario activo.
    public boolean verificarCredenciales(String nombreUsuario, String contrasena) {
        boolean existe = false;
        try {
            String sql = "SELECT id_usuario FROM usuario WHERE nombre_usuario=? AND contraseña_usuario=? AND activo=1";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();
            existe = rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return existe;
    }

    // Obtiene el ID del usuario autenticado por credenciales.
    public String getIdUsuario(String nombreUsuario, String contrasena) {
        String id = null;
        try {
            String sql = "SELECT id_usuario FROM usuario WHERE nombre_usuario=? AND contraseña_usuario=? AND activo=1";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getString("id_usuario");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return id;
    }

    // Retorna datos de autenticación junto con el rol asociado.
    public Object[] autenticarConRol(String nombreUsuario, String contrasena) {
        Object[] auth = null;
        try {
            String sql = "SELECT u.id_usuario, u.nombre_usuario, r.nombre_rol "
                    + "FROM usuario u "
                    + "INNER JOIN rol_usuarios ru ON ru.id_usuario = u.id_usuario "
                    + "INNER JOIN rol r ON r.id_rol = ru.id_rol "
                    + "WHERE u.nombre_usuario = ? AND u.contraseña_usuario = ? AND u.activo = 1 "
                    + "LIMIT 1";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();
            if (rs.next()) {
                auth = new Object[]{
                    rs.getString("id_usuario"),
                    rs.getString("nombre_usuario"),
                    rs.getString("nombre_rol")
                };
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return auth;
    }

    // Total de usuarios registrados.
    public int total() {
        int total = 0;
        try {
            String sql = "SELECT COUNT(*) FROM usuario";
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

    // Total de usuarios actualmente activos.
    public int totalActivos() {
        int total = 0;
        try {
            String sql = "SELECT COUNT(*) FROM usuario WHERE activo=1";
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

    // Verifica existencia de username para evitar duplicados.
    public boolean existe(String nombreUsuario) {
        boolean resp = false;
        try {
            String sql = "SELECT nombre_usuario FROM usuario WHERE nombre_usuario=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, nombreUsuario);
            rs = ps.executeQuery();
            resp = rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    // Trae el nombre real de persona asociado al id_usuario.
    public String getNombrePersona(String idUsuario) {
        String nombre = null;
        try {
            String sql = "SELECT p.nombre_persona FROM usuario u INNER JOIN personas p ON u.id_persona = p.id_persona WHERE u.id_usuario=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                nombre = rs.getString("nombre_persona");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return nombre;
    }

    // Cierra recursos JDBC abiertos por el DAO.
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
