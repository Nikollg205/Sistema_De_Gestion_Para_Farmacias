package data;

import database.Conexion;
import inventario.Factura;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing invoices (factura) and sales details
 */
public class FacturaDAO {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;

    public FacturaDAO() {
        CON = Conexion.getInstancia();
    }

    /**
     * Get total sales for today
     */
    public double getVentasHoy() {
        double total = 0;

        try {
            String sql = "SELECT COALESCE(SUM(precio_total), 0) as total FROM factura WHERE DATE(fecha_factura) = CURDATE()";
            ps = CON.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getDouble("total");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return total;
    }

    /**
     * Get total sales for last 7 days
     */
    public List<Double> getVentasSemanales() {
        List<Double> ventas = new ArrayList<>();

        try {
            String sql = "SELECT COALESCE(SUM(precio_total), 0) as total " +
                    "FROM factura WHERE fecha_factura >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                    "GROUP BY DATE(fecha_factura) ORDER BY DATE(fecha_factura)";
            ps = CON.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                ventas.add(rs.getDouble("total"));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return ventas;
    }

    /**
     * Get total number of invoices today
     */
    public int getFacturasHoy() {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) FROM factura WHERE DATE(fecha_factura) = CURDATE()";
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

    /**
     * Get recent invoices with limit
     */
    public List<Factura> getFacturasRecientes(int limite) {
        List<Factura> facturas = new ArrayList<>();

        try {
            String sql = "SELECT f.*, p.nombre_persona as vendedor_nombre " +
                    "FROM factura f " +
                    "LEFT JOIN usuario u ON f.vendedor = u.id_usuario " +
                    "LEFT JOIN personas p ON u.id_persona = p.id_persona " +
                    "ORDER BY f.fecha_factura DESC LIMIT ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setInt(1, limite);

            rs = ps.executeQuery();

            while (rs.next()) {
                Factura f = new Factura();
                f.setId(rs.getString("id_factura"));
                f.setFecha(rs.getTimestamp("fecha_factura"));
                f.setPrecioTotal(rs.getDouble("precio_total"));
                f.setSubTotal(rs.getDouble("sub_total"));
                f.setIva(rs.getDouble("IVA"));
                f.setEstado(rs.getString("estado_factura"));
                f.setVendedor(rs.getString("vendedor_nombre"));
                facturas.add(f);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return facturas;
    }

    /**
     * Insert a new invoice
     */
    public boolean insertar(Factura obj) {
        boolean resp = false;

        try {
            String sql = "INSERT INTO factura (id_factura, fecha_factura, precio_total, sub_total, IVA, estado_factura, vendedor) VALUES (?,?,?,?,?,?,?)";

            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, obj.getId());
            ps.setTimestamp(2, obj.getFecha());
            ps.setDouble(3, obj.getPrecioTotal());
            ps.setDouble(4, obj.getSubTotal());
            ps.setDouble(5, obj.getIva());
            ps.setString(6, obj.getEstado());
            if (obj.getVendedor() != null) {
                ps.setString(7, obj.getVendedor());
            } else {
                ps.setNull(7, java.sql.Types.VARCHAR);
            }

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    /**
     * Insert a sales detail line
     */
    public boolean insertarDetalle(String idDetalle, String idFactura, String idMedicamento, int cantidad) {
        boolean resp = false;

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

    /**
     * Update invoice state
     */
    public boolean actualizarEstado(String idFactura, String estado) {
        boolean resp = false;

        try {
            String sql = "UPDATE factura SET estado_factura=? WHERE id_factura=?";

            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, estado);
            ps.setString(2, idFactura);

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    /**
     * Get low stock medicines count
     */
    public int getAlertasStock() {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) FROM medicamento WHERE stock_medicamento <= 10";
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
