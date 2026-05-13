package presentacion.ventanas;

import data.MedicamentoDAO;
import database.Conexion;
import medicamentos.Medicamento;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * panelAgregarMedicamentos: clase del proyecto HealthPharmacy.
 */
public class panelAgregarMedicamentos extends javax.swing.JPanel {
    
    private formAdmin parent;
    private DefaultTableModel tableModel;
    private MedicamentoDAO medicamentoDAO;
    private NumberFormat formatoCOP = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    
    // Semantic color tokens
    private Color slate900 = new Color(15, 23, 42);
    private Color slate800 = new Color(30, 41, 59);
    private Color slate700 = new Color(51, 65, 85);
    private Color slate600 = new Color(71, 85, 105);
    private Color slate500 = new Color(100, 116, 139);
    private Color slate400 = new Color(148, 163, 184);
    private Color slate300 = new Color(203, 213, 225);
    private Color slate200 = new Color(226, 232, 240);
    private Color slate100 = new Color(241, 245, 249);
    private Color slate50 = new Color(248, 250, 252);
    private Color white = new Color(255, 255, 255);
    private Color blue600 = new Color(37, 99, 235);
    private Color blue700 = new Color(29, 78, 216);
    private Color blue50 = new Color(239, 246, 255);
    private Color blue500 = new Color(59, 130, 246);
    private Color green500 = new Color(34, 197, 94);
    private Color green50 = new Color(240, 253, 244);
    private Color red500 = new Color(239, 68, 68);
    private Color red50 = new Color(254, 242, 242);
    private Color amber500 = new Color(245, 158, 11);
    private Color amber50 = new Color(255, 251, 235);

    public panelAgregarMedicamentos(formAdmin parent) {
        this.parent = parent;
        this.medicamentoDAO = new MedicamentoDAO();
        initComponents();
        initTable();
        cargarDatos();
    }
    
