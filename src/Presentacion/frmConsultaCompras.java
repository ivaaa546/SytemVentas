/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package Presentacion;

import Entidades.Categoria;
import Entidades.GenerarPDFCompras;
import Negocio.ArticuloControl;
import Negocio.IngresoControl;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author pc
 */
public class frmConsultaCompras extends javax.swing.JInternalFrame {
    //generamos las instancias a la clase categoriaControl
    private final IngresoControl CONTROL;
    private String accion;
    private String nombreAnt;

    
    //declaramos las variables para el control del paginamiento
    private int totalPagina = 10;
    private int numPag = 1;
    private boolean primeraCarga = true;
    private int totalReg;
    private Date dtfecha1;
    private Date fech2;
    //DefaultTableModel
    public DefaultTableModel modeloDetalles;
    
    //crear un objeto de tipo contenedor
    public JFrame contenedor;
    
    
    /**
     * Creates new form frmArticulos
     */
    public frmConsultaCompras(JFrame frmP) {
        initComponents();
        this.contenedor = frmP;
        this.CONTROL = new IngresoControl();
        this.listar("", false);
        this.primeraCarga = false;
        this.Paginar();
        
        //deshabilitamos el panelde mantenimiento al cargar el formulario
        tabGeneral.setEnabledAt(1, false);
        
        this.accion = "Guardar";
        
        //ocultar el txtID
        //txtIdProveedor.setVisible(false);   
        this.Paginar();
        
        //llamamos al metodo crearDetalles
        this.crearDetalles();

    }
    //metodo para la paginacion
    private void Paginar(){
        int totalPaginas;
        this.totalReg = this.CONTROL.total();
        
        //obtenemos el valor del total de registros por pagina
        this.totalPagina = Integer.parseInt((String)cboTotalRegPag1.getSelectedItem());
        
        //hacemos el calculo de total de paginas
        totalPaginas = (int)(Math.ceil((double)this.totalReg / this.totalPagina));
        
        //mostramos las paginas en el cuadro combinado
        if(totalPaginas == 0){
            totalPaginas = 1;
        }
        cboNumPag1.removeAllItems();
        
        //agregamos los valores
        for(int i = 1; i <= totalPaginas; i++){
            cboNumPag1.addItem(Integer.toString(i));
        }
        cboNumPag1.setSelectedIndex(0);
    }
    
        //metodo para ocultar las columnas
    private void ocultarColumnas(){
        //cambiamos el tamaño del registro de la columna categoria id
        tablaListado.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getColumnModel().getColumn(1).setMinWidth(0);
        
        //cabiamos el tamaño del encabezado
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
    }
    
    //metodo para establecer la estructura de la tabla
    private void crearDetalles(){
        modeloDetalles = new DefaultTableModel(){
            @Override
            public void setValueAt(Object aValue, int fila, int columna) {
                super.setValueAt(aValue, fila, columna);
                calcularTotales();
                fireTableDataChanged();
            }

            //devuelve el valor de una celda especifica
            @Override
            public Object getValueAt(int fila, int columna) {
                if(columna == 5){
                    Double cantD;
                    try {
                        cantD = Double.parseDouble((String)getValueAt(fila, 3));
                        //cantD = Double.parseDouble((String)getValueAt(fila, 3));
                    } catch (Exception e) {
                        cantD = 1.0;
                    }
                    Double precioD = Double.parseDouble((String)getValueAt(fila, 4));
                    //precioD = Double.parseDouble((String)getValueAt(fila, 4));
                    if(cantD != null && precioD != null){
                        return String.format("%.2f", cantD*precioD);
                    }else{
                        return 0;
                    }
                }
                return super.getValueAt(fila, columna);
            }

            @Override
            public boolean isCellEditable(int fila, int columna) {
                if(columna == 3){
                    return columna == 3;
                }
                if(columna == 4){
                    return columna == 4;
                }
                return columna == 3;
            }
        };
        
        //definir las columnas de la tabla detalle
        modeloDetalles.setColumnIdentifiers(new Object[]{"ID", "CODIGO", "ARTICULO", "CANTIDAD", "PRECIO", "SUBTOTAL"});
        TablaDetalles.setModel(modeloDetalles);
    }
      //metodo para agregar los articulos a la tabla detalle
    public void agregarDetalles(String id, String codigo, String nombre, String precio){
        String idT;
        boolean existe = false;
        
        //recorrer la listaDetalles por ciclo for
        for (int i = 0; i < this.modeloDetalles.getRowCount(); i++) {
            idT = String.valueOf(this.modeloDetalles.getValueAt(i, 0));
            if(idT.equals(id)){
                existe = true;
            }
        }
        if(existe){
            this.mensajeError("El articulo ya ha sido agregado");
        }else{
            this.modeloDetalles.addRow(new Object[]{id, codigo, nombre, "1", precio, precio});
            this.calcularTotales();
        }
    }
     //creamos metodo para calcular totales
    private void calcularTotales(){
        double total = 0;
        double subTotal;
        int items = modeloDetalles.getRowCount();
        if(items == 0){
            total = 0;
        }else{
            for (int i = 0; i < items; i++) {
                total = total + Double.parseDouble(String.valueOf(modeloDetalles.getValueAt(i, 5)));
            }
        }
        subTotal = total/(1 + Double.parseDouble(txtImpuesto.getText()));
        txtTotal.setText(String.format("%.2f", total));
        txtSubTotal.setText(String.format("%.2f", subTotal));
        txtTotalImpuesto.setText(String.format("%.2f", total - subTotal));
        
    }
    
