package presentacion.ventanas;

import data.FacturaDAO;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Panel de reportes básicos para administrador.
 * Usa el modelo de datos actual (factura/detalle_venta) sin cambios de BD.
 */
public class panelAdminReportes extends JPanel {

    private final FacturaDAO facturaDAO;
    private final NumberFormat copFormat;
    private final SimpleDateFormat dateTimeFormat;

    // Paleta tomada del diseño admin.
    private final Color slate900 = new Color(15, 23, 42);
    private final Color slate600 = new Color(71, 85, 105);
    private final Color slate500 = new Color(100, 116, 139);
    private final Color slate300 = new Color(203, 213, 225);
    private final Color slate200 = new Color(226, 232, 240);
    private final Color slate100 = new Color(241, 245, 249);
    private final Color slate50 = new Color(248, 250, 252);
    private final Color white = new Color(255, 255, 255);
    private final Color blue600 = new Color(37, 99, 235);
    private final Color blue700 = new Color(29, 78, 216);

    private final JSpinner spFechaInicio;
    private final JSpinner spFechaFin;
    private final JComboBox<ComboItem> cbVendedor;
    private final JButton btnFiltrar;
    private final JButton btnLimpiar;

    private final JLabel lblTotalVendido;
    private final JLabel lblCantidadFacturas;
    private final JLabel lblTicketPromedio;

    private final DefaultTableModel modeloVentas;
    private final DefaultTableModel modeloProductos;

