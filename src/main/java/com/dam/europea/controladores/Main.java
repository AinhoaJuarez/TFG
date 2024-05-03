package com.dam.europea.controladores;
	
import java.io.IOException;

import jakarta.persistence.EntityManagerFactory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/InicioSesion.fxml"));
        Scene scene = new Scene(root);
        Image icon = new Image("iconoApp.jpeg");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PapeLeo");
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
