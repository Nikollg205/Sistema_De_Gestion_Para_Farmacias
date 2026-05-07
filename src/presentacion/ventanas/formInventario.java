package presentacion.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import data.MedicamentoDAO;
import medicamentos.Medicamento;

public class formInventario extends javax.swing.JPanel {
    
    private formVentas parent;
    private javax.swing.table.DefaultTableModel tableModel;
    private List<Map<String, Object>> inventarioData;
    private Map<String, Map<String, Object>> carrito;
    
    private static final NumberFormat COP_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    
    // Design tokens
    private Color azulProfundo = new Color(15, 23, 42);
    private Color azulPrimario = new Color(59, 130, 246);
    private Color azulHover = new Color(37, 99, 235);
    private Color azulSuave = new Color(239, 246, 255);
    private Color blanco = new Color(255, 255, 255);
    private Color blancoGris = new Color(248, 250, 252);
    private Color grisBorde = new Color(226, 232, 240);
    private Color grisClaro = new Color(241, 245, 249);
    private Color grisTexto = new Color(107, 114, 128);
    private Color verdeExito = new Color(16, 185, 129);
    private Color verdeSuave = new Color(240, 253, 244);
    private Color rojoStock = new Color(239, 68, 68);
    private Color rojoSuave = new Color(254, 242, 242);
    private Color amarilloStock = new Color(245, 158, 11);
    private Color amarilloSuave = new Color(254, 252, 232);

    public formInventario(formVentas parent) {
        this.parent = parent;
        this.carrito = new HashMap<>();
        initComponents();
        initTable();
        cargarInventario();
    }

    private String formatCOP(double value) {
        return COP_FORMAT.format(value).replace("COP", "").trim();
    }
    
