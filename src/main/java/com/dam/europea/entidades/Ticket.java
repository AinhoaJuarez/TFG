package com.dam.europea.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tickets")
public class Ticket {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int numTicket;
	@Column
	private Date fecha;
	private double importeTotal;
	private double descuentoTicket;
	private double importeTicket;

	@ManyToOne
	@JoinColumn(name = "ClienteAsociado_fk", referencedColumnName = "DNI")
	private Cliente cliente;

	@OneToMany(mappedBy = "numTicket", cascade = CascadeType.ALL)
    private List<TicketProductos> ticketProductos = new ArrayList<>();


	public Ticket(int numTicket, Date fecha, double importeTotal, Cliente cliente, ArrayList<TicketProductos>  ticketProductos, double descuentoTicket,double importeTicket ) {
		super();
		this.numTicket = numTicket;
		this.fecha = fecha;
		this.importeTotal = importeTotal;
		this.cliente = cliente;
		this.ticketProductos = ticketProductos;
		this.importeTicket = importeTicket;
		this.descuentoTicket = descuentoTicket;
		
	}

	public Ticket() {
	}

	public int getNumTicket() {
		return numTicket;
	}

	public void setNumTicket(int numTicket) {
		this.numTicket = numTicket;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public double getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setDescuentoTicket(double descuentoTicket) {
		this.descuentoTicket = descuentoTicket;
	}

	public double getImporteTicket() {
		return importeTicket;
	}

	public void setImporteTicket(double importeTicket) {
		this.importeTicket = importeTicket;
	}

	public double getDescuentoTicket() {
		return descuentoTicket;

	}


}
