/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.PersonaDao;
import Datos.RolDao;
import Datos.UsuarioDao;
import Entidades.Persona;
import Entidades.Rol;
import Entidades.Usuario;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class PersonaControl {
    //declaramos nuestras variables
    private final PersonaDao DATOS;
    private Persona obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    //creamos el constructor
    public PersonaControl(){
        this.DATOS = new PersonaDao();
        this.obj = new Persona();
        this.registrosMostrados = 0;
    }
    
  
    //metodo para obtener la lista de la tabla usuario
    public DefaultTableModel listarTipo(String texto, int totalRegPagina, int numPagina, String tipoPersona){
        List<Persona> lista = new ArrayList();
        lista.addAll(DATOS.listarTipo(texto, totalRegPagina, numPagina, tipoPersona));
        String[] titulos = {"Id", "Tipo Persona", "Persona", "Documento", "Num Documento", "Direccion", "Telefono", "Email", "Estados"};
        this.modeloTabla = new DefaultTableModel(null,titulos);
        
        //recorrer los items de la lista para llenar la tabla
        String estado;
        String[] registro = new String[9];
        this.registrosMostrados = 0;
        
        for(Persona item:lista){
            if(item.isActivo()){
                estado = "Activo";
            }else{
                estado = "Inactivo";
            }
            registro[0] = Integer.toString(item.getId());
            registro[1] = item.getTipoPersona();
            registro[2] = item.getNombre();
            registro[3] = item.getTipoDoc();
            registro[4] = item.getNumDoc();
            registro[5] = item.getDireccion();
            registro[6] = item.getTelefono();
            registro[7] = item.getEmail();
            registro[8] = estado;
            this.modeloTabla.addRow(registro);
            this.registrosMostrados = this.registrosMostrados + 1;
        }
        return this.modeloTabla;//51.31
    }
    
    
    //metodo para insertar
    public String insertar(String tipoPersona, String nombre, String tipoDocumento, String numDocumento, String Direccion,
            String Telefono, String Email){
        if(DATOS.existe(nombre)){
            return "El registro ya existe";
        }else{
            obj.setTipoPersona(tipoPersona);
            obj.setNombre(nombre);
            obj.setTipoDoc(tipoDocumento);
            obj.setNumDoc(numDocumento);
            obj.setDireccion(Direccion);
            obj.setTelefono(Telefono);
            obj.setEmail(Email);
            
            if(DATOS.insertar(obj)){
                return "OK";
            }else{
                return "Error en el registro.";
            }
        }
    } 
    //metodo para actualizar
    public String actualizar(int id, String tipoPersona, String nombre, String nombreAnt, String tipoDocumento, String numDocumento, String Direccion,
            String Telefono, String Email){
        if(nombre.equals(nombreAnt)){
            obj.setId(id);
            obj.setTipoPersona(tipoPersona);
            obj.setNombre(nombre);
            obj.setTipoDoc(tipoDocumento);
            obj.setNumDoc(numDocumento);
            obj.setDireccion(Direccion);
            obj.setTelefono(Telefono);
            obj.setEmail(Email);
            
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
                obj.setTipoPersona(tipoPersona);
                obj.setNombre(nombre);
                obj.setTipoDoc(Telefono);
                obj.setNumDoc(numDocumento);
                obj.setDireccion(Direccion);
                obj.setTelefono(Telefono);
                obj.setEmail(Email);

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
    }
    
    //metodos para controlar el total de los registros
    public int total(){
        return DATOS.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
    
   
    
}