    //metodo para listar los elementos de la tabla categoria
    private void listarConsulta(boolean paginar, Date fechaInicial, Date fechaFinal){
        //actualizar los valores para paginar
        this.totalPagina = Integer.parseInt((String)cboTotalRegPag1.getSelectedItem());
        //validamos que el valor no sea nulo
        if(cboNumPag1.getSelectedItem() != null){
            this.numPag = Integer.parseInt((String)cboNumPag1.getSelectedItem());
        }
        if(paginar == true){
            tablaListado.setModel(this.CONTROL.listarConsulta(this.totalPagina, this.numPag, fechaInicial, fechaFinal ));
        }else{
            tablaListado.setModel(this.CONTROL.listarConsulta(this.totalPagina, 1, fechaInicial,fechaFinal));
        }
        
        //agregamos un metodo para el ordenamiento de los registros deacuerdoa a la columna seleccionada
        TableRowSorter orden = new TableRowSorter(tablaListado.getModel());
        tablaListado.setRowSorter(orden);
        this.ocultarColumnas();
        lblTotalRegistros.setText("Mostrando: " + this.CONTROL.totalMostrados() + " de un total de " + this.CONTROL.total() + " registros.");
    }
    
        //metodo para listar los elementos de la tabla categoria
    private void listar(String texto, boolean paginar){
        //actualizar los valores para paginar
        this.totalPagina = Integer.parseInt((String)cboTotalRegPag1.getSelectedItem());
        //validamos que el valor no sea nulo
        if(cboNumPag1.getSelectedItem() != null){
            this.numPag = Integer.parseInt((String)cboNumPag1.getSelectedItem());
        }
        if(paginar == true){
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabGeneral = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        txtNombreB = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaListado = new javax.swing.JTable();
        btnAnular = new javax.swing.JButton();
        lblTotalRegistros = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        cboNumPag1 = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        cboTotalRegPag1 = new javax.swing.JComboBox<>();
        btnVerIngreso = new javax.swing.JButton();
        dtfecha2 = new com.toedter.calendar.JDateChooser();
        lblTotalRegistros1 = new javax.swing.JLabel();
        lblTotalRegistros2 = new javax.swing.JLabel();
        dtfecha3 = new com.toedter.calendar.JDateChooser();
        btnPDFCompras = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtIdProveedor = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtNumComprobante = new javax.swing.JTextField();
        btnVer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        txtTotal = new javax.swing.JTextField();
        cboTipoComprobante = new javax.swing.JComboBox<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        TablaDetalles = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        txtSubTotal = new javax.swing.JTextField();
        txtTotalImpuesto = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtSerieComprobante = new javax.swing.JTextField();
        txtNombreProveedor = new javax.swing.JTextField();
        btnBuscarP = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        txtImpuesto = new javax.swing.JTextField();
        btnQuitar = new javax.swing.JButton();
        btnPDFCompras2 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);

        txtNombreB.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        btnBuscar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        btnNuevo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        tablaListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tablaListado);

