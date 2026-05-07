package inventario;

/**
 * Represents a supplier/provider in the pharmacy system.
 * Maps to the 'proveedor' table in the database.
 */
public class Proveedor {

    private String id;
    private String nombre;
    private String producto;
    private String telefono;
    private String correo;
    private String estado;

    public Proveedor() {
    }

    public Proveedor(String id, String nombre, String producto, String telefono, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.producto = producto;
        this.telefono = telefono;
        this.correo = correo;
        this.estado = "Activo";
    }

    public Proveedor(String id, String nombre, String producto, String telefono, String correo, String estado) {
        this.id = id;
        this.nombre = nombre;
        this.producto = producto;
        this.telefono = telefono;
        this.correo = correo;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return nombre + " (" + producto + ")";
    }
}
