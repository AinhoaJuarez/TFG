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

public class ControladorInicioSesion implements Initializable {
	@FXML
	private ImageView imgView;
	private SessionFactory sf;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		InputStream inputStream = getClass().getResourceAsStream("/iconoGrande.jpeg");
        Image imagen = new Image(inputStream);
		imgView.setImage(imagen);
	}
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
	public ControladorInicioSesion(SessionFactory sessionFactory) {
        this.sf = sessionFactory;
    }
}
