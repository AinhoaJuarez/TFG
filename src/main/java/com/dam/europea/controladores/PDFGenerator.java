package com.dam.europea.controladores;
import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Factura;
import com.dam.europea.entidades.TicketProductos;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import jakarta.persistence.TypedQuery;

public class PDFGenerator {

    public static void createFacturaPDF(Factura factura, String dest, SessionFactory sf) {
        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("Factura")
                    .setBold()
                    .setFontSize(18)
                    .setMarginBottom(20));

            document.add(new Paragraph("Cliente: " + factura.getCliente().getNombre()));
            document.add(new Paragraph("Fecha de Expedición: " + factura.getFechaExpedicion().toString()));
            document.add(new Paragraph("Número de Factura: " + factura.getNumeroFactura()));
            document.add(new Paragraph("\n"));

            float[] columnWidths = {1, 5, 2, 2, 2};
            Table table = new Table(columnWidths);

            table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")));
            table.addHeaderCell(new Cell().add(new Paragraph("Descripción")));
            table.addHeaderCell(new Cell().add(new Paragraph("Precio Unitario")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total")));
            Session session = sf.openSession();
            TypedQuery<TicketProductos> query = session.createQuery(
					"SELECT tp FROM TicketProductos tp WHERE tp.numeroFactura=:factura", TicketProductos.class);
			query.setParameter("factura", factura);
			List<TicketProductos> ticketProductosList = query.getResultList();
            for (TicketProductos tp : ticketProductosList) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(tp.getCantidad()))));
                table.addCell(new Cell().add(new Paragraph(tp.getDescripcion())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(tp.getPrecioTotal()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(tp.getPrecioTotal()*tp.getCantidad()))));
            }

            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("Total sin IVA: " + factura.getTotalSinIVA()));
            document.add(new Paragraph("IVA: " + factura.getIVA()));
            document.add(new Paragraph("Total con IVA: " + factura.getTotalConIVA()));

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}