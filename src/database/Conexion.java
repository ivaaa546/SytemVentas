/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */ //ivan
public class Conexion {
    private final String DRIVER = "com.mysql.jdbc.Driver";
    private final String URL = "jdbc:mysql://localhost:3306/";//127.0.0.1
    private final String DB = "sistema_ventas";
    private final String USER = "root";
    private final String PASSWORD = "";
    
    public Connection cadena; //devuelve cadena de conexion//ivan
    public static Conexion instancia; //permite obtener la cadena
    //creamos un constructor |lo que devuelve contructor es lo que tiene cadena
    private Conexion()
    {
        this.cadena = null;
    }
    //metodo para conectar a la base de datos
    public Connection Conectar(){
        try {
            Class.forName(DRIVER);
            this.cadena = DriverManager.getConnection(URL + DB, USER, PASSWORD);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
             return this.cadena;
    }
    //Metodo para desconectarnos
    public void Desconectar()
    {
        try {
            this.cadena.close();
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
    
    //metodo para crear la cadena de conexion| activa el constructor con la cadena conexion
    public synchronized static Conexion getInstancia(){
        if(instancia == null){
            instancia = new Conexion();
        }
        return instancia;
    }   
}
