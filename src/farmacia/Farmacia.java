/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package farmacia;


import inventario.inventario;
import roles.Empleado;

import java.util.List;

public class Farmacia {
    private static Farmacia instancia;
    private String nombre;
    private inventario inventario;
    private List<Empleado> empleados;

    private Farmacia() { } // constructor privado

    public static Farmacia getInstance() {
        if (instancia == null) {
            instancia = new Farmacia();
        }
        return instancia;
    }
}
