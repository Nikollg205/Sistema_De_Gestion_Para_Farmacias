package data;

import database.Conexion;
import data.interfaces.CrudSimpleInterface;
import inventario.LoteInventario;
import medicamentos.Medicamento;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing inventory batches in database
 */
public class LoteInventarioDAO implements CrudSimpleInterface<LoteInventario> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public LoteInventarioDAO() {
        CON = Conexion.getInstancia();
    }

    /**
     * Lists batches filtered by medicine name
     */
    @Override
    public List<LoteInventario> listar(String texto) {
        List<LoteInventario> registros = new ArrayList<>();

        try {
            String sql = "SELECT l.id_lote, l.fecha_caducidad, l.cantidad, " +
                    "m.id_medicamento, m.nombre_medicamento, m.precio, m.formula, m.unidad_medida, m.tipo_medicamento " +
                    "FROM lote l " +
                    "INNER JOIN medicamento m ON l.id_medicamento = m.id_medicamento " +
                    "WHERE m.nombre_medicamento LIKE ?";

            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {

                Medicamento med = new Medicamento();

                med.setName(rs.getString("nombre_medicamento"));

                String formula = rs.getString("formula");
                if (formula == null || formula.trim().isEmpty() || formula.trim().length() < 5) {
                    formula = "N/A";
                }
                med.setDescription(formula);

                med.setCode(rs.getInt("id_medicamento"));
                med.setPrice(rs.getDouble("precio"));
                med.setCategory(rs.getString("tipo_medicamento"));
                med.setMeasurementUnit(rs.getString("unidad_medida"));
                med.setStock(0);

                LoteInventario lote = new LoteInventario(
                        rs.getString("id_lote"),
                        rs.getDate("fecha_caducidad").toLocalDate(),
                        rs.getInt("cantidad"),
                        med
                );

                registros.add(lote);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return registros;
    }

    /**
     * Inserts a new batch
     */
    @Override
    public boolean insertar(LoteInventario obj) {
        resp = false;

        try {
            String sql = "INSERT INTO lote (id_lote, fecha_caducidad, cantidad, id_medicamento) VALUES (?,?,?,?)";

            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, obj.getLoteNumber());
            ps.setDate(2, Date.valueOf(obj.getDueDate()));
            ps.setInt(3, obj.getAvailableQuantity());
            ps.setInt(4, obj.getMedicamento().getCode());

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    /**
     * Updates a batch quantity
     */
    @Override
    public boolean actualizar(LoteInventario obj) {
        resp = false;

        try {
            String sql = "UPDATE lote SET cantidad=? WHERE id_lote=?";

            ps = CON.conectar().prepareStatement(sql);
            ps.setInt(1, obj.getAvailableQuantity());
            ps.setString(2, obj.getLoteNumber());

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    /**
     * Counts total batches
     */
    @Override
    public int total() {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) FROM lote";
            ps = CON.conectar().prepareStatement(sql);
            rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt(1);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return total;
    }

    /**
     * Checks if a batch exists
     */
    @Override
    public boolean existe(String texto) {
        resp = false;

        try {
            String sql = "SELECT id_lote FROM lote WHERE id_lote=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, texto);

            rs = ps.executeQuery();

            resp = rs.next();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    /**
     * Closes resources
     */
    private void cerrar() {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
            CON.desconectar();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }
}