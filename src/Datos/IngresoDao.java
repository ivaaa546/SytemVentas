/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

import Datos.Interfaces.CrudIngresoInterface;
import Entidades.DetalleIngreso;
import Entidades.Ingreso;
import com.mysql.jdbc.Statement;
import database.Conexion;
import java.sql.Connection;
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
public class IngresoDao  implements CrudIngresoInterface<Ingreso, DetalleIngreso> {
    //declaramos una constante para instancia a la clase conexion
    private final Conexion CON;
    
    //creamos un objeto de tipo Statement o Preparedstatement
    private PreparedStatement ps;
    
    //creamos un objeto de tipo resultset
    private ResultSet rs;
    
    //creamos una variable tipo booleana
    private boolean resp;
    
    //creamos un constructor para generar la instancia a la clase conexion
    public IngresoDao(){
        CON = Conexion.getInstancia();
    }

    @Override
    public List<Ingreso> listar(String texto, int totalRegPagina, int numPagina) {
        //creamos un objeto de tipo lista
        List<Ingreso> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("Select i.id, i.usuario_id, u.nombre as usuario_nombre, i.persona_id, p.nombre as persona_nombre, i.tipo_comprobante, i.serie_comprobante, i.num_comproante, i.fecha, i.impuesto, i.total, i.estado from ingreso i inner join persona p on i.persona_id = p.id inner join usuario u on i.usuario_id = u.id"
                    + " where i.num_comproante Like ? order by i.id asc Limit ?, ?");//*
            ps.setString(1, "%" + texto + "%");
            ps.setInt(2, (numPagina - 1) * totalRegPagina);
            ps.setInt(3, totalRegPagina);//*
            rs = ps.executeQuery();
            while(rs.next()){
                registros.add(new Ingreso(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getInt(4), rs.getString(5), 
                        rs.getString(6), rs.getString(7), rs.getString(8), rs.getDate(9),  rs.getDouble(10), rs.getDouble(11),
                        rs.getString(12)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex);
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
    public List<DetalleIngreso> listarDetalle(int id) {
        //creamos un objeto de tipo lista
        List<DetalleIngreso> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("select a.id, a.codigo, a.nombre, d.cantidad, d.precio, (d.cantidad * precio) as sub_total from detalle_ingreso d inner join articulo a on d.articulo_id = a.id where d.ingreso_id = ?");//*
            ps.setInt(1, id);
            
            rs = ps.executeQuery();
            while(rs.next()){
                registros.add(new DetalleIngreso(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getDouble(5),
                rs.getDouble(6)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean insertar(Ingreso obj) {
        resp = false;
        Connection conn = null;
        try {
            conn = CON.Conectar();
            conn.setAutoCommit(false);
            String sqlInsertIngreso = "Insert into ingreso (persona_id, usuario_id, fecha, tipo_comprobante, serie_comprobante, num_comproante, impuesto, total, estado)"
                    +" values (?, ?, now(), ?, ?, ?, ?, ?, ?)";
            ps = CON.Conectar().prepareStatement(sqlInsertIngreso, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, obj.getPersonaId());
            ps.setInt(2, obj.getUsuarioId());
            ps.setString(3, obj.getTipoComprobante());
            ps.setString(4, obj.getSerieComprobante());
            ps.setString(5, obj.getNumComprobante());
            ps.setDouble(6, obj.getImpuesto());
            ps.setDouble(7, obj.getTotal());
            ps.setString(8, "Aceptado");
            
            //realizar el proceso de lectura o envio de informacion a la clase detalle ingreso
            int filasAfectadas = ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            int idGenerado = 0;
            if(rs.next()){
                idGenerado = rs.getInt(1);
            }
            if(filasAfectadas == 1){
                String sqlInsertDetalle = "insert into detalle_ingreso(ingreso_id, articulo_id, cantidad, precio) values (?, ?, ?, ?)";
                //usar la nueva conexion para generar esta insersion
                ps = conn.prepareStatement(sqlInsertDetalle);
                
                //recorrer el resulset para obtener los diferentes detalles de ingreso
                for(DetalleIngreso Item:obj.getDetalles()){
                    ps.setInt(1, idGenerado);
                    ps.setInt(2, Item.getArticuloId());
                    ps.setInt(3, Item.getCantidad());
                    ps.setDouble(4, Item.getPrecio());
                    resp = ps.executeUpdate() > 0;
                }
                conn.commit();
            }else{
                conn.rollback();
            }
        } catch (SQLException ex) {
                try {
                    if(conn != null){
                    conn.rollback();
                    }
                } catch (SQLException ex1) {
                    Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex1);
                }
            Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            try {
                if(rs != null) rs.close();
                if(ps != null) rs.close();
                if(conn != null) rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return resp;
    }

    @Override
    public boolean anular(int id) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update ingreso set estado = 'Anulado' where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex);
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
            ps = CON.Conectar().prepareStatement("Select count(id) from ingreso");
            rs = ps.executeQuery();
            while(rs.next()){
                totalRegistros = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex);
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
    public boolean existe(String texto, String texto2) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Select id from ingreso where serie_comprobante = ? and num_comproante = ?");
            ps.setString(1, texto);
            ps.setString(2, texto2);
            rs = ps.executeQuery();
            rs.last();
            if(rs.getRow() > 0){
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(IngresoDao.class.getName()).log(Level.SEVERE, null, ex);
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
