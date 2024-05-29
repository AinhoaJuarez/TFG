package com.dam.europea.entidades;

import java.util.ArrayList;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    // Definimos el ID de la entidad Cliente, que es el DNI, con restricciones de unicidad y no nulidad
    @Id
    @Column(name = "DNI", unique = true, nullable = false)
    private String dni;
    
    // Definimos las columnas de la tabla Cliente
    private String direccion;
    private String codPos;
    private String localidad;
    private String nombre;
    
    
    @OneToMany(mappedBy = "numeroFactura", cascade = CascadeType.PERSIST)
    private ArrayList<Factura> facturas;
    
   
    @OneToMany(mappedBy = "numTicket", cascade = CascadeType.PERSIST)
    private ArrayList<Ticket> tickets;
    
    // Constructor con parámetros para inicializar todos los campos de la clase Cliente
    public Cliente(String dni, String direccion, String codPos, String localidad, String nombre, ArrayList<Factura> facturas,
                   ArrayList<Ticket> tickets) {
        super();
        this.dni = dni;
        this.direccion = direccion;
        this.codPos = codPos;
        this.localidad = localidad;
        this.nombre = nombre;
        this.facturas = facturas;
        this.tickets = tickets;
    }
    
    // Métodos getters y setters para cada uno de los campos de la clase Cliente
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCodPos() {
        return codPos;
    }

    public void setCodPos(String codPos) {
        this.codPos = codPos;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Constructor con parámetros para inicializar DNI y listas de facturas y tickets
    public Cliente(String dni, ArrayList<Factura> facturas, ArrayList<Ticket> tickets) {
        super();
        this.dni = dni;
        this.facturas = facturas;
        this.tickets = tickets;
    }

    // Constructor con parámetro para inicializar solo el DNI
    public Cliente(String dni) {
        super();
        this.dni = dni;
    }

    // Constructor vacío necesario para JPA
    public Cliente() {
        super();
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public ArrayList<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(ArrayList<Factura> facturas) {
        this.facturas = facturas;
    }

    public ArrayList<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }
}
