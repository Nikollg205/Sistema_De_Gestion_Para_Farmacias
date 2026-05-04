/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package farmacia;


import inventario.LoteInventario;
import java.util.ArrayList;
import roles.Empleado;

import java.util.List;

public class Farmacia {
    private static Farmacia instancia;
    private String nombre;
    private List<LoteInventario> lotes;
    private List<Empleado> empleados;

    private Farmacia() { 
         lotes = new ArrayList<>();
         empleados = new ArrayList<>();
    } // constructor privado

    public static Farmacia getInstance() {
        if (instancia == null) {
            instancia = new Farmacia();
        }
        return instancia;
    }
    public void agregarEmpleado(Empleado empleado){
        empleados.add(empleado);
    
    }
    public void agregarLote(LoteInventario lote){
         lotes.add(lote);
    
    }
    
}
