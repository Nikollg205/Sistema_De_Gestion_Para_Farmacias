package inventario;

import medicamentos.Medicamento;

import java.time.LocalDate;

/**
 * Represents a batch of a specific medicine in inventory.
 * Each lote has its own expiration date and available quantity.
 */
public class LoteInventario {

    private final String loteNumber;
    private final LocalDate dueDate;
    private int availableQuantity;
    private final Medicamento medicamento;
    private String idProveedor;

    /**
     * Creates a new inventory batch
     * @param loteNumber unique batch identifier
     * @param dueDate expiration date
     * @param availableQuantity quantity available
     * @param medicamento associated medicine
     * @param idProveedor supplier ID
     * @throws IllegalArgumentException if any argument is invalid
     */
    public LoteInventario(
            String loteNumber,
            LocalDate dueDate,
            int availableQuantity,
            Medicamento medicamento,
            String idProveedor
    ) {
        if (loteNumber == null || loteNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de lote no puede estar vacío");
        }
        if (dueDate == null) {
            throw new IllegalArgumentException("La fecha no puede ser nula");
        }
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        if (medicamento == null) {
            throw new IllegalArgumentException("El medicamento no puede ser nulo");
        }
        if (idProveedor == null || idProveedor.trim().isEmpty()) {
            throw new IllegalArgumentException("El proveedor no puede estar vacío");
        }

        this.loteNumber = loteNumber.trim();
        this.dueDate = dueDate;
        this.availableQuantity = availableQuantity;
        this.medicamento = medicamento;
        this.idProveedor = idProveedor.trim();
    }

    /**
     * Creates a new inventory batch (convenience constructor)
     */
    public LoteInventario(
            String loteNumber,
            LocalDate dueDate,
            int availableQuantity,
            Medicamento medicamento
    ) {
        this(loteNumber, dueDate, availableQuantity, medicamento, "PROV001");
    }

    public String getLoteNumber() {
        return loteNumber;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public String getIdProveedor() {
        return idProveedor;
    }

    /**
     * Sets available quantity
     * @param availableQuantity quantity available
     */
    public void setAvailableQuantity(int availableQuantity) {
        if (availableQuantity < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.availableQuantity = availableQuantity;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    /**
     * Checks if the batch is expired
     * @return true if expired
     */
    public boolean isExpired() {
        return dueDate.isBefore(LocalDate.now());
    }
}