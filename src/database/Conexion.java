package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Maneja la conexión a MySQL usando patrón Singleton.
 * Centraliza credenciales y apertura/cierre de conexión para los DAO.
 */
public class Conexion {
    private final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3308/";
    private final String DB = "proyecto_farmacia";
    private final String USER = "root";
    private final String PASSWORD = "";



    public Connection cadena;
    public static Conexion instancia;

    // Constructor privado de facto para controlar la instancia única.
    public Conexion(){
        this.cadena = null;

    }

    // Abre conexión contra la base configurada y la retorna al DAO solicitante.
    public Connection conectar(){
        try {
            Class.forName(DRIVER);
            this.cadena = DriverManager.getConnection(URL+DB,USER,PASSWORD);

        } catch (ClassNotFoundException |SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return this.cadena;
    }
    // Cierra la conexión actual asociada al singleton.
    public void desconectar(){
        try {
            this.cadena.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

    }
    // Retorna la instancia única compartida por toda la capa data.
    public synchronized static Conexion getInstancia(){
        if(instancia==null){
            instancia = new Conexion();
        }
        return instancia;
    }

}
