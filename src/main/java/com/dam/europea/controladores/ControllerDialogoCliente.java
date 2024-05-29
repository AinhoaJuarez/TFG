package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Cliente;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ControllerDialogoCliente implements Initializable {
    private SessionFactory sf;
    private String dni;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDNI;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtCodPost;
    @FXML
    private TextField txtLocalidad;
    private Session session;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;

    public ControllerDialogoCliente(SessionFactory sf, String dni) {
        this.sf = sf;
        this.dni = dni;
    }

    // Inicializamos el controlador, abrimos sesión y configuramos los botones
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = sf.openSession();
        
        // Si el DNI no es nulo, cargamos los datos del cliente en los campos
        if (dni != null) {
            session.beginTransaction();
            Cliente c = session.find(Cliente.class, dni);
            if (c != null) {
                txtCodPost.setText(c.getCodPos());
                txtDireccion.setText(c.getDireccion());
                txtDNI.setText(c.getDni());
                txtLocalidad.setText(c.getLocalidad());
                txtNombre.setText(c.getNombre());
            }
        }
        
        // Configuramos el comportamiento del botón "Aceptar"
        btnAceptar.setOnAction(event -> {
            if (areFieldsValid()) {
                if (dni == null) {
                    crearCliente();
                } else {
                    modCliente();
                }
                closeWindow();
            } else {
                showWarning();
            }
        });

        // Configuramos el comportamiento del botón "Cancelar"
        btnCancelar.setOnAction(event -> closeWindow());
    }

    // Validamos que todos los campos estén completos
    private boolean areFieldsValid() {
        return !txtNombre.getText().isEmpty() && !txtDNI.getText().isEmpty() &&
               !txtDireccion.getText().isEmpty() && !txtCodPost.getText().isEmpty() &&
               !txtLocalidad.getText().isEmpty();
    }

    // Mostramos una alerta si hay campos vacíos
    private void showWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Campos vacíos");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, complete todos los campos.");
        alert.showAndWait();
    }

    // Creamos un nuevo cliente con los datos ingresados
    public void crearCliente() {
        Cliente c = new Cliente();
        c.setCodPos(txtCodPost.getText());
        c.setDireccion(txtDireccion.getText());
        c.setDni(txtDNI.getText());
        c.setLocalidad(txtLocalidad.getText());
        c.setNombre(txtNombre.getText());
        session.beginTransaction();
        session.persist(c);
        session.getTransaction().commit();
    }

    // Modificamos un cliente existente con los nuevos datos
    public void modCliente() {
        Cliente c = new Cliente();
        c.setCodPos(txtCodPost.getText());
        c.setDireccion(txtDireccion.getText());
        c.setDni(txtDNI.getText());
        c.setLocalidad(txtLocalidad.getText());
        c.setNombre(txtNombre.getText());
        session.beginTransaction();
        session.merge(c);
        session.getTransaction().commit();
    }

    // Cerramos la ventana y la sesión
    private void closeWindow() {
        if (session != null && session.isOpen()) {
            session.close();
        }
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
