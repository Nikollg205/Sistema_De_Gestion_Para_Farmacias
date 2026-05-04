package inventario;

import database.Conexion;
import data.interfaces.CrudSimpleInterface;
import inventario.loteInventario;
import medicamentos.Medicamento;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing inventory batches in database
 */
public class inventario implements CrudSimpleInterface<loteInventario> {

    private inventario DAO;
    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public inventario() {
        CON = Conexion.getInstancia();
        DAO = new inventario();
    }

    /**
     * Lists batches filtered by medicine name
     */
    @Override
    public List<loteInventario> listar(String texto) {
        List<loteInventario> registros = new ArrayList<>();

        try {
            String sql = "SELECT * FROM lote WHERE nombre_medicamento LIKE ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {
                Medicamento med = new Medicamento();
                // fix
                med.setStock(0);
                med.setName(rs.getString("nombre_medicamento"));
                med.setDescription("N/A");
                med.setCode(rs.getInt("código"));
                med.setPrice(rs.getDouble("precio"));
                med.setCategory("N/A");
                med.setMeasurementUnit("N/A");
                med.setDueDate(rs.getDate("fecha_vencimiento").toLocalDate());

                loteInventario lote = new loteInventario(
                        rs.getString("lote"),
                        rs.getDate("fecha_vencimiento").toLocalDate(),
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
    public boolean insertar(loteInventario obj) {
        resp = false;

        try {
            String sql = "INSERT INTO lote (lote, fecha_vencimiento, cantidad, codigo) VALUES (?,?,?,?)";

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
    public boolean actualizar(loteInventario obj) {
        resp = false;

        try {
            String sql = "UPDATE lote SET cantidad=? WHERE lote=?";

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
     * Deactivates a batch (logical delete)
     */
    @Override
    public boolean desactivar(int id) {
        resp = false;

        try {
            String sql = "UPDATE lote SET activo=0 WHERE id=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setInt(1, id);

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    @Override
    public boolean activar(int id) {
        resp = false;

        try {
            String sql = "UPDATE lote SET activo=1 WHERE id=?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setInt(1, id);

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
            String sql = "SELECT lote FROM lote WHERE lote=?";
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
    
    public Medicamento buscar(String nombre){
        List<loteInventario> lista = DAO.listar(nombre);
        
        if (!lista.isEmpty()) {
            return lista.get(0).getMedicamento(); // devuelve el primero
        }
        
        return null;
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