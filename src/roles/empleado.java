package roles;

/**
 * Represents an employee of the pharmacy system
 */
public class empleado {

    private int id;
    private String name;
    private String lastName;
    private String document;
    private String role;
    private boolean active;

    /**
     * Empty constructor for DAO usage
     */
    public empleado() {
    }

    /**
     * Creates a new employee
     * @param id employee id
     * @param name employee name
     * @param lastName employee last name
     * @param document identification document
     * @param role employee role
     * @param active status
     * @throws IllegalArgumentException if argument is invalid
     */
    public empleado(int id, String name, String lastName, String document, String role, boolean active) {
        setId(id);
        setName(name);
        setLastName(lastName);
        setDocument(document);
        setRole(role);
        setActive(active);
    }

    public int getId() {
        return id;
    }

    /**
     * Sets employee id
     * @param id employee id
     * @throws IllegalArgumentException when id <= 0
     */
    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor que 0");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    /**
     * Sets employee name
     * @param name employee name
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        this.name = name.trim().toUpperCase();
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * Sets employee last name
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        this.lastName = lastName.trim().toUpperCase();
    }

    public String getDocument() {
        return document;
    }

    /**
     * Sets employee document (ID)
     * @param document identification number
     */
    public void setDocument(String document) {
        if (document == null || document.trim().isEmpty()) {
            throw new IllegalArgumentException("El documento no puede estar vacío");
        }

        String doc = document.trim();

        if (!doc.matches("\\d+")) { // \d+ es para revisar si un string tiene digito 0-9
            throw new IllegalArgumentException("El documento debe contener solo números");
        }

        this.document = doc;
    }

    public String getRole() {
        return role;
    }

    /**
     * Sets employee role
     * @param role role (ADMIN, SELLER, etc.)
     */
    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo Rol no puede estar vacío");
        }
        this.role = role.trim().toUpperCase();
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Sets employee status
     * @param active true if active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns full name
     * @return full name
     */
    public String getFullName() {
        return name + " " + lastName;
    }
}