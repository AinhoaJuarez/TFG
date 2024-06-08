package com.dam.europea.controladores;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

// Controlador para el diálogo de gestión de FamiliaProducto
public class ControllerDialogoFamiliaProducto implements Initializable {

    private SessionFactory sf; // Fábrica de sesiones de Hibernate
    private String codigoFamilia; // Código de la familia de productos
    @FXML
    private TextField txtCodigoFamilia;
    @FXML
    private TextField txtNombreFamiliaProductos;
    @FXML
    private ComboBox<Integer> comboBoxIVA;
    private Session session;
    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;

    private FamiliaProducto fp;
    private ControllerGI_Fam ct2;

    // Constructor que recibe la fábrica de sesiones, el código de familia y el controlador
    public ControllerDialogoFamiliaProducto(SessionFactory sf, String codigoFamilia, ControllerGI_Fam ct2) {
        this.sf = sf;
        this.codigoFamilia = codigoFamilia;
        this.ct2 = ct2;
    }

    // Inicializamos el controlador y configuramos los elementos de la interfaz
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        session = sf.openSession();
        ArrayList<Integer> ivas = new ArrayList<>();
        ivas.add(4);
        ivas.add(21);
        comboBoxIVA.getItems().addAll(ivas);

        session.beginTransaction();
        if (codigoFamilia != null) {
            fp = session.find(FamiliaProducto.class, codigoFamilia);
            if (fp != null) {
                txtCodigoFamilia.setText(fp.getCodFamilia());
                txtNombreFamiliaProductos.setText(fp.getFamiliaProducto());
                comboBoxIVA.setValue(fp.getIVA());
            }
        }

        // Configuramos el botón aceptar para crear o modificar una familia de productos
        btnAceptar.setOnAction(event -> {
            if (areFieldsValid()) {
                if (codigoFamilia == null) {
                    crearFamiliaProducto();
                } else {
                    modFamiliaProducto(fp);
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
        return !txtCodigoFamilia.getText().isEmpty() && !txtNombreFamiliaProductos.getText().isEmpty();
    }

    // Mostramos una alerta si los campos están vacíos
    private void showWarning() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Campos vacíos");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, complete todos los campos.");
        alert.showAndWait();
    }

    // Método para crear una nueva familia de productos
    public void crearFamiliaProducto() {
        FamiliaProducto fp = new FamiliaProducto();
        fp.setCodFamilia(txtCodigoFamilia.getText());
        fp.setFamiliaProducto(txtNombreFamiliaProductos.getText());
        fp.setIVA(comboBoxIVA.getValue());
        session.persist(fp);
        session.getTransaction().commit();
    }

    // Método para modificar una familia de productos existente
    public void modFamiliaProducto(FamiliaProducto fp) {
        fp.setCodFamilia(txtCodigoFamilia.getText());
        fp.setFamiliaProducto(txtNombreFamiliaProductos.getText());
        fp.setIVA(comboBoxIVA.getValue());
        session.merge(fp);
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
