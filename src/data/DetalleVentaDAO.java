package data;

import database.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class DetalleVentaDAO {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public DetalleVentaDAO() {
        CON = Conexion.getInstancia();
    }

    public List<Object[]> listarPorFactura(String idFactura) {
        List<Object[]> registros = new ArrayList<>();
        try {
            String sql = "SELECT d.id_detalle_venta, d.cantidad_vendida, m.nombre_medicamento, m.precio, (d.cantidad_vendida * m.precio) as subtotal "
                    + "FROM detalle_venta d INNER JOIN medicamento m ON d.id_medicamento = m.id_medicamento "
                    + "WHERE d.id_factura = ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idFactura);
            rs = ps.executeQuery();
            while (rs.next()) {
                registros.add(new Object[]{
                    rs.getString("id_detalle_venta"),
                    rs.getInt("cantidad_vendida"),
                    rs.getString("nombre_medicamento"),
                    rs.getDouble("precio"),
                    rs.getDouble("subtotal")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return registros;
    }

    public boolean insertar(String idDetalle, String idFactura, String idMedicamento, int cantidad) {
        resp = false;
        try {
            String sql = "INSERT INTO detalle_venta (id_detalle_venta, id_factura, id_medicamento, cantidad_vendida) VALUES (?,?,?,?)";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idDetalle);
            ps.setString(2, idFactura);
            ps.setString(3, idMedicamento);
            ps.setInt(4, cantidad);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    public boolean eliminar(String idDetalle) {
        resp = false;
        try {
            String sql = "DELETE FROM detalle_venta WHERE id_detalle_venta=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idDetalle);
            resp = ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return resp;
    }

    public boolean eliminarPorFactura(String idFactura) {
        resp = false;
        try {
            String sql = "DELETE FROM detalle_venta WHERE id_factura=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, idFactura);
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
            String sql = "SELECT COUNT(*) FROM detalle_venta";
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
