/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.ventanas;

import java.awt.Component;
import java.awt.MenuItem;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import models.menuModel;

/**
 *
 * @author Miguel
 * @param <E>
 */
public class listMenu<E extends Object> extends JList<E>{
    private final DefaultListModel model;
    public listMenu(){
        model = new DefaultListModel();
        setModel(model);
    }
    
    @Override
    public ListCellRenderer<? super E> getCellRenderer(){
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
                menuModel data;
                if (o instanceof menuModel) {
                    data = (menuModel) o;
                } else {
                    data = new menuModel("", o + "", menuModel.MenuType.EMPTY);
                }
                menuItem item = new menuItem(data);
                
                return item;
            }
        };
    }
    
}
