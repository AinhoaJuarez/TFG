package com.dam.europea.entidades;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Proveedores")
public class Proveedor {
    
    // Definimos el ID de la entidad Proveedor
    @Id
    private String codigo;

    
    @Column
    private String nombre;

   
    @OneToMany(mappedBy = "proveedorProducto", cascade = CascadeType.PERSIST)
    private List<Producto> productosAsociados = new ArrayList<>();

    // Métodos getters y setters para la lista de productos asociados
    public List<Producto> getProductosAsociados() {
        return productosAsociados;
    }

    public void setProductosAsociados(List<Producto> productosAsociados) {
        this.productosAsociados = productosAsociados;
    }

    // Constructor por defecto necesario para JPA
    public Proveedor() {
    }

    // Métodos getters y setters para los campos de la clase Proveedor
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
