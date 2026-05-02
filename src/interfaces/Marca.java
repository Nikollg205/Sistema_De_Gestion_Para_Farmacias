package interfaces;

/**
 * Interface for branded medicines.
 * Defines contract for medicines with brand protection and patents.
 */
public interface Marca extends TipoComercial {

    /**
     * Returns brand name
     */
    String getBrand();

    /**
     * Returns patent information
     */
    String getPatent();

    /**
     * Returns formatted commercial information for branded medicine
     */
    @Override
    default String getCommercialInfo() {
        return "Brand - " + getBrand() + " (Patent: " + getPatent() + ")";
    }

    /**
     * Returns brand name (always the brand for branded medicines)
     */
    @Override
    default String getLaboratoryOrBrand() {
        return getBrand();
    }
}
