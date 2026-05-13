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

    /**
     * DTO simple para mostrar una fila de ventas en reportes.
     */
    public static class VentaReporteItem {
        private final String idFactura;
        private final Timestamp fechaFactura;
        private final String vendedorNombre;
        private final String estadoFactura;
        private final double totalFactura;

        public VentaReporteItem(String idFactura, Timestamp fechaFactura, String vendedorNombre, String estadoFactura, double totalFactura) {
            this.idFactura = idFactura;
            this.fechaFactura = fechaFactura;
            this.vendedorNombre = vendedorNombre;
            this.estadoFactura = estadoFactura;
            this.totalFactura = totalFactura;
        }

        public String getIdFactura() {
            return idFactura;
        }

        public Timestamp getFechaFactura() {
            return fechaFactura;
        }

        public String getVendedorNombre() {
            return vendedorNombre;
        }

        public String getEstadoFactura() {
            return estadoFactura;
        }

        public double getTotalFactura() {
            return totalFactura;
        }
    }

    /**
     * DTO para las métricas generales del rango de fechas.
     */
    public static class ResumenVentas {
        private final double totalVendido;
        private final int cantidadFacturas;
        private final double ticketPromedio;

        public ResumenVentas(double totalVendido, int cantidadFacturas, double ticketPromedio) {
            this.totalVendido = totalVendido;
            this.cantidadFacturas = cantidadFacturas;
            this.ticketPromedio = ticketPromedio;
        }

        public double getTotalVendido() {
            return totalVendido;
        }

        public int getCantidadFacturas() {
            return cantidadFacturas;
        }

        public double getTicketPromedio() {
            return ticketPromedio;
        }
    }

    /**
     * DTO para ranking básico de productos vendidos.
     */
    public static class ProductoReporteItem {
        private final String nombreProducto;
        private final int unidadesVendidas;
        private final double ingresoEstimado;

        public ProductoReporteItem(String nombreProducto, int unidadesVendidas, double ingresoEstimado) {
            this.nombreProducto = nombreProducto;
            this.unidadesVendidas = unidadesVendidas;
            this.ingresoEstimado = ingresoEstimado;
        }

        public String getNombreProducto() {
            return nombreProducto;
        }

        public int getUnidadesVendidas() {
            return unidadesVendidas;
        }

        public double getIngresoEstimado() {
            return ingresoEstimado;
        }
    }

    /**
     * Lista vendedores para poblar el filtro de reportes.
     */
    public List<String[]> listarVendedores() {
        List<String[]> vendedores = new ArrayList<>();
        try {
            String sql = "SELECT u.id_usuario, COALESCE(p.nombre_persona, u.nombre_usuario, u.id_usuario) as nombre " +
                    "FROM usuario u " +
                    "LEFT JOIN personas p ON p.id_persona = u.id_persona " +
                    "WHERE u.activo = 1 OR u.activo IS NULL " +
                    "ORDER BY nombre";
            ps = CON.conectar().prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                vendedores.add(new String[]{rs.getString("id_usuario"), rs.getString("nombre")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return vendedores;
    }

    /**
     * Devuelve ventas del rango con filtro opcional de vendedor.
     */
    public List<VentaReporteItem> getVentasPorRango(Timestamp fechaInicio, Timestamp fechaFin, String idVendedor) {
        List<VentaReporteItem> items = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT f.id_factura, f.fecha_factura, f.estado_factura, f.precio_total, ");
            sql.append("COALESCE(p.nombre_persona, u.nombre_usuario, f.vendedor, 'Sin vendedor') as vendedor_nombre ");
            sql.append("FROM factura f ");
            sql.append("LEFT JOIN usuario u ON f.vendedor = u.id_usuario ");
            sql.append("LEFT JOIN personas p ON u.id_persona = p.id_persona ");
            sql.append("WHERE f.fecha_factura BETWEEN ? AND ? ");
            if (idVendedor != null && !idVendedor.trim().isEmpty()) {
                sql.append("AND (");
                sql.append("f.vendedor = ? ");
                sql.append("OR f.vendedor = (SELECT u2.nombre_usuario FROM usuario u2 WHERE u2.id_usuario = ? LIMIT 1) ");
                sql.append("OR f.vendedor = (");
                sql.append("SELECT p2.nombre_persona FROM usuario u3 ");
                sql.append("LEFT JOIN personas p2 ON p2.id_persona = u3.id_persona ");
                sql.append("WHERE u3.id_usuario = ? LIMIT 1");
                sql.append(")) ");
            }
            sql.append("ORDER BY f.fecha_factura DESC");

            ps = CON.conectar().prepareStatement(sql.toString());
            ps.setTimestamp(1, fechaInicio);
            ps.setTimestamp(2, fechaFin);
            if (idVendedor != null && !idVendedor.trim().isEmpty()) {
                ps.setString(3, idVendedor);
                ps.setString(4, idVendedor);
                ps.setString(5, idVendedor);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(new VentaReporteItem(
                        rs.getString("id_factura"),
                        rs.getTimestamp("fecha_factura"),
                        rs.getString("vendedor_nombre"),
                        rs.getString("estado_factura"),
                        rs.getDouble("precio_total")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return items;
    }

    /**
     * Calcula total, cantidad de facturas y ticket promedio en un rango.
     */
    public ResumenVentas getResumenVentasRango(Timestamp fechaInicio, Timestamp fechaFin, String idVendedor) {
        double total = 0;
        int cantidad = 0;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT COALESCE(SUM(f.precio_total), 0) as total, COUNT(*) as cantidad ");
            sql.append("FROM factura f WHERE f.fecha_factura BETWEEN ? AND ? ");
            if (idVendedor != null && !idVendedor.trim().isEmpty()) {
                sql.append("AND (");
                sql.append("f.vendedor = ? ");
                sql.append("OR f.vendedor = (SELECT u2.nombre_usuario FROM usuario u2 WHERE u2.id_usuario = ? LIMIT 1) ");
                sql.append("OR f.vendedor = (");
                sql.append("SELECT p2.nombre_persona FROM usuario u3 ");
                sql.append("LEFT JOIN personas p2 ON p2.id_persona = u3.id_persona ");
                sql.append("WHERE u3.id_usuario = ? LIMIT 1");
                sql.append(")) ");
            }
            ps = CON.conectar().prepareStatement(sql.toString());
            ps.setTimestamp(1, fechaInicio);
            ps.setTimestamp(2, fechaFin);
            if (idVendedor != null && !idVendedor.trim().isEmpty()) {
                ps.setString(3, idVendedor);
                ps.setString(4, idVendedor);
                ps.setString(5, idVendedor);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
                cantidad = rs.getInt("cantidad");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        double ticket = cantidad > 0 ? (total / cantidad) : 0;
        return new ResumenVentas(total, cantidad, ticket);
    }

    /**
     * Obtiene top productos por unidades e ingreso estimado.
     * El ingreso se estima con el precio actual de medicamento.
     */
    public List<ProductoReporteItem> getTopProductos(Timestamp fechaInicio, Timestamp fechaFin, String idVendedor, int limite) {
        List<ProductoReporteItem> items = new ArrayList<>();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT m.nombre_medicamento, ");
            sql.append("COALESCE(SUM(d.cantidad_vendida), 0) as unidades, ");
            sql.append("COALESCE(SUM(d.cantidad_vendida * m.precio), 0) as ingreso ");
            sql.append("FROM detalle_venta d ");
            sql.append("INNER JOIN factura f ON f.id_factura = d.id_factura ");
            sql.append("INNER JOIN medicamento m ON m.id_medicamento = d.id_medicamento ");
            sql.append("WHERE f.fecha_factura BETWEEN ? AND ? ");
            if (idVendedor != null && !idVendedor.trim().isEmpty()) {
                sql.append("AND (");
                sql.append("f.vendedor = ? ");
                sql.append("OR f.vendedor = (SELECT u2.nombre_usuario FROM usuario u2 WHERE u2.id_usuario = ? LIMIT 1) ");
                sql.append("OR f.vendedor = (");
                sql.append("SELECT p2.nombre_persona FROM usuario u3 ");
                sql.append("LEFT JOIN personas p2 ON p2.id_persona = u3.id_persona ");
                sql.append("WHERE u3.id_usuario = ? LIMIT 1");
                sql.append(")) ");
            }
            sql.append("GROUP BY m.id_medicamento, m.nombre_medicamento ");
            sql.append("ORDER BY unidades DESC, ingreso DESC ");
            sql.append("LIMIT ?");

            ps = CON.conectar().prepareStatement(sql.toString());
            ps.setTimestamp(1, fechaInicio);
            ps.setTimestamp(2, fechaFin);
            int nextParam = 3;
            if (idVendedor != null && !idVendedor.trim().isEmpty()) {
                ps.setString(nextParam, idVendedor);
                nextParam++;
                ps.setString(nextParam, idVendedor);
                nextParam++;
                ps.setString(nextParam, idVendedor);
                nextParam++;
            }
            ps.setInt(nextParam, limite);

            rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new ProductoReporteItem(
                        rs.getString("nombre_medicamento"),
                        rs.getInt("unidades"),
                        rs.getDouble("ingreso")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }
        return items;
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
