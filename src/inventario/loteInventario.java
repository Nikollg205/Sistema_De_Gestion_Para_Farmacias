package inventario;

import medicamentos.Medicamento;

import java.time.LocalDate;

/**
 * Represents a batch of a specific medicine in inventory.
 * Each lote has its own expiration date and available quantity.
 */
public class loteInventario {

    private final String loteNumber;
    private final LocalDate dueDate;
    private int availableQuantity;
    private final Medicamento medicamento;

    /**
     * Creates a new inventory batch
     * @param loteNumber unique batch identifier
     * @param dueDate expiration date
     * @param availableQuantity quantity available
     * @param medicamento associated medicine
     * @throws IllegalArgumentException if any argument is invalid
     */
    public loteInventario(
            String loteNumber,
            LocalDate dueDate,
            int availableQuantity,
            Medicamento medicamento
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

        this.loteNumber = loteNumber.trim();
        this.dueDate = dueDate;
        this.availableQuantity = availableQuantity;
        this.medicamento = medicamento;
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