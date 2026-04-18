package medicamentos;

import interfaces.TipoComercial;

import java.time.LocalDate;

/**
 * Represents a branded liquid medicine
 */
public class liquidoMarca extends medicamentoLiquido implements TipoComercial {

    private String brand;
    private String patent;

    /**
     * Creates a new branded liquid medicine
     */
    public liquidoMarca(
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
            String brand,
            String patent
    ) {
        super(stock, name, description, code, price, category, measurementUnit, dueDate, volumeMl, liquidType);
        setBrand(brand);
        setPatent(patent);
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
    public String getLaboratoryOrBrand() {
        return brand;
    }

    @Override
    public String getCommercialInfo() {
        return "Brand - " + brand + " (Patent: " + patent + ")";
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