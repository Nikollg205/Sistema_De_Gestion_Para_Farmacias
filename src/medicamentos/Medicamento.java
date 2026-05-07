package medicamentos;

/**
 * Represents a generic medicine with common attributes.
 */
public class Medicamento {

    private String name;
    private String description;
    private String code;
    private double price;
    private int stock;
    private String category;
    private String measurementUnit;
    private int contenidoUnidad;
    private String tipoForma;
    private String tipoComercial;

    /**
     * Empty constructor for DAO usage
     */
    public Medicamento() {
        // Required for building object from database
    }

    /**
     * Creates a new medicine
     * @param stock stock of the medicine
     * @param name name of the medicine
     * @param description description of the medicine
     * @param code code of the medicine
     * @param price price of the medicine
     * @param category category of the medicine
     * @param measurementUnit measurement unit
     * @throws IllegalArgumentException if argument is invalid
     */
    public Medicamento(
            int stock,
            String name,
            String description,
            String code,
            double price,
            String category,
            String measurementUnit
    ) {
        setStock(stock);
        setName(name);
        setDescription(description);
        setCode(code);
        setPrice(price);
        setCategory(category);
        setMeasurementUnit(measurementUnit);
    }

    public String getName() {
        return name;
    }

    /**
     * Sets the name of the medicine
     * @param name name of the medicine
     * @throws IllegalArgumentException when name is null or empty
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        this.name = name.trim().toUpperCase(); // normalize
    }

    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the medicine
     * @param description description
     */
    public void setDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }

        String desc = description.trim();

        if (desc.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        if (desc.length() < 5) {
            throw new IllegalArgumentException("Description too short");
        }

        if (desc.length() > 255) {
            throw new IllegalArgumentException("Description too long");
        }

        this.description = desc;
    }

    public String getCode() {
        return code;
    }

    /**
     * Sets the code
     * @param code medicine code
     */
    public void setCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("Code cannot be empty");
        }
        this.code = code.trim();
    }

    public double getPrice() {
        return price;
    }

    /**
     * Sets the price
     * @param price medicine price
     */
    public void setPrice(double price) {
        if (price <= 0 || Double.isNaN(price)) {
            throw new IllegalArgumentException("Invalid price");
        }
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    /**
     * Sets stock
     * @param stock quantity
     */
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    /**
     * Sets category
     * @param category category name
     */
    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty");
        }
        this.category = category.trim().toUpperCase();
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    /**
     * Sets measurement unit
     * @param measurementUnit unit
     */
    public void setMeasurementUnit(String measurementUnit) {
        if (measurementUnit == null || measurementUnit.trim().isEmpty()) {
            throw new IllegalArgumentException("Measurement unit cannot be empty");
        }
        this.measurementUnit = measurementUnit.trim().toUpperCase();
    }

    public int getContenidoUnidad() {
        return contenidoUnidad;
    }

    /**
     * Sets content per unit
     * @param contenidoUnidad units per container
     */
    public void setContenidoUnidad(int contenidoUnidad) {
        if (contenidoUnidad < 0) {
            throw new IllegalArgumentException("Contenido unidad cannot be negative");
        }
        this.contenidoUnidad = contenidoUnidad;
    }

    public String getTipoForma() {
        return tipoForma;
    }

    public void setTipoForma(String tipoForma) {
        if (tipoForma == null || tipoForma.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo forma cannot be empty");
        }
        this.tipoForma = tipoForma.trim().toLowerCase();
    }

    public String getTipoComercial() {
        return tipoComercial;
    }

    public void setTipoComercial(String tipoComercial) {
        if (tipoComercial == null || tipoComercial.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo commercial cannot be empty");
        }
        this.tipoComercial = tipoComercial.trim().toLowerCase();
    }

    public void showInfo() {
        System.out.println("Medicine: " + name);
        System.out.println("Code: " + code);
        System.out.println("Price: " + price);
        System.out.println("Stock: " + stock);
    }

}