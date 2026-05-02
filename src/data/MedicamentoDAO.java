package data;

import database.Conexion;
import data.interfaces.CrudSimpleInterface;
import medicamentos.Medicamento;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class for managing Medicamento based on real DB schema
 */
public class MedicamentoDAO implements CrudSimpleInterface<Medicamento> {

    private final Conexion CON;
    private PreparedStatement ps;
    private ResultSet rs;
    private boolean resp;

    public MedicamentoDAO() {
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Medicamento> listar(String texto) {
        List<Medicamento> registros = new ArrayList<>();

        try {
            String sql = "SELECT * FROM medicamento WHERE nombre_medicamento LIKE ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, "%" + texto + "%");

            rs = ps.executeQuery();

            while (rs.next()) {

                Medicamento med = new Medicamento();

                med.setName(rs.getString("nombre_medicamento"));
                med.setDescription(rs.getString("formula"));
                med.setCode(rs.getInt("id_medicamento"));
                med.setPrice(rs.getDouble("precio"));
                med.setMeasurementUnit(rs.getString("unidad_medida"));
                med.setStock(rs.getInt("stock_medicamento"));
                med.setCategory(rs.getString("tipo_medicamento"));

                registros.add(med);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return registros;
    }

    @Override
    public boolean insertar(Medicamento obj) {
        resp = false;

        try {
            String sql = "INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento) VALUES (?,?,?,?,?,?,?,?)";

            ps = CON.conectar().prepareStatement(sql);

            ps.setInt(1, obj.getCode());
            ps.setString(2, obj.getCategory());
            ps.setDouble(3, obj.getPrice());
            ps.setString(4, obj.getMeasurementUnit());
            ps.setString(5, obj.getDescription());
            ps.setInt(6, obj.getStock());
            ps.setInt(7, 1);
            ps.setString(8, obj.getName());

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    @Override
    public boolean actualizar(Medicamento obj) {
        resp = false;

        try {
            String sql = "UPDATE medicamento SET tipo_medicamento=?, precio=?, unidad_medida=?, formula=?, stock_medicamento=?, nombre_medicamento=? WHERE id_medicamento=?";

            ps = CON.conectar().prepareStatement(sql);

            ps.setString(1, obj.getCategory());
            ps.setDouble(2, obj.getPrice());
            ps.setString(3, obj.getMeasurementUnit());
            ps.setString(4, obj.getDescription());
            ps.setInt(5, obj.getStock());
            ps.setString(6, obj.getName());
            ps.setInt(7, obj.getCode());

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    @Override
    public int total() {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) FROM medicamento";
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

    @Override
    public boolean existe(String texto) {
        resp = false;

        try {
            String sql = "SELECT nombre_medicamento FROM medicamento WHERE nombre_medicamento=?";
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