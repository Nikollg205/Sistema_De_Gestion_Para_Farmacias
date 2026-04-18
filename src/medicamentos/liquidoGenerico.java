package medicamentos;

import interfaces.TipoComercial;

import java.time.LocalDate;

/**
 * Represents a generic liquid medicine
 */
public class liquidoGenerico extends medicamentoLiquido implements TipoComercial {

    private String activeIngredient;
    private String laboratory;

    /**
     * Creates a new generic liquid medicine
     */
    public liquidoGenerico(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            LocalDate dueDate,
            double volumeMl,
            String liquidType,
            String activeIngredient,
            String laboratory
    ) {
        super(stock, name, description, code, price, category, measurementUnit, dueDate, volumeMl, liquidType);
        setActiveIngredient(activeIngredient);
        setLaboratory(laboratory);
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
    public String getLaboratoryOrBrand() {
        return laboratory;
    }

    @Override
    public String getCommercialInfo() {
        return "Generic - " + activeIngredient + " (" + laboratory + ")";
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