    private void initTable() {
        tableModel = new javax.swing.table.DefaultTableModel(
            new Object[]{"ID", "Nombre", "Descripci\u00F3n", "Lote", "Stock", "Precio", "Estado"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblInventario.setModel(tableModel);
        tblInventario.setRowHeight(46);
        tblInventario.setFont(new Font("Segoe UI", 0, 13));
        tblInventario.getTableHeader().setFont(new Font("Segoe UI Semibold", 1, 11));
        tblInventario.getTableHeader().setBackground(blancoGris);
        tblInventario.getTableHeader().setForeground(grisTexto);
        tblInventario.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tblInventario.setSelectionBackground(azulSuave);
        tblInventario.setGridColor(new Color(241, 245, 249));
        tblInventario.setShowVerticalLines(false);
        tblInventario.setShowHorizontalLines(true);
        tblInventario.setBackground(blanco);
        tblInventario.setIntercellSpacing(new Dimension(0, 0));
        
        tblInventario.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblInventario.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblInventario.getColumnModel().getColumn(2).setPreferredWidth(220);
        tblInventario.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblInventario.getColumnModel().getColumn(4).setPreferredWidth(70);
        tblInventario.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblInventario.getColumnModel().getColumn(6).setPreferredWidth(110);
        
        // Unified cell renderer for ALL columns
        UniversalRenderer universalRenderer = new UniversalRenderer();
        for (int i = 0; i < tblInventario.getColumnCount(); i++) {
            tblInventario.getColumnModel().getColumn(i).setCellRenderer(universalRenderer);
        }
        
        // Special alignment overrides
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                styleLabel(label, isSelected, row, column);
                return label;
            }
        };
        tblInventario.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tblInventario.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblInventario.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Stock column with color
        DefaultTableCellRenderer stockRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI Semibold", 1, 14));
                styleLabel(label, isSelected, row, column);
                
                int stock = Integer.parseInt(value.toString());
                if (stock <= 10) {
                    label.setForeground(rojoStock);
                } else if (stock <= 50) {
                    label.setForeground(amarilloStock);
                } else {
                    label.setForeground(verdeExito);
                }
                return label;
            }
        };
        tblInventario.getColumnModel().getColumn(4).setCellRenderer(stockRenderer);
        
        // Price column with COP format
        DefaultTableCellRenderer priceRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setText(formatCOP(Double.parseDouble(value.toString())));
                label.setHorizontalAlignment(SwingConstants.RIGHT);
                label.setFont(new Font("Segoe UI Semibold", 0, 13));
                label.setForeground(azulProfundo);
                styleLabel(label, isSelected, row, column);
                return label;
            }
        };
        tblInventario.getColumnModel().getColumn(5).setCellRenderer(priceRenderer);
        
        // Status badge column
        StatusCellRenderer statusRenderer = new StatusCellRenderer();
        tblInventario.getColumnModel().getColumn(6).setCellRenderer(statusRenderer);
    }
    
    private void styleLabel(JLabel label, boolean isSelected, int row, int column) {
        if (isSelected) {
            label.setBackground(azulSuave);
            label.setForeground(azulProfundo);
        } else {
            if (row % 2 == 1) {
                label.setBackground(grisClaro);
            } else {
                label.setBackground(blanco);
            }
            label.setForeground(azulProfundo);
        }
        label.setOpaque(true);
    }
    
    class UniversalRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            styleLabel(label, isSelected, row, column);
            return label;
        }
    }
    
    private void cargarInventario() {
        inventarioData = new ArrayList<>();
        MedicamentoDAO dao = new MedicamentoDAO();
        List<Medicamento> medicamentos = dao.listar("");
        
        for (Medicamento med : medicamentos) {
            Map<String, Object> prod = new HashMap<>();
            prod.put("id", med.getCode());
            prod.put("nombre", med.getName());
            prod.put("descripcion", med.getDescription());
            prod.put("lote", "");
            prod.put("stock", med.getStock());
            prod.put("precio", med.getPrice());
            inventarioData.add(prod);
        }
        
        actualizarTabla();
    }
    
    private void actualizarTabla() {
        tableModel.setRowCount(0);
        String filtro = txtBuscar.getText().toLowerCase().trim();
        if (filtro.contains("buscar")) filtro = "";
        
        for (Map<String, Object> prod : inventarioData) {
            String nombre = prod.get("nombre").toString().toLowerCase();
            String descripcion = prod.get("descripcion").toString().toLowerCase();
            
            if (filtro.isEmpty() || nombre.contains(filtro) || descripcion.contains(filtro)) {
                int stock = (Integer) prod.get("stock");
                String estado = stock > 50 ? "Disponible" : (stock > 10 ? "Limitado" : "Cr\u00EDtico");
                
                Object[] row = new Object[]{
                    prod.get("id"),
                    prod.get("nombre"),
                    prod.get("descripcion"),
                    prod.get("lote"),
                    stock,
                    prod.get("precio"),
                    estado
                };
                tableModel.addRow(row);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelPrincipal = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        panelTituloWrap = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelSearch = new javax.swing.JPanel();
        txtBuscar = new javax.swing.JTextField();
        panelTablaWrapper = new javax.swing.JPanel();
        panelTabla = new javax.swing.JScrollPane();
        tblInventario = new javax.swing.JTable();
        panelInferior = new javax.swing.JPanel();
        panelCarritoInfo = new javax.swing.JPanel();
        lblCarritoIcon = new javax.swing.JLabel();
        lblCarritoCount = new javax.swing.JLabel();
        btnIrACarrito = new javax.swing.JButton();

        setBackground(blancoGris);
        setLayout(new BorderLayout());

        panelPrincipal.setBackground(blancoGris);
        panelPrincipal.setBorder(new EmptyBorder(28, 28, 28, 28));
        panelPrincipal.setLayout(new BorderLayout(0, 20));

        // ===== HEADER =====
        panelHeader.setBackground(blancoGris);
        panelHeader.setLayout(new BorderLayout(16, 0));

        panelTituloWrap.setBackground(blancoGris);
        panelTituloWrap.setLayout(new java.awt.GridLayout(2, 1, 0, 6));

        lblTitulo.setFont(new Font("Segoe UI Semibold", 1, 24));
        lblTitulo.setForeground(azulProfundo);
        lblTitulo.setText("Inventario de Productos");
        panelTituloWrap.add(lblTitulo);

        lblSubtitulo.setFont(new Font("Segoe UI", 0, 13));
        lblSubtitulo.setForeground(grisTexto);
        lblSubtitulo.setText("Haz clic en un producto para agregarlo al carrito");
        panelTituloWrap.add(lblSubtitulo);

        panelHeader.add(panelTituloWrap, BorderLayout.CENTER);

        // Search bar
        panelSearch.setBackground(blanco);
        panelSearch.setBorder(new RoundedBorder(grisBorde, 1, 10));
        panelSearch.setLayout(new BorderLayout());
        panelSearch.setPreferredSize(new Dimension(320, 40));

        JLabel lblSearchIcon = new JLabel("  \uD83D\uDD0D");
        lblSearchIcon.setFont(new Font("Segoe UI Emoji", 0, 14));
        lblSearchIcon.setHorizontalAlignment(SwingConstants.CENTER);
        panelSearch.add(lblSearchIcon, BorderLayout.WEST);

        txtBuscar.setFont(new Font("Segoe UI", 0, 13));
        txtBuscar.setForeground(grisTexto);
        txtBuscar.setText("Buscar producto...");
        txtBuscar.setBorder(null);
        txtBuscar.setBackground(blanco);
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent evt) {
                if (txtBuscar.getText().equals("Buscar producto...")) {
                    txtBuscar.setText("");
                    txtBuscar.setForeground(azulProfundo);
                }
            }
            @Override
            public void focusLost(FocusEvent evt) {
                if (txtBuscar.getText().isEmpty()) {
                    txtBuscar.setText("Buscar producto...");
                    txtBuscar.setForeground(grisTexto);
                }
            }
        });
        txtBuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent evt) {
                actualizarTabla();
            }
        });
        panelSearch.add(txtBuscar, BorderLayout.CENTER);

        panelHeader.add(panelSearch, BorderLayout.EAST);

        panelPrincipal.add(panelHeader, BorderLayout.PAGE_START);

        // ===== TABLE =====
        panelTablaWrapper.setBackground(blanco);
        panelTablaWrapper.setBorder(new RoundedBorder(grisBorde, 1, 12));
        panelTablaWrapper.setLayout(new BorderLayout());
        
        panelTabla.setBackground(blanco);
        panelTabla.setBorder(null);
        panelTabla.getViewport().setBackground(blanco);

        tblInventario.setFont(new Font("Segoe UI", 0, 13));
        tblInventario.setSelectionBackground(azulSuave);
        tblInventario.setRowHeight(46);
        tblInventario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblInventario.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    agregarProductoSeleccionado(row);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                tblInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                tblInventario.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        panelTabla.setViewportView(tblInventario);

        panelTablaWrapper.add(panelTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelTablaWrapper, BorderLayout.CENTER);

        // ===== BOTTOM BAR =====
        panelInferior.setBackground(blanco);
        panelInferior.setBorder(new RoundedBorder(grisBorde, 1, 12));
        panelInferior.setLayout(new BorderLayout(16, 0));
        panelInferior.setPreferredSize(new Dimension(0, 56));

        panelCarritoInfo.setBackground(blanco);
        panelCarritoInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        lblCarritoIcon.setFont(new Font("Segoe UI Emoji", 0, 22));
        lblCarritoIcon.setText("\uD83D\uDED2");
        panelCarritoInfo.add(lblCarritoIcon);

        lblCarritoCount.setFont(new Font("Segoe UI Semibold", 0, 13));
        lblCarritoCount.setForeground(azulProfundo);
        lblCarritoCount.setText("0 productos en el carrito");
        panelCarritoInfo.add(lblCarritoCount);

        panelInferior.add(panelCarritoInfo, BorderLayout.CENTER);

        btnIrACarrito = new ModernButton("Ir al Carrito  \u2192");
        btnIrACarrito.setPreferredSize(new Dimension(160, 38));
        btnIrACarrito.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                parent.cambiarVista("ventas");
            }
        });
        panelInferior.add(btnIrACarrito, BorderLayout.EAST);

        panelPrincipal.add(panelInferior, BorderLayout.PAGE_END);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    private void agregarProductoSeleccionado(int row) {
        try {
            String id = tableModel.getValueAt(row, 0).toString();
            String nombre = tableModel.getValueAt(row, 1).toString();
            String lote = tableModel.getValueAt(row, 3).toString();
            int stock = Integer.parseInt(tableModel.getValueAt(row, 4).toString());
            double precio = Double.parseDouble(tableModel.getValueAt(row, 5).toString());
            
            String input = JOptionPane.showInputDialog(
                this,
                "Producto: " + nombre + "\nStock disponible: " + stock + "\nPrecio: " + formatCOP(precio) + "\n\nIngrese cantidad:",
                "1"
            );
            
            if (input == null || input.trim().isEmpty()) return;
            
            int cantidad = Integer.parseInt(input.trim());
            
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                    "La cantidad debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cantidad > stock) {
                JOptionPane.showMessageDialog(this,
                    "Stock insuficiente. Disponible: " + stock, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Map<String, Object> item = carrito.getOrDefault(id, null);
            if (item != null) {
                int actual = (Integer) item.get("cantidad");
                item.put("cantidad", actual + cantidad);
            } else {
                Map<String, Object> nuevo = new HashMap<>();
                nuevo.put("id", id);
                nuevo.put("nombre", nombre);
                nuevo.put("lote", lote);
                nuevo.put("precio", precio);
                nuevo.put("stock", stock);
                nuevo.put("cantidad", cantidad);
                carrito.put(id, nuevo);
            }
            actualizarCarritoInfo();
            
            parent.agregarProducto(id, nombre, lote, precio, cantidad);
            
            JOptionPane.showMessageDialog(this,
                cantidad + "x " + nombre + " agregado al carrito",
                "\u2705 \u00C9xito", JOptionPane.INFORMATION_MESSAGE);
                
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Ingrese una cantidad v\u00E1lida", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarCarritoInfo() {
        int total = 0;
        for (Map<String, Object> item : carrito.values()) {
            total += (Integer) item.get("cantidad");
        }
        lblCarritoCount.setText(total + " producto" + (total != 1 ? "s" : "") + " en el carrito");
    }

    // Variables declaration
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblCarritoCount;
    private javax.swing.JLabel lblCarritoIcon;
    private javax.swing.JButton btnIrACarrito;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JScrollPane panelTabla;
    private javax.swing.JPanel panelSearch;
    private javax.swing.JPanel panelTablaWrapper;
    private javax.swing.JPanel panelInferior;
    private javax.swing.JPanel panelCarritoInfo;
    private javax.swing.JPanel panelTituloWrap;
    private javax.swing.JTable tblInventario;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration
    
    // Rounded Border
    class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color;
        private int thickness;
        private int radius;
        
        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new java.awt.BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        
        @Override
        public java.awt.Insets getBorderInsets(Component c) {
            return new java.awt.Insets(thickness, thickness, thickness, thickness);
        }
    }
    
    // Modern Button
    class ModernButton extends JButton {
        private Color bgColor = azulPrimario;
        private Color hoverColor = azulHover;
        private boolean isHover = false;
        
        public ModernButton(String text) {
            super(text);
            setFont(new Font("Segoe UI Semibold", 1, 13));
            setForeground(blanco);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int radius = 10;
            g2.setColor(isHover ? hoverColor : bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }
    
    // Status Cell Renderer
    class StatusCellRenderer extends JLabel implements TableCellRenderer {
        public StatusCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI Semibold", 1, 11));
            setBorder(new EmptyBorder(4, 12, 4, 12));
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            
            String estado = value.toString();
            setText(estado);
            
            Color bg;
            Color fg;
            switch(estado) {
                case "Disponible": bg = verdeSuave; fg = verdeExito; break;
                case "Limitado": bg = amarilloSuave; fg = amarilloStock; break;
                case "Cr\u00EDtico": bg = rojoSuave; fg = rojoStock; break;
                default: bg = grisClaro; fg = grisTexto;
            }
            
            if (isSelected) {
                setBackground(azulSuave);
            } else {
                setBackground(bg);
            }
            setForeground(fg);
            
            return this;
        }
    }
}
