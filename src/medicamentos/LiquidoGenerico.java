package medicamentos;

import interfaces.Generico;

/**
 * Represents a generic liquid medicine
 */
public class LiquidoGenerico extends MedicamentoLiquido implements Generico {

    private String activeIngredient;
    private String laboratory;

    /**
     * Creates a new generic liquid medicine
     */
    public LiquidoGenerico(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            double volumeMl,
            String liquidType,
            String activeIngredient,
            String laboratory
    ) {
        super(stock, name, description, code, price, category, measurementUnit, volumeMl, liquidType);
        setActiveIngredient(activeIngredient);
        setLaboratory(laboratory);
    }

    @Override
    public String getActiveIngredient() {
        return activeIngredient;
    }

    /**
     * Sets the active ingredient
     */
    public void setActiveIngredient(String activeIngredient) {
        if (activeIngredient == null || activeIngredient.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo Ingrediente activo no puede quedar vacío");
        }
        this.activeIngredient = activeIngredient.trim();
    }

    @Override
    public String getLaboratory() {
        return laboratory;
    }

    /**
     * Sets the laboratory
     */
    public void setLaboratory(String laboratory) {
        if (laboratory == null || laboratory.trim().isEmpty()) {
            throw new IllegalArgumentException("El laboratorio no puede estar vacío");
        }
        this.laboratory = laboratory.trim();
    }

    @Override
    public void showInfo() {
        System.out.println("Generic Liquid Medicine");
        System.out.println("Name: " + getName());
        System.out.println("Presentation: " + getPresentation());
        System.out.println("Price: " + getPrice());
        System.out.println("Stock: " + getStock());
        System.out.println("Info: " + getCommercialInfo());
    }
}