    public panelAdminReportes() {
        this.facturaDAO = new FacturaDAO();
        this.copFormat = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
        this.dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        setLayout(new BorderLayout());
        setBackground(slate50);
        setBorder(new EmptyBorder(28, 28, 28, 28));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setOpaque(false);

        JLabel lblTitulo = new JLabel("Reportes de Ventas");
        lblTitulo.setFont(new Font("Inter", Font.BOLD, 24));
        lblTitulo.setForeground(slate900);
        panelHeader.add(lblTitulo, BorderLayout.WEST);

        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        panelFiltros.setOpaque(false);

        spFechaInicio = crearSpinnerFecha();
        spFechaFin = crearSpinnerFecha();
        cbVendedor = new JComboBox<>();
        cbVendedor.setPreferredSize(new Dimension(180, 34));
        estilizarInput(cbVendedor);

        btnFiltrar = crearBoton("Aplicar");
        btnLimpiar = crearBotonSecundario("Limpiar");

        panelFiltros.add(crearEtiquetaFiltro("Desde"));
        panelFiltros.add(spFechaInicio);
        panelFiltros.add(crearEtiquetaFiltro("Hasta"));
        panelFiltros.add(spFechaFin);
        panelFiltros.add(crearEtiquetaFiltro("Vendedor"));
        panelFiltros.add(cbVendedor);
        panelFiltros.add(btnFiltrar);
        panelFiltros.add(btnLimpiar);

        panelHeader.add(panelFiltros, BorderLayout.EAST);
        add(panelHeader, BorderLayout.PAGE_START);

        JPanel panelCentro = new JPanel(new BorderLayout(0, 16));
        panelCentro.setOpaque(false);

        JPanel panelTarjetas = new JPanel(new GridLayout(1, 3, 12, 0));
        panelTarjetas.setOpaque(false);

        lblTotalVendido = new JLabel("$0");
        panelTarjetas.add(crearTarjetaResumen("Total Vendido", lblTotalVendido));
        lblCantidadFacturas = new JLabel("0");
        panelTarjetas.add(crearTarjetaResumen("Facturas", lblCantidadFacturas));
        lblTicketPromedio = new JLabel("$0");
        panelTarjetas.add(crearTarjetaResumen("Ticket Promedio", lblTicketPromedio));

        panelCentro.add(panelTarjetas, BorderLayout.PAGE_START);

        modeloVentas = new DefaultTableModel(new Object[]{"Factura", "Fecha", "Vendedor", "Estado", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaVentas = new JTable(modeloVentas);
        estilizarTabla(tablaVentas);

        modeloProductos = new DefaultTableModel(new Object[]{"Producto", "Unidades", "Ingreso"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaProductos = new JTable(modeloProductos);
        estilizarTabla(tablaProductos);

        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 12, 0));
        panelTablas.setOpaque(false);
        panelTablas.add(crearSeccionTabla("Detalle de Ventas", tablaVentas));
        panelTablas.add(crearSeccionTabla("Top Productos", tablaProductos));

        panelCentro.add(panelTablas, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        inicializarEventos();
        inicializarFiltros();
        cargarVendedores();
        cargarReporte();
    }

    // Metodo crearSpinnerFecha: crea un selector simple de fecha para los filtros.
    private JSpinner crearSpinnerFecha() {
        JSpinner spinner = new JSpinner(new SpinnerDateModel());
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        spinner.setPreferredSize(new Dimension(110, 34));
        estilizarInput(spinner);
        return spinner;
    }

    // Metodo crearEtiquetaFiltro: etiqueta compacta para cada control del filtro.
    private JLabel crearEtiquetaFiltro(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Inter", Font.PLAIN, 12));
        label.setForeground(slate600);
        return label;
    }

    // Metodo estilizarInput: aplica borde y tipografía base a inputs Swing.
    private void estilizarInput(JComponent input) {
        input.setFont(new Font("Inter", Font.PLAIN, 12));
        input.setBorder(new RoundedBorder(slate200, 1, 8));
        input.setBackground(white);
        input.setOpaque(true);
    }

    // Metodo crearBoton: botón principal de acción.
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Inter", Font.BOLD, 12));
        btn.setForeground(white);
        btn.setBackground(blue600);
        btn.setBorder(new RoundedBorder(blue600, 1, 8));
        btn.setMargin(new Insets(8, 14, 8, 14));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(blue700);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(blue600);
            }
        });
        return btn;
    }

    // Metodo crearBotonSecundario: botón secundario para resetear filtros.
    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Inter", Font.BOLD, 12));
        btn.setForeground(slate600);
        btn.setBackground(white);
        btn.setBorder(new RoundedBorder(slate200, 1, 8));
        btn.setMargin(new Insets(8, 14, 8, 14));
        return btn;
    }

    // Metodo crearTarjetaResumen: componente reutilizable de KPI.
    private JPanel crearTarjetaResumen(String titulo, JLabel lblValor) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(white);
        card.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(slate200, 1, 12),
                new EmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Inter", Font.PLAIN, 12));
        lblTitulo.setForeground(slate500);
        card.add(lblTitulo, BorderLayout.PAGE_START);

        lblValor.setFont(new Font("Inter", Font.BOLD, 20));
        lblValor.setForeground(slate900);
        card.add(lblValor, BorderLayout.CENTER);
        return card;
    }

    // Metodo crearSeccionTabla: contenedor con título + scroll de tabla.
    private JPanel crearSeccionTabla(String titulo, JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(white);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(slate200, 1, 12),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel lbl = new JLabel(titulo);
        lbl.setFont(new Font("Inter", Font.BOLD, 14));
        lbl.setForeground(slate900);
        panel.add(lbl, BorderLayout.PAGE_START);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    // Metodo estilizarTabla: estilo uniforme de tablas del módulo admin.
    private void estilizarTabla(JTable tabla) {
        tabla.setRowHeight(36);
        tabla.setFont(new Font("Inter", Font.PLAIN, 12));
        tabla.getTableHeader().setFont(new Font("Inter", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(slate100);
        tabla.getTableHeader().setForeground(slate600);
        tabla.setGridColor(slate200);
        tabla.setShowVerticalLines(false);
        tabla.setSelectionBackground(new Color(239, 246, 255));
        tabla.setSelectionForeground(slate900);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setBorder(new EmptyBorder(0, 6, 0, 6));
        tabla.getColumnModel().getColumn(0).setCellRenderer(center);
        if (tabla.getColumnCount() > 1) {
            tabla.getColumnModel().getColumn(1).setCellRenderer(center);
        }
    }

    // Metodo inicializarEventos: conecta acciones de botones con carga de reporte.
    private void inicializarEventos() {
        btnFiltrar.addActionListener((ActionEvent e) -> cargarReporte());
        btnLimpiar.addActionListener((ActionEvent e) -> {
            inicializarFiltros();
            cbVendedor.setSelectedIndex(0);
            cargarReporte();
        });

        // Aplica filtro en tiempo real al cambiar vendedor.
        cbVendedor.addActionListener((ActionEvent e) -> cargarReporte());

        // Aplica filtro en tiempo real al cambiar fechas.
        spFechaInicio.addChangeListener(e -> cargarReporte());
        spFechaFin.addChangeListener(e -> cargarReporte());
    }

    // Metodo inicializarFiltros: define por defecto el rango del mes actual.
    private void inicializarFiltros() {
        Calendar inicio = Calendar.getInstance();
        inicio.set(Calendar.DAY_OF_MONTH, 1);
        inicio.set(Calendar.HOUR_OF_DAY, 0);
        inicio.set(Calendar.MINUTE, 0);
        inicio.set(Calendar.SECOND, 0);
        inicio.set(Calendar.MILLISECOND, 0);

        Calendar fin = Calendar.getInstance();
        fin.set(Calendar.HOUR_OF_DAY, 23);
        fin.set(Calendar.MINUTE, 59);
        fin.set(Calendar.SECOND, 59);
        fin.set(Calendar.MILLISECOND, 999);

        spFechaInicio.setValue(inicio.getTime());
        spFechaFin.setValue(fin.getTime());
    }

    // Metodo cargarVendedores: llena combo con todos los vendedores disponibles.
    private void cargarVendedores() {
        cbVendedor.removeAllItems();
        cbVendedor.addItem(new ComboItem("", "Todos"));

        List<String[]> vendedores = facturaDAO.listarVendedores();
        for (String[] v : vendedores) {
            cbVendedor.addItem(new ComboItem(v[0], v[1]));
        }
    }

    // Metodo cargarReporte: consulta datos y actualiza KPIs + tablas.
    private void cargarReporte() {
        Date desde = (Date) spFechaInicio.getValue();
        Date hasta = (Date) spFechaFin.getValue();
        if (desde.after(hasta)) {
            JOptionPane.showMessageDialog(this, "La fecha inicial no puede ser mayor a la fecha final.");
            return;
        }

        Calendar cIni = Calendar.getInstance();
        cIni.setTime(desde);
        cIni.set(Calendar.HOUR_OF_DAY, 0);
        cIni.set(Calendar.MINUTE, 0);
        cIni.set(Calendar.SECOND, 0);
        cIni.set(Calendar.MILLISECOND, 0);

        Calendar cFin = Calendar.getInstance();
        cFin.setTime(hasta);
        cFin.set(Calendar.HOUR_OF_DAY, 23);
        cFin.set(Calendar.MINUTE, 59);
        cFin.set(Calendar.SECOND, 59);
        cFin.set(Calendar.MILLISECOND, 999);

        ComboItem vendedor = (ComboItem) cbVendedor.getSelectedItem();
        String idVendedor = vendedor != null ? vendedor.id : "";

        java.sql.Timestamp tsInicio = new java.sql.Timestamp(cIni.getTimeInMillis());
        java.sql.Timestamp tsFin = new java.sql.Timestamp(cFin.getTimeInMillis());

        FacturaDAO.ResumenVentas resumen = facturaDAO.getResumenVentasRango(tsInicio, tsFin, idVendedor);
        lblTotalVendido.setText(formatearCop(resumen.getTotalVendido()));
        lblCantidadFacturas.setText(String.valueOf(resumen.getCantidadFacturas()));
        lblTicketPromedio.setText(formatearCop(resumen.getTicketPromedio()));

        List<FacturaDAO.VentaReporteItem> ventas = facturaDAO.getVentasPorRango(tsInicio, tsFin, idVendedor);
        refrescarTablaVentas(ventas);

        List<FacturaDAO.ProductoReporteItem> productos = facturaDAO.getTopProductos(tsInicio, tsFin, idVendedor, 10);
        refrescarTablaProductos(productos);
    }

    // Metodo refrescarTablaVentas: repinta tabla principal de facturas.
    private void refrescarTablaVentas(List<FacturaDAO.VentaReporteItem> ventas) {
        modeloVentas.setRowCount(0);
        for (FacturaDAO.VentaReporteItem item : ventas) {
            String fecha = item.getFechaFactura() != null ? dateTimeFormat.format(item.getFechaFactura()) : "";
            modeloVentas.addRow(new Object[]{
                    item.getIdFactura(),
                    fecha,
                    item.getVendedorNombre(),
                    item.getEstadoFactura(),
                    formatearCop(item.getTotalFactura())
            });
        }
    }

    // Metodo refrescarTablaProductos: repinta ranking de productos vendidos.
    private void refrescarTablaProductos(List<FacturaDAO.ProductoReporteItem> productos) {
        modeloProductos.setRowCount(0);
        for (FacturaDAO.ProductoReporteItem item : productos) {
            modeloProductos.addRow(new Object[]{
                    item.getNombreProducto(),
                    item.getUnidadesVendidas(),
                    formatearCop(item.getIngresoEstimado())
            });
        }
    }

    // Metodo formatearCop: estandariza moneda para UI.
    private String formatearCop(double valor) {
        return copFormat.format(valor).replace("COP", "").trim();
    }

    // Item simple para combo (id + nombre visible).
    private static class ComboItem {
        private final String id;
        private final String nombre;

        public ComboItem(String id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    /**
     * Borde redondeado reutilizable para mantener consistencia visual.
     */
    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

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
            g2.fillRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.setColor(((JComponent) c).getBackground());
            g2.fillRoundRect(x + thickness, y + thickness, width - (2 * thickness) - 1, height - (2 * thickness) - 1, radius - thickness, radius - thickness);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness + 2, thickness + 4, thickness + 2, thickness + 4);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = thickness + 4;
            insets.top = thickness + 2;
            insets.right = thickness + 4;
            insets.bottom = thickness + 2;
            return insets;
        }
    }
}
