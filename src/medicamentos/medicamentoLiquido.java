package medicamentos;

import java.time.LocalDate;

/**
 * Abstract class representing a liquid-type medicine.
 * Extends Medicamento and adds liquid-specific attributes.
 */
public abstract class medicamentoLiquido extends Medicamento {

    private double volumeMl;
    private String liquidType; // syrup, suspension, solution

    /**
     * Creates a new liquid medicine
     * @param stock stock of the medicine
     * @param name name of the medicine
     * @param description description of the medicine
     * @param code code of the medicine
     * @param price price of the medicine
     * @param category category of the medicine
     * @param measurementUnit measurement unit of the medicine
     * @param dueDate expiration date
     * @param volumeMl volume in milliliters
     * @param liquidType type of liquid
     * @throws IllegalArgumentException if argument is invalid
     */
    public medicamentoLiquido(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            LocalDate dueDate,
            double volumeMl,
            String liquidType
    ) {
        super(stock, name, description, code, price, category, measurementUnit, dueDate);
        setVolumeMl(volumeMl);
        setLiquidType(liquidType);
    }

    public double getVolumeMl() {
        return volumeMl;
    }

    /**
     * Sets the liquid volume
     * @param volumeMl volume in ml
     * @throws IllegalArgumentException when volume <= 0
     */
    public void setVolumeMl(double volumeMl) {
        if (volumeMl <= 0) {
            throw new IllegalArgumentException("El volumen debe ser superior a 0");
        }
        this.volumeMl = volumeMl;
    }

    public String getLiquidType() {
        return liquidType;
    }

    /**
     * Sets the type of liquid
     * @param liquidType type (syrup, suspension, etc.)
     * @throws IllegalArgumentException when invalid
     */
    public void setLiquidType(String liquidType) {
        if (liquidType == null || liquidType.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo Tipo de líquido no puede estar vacío");
        }
        this.liquidType = liquidType.trim().toLowerCase(); // normalize
    }

    /**
     * Returns formatted presentation
     * Example: "120 ml syrup"
     * @return presentation string
     */
    public String getPresentation() {
        return volumeMl + " ml " + liquidType;
    }

    /**
     * Displays medicine info
     */
    public abstract void showInfo();
}