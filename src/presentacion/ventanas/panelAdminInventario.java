package presentacion.ventanas;

import data.FacturaDAO;
import data.LoteInventarioDAO;
import database.Conexion;
import inventario.LoteInventario;
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
import javax.swing.DefaultListCellRenderer;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * panelAdminInventario: clase del proyecto HealthPharmacy.
 */
public class panelAdminInventario extends javax.swing.JPanel {
    
    private formAdmin parent;
    private DefaultTableModel tableModel;
    private LoteInventarioDAO loteDAO;
    private FacturaDAO facturaDAO;
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
    private Color purple500 = new Color(168, 85, 247);
    private Color purple50 = new Color(250, 245, 255);

    public panelAdminInventario(formAdmin parent) {
        this.parent = parent;
        this.loteDAO = new LoteInventarioDAO();
        this.facturaDAO = new FacturaDAO();
        initComponents();
        initTable();
        cargarDatos();
    }
    
    // Metodo initTable: logica de interfaz asociada a este formulario/panel.
    private void initTable() {
        tableModel = new DefaultTableModel(
            new Object[]{"Lote", "Medicamento", "Categoria", "Precio", "Cantidad", "Vencimiento", "Estado"},
            0
        ) {
            @Override
            // Metodo isCellEditable: logica de interfaz asociada a este formulario/panel.
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblInventario.setModel(tableModel);
        tblInventario.setRowHeight(48);
        tblInventario.setFont(new Font("Inter", Font.PLAIN, 13));
        tblInventario.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tblInventario.getTableHeader().setBackground(slate50);
        tblInventario.getTableHeader().setForeground(slate500);
        tblInventario.getTableHeader().setPreferredSize(new Dimension(0, 40));
        tblInventario.getTableHeader().setBorder(new RoundedBorder(slate200, 0, 0));
        tblInventario.setSelectionBackground(blue50);
        tblInventario.setGridColor(slate100);
        tblInventario.setShowVerticalLines(false);
        tblInventario.setShowHorizontalLines(true);
        tblInventario.setBackground(white);
        tblInventario.setIntercellSpacing(new Dimension(0, 0));
        
        tblInventario.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblInventario.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblInventario.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblInventario.getColumnModel().getColumn(3).setPreferredWidth(100);
        tblInventario.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblInventario.getColumnModel().getColumn(5).setPreferredWidth(110);
        tblInventario.getColumnModel().getColumn(6).setPreferredWidth(100);
        
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
        tblInventario.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblInventario.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tblInventario.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        
        // Price renderer
        tblInventario.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
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
        tblInventario.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        tblInventario.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        
        StatusCellRenderer statusRenderer = new StatusCellRenderer();
        tblInventario.getColumnModel().getColumn(6).setCellRenderer(statusRenderer);
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
        actualizarResumen();
    }
    
    // Metodo actualizarTabla: logica de interfaz asociada a este formulario/panel.
    private void actualizarTabla() {
        tableModel.setRowCount(0);
        String filtro = txtBuscar.getText().toLowerCase().trim();
        if (filtro.contains("buscar")) filtro = "";
        
        List<LoteInventario> lista = loteDAO.listar("");
        
        for (LoteInventario lote : lista) {
            Medicamento med = lote.getMedicamento();
            String nombre = med.getName().toLowerCase();
            String cat = med.getCategory().toLowerCase();
            
            if (filtro.isEmpty() || nombre.contains(filtro) || cat.contains(filtro)) {
                String estado;
                if (lote.getAvailableQuantity() == 0) estado = "Agotado";
                else if (lote.isExpired()) estado = "Vencido";
                else if (lote.getAvailableQuantity() <= 10) estado = "Bajo";
                else estado = "OK";
                
                tableModel.addRow(new Object[]{
                    lote.getLoteNumber(), med.getName(), med.getCategory(),
                    med.getPrice(), lote.getAvailableQuantity(), 
                    lote.getDueDate().toString(), estado
                });
            }
        }
        actualizarResumen();
    }
    
    // Metodo actualizarResumen: logica de interfaz asociada a este formulario/panel.
    private void actualizarResumen() {
        List<LoteInventario> lista = loteDAO.listar("");
        int totalLotes = lista.size();
        int bajos = 0;
        int agotados = 0;
        int valorTotal = 0;
        
        for (LoteInventario lote : lista) {
            int stock = lote.getAvailableQuantity();
            double precio = lote.getMedicamento().getPrice();
            valorTotal += stock * precio;
            
            if (stock == 0) agotados++;
            else if (stock <= 10) bajos++;
        }
        
        lblTotalProductos.setText(String.valueOf(totalLotes));
        lblStockBajo.setText(String.valueOf(bajos));
        lblAgotados.setText(String.valueOf(agotados));
        lblValorInventario.setText(formatoCOP.format(valorTotal));
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
        panelCategoriaWrap = new javax.swing.JPanel();
        cboCategoria = new javax.swing.JComboBox<>();
        panelStats = new javax.swing.JPanel();
        panelTablaWrapper = new javax.swing.JPanel();
        panelTabla = new javax.swing.JScrollPane();
        tblInventario = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();
        btnAjustarStock = new ModernButton("Ajustar Stock");
        btnAgregar = new ModernButton("+  Nuevo Lote");

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
        lblTitulo.setText("Inventario");
        panelTituloWrap.add(lblTitulo);

        lblSubtitulo.setFont(new Font("Inter", Font.PLAIN, 14));
        lblSubtitulo.setForeground(slate500);
        lblSubtitulo.setText("Gestiona los lotes y stock de medicamentos");
        panelTituloWrap.add(lblSubtitulo);

        panelHeader.add(panelTituloWrap, BorderLayout.CENTER);

        // Search
        panelSearchWrap.setBackground(slate50);
        panelSearchWrap.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));

