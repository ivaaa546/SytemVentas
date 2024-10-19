/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;


import Datos.Interfaces.PaginadoInterface;
import Entidades.Persona;
import database.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author pc
 */
public class PersonaDao implements PaginadoInterface<Persona> {
    //declaramos una constante para instancia a la clase conexion
    private final Conexion CON;
    
    //creamos un objeto de tipo Statement o Preparedstatement
    private PreparedStatement ps;
    
    //creamos un objeto de tipo resultset
    private ResultSet rs;
    
    //creamos una variable tipo booleana
    private boolean resp;
    
    //creamos un constructor para generar la instancia a la clase conexion
    public PersonaDao(){
        CON = Conexion.getInstancia();
    }       

    @Override
    public List<Persona> listar(String texto, int totalRegPagina, int numPagina) {
        //creamos un objeto de tipo lista
        List<Persona> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("Select p.id, p.tipo_persona, p.nombre, p.tipo_documento, p.num_documento, p.direccion, p.telefono, p.email, p.activo from persona p where p.nombre Like ? order by p.id asc Limit ?, ?");//*
            ps.setString(1, "%" + texto + "%");
            ps.setInt(2, (numPagina - 1) * totalRegPagina);
            ps.setInt(3, totalRegPagina);//*
            rs = ps.executeQuery();
            while(rs.next()){
                registros.add(new Persona(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), 
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getBoolean(9)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return registros;
    }
    
    public List<Persona> listarTipo(String texto, int totalRegPagina, int numPagina, String tipoPersona) {
        //creamos un objeto de tipo lista
        List<Persona> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("Select p.id, p.tipo_persona, p.nombre, p.tipo_documento, p.num_documento, p.direccion, p.telefono, p.email, p.activo from persona p where p.nombre Like ? and tipo_persona = ? order by p.id asc Limit ?, ?");//*
            ps.setString(1, "%" + texto + "%");
            ps.setString(2, tipoPersona);
            ps.setInt(3, (numPagina - 1) * totalRegPagina);
            ps.setInt(4, totalRegPagina);//*
            rs = ps.executeQuery();
            while(rs.next()){
                registros.add(new Persona(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), 
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getBoolean(9)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return registros;
    }
    
   
    @Override
    public boolean insertar(Persona obj) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Insert into persona (tipo_persona, nombre, tipo_documento, num_documento, direccion, "
                    + "telefono, email, activo) values (?, ?, ?, ?, ?, ?, ?, 1)");
            ps.setString(1, obj.getTipoPersona());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipoDoc());
            ps.setString(4, obj.getNumDoc());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            
            
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();//Puede dar error
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    }

   @Override
    public boolean actualizar(Persona obj) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update persona set tipo_persona = ?, nombre = ?, tipo_documento = ?,"
                    + "num_documento = ?, direccion = ?, telefono = ?, email = ? where id = ?");
            ps.setString(1, obj.getTipoPersona());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipoDoc());
            ps.setString(4, obj.getNumDoc());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail()); 
            ps.setInt(8, obj.getId());
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;//1.30.48
    }

    @Override
    public boolean desactivar(int id) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update persona set activo = 0 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    }

    @Override
    public boolean activar(int id) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update persona set activo = 1 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    }

    @Override
    public int total() {
       int totalRegistros = 0;
        try {
            ps = CON.Conectar().prepareStatement("Select count(id) from persona");
            rs = ps.executeQuery();
            while(rs.next()){
                totalRegistros = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return totalRegistros;
    }

    @Override
    public boolean existe(String texto) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Select nombre from persona where nombre = ?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            rs.last();
            if(rs.getRow() > 0){
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(PersonaDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return resp;
    }
    
    
    
}
