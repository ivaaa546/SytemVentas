/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Datos.Interfaces.InterfaceSimple;
import Datos.Interfaces.PaginadoInterface;
import Entidades.Articulo;
import Entidades.Categoria;
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
public class ArticuloDao implements PaginadoInterface<Articulo> {
    //declaramos una constante para instancia a la clase conexion
    private final Conexion CON;
    
    //creamos un objeto de tipo Statement o Preparedstatement
    private PreparedStatement ps;
        
    //creamos un objeto de tipo resultset
    private ResultSet rs;
    
    //creamos una variable tipo booleana
    private boolean resp;
    //creamos un constructor para generar la instancia a la clase conexion
    public ArticuloDao(){
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Articulo> listar(String texto, int totalRegPagina, int numPagina) {
        //creamos un objeto de tipo lista
        List<Articulo> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("Select a.id, a.categoria_id, c.nombre as categoria_nombre, a.codigo, a.nombre,"
                    + "a.precio_venta, a.stock, a.descripcion, a.imagen, a.ativo from articulo a inner join categoria c on a.categoria_id = c.id"
                    + " where a.nombre Like ? order by a.id asc Limit ?, ?");//*
            ps.setString(1, "%" + texto + "%");
            ps.setInt(2, (numPagina - 1) * totalRegPagina);
            ps.setInt(3, totalRegPagina);//*
            rs = ps.executeQuery();
            while(rs.next()){
                registros.add(new Articulo(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), 
                        rs.getDouble(6), rs.getInt(7), rs.getString(8), rs.getString(9),  rs.getBoolean(10)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return registros;
    }
    
    //metodo para obtener el codigo del articulo
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
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return art;
    }
    
    
    
    
    
    
    @Override
    public boolean insertar(Articulo obj) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Insert into articulo (categoria_id, codigo, nombre, precio_venta, stock,"
                    + "descripcion, imagen, ativo) values (?, ?, ?, ?, ?, ?, ?, 1)");
            ps.setInt(1, obj.getCategoriaId());
            ps.setString(2, obj.getCodigo());
            ps.setString(3, obj.getNombre());
            ps.setDouble(4, obj.getPrecioVenta());
            ps.setInt(5, obj.getStock());
            ps.setString(6, obj.getDescripcion());
            ps.setString(7, obj.getImagen());
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();//Puede dar error
        } catch (SQLException ex) {
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    }

    @Override
    public boolean actualizar(Articulo obj) {
       resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update articulo set categoria_id = ?, codigo = ?, nombre = ?,"
                    + "precio_venta = ?, stock = ?, descripcion = ?, imagen = ? where id = ?");
            ps.setInt(1, obj.getCategoriaId());
            ps.setString(2, obj.getCodigo());
            ps.setString(3, obj.getNombre());
            ps.setDouble(4, obj.getPrecioVenta());
            ps.setInt(5, obj.getStock());
            ps.setString(6, obj.getDescripcion());
            ps.setString(7, obj.getImagen());
            ps.setInt(8, obj.getId());
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Update articulo set ativo = 0 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Update articulo set ativo = 1 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Select count(id) from articulo");
            rs = ps.executeQuery();
            while(rs.next()){
                totalRegistros = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Select nombre from articulo where nombre = ?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            rs.last();
            if(rs.getRow() > 0){
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(ArticuloDao.class.getName()).log(Level.SEVERE, null, ex);
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
