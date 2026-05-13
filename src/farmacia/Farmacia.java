/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package farmacia;


import inventario.LoteInventario;
import java.util.ArrayList;
import roles.Empleado;

import java.util.List;

/**
 * Entidad principal del dominio con estado global en memoria.
 * Usa Singleton para compartir lotes y empleados durante la ejecución.
 */
public class Farmacia {
    private static Farmacia instancia;
    private String nombre;
    private List<LoteInventario> lotes;
    private List<Empleado> empleados;

    // Inicializa las colecciones internas del dominio.
    private Farmacia() { 
         lotes = new ArrayList<>();
         empleados = new ArrayList<>();
    } // constructor privado

    // Retorna la única instancia disponible del agregado Farmacia.
    public static Farmacia getInstance() {
        if (instancia == null) {
            instancia = new Farmacia();
        }
        return instancia;
    }
    // Agrega un empleado a la colección de trabajo en memoria.
    public void agregarEmpleado(Empleado empleado){
        empleados.add(empleado);
    
    }
    // Agrega un lote al inventario en memoria.
    public void agregarLote(LoteInventario lote){
         lotes.add(lote);
    
    }
    
}
