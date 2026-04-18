package medicamentos;

import interfaces.Generico;

/**
 * Represents a generic pill medicine
 */
public class PastillaGenerica extends MedicamentoPastilla implements Generico {

    private String activeIngredient;
    private String laboratory;

    public PastillaGenerica(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            int unitCount,
            String pillType,
            String activeIngredient,
            String laboratory
    ) {
        super(stock, name, description, code, price, category, measurementUnit, unitCount, pillType);
        setActiveIngredient(activeIngredient);
        setLaboratory(laboratory);
    }

    @Override
    public String getActiveIngredient() {
        return activeIngredient;
    }

    /**
     * Sets the active ingredient
     * @param activeIngredient active ingredient name
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
     * @param laboratory laboratory name
     */
    public void setLaboratory(String laboratory) {
        if (laboratory == null || laboratory.trim().isEmpty()) {
            throw new IllegalArgumentException("El laboratorio no puede estar vacío");
        }
        this.laboratory = laboratory.trim();
    }

    @Override
    public void showInfo() {
        System.out.println("Generic Medicine");
        System.out.println("Name: " + getName());
        System.out.println("Presentation: " + getPresentation());
        System.out.println("Price: " + getPrice());
        System.out.println("Stock: " + getStock());
        System.out.println("Laboratory: " + getLaboratoryOrBrand());
        System.out.println("Info: " + getCommercialInfo());
    }
}