/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;


import Datos.Interfaces.PaginadoInterface;
import Entidades.Usuario;
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
public class UsuarioDao implements PaginadoInterface<Usuario> {
    //declaramos una constante para instancia a la clase conexion
    private final Conexion CON;
    
    //creamos un objeto de tipo Statement o Preparedstatement
    private PreparedStatement ps;
        
    //creamos un objeto de tipo resultset
    private ResultSet rs;
    
    //creamos una variable tipo booleana
    private boolean resp;
    //creamos un constructor para generar la instancia a la clase conexion
    public UsuarioDao(){
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Usuario> listar(String texto, int totalRegPagina, int numPagina) {
        //creamos un objeto de tipo lista
        List<Usuario> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("Select u.id, u.rol_id, r.nombre, u.nombre, u.tipo_documento, "
                    + "u.num_documento, u.direccion, u.telefono, u.email, u.clave, u.activo from usuario u inner join rol r "
                    + "on u.rol_id = r.id where u.nombre Like ? order by u.id asc Limit ?, ?");//*
            ps.setString(1, "%" + texto + "%");
            ps.setInt(2, (numPagina - 1) * totalRegPagina);
            ps.setInt(3, totalRegPagina);//*
            rs = ps.executeQuery();
            while(rs.next()){
                //cargamos en la lista registros la informacion de la base de datos que se envia a la entidad usuario
                //los valores de rs.getInt(1), es la poicion del id en la tabla usuario. rs.getString(2) es el nombre y asi sucesivamente
                registros.add(new Usuario(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), 
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getBoolean(11)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return registros;
    }
    
    
    //Metodo que no se si sea necesario xdd
    
   /* //metodo para obtener el codigo del articulo
    public Articulo obtenerArticuloCodigoIngreso(String codigo){
        Articulo art = null;
        //creamos un objeto de tipo lista
        List<Articulo> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("Select id, codigo, nombre, precio_venta, stock from articulo"
                    + " where codigo = ?");//*
            ps.setString(1, codigo);
            rs = ps.executeQuery();
            if(rs.first()){
                art = new Articulo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getInt(5));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return art;
    }
*/
    
    
    
    
    
    
    @Override
    public boolean insertar(Usuario obj) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Insert into usuario (rol_id, nombre, tipo_documento, num_documento, direccion,"
                    + "telefono, email, clave, activo) values (?, ?, ?, ?, ?, ?, ?, ?, 1)");
            ps.setInt(1, obj.getRolid());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipoDocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setString(8, obj.getClave());
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();//Puede dar error
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Usuario obj) {
       resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update usuario set rol_id = ?, nombre = ?, tipo_documento = ?,"
                    + "num_documento = ?, direccion = ?, telefono = ?, email = ?, clave = ? where id = ?");
            ps.setInt(1, obj.getRolid());
            ps.setString(2, obj.getNombre());
            ps.setString(3, obj.getTipoDocumento());
            ps.setString(4, obj.getNumDocumento());
            ps.setString(5, obj.getDireccion());
            ps.setString(6, obj.getTelefono());
            ps.setString(7, obj.getEmail());
            ps.setString(8, obj.getClave());
            ps.setInt(9, obj.getId());
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Update usuario set activo = 0 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Update usuario set activo = 1 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Select count(id) from usuario");
            rs = ps.executeQuery();
            while(rs.next()){
                totalRegistros = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Select email from usuario where email = ?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            rs.last();
            if(rs.getRow() > 0){
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return resp;
    }
    
    public Usuario login(String email, String clave){
        Usuario u = null;
        try {
            ps = CON.Conectar().prepareStatement("Select u.id, u.rol_id, r.nombre as rol_nombre, u.nombre, u.tipo_documento,"
                    + " u.num_documento, u.direccion, u.telefono, u.email, u.activo from usuario u inner join rol r"
                    + " on u.rol_id = r.id"//id
                    + " where u.email = ? and clave = ?");//*
            ps.setString(1, email);
            ps.setString(2, clave);
            rs = ps.executeQuery();
            if(rs.first()){
                u = new Usuario(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), 
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getBoolean(10));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return u;
    }
    
}
