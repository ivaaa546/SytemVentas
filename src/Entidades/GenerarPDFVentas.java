/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;
import database.Conectar;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Font;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author pc
 */
public class GenerarPDFVentas {
     public static void generatePDF() {
      Document document = new Document();
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "ReporteVentas_" + timestamp + ".pdf";
            
            // Asocia el documento con PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();
            
            // Encabezado centrado
            String texto = "Reporte Ventas";
            Paragraph parrafo = new Paragraph(texto, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD));
            parrafo.setAlignment(Element.ALIGN_CENTER);
            parrafo.setSpacingAfter(20f);
            document.add(parrafo);
            
            // Tabla de 7 columnas
            PdfPTable tabla = new PdfPTable(10);
            tabla.setWidthPercentage(100);
            tabla.addCell("Id");
            tabla.addCell("usuario");
            tabla.addCell("cliente");
            tabla.addCell("Tipo Comprobante");
            tabla.addCell("Serie");
            tabla.addCell("No. Comprobante");
            tabla.addCell("Fecha");
            tabla.addCell("Impuesto");
            tabla.addCell("Total");
            tabla.addCell("Estado");
            
            // Conexión a la base de datos y llenado de la tabla
            Connection conn = Conectar.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT v.id, p.nombre AS cliente, u.nombre AS usuario, v.tipo_comprobante, v.serie_comprobante, v.num_comproante, " +
    "v.fecha, v.impuesto, v.total, v.estado " +
    "FROM venta v " +
    "INNER JOIN persona p ON v.persona_id = p.id " +
    "INNER JOIN usuario u ON usuario_id= u.id");

            // Agregar las filas al PDF
            while (rs.next()) {
                tabla.addCell(rs.getString("id"));
                tabla.addCell(rs.getString("cliente"));
                tabla.addCell(rs.getString("usuario"));
                tabla.addCell(rs.getString("tipo_comprobante"));
                tabla.addCell(rs.getString("serie_comprobante"));
                tabla.addCell(rs.getString("num_comproante"));
                tabla.addCell(rs.getString("fecha"));
                tabla.addCell(rs.getString("impuesto"));
                tabla.addCell(rs.getString("total"));
                tabla.addCell(rs.getString("estado"));
            }
            document.add(tabla); // Agregar tabla al documento
            
            // Cerrar conexiones y el documento
            rs.close();
            stmt.close();
            conn.close();
            document.close();
            
            System.out.println("PDF generado: " + nombreArchivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
   }
     
      public static void generatePDF2() {
       try {
           Document document = new Document();
           PdfWriter.getInstance(document, new FileOutputStream("ReporteVenta.pdf"));
           document.open();
           
           // Recupera los datos de la base de datos y agrega al PDF
           Connection conn = Conectar.getConnection();
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("select a.id, a.codigo, a.nombre, d.cantidad, d.precio, d.descuento, (d.cantidad * precio) as sub_total from detalle_venta d inner join articulo a on d.articulo_id = a.id");
           
           while (rs.next()) {
               String id = rs.getString("id");
               String codigo = rs.getString("codigo");
               String articulo = rs.getString("nombre");
               String cantidad = rs.getString("cantidad");
               String precio = rs.getString("precio");
               String descuento =rs.getString("descuento");
               String subtotal = rs.getString("sub_total");
               
               // Agrega las columnas al PDF
               document.add(new Paragraph("id: " + id));
               document.add(new Paragraph("codigo: " + codigo));
               document.add(new Paragraph("nombre: " + articulo));
               document.add(new Paragraph("cantidad: " + cantidad));
               document.add(new Paragraph("precio: " + precio));
               document.add(new Paragraph("descuento: " + descuento));
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
