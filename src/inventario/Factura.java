package inventario;

import java.sql.Timestamp;

/**
 * Represents an invoice in the pharmacy system.
 * Maps to the 'factura' table in the database.
 */
public class Factura {

    private String id;
    private Timestamp fecha;
    private double precioTotal;
    private double subTotal;
    private double iva;
    private String estado;
    private String vendedor;
    private String idDetalleVenta;

    public Factura() {
    }

    public Factura(String id, Timestamp fecha, double precioTotal, double subTotal, double iva, String estado, String vendedor, String idDetalleVenta) {
        this.id = id;
        this.fecha = fecha;
        this.precioTotal = precioTotal;
        this.subTotal = subTotal;
        this.iva = iva;
        this.estado = estado;
        this.vendedor = vendedor;
        this.idDetalleVenta = idDetalleVenta;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public double getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(double precioTotal) {
        this.precioTotal = precioTotal;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public void setIdDetalleVenta(String idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }
}
