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
import javax.swing.JTable;
/**
 *
 * @author pc
 */
public class GenerarPDFCompras {
        public static void generatePDF() {
             Document document = new Document();
       try {
          
           String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "ReporteCompras" + timestamp + ".pdf";
           document.open();
           // Asocia el documento con PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();
            
             // Encabezado centrado
            String texto = "Reporte Compras";
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
            
           // Recupera los datos de la base de datos y agrega al PDF
           Connection conn = Conectar.getConnection();
           Statement stmt = conn.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT v.id, p.nombre AS cliente, u.nombre AS usuario, v.tipo_comprobante, v.serie_comprobante, v.num_comproante, " +
    "v.fecha, v.impuesto, v.total, v.estado " +
    "FROM ingreso v " +
    "INNER JOIN persona p ON v.persona_id = p.id " +
    "INNER JOIN usuario u ON usuario_id= u.id");
           
           while (rs.next()) {
                tabla.addCell(rs.getString("id"));
                tabla.addCell(rs.getString("usuario"));
                tabla.addCell(rs.getString("cliente"));
                tabla.addCell(rs.getString("tipo_comprobante"));
                tabla.addCell(rs.getString("serie_comprobante"));
                tabla.addCell(rs.getString("num_comproante"));
                tabla.addCell(rs.getString("fecha"));
                tabla.addCell(rs.getString("impuesto"));
                tabla.addCell(rs.getString("total"));
                tabla.addCell(rs.getString("estado"));
           }
           document.add(tabla); // Agregar tabla al documento
            
           document.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
   
        
         public static void generatePDF2(JTable table) {
        Document document = new Document();
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "Comprobante Compras" + timestamp + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();
            
            String texto = "Comprobante Compras";
            Paragraph parrafo = new Paragraph(texto);
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.setSpacingAfter(20f);
            document.add(parrafo);
            
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);

            // Agregar encabezados
            for (int i = 0; i < table.getColumnCount(); i++) {
                pdfTable.addCell(table.getColumnName(i));
            }

            // Agregar datos de las filas
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    pdfTable.addCell(table.getValueAt(row, col).toString());
                }
            }

            document.add(pdfTable);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        
   /*public static void main(String[] args) {
       generatePDF();
   }*/
        public static void generatePDF3(JTable table) {
        Document document = new Document();
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String nombreArchivo = "Reporte Compras" + timestamp + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo));
            document.open();
            
            String texto = "Reporte Compras";
            Paragraph parrafo = new Paragraph(texto);
            parrafo.setAlignment(Paragraph.ALIGN_CENTER);
            parrafo.setSpacingAfter(20f);
            document.add(parrafo);
            
            PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
            pdfTable.setWidthPercentage(100);

            // Agregar encabezados
            for (int i = 0; i < table.getColumnCount(); i++) {
                pdfTable.addCell(table.getColumnName(i));
            }

            // Agregar datos de las filas
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    pdfTable.addCell(table.getValueAt(row, col).toString());
                }
            }

            document.add(pdfTable);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
