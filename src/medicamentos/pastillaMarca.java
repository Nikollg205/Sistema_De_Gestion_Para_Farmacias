package medicamentos;

import interfaces.TipoComercial;

import java.time.LocalDate;

/**
 * Represents a branded pill medicine
 */
public class pastillaMarca extends medicamentoPastilla implements TipoComercial {

    private String brand;
    private String patent;

    public pastillaMarca(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            LocalDate dueDate,
            int unitCount,
            String pillType,
            String brand,
            String patent
    ) {
        super(stock, name, description, code, price, category, measurementUnit, dueDate, unitCount, pillType);
        setBrand(brand);
        setPatent(patent);
    }

    /**
     * Sets the brand
     * @param brand brand name
     */
    public void setBrand(String brand) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo Marca no puede estar vacío");
        }
        this.brand = brand.trim();
    }

    /**
     * Sets the patent
     * @param patent patent info
     */
    public void setPatent(String patent) {
        if (patent == null || patent.trim().isEmpty()) {
            throw new IllegalArgumentException("Una patente no puede estar vacía");
        }
        this.patent = patent.trim();
    }

    /**
     * Returns the brand name
     */
    @Override
    public String getLaboratoryOrBrand() {
        return brand;
    }

    /**
     * Returns formatted commercial info
     */
    @Override
    public String getCommercialInfo() {
        return "Brand - " + brand + " (Patent: " + patent + ")";
    }

    @Override
    public void showInfo() {
        System.out.println("Branded Medicine");
        System.out.println("Name: " + getName());
        System.out.println("Presentation: " + getPresentation());
        System.out.println("Price: " + getPrice());
        System.out.println("Stock: " + getStock());
        System.out.println("Laboratory: " + getLaboratoryOrBrand());
        System.out.println("Info: " + getCommercialInfo());
    }
}