    // Metodo initTable: logica de interfaz asociada a este formulario/panel.
    private void initTable() {
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Medicamento", "Categoria", "Precio", "Stock", "Formula", "Estado"},
            0
        ) {
            @Override
            // Metodo isCellEditable: logica de interfaz asociada a este formulario/panel.
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblMedicamentos.setModel(tableModel);
        tblMedicamentos.setRowHeight(48);
        tblMedicamentos.setFont(new Font("Inter", Font.PLAIN, 13));
        tblMedicamentos.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tblMedicamentos.getTableHeader().setBackground(slate50);
        tblMedicamentos.getTableHeader().setForeground(slate500);
        tblMedicamentos.getTableHeader().setPreferredSize(new Dimension(0, 40));
        tblMedicamentos.getTableHeader().setBorder(new RoundedBorder(slate200, 0, 0));
        tblMedicamentos.setSelectionBackground(blue50);
        tblMedicamentos.setGridColor(slate100);
        tblMedicamentos.setShowVerticalLines(false);
        tblMedicamentos.setShowHorizontalLines(true);
        tblMedicamentos.setBackground(white);
        tblMedicamentos.setIntercellSpacing(new Dimension(0, 0));
        
        tblMedicamentos.getColumnModel().getColumn(0).setPreferredWidth(70);
        tblMedicamentos.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblMedicamentos.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblMedicamentos.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblMedicamentos.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblMedicamentos.getColumnModel().getColumn(5).setPreferredWidth(200);
        tblMedicamentos.getColumnModel().getColumn(6).setPreferredWidth(100);
        
        // Center aligned renderers
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                styleLabel(label, isSelected, row);
                return label;
            }
        };
        tblMedicamentos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblMedicamentos.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                double precio = Double.parseDouble(value.toString());
                label.setText(formatoCOP.format(precio));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                styleLabel(label, isSelected, row);
                return label;
            }
        });
        tblMedicamentos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblMedicamentos.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                styleLabel(label, isSelected, row);
                return label;
            }
        });
        
        // Text aligned renderers
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                styleLabel(label, isSelected, row);
                return label;
            }
        };
        tblMedicamentos.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        tblMedicamentos.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        
        StatusCellRenderer statusRenderer = new StatusCellRenderer();
        tblMedicamentos.getColumnModel().getColumn(6).setCellRenderer(statusRenderer);
    }
    
    // Metodo styleLabel: logica de interfaz asociada a este formulario/panel.
    private void styleLabel(JLabel label, boolean isSelected, int row) {
        if (isSelected) {
            label.setBackground(blue50);
            label.setForeground(slate900);
        } else {
            label.setBackground(row % 2 == 0 ? white : slate50);
            label.setForeground(slate900);
        }
        label.setOpaque(true);
        label.setBorder(new EmptyBorder(0, 12, 0, 12));
    }
    
    // Metodo cargarDatos: logica de interfaz asociada a este formulario/panel.
    private void cargarDatos() {
        actualizarTabla();
    }
    
    // Metodo actualizarTabla: logica de interfaz asociada a este formulario/panel.
    private void actualizarTabla() {
        tableModel.setRowCount(0);
        String filtro = txtBuscar.getText().toLowerCase().trim();
        if (filtro.contains("buscar")) filtro = "";
        
        List<Medicamento> lista = medicamentoDAO.listar("");
        
        for (Medicamento m : lista) {
            String nombre = m.getName().toLowerCase();
            String cat = m.getCategory().toLowerCase();
            
            if (filtro.isEmpty() || nombre.contains(filtro) || cat.contains(filtro)) {
                String estado = m.getStock() == 0 ? "Agotado" : m.getStock() <= 10 ? "Bajo" : "Disponible";
                tableModel.addRow(new Object[]{
                    m.getCode(), m.getName(), m.getCategory(),
                    m.getPrice(), m.getStock(), m.getDescription(), estado
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    // Metodo initComponents: logica de interfaz asociada a este formulario/panel.
    private void initComponents() {
        panelPrincipal = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        panelTituloWrap = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelSearchWrap = new javax.swing.JPanel();
        panelSearch = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        panelTablaWrapper = new javax.swing.JPanel();
        panelTabla = new javax.swing.JScrollPane();
        tblMedicamentos = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();
        btnAgregar = new ModernButton("+  Agregar Medicamento");

        setBackground(slate50);
        setLayout(new BorderLayout());

        panelPrincipal.setBackground(slate50);
        panelPrincipal.setBorder(new EmptyBorder(28, 28, 28, 28));
        panelPrincipal.setLayout(new BorderLayout(0, 20));

        // Header
        panelHeader.setBackground(slate50);
        panelHeader.setLayout(new BorderLayout(16, 0));

        panelTituloWrap.setBackground(slate50);
        panelTituloWrap.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        lblTitulo.setFont(new Font("Inter", Font.BOLD, 24));
        lblTitulo.setForeground(slate900);
        lblTitulo.setText("Medicamentos");
        panelTituloWrap.add(lblTitulo);

        lblSubtitulo.setFont(new Font("Inter", Font.PLAIN, 14));
        lblSubtitulo.setForeground(slate500);
        lblSubtitulo.setText("Gestiona el catalogo de medicamentos del sistema");
        panelTituloWrap.add(lblSubtitulo);

        panelHeader.add(panelTituloWrap, BorderLayout.CENTER);

        // Search
        panelSearchWrap.setBackground(slate50);
        panelSearchWrap.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        panelSearch.setBackground(white);
        panelSearch.setBorder(new RoundedBorder(slate200, 1, 10));
        panelSearch.setLayout(new BorderLayout());
        panelSearch.setPreferredSize(new Dimension(300, 40));

        JLabel lblSearchIcon = new JLabel();
        lblSearchIcon.setIcon(new LucideIcon("search", 18, slate400));
        lblSearchIcon.setBorder(new EmptyBorder(0, 12, 0, 8));
        panelSearch.add(lblSearchIcon, BorderLayout.WEST);

        txtBuscar.setFont(new Font("Inter", Font.PLAIN, 13));
        txtBuscar.setForeground(slate500);
        txtBuscar.setText("Buscar medicamento...");
        txtBuscar.setBorder(null);
        txtBuscar.setBackground(white);
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            // Metodo focusGained: logica de interfaz asociada a este formulario/panel.
            public void focusGained(FocusEvent evt) {
                if (txtBuscar.getText().equals("Buscar medicamento...")) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(slate900);
                }
            }
            @Override
            // Metodo focusLost: logica de interfaz asociada a este formulario/panel.
            public void focusLost(FocusEvent evt) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText("Buscar medicamento...");
                    txtBuscar.setForeground(slate500);
                }
            }
        });
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            // Metodo keyReleased: logica de interfaz asociada a este formulario/panel.
            public void keyReleased(KeyEvent evt) { actualizarTabla(); }
        });
        panelSearch.add(txtBuscar, BorderLayout.CENTER);

        panelSearchWrap.add(panelSearch);
        panelHeader.add(panelSearchWrap, BorderLayout.EAST);
        panelPrincipal.add(panelHeader, BorderLayout.PAGE_START);

        // Table
        panelTablaWrapper.setBackground(white);
        panelTablaWrapper.setBorder(new RoundedBorder(slate200, 1, 12));
        panelTablaWrapper.setLayout(new BorderLayout());
        
        panelTabla.setBackground(white);
        panelTabla.setBorder(null);
        panelTabla.getViewport().setBackground(white);

        tblMedicamentos.setFont(new Font("Inter", Font.PLAIN, 13));
        tblMedicamentos.setSelectionBackground(blue50);
        tblMedicamentos.setRowHeight(48);
        tblMedicamentos.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                int row = tblMedicamentos.rowAtPoint(e.getPoint());
                if (row >= 0 && e.getClickCount() >= 2) {
                    mostrarDetalle(row);
                }
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                tblMedicamentos.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                tblMedicamentos.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        panelTabla.setViewportView(tblMedicamentos);
        panelTablaWrapper.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelTablaWrapper, BorderLayout.CENTER);

        // Bottom bar
        panelInferior.setBackground(white);
        panelInferior.setBorder(new RoundedBorder(slate200, 1, 12));
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelInferior.setPreferredSize(new Dimension(0, 56));

        btnAgregar.setPreferredSize(new Dimension(200, 40));
        btnAgregar.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                mostrarFormularioAgregar();
            }
        });
        panelInferior.add(btnAgregar);

        panelPrincipal.add(panelInferior, BorderLayout.PAGE_END);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    // Metodo mostrarFormularioAgregar: logica de interfaz asociada a este formulario/panel.
    private void mostrarFormularioAgregar() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(white);
        formPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        formPanel.setPreferredSize(new Dimension(420, 820));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 8, 0);
        gbc.gridx = 0;
        gbc.weightx = 1;
        
        JTextField txtNombreMedicamento = createField();
        JComboBox<String> cboCategoria = new JComboBox<>(new String[]{
            "Angeticos", "Antiinflamatorios", "Antibioticos",
            "Antialergicos", "Gastricos", "Antidiabeticos", "Antihipertensivos"
        });
        cboCategoria.setFont(new Font("Inter", Font.PLAIN, 13));
        cboCategoria.setBackground(white);
        cboCategoria.setPreferredSize(new Dimension(320, 34));
        cboCategoria.setMaximumSize(new Dimension(320, 34));
        
        JTextField txtPrecio = createField();
        JTextField txtStock = createField();
        JTextField txtFormula = createField();
        JTextField txtUnidadMedida = createField();
        txtUnidadMedida.setText("UND");
        JComboBox<String> cboFormaFarmaceutica = new JComboBox<>(new String[]{"Pastilla", "Liquido"});
        cboFormaFarmaceutica.setFont(new Font("Inter", Font.PLAIN, 13));
        cboFormaFarmaceutica.setBackground(white);
        cboFormaFarmaceutica.setPreferredSize(new Dimension(320, 34));
        cboFormaFarmaceutica.setMaximumSize(new Dimension(320, 34));
        JComboBox<String> cboTipoComercial = new JComboBox<>(new String[]{"Generico", "Marca"});
        cboTipoComercial.setFont(new Font("Inter", Font.PLAIN, 13));
        cboTipoComercial.setBackground(white);
        cboTipoComercial.setPreferredSize(new Dimension(320, 34));
        cboTipoComercial.setMaximumSize(new Dimension(320, 34));
        
        JPanel panelCamposEspecificos = new JPanel(new GridBagLayout());
        panelCamposEspecificos.setBackground(slate50);
        panelCamposEspecificos.setBorder(new RoundedBorder(slate200, 1, 8));
        panelCamposEspecificos.setPreferredSize(new Dimension(320, 210));
        
        JTextField txtIngredienteActivo = createField();
        JTextField txtLaboratorio = createField();
        JTextField txtMarca = createField();
        JTextField txtPatente = createField();
        JTextField txtVolumenMl = createField();
        JTextField txtTipoLiquido = createField();
        JTextField txtCantidadUnidades = createField();
        JTextField txtTipoPastilla = createField();
        
        updateCamposEspecificos(panelCamposEspecificos, cboFormaFarmaceutica, cboTipoComercial,
            txtIngredienteActivo, txtLaboratorio, txtMarca, txtPatente,
            txtVolumenMl, txtTipoLiquido, txtCantidadUnidades, txtTipoPastilla);
        
        cboFormaFarmaceutica.addActionListener(e -> updateCamposEspecificos(panelCamposEspecificos, cboFormaFarmaceutica, cboTipoComercial,
            txtIngredienteActivo, txtLaboratorio, txtMarca, txtPatente,
            txtVolumenMl, txtTipoLiquido, txtCantidadUnidades, txtTipoPastilla));
        cboTipoComercial.addActionListener(e -> updateCamposEspecificos(panelCamposEspecificos, cboFormaFarmaceutica, cboTipoComercial,
            txtIngredienteActivo, txtLaboratorio, txtMarca, txtPatente,
            txtVolumenMl, txtTipoLiquido, txtCantidadUnidades, txtTipoPastilla));
        
        String[] labels = {"Nombre del Medicamento", "Categoria", "Precio (COP)", "Stock", "Formula / Composicion", "Unidad de Medida", "Forma Farmaceutica", "Tipo Comercial"};
        Component[] fields = {txtNombreMedicamento, cboCategoria, txtPrecio, txtStock, txtFormula, txtUnidadMedida, cboFormaFarmaceutica, cboTipoComercial};
        
        int row = 0;
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.insets = new Insets(i > 0 ? 6 : 2, 0, 2, 0);
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Inter", Font.BOLD, 11));
            lbl.setForeground(slate500);
            formPanel.add(lbl, gbc);
            
            gbc.gridy = row + 1;
            gbc.weightx = 1;
            gbc.insets = new Insets(0, 0, 6, 0);
            formPanel.add(fields[i], gbc);
            row += 2;
        }
        
        gbc.gridy = row;
        gbc.weightx = 1;
        gbc.insets = new Insets(8, 0, 4, 0);
        JLabel lblCamposExtra = new JLabel("Campos especificos");
        lblCamposExtra.setFont(new Font("Inter", Font.BOLD, 11));
        lblCamposExtra.setForeground(slate500);
        formPanel.add(lblCamposExtra, gbc);
        
        gbc.gridy = row + 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        formPanel.add(panelCamposEspecificos, gbc);
        row += 2;
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setBackground(white);
        btnPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        JButton btnCancel = createDialogBtn("Cancelar", slate200, slate700);
        JButton btnSave = createDialogBtn("Guardar Medicamento", blue600, white);
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        
        gbc.gridy = row + 1;
        gbc.insets = new Insets(8, 0, 0, 0);
        formPanel.add(btnPanel, gbc);
        
        final boolean[] saved = {false};
        btnCancel.addActionListener(e -> { saved[0] = false; JOptionPane.getRootFrame().dispose(); });
        btnSave.addActionListener(e -> { saved[0] = true; JOptionPane.getRootFrame().dispose(); });
        
        txtNombreMedicamento.requestFocusInWindow();
        
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        formScroll.setPreferredSize(new Dimension(500, 620));

        JOptionPane optionPane = new JOptionPane(formScroll, JOptionPane.PLAIN_MESSAGE, JOptionPane.NO_OPTION);
        javax.swing.JDialog dialog = optionPane.createDialog(this, "Nuevo Medicamento");
        dialog.setResizable(true);
        dialog.setVisible(true);
        
        if (!saved[0]) return;
        
        String nombre = txtNombreMedicamento.getText().trim();
        String categoria = cboCategoria.getSelectedItem().toString();
        String precioStr = txtPrecio.getText().trim();
        String stockStr = txtStock.getText().trim();
        String formula = txtFormula.getText().trim();
        String unidad = txtUnidadMedida.getText().trim();
        
        if (nombre.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre y precio son obligatorios",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double precio = Double.parseDouble(precioStr);
            int stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);
            
            int newId = medicamentoDAO.total() + 1;
            String idMed = "MED" + String.format("%03d", newId);
            
            String forma = cboFormaFarmaceutica.getSelectedItem().toString().toLowerCase();
            String comercial = cboTipoComercial.getSelectedItem().toString().toLowerCase();
            
            Medicamento med;
            
            if (forma.equals("pastilla") && comercial.equals("generico")) {
                String ucStr = txtCantidadUnidades.getText().trim();
                int unitCount = ucStr.isEmpty() ? Math.max(1, stock) : Integer.parseInt(ucStr);
                String pillType = txtTipoPastilla.getText().trim().isEmpty() ? "tableta" : txtTipoPastilla.getText().trim();
                String ingredient = txtIngredienteActivo.getText().trim().isEmpty() ? nombre : txtIngredienteActivo.getText().trim();
                String lab = txtLaboratorio.getText().trim().isEmpty() ? "Generico" : txtLaboratorio.getText().trim();
                med = new entidades.PastillaGenerica(stock, nombre, formula.isEmpty() ? "Sin formula especificada" : formula, idMed, precio, categoria, unidad.isEmpty() ? "UND" : unidad, unitCount, pillType, ingredient, lab);
            } else if (forma.equals("pastilla") && comercial.equals("marca")) {
                String ucStr = txtCantidadUnidades.getText().trim();
                int unitCount = ucStr.isEmpty() ? Math.max(1, stock) : Integer.parseInt(ucStr);
                String pillType = txtTipoPastilla.getText().trim().isEmpty() ? "tableta" : txtTipoPastilla.getText().trim();
                String brand = txtMarca.getText().trim().isEmpty() ? "Sin marca" : txtMarca.getText().trim();
                String patent = txtPatente.getText().trim().isEmpty() ? "N/A" : txtPatente.getText().trim();
                med = new entidades.PastillaMarca(stock, nombre, formula.isEmpty() ? "Sin formula especificada" : formula, idMed, precio, categoria, unidad.isEmpty() ? "UND" : unidad, unitCount, pillType, brand, patent);
            } else if (forma.equals("liquido") && comercial.equals("generico")) {
                String volStr = txtVolumenMl.getText().trim();
                double volumeMl = volStr.isEmpty() ? 100 : Double.parseDouble(volStr);
                String liquidType = txtTipoLiquido.getText().trim().isEmpty() ? "jarabe" : txtTipoLiquido.getText().trim();
                String ingredient = txtIngredienteActivo.getText().trim().isEmpty() ? nombre : txtIngredienteActivo.getText().trim();
                String lab = txtLaboratorio.getText().trim().isEmpty() ? "Generico" : txtLaboratorio.getText().trim();
                med = new entidades.LiquidoGenerico(stock, nombre, formula.isEmpty() ? "Sin formula especificada" : formula, idMed, precio, categoria, unidad.isEmpty() ? "UND" : unidad, volumeMl, liquidType, ingredient, lab);
            } else {
                String volStr = txtVolumenMl.getText().trim();
                double volumeMl = volStr.isEmpty() ? 100 : Double.parseDouble(volStr);
                String liquidType = txtTipoLiquido.getText().trim().isEmpty() ? "jarabe" : txtTipoLiquido.getText().trim();
                String brand = txtMarca.getText().trim().isEmpty() ? "Sin marca" : txtMarca.getText().trim();
                String patent = txtPatente.getText().trim().isEmpty() ? "N/A" : txtPatente.getText().trim();
                med = new entidades.LiquidoMarca(stock, nombre, formula.isEmpty() ? "Sin formula especificada" : formula, idMed, precio, categoria, unidad.isEmpty() ? "UND" : unidad, volumeMl, liquidType, brand, patent);
            }
            
            med.setTipoForma(forma);
            med.setTipoComercial(comercial);
            boolean exito = medicamentoDAO.insertar(med);
            
            if (exito) {
                actualizarTabla();
                JOptionPane.showMessageDialog(this,
                    "Medicamento agregado exitosamente", "Exito",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al agregar el medicamento. Verifica los datos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Precio y stock deben ser numeros validos",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Metodo createField: logica de interfaz asociada a este formulario/panel.
    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Inter", Font.PLAIN, 13));
        field.setForeground(slate900);
        field.setBackground(white);
        field.setBorder(new LineBorder(slate200, 1, true));
        field.setPreferredSize(new Dimension(320, 34));
        field.setMaximumSize(new Dimension(320, 34));
        field.setMargin(new Insets(4, 10, 4, 10));
        field.addFocusListener(new FocusAdapter() {
            @Override
            // Metodo focusGained: logica de interfaz asociada a este formulario/panel.
            public void focusGained(FocusEvent e) {
                field.setBorder(new LineBorder(blue500, 2, true));
            }
            @Override
            // Metodo focusLost: logica de interfaz asociada a este formulario/panel.
            public void focusLost(FocusEvent e) {
                field.setBorder(new LineBorder(slate200, 1, true));
            }
        });
        return field;
    }
    
    // Metodo createDialogBtn: logica de interfaz asociada a este formulario/panel.
    private JButton createDialogBtn(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Inter", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(text.length() > 15 ? 160 : 90, 34));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.equals(blue600) ? blue700 : slate300);
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }
    
    // Metodo updateCamposEspecificos: logica de interfaz asociada a este formulario/panel.
    private void updateCamposEspecificos(JPanel panel, JComboBox<String> cboForma, JComboBox<String> cboTipoComercial,
            JTextField txtIngredienteActivo, JTextField txtLaboratorio, JTextField txtMarca, JTextField txtPatente,
            JTextField txtVolumenMl, JTextField txtTipoLiquido, JTextField txtCantidadUnidades, JTextField txtTipoPastilla) {
        panel.removeAll();
        String forma = cboForma.getSelectedItem().toString().toLowerCase();
        String comercial = cboTipoComercial.getSelectedItem().toString().toLowerCase();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.gridx = 0;
        gbc.weightx = 0;
        int row = 0;
        
        if (forma.equals("pastilla")) {
            addCampoExtra(panel, gbc, row++, "Unidades:", txtCantidadUnidades);
            addCampoExtra(panel, gbc, row++, "Tipo:", txtTipoPastilla);
            if (comercial.equals("generico")) {
                addCampoExtra(panel, gbc, row++, "Ingrediente:", txtIngredienteActivo);
                addCampoExtra(panel, gbc, row++, "Laboratorio:", txtLaboratorio);
            } else {
                addCampoExtra(panel, gbc, row++, "Marca:", txtMarca);
                addCampoExtra(panel, gbc, row++, "Patente:", txtPatente);
            }
        } else {
            addCampoExtra(panel, gbc, row++, "Volumen (ml):", txtVolumenMl);
            addCampoExtra(panel, gbc, row++, "Tipo:", txtTipoLiquido);
            if (comercial.equals("generico")) {
                addCampoExtra(panel, gbc, row++, "Ingrediente:", txtIngredienteActivo);
                addCampoExtra(panel, gbc, row++, "Laboratorio:", txtLaboratorio);
            } else {
                addCampoExtra(panel, gbc, row++, "Marca:", txtMarca);
                addCampoExtra(panel, gbc, row++, "Patente:", txtPatente);
            }
        }
        
        panel.revalidate();
        panel.repaint();
    }
    
    // Metodo addCampoExtra: logica de interfaz asociada a este formulario/panel.
    private void addCampoExtra(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        gbc.gridy = row * 2;
        gbc.weightx = 0;
        gbc.insets = new Insets(4, 8, 2, 0);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Inter", Font.BOLD, 10));
        lbl.setForeground(slate500);
        panel.add(lbl, gbc);
        
        gbc.gridy = row * 2 + 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 8, 6, 8);
        panel.add(field, gbc);
    }
    
    // Metodo mostrarDetalle: logica de interfaz asociada a este formulario/panel.
    private void mostrarDetalle(int row) {
        try {
            String id = tableModel.getValueAt(row, 0).toString();
            String nombre = tableModel.getValueAt(row, 1).toString();
            String categoria = tableModel.getValueAt(row, 2).toString();
            String precio = tableModel.getValueAt(row, 3).toString();
            String stock = tableModel.getValueAt(row, 4).toString();
            String formula = tableModel.getValueAt(row, 5).toString();
            String estado = tableModel.getValueAt(row, 6).toString();
            
            String mensaje = String.format(
                "ID: %s\nMedicamento: %s\nCategoria: %s\nPrecio: %s\nStock: %s\nFormula: %s\nEstado: %s",
                id, nombre, categoria, precio, stock, formula, estado);
            
            JOptionPane.showMessageDialog(this, mensaje,
                "Detalle del Medicamento", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) { /* ignore */ }
    }

    // Variables declaration
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JScrollPane panelTabla;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelSearchWrap;
    private javax.swing.JPanel panelTablaWrapper;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelTituloWrap;
    private javax.swing.JTable tblMedicamentos;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration
    
    // ===== UI Components =====
    
    class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color; private int thickness; private int radius;
        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color; this.thickness = thickness; this.radius = radius;
        }
        @Override
        // Metodo paintBorder: logica de interfaz asociada a este formulario/panel.
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public java.awt.Insets getBorderInsets(Component c) {
            return new java.awt.Insets(thickness, thickness, thickness, thickness);
        }
    }
    
    class ModernButton extends JButton {
        private Color bgColor = blue600;
        private Color hoverColor = blue700;
        private boolean isHover = false;
        public ModernButton(String text) {
            super(text);
            setFont(new Font("Inter", Font.BOLD, 13));
            setForeground(white);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { isHover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { isHover = false; repaint(); }
            });
        }
        @Override
        // Metodo paintComponent: logica de interfaz asociada a este formulario/panel.
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isHover ? hoverColor : bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    class StatusCellRenderer extends JLabel implements TableCellRenderer {
        public StatusCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Inter", Font.BOLD, 12));
            setBorder(new EmptyBorder(6, 12, 6, 12));
        }
        @Override
        // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            String estado = value.toString();
            setText(estado);
            Color bg, fg;
            if (estado.equals("Disponible")) { bg = green50; fg = green500; }
            else if (estado.equals("Bajo")) { bg = amber50; fg = amber500; }
            else { bg = red50; fg = red500; }
            if (isSelected) { setBackground(blue50); } else { setBackground(bg); }
            setForeground(fg);
            return this;
        }
    }
    
    class LucideIcon implements javax.swing.Icon {
        private String name;
        private int size;
        private Color color;
        
        public LucideIcon(String name, int size, Color color) {
            this.name = name;
            this.size = size;
            this.color = color;
        }
        
        @Override
        // Metodo getIconWidth: logica de interfaz asociada a este formulario/panel.
        public int getIconWidth() { return size; }
        @Override
        // Metodo getIconHeight: logica de interfaz asociada a este formulario/panel.
        public int getIconHeight() { return size; }
        
        @Override
        // Metodo paintIcon: logica de interfaz asociada a este formulario/panel.
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(color);
            
            double scale = size / 24.0;
            g2.translate(x, y);
            g2.scale(scale, scale);
            
            switch (name) {
                case "search":
                    g2.drawOval(3, 3, 10, 10);
                    g2.drawLine(11, 11, 19, 19);
                    break;
                case "plus":
                    g2.drawLine(12, 5, 12, 19);
                    g2.drawLine(5, 12, 19, 12);
                    break;
            }
            
            g2.dispose();
        }
    }
}

