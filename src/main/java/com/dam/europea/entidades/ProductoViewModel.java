package com.dam.europea.entidades;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ProductoViewModel {

    // Definimos las propiedades para los campos del modelo de vista de Producto
    private StringProperty codigoBarras;
    private StringProperty descripcion;
    private ObjectProperty<FamiliaProducto> familiaProducto;
    private ObjectProperty<Proveedor> proveedor;
    private DoubleProperty precioCompra;
    private DoubleProperty precioVenta;
    private DoubleProperty margen;
    private IntegerProperty cantidad;
    private IntegerProperty stock;

    // Métodos getters y setters para la propiedad proveedor
    public ObjectProperty<Proveedor> getProveedor() {
        return proveedor;
    }

    public void setProveedor(ObjectProperty<Proveedor> proveedor) {
        this.proveedor = proveedor;
    }

    // Métodos getters y setters para las propiedades del modelo de vista de Producto
    public StringProperty getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(StringProperty codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public StringProperty getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(StringProperty descripcion) {
        this.descripcion = descripcion;
    }

    public ObjectProperty<FamiliaProducto> getFamiliaProducto() {
        return familiaProducto;
    }

    public void setFamiliaProducto(ObjectProperty<FamiliaProducto> familiaProducto) {
        this.familiaProducto = familiaProducto;
    }

    public DoubleProperty getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(DoubleProperty precioCompra) {
        this.precioCompra = precioCompra;
    }

    public DoubleProperty getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(DoubleProperty precioVenta) {
        this.precioVenta = precioVenta;
    }

    public DoubleProperty getMargen() {
        return margen;
    }

    public void setMargen(DoubleProperty margen) {
        this.margen = margen;
    }

    public IntegerProperty getCantidad() {
        return cantidad;
    }

    public void setCantidad(IntegerProperty cantidad) {
        this.cantidad = cantidad;
    }

    public IntegerProperty getStock() {
        return stock;
    }

    public void setStock(IntegerProperty stock) {
        this.stock = stock;
    }

    // Constructor por defecto para inicializar las propiedades con valores predeterminados
    public ProductoViewModel() {
        this.codigoBarras = new SimpleStringProperty();
        this.descripcion = new SimpleStringProperty();
        this.familiaProducto = new SimpleObjectProperty<>();
        this.proveedor = new SimpleObjectProperty<>();
        this.precioCompra = new SimpleDoubleProperty();
        this.precioVenta = new SimpleDoubleProperty();
        this.margen = new SimpleDoubleProperty();
        this.cantidad = new SimpleIntegerProperty();
        this.stock = new SimpleIntegerProperty();
    }
}
