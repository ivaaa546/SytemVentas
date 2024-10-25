/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package Presentacion;

import Negocio.PersonaControl;
import javax.swing.JOptionPane;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author josec
 */
public class frmSeleccionarClienteVenta extends javax.swing.JDialog {

    //declaramos variable de tipo onjeto para generar una instancia al formulario ingreso
    private frmVentas vista;
    
    //generamos las instancias a la clase RolControl
    private final PersonaControl CONTROL;
    private String accion;
    private String nombreAnt;
    
    //declaramos las variables para el control del paginamiento
    private int totalPagina = 10;
    private int numPag = 1;
    private boolean primeraCarga = true;
    private int totalReg;
    
    public frmSeleccionarClienteVenta(java.awt.Frame parent, frmVentas frm, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        this.vista = frm;
        this.setTitle("Seleccione un cliente para el egreso a almacen");
        
        this.CONTROL = new PersonaControl();
        this.listar("", false);
        this.primeraCarga = false;
        
        //deshabilitamos el panelde mantenimiento al cargar el formulario
        tabGeneral.setEnabledAt(1, false);
        
        this.accion = "Guardar";
        this.txtID.setVisible(false);
        
        this.Paginar();
        
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

    //metodo para listar los elementos de la tabla categoria
    private void listar(String texto, boolean paginar){
        //actualizar los valores para paginar
        this.totalPagina = Integer.parseInt((String)cboTotalRegPag.getSelectedItem());
        //validamos que el valor no sea nulo
        if(cboNumPag.getSelectedItem() != null){
            this.numPag = Integer.parseInt((String)cboNumPag.getSelectedItem());
        }
        if(paginar == true){
            tablaListado.setModel(this.CONTROL.listarTipo(texto, this.totalPagina, this.numPag, "Cliente"));
        }else{
            tablaListado.setModel(this.CONTROL.listarTipo(texto, this.totalPagina, 1, "Cliente"));
        }
        
        //agregamos un metodo para el ordenamiento de los registros deacuerdoa a la columna seleccionada
        TableRowSorter orden = new TableRowSorter(tablaListado.getModel());
        tablaListado.setRowSorter(orden);
        lblTotalRegistros.setText("Mostrando: " + this.CONTROL.totalMostrados() + " de un total de " + this.CONTROL.total() + " registros.");
    }
    
    //metodo para el control de los mensajes de error y ok
    private void mensajeError(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.ERROR_MESSAGE);//26.00
    }
    
    private void mensajeOk(String mensaje){
        JOptionPane.showMessageDialog(this, mensaje, "Sistema Ventas", JOptionPane.OK_OPTION);//26.00
    }
    
    private void Limpiar(){
        txtNombre.setText("");
        txtEmail.setText("");
        txtNumDoc.setText("");
        txtID.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        this.accion = "Guardar";
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
        btnGuardar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cboTipoDoc = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        txtNumDoc = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();

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
                        .addGap(0, 234, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel5.setText("Tipo Documento*: ");

        jLabel7.setText("Nombre*: ");

        jLabel8.setText("Num Documento: ");

        jLabel9.setText("Telefono: ");

        cboTipoDoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "DPI", "Pasaporte", "NIT" }));

        jLabel12.setText("Direccion: ");

        jLabel10.setText("Email*: ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel8)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabel9))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(cboTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNumDoc, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 342, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(95, 95, 95)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(btnGuardar)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancelar)))))
                .addContainerGap(342, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cboTipoDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtNumDoc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10)
                            .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(50, 50, 50)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGuardar)
                    .addComponent(btnCancelar))
                .addContainerGap(222, Short.MAX_VALUE))
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
            //obtenemos el id del rol
            String tipoPersona = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 1));
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 2));
            this.nombreAnt = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 2));
            String tipoDoc = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 3));
            String numDoc = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 4));
            String direccion = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 5));
            String telefono = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 6));
            String email = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 7));

            //mostramos el contenido
            txtID.setText(id);

            //seleccionar la categoria
            txtNombre.setText(nombre);
            cboTipoDoc.setSelectedItem(tipoDoc);
            txtNumDoc.setText(numDoc);
            txtDireccion.setText(direccion);
            txtTelefono.setText(telefono);
            txtEmail.setText(email);

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
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 2));

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
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 2));

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

        if(txtNombre.getText().length() == 0 || txtNombre.getText().length() > 70){
            JOptionPane.showMessageDialog(this, "Debe ingresar un nombre, no mayor a 70 caracteres", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        if(txtEmail.getText().length() > 50){
            JOptionPane.showMessageDialog(this, "Debe ingresar un email, no mayor a 50 caracteres", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }
        if(txtNumDoc.getText().length() > 20){
            JOptionPane.showMessageDialog(this, "Debe ingresar un numero de documento, no mayor a 20 caracteres", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtNumDoc.requestFocus();
            return;
        }
        if(txtDireccion.getText().length() > 70){
            JOptionPane.showMessageDialog(this, "Debe ingresar una direccion, no mayor a 70 caracteres", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtDireccion.requestFocus();
            return;
        }
        if(txtTelefono.getText().length() > 15){
            JOptionPane.showMessageDialog(this, "Debe ingresar un numero de telefono, no mayor a 15 caracteres", "Sistema de Ventas", JOptionPane.WARNING_MESSAGE);
            txtTelefono.requestFocus();
            return;
        }

        String resp;
        if(this.accion.equals("Editar")){
            //editar
            resp = this.CONTROL.actualizar(Integer.parseInt(txtID.getText()), "Cliente", txtNombre.getText(), this.nombreAnt,
                (String)cboTipoDoc.getSelectedItem(), txtNumDoc.getText(), txtDireccion.getText(),
                txtTelefono.getText(), txtEmail.getText());
            if(resp.equals("OK")){
                this.mensajeOk("Usuario actualizado correctamente");
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
            resp = this.CONTROL.insertar("Cliente", txtNombre.getText(),
                (String)cboTipoDoc.getSelectedItem(), txtNumDoc.getText(), txtDireccion.getText(),
                txtTelefono.getText(), txtEmail.getText());
            if(resp.equals("OK")){
                this.mensajeOk("Usuario registrado correctamente");
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

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
        //validar que se halla seleccionado un registro
        if(tablaListado.getSelectedRowCount() == 1){
            String id = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 0));
            String nombre = String.valueOf(tablaListado.getValueAt(tablaListado.getSelectedRow(), 2));

            this.vista.txtIdCliente.setText(id);
            this.vista.txtNombreCliente.setText(nombre);
            this.setVisible(false);
        }else{
            this.mensajeError("Debe seleccionar un cliente");
        }
    }//GEN-LAST:event_btnSeleccionarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDesactivar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JComboBox<String> cboNumPag;
    private javax.swing.JComboBox<String> cboTipoDoc;
    private javax.swing.JComboBox<String> cboTotalRegPag;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalRegistros;
    private javax.swing.JTabbedPane tabGeneral;
    private javax.swing.JTable tablaListado;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtNombreB;
    private javax.swing.JTextField txtNumDoc;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
