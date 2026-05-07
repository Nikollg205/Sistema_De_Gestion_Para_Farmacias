package presentacion.ventanas;

import data.FacturaDAO;
import data.MedicamentoDAO;
import data.ProveedorDAO;
import database.Conexion;
import inventario.Factura;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class panelAdminDashboard extends javax.swing.JPanel {
    
    private formAdmin parent;
    private static final NumberFormat COP_FORMAT = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
    
    // DAOs
    private FacturaDAO facturaDAO;
    private MedicamentoDAO medicamentoDAO;
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
    private Color amber500 = new Color(245, 158, 11);
    private Color amber50 = new Color(255, 251, 235);
    private Color purple500 = new Color(168, 85, 247);
    private Color purple50 = new Color(250, 245, 255);

    public panelAdminDashboard(formAdmin parent) {
        this.parent = parent;
        this.facturaDAO = new FacturaDAO();
        this.medicamentoDAO = new MedicamentoDAO();
        this.proveedorDAO = new ProveedorDAO();
        initComponents();
        initCards();
        initStats();
        initRecentActivity();
    }
    
    private String formatCOP(double value) {
        return COP_FORMAT.format(value).replace("COP", "").trim();
    }
    
    private void initCards() {
        cardPanel.removeAll();
        cardPanel.setLayout(new java.awt.GridLayout(1, 4, 16, 0));
        cardPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Real data from DB
        double ventasHoy = facturaDAO.getVentasHoy();
        int productosActivos = medicamentoDAO.total();
        int proveedores = proveedorDAO.total();
        int alertasStock = facturaDAO.getAlertasStock();
        
        cardPanel.add(new StatCard("Ventas del Dia", formatCOP(ventasHoy), "bar-chart-3", blue600, blue50, formatCOP(ventasHoy) + " hoy", true));
        cardPanel.add(new StatCard("Productos Activos", String.valueOf(productosActivos), "package", green500, green50, "En catalogo", false));
        cardPanel.add(new StatCard("Proveedores", String.valueOf(proveedores), "users", purple500, purple50, "Registrados", false));
        cardPanel.add(new StatCard("Alertas Stock", String.valueOf(alertasStock), "alert-circle", red500, red50, "Requieren atencion", true));
        
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    private void initStats() {
        statsPanel.removeAll();
        statsPanel.setLayout(new BorderLayout(0, 0));
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(white);
        header.setBorder(new EmptyBorder(20, 24, 16, 24));
        
        JLabel lblTitle = new JLabel("Resumen Semanal");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 16));
        lblTitle.setForeground(slate900);
        header.add(lblTitle, BorderLayout.WEST);
        
        JLabel lblPeriod = new JLabel("Ultimos 7 dias");
        lblPeriod.setFont(new Font("Inter", Font.PLAIN, 13));
        lblPeriod.setForeground(slate500);
        header.add(lblPeriod, BorderLayout.EAST);
        
        statsPanel.add(header, BorderLayout.PAGE_START);
        
        JPanel chartArea = new JPanel(new BorderLayout());
        chartArea.setBackground(white);
        chartArea.setBorder(new EmptyBorder(0, 24, 24, 24));
        chartArea.setPreferredSize(new Dimension(0, 220));
        
        JPanel bars = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bars.setBackground(white);
        bars.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Get real weekly sales or use sample if none
        List<Double> ventas = facturaDAO.getVentasSemanales();
        final String[] days = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};
        final int maxH = 140;
        
        // Normalize values to fit chart height
        double maxVenta = ventas.isEmpty() ? 100 : ventas.stream().mapToDouble(Double::doubleValue).max().orElse(100);
        if (maxVenta == 0) maxVenta = 100;
        
        int count = Math.min(days.length, Math.max(ventas.size(), 1));
        for (int i = 0; i < count; i++) {
            final int idx = i;
            double valor = i < ventas.size() ? ventas.get(i) : 0;
            int barHeight = (int) ((valor / maxVenta) * maxH);
            if (barHeight < 5 && valor > 0) barHeight = 5;
            
            JPanel col = new JPanel(new BorderLayout());
            col.setBackground(white);
            col.setPreferredSize(new Dimension(48, maxH + 30));
            
            JPanel barWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            barWrap.setBackground(white);
            barWrap.setPreferredSize(new Dimension(48, maxH));
            
            Color barColor = i == count - 1 ? blue600 : blue500;
            BarPanel bp = new BarPanel(barHeight, barColor, maxH);
            bp.setPreferredSize(new Dimension(32, maxH));
            bp.setCursor(new Cursor(Cursor.HAND_CURSOR));
            bp.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { bp.setHover(true); repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { bp.setHover(false); repaint(); }
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(panelAdminDashboard.this,
                        "Ventas del " + days[idx] + ": " + formatCOP(valor),
                        "Detalle", JOptionPane.INFORMATION_MESSAGE);
                }
            });
            barWrap.add(bp);
            
            col.add(barWrap, BorderLayout.CENTER);
            
            JLabel lblDay = new JLabel(days[idx], SwingConstants.CENTER);
            lblDay.setFont(new Font("Inter", Font.PLAIN, 11));
            lblDay.setForeground(slate500);
            col.add(lblDay, BorderLayout.PAGE_END);
            
            bars.add(col);
        }
        
        chartArea.add(bars, BorderLayout.CENTER);
        statsPanel.add(chartArea, BorderLayout.CENTER);
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    private void initRecentActivity() {
        activityPanel.removeAll();
        activityPanel.setLayout(new BorderLayout());
        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(white);
        header.setBorder(new EmptyBorder(20, 24, 16, 24));
        
        JLabel lblTitle = new JLabel("Actividad Reciente");
        lblTitle.setFont(new Font("Inter", Font.BOLD, 16));
        lblTitle.setForeground(slate900);
        header.add(lblTitle, BorderLayout.WEST);
        
        JLabel lblViewAll = new JLabel("Ver todo");
        lblViewAll.setFont(new Font("Inter", Font.PLAIN, 13));
        lblViewAll.setForeground(blue600);
        lblViewAll.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblViewAll.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(panelAdminDashboard.this,
                    "Modulo de actividad en desarrollo", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            @Override
            public void mouseEntered(MouseEvent e) { lblViewAll.setForeground(blue700); }
            @Override
            public void mouseExited(MouseEvent e) { lblViewAll.setForeground(blue600); }
        });
        header.add(lblViewAll, BorderLayout.EAST);
        
        activityPanel.add(header, BorderLayout.PAGE_START);
        
        JPanel list = new JPanel();
        list.setLayout(new java.awt.GridLayout(0, 1, 0, 1));
        list.setBackground(white);
        list.setBorder(new EmptyBorder(0, 24, 24, 24));
        
        // Real activity from DB
        List<Factura> facturas = facturaDAO.getFacturasRecientes(5);
        
        if (facturas.isEmpty()) {
            // Show placeholder if no data
            addActivityItem("Sin ventas aun", "Registra tu primera venta para verla aqui", "Ahora", slate400, "clock", list);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            for (Factura f : facturas) {
                String hora = f.getFecha() != null ? sdf.format(new Date(f.getFecha().getTime())) : "N/A";
                String tipo = "Venta " + (f.getEstado() != null ? f.getEstado() : "completada");
                String detalle = "Factura " + f.getId() + " - " + formatCOP(f.getPrecioTotal());
                Color color = "completada".equals(f.getEstado()) ? green500 : amber500;
                String icon = "completada".equals(f.getEstado()) ? "check-circle" : "clock";
                addActivityItem(tipo, detalle, hora, color, icon, list);
            }
        }
        
        activityPanel.add(list, BorderLayout.CENTER);
        
        activityPanel.revalidate();
        activityPanel.repaint();
    }
    
    private void addActivityItem(String tipo, String detalle, String hora, Color color, String icon, JPanel parent) {
        JPanel row = new JPanel(new BorderLayout(12, 0));
        row.setBackground(white);
        row.setBorder(new EmptyBorder(12, 0, 12, 0));
        
        // Icon circle
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 15));
                g2.fillOval(0, 0, 40, 40);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                int s = 20;
                g2.translate(10, 10);
                g2.scale(s / 24.0, s / 24.0);
                drawIcon(g2, icon, color);
                g2.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(40, 40));
        iconPanel.setOpaque(false);
        row.add(iconPanel, BorderLayout.WEST);
        
        JPanel textPanel = new JPanel(new java.awt.GridLayout(2, 1, 0, 4));
        textPanel.setBackground(white);
        textPanel.setOpaque(false);
        
        JLabel lblTipo = new JLabel(tipo);
        lblTipo.setFont(new Font("Inter", Font.BOLD, 13));
        lblTipo.setForeground(slate900);
        textPanel.add(lblTipo);
        
        JLabel lblDetalle = new JLabel(detalle);
        lblDetalle.setFont(new Font("Inter", Font.PLAIN, 12));
        lblDetalle.setForeground(slate500);
        textPanel.add(lblDetalle);
        
        row.add(textPanel, BorderLayout.CENTER);
        
        JLabel lblHora = new JLabel(hora);
        lblHora.setFont(new Font("Inter", Font.PLAIN, 12));
        lblHora.setForeground(slate400);
        lblHora.setPreferredSize(new Dimension(80, 0));
        row.add(lblHora, BorderLayout.EAST);
        
        parent.add(row);
    }
    
    private void drawIcon(Graphics2D g2, String name, Color color) {
        switch (name) {
            case "check-circle":
                g2.draw(new RoundRectangle2D.Double(3, 3, 18, 18, 9, 9));
                g2.drawPolyline(new int[]{8, 11, 16}, new int[]{12, 15, 9}, 3);
                break;
            case "activity":
                g2.drawPolyline(new int[]{3, 8, 11, 15, 21}, new int[]{12, 12, 5, 19, 12}, 5);
                break;
            case "users":
                g2.drawOval(5, 3, 8, 8);
                g2.drawArc(1, 15, 16, 8, 0, -180);
                g2.drawOval(15, 5, 5, 5);
                g2.drawArc(14, 13, 9, 6, 0, -180);
                break;
            case "alert-circle":
                g2.draw(new RoundRectangle2D.Double(3, 3, 18, 18, 9, 9));
                g2.drawLine(12, 8, 12, 13);
                g2.fillOval(11, 15, 2, 2);
                break;
            case "clock":
                g2.draw(new RoundRectangle2D.Double(3, 3, 18, 18, 9, 9));
                g2.drawLine(12, 7, 12, 12);
                g2.drawLine(12, 12, 16, 14);
                break;
            case "package":
                g2.draw(new RoundRectangle2D.Double(3, 8, 18, 12, 2, 2));
                g2.drawPolyline(new int[]{3, 12, 21}, new int[]{8, 2, 8}, 3);
                g2.drawLine(12, 2, 12, 20);
                break;
            case "bar-chart-3":
                g2.draw(new RoundRectangle2D.Double(4, 14, 4, 6, 1, 1));
                g2.draw(new RoundRectangle2D.Double(10, 8, 4, 12, 1, 1));
                g2.draw(new RoundRectangle2D.Double(16, 3, 4, 17, 1, 1));
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelPrincipal = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        panelTituloWrap = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelFecha = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btnReporte = new javax.swing.JButton();
        cardPanel = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        activityPanel = new javax.swing.JPanel();

        setBackground(slate50);
        setLayout(new BorderLayout());

        panelPrincipal.setBackground(slate50);
        panelPrincipal.setLayout(new BorderLayout(0, 24));
        panelPrincipal.setBorder(new EmptyBorder(28, 28, 28, 28));

        // Header
        panelHeader.setBackground(slate50);
        panelHeader.setLayout(new BorderLayout(16, 0));

        panelTituloWrap.setBackground(slate50);
        panelTituloWrap.setLayout(new java.awt.GridLayout(2, 1, 0, 4));

        lblTitulo.setFont(new Font("Inter", Font.BOLD, 24));
        lblTitulo.setForeground(slate900);
        lblTitulo.setText("Dashboard");
        panelTituloWrap.add(lblTitulo);

        lblSubtitulo.setFont(new Font("Inter", Font.PLAIN, 14));
        lblSubtitulo.setForeground(slate500);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        lblSubtitulo.setText("Resumen general de la farmacia - " + sdf.format(new Date()));
        panelTituloWrap.add(lblSubtitulo);

        panelHeader.add(panelTituloWrap, BorderLayout.CENTER);

        // Date + Actions
        panelAcciones.setBackground(slate50);
        panelAcciones.setLayout(new FlowLayout(FlowLayout.RIGHT, 12, 0));

        panelFecha.setBackground(white);
        panelFecha.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(slate200, 1, 10),
            new EmptyBorder(8, 16, 8, 16)
        ));
        panelFecha.setLayout(new BorderLayout());

        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyyy", new Locale("es", "ES"));
        lblFecha.setFont(new Font("Inter", Font.PLAIN, 13));
        lblFecha.setForeground(slate600);
        lblFecha.setText(sdf2.format(new Date()));
        panelFecha.add(lblFecha, BorderLayout.CENTER);

        panelAcciones.add(panelFecha);

        btnReporte = new ModernButton("Exportar Reporte");
        btnReporte.setPreferredSize(new Dimension(150, 38));
        btnReporte.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(panelAdminDashboard.this,
                    "Modulo de reportes en desarrollo", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        panelAcciones.add(btnReporte);

        panelHeader.add(panelAcciones, BorderLayout.EAST);
        panelPrincipal.add(panelHeader, BorderLayout.PAGE_START);

        // Stat Cards
        cardPanel.setBackground(slate50);
        cardPanel.setLayout(new java.awt.GridLayout(1, 4, 16, 0));
        panelPrincipal.add(cardPanel, BorderLayout.PAGE_START);

        // Weekly Stats
        statsPanel.setBackground(white);
        statsPanel.setBorder(new RoundedBorder(slate200, 1, 12));
        statsPanel.setPreferredSize(new Dimension(0, 260));
        statsPanel.setLayout(new BorderLayout());
        panelPrincipal.add(statsPanel, BorderLayout.CENTER);

        // Recent Activity
        activityPanel.setBackground(white);
        activityPanel.setBorder(new RoundedBorder(slate200, 1, 12));
        activityPanel.setPreferredSize(new Dimension(0, 320));
        activityPanel.setLayout(new BorderLayout());
        panelPrincipal.add(activityPanel, BorderLayout.PAGE_END);

        add(panelPrincipal, BorderLayout.CENTER);
    }

    // Variables declaration
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JButton btnReporte;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelTituloWrap;
    private javax.swing.JPanel panelFecha;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel cardPanel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel activityPanel;
    // End of variables declaration
    
    // ===== UI Components =====
    
    class StatCard extends JPanel {
        private String titulo, valor, iconName, badge;
        private Color iconColor, bgTint;
        private boolean esAlerta;
        
        public StatCard(String titulo, String valor, String iconName, Color iconColor, Color bgTint, String badge, boolean esAlerta) {
            this.titulo = titulo;
            this.valor = valor;
            this.iconName = iconName;
            this.iconColor = iconColor;
            this.bgTint = bgTint;
            this.badge = badge;
            this.esAlerta = esAlerta;
            
            setLayout(new BorderLayout());
            setBackground(white);
            setBorder(new RoundedBorder(slate200, 1, 12));
            setPreferredSize(new Dimension(0, 100));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(bgTint);
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(white);
                    repaint();
                }
                @Override
                public void mouseClicked(MouseEvent e) {
                    JOptionPane.showMessageDialog(panelAdminDashboard.this,
                        "Detalle de " + titulo + ": " + valor,
                        "Informacion", JOptionPane.INFORMATION_MESSAGE);
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Icon background circle
            int circleSize = 40;
            int cx = 16, cy = 16;
            g2.setColor(bgTint);
            g2.fillOval(cx, cy, circleSize, circleSize);
            
            // Icon
            g2.setColor(iconColor);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.translate(cx + 10, cy + 10);
            float s = 20 / 24.0f;
            g2.scale(s, s);
            drawIcon(g2, iconName, iconColor);
            g2.dispose();
            
            // Text
            Graphics2D g3 = (Graphics2D) g.create();
            g3.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            g3.setFont(new Font("Inter", Font.PLAIN, 13));
            g3.setColor(slate500);
            g3.drawString(titulo, cx + circleSize + 14, 32);
            
            g3.setFont(new Font("Inter", Font.BOLD, 22));
            g3.setColor(slate900);
            g3.drawString(valor, cx + circleSize + 14, 62);
            
            g3.setFont(new Font("Inter", Font.PLAIN, 11));
            g3.setColor(esAlerta ? red500 : green500);
            g3.drawString(badge, cx + circleSize + 14, 80);
            
            g3.dispose();
        }
    }
    
    class BarPanel extends JPanel {
        private int height, maxHeight;
        private Color color;
        private boolean isHover = false;
        
        public BarPanel(int height, Color color, int maxHeight) {
            this.height = height;
            this.color = color;
            this.maxHeight = maxHeight;
            setOpaque(false);
        }
        
        public void setHover(boolean h) { isHover = h; repaint(); }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = height;
            int y = maxHeight - h;
            int radius = 4;
            
            if (isHover) {
                g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 40));
                g2.fill(new RoundRectangle2D.Double(0, y - 2, w, h + 2, radius, radius));
            }
            
            g2.setColor(color);
            g2.fill(new RoundRectangle2D.Double(0, y, w, h, radius, radius));
            g2.dispose();
        }
    }
    
    class RoundedBorder extends javax.swing.border.AbstractBorder {
        private Color color; private int thickness; private int radius;
        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color; this.thickness = thickness; this.radius = radius;
        }
        @Override
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
    
    class ModernButton extends javax.swing.JButton {
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
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isHover ? hoverColor : bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