        btnAnular.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAnular.setText("Anular");
        btnAnular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnularActionPerformed(evt);
            }
        });

        lblTotalRegistros.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalRegistros.setText("Registros");

        jLabel13.setText("NumPag: ");

        cboNumPag1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboNumPag1ActionPerformed(evt);
            }
        });

        jLabel14.setText("Total Registros por Pagina: ");

        cboTotalRegPag1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "10", "15", "20", "50", "100" }));
        cboTotalRegPag1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTotalRegPag1ActionPerformed(evt);
            }
        });

        btnVerIngreso.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnVerIngreso.setText("Ver Ingreso");
        btnVerIngreso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerIngresoActionPerformed(evt);
            }
        });

        lblTotalRegistros1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalRegistros1.setText("Fecha Final");

        lblTotalRegistros2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTotalRegistros2.setText("Fecha de Incio");

        btnPDFCompras.setText("Generar PDF");
        btnPDFCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFComprasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1026, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(btnAnular)
                                .addGap(204, 204, 204)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel13)
                                    .addComponent(cboNumPag1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(55, 55, 55)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addGap(46, 46, 46)
                                        .addComponent(lblTotalRegistros)
                                        .addGap(158, 158, 158)
                                        .addComponent(btnPDFCompras))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(cboTotalRegPag1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblTotalRegistros2)
                                .addGap(139, 139, 139)
                                .addComponent(lblTotalRegistros1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dtfecha3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(dtfecha2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(143, 143, 143)
                        .addComponent(btnBuscar)
                        .addGap(55, 55, 55)
                        .addComponent(btnNuevo)
                        .addGap(47, 47, 47)
                        .addComponent(txtNombreB, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                        .addComponent(btnVerIngreso)
                        .addGap(157, 157, 157))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNombreB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnVerIngreso))
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btnNuevo)
                                .addComponent(btnBuscar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblTotalRegistros2)
                                    .addComponent(lblTotalRegistros1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dtfecha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dtfecha3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTotalRegistros)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14)
                            .addComponent(btnPDFCompras))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cboNumPag1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboTotalRegPag1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnAnular)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        tabGeneral.addTab("Listado", jPanel1);

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Proveedor*: ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 10, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Serie Comprobante*: ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, -1, 20));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Total:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 590, -1, -1));
        jPanel2.add(txtIdProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 10, 160, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("Impuesto*:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 10, -1, -1));
        jPanel2.add(txtNumComprobante, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, 110, -1));

        btnVer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnVer.setText("Ver");
        btnVer.setEnabled(false);
        btnVer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerActionPerformed(evt);
            }
        });
        jPanel2.add(btnVer, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 80, -1, 30));

        jLabel4.setText("(*) Indica que es un campo obligatorio");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 530, -1, -1));

        btnGuardar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        jPanel2.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 560, -1, -1));

        btnCancelar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel2.add(btnCancelar, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 560, -1, -1));

        txtTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalActionPerformed(evt);
            }
        });
        jPanel2.add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 590, 100, -1));

        cboTipoComprobante.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Boleta", "Factura", "Ticket" }));
        jPanel2.add(cboTipoComprobante, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 50, 160, -1));

        TablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane3.setViewportView(TablaDetalles);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 1017, 370));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Articulo*: ");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, -1, -1));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel12.setText("Sub Total:");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 530, -1, -1));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setText("Total Impuestos:");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 560, -1, -1));

        txtCodigo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCodigoKeyReleased(evt);
            }
        });
        jPanel2.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 80, 390, -1));

        txtSubTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSubTotalActionPerformed(evt);
            }
        });
        jPanel2.add(txtSubTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 530, 100, -1));

        txtTotalImpuesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTotalImpuestoActionPerformed(evt);
            }
        });
        jPanel2.add(txtTotalImpuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 560, 100, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setText("Tipo Comprobante*: ");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));
        jPanel2.add(txtSerieComprobante, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 160, -1));
        jPanel2.add(txtNombreProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 10, 342, -1));

        btnBuscarP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBuscarP.setText("...");
        btnBuscarP.setEnabled(false);
        btnBuscarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarPActionPerformed(evt);
            }
        });
        jPanel2.add(btnBuscarP, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 10, 50, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setText("Numero Comprobante*:");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 60, -1, -1));

        txtImpuesto.setText("0.12");
        jPanel2.add(txtImpuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 10, 50, -1));

        btnQuitar.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnQuitar.setText("Quitar");
        btnQuitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQuitarActionPerformed(evt);
            }
        });
        jPanel2.add(btnQuitar, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 100, 90, -1));

        btnPDFCompras2.setText("Generar PDF");
        btnPDFCompras2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPDFCompras2ActionPerformed(evt);
            }
        });
        jPanel2.add(btnPDFCompras2, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 560, -1, -1));

        tabGeneral.addTab("Mantenimiento", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tabGeneral, javax.swing.GroupLayout.PREFERRED_SIZE, 661, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 

    //metodo para el control de los mensajes de error y ok
    private void mensajeError(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.ERROR_MESSAGE);//26.00
    }

    private void mensajeOk(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.OK_OPTION);//26.00
    }  
    private void btnVerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerActionPerformed
         // frmSeleccionarArticuloCompra frmn = new frmSeleccionarArticuloCompra(contenedor, this, true);
       // frmn.toFront();
    }//GEN-LAST:event_btnVerActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        
        if(txtIdProveedor.getText().length() == 0 ){
            JOptionPane.showMessageDialog(this, "Debe ingresar un proveedor", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtIdProveedor.requestFocus();
            return;
        }
        if(txtSerieComprobante.getText().length() > 7){
            JOptionPane.showMessageDialog(this, "Debe ingresar una serie de comprobante, no mayor a 7 "
                    + "caracteres, es obligartorio", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtSerieComprobante.requestFocus();
            return;
        }
        if(txtNumComprobante.getText().length() == 7 || txtNumComprobante.getText().length() > 10){
            JOptionPane.showMessageDialog(this, "Debe ingresar un numero de comprobante, no mayor a 10 "
                    + "caracteres, es obligartorio", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtNumComprobante.requestFocus();
            return;
        }
        if(modeloDetalles.getRowCount() == 0){
            JOptionPane.showMessageDialog(this, "Debe agregar articulos al detalle", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String resp = "";
            
        //guardar
        resp = this.CONTROL.insertar(Integer.parseInt(txtIdProveedor.getText()), (String)cboTipoComprobante.getSelectedItem(),
                txtSerieComprobante.getText(), txtNumComprobante.getText(), Double.parseDouble(txtImpuesto.getText()),
                Double.parseDouble(txtTotal.getText()), modeloDetalles);
        if(resp.equals("OK")){
            this.mensajeOk("Articulo registrado correctamente");
            this.Limpiar();
            this.listar("", false);
        }else{
            this.mensajeError(resp);
            }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
            tabGeneral.setEnabledAt(1, false);
        tabGeneral.setEnabledAt(0, true);
        
        //trasladamos al panel de mantemientos
        tabGeneral.setSelectedIndex(0);
        
        this.Limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void txtTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalActionPerformed

    private void txtSubTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSubTotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubTotalActionPerformed

    private void txtTotalImpuestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTotalImpuestoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalImpuestoActionPerformed

    private void btnBuscarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarPActionPerformed
      //generar una instancia al frmSeleccionarProveedorCompra
       // frmSeleccionarProveedorCompra frm = new frmSeleccionarProveedorCompra(contenedor, this, true);
       // frm.toFront();
    }//GEN-LAST:event_btnBuscarPActionPerformed

    private void btnQuitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQuitarActionPerformed
        //determinar si tiene seleccionado un elemento a quitar
        if(TablaDetalles.getSelectedRowCount() == 1){
            this.modeloDetalles.removeRow(TablaDetalles.getSelectedRow());
            this.calcularTotales();
        }else{
            this.mensajeError("Debe seleccionar el articulo a quitar");
        }
    }//GEN-LAST:event_btnQuitarActionPerformed

    private void txtCodigoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoKeyReleased
        if(txtCodigo.getText().length() > 0){
            if(evt.getKeyCode() == KeyEvent.VK_ENTER){
                Entidades.Articulo art;
                art = this.CONTROL.obtenerArticuloCodigoIngreso(txtCodigo.getText());
                if(art == null){
                    this.mensajeError("No existe un articulo con ese codigo");
                }else{
                    this.agregarDetalles(Integer.toString(art.getId()), art.getCodigo(), art.getNombre(), Double.toString(art.getPrecioVenta()));
                    
                }
            }
        }else{
            this.mensajeError("Ingrese el codigo a buscar");
        }
    }//GEN-LAST:event_txtCodigoKeyReleased

    private void btnVerIngresoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerIngresoActionPerformed
        //determinamos si el usuario a seleccionado uno de los ingresos de la tabla
        if(tablaListado.getSelectedRowCount() == 1){
            //obtenemos los valores de la fila seleccionada
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String idProveedor = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 3));
            String nombreProveedor = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));
            String tipoComprobante = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String serie = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String numero = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 7));
            String impuesto = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 9));

            //debemos mostrarlos en los cuadros de texto
            txtIdProveedor.setText(idProveedor);
            txtNombreProveedor.setText(nombreProveedor);
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
            jLabel8.setVisible(true);

        }else{
            this.mensajeError("Seleccione el ingreso para ver el detalle de compra");
        }
    }//GEN-LAST:event_btnVerIngresoActionPerformed

    private void cboTotalRegPag1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTotalRegPag1ActionPerformed
        // TODO add your handling code here:
        this.Paginar();
    }//GEN-LAST:event_cboTotalRegPag1ActionPerformed

    private void cboNumPag1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNumPag1ActionPerformed
        //activar la primera carga si no esta activa
        if(this.primeraCarga == false){
            this.listar("", true);
        }
    }//GEN-LAST:event_cboNumPag1ActionPerformed

    private void btnAnularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnularActionPerformed
        // programar la anulacion de las compras seleccionada
        if(tablaListado.getSelectedRowCount() == 1){
            //obtenemos los valores de la fila seleccionada
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String comprobante = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String serie = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String numero = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 7));

            if(JOptionPane.showConfirmDialog(this, "¿Desea anular el registro: " + comprobante + " " + serie + " - " + numero,
                "Sistema Ventas", JOptionPane.YES_NO_OPTION) == 0){
            String resp = this.CONTROL.anular(Integer.parseInt(id));
            if(resp == "OK"){
                this.mensajeOk("Registro anulado");
                this.listar("", false);
            }else{
                this.mensajeError(resp);
            }

        }

        }else{
            this.mensajeError("Selecciona un registro para anularlo");
        }
    }//GEN-LAST:event_btnAnularActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        // Habilitar y deshabiltar los paneles
        tabGeneral.setEnabledAt(1, true);
        tabGeneral.setEnabledAt(0, false);

        //trasladamos al panel de mantemientos
        tabGeneral.setSelectedIndex(1);

        this.accion = "Guardar";
        btnGuardar.setText("Guardar");
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // Obtener la informacion del cuadro de texto para realizar la busqueda
         // Obtener las fechas del JDateChooser
    Date fechaInicial = dtfecha3.getDate(); 
    Date fechaFinal = dtfecha2.getDate();

    // Validar que las fechas no sean nulas
    if (fechaInicial != null && fechaFinal != null) {
        // Llamar al método listarConsulta con la lógica de paginación
        boolean paginar = (cboNumPag1.getSelectedItem() != null); // Verificar si hay un número de página seleccionado
        listarConsulta(paginar, fechaInicial, fechaFinal);
    } else {
        JOptionPane.showMessageDialog(this, "Por favor, seleccione ambas fechas.", "Error de Fecha", JOptionPane.WARNING_MESSAGE);
    }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnPDFComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFComprasActionPerformed
        GenerarPDFCompras.generatePDF();
    }//GEN-LAST:event_btnPDFComprasActionPerformed

    private void btnPDFCompras2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPDFCompras2ActionPerformed
        GenerarPDFCompras.generatePDF2();
    }//GEN-LAST:event_btnPDFCompras2ActionPerformed

    private void Limpiar(){
      txtNombreProveedor.setText("");
        txtIdProveedor.setText("");
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

        

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TablaDetalles;
    private javax.swing.JButton btnAnular;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnBuscarP;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnPDFCompras;
    private javax.swing.JButton btnPDFCompras2;
    private javax.swing.JButton btnQuitar;
    private javax.swing.JButton btnVer;
    private javax.swing.JButton btnVerIngreso;
    private javax.swing.JComboBox<String> cboNumPag1;
    private javax.swing.JComboBox<String> cboTipoComprobante;
    private javax.swing.JComboBox<String> cboTotalRegPag1;
    private com.toedter.calendar.JDateChooser dtfecha2;
    private com.toedter.calendar.JDateChooser dtfecha3;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTotalRegistros;
    private javax.swing.JLabel lblTotalRegistros1;
    private javax.swing.JLabel lblTotalRegistros2;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTable tablaListado;
    private javax.swing.JTextField txtCodigo;
    public javax.swing.JTextField txtIdProveedor;
    private javax.swing.JTextField txtImpuesto;
    private javax.swing.JTextField txtNombreB;
    public javax.swing.JTextField txtNombreProveedor;
    private javax.swing.JTextField txtNumComprobante;
    private javax.swing.JTextField txtSerieComprobante;
    private javax.swing.JTextField txtSubTotal;
    private javax.swing.JTextField txtTotal;
    private javax.swing.JTextField txtTotalImpuesto;
    // End of variables declaration//GEN-END:variables
}
