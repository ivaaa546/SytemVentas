/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Datos.Interfaces;

import java.util.List;
//lista de acciones es una interface
/**
 *
 * @author pc
 */
public interface InterfaceSimple<T> {
    
    public List<T> listar(String texto); //estructura a utilizar todas las clases para conectarse a las diferentes de las tablas
    public boolean insertar(T obj);
    public boolean actualizar(T obj);
    public boolean desactivar(int id);
    public boolean activar(int id);
    public int total();
    public boolean existe(String texto);
    //esos elementos se van a volver metodos
}
