package presentacion.ventanas;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import data.DetalleVentaDAO;
import data.FacturaDAO;
import data.MedicamentoDAO;
import inventario.Factura;
import roles.SesionUsuario;

/**
 * formRegistrarVenta: clase del proyecto HealthPharmacy.
 */
public class formRegistrarVenta extends javax.swing.JPanel {
    
    private formVentas parent;
    private CardLayout cardLayout;
    private int pasoActual = 1;
    List<Map<String, Object>> carritoVentas;
    private double subtotal;
    private double impuesto;
    private double total;
    
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
    private Color verdeHover = new Color(5, 150, 105);
    private Color verdeSuave = new Color(240, 253, 244);
    private Color rojoError = new Color(239, 68, 68);
    private Color rojoHover = new Color(220, 38, 38);
    private Color sombraColor = new Color(0, 0, 0, 12);

    public formRegistrarVenta(formVentas parent) {
        this.parent = parent;
        this.carritoVentas = new ArrayList<>();
        initComponents();
        initPasos();
        actualizarCarrito();
    }

    // Metodo formatCOP: logica de interfaz asociada a este formulario/panel.
    private String formatCOP(double value) {
        return COP_FORMAT.format(value).replace("COP", "").trim();
    }
    
    // Metodo initPasos: logica de interfaz asociada a este formulario/panel.
    private void initPasos() {
        cardLayout = (CardLayout) panelContenidoPasos.getLayout();
        actualizarBarraProgreso();
    }
    
    // Metodo actualizarBarraProgreso: logica de interfaz asociada a este formulario/panel.
    private void actualizarBarraProgreso() {
        lblPaso1Num.setActivo(pasoActual >= 1);
        lblPaso1Num.setCompletado(false);
        lblPaso1Text.setFont(new Font("Segoe UI Semibold", pasoActual >= 1 ? 1 : 0, 13));
        lblPaso1Text.setForeground(pasoActual >= 1 ? azulProfundo : grisTexto);
        
        lblPaso2Num.setActivo(pasoActual >= 2);
        lblPaso2Num.setCompletado(pasoActual >= 2);
        lblPaso2Text.setFont(new Font("Segoe UI Semibold", pasoActual >= 2 ? 1 : 0, 13));
        lblPaso2Text.setForeground(pasoActual >= 2 ? azulProfundo : grisTexto);
        
        lblLinea1.setActivo(pasoActual >= 2);
        
        int progreso = pasoActual == 1 ? 50 : 100;
        barraProgreso.setValue(progreso);
        barraProgreso.setForeground(azulPrimario);
        
        if (pasoActual == 1) {
            cardLayout.show(panelContenidoPasos, "paso1");
        } else {
            cardLayout.show(panelContenidoPasos, "paso2");
            actualizarFactura();
        }
    }
    
    void actualizarCarrito() {
        DefaultTableModel model = (DefaultTableModel) tblCarrito.getModel();
        model.setRowCount(0);
        
        subtotal = 0;
        
        for (Map<String, Object> item : carritoVentas) {
            double precio = (Double) item.get("precio");
            int cantidad = (Integer) item.get("cantidad");
            double subTotalItem = precio * cantidad;
            subtotal += subTotalItem;
            
            model.addRow(new Object[]{
                item.get("nombre"),
                item.get("lote"),
                cantidad,
                formatCOP(precio),
                formatCOP(subTotalItem),
                ""
            });
        }
        
        impuesto = subtotal * 0.19;
        total = subtotal + impuesto;
        
        lblSubtotal.setText(formatCOP(subtotal));
        lblImpuesto.setText(formatCOP(impuesto));
        lblTotalPaso1.setText(formatCOP(total));
    }
    
