/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Datos.ArticuloDao;
import Datos.IngresoDao;
import Entidades.Articulo;
import Entidades.DetalleIngreso;
import Entidades.Ingreso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author pc
 */
public class IngresoControl {
    //declaramos nuestras variables
    private final IngresoDao DATOS;
    private final ArticuloDao DATOSART;
    private Ingreso obj;
    private DefaultTableModel modeloTabla;
    public int registrosMostrados;
    
    //creamos el constructor
    public IngresoControl(){
        this.DATOS = new IngresoDao();
        this.DATOSART = new ArticuloDao();
        this.obj = new Ingreso();
        this.registrosMostrados = 0;
    }
     //metodo para obtener la lista de la tabla articulo
    public DefaultTableModel listar(String texto, int totalRegPagina, int numPagina){
        List<Ingreso> lista = new ArrayList();
        lista.addAll(DATOS.listar(texto, totalRegPagina, numPagina));
        String[] titulos = {"Id", "Usuario Id", "Usuario", "Proveedor Id", "Proveedor", "Tipo Comprobante", "Serie", "Num Comprobante", "Fecha", 
            "Impuesto", "Total", "Estado"};
        this.modeloTabla = new DefaultTableModel(null,titulos);
        
        //recorrer los items de la lista para llenar la tabla
        String estado;
        String[] registro = new String[12];
        this.registrosMostrados = 0;
        //vamos a definir el formato para la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        
        for(Ingreso item:lista){
            registro[0] = Integer.toString(item.getId());
            registro[1] = Integer.toString(item.getUsuarioId());
            registro[2] = item.getUsuarioNombre();
            registro[3] = Integer.toString(item.getPersonaId());
            registro[4] = item.getPersonaNombre();
            registro[5] = item.getTipoComprobante();
            registro[6] = item.getSerieComprobante();
            registro[7] = item.getNumComprobante();
            registro[8] = sdf.format(item.getFecha());
            registro[9] = Double.toString(item.getImpuesto());
            registro[10] = Double.toString(item.getTotal());
            registro[11] = item.getEstado();
            this.modeloTabla.addRow(registro);
            this.registrosMostrados = this.registrosMostrados + 1;
        }
        return this.modeloTabla;//51.31
    }
     //metodo para listar el detalle de ingresos
    public DefaultTableModel listarDetalle(int id) {
        List<DetalleIngreso> lista = new ArrayList();
        lista.addAll(DATOS.listarDetalle(id));
        String[] titulos = {"ID", "CODIGO", "ARTICULO", "CANTIDAD", "PRECIO", "SUBTOTAL"};
        this.modeloTabla = new DefaultTableModel(null, titulos);

        //recorrer los items de la lista para llenar la tabla
        String[] registro = new String[6];
        this.registrosMostrados = 0;
        //vamos a definir el formato para la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");

        for (DetalleIngreso item : lista) {
            registro[0] = Integer.toString(item.getArticuloId());
            registro[1] = item.getArticuloCodigo();
            registro[2] = item.getArticuloNombre();
            registro[3] = Integer.toString(item.getCantidad());
            registro[4] = Double.toString(item.getPrecio());
            registro[5] = Double.toString(item.getSubTotal());
            
            this.modeloTabla.addRow(registro);
        }
        return this.modeloTabla;//51.31
    }
    //metodo para ingresar los articulos por codigo
    public Articulo obtenerArticuloCodigoIngreso(String codigo){
        Articulo art = DATOSART.obtenerArticuloCodigoIngreso(codigo);
        return art;
    }
    
    //metodo para insertar
    public String insertar(int PersonaId, String tipoComprobante, String serieComprobante, String numComprobante, double impuesto, double total, 
            DefaultTableModel modeloDetalles){
        if(DATOS.existe(serieComprobante, numComprobante)){
            return "El registro ya existe";
        }else{
            obj.setUsuarioId(Variables.usuarioId);
            obj.setPersonaId(PersonaId);
            obj.setTipoComprobante(tipoComprobante);
            obj.setSerieComprobante(serieComprobante);
            obj.setNumComprobante(numComprobante);
            obj.setImpuesto(impuesto);
            obj.setTotal(total);
            //vamos a enviar los datos del detalle del ingreso
            List<DetalleIngreso> detalles = new ArrayList();
            //declaramos variables para un mejor control de los elementos del detalle de ingreso
            int articuloId;
            int cantidad;
            double precio;
            
            for(int i = 0; i < modeloDetalles.getRowCount(); i++){
                articuloId = Integer.parseInt(String.valueOf(modeloDetalles.getValueAt(i, 0)));
                cantidad = Integer.parseInt(String.valueOf(modeloDetalles.getValueAt(i, 3)));
                precio = Double.parseDouble(String.valueOf(modeloDetalles.getValueAt(i, 4)));
                
                //Agregar la informacion al elemento List<detalleIngreso> para llenar la clase
                detalles.add(new DetalleIngreso(articuloId, cantidad, precio));
            }
            obj.setDetalles(detalles);
            if(DATOS.insertar(obj)){
                return "OK";
            }else{
                return "Error en el registro.";
            }
        }
    }
    
    //metodo para desactivar los registros
    public String anular(int id){
        if(DATOS.anular(id)){
            return "OK";
        }else{
            return "No se puede anular el registro.";
        }
    }
    
    //metodos para controlar el total de los registros
    public int total(){
        return DATOS.total();
    }
    
    public int totalMostrados(){
        return this.registrosMostrados;
    }
    
    //buscar por rango fechas
         //metodo para obtener la lista de la tabla articulo
    public DefaultTableModel listarConsulta(int totalRegPagina, int numPagina, Date fechaInicio, Date fechaFinal){
        List<Ingreso> lista = new ArrayList();
        lista.addAll(DATOS.listarConsulta(totalRegPagina, numPagina,fechaInicio, fechaFinal));
        String[] titulos = {"Id", "Usuario Id", "Usuario", "Proveedor Id", "Proveedor", "Tipo Comprobante", "Serie", "Num Comprobante", "Fecha", 
            "Impuesto", "Total", "Estado"};
        this.modeloTabla = new DefaultTableModel(null,titulos);
        
        //recorrer los items de la lista para llenar la tabla
        String estado;
        String[] registro = new String[12];
        this.registrosMostrados = 0;
        //vamos a definir el formato para la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        
        for(Ingreso item:lista){
            registro[0] = Integer.toString(item.getId());
            registro[1] = Integer.toString(item.getUsuarioId());
            registro[2] = item.getUsuarioNombre();
            registro[3] = Integer.toString(item.getPersonaId());
            registro[4] = item.getPersonaNombre();
            registro[5] = item.getTipoComprobante();
            registro[6] = item.getSerieComprobante();
            registro[7] = item.getNumComprobante();
            registro[8] = sdf.format(item.getFecha());
            registro[9] = Double.toString(item.getImpuesto());
            registro[10] = Double.toString(item.getTotal());
            registro[11] = item.getEstado();
            this.modeloTabla.addRow(registro);
            this.registrosMostrados = this.registrosMostrados + 1;
        }
        return this.modeloTabla;//51.31
    }
}
