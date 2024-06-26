package com.dam.europea.entidades;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Factura {
    
    // Definimos el ID de la factura
    @Id
    private int numeroFactura;
    
    @ManyToOne
    @JoinColumn(name = "ClienteAsociado_fk", referencedColumnName= "DNI")
    private Cliente cliente;

    // Definimos las columnas de la tabla Factura
    @Column
    private LocalDate fechaExpedicion;
    private LocalDate fechaOperacion;
    private String NIF;
    private String direccion;
    private int telefono;
    private String nombreApellidos;
    
    @OneToMany(mappedBy = "numeroFactura", cascade = {CascadeType.ALL})
	private List<TicketProductos> ticketProductos = new ArrayList<>();
    
    private double IVA;
    private double totalSinIVA;
    private double totalConIVA;
    private double descuento;

    // Constructor con parámetros para inicializar todos los campos de la clase Factura
    public Factura(int numeroFactura, Cliente cliente, LocalDate fechaExpedicion, LocalDate fechaOperacion, String nIF,
                   String direccion, int telefono, String nombreApellidos, List<TicketProductos> ticketProductos, int iVA,
                   double totalSinIVA, double totalConIVA, double descuento) {
        super();
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.fechaExpedicion = fechaExpedicion;
        this.fechaOperacion = fechaOperacion;
        NIF = nIF;
        this.direccion = direccion;
        this.telefono = telefono;
        this.nombreApellidos = nombreApellidos;
        this.ticketProductos = ticketProductos;
        IVA = iVA;
        this.totalSinIVA = totalSinIVA;
        this.totalConIVA = totalConIVA;
        this.descuento = descuento;
    }

    // Constructor vacío necesario para JPA
    public Factura() {
    }

    // Métodos getters y setters para cada uno de los campos de la clase Factura
    public int getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDate getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(LocalDate fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public LocalDate getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(LocalDate fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public String getNIF() {
        return NIF;
    }

    public void setNIF(String nIF) {
        NIF = nIF;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNombreApellidos() {
        return nombreApellidos;
    }

    public void setNombreApellidos(String nombreApellidos) {
        this.nombreApellidos = nombreApellidos;
    }

    public List<TicketProductos> getListadoProductos() {
        return ticketProductos;
    }

    public void setListadoProductos(List<TicketProductos> ticketProductos) {
        this.ticketProductos = ticketProductos;
    }

    public double getIVA() {
        return IVA;
    }

    public void setIVA(double iVA) {
        IVA = iVA;
    }

    public double getTotalSinIVA() {
        return totalSinIVA;
    }

    public void setTotalSinIVA(double totalSinIVA) {
        this.totalSinIVA = totalSinIVA;
    }

    public double getTotalConIVA() {
        return totalConIVA;
    }

    public void setTotalConIVA(double totalConIVA) {
        this.totalConIVA = totalConIVA;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }
}
