/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Presentacion;

import Entidades.Categoria;
import Negocio.ArticuloControl;
import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;

public class frmSeleccionarArticuloVenta extends javax.swing.JDialog {
    //generar una instancia al formulario ingreso
    private frmVentas vista;
    
    //generamos las instancias a la clase categoriaControl
    private final ArticuloControl CONTROL;
    private String accion;
    private String nombreAnt;
    
    //variables para el control de almacenamiento de las imagenes
    private String rutaOrigen;
    private String rutaDestino;
    private final String directorio = "src/files/articulos/"; //*
    private String imagen = "";
    private String imagenAnt = "";
    
    //declaramos las variables para el control del paginamiento
    private int totalPagina = 10;
    private int numPag = 1;
    private boolean primeraCarga = true;
    private int totalReg;

    
    public frmSeleccionarArticuloVenta(java.awt.Frame parent, frmVentas frm, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.vista = frm;
        this.setTitle("Seleccione un articulo para el egreso al almacen");
        
        this.CONTROL = new ArticuloControl();
        this.listar("", false);
        this.primeraCarga = false;
        
        //deshabilitamos el panelde mantenimiento al cargar el formulario
        tabGeneral.setEnabledAt(1, false);
        
        this.accion = "Guardar";
        
        //ocultar el txtID
        txtID.setVisible(false);   
        
        //mostramos la informacion en el cuadro combinado
        cargarCategorias();
        
        this.setVisible(true);
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
    private void ocultarColumnas(){
        //cambiamos el tamaño del registro de la columna categoria id
        tablaListado.getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getColumnModel().getColumn(1).setMinWidth(0);
        
        //cabiamos el tamaño del encabezado
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMaxWidth(0);
        tablaListado.getTableHeader().getColumnModel().getColumn(1).setMinWidth(0);
    }

    //metodo para listar los elementos de la tabla categoria
    private void listar(String texto, boolean paginar){
        //actualizar los valores para paginar
        this.totalPagina = Integer.parseInt((String)cboTotalRegPag.getSelectedItem());
        //validamos que el valor no sea nulo
        if(cboNumPag.getSelectedItem() != null){
            this.numPag = Integer.parseInt((String)cboNumPag.getSelectedItem());
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
    
    //metodo para cargar las categorias
    private void cargarCategorias(){
        DefaultComboBoxModel items = this.CONTROL.seleccionar();
        cboCategoria.setModel(items);
    }
    
    //metodo para el control de los mensajes de error y ok
    private void mensajeError(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.ERROR_MESSAGE);//26.00
    }
    
    private void mensajeOk(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.OK_OPTION);//26.00
    }
    
    private void Limpiar(){
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtID.setText("");
        txtPrecioVenta.setText("");
        txtStock.setText("");
        this.imagen = "";
        this.imagenAnt = "";
        lblImagen.setIcon(null);
        this.rutaDestino = "";
        this.rutaOrigen = "";
        this.accion = "guardar";
    }
    
    //metodo para subir la imagen
    private void subirImagen(){
        File origen = new File(this.rutaOrigen);
        File destino = new File(this.rutaDestino);
        
        try{
            InputStream in = new FileInputStream(origen);
            OutputStream out = new FileOutputStream(destino);
            byte[] buf = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }catch(IOException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
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
        btnEditar = new javax.swing.JButton();
        btnDesactivar = new javax.swing.JButton();
        btnActivar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cboNumPag = new javax.swing.JComboBox<>();
        cboTotalRegPag = new javax.swing.JComboBox<>();
        btnSeleccionar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        txtID = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cboCategoria = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPrecioVenta = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        txtStock = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        lblImagen = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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

        btnEditar.setText("Editar");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnDesactivar.setText("Desactivar");
        btnDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesactivarActionPerformed(evt);
            }
        });

        btnActivar.setText("Activar");
        btnActivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarActionPerformed(evt);
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

