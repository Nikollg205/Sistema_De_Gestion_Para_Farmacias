package presentacion.ventanas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class panelDashboard extends javax.swing.JPanel {
    
    private formVentas parent;
    
    private Color azulProfundo = new Color(15, 23, 42);
    private Color azulPrimario = new Color(59, 130, 246);
    private Color azulHover = new Color(37, 99, 235);
    private Color azulSuave = new Color(239, 246, 255);
    private Color blanco = new Color(255, 255, 255);
    private Color blancoGris = new Color(248, 250, 252);
    private Color grisBorde = new Color(226, 232, 240);
    private Color grisTexto = new Color(107, 114, 128);
    private Color textoPrincipal = new Color(15, 23, 42);
    private Color verdeExito = new Color(16, 185, 129);
    private Color acentoNaranja = new Color(249, 115, 22);
    private Color acentoMorado = new Color(139, 92, 246);
    private Color sombraColor = new Color(0, 0, 0, 18);

    public panelDashboard(formVentas parent) {
        this.parent = parent;
        initComponents();
        initCards();
        initQuickStats();
    }
    
    private void initCards() {
        cardPanel.removeAll();
        cardPanel.setLayout(new java.awt.GridLayout(1, 3, 20, 0));
        cardPanel.setBorder(new EmptyBorder(0, 2, 0, 2));
        
        cardPanel.add(new ActionCard(
            "Registrar Venta",
            "Procesa ventas rápidamente",
            "\uD83D\uDCB0",
            new Color[]{new Color(59, 130, 246), new Color(29, 78, 216)},
            () -> parent.cambiarVista("ventas")
        ));
        
        cardPanel.add(new ActionCard(
            "Consultar Inventario",
            "Gestiona el stock disponible",
            "\uD83D\uDCE6",
            new Color[]{new Color(16, 185, 129), new Color(5, 150, 105)},
            () -> parent.cambiarVista("inventario")
        ));
        
        cardPanel.add(new ActionCard(
            "Ver Reportes",
            "Analiza las m\u00E9tricas del d\u00EDa",
            "\uD83D\uDCCA",
            new Color[]{new Color(139, 92, 246), new Color(109, 40, 217)},
            null
        ));
        
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    private void initQuickStats() {
        statsPanel.removeAll();
        statsPanel.setLayout(new java.awt.GridLayout(1, 4, 18, 0));
        statsPanel.setBorder(new EmptyBorder(0, 2, 0, 2));
        
        data.FacturaDAO facturaDAO = new data.FacturaDAO();
        data.MedicamentoDAO medicamentoDAO = new data.MedicamentoDAO();
        
        double ventasHoy = facturaDAO.getVentasHoy();
        int facturasHoy = facturaDAO.getFacturasHoy();
        int totalProductos = medicamentoDAO.total();
        int stockBajo = medicamentoDAO.getStockBajo();
        
        statsPanel.add(new StatCard(
            "Ventas Hoy",
            formatMoney(ventasHoy),
            "\uD83D\uDCC8",
            facturasHoy + " facturas",
            true
        ));
        statsPanel.add(new StatCard(
            "Productos",
            String.valueOf(totalProductos),
            "\uD83D\uDCE6",
            "En stock",
            false
        ));
        statsPanel.add(new StatCard(
            "Clientes",
            String.valueOf(facturasHoy),
            "\uD83D\uDC65",
            "Atendidos hoy",
            false
        ));
        statsPanel.add(new StatCard(
            "Alertas Stock",
            String.valueOf(stockBajo),
            "\u26A0\uFE0F",
            stockBajo > 0 ? "Requieren atenci\u00F3n" : "Todo en orden",
            stockBajo == 0
        ));
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    private String formatMoney(double value) {
        if (value >= 1000000) {
            return "$" + (value / 1000000) + "M";
        } else if (value >= 1000) {
            return "$" + (value / 1000) + "K";
        }
        return "$" + (int) value;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelPrincipal = new javax.swing.JPanel();
        panelHeader = new javax.swing.JPanel();
        panelTitulo = new javax.swing.JPanel();
        lblBienvenida = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelInfo = new javax.swing.JPanel();
        panelFecha = new javax.swing.JPanel();
        lblFecha = new javax.swing.JLabel();
        lblHora = new javax.swing.JLabel();
        cardPanel = new javax.swing.JPanel();
        statsPanel = new javax.swing.JPanel();
        separadorSuperior = new javax.swing.JPanel();
        separadorInferior = new javax.swing.JPanel();

        setBackground(blancoGris);
        setLayout(new BorderLayout());

        panelPrincipal.setBackground(blancoGris);
        panelPrincipal.setBorder(new EmptyBorder(36, 36, 36, 36));
        panelPrincipal.setLayout(new BorderLayout(0, 0));

        // ===== HEADER =====
        panelHeader.setBackground(blancoGris);
        panelHeader.setLayout(new BorderLayout(0, 0));
        panelHeader.setBorder(new EmptyBorder(0, 0, 24, 0));

        // Title section
        panelTitulo.setBackground(blancoGris);
        panelTitulo.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        lblBienvenida.setFont(new Font("Segoe UI Semibold", 1, 28));
        lblBienvenida.setForeground(azulProfundo);
        lblBienvenida.setText("Panel de Control");
        panelTitulo.add(lblBienvenida);

        lblSubtitulo.setFont(new Font("Segoe UI", 0, 14));
        lblSubtitulo.setForeground(grisTexto);
        lblSubtitulo.setText("   \u2022   Gestiona tu farmacia de manera eficiente");
        panelTitulo.add(lblSubtitulo);

        panelHeader.add(panelTitulo, BorderLayout.CENTER);

        // Date/time card
        panelInfo.setBackground(blanco);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(grisBorde, 1, 12),
            new EmptyBorder(12, 24, 12, 24)
        ));
        panelInfo.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

        lblFecha.setFont(new Font("Segoe UI Semibold", 0, 12));
        lblFecha.setForeground(azulPrimario);
        lblFecha.setHorizontalAlignment(SwingConstants.CENTER);
        lblFecha.setText(getFechaCorta());
        panelInfo.add(lblFecha);

        // Vertical separator
        JLabel sep = new JLabel("  |  ");
        sep.setForeground(grisBorde);
        sep.setFont(new Font("Segoe UI", 0, 14));
        panelInfo.add(sep);

        lblHora.setFont(new Font("Segoe UI Semibold", 0, 18));
        lblHora.setForeground(azulProfundo);
        lblHora.setHorizontalAlignment(SwingConstants.CENTER);
        lblHora.setText(getHora());
        panelInfo.add(lblHora);

        panelHeader.add(panelInfo, BorderLayout.EAST);

        panelPrincipal.add(panelHeader, BorderLayout.PAGE_START);

        // Separator line
        separadorSuperior.setBackground(grisBorde);
        separadorSuperior.setPreferredSize(new Dimension(0, 1));
        panelPrincipal.add(separadorSuperior, BorderLayout.CENTER);

        // ===== CARDS SECTION =====
        JPanel cardsWrapper = new JPanel(new BorderLayout());
        cardsWrapper.setBackground(blancoGris);
        cardsWrapper.setBorder(new EmptyBorder(28, 0, 28, 0));

        // Section label
        JPanel sectionLabel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        sectionLabel.setBackground(blancoGris);
        JLabel lblSection = new JLabel("Acciones R\u00E1pidas");
        lblSection.setFont(new Font("Segoe UI Semibold", 1, 15));
        lblSection.setForeground(azulProfundo);
        sectionLabel.add(lblSection);
        cardsWrapper.add(sectionLabel, BorderLayout.PAGE_START);

        cardPanel.setBackground(blancoGris);
        cardPanel.setBorder(new EmptyBorder(16, 0, 0, 0));
        cardsWrapper.add(cardPanel, BorderLayout.CENTER);

        panelPrincipal.add(cardsWrapper, BorderLayout.PAGE_END);

        // Add everything with manual spacing
        add(panelPrincipal, BorderLayout.CENTER);
    }

    private String getFechaCorta() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEE, dd MMM yyyy");
        return sdf.format(new java.util.Date()).toUpperCase();
    }
    
    private String getHora() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
        return sdf.format(new java.util.Date());
    }

    // Variables declaration
    private javax.swing.JPanel cardPanel;
    private javax.swing.JLabel lblBienvenida;
    private javax.swing.JLabel lblFecha;
    private javax.swing.JLabel lblHora;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JPanel panelFecha;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelTitulo;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JPanel separadorSuperior;
    private javax.swing.JPanel separadorInferior;
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
    
    // Action Card Component
    class ActionCard extends JPanel {
        private Color[] gradientColors;
        private String emoji;
        private Runnable action;
        private boolean hover = false;
        
        public ActionCard(String title, String subtitle, String emoji, Color[] colors, Runnable action) {
            this.gradientColors = colors;
            this.emoji = emoji;
            this.action = action;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setLayout(new BorderLayout());
            setMinimumSize(new Dimension(200, 120));
            setPreferredSize(new Dimension(200, 120));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (action != null) action.run();
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    hover = true;
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    hover = false;
                    repaint();
                }
            });
            
            // Content with centered icon and text
            JPanel contentPanel = new JPanel();
            contentPanel.setOpaque(false);
            contentPanel.setLayout(new BorderLayout(16, 6));
            contentPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
            
            // Icon panel
            JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            iconPanel.setOpaque(false);
            JLabel lblIcon = new JLabel(emoji);
            lblIcon.setFont(new Font("Segoe UI Emoji", 0, 38));
            iconPanel.add(lblIcon);
            contentPanel.add(iconPanel, BorderLayout.WEST);
            
            // Text panel
            JPanel textPanel = new JPanel(new java.awt.GridLayout(2, 1, 0, 4));
            textPanel.setOpaque(false);
            
            JLabel lblTitle = new JLabel(title);
            lblTitle.setFont(new Font("Segoe UI Semibold", 1, 16));
            lblTitle.setForeground(blanco);
            
            JLabel lblSubtitle = new JLabel(subtitle);
            lblSubtitle.setFont(new Font("Segoe UI", 0, 12));
            lblSubtitle.setForeground(new Color(255, 255, 255, 190));
            
            textPanel.add(lblTitle);
            textPanel.add(lblSubtitle);
            contentPanel.add(textPanel, BorderLayout.CENTER);
            
            // Arrow indicator
            JLabel arrow = new JLabel("\u2192");
            arrow.setFont(new Font("Segoe UI", 1, 20));
            arrow.setForeground(new Color(255, 255, 255, 160));
            arrow.setHorizontalAlignment(SwingConstants.RIGHT);
            contentPanel.add(arrow, BorderLayout.EAST);
            
            add(contentPanel, BorderLayout.CENTER);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int radius = 16;
            int pad = hover ? 4 : 6;
            int w = getWidth() - pad * 2;
            int h = getHeight() - pad * 2;
            
            // Drop shadow
            g2.setColor(sombraColor);
            g2.fillRoundRect(pad, pad + 3, w, h, radius, radius);
            g2.setColor(new Color(0, 0, 0, 8));
            g2.fillRoundRect(pad, pad + 1, w, h, radius, radius);
            
            // Gradient fill
            GradientPaint gradient = new GradientPaint(
                0, 0, gradientColors[0],
                getWidth(), getHeight(), gradientColors[1]
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(pad, pad, w, h, radius, radius);
            
            // Hover overlay
            if (hover) {
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(pad, pad, w, h, radius, radius);
            }
            
            // Top highlight
            g2.setColor(new Color(255, 255, 255, 35));
            g2.setStroke(new java.awt.BasicStroke(1f));
            g2.drawRoundRect(pad, pad, w, h, radius, radius);
            
            g2.dispose();
        }
    }
    
    // Stat Card Component
    class StatCard extends JPanel {
        private String titulo, valor, emoji, subtexto;
        private boolean esPositivo;
        
        public StatCard(String titulo, String valor, String emoji, String subtexto, boolean esPositivo) {
            this.titulo = titulo;
            this.valor = valor;
            this.emoji = emoji;
            this.subtexto = subtexto;
            this.esPositivo = esPositivo;
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setLayout(new BorderLayout());
            setMinimumSize(new Dimension(150, 100));
            setPreferredSize(new Dimension(150, 100));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(blancoGris);
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(blanco);
                    repaint();
                }
            });
            setBackground(blanco);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int radius = 14;
            int pad = 6;
            int w = getWidth() - pad * 2;
            int h = getHeight() - pad * 2;
            
            // Shadow
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(pad, pad + 2, w, h, radius, radius);
            
            // Background
            Color bgColor = getBackground();
            g2.setColor(bgColor);
            g2.fillRoundRect(pad, pad, w, h, radius, radius);
            
            // Border
            g2.setColor(grisBorde);
            g2.setStroke(new java.awt.BasicStroke(1f));
            g2.drawRoundRect(pad, pad, w, h, radius, radius);
            
            // Emoji circle
            int circleSize = 38;
            int cx = pad + 16;
            int cy = pad + 14;
            g2.setColor(azulSuave);
            g2.fillRoundRect(cx, cy, circleSize, circleSize, circleSize / 2, circleSize / 2);
            
            g2.setFont(new Font("Segoe UI Emoji", 0, 20));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(textoPrincipal);
            g2.drawString(emoji, cx + (circleSize - fm.stringWidth(emoji)) / 2, cy + (circleSize + fm.getAscent()) / 2 - 2);
            
            // Title
            g2.setFont(new Font("Segoe UI", 0, 11));
            g2.setColor(grisTexto);
            g2.drawString(titulo, cx + circleSize + 12, pad + 30);
            
            // Value
            g2.setFont(new Font("Segoe UI Semibold", 1, 22));
            g2.setColor(azulProfundo);
            g2.drawString(valor, pad + 16, pad + 66);
            
            // Subtext
            g2.setFont(new Font("Segoe UI", 0, 10));
            g2.setColor(esPositivo ? verdeExito : grisTexto);
            String indicator = esPositivo ? "\u25B2 " : "";
            g2.drawString(indicator + subtexto, pad + 16, pad + 82);
            
            g2.dispose();
        }
    }
}
