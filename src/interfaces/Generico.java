package interfaces;

/**
 * Interface for generic medicines.
 * Defines contract for medicines without brand protection.
 */
public interface Generico extends TipoComercial {

    /**
     * Returns active ingredient
     */
    String getActiveIngredient();

    /**
     * Returns laboratory name
     */
    String getLaboratory();

    /**
     * Returns formatted commercial information for generic medicine
     */
    @Override
    default String getCommercialInfo() {
        return "Generic - " + getActiveIngredient() + " (" + getLaboratory() + ")";
    }

    /**
     * Returns laboratory name (always the laboratory for generic medicines)
     */
    @Override
    default String getLaboratoryOrBrand() {
        return getLaboratory();
    }
}
