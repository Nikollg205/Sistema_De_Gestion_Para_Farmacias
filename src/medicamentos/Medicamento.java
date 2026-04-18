package medicamentos;

/**
 * Represents a generic medicine with common attributes.
 */
public class Medicamento {

    private String name;
    private String description;
    private int code;
    private double price;
    private int stock;
    private String category;
    private String measurementUnit;

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
            int code,
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

    public int getCode() {
        return code;
    }

    /**
     * Sets the code
     * @param code medicine code
     */
    public void setCode(int code) {
        if (code <= 0) {
            throw new IllegalArgumentException("Code must be > 0");
        }
        this.code = code;
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

}