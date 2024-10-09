/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.RolDao;
import Datos.UsuarioDao;
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
public class UsuarioControl {
    //declaramos nuestras variables
    private final UsuarioDao DATOS;
    private final RolDao DATOSROL;
    private Usuario obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    //creamos el constructor
    public UsuarioControl(){
        this.DATOS = new UsuarioDao();
        this.DATOSROL = new RolDao();
        this.obj = new Usuario();
    }
    
    
    //metodo para obtener la lista de la tabla usuario
    public DefaultTableModel listar(String texto, int totalRegPagina, int numPagina){
    List<Usuario> lista = new ArrayList();
    lista.addAll(DATOS.listar(texto, totalRegPagina, numPagina));
    String[] titulos = {"Id", "Rol Id", "Rol", "Usuario", "Documento", "Num Documento", "Direccion", "Telefono", "Email", "Clave", "Estado"};
        this.modeloTabla = new DefaultTableModel(null,titulos);
        //recorrer los items de la lista para llenar la tabla
        String estado;
        String[] registro = new String[11];
        this.registrosMostrados = 0;
        
        for(Usuario item:lista){
            if(item.isActivo()){
                estado = "Activo";
            }else{
                estado = "Inactivo";
            }
            registro[0] = Integer.toString(item.getId());
            registro[1] = Integer.toString(item.getRolid());
            registro[2] = item.getRolNombre();
            registro[3] = item.getNombre();
            registro[4] = item.getTipoDocumento();
            registro[5] = item.getNumDocumento();
            registro[6] = item.getDireccion();
            registro[7] = item.getTelefono();
            registro[8] = item.getEmail();
            registro[9] = item.getClave();
            registro[10] = estado;
            this.modeloTabla.addRow(registro);
            this.registrosMostrados = this.registrosMostrados + 1;
        }
        return this.modeloTabla;//51.31
    
    }
    
    //método para poder llenar el cuadro combinado con los datos de la tabla roles
    //metodo para llenar el cuadro combinado
    public DefaultComboBoxModel seleccionar(){
        DefaultComboBoxModel items = new DefaultComboBoxModel();
        List<Rol> lista = new ArrayList();
        lista = DATOSROL.seleccionar();
        for(Rol item:lista){
            items.addElement(new Rol(item.getId(), item.getNombre()));
        }
        return items;
    }
    
    //metodo para encriptar la clave
    private static String encriptar(String valor)
    {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UsuarioControl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        byte [] hash = md.digest(valor.getBytes());
        StringBuilder sb = new StringBuilder();
        for(byte b: hash) 
        {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
        
    }
    
    //metodo para insertar valores
    public String insertar(int RolId, String nombre, String tipoDocumento, String numDocumento, String Direccion, String telefono,
            String email, String clave){
        if(DATOS.existe(email)){
            return "El registro ya existe";
        }else{
            obj.setRolid(RolId);
            obj.setNombre(nombre);
            obj.setTipoDocumento(tipoDocumento);
            obj.setNumDocumento(numDocumento);
            obj.setDireccion(Direccion);
            obj.setTelefono(telefono);
            obj.setEmail(email);
            obj.setClave(this.encriptar(clave));
            if(DATOS.insertar(obj)){
                return "OK";
            }else{
                return "Error en el registro.";
            }
        }
    }
    //metodo para actualizar
    public String actualizar(int id, int RolId, String nombre, String tipoDocumento, String numDocumento, String Direccion, String telefono,
            String email, String emailAnt, String clave){
        if(email.equals(emailAnt)){
            
            obj.setId(id);
            obj.setRolid(RolId);
            obj.setNombre(nombre);
            obj.setTipoDocumento(tipoDocumento);
            obj.setNumDocumento(numDocumento);
            obj.setDireccion(Direccion);
            obj.setTelefono(telefono);
            obj.setEmail(email);
            //encriptar la clave solamente si el usuario la modifico
            String encriptado;
            //revisar si el usuario cambio su clave, esto a traves del largo de la cadena
            if(clave.length()== 64)
            {
                encriptado = clave;
            }
            else
            {
                encriptado = this.encriptar(clave);
            }
            
            obj.setClave(encriptado);
            
            if(DATOS.actualizar(obj)){
                return "OK";
            }else{
                return "Error en la actualizacion.";
            }
        }else{
            if(DATOS.existe(email)){
            return "El registro ya existe";
            }else{
                obj.setId(id);    
                obj.setRolid(RolId);
                obj.setNombre(nombre);
                obj.setTipoDocumento(tipoDocumento);
                obj.setNumDocumento(numDocumento);
                obj.setDireccion(Direccion);
                obj.setTelefono(telefono);
                obj.setEmail(email);
                //encriptar la clave solamente si el usuario la modifico
                String encriptado;
                //revisar si el usuario cambio su clave, esto a traves del largo de la cadena
                if(clave.length()== 64)
                {
                    encriptado = clave;
                }
                else
                {
                    encriptado = this.encriptar(clave);
                }

                obj.setClave(encriptado);
            
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
    
    //metodo para mandarle los datos al metodo login
    public String login(String email, String clave){
        String resp = "0";
        Usuario u = this.DATOS.login(email, this.encriptar(clave));
        if(u != null){
            if(u.isActivo()){
                Variables.usuarioId = u.getId();
                Variables.rolId = u.getRolid();
                Variables.rolNombre = u.getRolNombre();
                Variables.usuarioNombre = u.getNombre();
                Variables.usuarioEmail = u.getEmail();
                
                resp = "1";
            }else{
                resp = "2"; //1.15.00
            }
        }
        return resp;
    }
    
}
