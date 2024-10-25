/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;
import database.Conectar;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 *
 * @author pc
 */
public class GenerarPDFCompras {
        public static void generatePDF() {
       try {
           Document document = new Document();
           PdfWriter.getInstance(document, new FileOutputStream("ReporteCompra.pdf"));
           document.open();
           
           // Recupera los datos de la base de datos y agrega al PDF
           Connection conn = Conectar.getConnection();
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("select a.id, a.codigo, a.nombre, d.cantidad, d.precio, (d.cantidad * precio) as sub_total from detalle_ingreso d inner join articulo a on d.articulo_id = a.id");
           
           while (rs.next()) {
               String id = rs.getString("id");
               String codigo = rs.getString("codigo");
               String articulo = rs.getString("nombre");
               String cantidad = rs.getString("cantidad");
               String precio = rs.getString("precio");
               String subtotal = rs.getString("sub_total");
               
               // Agrega las columnas al PDF
               document.add(new Paragraph("id: " + id));
               document.add(new Paragraph("codigo: " + codigo));
               document.add(new Paragraph("nombre: " + articulo));
               document.add(new Paragraph("cantidad: " + cantidad));
               document.add(new Paragraph("precio: " + precio));
               document.add(new Paragraph("sub_total: " + subtotal));
           }
           
           document.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
        
         public static void generatePDF2() {
       try {
           Document document = new Document();
           PdfWriter.getInstance(document, new FileOutputStream("ReporteCompra.pdf"));
           document.open();
           
           // Recupera los datos de la base de datos y agrega al PDF
           Connection conn = Conectar.getConnection();
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("select a.id, a.codigo, a.nombre, d.cantidad, d.precio, (d.cantidad * precio) as sub_total from detalle_ingreso d inner join articulo a on d.articulo_id = a.id");
           
           while (rs.next()) {
               String id = rs.getString("id");
               String codigo = rs.getString("codigo");
               String articulo = rs.getString("nombre");
               String cantidad = rs.getString("cantidad");
               String precio = rs.getString("precio");
               String subtotal = rs.getString("sub_total");
               
               // Agrega las columnas al PDF
               document.add(new Paragraph("id: " + id));
               document.add(new Paragraph("codigo: " + codigo));
               document.add(new Paragraph("nombre: " + articulo));
               document.add(new Paragraph("cantidad: " + cantidad));
               document.add(new Paragraph("precio: " + precio));
               document.add(new Paragraph("sub_total: " + subtotal));
           }
           
           document.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
        
   /*public static void main(String[] args) {
       generatePDF();
   }*/
}
