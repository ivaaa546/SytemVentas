/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Presentacion;

import Negocio.VentasControl;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author josec
 */
public class frmVentas extends javax.swing.JInternalFrame {

    //generamos las instancias a la clase categoriaControl
    private final VentasControl CONTROL;
    private String accion;
    private String nombreAnt;
    
    //declaramos las variables para el control del paginamiento
    private int totalPagina = 10;
    private int numPag = 1;
    private boolean primeraCarga = true;
    private int totalReg;
    
    //DefaultTableModel
    public DefaultTableModel modeloDetalles;
    
    //crear un objeto de tipo contenedor
    public JFrame contenedor;
    
    
    public frmVentas(JFrame frmC) {
        initComponents();
        this.contenedor = frmC;
        this.CONTROL = new VentasControl();
        this.listar("", false);
        this.primeraCarga = false;
        
        //deshabilitamos el panelde mantenimiento al cargar el formulario
        tabGeneral.setEnabledAt(1, false);
        
        this.accion = "Guardar";
        
        this.Paginar();
        
        //llamamos al metodo crearDetalles
        this.crearDetalles();
    }
    
    //metodo para la paginacion
    private void Paginar(){
        int totalPaginas;
        this.totalReg = this.CONTROL.total();
        
        //obtenemos el valor del total de registros por pagina
        this.totalPagina = Integer.parseInt((String)cboTotalRegPag.getSelectedItem());
        
        //hacemos el calculo de total de paginas
        totalPaginas = (int)(Math.ceil((double)this.totalReg / this.totalPagina));
        
        //mostramos las paginas en el cuadro combinado
        if(totalPaginas == 0){
            totalPaginas = 1;
        }
        cboNumPag.removeAllItems();
        
        //agregamos los valores
        for(int i = 1; i <= totalPaginas; i++){
            cboNumPag.addItem(Integer.toString(i));
        }
        cboNumPag.setSelectedIndex(0);
    }
    
    //metodo para ocultar las columnas
    private void ocultarColumnas()
    {
        //cambiamos el tamaño del registro de la columna categoria id
        tablaListado.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getColumnModel().getColumn(1).setMinWidth(0);
        
        //cabiamos el tamaño del encabezado
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
        
        //cambiamos el tamaño del registro de la columna categoria id
        tablaListado.getColumnModel().getColumn(3).setMaxWidth(0);
        tablaListado.getColumnModel().getColumn(3).setMinWidth(0);
        
        //cabiamos el tamaño del encabezado
        tablaListado.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
    }
    
    //metodo para establecer la estructura de la tabla
    private void crearDetalles()
    {
        modeloDetalles = new DefaultTableModel(){
            @Override
            public void setValueAt(Object aValue, int fila, int columna) {
                super.setValueAt(aValue, fila, columna);
                calcularTotales();
                fireTableDataChanged();
            }

            //devuelve el valor de una celda especifica
            @Override
            public Object getValueAt(int fila, int columna) 
            {
                if(columna == 6)
                {
                    Double cantD, descD;
                    try 
                    {
                        cantD = Double.parseDouble((String)getValueAt(fila, 3));
                        descD = Double.parseDouble((String)getValueAt(fila, 5));
                    }
                    catch (Exception e) 
                    {
                        cantD = 1.0;
                        descD = 0.00;
                    }
                    Double precioD = Double.parseDouble((String)getValueAt(fila, 4));
                    
                    if(cantD != null && descD != null && precioD != null)
                    {
                        return String.format("%.2f", (cantD*precioD) - descD);
                    }else
                    {
                        return 0;
                    }
                }
                return super.getValueAt(fila, columna);
            }

            @Override
            public boolean isCellEditable(int fila, int columna) 
            {
                if(columna == 3)
                {
                    return columna == 3;
                }
                if(columna == 4)
                {
                    return columna == 4;
                }
                if(columna == 5)
                {
                    return columna == 5;
                }
                return columna == 3;
                
            }
        };
        
        //definir las columnas de la tabla detalle
        modeloDetalles.setColumnIdentifiers(new Object[]{"ID", "CODIGO", "ARTICULO", "CANTIDAD", "PRECIO", "DESCUENTO", "SUBTOTAL"});
        TablaDetalles.setModel(modeloDetalles);
    }
    
    //metodo para agregar los articulos a la tabla detalle
    public void agregarDetalles(String id, String codigo, String nombre, String precio)
    {
        String idT;
        boolean existe = false;
        
        //recorrer la lista detalles por ciclo for
        for (int i = 0; i < this.modeloDetalles.getRowCount(); i++) 
        {
            idT = String.valueOf(this.modeloDetalles.getValueAt(i, 0));
            if(idT.equals(id))
            {
                existe = true;
            }
        }
        if(existe)
        {
            this.mensajeError("El articulo ya ha sido agregado");
        }else
        {
            this.modeloDetalles.addRow(new Object[]{id, codigo, nombre, "1", precio, "0.00", precio});
            this.calcularTotales();
        }
    }
    
