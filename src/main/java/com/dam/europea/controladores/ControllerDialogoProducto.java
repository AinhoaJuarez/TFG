package com.dam.europea.controladores;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;
import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Proveedor;

import jakarta.persistence.TypedQuery;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Controlador para el diálogo de gestión de Producto
public class ControllerDialogoProducto implements Initializable {

    private SessionFactory sf; // Fábrica de sesiones de Hibernate
    private String codBarras; // Código de barras del producto
    @FXML
    private TextField txtCodBarras;
    @FXML
    private ComboBox<String> comboBoxFam;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtPrecioCompra;
    @FXML
    private TextField txtPrecioVenta;
    @FXML
    private TextField txtMargen;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<String> comboBoxProv;
    private Session session; // Sesión de Hibernate
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    private Producto p;
    private ControllerGI_Prods ct2; // Controlador para gestionar la tabla de productos

    // Constructor que recibe la fábrica de sesiones, el código de barras y el
    // controlador
    public ControllerDialogoProducto(SessionFactory sf, String codBarras, ControllerGI_Prods ct2) {
        this.sf = sf;
        this.codBarras = codBarras;
        this.ct2 = ct2;
    }

    // Inicializamos el controlador y configuramos los elementos de la interfaz
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = sf.openSession();
        session.beginTransaction();

        // Cargamos las familias de productos en el ComboBox
        TypedQuery<String> query = session.createQuery("SELECT fp.codFamilia FROM FamiliaProducto fp", String.class);
        List<String> codFamilias = query.getResultList();
        comboBoxFam.getItems().addAll(codFamilias);

        // Cargamos los proveedores en el ComboBox
        TypedQuery<String> query2 = session.createQuery("SELECT p.codigo FROM Proveedor p", String.class);
        List<String> codProveedor = query2.getResultList();
        comboBoxProv.getItems().addAll(codProveedor);

        // Obtener la ventana y establecer el título adecuado
        Stage stage = (Stage) btnAceptar.getScene().getWindow();

        // Si el código de barras no es nulo, cargamos los datos del producto
        if (codBarras != null) {
            p = session.find(Producto.class, codBarras);
            if (p != null) {
                txtCodBarras.setText(p.getCodigoBarras());
                comboBoxFam.setValue(p.getFamiliaArticulo().getCodFamilia());
                txtDescripcion.setText(p.getDescripcion());
                txtPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
                txtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
                txtMargen.setText(String.valueOf(p.getMargen()));
                txtStock.setText(String.valueOf(p.getStock()));
                comboBoxProv.setValue(p.getProveedorProducto().getCodigo());
                stage.setTitle("Modificar Producto");  // Cambiar título a "Modificar"
            }
        } else {
            stage.setTitle("Crear Producto");  // Cambiar título a "Crear"
        }

        // Configuramos el botón aceptar para crear o modificar un producto
        btnAceptar.setOnAction(event -> {
            if (areFieldsValid()) {
                session = sf.openSession();
                session.beginTransaction();
                if (codBarras == null) {
                    crearProducto();
                    showInformation("Producto creado con éxito.");
                } else {
                    modProducto(p);
                    showInformation("Producto modificado con éxito.");
                }
                closeWindow();
            } else {
                showWarning();
            }
        });

        btnCancelar.setOnAction(event -> closeWindow());

        // Listener para calcular el margen automáticamente cuando cambian los precios
        txtPrecioCompra.textProperty().addListener((observable, oldValue, newValue) -> calculateMargen());
        txtPrecioVenta.textProperty().addListener((observable, oldValue, newValue) -> calculateMargen());

        session.close();
    }

    // Método para calcular el margen
    private void calculateMargen() {
        try {
            double precioCompra = Double.parseDouble(txtPrecioCompra.getText());
            double precioVenta = Double.parseDouble(txtPrecioVenta.getText());
            double margen = precioVenta - precioCompra;
            txtMargen.setText(String.valueOf(margen));
        } catch (NumberFormatException e) {
            txtMargen.setText("");
        }
    }

    // Validamos que los campos no estén vacíos
    private boolean areFieldsValid() {
        return !txtCodBarras.getText().isEmpty() && !txtDescripcion.getText().isEmpty()
                && !txtPrecioCompra.getText().isEmpty() && !txtPrecioVenta.getText().isEmpty()
                && !txtMargen.getText().isEmpty() && !txtStock.getText().isEmpty();
    }

    // Mostramos una alerta si los campos están vacíos
    private void showWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Campos vacíos");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, complete todos los campos.");
        alert.showAndWait();
    }

    // Mostramos una información si la operación fue exitosa
    private void showInformation(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Método para crear un nuevo producto
    public void crearProducto() {
        Producto p = new Producto();
        p.setCodigoBarras(txtCodBarras.getText());
        String codFamilia = comboBoxFam.getValue();
        FamiliaProducto fp = session.find(FamiliaProducto.class, codFamilia);
        p.setFamiliaArticulo(fp);
        p.setDescripcion(txtDescripcion.getText());
        p.setPrecioCompra(Double.valueOf(txtPrecioCompra.getText()));
        p.setPrecioVenta(Double.valueOf(txtPrecioVenta.getText()));
        p.setMargen(Double.valueOf(txtMargen.getText()));
        p.setStock(Integer.valueOf(txtStock.getText()));
        String codProveedor = comboBoxProv.getValue();
        Proveedor pv = session.find(Proveedor.class, codProveedor);
        p.setProveedorProducto(pv);

        session.persist(p);
        session.getTransaction().commit();
    }

    // Método para modificar un producto existente
    public void modProducto(Producto p) {
        p.setCodigoBarras(txtCodBarras.getText());
        String codFamilia = comboBoxFam.getValue();
        FamiliaProducto fp = session.find(FamiliaProducto.class, codFamilia);
        p.setFamiliaArticulo(fp);
        p.setDescripcion(txtDescripcion.getText());
        p.setPrecioCompra(Double.valueOf(txtPrecioCompra.getText()));
        p.setPrecioVenta(Double.valueOf(txtPrecioVenta.getText()));
        p.setMargen(Double.valueOf(txtMargen.getText()));
        p.setStock(Integer.valueOf(txtStock.getText()));
        String codProveedor = comboBoxProv.getValue();
        Proveedor pv = session.find(Proveedor.class, codProveedor);
        p.setProveedorProducto(pv);

        session.merge(p);
        session.getTransaction().commit();
    }

    // Método para cerrar la ventana
    private void closeWindow() {
        ct2.cargarTabla(); // Recargamos la tabla en el controlador principal
        if (session != null && session.isOpen()) {
            session.close();
        }
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
