package presentacion.ventanas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import presentacion.Login;
import roles.SesionUsuario;

/**
 * Ventana principal del rol Administrador.
 * Contiene navegación lateral y módulos de gestión (inventario, proveedores, etc.).
 */
public class formAdmin extends javax.swing.JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelActivo;
    private panelAdminDashboard dashboardPanel;
    private panelAgregarMedicamentos medicamentosPanel;
    private panelGestionProveedores proveedoresPanel;
    private panelAdminInventario inventarioPanel;
    
    // Design tokens: paleta central para mantener consistencia visual.
    private Color slate900 = new Color(15, 23, 42);
    private Color slate800 = new Color(30, 41, 59);
    private Color slate700 = new Color(51, 65, 85);
    private Color slate600 = new Color(71, 85, 105);
    private Color slate500 = new Color(100, 116, 139);
    private Color slate400 = new Color(148, 163, 184);
    private Color slate200 = new Color(226, 232, 240);
    private Color slate100 = new Color(241, 245, 249);
    private Color slate50 = new Color(248, 250, 252);
    private Color white = new Color(255, 255, 255);
    private Color blue600 = new Color(37, 99, 235);
    private Color blue700 = new Color(29, 78, 216);
    private Color blue50 = new Color(239, 246, 255);
    private Color red500 = new Color(239, 68, 68);
    private Color red50 = new Color(254, 242, 242);
    private Color green500 = new Color(34, 197, 94);
    private Color sidebarBg = new Color(15, 23, 42);
    private Color sidebarItem = new Color(15, 23, 42);
    private Color sidebarHover = new Color(30, 42, 72);
    private Color sidebarActive = new Color(37, 99, 235);

    public formAdmin() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(1400, 800);
        
        cargarPaneles();
        seleccionarBoton(btnDashboard);
    }
    
    // Registra paneles hijos dentro del CardLayout principal.
    private void cargarPaneles() {
        cardLayout = (CardLayout) panelContenido.getLayout();
        
        dashboardPanel = new panelAdminDashboard(this);
        panelContenido.add(dashboardPanel, "dashboard");
        
        medicamentosPanel = new panelAgregarMedicamentos(this);
        panelContenido.add(medicamentosPanel, "medicamentos");
        
        proveedoresPanel = new panelGestionProveedores(this);
        panelContenido.add(proveedoresPanel, "proveedores");
        
        inventarioPanel = new panelAdminInventario(this);
        panelContenido.add(inventarioPanel, "inventario");
    }
    
    // Cambia la vista activa del contenedor central.
    public void cambiarVista(String vista) {
        cardLayout.show(panelContenido, vista);
    }
    
    // Actualiza estado visual del menú lateral (item activo vs inactivo).
    private void seleccionarBoton(JPanel boton) {
        // Reset all buttons
        btnDashboard.setBackground(sidebarItem);
        btnMedicamentos.setBackground(sidebarItem);
        btnProveedores.setBackground(sidebarItem);
        btnInventario.setBackground(sidebarItem);
        btnSalir.setBackground(sidebarItem);
        
        lblDashboard.setForeground(slate400);
        lblMedicamentos.setForeground(slate400);
        lblProveedores.setForeground(slate400);
        lblInventario.setForeground(slate400);
        lblSalir.setForeground(new Color(252, 165, 165));
        
        // Set active state
        panelActivo = boton;
        if (boton != null) {
            boton.setBackground(sidebarActive);
            if (boton == btnDashboard) lblDashboard.setForeground(white);
            if (boton == btnMedicamentos) lblMedicamentos.setForeground(white);
            if (boton == btnProveedores) lblProveedores.setForeground(white);
            if (boton == btnInventario) lblInventario.setForeground(white);
            if (boton == btnSalir) lblSalir.setForeground(white);
        }
        panelNavegacion.repaint();
    }
    
    // Cierra sesión actual y retorna al Login si el usuario confirma.
    public void mostrarConfirmacionSalida() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Esta seguro que desea cerrar sesion?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (result == JOptionPane.YES_OPTION) {
            SesionUsuario.getInstancia().cerrarSesion();
            this.dispose();
            new Login().setVisible(true);
        }
    }
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            new formAdmin().setVisible(true);
        });
    }

    @SuppressWarnings("unchecked")
    // Metodo initComponents: logica de interfaz asociada a este formulario/panel.
    private void initComponents() {
        panelPrincipal = new javax.swing.JPanel();
        panelSidebar = new javax.swing.JPanel();
        panelMarca = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelNavegacion = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JPanel();
        lblIconDashboard = new javax.swing.JLabel();
        lblDashboard = new javax.swing.JLabel();
        btnMedicamentos = new javax.swing.JPanel();
        lblIconMedicamentos = new javax.swing.JLabel();
        lblMedicamentos = new javax.swing.JLabel();
        btnProveedores = new javax.swing.JPanel();
        lblIconProveedores = new javax.swing.JLabel();
        lblProveedores = new javax.swing.JLabel();
        btnInventario = new javax.swing.JPanel();
        lblIconInventario = new javax.swing.JLabel();
        lblInventario = new javax.swing.JLabel();
        btnSalir = new javax.swing.JPanel();
        lblIconSalir = new javax.swing.JLabel();
        lblSalir = new javax.swing.JLabel();
        panelContenido = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelPrincipal.setBackground(slate50);
        panelPrincipal.setLayout(new java.awt.BorderLayout());

        // ===== SIDEBAR =====
        panelSidebar.setPreferredSize(new java.awt.Dimension(260, 800));
        panelSidebar.setLayout(new java.awt.BorderLayout());
        panelSidebar.setBackground(sidebarBg);

        // Brand section
        panelMarca.setBackground(sidebarBg);
        panelMarca.setPreferredSize(new java.awt.Dimension(260, 120));
        panelMarca.setLayout(new BorderLayout());
        panelMarca.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setPreferredSize(new Dimension(48, 48));
        lblLogo.setIcon(new LucideIcon("pill", 28, blue600));
        panelMarca.add(lblLogo, BorderLayout.CENTER);

        lblTitulo.setFont(new Font("Inter", Font.BOLD, 16));
        lblTitulo.setForeground(white);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setText("FarmaciaPlus");
        panelMarca.add(lblTitulo, BorderLayout.PAGE_END);

        lblSubtitulo.setFont(new Font("Inter", Font.PLAIN, 12));
        lblSubtitulo.setForeground(slate400);
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setText("Panel de Administracion");
        panelMarca.add(lblSubtitulo, BorderLayout.PAGE_END);

        panelSidebar.add(panelMarca, BorderLayout.PAGE_START);

        // Navigation
        panelNavegacion.setBackground(sidebarBg);
        panelNavegacion.setLayout(new java.awt.GridLayout(5, 1, 0, 4));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        // Dashboard button
        btnDashboard.setBackground(sidebarActive);
        btnDashboard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDashboard.setLayout(new BorderLayout(12, 0));
        btnDashboard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 14, 10, 14),
            BorderFactory.createEmptyBorder()
        ));
        btnDashboard.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                seleccionarBoton(btnDashboard);
                cambiarVista("dashboard");
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                if (panelActivo != btnDashboard) btnDashboard.setBackground(sidebarHover);
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                if (panelActivo != btnDashboard) btnDashboard.setBackground(sidebarItem);
            }
        });
        lblIconDashboard.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconDashboard.setPreferredSize(new Dimension(24, 24));
        lblIconDashboard.setIcon(new LucideIcon("layout-grid", 20, slate400));
        btnDashboard.add(lblIconDashboard, BorderLayout.WEST);
        lblDashboard.setFont(new Font("Inter", Font.BOLD, 14));
        lblDashboard.setForeground(white);
        lblDashboard.setText("Dashboard");
        btnDashboard.add(lblDashboard, BorderLayout.CENTER);
        panelNavegacion.add(btnDashboard);

        // Medicamentos button
        btnMedicamentos.setBackground(sidebarItem);
        btnMedicamentos.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMedicamentos.setLayout(new BorderLayout(12, 0));
        btnMedicamentos.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 14, 10, 14),
            BorderFactory.createEmptyBorder()
        ));
        btnMedicamentos.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                seleccionarBoton(btnMedicamentos);
                cambiarVista("medicamentos");
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                if (panelActivo != btnMedicamentos) btnMedicamentos.setBackground(sidebarHover);
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                if (panelActivo != btnMedicamentos) btnMedicamentos.setBackground(sidebarItem);
            }
        });
        lblIconMedicamentos.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconMedicamentos.setPreferredSize(new Dimension(24, 24));
        lblIconMedicamentos.setIcon(new LucideIcon("pill", 20, slate400));
        btnMedicamentos.add(lblIconMedicamentos, BorderLayout.WEST);
        lblMedicamentos.setFont(new Font("Inter", Font.BOLD, 14));
        lblMedicamentos.setForeground(slate400);
        lblMedicamentos.setText("Medicamentos");
        btnMedicamentos.add(lblMedicamentos, BorderLayout.CENTER);
        panelNavegacion.add(btnMedicamentos);

        // Proveedores button
        btnProveedores.setBackground(sidebarItem);
        btnProveedores.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnProveedores.setLayout(new BorderLayout(12, 0));
        btnProveedores.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 14, 10, 14),
            BorderFactory.createEmptyBorder()
        ));
        btnProveedores.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                seleccionarBoton(btnProveedores);
                cambiarVista("proveedores");
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                if (panelActivo != btnProveedores) btnProveedores.setBackground(sidebarHover);
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                if (panelActivo != btnProveedores) btnProveedores.setBackground(sidebarItem);
            }
        });
        lblIconProveedores.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconProveedores.setPreferredSize(new Dimension(24, 24));
        lblIconProveedores.setIcon(new LucideIcon("truck", 20, slate400));
        btnProveedores.add(lblIconProveedores, BorderLayout.WEST);
        lblProveedores.setFont(new Font("Inter", Font.BOLD, 14));
        lblProveedores.setForeground(slate400);
        lblProveedores.setText("Proveedores");
        btnProveedores.add(lblProveedores, BorderLayout.CENTER);
        panelNavegacion.add(btnProveedores);

        // Inventario button
        btnInventario.setBackground(sidebarItem);
        btnInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInventario.setLayout(new BorderLayout(12, 0));
        btnInventario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 14, 10, 14),
            BorderFactory.createEmptyBorder()
        ));
        btnInventario.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                seleccionarBoton(btnInventario);
                cambiarVista("inventario");
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                if (panelActivo != btnInventario) btnInventario.setBackground(sidebarHover);
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                if (panelActivo != btnInventario) btnInventario.setBackground(sidebarItem);
            }
        });
        lblIconInventario.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconInventario.setPreferredSize(new Dimension(24, 24));
        lblIconInventario.setIcon(new LucideIcon("package", 20, slate400));
        btnInventario.add(lblIconInventario, BorderLayout.WEST);
        lblInventario.setFont(new Font("Inter", Font.BOLD, 14));
        lblInventario.setForeground(slate400);
        lblInventario.setText("Inventario");
        btnInventario.add(lblInventario, BorderLayout.CENTER);
        panelNavegacion.add(btnInventario);

        // Salir button
        btnSalir.setBackground(sidebarItem);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setLayout(new BorderLayout(12, 0));
        btnSalir.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(10, 14, 10, 14),
            BorderFactory.createEmptyBorder()
        ));
        btnSalir.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                mostrarConfirmacionSalida();
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                btnSalir.setBackground(new Color(127, 29, 29));
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                btnSalir.setBackground(sidebarItem);
            }
        });
        lblIconSalir.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconSalir.setPreferredSize(new Dimension(24, 24));
        lblIconSalir.setIcon(new LucideIcon("log-out", 20, new Color(252, 165, 165)));
        btnSalir.add(lblIconSalir, BorderLayout.WEST);
        lblSalir.setFont(new Font("Inter", Font.BOLD, 14));
        lblSalir.setForeground(new Color(252, 165, 165));
        lblSalir.setText("Cerrar Sesion");
        btnSalir.add(lblSalir, BorderLayout.CENTER);
        panelNavegacion.add(btnSalir);

        panelSidebar.add(panelNavegacion, BorderLayout.CENTER);
        panelPrincipal.add(panelSidebar, BorderLayout.LINE_START);

        // Content panel
        panelContenido.setBackground(slate50);
        panelContenido.setLayout(new java.awt.CardLayout());
        panelPrincipal.add(panelContenido, BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    // Variables declaration
    private javax.swing.JPanel btnDashboard;
    private javax.swing.JPanel btnInventario;
    private javax.swing.JPanel btnMedicamentos;
    private javax.swing.JPanel btnProveedores;
    private javax.swing.JPanel btnSalir;
    private javax.swing.JLabel lblDashboard;
    private javax.swing.JLabel lblIconDashboard;
    private javax.swing.JLabel lblIconInventario;
    private javax.swing.JLabel lblIconMedicamentos;
    private javax.swing.JLabel lblIconProveedores;
    private javax.swing.JLabel lblIconSalir;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblMedicamentos;
    private javax.swing.JLabel lblProveedores;
    private javax.swing.JLabel lblSalir;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelMarca;
    private javax.swing.JPanel panelNavegacion;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelSidebar;
    // End of variables declaration
    
    // Iconos vectoriales dibujados en tiempo real para evitar assets externos.
    class LucideIcon implements javax.swing.Icon {
        private String name;
        private int size;
        private Color color;
        
        public LucideIcon(String name, int size, Color color) {
            this.name = name;
            this.size = size;
            this.color = color;
        }
        
        // Permite actualizar color del icono sin recrear la instancia.
        public void setColor(Color c) { this.color = c; }
        
        @Override
        // Metodo getIconWidth: logica de interfaz asociada a este formulario/panel.
        public int getIconWidth() { return size; }
        @Override
        // Metodo getIconHeight: logica de interfaz asociada a este formulario/panel.
        public int getIconHeight() { return size; }
        
        @Override
        // Metodo paintIcon: logica de interfaz asociada a este formulario/panel.
        public void paintIcon(Component c, Graphics g, int x, int y) {
            // Renderiza cada icono a escala 24x24 usando primitivas 2D.
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(color);
            
            double scale = size / 24.0;
            g2.translate(x, y);
            g2.scale(scale, scale);
            
            switch (name) {
                case "layout-grid":
                    g2.draw(new RoundRectangle2D.Double(3, 3, 7, 7, 1.5, 1.5));
                    g2.draw(new RoundRectangle2D.Double(14, 3, 7, 7, 1.5, 1.5));
                    g2.draw(new RoundRectangle2D.Double(3, 14, 7, 7, 1.5, 1.5));
                    g2.draw(new RoundRectangle2D.Double(14, 14, 7, 7, 1.5, 1.5));
                    break;
                case "pill":
                    g2.draw(new RoundRectangle2D.Double(6.5, 2.5, 11, 19, 5.5, 5.5));
                    g2.drawLine(6, 12, 18, 12);
                    g2.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 60));
                    g2.fill(new RoundRectangle2D.Double(7, 3, 10, 9, 5, 5));
                    g2.setColor(color);
                    break;
                case "truck":
                    g2.draw(new RoundRectangle2D.Double(1, 3, 15, 13, 2, 2));
                    g2.draw(new RoundRectangle2D.Double(16, 8, 6, 8, 2, 2));
                    g2.drawLine(16, 12, 20, 12);
                    g2.drawOval(5, 16, 4, 4);
                    g2.drawOval(17, 16, 4, 4);
                    break;
                case "package":
                    g2.draw(new RoundRectangle2D.Double(3, 8, 18, 12, 2, 2));
                    g2.drawPolyline(new int[]{3, 12, 21}, new int[]{8, 2, 8}, 3);
                    g2.drawLine(12, 2, 12, 20);
                    break;
                case "log-out":
                    g2.drawPolyline(new int[]{15, 19, 19, 19, 15}, new int[]{3, 3, 12, 21, 21}, 5);
                    g2.drawPolyline(new int[]{15, 19, 19}, new int[]{12, 8, 16}, 3);
                    g2.draw(new RoundRectangle2D.Double(3, 3, 9, 18, 2, 2));
                    break;
                case "search":
                    g2.drawOval(3, 3, 10, 10);
                    g2.drawLine(11, 11, 19, 19);
                    break;
                case "plus":
                    g2.drawLine(12, 5, 12, 19);
                    g2.drawLine(5, 12, 19, 12);
                    break;
                case "minus":
                    g2.drawLine(5, 12, 19, 12);
                    break;
                case "x":
                    g2.drawLine(6, 6, 18, 18);
                    g2.drawLine(18, 6, 6, 18);
                    break;
                case "chevron-down":
                    g2.drawPolyline(new int[]{6, 12, 18}, new int[]{9, 15, 9}, 3);
                    break;
                case "alert-circle":
                    g2.drawOval(3, 3, 18, 18);
                    g2.drawLine(12, 8, 12, 13);
                    g2.fillOval(11, 15, 2, 2);
                    break;
                case "check-circle":
                    g2.drawOval(3, 3, 18, 18);
                    g2.drawPolyline(new int[]{8, 11, 16}, new int[]{12, 15, 9}, 3);
                    break;
                case "trending-up":
                    g2.drawPolyline(new int[]{3, 9, 13, 17, 21}, new int[]{17, 11, 15, 7, 3}, 5);
                    g2.drawPolyline(new int[]{17, 21, 21}, new int[]{3, 3, 7}, 3);
                    break;
                case "users":
                    g2.drawOval(5, 3, 8, 8);
                    g2.drawArc(1, 15, 16, 8, 0, -180);
                    g2.drawOval(15, 5, 5, 5);
                    g2.drawArc(14, 13, 9, 6, 0, -180);
                    break;
                case "bar-chart-3":
                    g2.draw(new RoundRectangle2D.Double(4, 14, 4, 6, 1, 1));
                    g2.draw(new RoundRectangle2D.Double(10, 8, 4, 12, 1, 1));
                    g2.draw(new RoundRectangle2D.Double(16, 3, 4, 17, 1, 1));
                    break;
                case "activity":
                    g2.drawPolyline(new int[]{3, 8, 11, 15, 21}, new int[]{12, 12, 5, 19, 12}, 5);
                    break;
                case "clock":
                    g2.drawOval(3, 3, 18, 18);
                    g2.drawLine(12, 7, 12, 12);
                    g2.drawLine(12, 12, 16, 14);
                    break;
            }
            
            g2.dispose();
        }
    }
}