    //creamos metodo para calcular totales
    private void calcularTotales()
    {
        double total = 0;
        double subTotal;
        int items = modeloDetalles.getRowCount();
        if(items == 0)
        {
            total = 0;
        }else
        {
            for (int i = 0; i < items; i++) 
            {
                total = total + Double.parseDouble(String.valueOf(modeloDetalles.getValueAt(i, 6)));
            }
        }
        subTotal = total/(1 + Double.parseDouble(txtImpuesto.getText()));
        txtTotal.setText(String.format("%.2f", total));
        txtSubTotal.setText(String.format("%.2f", subTotal));
        txtTotalImpuesto.setText(String.format("%.2f", total - subTotal));        
    }

    //metodo para listar los elementos de la tabla categoria
    private void listar(String texto, boolean paginar)
    {
        //actualizar los valores para paginar
        this.totalPagina = Integer.parseInt((String)cboTotalRegPag.getSelectedItem());
        
        //validamos que el valor no sea nulo
        if(cboNumPag.getSelectedItem() != null)
        {
            this.numPag = Integer.parseInt((String)cboNumPag.getSelectedItem());
        }
        if(paginar == true)
        {
            tablaListado.setModel(this.CONTROL.listar(texto, this.totalPagina, this.numPag));
        }else{
            tablaListado.setModel(this.CONTROL.listar(texto, this.totalPagina, 1));
        }
        
        //agregamos un metodo para el ordenamiento de los registros deacuerdoa a la columna seleccionada
        TableRowSorter orden = new TableRowSorter(tablaListado.getModel());
        tablaListado.setRowSorter(orden);
        this.ocultarColumnas();
        lblTotalRegistros.setText("Mostrando: " + this.CONTROL.totalMostrados() + " de un total de " + this.CONTROL.total() + " registros.");
    }
    
