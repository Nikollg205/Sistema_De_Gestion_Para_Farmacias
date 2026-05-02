package interfaces;

/**
 * Creation of brand CONTRACT
 * --
 * Defines two main methods that
 * Classes that implements this INTERFACE
 * MUST define each one.
 * --
 * Is design for be used for identify the type
 * of the medicine.
 */
public interface TipoComercial {

    /**
     * Returns the laboratory (generic) or brand (branded)
     * @return laboratory or brand name
     */
    String getLaboratoryOrBrand();

    /**
     * Returns formatted commercial information
     * @return commercial info string
     */
    String getCommercialInfo();
}
