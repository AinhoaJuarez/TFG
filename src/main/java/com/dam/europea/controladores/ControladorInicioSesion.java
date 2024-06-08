package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import org.hibernate.SessionFactory;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

// Este es el controlador para la pantalla de inicio de sesión
public class ControladorInicioSesion implements Initializable {
    @FXML
    private ImageView imgView; // Vista para mostrar una imagen
    private SessionFactory sf; // Fábrica de sesiones de Hibernate

    // Inicializamos el controlador cargando una imagen
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InputStream inputStream = getClass().getResourceAsStream("/iconoGrande.jpeg");
        Image imagen = new Image(inputStream);
        imgView.setImage(imagen);
    }

    // Cambiamos a la escena del menú principal
    public void switchToMenu(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/erae.fxml"));
        Controller controller = new Controller(sf);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Constructor que recibe una fábrica de sesiones de Hibernate
    public ControladorInicioSesion(SessionFactory sessionFactory) {
        this.sf = sessionFactory;
    }
}
