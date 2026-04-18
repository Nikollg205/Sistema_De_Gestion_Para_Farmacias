package medicamentos;

import interfaces.Marca;

/**
 * Represents a branded liquid medicine
 */
public class LiquidoMarca extends MedicamentoLiquido implements Marca {

    private String brand;
    private String patent;

    /**
     * Creates a new branded liquid medicine
     */
    public LiquidoMarca(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            double volumeMl,
            String liquidType,
            String brand,
            String patent
    ) {
        super(stock, name, description, code, price, category, measurementUnit, volumeMl, liquidType);
        setBrand(brand);
        setPatent(patent);
    }

    @Override
    public String getBrand() {
        return brand;
    }

    /**
     * Sets the brand
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
     */
    public void setPatent(String patent) {
        if (patent == null || patent.trim().isEmpty()) {
            throw new IllegalArgumentException("La patente no puede estar vacía");
        }
        this.patent = patent.trim();
    }

    @Override
    public void showInfo() {
        System.out.println("Branded Liquid Medicine");
        System.out.println("Name: " + getName());
        System.out.println("Presentation: " + getPresentation());
        System.out.println("Price: " + getPrice());
        System.out.println("Stock: " + getStock());
        System.out.println("Info: " + getCommercialInfo());
    }
}