    //metodo para el control de los mensajes de error y ok
    private void mensajeError(String mensaje)
    {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.ERROR_MESSAGE);//26.00
    }
    
    private void mensajeOk(String mensaje)
    {
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.OK_OPTION);//26.00
    }
    
    private void Limpiar()
    {
        txtNombreCliente.setText("");
        txtIdCliente.setText("");
        txtSerieComprobante.setText("");
        txtNumComprobante.setText("");
        txtImpuesto.setText("");
        txtCodigo.setText("");
        
        this.accion = "Guardar";
        
        txtTotal.setText("0.00");
        txtSubTotal.setText("0.00");
        txtTotalImpuesto.setText("0.00");
        
        this.crearDetalles();
        
        btnGuardar.setVisible(true);
        btnVer.setVisible(true);
        btnQuitar.setVisible(true);
        txtCodigo.setVisible(true);
        jLabel8.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabGeneral = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombreB = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaListado = new javax.swing.JTable();
        lblTotalRegistros = new javax.swing.JLabel();
        btnNuevo = new javax.swing.JButton();
        btnAnular = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cboNumPag = new javax.swing.JComboBox<>();
        cboTotalRegPag = new javax.swing.JComboBox<>();
        btnDetalleIngreso = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtIdCliente = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNumComprobante = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JFormattedTextField();
        txtNombreCliente = new javax.swing.JTextField();
        btnBuscarP = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        txtImpuesto = new javax.swing.JTextField();
        cboTipoComprobante = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        txtSerieComprobante = new javax.swing.JTextField();
        btnVer = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaDetalles = new javax.swing.JTable();
        jLabel14 = new javax.swing.JLabel();
        txtSubTotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtTotalImpuesto = new javax.swing.JTextField();
        txtTotal = new javax.swing.JTextField();
        btnQuitar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Egresos del Almacen");

        jLabel1.setText("Nombre: ");

        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        tablaListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaListado);

        lblTotalRegistros.setText("Registros: ");
        lblTotalRegistros.setToolTipText("");

        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnAnular.setText("Anular");
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });

        jLabel2.setText("NumPag: ");

        jLabel11.setText("Total Registros por Pagina: ");

        cboNumPag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNumPagActionPerformed(evt);
            }
        });

        cboTotalRegPag.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15", "20", "50", "100" }));
        cboTotalRegPag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTotalRegPagActionPerformed(evt);
            }
        });

        btnDetalleIngreso.setText("Ver Detalles de venta");
        btnDetalleIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalleIngresoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTotalRegistros)
                            .addComponent(btnAnular))
                        .addGap(225, 225, 225)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cboNumPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(133, 133, 133)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboTotalRegPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(0, 313, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtNombreB, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDetalleIngreso)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombreB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(btnNuevo)
                    .addComponent(btnDetalleIngreso))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalRegistros)
                    .addComponent(jLabel2)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAnular)
                    .addComponent(cboNumPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTotalRegPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        tabGeneral.addTab("Listado", jPanel1);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        jLabel4.setText("(*) Indica que es un campo obligatorio");

        jLabel5.setText("Numero Comprobante*: ");

        jLabel6.setText("Cliente*: ");

        jLabel7.setText("Tipo Comprobante*: ");

        jLabel8.setText("Articulo: ");

        txtCodigo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoKeyReleased(evt);
            }
        });

        btnBuscarP.setText("...");
        btnBuscarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPActionPerformed(evt);
            }
        });

        jLabel12.setText("Impuesto*: ");

        txtImpuesto.setText("0.12");

        cboTipoComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Boleta", "Factura", "Ticket" }));

        jLabel13.setText("Serie Comprobante*: ");

        btnVer.setText("Ver");
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });

        TablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(TablaDetalles);

        jLabel14.setText("SubTotal: ");

        txtSubTotal.setEditable(false);

        jLabel15.setText("Total Impuesto: ");

        jLabel16.setText("Total: ");

        txtTotalImpuesto.setEditable(false);

        txtTotal.setEditable(false);

        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(531, 531, 531)
                                .addComponent(jLabel14)
                                .addGap(51, 51, 51))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel15))
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotalImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel6)))
                            .addGap(25, 25, 25)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtNombreCliente))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(cboTipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel13)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtSerieComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(btnVer))))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtNumComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(btnBuscarP, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel12)
                                            .addGap(18, 18, 18)
                                            .addComponent(txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnQuitar))))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jScrollPane3))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarP)
                    .addComponent(jLabel6)
                    .addComponent(jLabel12)
                    .addComponent(txtImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(cboTipoComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(txtSerieComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtNumComprobante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnVer)
                    .addComponent(btnQuitar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnGuardar)
                            .addComponent(btnCancelar)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(9, 9, 9)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotalImpuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))))
                .addContainerGap())
        );

        tabGeneral.addTab("Mantenimiento", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral)
        );

        getAccessibleContext().setAccessibleName("Ingreso a Almacen");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // Obtener la informacion del cuadro de texto para realizar la busqueda
        this.listar(txtNombreB.getText(), false);
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // Habilitar y deshabiltar los paneles
        tabGeneral.setEnabledAt(1, true);
        tabGeneral.setEnabledAt(0, false);
        
        //trasladamos al panel de mantemientos
        tabGeneral.setSelectedIndex(1);
        
        this.accion = "Guardar";
        btnGuardar.setText("Guardar");
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        tabGeneral.setEnabledAt(1, false);
        tabGeneral.setEnabledAt(0, true);
        
        //trasladamos al panel de mantemientos
        tabGeneral.setSelectedIndex(0);
        
        this.Limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        if(txtIdCliente.getText().length() == 0 )
        {
            JOptionPane.showMessageDialog(this, "Debe ingresar un proveedor", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtIdCliente.requestFocus();
            return;
        }
        if(txtSerieComprobante.getText().length() > 7)
        {
            JOptionPane.showMessageDialog(this, "Debe ingresar una serie de comprobante, no mayor a 7 "
                    + "caracteres, es obligartorio", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtSerieComprobante.requestFocus();
            return;
        }
        if(txtNumComprobante.getText().length() == 7 || txtNumComprobante.getText().length() > 10)
        {
            JOptionPane.showMessageDialog(this, "Debe ingresar un numero de comprobante, no mayor a 10 "
                    + "caracteres, es obligartorio", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtNumComprobante.requestFocus();
            return;
        }
        if(modeloDetalles.getRowCount() == 0)
        {
            JOptionPane.showMessageDialog(this, "Debe agregar articulos al detalle", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String resp = "";
            
        //guardar
        resp = this.CONTROL.insertar(Integer.parseInt(txtIdCliente.getText()), (String)cboTipoComprobante.getSelectedItem(),
                txtSerieComprobante.getText(), txtNumComprobante.getText(), Double.parseDouble(txtImpuesto.getText()),
                Double.parseDouble(txtTotal.getText()), modeloDetalles);
        if(resp.equals("OK"))
        {
            this.mensajeOk("Articulo registrado correctamente");
            this.Limpiar();
            this.listar("", false);
        }
        else
        {
            this.mensajeError(resp);
            }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed
        // programar la anulacion de las compras seleccionada
        if(tablaListado.getSelectedRowCount() == 1)
        {
            //obtenemos los valores de la fila seleccionada
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String comprobante = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String serie = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String numero = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 7));
            
            if(JOptionPane.showConfirmDialog(this, "¿Desea anular el registro: " + comprobante + " " + serie + " - " + numero, 
                    "Sistema Ventas", JOptionPane.YES_NO_OPTION) == 0)
            {
                String resp = this.CONTROL.anular(Integer.parseInt(id));
                if(resp == "OK")
                {
                    this.mensajeOk("Registro anulado");
                    this.listar("", false);
                }
                else
                {
                    this.mensajeError(resp);
                }
            
        }
            
        }
        else
        {
            this.mensajeError("Selecciona un registro para anularlo");
        }
    }//GEN-LAST:event_btnAnularActionPerformed

    private void cboNumPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNumPagActionPerformed
        //activar la primera carga si no esta activa
        if(this.primeraCarga == false)
        {
            this.listar("", true);
        }
    }//GEN-LAST:event_cboNumPagActionPerformed

    private void cboTotalRegPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTotalRegPagActionPerformed
        // TODO add your handling code here:
        this.Paginar();
    }//GEN-LAST:event_cboTotalRegPagActionPerformed

    private void btnBuscarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPActionPerformed
        //generar una instancia al frmSeleccionarProveedorCompra
        frmSeleccionarClienteVenta fsc = new frmSeleccionarClienteVenta(contenedor, this, true);
        fsc.toFront();
        
    }//GEN-LAST:event_btnBuscarPActionPerformed

    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
        //generar una instancia al formulario Seleccionar articulos
        frmSeleccionarArticuloVenta fav = new frmSeleccionarArticuloVenta(contenedor, this, true);
        fav.toFront();
    }//GEN-LAST:event_btnVerActionPerformed

    private void txtCodigoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyReleased
        if(txtCodigo.getText().length() > 0)
        {
            if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            {
                Entidades.Articulo art;
                art = this.CONTROL.obtenerArticuloCodigoIngreso(txtCodigo.getText());
                if(art == null)
                {
                    this.mensajeError("No existe un articulo con ese codigo");
                }
                else
                {
                    this.agregarDetalles(Integer.toString(art.getId()), art.getCodigo(), art.getNombre(), Double.toString(art.getPrecioVenta()));   
                }
            }
        }
        else
        {
            this.mensajeError("Ingrese el codigo a buscar");
        }
    }//GEN-LAST:event_txtCodigoKeyReleased

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        //determinar si tiene seleccionado un elemento a quitar
        if(TablaDetalles.getSelectedRowCount() == 1)
        {
            this.modeloDetalles.removeRow(TablaDetalles.getSelectedRow());
            this.calcularTotales();
        }
        else
        {
            this.mensajeError("Debe seleccionar el articulo a quitar");
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void btnDetalleIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalleIngresoActionPerformed
        //determinamos si el usuario a seleccionado uno de los ingresos de la tabla
        if(tablaListado.getSelectedRowCount() == 1)
        {
            //obtenemos los valores de la fila seleccionada
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String idCliente = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 3));
            String nombreCliente = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));
            String tipoComprobante = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String serie = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String numero = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 7));
            String impuesto = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 9));
            
            //debemos mostrarlos en los cuadros de texto
            txtIdCliente.setText(idCliente);
            txtNombreCliente.setText(nombreCliente);
            cboTipoComprobante.setSelectedItem(tipoComprobante);
            txtSerieComprobante.setText(serie);
            txtNumComprobante.setText(numero);
            txtImpuesto.setText(impuesto);
            
            this.modeloDetalles = CONTROL.listarDetalle(Integer.parseInt(id));
            TablaDetalles.setModel(modeloDetalles);
            this.calcularTotales();
            
            tabGeneral.setEnabledAt(1, true);
            tabGeneral.setEnabledAt(0, false);
            tabGeneral.setSelectedIndex(1);
            btnGuardar.setVisible(false);
            btnVer.setVisible(false);
            btnQuitar.setVisible(false);
            txtCodigo.setVisible(false);
            jLabel8.setVisible(false);
            
        }
        else
        {
            this.mensajeError("Seleccione la venta para ver el detalle de venta");
        }
    }//GEN-LAST:event_btnDetalleIngresoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaDetalles;
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarP;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDetalleIngreso;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnVer;
    private javax.swing.JComboBox<String> cboNumPag;
    private javax.swing.JComboBox<String> cboTipoComprobante;
    private javax.swing.JComboBox<String> cboTotalRegPag;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTotalRegistros;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTable tablaListado;
    private javax.swing.JFormattedTextField txtCodigo;
    public javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtImpuesto;
    private javax.swing.JTextField txtNombreB;
    public javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtNumComprobante;
    private javax.swing.JTextField txtSerieComprobante;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalImpuesto;
    // End of variables declaration//GEN-END:variables
}