    // Metodo actualizarFactura: logica de interfaz asociada a este formulario/panel.
    private void actualizarFactura() {
        panelFactura.removeAll();
        panelFactura.setLayout(new BorderLayout(0, 0));
        
        // Header
        JPanel headerCard = new JPanel(new BorderLayout());
        headerCard.setBackground(blanco);
        headerCard.setBorder(new EmptyBorder(24, 28, 20, 28));
        
        JPanel headerLeft = new JPanel(new java.awt.GridLayout(2, 1, 0, 4));
        headerLeft.setOpaque(false);
        
        JLabel lblTituloFactura = new JLabel("Factura de Venta");
        lblTituloFactura.setFont(new Font("Segoe UI Semibold", 1, 20));
        lblTituloFactura.setForeground(azulProfundo);
        
        JLabel lblFecha = new JLabel(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        lblFecha.setFont(new Font("Segoe UI", 0, 13));
        lblFecha.setForeground(grisTexto);
        
        headerLeft.add(lblTituloFactura);
        headerLeft.add(lblFecha);
        headerCard.add(headerLeft, BorderLayout.CENTER);
        
        JLabel lblNumero = new JLabel("#FV-" + System.currentTimeMillis() % 10000);
        lblNumero.setFont(new Font("Segoe UI Semibold", 1, 13));
        lblNumero.setForeground(azulPrimario);
        lblNumero.setHorizontalAlignment(SwingConstants.RIGHT);
        headerCard.add(lblNumero, BorderLayout.EAST);
        
        panelFactura.add(headerCard, BorderLayout.PAGE_START);
        
        // Separator
        JPanel sep1 = new JPanel();
        sep1.setPreferredSize(new Dimension(0, 1));
        sep1.setBackground(grisBorde);
        panelFactura.add(sep1, BorderLayout.CENTER);
        
        // Items list
        JPanel itemsPanel = new JPanel();
        itemsPanel.setOpaque(false);
        itemsPanel.setBorder(new EmptyBorder(16, 28, 16, 28));
        itemsPanel.setLayout(new java.awt.GridLayout(0, 1, 10, 0));
        
        for (Map<String, Object> item : carritoVentas) {
            String nombre = item.get("nombre").toString();
            int cantidad = (Integer) item.get("cantidad");
            double precio = (Double) item.get("precio");
            double subTotal = precio * cantidad;
            
            JPanel itemRow = new JPanel(new BorderLayout(12, 0));
            itemRow.setBackground(blancoGris);
            itemRow.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(226, 232, 240), 1, 8),
                new EmptyBorder(12, 16, 12, 16)
            ));
            
            JPanel qtyBadge = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            qtyBadge.setOpaque(false);
            qtyBadge.setPreferredSize(new Dimension(36, 0));
            JLabel lblQty = new JLabel("x" + cantidad);
            lblQty.setFont(new Font("Segoe UI Semibold", 1, 12));
            lblQty.setForeground(azulPrimario);
            qtyBadge.add(lblQty);
            
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Segoe UI", 0, 14));
            lblNombre.setForeground(azulProfundo);
            
            JLabel lblPrecio = new JLabel(formatCOP(subTotal));
            lblPrecio.setFont(new Font("Segoe UI Semibold", 1, 15));
            lblPrecio.setForeground(azulProfundo);
            lblPrecio.setHorizontalAlignment(SwingConstants.RIGHT);
            
            itemRow.add(qtyBadge, BorderLayout.WEST);
            itemRow.add(lblNombre, BorderLayout.CENTER);
            itemRow.add(lblPrecio, BorderLayout.EAST);
            
