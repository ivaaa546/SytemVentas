/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;
import Entidades.Categoria;
import Entidades.Rol;
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
public class RolDao {
    //declaramos una constante para instancia a la clase conexion
    private final Conexion CON;
    //creamos un objeto de tipo Statement o Preparedstatement|prepara la bd pararecibir una consulta
    //insert, update, select y lo envia a bd
    private PreparedStatement ps;
    
    //creamos un objeto de tipo resultset, sirve para gener una tabla en memoria
    //almacena la informacion obtenida con un select en una tabla en memoria
    private ResultSet rs;
    
    //creamos una variable tipo booleana
    private boolean resp;
    
    //creamos un constructor para generar la instancia a la clase conexion
    public RolDao(){
        CON = Conexion.getInstancia();
    }
    
    
    
    public List<Rol> listar() {
        //obtiene la informacion en la base de datos en memoria
        //creamos un objeto de tipo lista
            List<Rol> registros = new ArrayList();
        try {
            ps = CON.Conectar().prepareStatement("Select * from rol");
            rs = ps.executeQuery();
            while(rs.next()){
                //cargamos en la lista registros la informacion de la base de datos que se envia a la entidad rol
                //los valores de rs.getInt(1), es la posicion del id en la tabla rol, rs.getString(2) es el nombre y asi sucesivamente
                registros.add(new Rol(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(RolDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return registros;
    }
    
    //metodo para obtener el id y el nombre del rol
        public List<Rol> seleccionar() {
        //creamos un objeto de tipo lista
        List<Rol> registros = new ArrayList();
        
        try {
            ps = CON.Conectar().prepareStatement("Select id, nombre from rol order by nombre asc");
            rs = ps.executeQuery();
            
            // Agregar la opción "Seleccione una opción" al inicio de la lista
            registros.add(new Rol(0, "Seleccione una opción"));
            
            while(rs.next()){
                registros.add(new Rol(rs.getInt(1), rs.getString(2)));
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
    
        //Metodos que se utilizarían si es que se hace un formulario para insertar datos, por ahora se trabajará con informacion pre cargada

   /* public boolean insertar(Categoria obj) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Insert into categoria (nombre, descripcion, activo) values (?, ?, 1)");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();//Puede dar error
        } catch (SQLException ex) {
            Logger.getLogger(RolDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    }


    public boolean actualizar(Categoria obj) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update categoria set nombre = ?, descripcion = ? where id = ?");
            ps.setString(1, obj.getNombre());
            ps.setString(2, obj.getDescripcion());
            ps.setInt(3, obj.getId());
            if(ps.executeUpdate()>0){
                resp = true;
            }
            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(RolDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;//1.30.48
    }


    public boolean desactivar(int id) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update categoria set activo = 0 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RolDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    }

    public boolean activar(int id) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Update categoria set activo = 1 where id = ?");
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                resp = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(RolDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            CON.Desconectar();
        }
        return resp;
    } */


    public int total() {
        int totalRegistros = 0;
        try {
            ps = CON.Conectar().prepareStatement("Select count(id) from rol");
            rs = ps.executeQuery();
            while(rs.next()){
                totalRegistros = rs.getInt("COUNT(id)");
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(RolDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return totalRegistros;
    }

   /* public boolean existe(String texto) {
        resp = false;
        try {
            ps = CON.Conectar().prepareStatement("Select nombre from categoria where nombre = ?");
            ps.setString(1, texto);
            rs = ps.executeQuery();
            rs.last();
            if(rs.getRow() > 0){
                resp = true;
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(RolDao.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
        finally{
            ps = null;
            rs = null;
            CON.Desconectar();
        }
        return resp;
    }
*/
    
}
