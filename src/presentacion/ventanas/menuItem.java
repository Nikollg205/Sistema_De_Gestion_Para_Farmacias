package presentacion.ventanas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import models.menuModel;

/**
 * menuItem: clase del proyecto HealthPharmacy.
 */
public class menuItem extends javax.swing.JPanel {

    private boolean selected;
    private String viewName;
    
    public menuItem(menuModel data) {
        initComponents();
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        this.viewName = data.getIcon();
        
        if (data.getType() == menuModel.MenuType.MENU) {
            lblIcon.setIcon(loadIcon(data.getIcon()));
            lblName.setText(data.getName());
            
            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    handleClick(data);
                }
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!selected) {
                        setBackground(new Color(255, 255, 255, 30));
                    }
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!selected) {
                        setBackground(new Color(0, 0, 0, 0));
                    }
                }
            });
        } else if (data.getType() == menuModel.MenuType.TITLE) {
            lblIcon.setText(data.getName());
            lblIcon.setFont(new Font("Segoe UI", 1, 12));
            lblName.setVisible(false);
        } else {
            lblName.setText(" ");
        }
    }
    
    private ImageIcon loadIcon(String iconName) {
        try {
            java.net.URL imgURL = getClass().getResource("/imagenes/" + iconName + ".png");
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                java.awt.Image img = icon.getImage().getScaledInstance(24, 24, java.awt.Image.SCALE_SMOOTH);
                return new ImageIcon(img);
            }
        } catch (Exception e) {
            // Icon not found
        }
        
        // Default icons based on name
        switch (iconName) {
            case "dashboard": return createTextIcon("🏠");
            case "ventas": return createTextIcon("💰");
            case "inventario": return createTextIcon("📦");
            case "logout": return createTextIcon("🚪");
            default: return createTextIcon("📄");
        }
    }
    
    private ImageIcon createTextIcon(String emoji) {
        BufferedImage image = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        g2d.setColor(Color.WHITE);
        g2d.drawString(emoji, 2, 20);
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private void handleClick(menuModel data) {
        String name = data.getName().toLowerCase();
        
        if (name.contains("sesión") || name.contains("sesion") || name.contains("logout")) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof formVentas) {
                ((formVentas) frame).mostrarConfirmacionSalida();
            }
        } else if (name.contains("dashboard")) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof formVentas) {
                ((formVentas) frame).cambiarVista("dashboard");
            }
        } else if (name.contains("venta")) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof formVentas) {
                ((formVentas) frame).cambiarVista("ventas");
            }
        } else if (name.contains("inventario")) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame instanceof formVentas) {
                ((formVentas) frame).cambiarVista("inventario");
            }
        }
    }
    
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        lblIcon = new javax.swing.JLabel();
        lblName = new javax.swing.JLabel();

        lblIcon.setForeground(new java.awt.Color(255, 255, 255));
        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        lblName.setForeground(new java.awt.Color(255, 255, 255));
        lblName.setText("Menu Name");
        lblName.setFont(new Font("Segoe UI", 0, 13));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(lblIcon)
                .addGap(18, 18, 18)
                .addComponent(lblName))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblIcon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblName, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );
    }// </editor-fold>
    
    @Override
    public void paintComponents(Graphics grphcs) {
        if (selected) {
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(255,255,255,80));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 5, 5);
        } 
        super.paintComponents(grphcs); 
    }

    // Variables declaration - do not modify
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblName;
    // End of variables declaration
}

