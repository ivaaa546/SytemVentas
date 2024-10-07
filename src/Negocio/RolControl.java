/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.RolDao;

import Entidades.Rol;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class RolControl {
    private final RolDao DATOS;
    private Rol obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    //creamos el constructor
    public RolControl(){
        this.DATOS = new RolDao();
        this.obj = new Rol();
        this.registrosMostrados = 0;
    }
    
    //metodo para obtener la lista de la tabla rol
    public DefaultTableModel listar(){
    List<Rol> lista = new ArrayList();
    lista.addAll(DATOS.listar());
    String[] titulos = {"Id", "Nombre", "Descripcion",};
    this.modeloTabla = new DefaultTableModel(null,titulos);
    String estado;
        String[] registro = new String[3];
        this.registrosMostrados = 0;
        
        for(Rol item:lista){
            
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getNombre();
            registro[2] = item.getDescripcion();
            
            this.modeloTabla.addRow(registro);
            this.registrosMostrados = this.registrosMostrados + 1;
        }
        return this.modeloTabla;
    }
    
   //Metodos que se utilizarían si es que se hace un formulario para insertar datos, por ahora se trabajará con informacion pre cargada
    /*
//metodo para insertar
    public String insertar(String nombre, String descripcion){
        if(DATOS.existe(nombre)){
            return "El registro ya existe";
        }else{
            obj.setNombre(nombre);
            obj.setDescripcion(descripcion);
            if(DATOS.insertar(obj)){
                return "OK";
            }else{
                return "Error en el registro.";
            }
        }
    } 
    //metodo para actualizar
    public String actualizar(int id, String nombre, String nombreAnt, String descripcion){
        if(nombre.equals(nombreAnt)){
            obj.setId(id);
            obj.setNombre(nombre);
            obj.setDescripcion(descripcion);
            if(DATOS.actualizar(obj)){
                return "OK";
            }else{
                return "Error en la actualizacion.";
            }
        }else{
            if(DATOS.existe(nombre)){
            return "El registro ya existe";
            }else{
                obj.setId(id);
                obj.setNombre(nombre);
                obj.setDescripcion(descripcion);
                if(DATOS.actualizar(obj)){
                    return "OK";
                }else{
                return "Error en la actualizacion.";
                }
            }
        }
    }
    //metodo para desactivar los registros
    public String desactivar(int id){
        if(DATOS.desactivar(id)){
            return "OK";
        }else{
            return "No se puede desactivar el registro.";
        }
    }
    
    //metodo para activar los registros
    public String activar(int id){
        if(DATOS.activar(id)){
            return "OK";
        }else{
            return "No se puede activar el registro.";
        }
    } */
    
    //metodos para el total de registros almacenados en la tabla rol
    public int total(){
        return DATOS.total();
    }
    
    //metodo para mostrar el total de los registros mostrados en el sistema
    public int totalMostrados(){
        return this.registrosMostrados;
    }
}
