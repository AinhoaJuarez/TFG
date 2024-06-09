package com.dam.europea.controladores;

import java.io.IOException;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.dam.europea.entidades.DatosEmpresa;
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
			Session session = sf.openSession();
			Query<DatosEmpresa> query2 = session.createQuery("select dt from DatosEmpresa dt where dt.id = 1",
					DatosEmpresa.class);
			DatosEmpresa dt = query2.getSingleResult();
			PdfWriter writer = new PdfWriter(dest);
			PdfDocument pdf = new PdfDocument(writer);
			Document document = new Document(pdf);

			document.add(new Paragraph("Factura").setBold().setFontSize(18).setMarginBottom(20));
			document.add(new Paragraph("Número de Factura: " + factura.getNumeroFactura()));
			document.add(new Paragraph("Fecha de Operación: " + factura.getFechaOperacion().toString()));
			document.add(new Paragraph("Fecha de Expedición: " + factura.getFechaExpedicion().toString()));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Datos del emitente").setBold().setFontSize(16));
			document.add(new Paragraph("NIF: " + dt.getNif()));
			document.add(new Paragraph("Nombre Empresa: " + dt.getNombreEmpresa()));
			document.add(new Paragraph("Nombre: " + dt.getNombreDueno()));
			document.add(new Paragraph(
					"Dirección: " + dt.getDireccion() + ", " + dt.getLocalidad() + ", " + dt.getCodigoPostal()));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("Datos del cliente").setBold().setFontSize(16));
			document.add(new Paragraph("DNI: " + factura.getCliente().getDni()));
			document.add(new Paragraph("Nombre: " + factura.getCliente().getNombre()));
			document.add(new Paragraph("Dirección: " + factura.getCliente().getDireccion() + ", "
					+ factura.getCliente().getLocalidad() + ", " + factura.getCliente().getCodPos()));
			
			document.add(new Paragraph("\n"));

			float[] columnWidths = { 5, 1, 2, 2 };
			Table table = new Table(columnWidths);

			table.addHeaderCell(new Cell().add(new Paragraph("Cantidad")));
			table.addHeaderCell(new Cell().add(new Paragraph("Descripción")));
			table.addHeaderCell(new Cell().add(new Paragraph("Precio Unitario")));
			table.addHeaderCell(new Cell().add(new Paragraph("Total")));

			TypedQuery<TicketProductos> query = session.createQuery(
					"SELECT tp FROM TicketProductos tp WHERE tp.numeroFactura=:factura", TicketProductos.class);
			query.setParameter("factura", factura);
			List<TicketProductos> ticketProductosList = query.getResultList();
			for (TicketProductos tp : ticketProductosList) {
				table.addCell(new Cell().add(new Paragraph(tp.getDescripcion())));
				table.addCell(new Cell().add(new Paragraph(String.valueOf(tp.getPrecioTotal()))));
				table.addCell(new Cell().add(new Paragraph(String.valueOf(tp.getCantidad()))));
				table.addCell(new Cell().add(new Paragraph(String.valueOf(tp.getPrecioTotal() * tp.getCantidad()))));
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