            itemsPanel.add(itemRow);
        }
        
        JScrollPane scroll = new JScrollPane(itemsPanel);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(100, 240));
        scroll.getViewport().setBackground(blanco);
        panelFactura.add(scroll, BorderLayout.CENTER);
        
        // Totales card
        JPanel totalesCard = new JPanel(new BorderLayout());
        totalesCard.setBackground(blancoGris);
        totalesCard.setBorder(new EmptyBorder(16, 28, 24, 28));
        
        JPanel sep2 = new JPanel();
        sep2.setPreferredSize(new Dimension(0, 1));
        sep2.setBackground(grisBorde);
        totalesCard.add(sep2, BorderLayout.PAGE_START);
        
        JPanel totalesGrid = new JPanel(new java.awt.GridLayout(3, 2, 0, 12));
        totalesGrid.setOpaque(false);
        totalesGrid.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel lblST = new JLabel("Subtotal");
        lblST.setFont(new Font("Segoe UI", 0, 13));
        lblST.setForeground(grisTexto);
        
        JLabel lblSTV = new JLabel(formatCOP(subtotal));
        lblSTV.setFont(new Font("Segoe UI Semibold", 0, 14));
        lblSTV.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSTV.setForeground(azulProfundo);
        
        JLabel lblIT = new JLabel("Impuesto (19%)");
        lblIT.setFont(new Font("Segoe UI", 0, 13));
        lblIT.setForeground(grisTexto);
        
        JLabel lblITV = new JLabel(formatCOP(impuesto));
        lblITV.setFont(new Font("Segoe UI Semibold", 0, 14));
        lblITV.setHorizontalAlignment(SwingConstants.RIGHT);
        lblITV.setForeground(azulProfundo);
        
        JLabel lblTT = new JLabel("Total a Pagar");
        lblTT.setFont(new Font("Segoe UI Semibold", 1, 15));
        lblTT.setForeground(azulProfundo);
        
        JLabel lblTTV = new JLabel(formatCOP(total));
        lblTTV.setFont(new Font("Segoe UI Semibold", 1, 24));
        lblTTV.setForeground(verdeExito);
        lblTTV.setHorizontalAlignment(SwingConstants.RIGHT);
        
        totalesGrid.add(lblST);
        totalesGrid.add(lblSTV);
        totalesGrid.add(lblIT);
        totalesGrid.add(lblITV);
        totalesGrid.add(lblTT);
        totalesGrid.add(lblTTV);
        
        totalesCard.add(totalesGrid, BorderLayout.CENTER);
        panelFactura.add(totalesCard, BorderLayout.PAGE_END);
        
        panelFactura.revalidate();
        panelFactura.repaint();
    }
    
    // Metodo siguientePaso: logica de interfaz asociada a este formulario/panel.
    private void siguientePaso() {
        if (carritoVentas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "El carrito est\u00E1 vac\u00EDo. Agregue productos antes de continuar.",
                "Carrito Vac\u00EDo",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        pasoActual = 2;
        actualizarBarraProgreso();
    }
    
    // Metodo volverPaso: logica de interfaz asociada a este formulario/panel.
    private void volverPaso() {
        pasoActual = 1;
        actualizarBarraProgreso();
    }
    
    // Metodo finalizarCompra: logica de interfaz asociada a este formulario/panel.
    private void finalizarCompra() {
        if (carritoVentas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "No hay productos para procesar.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "\u00BFConfirmar y finalizar la venta?\n\nTotal a pagar: " + formatCOP(total),
            "Confirmar Venta",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            FacturaDAO facturaDAO = new FacturaDAO();
            MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
            DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
            
            String idFactura = "FAC" + System.currentTimeMillis() % 100000;
            java.sql.Timestamp ahora = new java.sql.Timestamp(System.currentTimeMillis());
            
            Factura factura = new Factura();
            factura.setId(idFactura);
            factura.setFecha(ahora);
            factura.setPrecioTotal(total);
            factura.setSubTotal(subtotal);
            factura.setIva(impuesto);
            factura.setEstado("pagada");
            // Guardar el cajero autenticado para enlazar correctamente venta -> usuario.
            factura.setVendedor(SesionUsuario.getInstancia().getIdUsuario());
            factura.setIdDetalleVenta("DET" + System.currentTimeMillis() % 100000);
            
            boolean facturaGuardada = facturaDAO.insertar(factura);
            
            if (facturaGuardada) {
                boolean detallesGuardados = true;
                for (int i = 0; i < carritoVentas.size(); i++) {
                    Map<String, Object> item = carritoVentas.get(i);
                    String idMed = item.get("id") != null ? item.get("id").toString() : "";
                    int cantidad = (Integer) item.get("cantidad");
                    String idDetalle = "DV" + System.currentTimeMillis() % 10000 + i;
                    
                    if (!detalleDAO.insertar(idDetalle, idFactura, idMed, cantidad)) {
                        detallesGuardados = false;
                    }
                    
                    if (!medicamentoDAO.reducirStock(idMed, cantidad)) {
                        JOptionPane.showMessageDialog(this,
                            "Advertencia: No se pudo reducir stock de " + item.get("nombre"),
                            "Alerta", JOptionPane.WARNING_MESSAGE);
                    }
                }
                
                if (detallesGuardados) {
                    JOptionPane.showMessageDialog(this,
                        "Venta realizada con \u00E9xito!\n" +
                        "Total cobrado: " + formatCOP(total),
                        "\u2705 Venta Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    carritoVentas.clear();
                    actualizarCarrito();
                    pasoActual = 1;
                    actualizarBarraProgreso();
                    parent.cambiarVista("dashboard");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Factura guardada pero algunos detalles fallaron.",
                        "Error Parcial", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Error al guardar la venta. Intente nuevamente.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Metodo agregarProducto: logica de interfaz asociada a este formulario/panel.
    public void agregarProducto(String id, String nombre, String lote, double precio, int cantidad) {
        boolean existe = false;
        for (Map<String, Object> item : carritoVentas) {
            if (item.get("id").equals(id) && item.get("lote").equals(lote)) {
                int cantidadActual = (Integer) item.get("cantidad");
                item.put("cantidad", cantidadActual + cantidad);
                existe = true;
                break;
            }
        }
        
        if (!existe) {
            Map<String, Object> nuevoItem = new HashMap<>();
            nuevoItem.put("id", id);
            nuevoItem.put("nombre", nombre);
            nuevoItem.put("lote", lote);
            nuevoItem.put("precio", precio);
            nuevoItem.put("cantidad", cantidad);
            carritoVentas.add(nuevoItem);
        }
        
        actualizarCarrito();
    }

    @SuppressWarnings("unchecked")
    // Metodo initComponents: logica de interfaz asociada a este formulario/panel.
    private void initComponents() {
        panelPrincipal = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        panelPasos = new javax.swing.JPanel();
        lblPaso1Num = new StepCircle(1);
        lblPaso1Text = new javax.swing.JLabel();
        lblLinea1 = new StepLine();
        lblPaso2Num = new StepCircle(2);
        lblPaso2Text = new javax.swing.JLabel();
        barraProgreso = new javax.swing.JProgressBar();
        panelContenidoPasos = new javax.swing.JPanel();
        panelPaso1 = new javax.swing.JPanel();
        panelTablaCarrito = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        panelTotales = new javax.swing.JPanel();
        panelTotalesIzquierda = new javax.swing.JPanel();
        lblSubtotalLabel = new javax.swing.JLabel();
        lblImpuestoLabel = new javax.swing.JLabel();
        lblTotalLabel = new javax.swing.JLabel();
        panelTotalesDerecha = new javax.swing.JPanel();
        lblSubtotal = new javax.swing.JLabel();
        lblImpuesto = new javax.swing.JLabel();
        lblTotalPaso1 = new javax.swing.JLabel();
        panelBotonesPaso1 = new javax.swing.JPanel();
        btnContinuar = new javax.swing.JButton();
        panelPaso2 = new javax.swing.JPanel();
        panelFactura = new javax.swing.JPanel();
        panelBotonesPaso2 = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        btnFinalizar = new javax.swing.JButton();

        setBackground(blancoGris);
        setLayout(new BorderLayout());

        panelPrincipal.setBackground(blancoGris);
        panelPrincipal.setBorder(new EmptyBorder(28, 28, 28, 28));
        panelPrincipal.setLayout(new BorderLayout(0, 20));

        // ===== HEADER =====
        panelHeader.setBackground(blancoGris);
        panelHeader.setLayout(new BorderLayout(0, 0));
        panelHeader.setBorder(new EmptyBorder(0, 0, 16, 0));

        JPanel titleWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleWrap.setBackground(blancoGris);
        
        lblTitulo.setFont(new Font("Segoe UI Semibold", 1, 24));
        lblTitulo.setForeground(azulProfundo);
        lblTitulo.setText("Registrar Venta");
        titleWrap.add(lblTitulo);
        panelHeader.add(titleWrap, BorderLayout.PAGE_START);

        // Steps indicator
        panelPasos.setBackground(blanco);
        panelPasos.setBorder(new RoundedBorder(grisBorde, 1, 12));
        panelPasos.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 12));
        panelPasos.setPreferredSize(new Dimension(0, 52));

        lblPaso1Text.setFont(new Font("Segoe UI Semibold", 1, 13));
        lblPaso1Text.setForeground(azulProfundo);
        lblPaso1Text.setText("Carrito de Productos");
        panelPasos.add(lblPaso1Num);
        panelPasos.add(lblPaso1Text);
        
        panelPasos.add(lblLinea1);
        
        lblPaso2Text.setFont(new Font("Segoe UI", 0, 13));
        lblPaso2Text.setForeground(grisTexto);
        lblPaso2Text.setText("Confirmaci\u00F3n y Pago");
        panelPasos.add(lblPaso2Num);
        panelPasos.add(lblPaso2Text);

        panelHeader.add(panelPasos, BorderLayout.CENTER);

        // Progress bar
        JPanel progressWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
        progressWrap.setBackground(blancoGris);
        
        barraProgreso.setForeground(azulPrimario);
        barraProgreso.setStringPainted(false);
        barraProgreso.setPreferredSize(new Dimension(500, 6));
        barraProgreso.setValue(50);
        progressWrap.add(barraProgreso);
        
        panelHeader.add(progressWrap, BorderLayout.PAGE_END);

        panelPrincipal.add(panelHeader, BorderLayout.PAGE_START);

        // ===== CONTENT =====
        panelContenidoPasos.setLayout(new CardLayout());

        // ===== STEP 1: Cart =====
        panelPaso1.setBackground(blancoGris);
        panelPaso1.setLayout(new BorderLayout(0, 16));

        // Table with rounded border
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(blanco);
        tableWrapper.setBorder(new RoundedBorder(grisBorde, 1, 12));

        panelTablaCarrito.setBackground(blanco);
        panelTablaCarrito.setBorder(null);
        panelTablaCarrito.getViewport().setBackground(blanco);

        tblCarrito.setFont(new Font("Segoe UI", 0, 13));
        tblCarrito.setModel(new DefaultTableModel(
            new Object[][]{},
            new String[]{"Producto", "Lote", "Cant.", "Precio", "Subtotal", "Acciones"}
        ) {
            boolean[] canEdit = new boolean[]{false, false, false, false, false, false};
            // Metodo isCellEditable: logica de interfaz asociada a este formulario/panel.
            public boolean isCellEditable(int row, int column) {
                return canEdit[column];
            }
        });
        tblCarrito.setRowHeight(48);
        tblCarrito.getTableHeader().setFont(new Font("Segoe UI Semibold", 1, 11));
        tblCarrito.getTableHeader().setBackground(blancoGris);
        tblCarrito.getTableHeader().setForeground(grisTexto);
        tblCarrito.getTableHeader().setPreferredSize(new Dimension(0, 36));
        tblCarrito.setSelectionBackground(azulSuave);
        tblCarrito.setGridColor(new Color(241, 245, 249));
        tblCarrito.setShowVerticalLines(false);
        tblCarrito.setShowHorizontalLines(true);
        tblCarrito.setBackground(blanco);
        tblCarrito.setIntercellSpacing(new Dimension(0, 0));
        
        tblCarrito.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblCarrito.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblCarrito.getColumnModel().getColumn(2).setPreferredWidth(60);
        tblCarrito.getColumnModel().getColumn(3).setPreferredWidth(90);
        tblCarrito.getColumnModel().getColumn(4).setPreferredWidth(90);
        tblCarrito.getColumnModel().getColumn(5).setPreferredWidth(100);
        
        // Universal renderer for text columns
        DefaultTableCellRenderer textRenderer = new DefaultTableCellRenderer() {
            @Override
            // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    label.setBackground(azulSuave);
                    label.setForeground(azulProfundo);
                } else {
                    label.setBackground(row % 2 == 0 ? blanco : grisClaro);
                    label.setForeground(azulProfundo);
                }
                label.setOpaque(true);
                return label;
            }
        };
        tblCarrito.getColumnModel().getColumn(0).setCellRenderer(textRenderer);
        tblCarrito.getColumnModel().getColumn(1).setCellRenderer(textRenderer);
        
        // Center columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                if (isSelected) {
                    label.setBackground(azulSuave);
                    label.setForeground(azulProfundo);
                } else {
                    label.setBackground(row % 2 == 0 ? blanco : grisClaro);
                    label.setForeground(azulProfundo);
                }
                label.setOpaque(true);
                return label;
            }
        };
        tblCarrito.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tblCarrito.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        tblCarrito.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        
        // Actions column with +/- buttons rendered properly
        ActionCellRenderer actionRenderer = new ActionCellRenderer();
        tblCarrito.getColumnModel().getColumn(5).setCellRenderer(actionRenderer);
        
        // Mouse listener for action buttons
        tblCarrito.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                int col = tblCarrito.columnAtPoint(e.getPoint());
                int row = tblCarrito.rowAtPoint(e.getPoint());
                
                if (col == 5 && row >= 0 && row < carritoVentas.size()) {
                    Rectangle cellRect = tblCarrito.getCellRect(row, col, true);
                    int x = e.getX() - cellRect.x;
                    int cellWidth = cellRect.width;
                    
                    // Two buttons: minus on left, plus on right
                    int btnWidth = 28;
                    int gap = (cellWidth - btnWidth * 2) / 3;
                    int minusStart = gap;
                    int minusEnd = gap + btnWidth;
                    int plusStart = gap * 2 + btnWidth;
                    int plusEnd = plusStart + btnWidth;
                    
                    Map<String, Object> item = carritoVentas.get(row);
                    
                    if (x >= minusStart && x <= minusEnd) {
                        // Minus button
                        int cantidad = (Integer) item.get("cantidad");
                        if (cantidad > 1) {
                            item.put("cantidad", cantidad - 1);
                        } else {
                            int confirm = JOptionPane.showConfirmDialog(formRegistrarVenta.this,
                                "\u00BFEliminar producto del carrito?",
                                "Confirmar",
                                JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                carritoVentas.remove(row);
                            }
                        }
                        actualizarCarrito();
                    } else if (x >= plusStart && x <= plusEnd) {
                        // Plus button
                        int cantidad = (Integer) item.get("cantidad");
                        item.put("cantidad", cantidad + 1);
                        actualizarCarrito();
                    }
                }
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                tblCarrito.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                tblCarrito.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        panelTablaCarrito.setViewportView(tblCarrito);
        tableWrapper.add(panelTablaCarrito, BorderLayout.CENTER);
        panelPaso1.add(tableWrapper, BorderLayout.CENTER);

        // Totales with rounded card
        panelTotales.setBackground(blanco);
        panelTotales.setBorder(new RoundedBorder(grisBorde, 1, 12));
        panelTotales.setLayout(new BorderLayout(24, 0));

        panelTotalesIzquierda.setBackground(blanco);
        panelTotalesIzquierda.setLayout(new java.awt.GridLayout(3, 1, 10, 10));
        panelTotalesIzquierda.setBorder(new EmptyBorder(24, 28, 24, 0));

        lblSubtotalLabel.setFont(new Font("Segoe UI", 0, 13));
        lblSubtotalLabel.setForeground(grisTexto);
        lblSubtotalLabel.setText("Subtotal");
        panelTotalesIzquierda.add(lblSubtotalLabel);

        lblImpuestoLabel.setFont(new Font("Segoe UI", 0, 13));
        lblImpuestoLabel.setForeground(grisTexto);
        lblImpuestoLabel.setText("Impuesto (19%)");
        panelTotalesIzquierda.add(lblImpuestoLabel);

        lblTotalLabel.setFont(new Font("Segoe UI Semibold", 1, 16));
        lblTotalLabel.setForeground(azulProfundo);
        lblTotalLabel.setText("TOTAL");
        panelTotalesIzquierda.add(lblTotalLabel);

        panelTotales.add(panelTotalesIzquierda, BorderLayout.CENTER);

        panelTotalesDerecha.setBackground(blanco);
        panelTotalesDerecha.setLayout(new java.awt.GridLayout(3, 1, 10, 10));
        panelTotalesDerecha.setBorder(new EmptyBorder(24, 0, 24, 28));

        lblSubtotal.setFont(new Font("Segoe UI Semibold", 0, 14));
        lblSubtotal.setForeground(azulProfundo);
        lblSubtotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblSubtotal.setText(formatCOP(0));
        panelTotalesDerecha.add(lblSubtotal);

        lblImpuesto.setFont(new Font("Segoe UI Semibold", 0, 14));
        lblImpuesto.setForeground(azulProfundo);
        lblImpuesto.setHorizontalAlignment(SwingConstants.RIGHT);
        lblImpuesto.setText(formatCOP(0));
        panelTotalesDerecha.add(lblImpuesto);

        lblTotalPaso1.setFont(new Font("Segoe UI Semibold", 1, 22));
        lblTotalPaso1.setForeground(azulPrimario);
        lblTotalPaso1.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotalPaso1.setText(formatCOP(0));
        panelTotalesDerecha.add(lblTotalPaso1);

        panelTotales.add(panelTotalesDerecha, BorderLayout.EAST);

        panelPaso1.add(panelTotales, BorderLayout.PAGE_END);

        // Buttons paso 1
        panelBotonesPaso1.setBackground(blancoGris);
        panelBotonesPaso1.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        btnContinuar = createPrimaryButton("Continuar  \u2192");
        btnContinuar.addActionListener(e -> siguientePaso());
        panelBotonesPaso1.add(btnContinuar);

        panelPaso1.add(panelBotonesPaso1, BorderLayout.PAGE_START);

        panelContenidoPasos.add(panelPaso1, "paso1");

        // ===== STEP 2: Confirmation =====
        panelPaso2.setBackground(blancoGris);
        panelPaso2.setLayout(new BorderLayout(0, 16));

        panelFactura.setBackground(blanco);
        panelFactura.setBorder(new RoundedBorder(grisBorde, 1, 14));
        panelFactura.setLayout(new BorderLayout());
        panelPaso2.add(panelFactura, BorderLayout.CENTER);

        // Buttons paso 2
        panelBotonesPaso2.setBackground(blancoGris);
        panelBotonesPaso2.setLayout(new FlowLayout(FlowLayout.CENTER, 16, 0));

        btnVolver = createSecondaryButton("\u2190  Volver");
        btnVolver.addActionListener(e -> volverPaso());
        panelBotonesPaso2.add(btnVolver);

        btnFinalizar = createSuccessButton("\u2713  Finalizar Compra");
        btnFinalizar.addActionListener(e -> finalizarCompra());
        panelBotonesPaso2.add(btnFinalizar);

        panelPaso2.add(panelBotonesPaso2, BorderLayout.PAGE_END);

        panelContenidoPasos.add(panelPaso2, "paso2");

        panelPrincipal.add(panelContenidoPasos, BorderLayout.CENTER);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    // Metodo createPrimaryButton: logica de interfaz asociada a este formulario/panel.
    private JButton createPrimaryButton(String text) {
        ModernButton btn = new ModernButton(text, azulPrimario, azulHover);
        btn.setFont(new Font("Segoe UI Semibold", 1, 14));
        btn.setPreferredSize(new Dimension(170, 44));
        return btn;
    }
    
    // Metodo createSecondaryButton: logica de interfaz asociada a este formulario/panel.
    private JButton createSecondaryButton(String text) {
        ModernButton btn = new ModernButton(text, blanco, new Color(241, 245, 249));
        btn.setFont(new Font("Segoe UI Semibold", 1, 14));
        btn.setPreferredSize(new Dimension(150, 44));
        btn.setForeground(azulProfundo);
        return btn;
    }
    
    // Metodo createSuccessButton: logica de interfaz asociada a este formulario/panel.
    private JButton createSuccessButton(String text) {
        ModernButton btn = new ModernButton(text, verdeExito, verdeHover);
        btn.setFont(new Font("Segoe UI Semibold", 1, 14));
        btn.setPreferredSize(new Dimension(200, 44));
        return btn;
    }

    // Variables declaration
    private javax.swing.JProgressBar barraProgreso;
    private javax.swing.JButton btnContinuar;
    private javax.swing.JButton btnFinalizar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JLabel lblImpuesto;
    private javax.swing.JLabel lblImpuestoLabel;
    private StepLine lblLinea1;
    private javax.swing.JLabel lblPaso1Text;
    private javax.swing.JLabel lblPaso2Text;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblSubtotalLabel;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTotalLabel;
    private javax.swing.JLabel lblTotalPaso1;
    private javax.swing.JPanel panelBotonesPaso1;
    private javax.swing.JPanel panelBotonesPaso2;
    private javax.swing.JPanel panelContenidoPasos;
    private javax.swing.JPanel panelFactura;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelPaso1;
    private javax.swing.JPanel panelPaso2;
    private javax.swing.JPanel panelPasos;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JScrollPane panelTablaCarrito;
    private javax.swing.JPanel panelTotales;
    private javax.swing.JPanel panelTotalesDerecha;
    private javax.swing.JPanel panelTotalesIzquierda;
    private javax.swing.JTable tblCarrito;
    private StepCircle lblPaso1Num;
    private StepCircle lblPaso2Num;
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
        // Metodo paintBorder: logica de interfaz asociada a este formulario/panel.
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
    
    // Step Circle Component
    class StepCircle extends JLabel {
        private int numero;
        private boolean activo;
        private boolean completado;
        
        public StepCircle(int numero) {
            this.numero = numero;
            this.activo = false;
            this.completado = false;
            setOpaque(false);
            setPreferredSize(new Dimension(32, 32));
            setHorizontalAlignment(SwingConstants.CENTER);
            setFont(new Font("Segoe UI Semibold", 1, 14));
        }
        
        // Metodo setActivo: logica de interfaz asociada a este formulario/panel.
        public void setActivo(boolean activo) {
            this.activo = activo;
            repaint();
        }
        
        // Metodo setCompletado: logica de interfaz asociada a este formulario/panel.
        public void setCompletado(boolean completado) {
            this.completado = completado;
            repaint();
        }
        
        @Override
        // Metodo paintComponent: logica de interfaz asociada a este formulario/panel.
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int d = 30;
            int x = (getWidth() - d) / 2;
            int y = (getHeight() - d) / 2;
            
            if (completado) {
                g2.setColor(verdeExito);
                g2.fillOval(x, y, d, d);
                g2.setColor(blanco);
                g2.setFont(new Font("Segoe UI", 1, 16));
                FontMetrics fm = g2.getFontMetrics();
                String check = "\u2713";
                g2.drawString(check, x + (d - fm.stringWidth(check)) / 2, y + (d + fm.getAscent() - fm.getDescent()) / 2);
            } else if (activo) {
                g2.setColor(azulPrimario);
                g2.fillOval(x, y, d, d);
                g2.setColor(blanco);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = String.valueOf(numero);
                g2.drawString(text, x + (d - fm.stringWidth(text)) / 2, y + (d + fm.getAscent() - fm.getDescent()) / 2);
            } else {
                g2.setColor(blanco);
                g2.fillOval(x, y, d, d);
                g2.setColor(grisBorde);
                g2.setStroke(new java.awt.BasicStroke(2));
                g2.drawOval(x, y, d, d);
                g2.setColor(grisTexto);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = String.valueOf(numero);
                g2.drawString(text, x + (d - fm.stringWidth(text)) / 2, y + (d + fm.getAscent() - fm.getDescent()) / 2);
            }
            
            g2.dispose();
        }
    }
    
    // Step Line Component
    class StepLine extends JLabel {
        private boolean activo;
        
        public StepLine() {
            this.activo = false;
            setOpaque(false);
            setPreferredSize(new Dimension(48, 3));
        }
        
        // Metodo setActivo: logica de interfaz asociada a este formulario/panel.
        public void setActivo(boolean activo) {
            this.activo = activo;
            repaint();
        }
        
        @Override
        // Metodo paintComponent: logica de interfaz asociada a este formulario/panel.
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(activo ? azulPrimario : grisBorde);
            g2.setStroke(new java.awt.BasicStroke(3, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
            g2.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            g2.dispose();
        }
    }
    
    // Modern Button Component
    class ModernButton extends JButton {
        private Color bgColor;
        private Color hoverColor;
        private boolean isHover = false;
        
        public ModernButton(String text, Color bg, Color hover) {
            super(text);
            this.bgColor = bg;
            this.hoverColor = hover;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
                public void mouseEntered(MouseEvent e) {
                    isHover = true;
                    repaint();
                }
                @Override
                // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
                public void mouseExited(MouseEvent e) {
                    isHover = false;
                    repaint();
                }
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
    
    // Action Cell Renderer - draws +/- buttons properly
    class ActionCellRenderer extends JLabel implements TableCellRenderer {
        private Color minusBg = rojoError;
        private Color minusHover = rojoHover;
        private Color plusBg = verdeExito;
        private Color plusHover = verdeHover;
        private int hoverBtn = -1; // -1 none, 0 minus, 1 plus
        private int hoverRow = -1;
        
        public ActionCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(SwingConstants.CENTER);
        }
        
        @Override
        // Metodo getTableCellRendererComponent: logica de interfaz asociada a este formulario/panel.
        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (isSelected) {
                setBackground(azulSuave);
            } else {
                setBackground(row % 2 == 0 ? blanco : grisClaro);
            }
            setForeground(azulProfundo);
            
            return this;
        }
        
        @Override
        // Metodo paintComponent: logica de interfaz asociada a este formulario/panel.
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int btnSize = 28;
            int radius = 7;
            int cellW = getWidth();
            int cellH = getHeight();
            int gap = (cellW - btnSize * 2) / 3;
            int y = (cellH - btnSize) / 2;
            
            // Minus button
            g2.setColor(minusBg);
            g2.fillRoundRect(gap, y, btnSize, btnSize, radius, radius);
            g2.setColor(blanco);
            g2.setFont(new Font("Segoe UI", 1, 16));
            FontMetrics fm = g2.getFontMetrics();
            String minus = "\u2212";
            g2.drawString(minus, gap + (btnSize - fm.stringWidth(minus)) / 2, y + (btnSize + fm.getAscent() - fm.getDescent()) / 2);
            
            // Plus button
            g2.setColor(plusBg);
            int plusX = gap * 2 + btnSize;
            g2.fillRoundRect(plusX, y, btnSize, btnSize, radius, radius);
            g2.setColor(blanco);
            String plus = "\uFF0B";
            g2.drawString(plus, plusX + (btnSize - fm.stringWidth(plus)) / 2, y + (btnSize + fm.getAscent() - fm.getDescent()) / 2);
            
            g2.dispose();
        }
    }
}

