package medicamentos;

/**
 * Abstract class representing a pill-type medicine.
 */
public abstract class MedicamentoPastilla extends Medicamento {

    private int unitCount;
    private String pillType;

    /**
     * Creates a new pill medicine
     */
    public MedicamentoPastilla(
            int stock,
            String name,
            String description,
            int code,
            double price,
            String category,
            String measurementUnit,
            int unitCount,
            String pillType
    ) {
        super(stock, name, description, code, price, category, measurementUnit);
        setUnitCount(unitCount);
        setPillType(pillType);
    }

    /**
     * Sets unit count
     */
    public void setUnitCount(int unitCount) {
        if (unitCount <= 0) {
            throw new IllegalArgumentException("La cantidad de unidades no puede ser menor que 0");
        }
        this.unitCount = unitCount;
    }

    /**
     * Sets pill type
     */
    public void setPillType(String pillType) {
        if (pillType == null || pillType.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de pastilla invalido");
        }
        this.pillType = pillType.trim().toLowerCase();
    }

    /**
     * Returns formatted presentation
     */
    public String getPresentation() {
        return unitCount + " " + pillType;
    }

    public abstract void showInfo();
}