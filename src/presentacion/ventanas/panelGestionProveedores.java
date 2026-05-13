package presentacion.ventanas;

import data.ProveedorDAO;
import database.Conexion;
import inventario.Proveedor;

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
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
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
 * panelGestionProveedores: clase del proyecto HealthPharmacy.
 */
public class panelGestionProveedores extends javax.swing.JPanel {
    
    private formAdmin parent;
    private DefaultTableModel tableModel;
    private ProveedorDAO proveedorDAO;
    
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

    public panelGestionProveedores(formAdmin parent) {
        this.parent = parent;
        this.proveedorDAO = new ProveedorDAO();
        initComponents();
        initTable();
        cargarDatos();
    }
    
    // Metodo initTable: logica de interfaz asociada a este formulario/panel.
    private void initTable() {
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Empresa", "Producto", "Telefono", "Email", "Lotes", "Estado"},
            0
        ) {
            @Override
            // Metodo isCellEditable: logica de interfaz asociada a este formulario/panel.
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tblProveedores.setModel(tableModel);
        tblProveedores.setRowHeight(48);
        tblProveedores.setFont(new Font("Inter", Font.PLAIN, 13));
        tblProveedores.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tblProveedores.getTableHeader().setBackground(slate50);
        tblProveedores.getTableHeader().setForeground(slate500);
        tblProveedores.getTableHeader().setPreferredSize(new Dimension(0, 40));
        tblProveedores.getTableHeader().setBorder(new RoundedBorder(slate200, 0, 0));
        tblProveedores.setSelectionBackground(blue50);
        tblProveedores.setGridColor(slate100);
        tblProveedores.setShowVerticalLines(false);
        tblProveedores.setShowHorizontalLines(true);
        tblProveedores.setBackground(white);
        tblProveedores.setIntercellSpacing(new Dimension(0, 0));
        
        tblProveedores.getColumnModel().getColumn(0).setPreferredWidth(80);
        tblProveedores.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblProveedores.getColumnModel().getColumn(2).setPreferredWidth(140);
        tblProveedores.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblProveedores.getColumnModel().getColumn(4).setPreferredWidth(180);
        tblProveedores.getColumnModel().getColumn(5).setPreferredWidth(70);
        tblProveedores.getColumnModel().getColumn(6).setPreferredWidth(100);
        
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
        tblProveedores.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblProveedores.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblProveedores.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        
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
        tblProveedores.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        tblProveedores.getColumnModel().getColumn(2).setCellRenderer(textRenderer);
        tblProveedores.getColumnModel().getColumn(4).setCellRenderer(textRenderer);
        
        StatusCellRenderer statusRenderer = new StatusCellRenderer();
        tblProveedores.getColumnModel().getColumn(6).setCellRenderer(statusRenderer);
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
        
        List<Proveedor> lista = proveedorDAO.listar("");
        
        for (Proveedor p : lista) {
            String nombre = p.getNombre().toLowerCase();
            String prod = p.getProducto().toLowerCase();
            
            if (filtro.isEmpty() || nombre.contains(filtro) || prod.contains(filtro)) {
                int lotes = proveedorDAO.getProductosCount(p.getId());
                String estado = p.getEstado() != null ? p.getEstado() : "Activo";
                tableModel.addRow(new Object[]{
                    p.getId(), p.getNombre(), p.getProducto(),
                    p.getTelefono(), p.getCorreo(), lotes, estado
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
        tblProveedores = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();
        btnAgregar = new ModernButton("+  Nuevo Proveedor");

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
        lblTitulo.setText("Proveedores");
        panelTituloWrap.add(lblTitulo);

        lblSubtitulo.setFont(new Font("Inter", Font.PLAIN, 14));
        lblSubtitulo.setForeground(slate500);
        lblSubtitulo.setText("Administra los proveedores del sistema");
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
        txtBuscar.setText("Buscar proveedor...");
        txtBuscar.setBorder(null);
        txtBuscar.setBackground(white);
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            // Metodo focusGained: logica de interfaz asociada a este formulario/panel.
            public void focusGained(FocusEvent evt) {
                if (txtBuscar.getText().equals("Buscar proveedor...")) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(slate900);
                }
            }
            @Override
            // Metodo focusLost: logica de interfaz asociada a este formulario/panel.
            public void focusLost(FocusEvent evt) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText("Buscar proveedor...");
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

        tblProveedores.setFont(new Font("Inter", Font.PLAIN, 13));
        tblProveedores.setSelectionBackground(blue50);
        tblProveedores.setRowHeight(48);
        tblProveedores.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                int row = tblProveedores.rowAtPoint(e.getPoint());
                int col = tblProveedores.columnAtPoint(e.getPoint());
                
                if (row >= 0 && col == 6) {
                    String id = tableModel.getValueAt(row, 0).toString();
                    String estadoActual = tableModel.getValueAt(row, 6).toString();
                    
                    String nuevoEstado = estadoActual.equals("Activo") ? "Inactivo" : "Activo";
                    int confirm = JOptionPane.showConfirmDialog(
                        panelGestionProveedores.this,
                        "Cambiar estado de " + estadoActual + " a " + nuevoEstado + "?",
                        "Confirmar cambio",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean exito = proveedorDAO.toggleEstado(id);
                        if (exito) {
                            actualizarTabla();
                        } else {
                            JOptionPane.showMessageDialog(panelGestionProveedores.this,
                                "Error al cambiar el estado", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (row >= 0 && e.getClickCount() >= 2) {
                    mostrarDetalle(row);
                }
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                tblProveedores.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                tblProveedores.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        panelTabla.setViewportView(tblProveedores);
        panelTablaWrapper.add(panelTabla, BorderLayout.CENTER);
        panelPrincipal.add(panelTablaWrapper, BorderLayout.CENTER);

        // Bottom bar
        panelInferior.setBackground(white);
        panelInferior.setBorder(new RoundedBorder(slate200, 1, 12));
        panelInferior.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelInferior.setPreferredSize(new Dimension(0, 56));

        btnAgregar.setPreferredSize(new Dimension(180, 40));
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
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        
        JTextField txtIdProveedor = createField();
        JTextField txtNombreEmpresa = createField();
        JTextField txtProductoPrincipal = createField();
        JTextField txtTelefono = createField();
        JTextField txtEmail = createField();
        
        String[] labels = {"ID Proveedor", "Nombre Empresa", "Producto Principal", "Telefono", "Email"};
        JTextField[] fields = {txtIdProveedor, txtNombreEmpresa, txtProductoPrincipal, txtTelefono, txtEmail};
        
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i * 2;
            gbc.weightx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Inter", Font.BOLD, 12));
            lbl.setForeground(slate600);
            formPanel.add(lbl, gbc);
            
            gbc.gridy = i * 2 + 1;
            gbc.weightx = 1;
            gbc.insets = new Insets(2, 0, 10, 0);
            formPanel.add(fields[i], gbc);
            gbc.insets = new Insets(6, 0, 6, 0);
        }
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        btnPanel.setBackground(white);
        btnPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
        
        JButton btnCancel = createDialogBtn("Cancelar", slate200, slate700);
        JButton btnSave = createDialogBtn("Guardar", blue600, white);
        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        
        gbc.gridy = labels.length * 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(16, 0, 0, 0);
        formPanel.add(btnPanel, gbc);
        
        final boolean[] saved = {false};
        
        btnCancel.addActionListener(e -> {
            saved[0] = false;
            JOptionPane.getRootFrame().dispose();
        });
        btnSave.addActionListener(e -> {
            saved[0] = true;
            JOptionPane.getRootFrame().dispose();
        });
        
        txtIdProveedor.requestFocusInWindow();
        
        JOptionPane optionPane = new JOptionPane(formPanel, JOptionPane.PLAIN_MESSAGE, JOptionPane.NO_OPTION);
        optionPane.createDialog(this, "Nuevo Proveedor").setVisible(true);
        
        if (!saved[0]) return;
        
        String id = txtIdProveedor.getText().trim();
        String nombre = txtNombreEmpresa.getText().trim();
        String producto = txtProductoPrincipal.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String email = txtEmail.getText().trim();
        
        if (id.isEmpty() || nombre.isEmpty() || producto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID, nombre y producto son obligatorios",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (proveedorDAO.existe(nombre)) {
            JOptionPane.showMessageDialog(this, "Ya existe un proveedor con ese nombre",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Proveedor prov = new Proveedor(id, nombre, producto, telefono, email);
        boolean exito = proveedorDAO.insertar(prov);
        
        if (exito) {
            actualizarTabla();
            JOptionPane.showMessageDialog(this,
                "Proveedor agregado exitosamente", "Exito",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Error al agregar el proveedor",
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
        btn.setPreferredSize(new Dimension(90, 34));
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        if (bg.equals(blue600)) {
            btn.setBorder(new RoundedBorder(blue600, 0, 8));
        } else {
            btn.setBorder(new RoundedBorder(slate300, 1, 8));
        }
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
    
    // Metodo mostrarDetalle: logica de interfaz asociada a este formulario/panel.
    private void mostrarDetalle(int row) {
        try {
            String id = tableModel.getValueAt(row, 0).toString();
            String empresa = tableModel.getValueAt(row, 1).toString();
            String producto = tableModel.getValueAt(row, 2).toString();
            String telefono = tableModel.getValueAt(row, 3).toString();
            String email = tableModel.getValueAt(row, 4).toString();
            String lotes = tableModel.getValueAt(row, 5).toString();
            String estado = tableModel.getValueAt(row, 6).toString();
            
            String mensaje = String.format(
                "ID: %s\nEmpresa: %s\nProducto: %s\nTelefono: %s\nEmail: %s\nLotes: %s\nEstado: %s",
                id, empresa, producto, telefono, email, lotes, estado);
            
            JOptionPane.showMessageDialog(this, mensaje,
                "Detalle del Proveedor", JOptionPane.INFORMATION_MESSAGE);
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
    private javax.swing.JTable tblProveedores;
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
            setText(estado + " \u27F3");
            Color bg, fg;
            if (estado.equals("Activo")) { bg = green50; fg = green500; }
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
            }
            
            g2.dispose();
        }
    }
}

