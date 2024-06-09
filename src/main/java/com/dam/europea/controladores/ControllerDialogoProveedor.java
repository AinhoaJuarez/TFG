package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import com.dam.europea.entidades.Proveedor;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Controlador para el diálogo de gestión de Proveedor
public class ControllerDialogoProveedor implements Initializable {

    private SessionFactory sf; // Fábrica de sesiones de Hibernate
    @FXML
    private TextField txtCodigoProveedor;
    @FXML
    private TextField txtNombreProveedor;
    private Session session;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    // Plantilla rellenable de clase Proveedor
    private Proveedor pv; 
    private String codigoProveedor;
    private ControllerGI_Prov ct2;

    // Constructor que recibe la fábrica de sesiones, el código del proveedor y el controlador
    public ControllerDialogoProveedor(SessionFactory sf, String codigoProveedor, ControllerGI_Prov ct2) {
        this.sf = sf;
        this.codigoProveedor = codigoProveedor;
        this.ct2 = ct2;
    }

    // Inicializamos el controlador y configuramos los elementos de la interfaz
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = sf.openSession();
        session.beginTransaction();

        // Obtener la ventana y establecer el título adecuado
        Stage stage = (Stage) btnAceptar.getScene().getWindow();

        if (codigoProveedor != null) {
            pv = session.find(Proveedor.class, codigoProveedor);
            if (pv != null) {
                txtCodigoProveedor.setText(pv.getCodigo());
                txtNombreProveedor.setText(pv.getNombre());
                stage.setTitle("Modificar Proveedor");  // Cambiar título a "Modificar"
            }
        } else {
            stage.setTitle("Crear Proveedor");  // Cambiar título a "Crear"
        }

        // Configuramos el botón aceptar para crear o modificar un proveedor
        btnAceptar.setOnAction(event -> {
            if (areFieldsValid()) {
                session = sf.openSession();
                session.beginTransaction();
                if (codigoProveedor == null) {
                    crearProveedor();
                    showInformation("Proveedor creado con éxito.");
                } else {
                    modProveedor(pv);
                    showInformation("Proveedor modificado con éxito.");
                }
                closeWindow();
            } else {
                showWarning();
            }
        });

        btnCancelar.setOnAction(event -> closeWindow());
    }

    // Validamos que los campos no estén vacíos
    private boolean areFieldsValid() {
        return !txtCodigoProveedor.getText().isEmpty() && !txtNombreProveedor.getText().isEmpty();
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

    // Método para crear un nuevo proveedor
    public void crearProveedor() {
        Proveedor pv = new Proveedor();
        pv.setCodigo(txtCodigoProveedor.getText());
        pv.setNombre(txtNombreProveedor.getText());
        session.persist(pv);
        session.getTransaction().commit();
    }

    // Método para modificar un proveedor existente
    public void modProveedor(Proveedor pv) {
        pv.setCodigo(txtCodigoProveedor.getText());
        pv.setNombre(txtNombreProveedor.getText());
        session.merge(pv);
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