        panelSearch.setBackground(white);
        panelSearch.setBorder(new RoundedBorder(slate200, 1, 10));
        panelSearch.setLayout(new BorderLayout());
        panelSearch.setPreferredSize(new Dimension(280, 40));

        JLabel lblSearchIcon = new JLabel();
        lblSearchIcon.setIcon(new LucideIcon("search", 18, slate400));
        lblSearchIcon.setBorder(new EmptyBorder(0, 12, 0, 8));
        panelSearch.add(lblSearchIcon, BorderLayout.WEST);

        txtBuscar.setFont(new Font("Inter", Font.PLAIN, 13));
        txtBuscar.setForeground(slate500);
        txtBuscar.setText("Buscar producto...");
        txtBuscar.setBorder(null);
        txtBuscar.setBackground(white);
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            // Metodo focusGained: logica de interfaz asociada a este formulario/panel.
            public void focusGained(FocusEvent evt) {
                if (txtBuscar.getText().equals("Buscar producto...")) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(slate900);
                }
            }
            @Override
            // Metodo focusLost: logica de interfaz asociada a este formulario/panel.
            public void focusLost(FocusEvent evt) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText("Buscar producto...");
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

        // Stats Row
        panelStats.setLayout(new FlowLayout(FlowLayout.LEFT, 16, 0));
        panelStats.setBackground(slate50);
        panelStats.setPreferredSize(new Dimension(0, 72));

        statTotal = createStatCard("Lotes", "0", blue50, blue600);
        statBajo = createStatCard("Stock Bajo", "0", amber50, amber500);
        statAgotado = createStatCard("Agotados", "0", red50, red500);
        statValor = createStatCard("Valor Total", "$0", purple50, purple500);

        panelStats.add(statTotal);
        panelStats.add(statBajo);
        panelStats.add(statAgotado);
        panelStats.add(statValor);
        panelPrincipal.add(panelStats, BorderLayout.PAGE_START);

        // Table
        panelTablaWrapper.setBackground(white);
        panelTablaWrapper.setBorder(new RoundedBorder(slate200, 1, 12));
        panelTablaWrapper.setLayout(new BorderLayout());
        
        panelTabla.setBackground(white);
        panelTabla.setBorder(null);
        panelTabla.getViewport().setBackground(white);

        tblInventario.setFont(new Font("Inter", Font.PLAIN, 13));
        tblInventario.setSelectionBackground(blue50);
        tblInventario.setRowHeight(48);
        tblInventario.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                int row = tblInventario.rowAtPoint(e.getPoint());
                if (row >= 0 && e.getClickCount() >= 2) {
                    mostrarDetalle(row);
                }
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                tblInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                tblInventario.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        panelTabla.setViewportView(tblInventario);
        panelTablaWrapper.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelTablaWrapper, BorderLayout.CENTER);

        // Bottom bar
        panelInferior.setBackground(white);
        panelInferior.setBorder(new RoundedBorder(slate200, 1, 12));
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelInferior.setPreferredSize(new Dimension(0, 56));

        btnAjustarStock.setPreferredSize(new Dimension(140, 40));
        btnAjustarStock.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                ajustarStockMasivo();
            }
        });
        panelInferior.add(btnAjustarStock);

        btnAgregar.setPreferredSize(new Dimension(160, 40));
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
    
    private javax.swing.JPanel createStatCard(String label, String value, Color bgColor, Color textColor) {
        javax.swing.JPanel card = new javax.swing.JPanel(new java.awt.GridLayout(2, 1, 0, 4));
        card.setBackground(bgColor);
        card.setBorder(new RoundedBorder(bgColor, 0, 10));
        card.setPreferredSize(new Dimension(160, 64));
        
        JLabel lblVal = new JLabel(value, SwingConstants.CENTER);
        lblVal.setFont(new Font("Inter", Font.BOLD, 20));
        lblVal.setForeground(textColor);
        lblVal.setOpaque(false);
        
        JLabel lblLbl = new JLabel(label, SwingConstants.CENTER);
        lblLbl.setFont(new Font("Inter", Font.PLAIN, 12));
        lblLbl.setForeground(new Color(100, 116, 139));
        lblLbl.setOpaque(false);
        
        card.add(lblVal);
        card.add(lblLbl);
        
        if (label.equals("Lotes")) lblTotalProductos = lblVal;
        else if (label.equals("Stock Bajo")) lblStockBajo = lblVal;
        else if (label.equals("Agotados")) lblAgotados = lblVal;
        else if (label.equals("Valor Total")) lblValorInventario = lblVal;
        
        return card;
    }

    // Metodo mostrarFormularioAgregar: logica de interfaz asociada a este formulario/panel.
    private void mostrarFormularioAgregar() {
        JPanel formPanel = new JPanel(new java.awt.GridLayout(5, 2, 12, 12));
        formPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        
        JTextField txtNumeroLote = createField();
        JTextField txtCantidadLote = createField();
        JTextField txtFechaVencimiento = createField("2026-12-31");
        JTextField txtIdMedicamentoLote = createField();
        
        String[] labels = {"Numero Lote:", "Cantidad:", "Vencimiento (YYYY-MM-DD):", "ID Medicamento:"};
        JTextField[] fields = {txtNumeroLote, txtCantidadLote, txtFechaVencimiento, txtIdMedicamentoLote};
        
        for (int i = 0; i < 4; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Inter", Font.BOLD, 12));
            lbl.setForeground(slate700);
            formPanel.add(lbl);
            formPanel.add(fields[i]);
        }
        formPanel.add(new JLabel());
        formPanel.add(new JLabel());
        
        int result = JOptionPane.showConfirmDialog(this, formPanel,
            "Nuevo Lote", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String idLote = txtNumeroLote.getText().trim();
            String cantidadStr = txtCantidadLote.getText().trim();
            String vencimiento = txtFechaVencimiento.getText().trim();
            String idMedStr = txtIdMedicamentoLote.getText().trim();
            
            if (idLote.isEmpty() || cantidadStr.isEmpty() || idMedStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los campos obligatorios",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int cantidad = Integer.parseInt(cantidadStr);
                String idMed = idMedStr;
                java.time.LocalDate fecha = java.time.LocalDate.parse(vencimiento);
                
                // Get the medicine from DB
                List<Medicamento> meds = new data.MedicamentoDAO().listar("");
                Medicamento med = null;
                for (Medicamento m : meds) {
                    if (m.getCode().equals(idMed)) {
                        med = m;
                        break;
                    }
                }
                
                if (med == null) {
                    JOptionPane.showMessageDialog(this,
                        "Medicamento con ID " + idMed + " no encontrado",
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                LoteInventario lote = new LoteInventario(idLote, fecha, cantidad, med);
                boolean exito = loteDAO.insertar(lote);
                
                if (exito) {
                    actualizarTabla();
                    JOptionPane.showMessageDialog(this,
                        "Lote agregado exitosamente", "Exito",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Error al agregar el lote",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Cantidad e ID deben ser numeros validos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this,
                    "Fecha debe tener formato YYYY-MM-DD",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Metodo ajustarStockMasivo: logica de interfaz asociada a este formulario/panel.
    private void ajustarStockMasivo() {
        int selectedRow = tblInventario.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un lote de la tabla",
                "Atencion", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String idLote = tableModel.getValueAt(selectedRow, 0).toString();
        String nombre = tableModel.getValueAt(selectedRow, 1).toString();
        int stockActual = Integer.parseInt(tableModel.getValueAt(selectedRow, 4).toString());
        
        JTextField txtCantidad = new JTextField();
        txtCantidad.setFont(new Font("Inter", Font.PLAIN, 14));
        txtCantidad.setHorizontalAlignment(JTextField.CENTER);
        txtCantidad.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        
        String[] opciones = {"Entrada (+)", "Salida (-)", "Ajuste Manual"};
        JComboBox<String> cboTipo = new JComboBox<>(opciones);
        cboTipo.setFont(new Font("Inter", Font.PLAIN, 13));
        
        JPanel panel = new JPanel(new java.awt.GridLayout(5, 2, 12, 12));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.add(new JLabel("Lote:"));
        panel.add(new JLabel(idLote));
        panel.add(new JLabel("Medicamento:"));
        panel.add(new JLabel(nombre));
        panel.add(new JLabel("Stock Actual:"));
        panel.add(new JLabel(String.valueOf(stockActual)));
        panel.add(new JLabel("Tipo de Ajuste:"));
        panel.add(cboTipo);
        panel.add(new JLabel("Cantidad:"));
        panel.add(txtCantidad);
        
        JPanel inputPanel = new JPanel(new BorderLayout(8, 8));
        inputPanel.setPreferredSize(new Dimension(430, 260));
        inputPanel.add(panel, BorderLayout.CENTER);

        JLabel helper = new JLabel("Entrada suma, salida resta, ajuste manual reemplaza el stock.");
        helper.setFont(new Font("Inter", Font.PLAIN, 11));
        helper.setForeground(slate500);
        helper.setBorder(new EmptyBorder(0, 16, 8, 16));
        inputPanel.add(helper, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(inputPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setPreferredSize(new Dimension(460, 280));

        int result = JOptionPane.showConfirmDialog(this, scrollPane,
            "Ajustar Stock - " + nombre, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int cantidad = Integer.parseInt(txtCantidad.getText().trim());
                int nuevoStock = stockActual;
                String tipo = cboTipo.getSelectedItem().toString();
                
                if (tipo.equals("Entrada (+)")) nuevoStock = stockActual + cantidad;
                else if (tipo.equals("Salida (-)")) {
                    nuevoStock = stockActual - cantidad;
                    if (nuevoStock < 0) {
                        JOptionPane.showMessageDialog(this,
                            "Stock insuficiente para esta salida",
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
                else if (tipo.equals("Ajuste Manual")) nuevoStock = cantidad;
                
                // Get the lote from DB and update
                List<LoteInventario> lista = loteDAO.listar("");
                for (LoteInventario lote : lista) {
                    if (lote.getLoteNumber().equals(idLote)) {
                        lote.setAvailableQuantity(nuevoStock);
                        boolean exito = loteDAO.actualizar(lote);
                        
                        if (exito) {
                            actualizarTabla();
                            JOptionPane.showMessageDialog(this,
                                String.format("Stock actualizado: %d -> %d", stockActual, nuevoStock),
                                "Stock Actualizado", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                "Error al actualizar el stock",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        break;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "Ingrese una cantidad valida",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Metodo mostrarDetalle: logica de interfaz asociada a este formulario/panel.
    private void mostrarDetalle(int row) {
        try {
            String lote = tableModel.getValueAt(row, 0).toString();
            String nombre = tableModel.getValueAt(row, 1).toString();
            String categoria = tableModel.getValueAt(row, 2).toString();
            String precio = tableModel.getValueAt(row, 3).toString();
            int stock = Integer.parseInt(tableModel.getValueAt(row, 4).toString());
            String vencimiento = tableModel.getValueAt(row, 5).toString();
            String estado = tableModel.getValueAt(row, 6).toString();
            
            double precioNum = Double.parseDouble(precio.replace(".", "").replace(",", ""));
            String valorTotal = formatoCOP.format(precioNum * stock);
            
            String mensaje = String.format(
                "Lote: %s\nMedicamento: %s\nCategoria: %s\nPrecio: %s\nStock: %d\nVencimiento: %s\nEstado: %s\nValor en Inventario: %s",
                lote, nombre, categoria, precio, stock, vencimiento, estado, valorTotal);
            
            JOptionPane.showMessageDialog(this, mensaje,
                "Detalle del Lote", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) { /* ignore */ }
    }

    // Variables declaration
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTotalProductos;
    private javax.swing.JLabel lblStockBajo;
    private javax.swing.JLabel lblAgotados;
    private javax.swing.JLabel lblValorInventario;
    private javax.swing.JButton btnAjustarStock;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JScrollPane panelTabla;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelSearchWrap;
    private javax.swing.JPanel panelCategoriaWrap;
    private javax.swing.JPanel panelStats;
    private javax.swing.JPanel panelTablaWrapper;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelTituloWrap;
    private javax.swing.JPanel statTotal;
    private javax.swing.JPanel statBajo;
    private javax.swing.JPanel statAgotado;
    private javax.swing.JPanel statValor;
    private javax.swing.JTable tblInventario;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JComboBox<String> cboCategoria;
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
            if (estado.equals("OK")) { bg = green50; fg = green500; }
            else if (estado.equals("Bajo")) { bg = amber50; fg = amber500; }
            else if (estado.equals("Agotado")) { bg = red50; fg = red500; }
            else if (estado.equals("Vencido")) { bg = red50; fg = red500; }
            else { bg = slate50; fg = slate500; }
            
            if (isSelected) setBackground(blue50);
            else setBackground(bg);
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
            }
            
            g2.dispose();
        }
    }

    // Metodo createField: logica de interfaz asociada a este formulario/panel.
    private JTextField createField() {
        return createField("");
    }

    // Metodo createField: logica de interfaz asociada a este formulario/panel.
    private JTextField createField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Inter", Font.PLAIN, 13));
        field.setPreferredSize(new Dimension(200, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
            BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        field.setText(placeholder);
        if (!placeholder.isEmpty()) {
            field.setForeground(new Color(148, 163, 184));
        }
        field.addFocusListener(new FocusAdapter() {
            @Override
            // Metodo focusGained: logica de interfaz asociada a este formulario/panel.
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(59, 130, 246), 2),
                    BorderFactory.createEmptyBorder(3, 9, 3, 9)
                ));
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(slate900);
                }
            }
            @Override
            // Metodo focusLost: logica de interfaz asociada a este formulario/panel.
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                    BorderFactory.createEmptyBorder(4, 10, 4, 10)
                ));
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(148, 163, 184));
                }
            }
        });
        return field;
    }
}

