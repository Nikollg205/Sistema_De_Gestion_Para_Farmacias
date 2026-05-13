package roles;

/**
 * Session context del usuario autenticado.
 * Guarda identidad y rol activo para control de acceso en formularios.
 */
public class SesionUsuario {

    // Instancia única de sesión para toda la app de escritorio.
    private static SesionUsuario instancia;

    private String idUsuario;
    private String nombreUsuario;
    private String nombreRol;

    private SesionUsuario() {
    }

    // Retorna la sesión activa compartida.
    public static synchronized SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    // Registra datos del usuario autenticado tras login exitoso.
    public void iniciarSesion(String idUsuario, String nombreUsuario, String nombreRol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.nombreRol = nombreRol;
    }

    // Limpia por completo la sesión al cerrar sesión.
    public void cerrarSesion() {
        this.idUsuario = null;
        this.nombreUsuario = null;
        this.nombreRol = null;
    }

    // Indica si existe un usuario actualmente autenticado.
    public boolean haySesionActiva() {
        return idUsuario != null && !idUsuario.isEmpty();
    }

    // Atajo para validaciones de permisos del rol administrador.
    public boolean esAdmin() {
        return nombreRol != null && nombreRol.equalsIgnoreCase("Administrador");
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreRol() {
        return nombreRol;
    }
}