        btnSeleccionar.setText("Seleccionar");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
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
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnActivar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDesactivar)))
                        .addGap(147, 147, 147)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(cboNumPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(133, 133, 133)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboTotalRegPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addGap(0, 226, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtNombreB, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNuevo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEditar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSeleccionar)))
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
                    .addComponent(btnEditar)
                    .addComponent(btnSeleccionar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTotalRegistros)
                    .addComponent(jLabel2)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnActivar)
                    .addComponent(btnDesactivar)
                    .addComponent(cboNumPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTotalRegPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        tabGeneral.addTab("Listado", jPanel1);

        jLabel3.setText("Descripcion: ");

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        jScrollPane2.setViewportView(txtDescripcion);

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

        jLabel5.setText("Nombre*: ");

        jLabel6.setText("Categoria*: ");

        jLabel7.setText("Codigo*: ");

        jLabel8.setText("PrecioVenta*: ");

        txtPrecioVenta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabel9.setText("Stock*: ");

        txtStock.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

        jLabel10.setText("Imagen*: ");

        lblImagen.setBackground(new java.awt.Color(255, 255, 255));
        lblImagen.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnAgregar.setText("Agregar");
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel7)
                        .addGap(30, 30, 30)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5)
                        .addGap(26, 26, 26)
                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel10)
                        .addGap(30, 30, 30)
                        .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(197, 197, 197)
                        .addComponent(btnAgregar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(jLabel4))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancelar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addGap(41, 41, 41)
                                .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(6, 6, 6)
                                .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel3)
                        .addGap(12, 12, 12)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel6))
                    .addComponent(cboCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel7))
                    .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel5))
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel8))
                    .addComponent(txtPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel9))
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(lblImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar)))
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

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // Obtener la informacion de la tabla al seleccionar o al hacer un click en la fila
        if(tablaListado.getSelectedRowCount() == 1){
            //obtenemos los valores de la fila seleccionada
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            //obtenemos el id de la categoria
            int categoriaId = Integer.parseInt(String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 1)));
            String categoriaNombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 2));
            String codigo = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 3));
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));
            this.nombreAnt = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));
            String precioVenta = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String stock = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String descripcion = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 2));
            this.imagenAnt = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 8));

            //mostramos el contenido
            txtID.setText(id);

            //seleccionar la categoria
            Categoria seleccionado = new Categoria(categoriaId, categoriaNombre);
            cboCategoria.setSelectedItem(seleccionado);
            txtCodigo.setText(codigo);
            txtNombre.setText(nombre);
            txtPrecioVenta.setText(precioVenta);
            txtStock.setText(stock);
            txtDescripcion.setText(descripcion);
            //programamos como cargar el nombre de la imagen
            ImageIcon im = new ImageIcon(this.directorio + this.imagenAnt);
            Icon icono = new ImageIcon(im.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
            lblImagen.setIcon(icono);
            lblImagen.repaint();

            tabGeneral.setEnabledAt(1,true);
            tabGeneral.setEnabledAt(0,false);
            tabGeneral.setSelectedIndex(1);
            this.accion = "Editar";
            btnGuardar.setText("Editar");
        }else{
            this.mensajeError("Selecciona un registro a editar");
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesactivarActionPerformed
        // programar la desactivacion de la categoria seleccionada
        if(tablaListado.getSelectedRowCount() == 1){
            //obtenemos los valores de la fila seleccionada
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));

            if(JOptionPane.showConfirmDialog(this, "¿Desea desactivar el registro: " + nombre + " ?","Sistema Ventas", JOptionPane.YES_NO_OPTION) == 0){
                String resp = this.CONTROL.desactivar(Integer.parseInt(id));
                if(resp == "OK"){
                    this.mensajeOk("Registro desactivado");
                    this.listar("", false);
                }else{
                    this.mensajeError(resp);
                }

            }

        }else{
            this.mensajeError("Selecciona un registro a desactivar");
        }
    }//GEN-LAST:event_btnDesactivarActionPerformed

    private void btnActivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarActionPerformed
        // programar la activacion de la categoria seleccionada
        if(tablaListado.getSelectedRowCount() == 1){
            //obtenemos los valores de la fila seleccionada
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));

            if(JOptionPane.showConfirmDialog(this, "¿Desea activar el registro: " + nombre + " ?","Sistema Ventas", JOptionPane.YES_NO_OPTION) == 0){
                String resp = this.CONTROL.activar(Integer.parseInt(id));
                if(resp == "OK"){
                    this.mensajeOk("Registro activado");
                    this.listar("", false);
                }else{
                    this.mensajeError(resp);
                }
            }

        }else{
            this.mensajeError("Selecciona un registro a activar");
        }
    }//GEN-LAST:event_btnActivarActionPerformed

    private void cboNumPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboNumPagActionPerformed
        //activar la primera carga si no esta activa
        if(this.primeraCarga == false){
            this.listar("", true);
        }
    }//GEN-LAST:event_cboNumPagActionPerformed

    private void cboTotalRegPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTotalRegPagActionPerformed
        // TODO add your handling code here:
        this.Paginar();
    }//GEN-LAST:event_cboTotalRegPagActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if(txtNombre.getText().length() == 0 || txtNombre.getText().length() > 100){
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre, no mayor a 100 caracteres", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        if(txtPrecioVenta.getText().length() == 0){
            JOptionPane.showMessageDialog(this, "Debe ingresar el precio de venta, es obligatorio "
                + "caracteres, es obligartorio", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtPrecioVenta.requestFocus();
            return;
        }
        if(txtStock.getText().length() == 0){
            JOptionPane.showMessageDialog(this, "Debe ingresar stock, es obligatorio "
                + "caracteres, es obligartorio", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtStock.requestFocus();
            return;
        }
        if(txtDescripcion.getText().length() > 255){
            JOptionPane.showMessageDialog(this, "Debe ingresar una descripcion, no mayor a 255 caracteres", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtDescripcion.requestFocus();
            return;
        }

        String resp;
        if(this.accion.equals("Editar")){
            //editar
            //declarar variable para la imagen actual
            String imagenActual = "";
            if(this.imagen.equals("")){
                imagenActual = this.imagenAnt;
            }else{
                imagenActual = this.imagen;
            }
            Categoria seleccionado = (Categoria)cboCategoria.getSelectedItem();
            resp = this.CONTROL.actualizar(Integer.parseInt(txtID.getText()), seleccionado.getId(), txtCodigo.getText(), txtNombre.getText(),
                this.nombreAnt, Double.parseDouble(txtPrecioVenta.getText()), Integer.parseInt(txtStock.getText()),
                txtDescripcion.getText(), this.imagen);
            if(resp.equals("OK")){
                if(!this.imagen.equals("")){
                    this.subirImagen();
                }
                this.mensajeOk("Articulo actualizado correctamente");
                this.Limpiar();
                this.listar("", false);
                tabGeneral.setEnabledAt(1,false);
                tabGeneral.setEnabledAt(0,true);
                tabGeneral.setSelectedIndex(0);
            }else{
                this.mensajeError(resp);
            }
        }else{
            //guardar
            Categoria seleccionado = (Categoria)cboCategoria.getSelectedItem();
            resp = this.CONTROL.insertar(seleccionado.getId(), txtCodigo.getText(), txtNombre.getText(),
                Double.parseDouble(txtPrecioVenta.getText()), Integer.parseInt(txtStock.getText()),
                txtDescripcion.getText(), this.imagen);
            if(resp.equals("OK")){
                if(!this.imagen.equals("")){
                    this.subirImagen();
                }
                this.mensajeOk("Articulo registrado correctamente");
                this.Limpiar();
                this.listar("", false);
            }else{
                this.mensajeError(resp);
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // TODO add your handling code here:
        tabGeneral.setEnabledAt(1, false);
        tabGeneral.setEnabledAt(0, true);

        //trasladamos al panel de mantemientos
        tabGeneral.setSelectedIndex(0);

        this.Limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        //declarar un objeto que hace referencia a la clase JFilesChooser
        JFileChooser file = new JFileChooser();
        int estado = file.showOpenDialog(this);
        if(estado == JFileChooser.APPROVE_OPTION){
            this.imagen = file.getSelectedFile().getName();
            this.rutaOrigen = file.getSelectedFile().getAbsolutePath();
            this.rutaDestino = this.directorio + this.imagen;
            ImageIcon im = new ImageIcon(this.rutaOrigen);
            Icon icono = new ImageIcon(im.getImage().getScaledInstance(lblImagen.getWidth(), lblImagen.getHeight(), Image.SCALE_DEFAULT));
            lblImagen.setIcon(icono);
            lblImagen.repaint();
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        //validar que se halla seleccionado un registro
        if(tablaListado.getSelectedRowCount() == 1){
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String codigo = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 3));
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));
            String precio = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            //String descuento = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));

            //this.vista.modeloDetalles.addRow(new Object[]{id, codigo, nombre, "1", precio, precio});
            this.vista.agregarDetalles(id, codigo, nombre, precio);
        }else{
            this.mensajeError("Debe seleccionar un articulo para agregar al detalle");
        }
    }//GEN-LAST:event_btnSeleccionarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivar;
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JComboBox<String> cboCategoria;
    private javax.swing.JComboBox<String> cboNumPag;
    private javax.swing.JComboBox<String> cboTotalRegPag;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblImagen;
    private javax.swing.JLabel lblTotalRegistros;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTable tablaListado;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreB;
    private javax.swing.JFormattedTextField txtPrecioVenta;
    private javax.swing.JFormattedTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
