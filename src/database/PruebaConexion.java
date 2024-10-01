/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author pc
 */
public class PruebaConexion {
    public static void main(String[] args){
        Conexion con = Conexion.getInstancia();
        con.Conectar();
        if(con.cadena != null){
            System.out.println("Conectado");
        }else{
            System.out.println("Desconectado");
        }
    }
}
    

