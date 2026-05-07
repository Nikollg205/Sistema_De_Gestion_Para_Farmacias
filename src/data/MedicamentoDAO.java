package data;

import database.Conexion;
import data.interfaces.CrudSimpleInterface;
import entidades.LiquidoGenerico;
import entidades.LiquidoMarca;
import entidades.PastillaGenerica;
import entidades.PastillaMarca;
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
                registros.add(crearMedicamento(rs));
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
            String sql = "INSERT INTO medicamento (id_medicamento, tipo_medicamento, precio, unidad_medida, formula, stock_medicamento, contenido_unidad, nombre_medicamento, tipo_forma, tipo_comercial, ingrediente_activo, laboratorio, marca, patente, volumen_ml, tipo_liquido, cantidad_unidades, tipo_pastilla) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            ps = CON.conectar().prepareStatement(sql);

            ps.setString(1, obj.getCode());
            ps.setString(2, obj.getCategory());
            ps.setDouble(3, obj.getPrice());
            ps.setString(4, obj.getMeasurementUnit());
            ps.setString(5, obj.getDescription());
            ps.setInt(6, obj.getStock());
            ps.setInt(7, obj.getContenidoUnidad());
            ps.setString(8, obj.getName());
            ps.setString(9, obj.getTipoForma());
            ps.setString(10, obj.getTipoComercial());

            if (obj instanceof LiquidoGenerico) {
                LiquidoGenerico lg = (LiquidoGenerico) obj;
                ps.setString(11, lg.getActiveIngredient());
                ps.setString(12, lg.getLaboratory());
                ps.setNull(13, Types.VARCHAR);
                ps.setNull(14, Types.VARCHAR);
                ps.setDouble(15, lg.getVolumeMl());
                ps.setString(16, lg.getLiquidType());
                ps.setNull(17, Types.INTEGER);
                ps.setNull(18, Types.VARCHAR);
            } else if (obj instanceof LiquidoMarca) {
                LiquidoMarca lm = (LiquidoMarca) obj;
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.VARCHAR);
                ps.setString(13, lm.getBrand());
                ps.setString(14, lm.getPatent());
                ps.setDouble(15, lm.getVolumeMl());
                ps.setString(16, lm.getLiquidType());
                ps.setNull(17, Types.INTEGER);
                ps.setNull(18, Types.VARCHAR);
            } else if (obj instanceof PastillaGenerica) {
                PastillaGenerica pg = (PastillaGenerica) obj;
                ps.setString(11, pg.getActiveIngredient());
                ps.setString(12, pg.getLaboratory());
                ps.setNull(13, Types.VARCHAR);
                ps.setNull(14, Types.VARCHAR);
                ps.setNull(15, Types.DECIMAL);
                ps.setNull(16, Types.VARCHAR);
                ps.setInt(17, pg.getUnitCount());
                ps.setString(18, pg.getPillType());
            } else if (obj instanceof PastillaMarca) {
                PastillaMarca pm = (PastillaMarca) obj;
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.VARCHAR);
                ps.setString(13, pm.getBrand());
                ps.setString(14, pm.getPatent());
                ps.setNull(15, Types.DECIMAL);
                ps.setNull(16, Types.VARCHAR);
                ps.setInt(17, pm.getUnitCount());
                ps.setString(18, pm.getPillType());
            } else {
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.VARCHAR);
                ps.setNull(13, Types.VARCHAR);
                ps.setNull(14, Types.VARCHAR);
                ps.setNull(15, Types.DECIMAL);
                ps.setNull(16, Types.VARCHAR);
                ps.setNull(17, Types.INTEGER);
                ps.setNull(18, Types.VARCHAR);
            }

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
            String sql = "UPDATE medicamento SET tipo_medicamento=?, precio=?, unidad_medida=?, formula=?, stock_medicamento=?, contenido_unidad=?, nombre_medicamento=?, tipo_forma=?, tipo_comercial=?, ingrediente_activo=?, laboratorio=?, marca=?, patente=?, volumen_ml=?, tipo_liquido=?, cantidad_unidades=?, tipo_pastilla=? WHERE id_medicamento=?";

            ps = CON.conectar().prepareStatement(sql);

            ps.setString(1, obj.getCategory());
            ps.setDouble(2, obj.getPrice());
            ps.setString(3, obj.getMeasurementUnit());
            ps.setString(4, obj.getDescription());
            ps.setInt(5, obj.getStock());
            ps.setInt(6, obj.getContenidoUnidad());
            ps.setString(7, obj.getName());
            ps.setString(8, obj.getTipoForma());
            ps.setString(9, obj.getTipoComercial());

            if (obj instanceof LiquidoGenerico) {
                LiquidoGenerico lg = (LiquidoGenerico) obj;
                ps.setString(10, lg.getActiveIngredient());
                ps.setString(11, lg.getLaboratory());
                ps.setNull(12, Types.VARCHAR);
                ps.setNull(13, Types.VARCHAR);
                ps.setDouble(14, lg.getVolumeMl());
                ps.setString(15, lg.getLiquidType());
                ps.setNull(16, Types.INTEGER);
                ps.setNull(17, Types.VARCHAR);
            } else if (obj instanceof LiquidoMarca) {
                LiquidoMarca lm = (LiquidoMarca) obj;
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR);
                ps.setString(12, lm.getBrand());
                ps.setString(13, lm.getPatent());
                ps.setDouble(14, lm.getVolumeMl());
                ps.setString(15, lm.getLiquidType());
                ps.setNull(16, Types.INTEGER);
                ps.setNull(17, Types.VARCHAR);
            } else if (obj instanceof PastillaGenerica) {
                PastillaGenerica pg = (PastillaGenerica) obj;
                ps.setString(10, pg.getActiveIngredient());
                ps.setString(11, pg.getLaboratory());
                ps.setNull(12, Types.VARCHAR);
                ps.setNull(13, Types.VARCHAR);
                ps.setNull(14, Types.DECIMAL);
                ps.setNull(15, Types.VARCHAR);
                ps.setInt(16, pg.getUnitCount());
                ps.setString(17, pg.getPillType());
            } else if (obj instanceof PastillaMarca) {
                PastillaMarca pm = (PastillaMarca) obj;
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR);
                ps.setString(12, pm.getBrand());
                ps.setString(13, pm.getPatent());
                ps.setNull(14, Types.DECIMAL);
                ps.setNull(15, Types.VARCHAR);
                ps.setInt(16, pm.getUnitCount());
                ps.setString(17, pm.getPillType());
            } else {
                ps.setNull(10, Types.VARCHAR);
                ps.setNull(11, Types.VARCHAR);
                ps.setNull(12, Types.VARCHAR);
                ps.setNull(13, Types.VARCHAR);
                ps.setNull(14, Types.DECIMAL);
                ps.setNull(15, Types.VARCHAR);
                ps.setNull(16, Types.INTEGER);
                ps.setNull(17, Types.VARCHAR);
            }

            ps.setString(18, obj.getCode());

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

    /**
     * Get medicine by ID
     */
    public Medicamento buscarPorId(String id) {
        Medicamento med = null;

        try {
            String sql = "SELECT * FROM medicamento WHERE id_medicamento = ?";
            ps = CON.conectar().prepareStatement(sql);
            ps.setString(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                med = crearMedicamento(rs);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return med;
    }

    /**
     * Factory method to create the correct Medicamento subclass from ResultSet
     */
    private Medicamento crearMedicamento(ResultSet rs) throws SQLException {
        String nombre = rs.getString("nombre_medicamento");
        String formula = rs.getString("formula");
        String code = rs.getString("id_medicamento");
        double price = rs.getDouble("precio");
        String unit = rs.getString("unidad_medida");
        int stock = rs.getInt("stock_medicamento");
        String category = rs.getString("tipo_medicamento");
        int contenido = rs.getInt("contenido_unidad");
        String tipoForma = rs.getString("tipo_forma");
        String tipoComercial = rs.getString("tipo_comercial");

        if (tipoForma == null) tipoForma = "pastilla";
        if (tipoComercial == null) tipoComercial = "generico";

        String desc = (formula != null && !formula.trim().isEmpty() && formula.trim().length() >= 5)
                ? formula : "N/A";

        if (tipoForma.equals("pastilla")) {
            int unitCount = 0;
            String pillType = "";
            try { unitCount = rs.getInt("cantidad_unidades"); } catch (Exception e) { unitCount = contenido; }
            try { pillType = rs.getString("tipo_pastilla"); } catch (Exception e) { pillType = "tableta"; }
            if (pillType == null) pillType = "tableta";

            if (tipoComercial.equals("marca")) {
                String brand = rs.getString("marca");
                String patent = rs.getString("patente");
                if (brand == null) brand = "Sin marca";
                if (patent == null) patent = "N/A";
                return new PastillaMarca(stock, nombre, desc, code, price, category, unit, unitCount, pillType, brand, patent);
            } else {
                String ingredient = rs.getString("ingrediente_activo");
                String lab = rs.getString("laboratorio");
                if (ingredient == null) ingredient = nombre;
                if (lab == null) lab = "N/A";
                return new PastillaGenerica(stock, nombre, desc, code, price, category, unit, unitCount, pillType, ingredient, lab);
            }
        } else if (tipoForma.equals("liquido")) {
            double volumeMl = 0;
            String liquidType = "";
            try { volumeMl = rs.getDouble("volumen_ml"); } catch (Exception e) { volumeMl = 0; }
            try { liquidType = rs.getString("tipo_liquido"); } catch (Exception e) { liquidType = "jarabe"; }
            if (liquidType == null) liquidType = "jarabe";
            if (volumeMl <= 0) volumeMl = 100;

            if (tipoComercial.equals("marca")) {
                String brand = rs.getString("marca");
                String patent = rs.getString("patente");
                if (brand == null) brand = "Sin marca";
                if (patent == null) patent = "N/A";
                return new LiquidoMarca(stock, nombre, desc, code, price, category, unit, volumeMl, liquidType, brand, patent);
            } else {
                String ingredient = rs.getString("ingrediente_activo");
                String lab = rs.getString("laboratorio");
                if (ingredient == null) ingredient = nombre;
                if (lab == null) lab = "N/A";
                return new LiquidoGenerico(stock, nombre, desc, code, price, category, unit, volumeMl, liquidType, ingredient, lab);
            }
        }

        return new Medicamento(stock, nombre, desc, code, price, category, unit);
    }

    /**
     * Reduce stock for a medicine
     */
    public boolean reducirStock(String idMedicamento, int cantidad) {
        boolean resp = false;

        try {
            String sql = "UPDATE medicamento SET stock_medicamento = stock_medicamento - ? WHERE id_medicamento = ? AND stock_medicamento >= ?";

            ps = CON.conectar().prepareStatement(sql);
            ps.setInt(1, cantidad);
            ps.setString(2, idMedicamento);
            ps.setInt(3, cantidad);

            resp = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            cerrar();
        }

        return resp;
    }

    /**
     * Get low stock medicines count
     */
    public int getStockBajo() {
        int total = 0;

        try {
            String sql = "SELECT COUNT(*) FROM medicamento WHERE stock_medicamento <= 10";
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
