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
    
    // Atributo para usar como relleno
    private Cliente c;
    private ControllerGI_Clientes ct2;

    public ControllerDialogoCliente(SessionFactory sf, String dni, ControllerGI_Clientes ct2) {
        this.sf = sf;
        this.ct2 = ct2;
        this.dni = dni;
    }

    // Inicializamos el controlador, abrimos sesión y configuramos los botones
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = sf.openSession();
        
        // Obtener la ventana y establecer el título adecuado
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        
        if (dni != null) {
            c = session.find(Cliente.class, dni);
            if (c != null) {
                txtCodPost.setText(c.getCodPos());
                txtDireccion.setText(c.getDireccion());
                txtDNI.setText(c.getDni());
                txtDNI.setEditable(false);
                txtLocalidad.setText(c.getLocalidad());
                txtNombre.setText(c.getNombre());
                stage.setTitle("Modificar Cliente");
            }
        } else {
            stage.setTitle("Crear Cliente");
        }
        session.close();
        // Configuramos el comportamiento del botón "Aceptar"
        btnAceptar.setOnAction(event -> {
            if (areFieldsValid()) {
                if (txtDNI.getText().isEmpty()) {
                    showWarning("El campo DNI no puede estar vacío.");
                } else {
                    if (dni == null) {
                        crearCliente();
                        showInformation("Cliente creado con éxito.");
                    } else {
                        modCliente(c);
                        showInformation("Cliente modificado con éxito.");
                    }
                    closeWindow();
                }
            } else {
                showWarning("Por favor, complete todos los campos.");
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

    // Mostramos una alerta si hay campos vacíos o si hay otro tipo de advertencia
    private void showWarning(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(message);
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

    // Creamos un nuevo cliente con los datos ingresados
    public void crearCliente() {
    	session=sf.openSession();
    	session.beginTransaction();
        Cliente c = new Cliente();
        c.setCodPos(txtCodPost.getText());
        c.setDireccion(txtDireccion.getText());
        c.setDni(txtDNI.getText());
        c.setLocalidad(txtLocalidad.getText());
        c.setNombre(txtNombre.getText());
        session.persist(c);
        session.getTransaction().commit();
        session.close();
    }

    // Modificamos un cliente existente con los datos ingresados
    public void modCliente(Cliente c) {
        session = sf.openSession();
        session.beginTransaction();
        
        Cliente existingCliente = session.find(Cliente.class, c.getDni());
        
        existingCliente.setCodPos(txtCodPost.getText());
        existingCliente.setDireccion(txtDireccion.getText());
        existingCliente.setLocalidad(txtLocalidad.getText());
        existingCliente.setNombre(txtNombre.getText());
        
        session.merge(existingCliente);
        session.getTransaction().commit();
        session.close();
    }

    // Cerramos la ventana y la sesión
    private void closeWindow() {
        ct2.cargarTabla();
        if (session != null && session.isOpen()) {
            session.close();
        }
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
