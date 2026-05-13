package presentacion.ventanas;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import presentacion.Login;
import roles.SesionUsuario;

/**
 * Ventana principal para el rol de cajero/operador.
 * Gestiona navegación lateral y vistas internas (dashboard, ventas, inventario).
 */
public class formVentas extends javax.swing.JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelActivo;
    private formInventario inventarioPanel;
    private formRegistrarVenta ventasPanel;
    private panelDashboard dashboardPanel;
    
    // Paleta visual centralizada para mantener consistencia en toda la pantalla.
    private Color azulProfundo = new Color(15, 23, 42);
    private Color azulPrimario = new Color(59, 130, 246);
    private Color azulHover = new Color(37, 99, 235);
    private Color azulSuave = new Color(239, 246, 255);
    private Color azulSidebar = new Color(30, 64, 175);
    private Color azulSidebarHover = new Color(37, 79, 205);
    private Color azulSidebarActivo = new Color(59, 130, 246);
    private Color blanco = new Color(255, 255, 255);
    private Color blancoGris = new Color(248, 250, 252);
    private Color grisBorde = new Color(226, 232, 240);
    private Color grisTexto = new Color(107, 114, 128);
    private Color textoPrincipal = new Color(15, 23, 42);
    private Color verdeExito = new Color(16, 185, 129);
    private Color rojoPeligro = new Color(239, 68, 68);

    public formVentas() {
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(1400, 800);
        
        cargarPaneles();
        seleccionarBoton(btnDashboard);
    }
    
    // Carga paneles funcionales dentro del contenedor con CardLayout.
    private void cargarPaneles() {
        cardLayout = (CardLayout) panelContenido.getLayout();
        
        dashboardPanel = new panelDashboard(this);
        panelContenido.add(dashboardPanel, "dashboard");
        
        inventarioPanel = new formInventario(this);
        panelContenido.add(inventarioPanel, "inventario");
        
        ventasPanel = new formRegistrarVenta(this);
        panelContenido.add(ventasPanel, "ventas");
    }
    
    // Cambia la vista visible en el área principal.
    public void cambiarVista(String vista) {
        cardLayout.show(panelContenido, vista);
    }
    
    // Sincroniza carrito externo con panel de ventas.
    public void actualizarCarrito(Map<String, Map<String, Object>> carrito) {
        if (ventasPanel != null) {
            ventasPanel.carritoVentas.clear();
            for (Map<String, Object> item : carrito.values()) {
                ventasPanel.carritoVentas.add(item);
            }
            ventasPanel.actualizarCarrito();
        }
    }
    
    // Atajo para agregar producto al carrito desde otras vistas.
    public void agregarProducto(String id, String nombre, String lote, double precio, int cantidad) {
        if (ventasPanel != null) {
            ventasPanel.agregarProducto(id, nombre, lote, precio, cantidad);
        }
    }
    
    // Controla estado visual (activo/inactivo) de botones del sidebar.
    private void seleccionarBoton(JPanel boton) {
        btnDashboard.setBackground(azulSidebar);
        btnVentas.setBackground(azulSidebar);
        btnInventario.setBackground(azulSidebar);
        btnSalir.setBackground(azulSidebar);
        
        lblDashboard.setForeground(new Color(147, 197, 253));
        lblVentas.setForeground(new Color(147, 197, 253));
        lblInventario.setForeground(new Color(147, 197, 253));
        lblSalir.setForeground(new Color(252, 165, 165));
        
        panelActivo = boton;
        if (boton != null) {
            boton.setBackground(azulSidebarActivo);
            if (boton == btnDashboard) lblDashboard.setForeground(blanco);
            if (boton == btnVentas) lblVentas.setForeground(blanco);
            if (boton == btnInventario) lblInventario.setForeground(blanco);
            if (boton == btnSalir) lblSalir.setForeground(blanco);
        }
        panelNavegacion.repaint();
    }
    
    // Confirmación de logout con limpieza de sesión y retorno a Login.
    public void mostrarConfirmacionSalida() {
        int result = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro que desea cerrar sesión?",
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
            new formVentas().setVisible(true);
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
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
        btnVentas = new javax.swing.JPanel();
        lblIconVentas = new javax.swing.JLabel();
        lblVentas = new javax.swing.JLabel();
        btnInventario = new javax.swing.JPanel();
        lblIconInventario = new javax.swing.JLabel();
        lblInventario = new javax.swing.JLabel();
        btnSalir = new javax.swing.JPanel();
        lblIconSalir = new javax.swing.JLabel();
        lblSalir = new javax.swing.JLabel();
        panelContenido = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        panelPrincipal.setBackground(blancoGris);
        panelPrincipal.setLayout(new java.awt.BorderLayout());

        // ===== SIDEBAR VERTICAL =====
        panelSidebar.setPreferredSize(new java.awt.Dimension(280, 800));
        panelSidebar.setLayout(new java.awt.BorderLayout());
        panelSidebar.setBackground(azulSidebar);
        panelSidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, grisBorde));

        // Panel de Marca
        panelMarca.setBackground(azulSidebar);
        panelMarca.setPreferredSize(new java.awt.Dimension(280, 140));
        panelMarca.setLayout(new BorderLayout());
        panelMarca.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/imagenes/pharmacy.png"));
            java.awt.Image img = icon.getImage().getScaledInstance(56, 56, java.awt.Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            lblLogo.setText("💊");
            lblLogo.setFont(new Font("Segoe UI Emoji", 0, 48));
        }
        panelMarca.add(lblLogo, BorderLayout.CENTER);

        lblTitulo.setFont(new Font("Segoe UI Semibold", 1, 17));
        lblTitulo.setForeground(blanco);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setText("Proyecto Farmacia");
        panelMarca.add(lblTitulo, BorderLayout.PAGE_END);

        lblSubtitulo.setFont(new Font("Segoe UI", 0, 12));
        lblSubtitulo.setForeground(new Color(147, 197, 253));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setText("HealthPharmacy");
        panelMarca.add(lblSubtitulo, BorderLayout.PAGE_END);

        panelSidebar.add(panelMarca, BorderLayout.PAGE_START);

        // Panel de Navegación
        panelNavegacion.setBackground(azulSidebar);
        panelNavegacion.setLayout(new java.awt.GridLayout(4, 1, 0, 4));
        panelNavegacion.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Botón Dashboard
        btnDashboard.setBackground(azulSidebarActivo);
        btnDashboard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDashboard.setLayout(new BorderLayout(14, 0));
        btnDashboard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(14, 16, 14, 16),
            BorderFactory.createLineBorder(azulSidebarActivo, 2)
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
                if (panelActivo != btnDashboard) {
                    btnDashboard.setBackground(azulSidebarHover);
                }
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                if (panelActivo != btnDashboard) {
                    btnDashboard.setBackground(azulSidebar);
                }
            }
        });

        lblIconDashboard.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconDashboard.setForeground(blanco);
        lblIconDashboard.setText("🏠");
        lblIconDashboard.setFont(new Font("Segoe UI Emoji", 0, 20));
        btnDashboard.add(lblIconDashboard, BorderLayout.WEST);

        lblDashboard.setFont(new Font("Segoe UI Semibold", 1, 14));
        lblDashboard.setForeground(blanco);
        lblDashboard.setText("Dashboard");
        btnDashboard.add(lblDashboard, BorderLayout.CENTER);

        panelNavegacion.add(btnDashboard);

        // Botón Ventas
        btnVentas.setBackground(azulSidebar);
        btnVentas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVentas.setLayout(new BorderLayout(14, 0));
        btnVentas.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(14, 16, 14, 16),
            BorderFactory.createLineBorder(azulSidebar, 2)
        ));
        btnVentas.addMouseListener(new MouseAdapter() {
            @Override
            // Metodo mouseClicked: logica de interfaz asociada a este formulario/panel.
            public void mouseClicked(MouseEvent e) {
                seleccionarBoton(btnVentas);
                cambiarVista("ventas");
            }
            @Override
            // Metodo mouseEntered: logica de interfaz asociada a este formulario/panel.
            public void mouseEntered(MouseEvent e) {
                if (panelActivo != btnVentas) {
                    btnVentas.setBackground(azulSidebarHover);
                }
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                if (panelActivo != btnVentas) {
                    btnVentas.setBackground(azulSidebar);
                }
            }
        });

        lblIconVentas.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconVentas.setForeground(blanco);
        lblIconVentas.setText("💰");
        lblIconVentas.setFont(new Font("Segoe UI Emoji", 0, 20));
        btnVentas.add(lblIconVentas, BorderLayout.WEST);

        lblVentas.setFont(new Font("Segoe UI Semibold", 1, 14));
        lblVentas.setForeground(new Color(147, 197, 253));
        lblVentas.setText("Registrar Venta");
        btnVentas.add(lblVentas, BorderLayout.CENTER);

        panelNavegacion.add(btnVentas);

        // Botón Inventario
        btnInventario.setBackground(azulSidebar);
        btnInventario.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnInventario.setLayout(new BorderLayout(14, 0));
        btnInventario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(14, 16, 14, 16),
            BorderFactory.createLineBorder(azulSidebar, 2)
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
                if (panelActivo != btnInventario) {
                    btnInventario.setBackground(azulSidebarHover);
                }
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                if (panelActivo != btnInventario) {
                    btnInventario.setBackground(azulSidebar);
                }
            }
        });

        lblIconInventario.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconInventario.setForeground(blanco);
        lblIconInventario.setText("📦");
        lblIconInventario.setFont(new Font("Segoe UI Emoji", 0, 20));
        btnInventario.add(lblIconInventario, BorderLayout.WEST);

        lblInventario.setFont(new Font("Segoe UI Semibold", 1, 14));
        lblInventario.setForeground(new Color(147, 197, 253));
        lblInventario.setText("Consultar Inventario");
        btnInventario.add(lblInventario, BorderLayout.CENTER);

        panelNavegacion.add(btnInventario);

        // Botón Salir
        btnSalir.setBackground(azulSidebar);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.setLayout(new BorderLayout(14, 0));
        btnSalir.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(14, 16, 14, 16),
            BorderFactory.createLineBorder(azulSidebar, 2)
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
                btnSalir.setBackground(new Color(185, 28, 28));
                lblIconSalir.setForeground(blanco);
                lblSalir.setForeground(blanco);
            }
            @Override
            // Metodo mouseExited: logica de interfaz asociada a este formulario/panel.
            public void mouseExited(MouseEvent e) {
                btnSalir.setBackground(azulSidebar);
                lblIconSalir.setForeground(blanco);
                lblSalir.setForeground(new Color(252, 165, 165));
            }
        });

        lblIconSalir.setHorizontalAlignment(SwingConstants.CENTER);
        lblIconSalir.setForeground(blanco);
        lblIconSalir.setText("🚪");
        lblIconSalir.setFont(new Font("Segoe UI Emoji", 0, 20));
        btnSalir.add(lblIconSalir, BorderLayout.WEST);

        lblSalir.setFont(new Font("Segoe UI Semibold", 1, 14));
        lblSalir.setForeground(new Color(252, 165, 165));
        lblSalir.setText("Cerrar Sesión");
        btnSalir.add(lblSalir, BorderLayout.CENTER);

        panelNavegacion.add(btnSalir);

        panelSidebar.add(panelNavegacion, BorderLayout.CENTER);

        panelPrincipal.add(panelSidebar, BorderLayout.LINE_START);

        // Panel de Contenido Principal
        panelContenido.setBackground(blancoGris);
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
    }// </editor-fold>

    // Variables declaration - do not modify
    private javax.swing.JPanel btnDashboard;
    private javax.swing.JPanel btnInventario;
    private javax.swing.JPanel btnSalir;
    private javax.swing.JPanel btnVentas;
    private javax.swing.JLabel lblDashboard;
    private javax.swing.JLabel lblIconDashboard;
    private javax.swing.JLabel lblIconInventario;
    private javax.swing.JLabel lblIconSalir;
    private javax.swing.JLabel lblIconVentas;
    private javax.swing.JLabel lblInventario;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblSalir;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblVentas;
    private javax.swing.JPanel panelContenido;
    private javax.swing.JPanel panelMarca;
    private javax.swing.JPanel panelNavegacion;
    private javax.swing.JPanel panelPrincipal;
    private javax.swing.JPanel panelSidebar;
    // End of variables declaration
}
