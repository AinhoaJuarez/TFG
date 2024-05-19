# Aplicación de Gestión de Librerías

¡Bienvenido a la Aplicación de Gestión de Librerías! Esta aplicación está diseñada para optimizar las operaciones de una librería, proporcionando funcionalidades para la gestión de inventarios y la emisión de tickets.

## Tabla de Contenidos

- [Características](#características)
- [Instalación](#instalación)
- [Uso](#uso)

## Características

- **Gestión de Inventarios**:
  - Añadir, modificar y eliminar registros de productos, asociándolos a su correspondiente proveedor o familia.
  - Rastrear detalles de los tickets y facturas emitidos.
  - Añadir, modificar y eliminar clientes.
  - Creación de usuarios de la aplicación con su correspondiente rol y permiso.

- **Sistema de Emisión de Tickets**:
  - Emitir tickets para ventas de libros.
  - Rastrear las compras de los clientes.
  - Generar informes de ventas.


## Instalación

### Requisitos Previos

- Java Development Kit (JDK) 11 o superior
- Apache Maven
- Hibernate ORM
- JavaFX

### Pasos

1. **Clonar el Repositorio**:

    ```bash
    git clone https://github.com/yourusername/bookstore-management.git
    cd bookstore-management
    ```

2. **Construir el Proyecto**:

    ```bash
    mvn clean install
    ```

3. **Ejecutar la Aplicación**:

    ```bash
    mvn javafx:run
    ```

## Uso

### Iniciar la Aplicación

Después de ejecutar la aplicación, aparecerá la ventana principal, que ofrece opciones para gestionar el inventario y manejar la emisión de tickets y facturas.

### Emisión de Tickets

- **Emitir Ticket**: Ve a la sección de emisión de tickets, selecciona los productos que se van a vender y emite un ticket.
- 
### Emisión de Facturas

- **Emitir Factura**: Ve a la sección de emisión de factura, selecciona los productos que se van a vender y emite una factura.


- **Gestión de Inventarios**:
  - **Tabla de Productos**: Añadir, modificar y eliminar registros de productos.
  - **Tabla de Familia de Productos**: Clasificación y gestión de categorías de productos.
  - **Tabla de Proveedores**: Gestión de información y contacto de proveedores.
  - **Tabla de Clientes**: Gestión de información de clientes y seguimiento de compras.
  - **Tabla de Usuarios de la Aplicación**: Gestión de usuarios que tienen acceso a la aplicación, con sus correspondientes roles.
  - **Tabla de Gestión de Tickets**: Registro y seguimiento de ventas de productos.
  - **Tabla de Gestión de Facturas**: Emisión y seguimiento de facturas generadas.

