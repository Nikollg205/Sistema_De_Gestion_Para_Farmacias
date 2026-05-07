package data;

import database.Conexion;
import data.interfaces.CrudSimpleInterface;
import inventario.Proveedor;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing Proveedor based on real DB schema
 */
public class ProveedorDAO implements CrudSimpleInterface<Proveedor> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public ProveedorDAO() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Proveedor> listar(String texto) {
        List<Proveedor> registros = new ArrayList<>();

        try {
            String sql = "SELECT * FROM proveedor WHERE nombre_proveedor LIKE ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Proveedor prov = new Proveedor();
                prov.setId(rs.getString("id_proveedor"));
                prov.setNombre(rs.getString("nombre_proveedor"));
                prov.setProducto(rs.getString("producto_proveedor"));
                prov.setTelefono(rs.getString("telefono_proveedor"));
                prov.setCorreo(rs.getString("correo_proveedor"));
                prov.setEstado(rs.getString("estado"));

                registros.add(prov);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return registros;
    }

    @Override
    public boolean insertar(Proveedor obj) {
        resp = false;

        try {
            String sql = "INSERT INTO proveedor (id_proveedor, nombre_proveedor, producto_proveedor, telefono_proveedor, correo_proveedor) VALUES (?,?,?,?,?)";

            ps = CON.conectar().prepareStatement(sql);

            ps.setString(1, obj.getId());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getProducto());
            ps.setString(4, obj.getTelefono());
            ps.setString(5, obj.getCorreo());

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    @Override
    public boolean actualizar(Proveedor obj) {
        resp = false;

        try {
            String sql = "UPDATE proveedor SET nombre_proveedor=?, producto_proveedor=?, telefono_proveedor=?, correo_proveedor=? WHERE id_proveedor=?";

            ps = CON.conectar().prepareStatement(sql);

            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getProducto());
            ps.setString(3, obj.getTelefono());
            ps.setString(4, obj.getCorreo());
            ps.setString(5, obj.getId());

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    @Override
    public int total() {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) FROM proveedor";
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

    @Override
    public boolean existe(String texto) {
        resp = false;

        try {
            String sql = "SELECT nombre_proveedor FROM proveedor WHERE nombre_proveedor=?";
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

    /**
     * Gets the number of products associated with a supplier
     */
    public int getProductosCount(String idProveedor) {
        int count = 0;

        try {
            String sql = "SELECT COUNT(*) FROM lote WHERE id_proveedor = ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idProveedor);

            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return count;
    }

    /**
     * Toggles provider status between Activo and Inactivo
     */
    public boolean toggleEstado(String idProveedor) {
        boolean resp = false;

        try {
            String sql = "UPDATE proveedor SET estado = IF(estado = 'Activo', 'Inactivo', 'Activo') WHERE id_proveedor = ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idProveedor);

            resp = ps.executeUpdate() > 0;

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
