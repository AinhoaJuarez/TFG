package com.dam.europea.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TicketProductos")
public class TicketProductos {

    // Definimos el ID de la entidad TicketProductos, con generación automática de valores
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @OneToOne
    @JoinColumn(name = "numTicket")
    private Ticket numTicket;

    
    @OneToOne
    @JoinColumn(name = "numeroFactura")
    private Factura numerofactura;

    
    @ManyToOne
    @JoinColumn(name = "codigoBarras")
    private Producto producto;

    // Columnas para la cantidad, el descuento y el precio con descuento de los productos del ticket
    @Column
    private int cantidad;
    private double descuento;
    private double precioDescuento;

    // Constructor vacío necesario para JPA
    public TicketProductos() {
    }

    // Métodos getters y setters para cada uno de los campos de la clase TicketProductos
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ticket getTicket() {
        return numTicket;
    }

    public void setTicket(Ticket ticket) {
        this.numTicket = ticket;
    }

    public Factura getFactura() {
        return numerofactura;
    }

    public void setFactura(Factura numerofactura) {
        this.numerofactura = numerofactura;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public double getPrecioDescuento() {
        return precioDescuento;
    }

    public void setPrecioDescuento(double precioDescuento) {
        this.precioDescuento = precioDescuento;
    }
}
