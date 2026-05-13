package data.interfaces;

import java.util.List;

/**
 * CrudSimpleInterface: interfaz del proyecto HealthPharmacy.
 */
public interface CrudSimpleInterface<T> {
    public List<T> listar(String texto);
    public boolean insertar (T obj);
    public boolean actualizar(T obj);
    public int total();
    public boolean existe(String texto);
}
