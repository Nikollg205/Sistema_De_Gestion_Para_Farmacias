package medicamentos;

import interfaces.Marca;

/**
 * Represents a branded pill medicine
 */
public class PastillaMarca extends MedicamentoPastilla implements Marca {

    private String brand;
    private String patent;

    public PastillaMarca(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            int unitCount,
            String pillType,
            String brand,
            String patent
    ) {
        super(stock, name, description, code, price, category, measurementUnit, unitCount, pillType);
        setBrand(brand);
        setPatent(patent);
    }

    @Override
    public String getBrand() {
        return brand;
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

    @Override
    public String getPatent() {
        return patent;
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