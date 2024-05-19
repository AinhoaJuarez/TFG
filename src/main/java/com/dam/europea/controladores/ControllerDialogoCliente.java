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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = sf.openSession();
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

        btnCancelar.setOnAction(event -> closeWindow());
    }

    private boolean areFieldsValid() {
        return !txtNombre.getText().isEmpty() && !txtDNI.getText().isEmpty() &&
               !txtDireccion.getText().isEmpty() && !txtCodPost.getText().isEmpty() &&
               !txtLocalidad.getText().isEmpty();
    }

    private void showWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Campos vacíos");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, complete todos los campos.");
        alert.showAndWait();
    }

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

    private void closeWindow() {
        if (session != null && session.isOpen()) {
            session.close();
        }
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
