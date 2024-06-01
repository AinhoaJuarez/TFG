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
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller implements Initializable {

    @FXML
    private Button botonTicket;
    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonFactura;
    @FXML
    private Button botonCaja;
    
    private SessionFactory sf;

    public Controller(SessionFactory sf) {
        this.sf = sf;
    }

    // Inicializamos el controlador, cargamos imágenes y configuramos eventos de botones
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarImagenes();
        botonCerrar.setOnAction(arg0 -> {
            try {
                switchToInicioSesion(arg0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        botonTicket.setOnAction(arg0 -> {
            try {
                switchToTickets(arg0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        botonCaja.setOnAction(arg0 -> {
            try {
                switchToGestion(arg0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    // Cambiamos a la escena de inicio de sesión
    public void switchToInicioSesion(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InicioSesion.fxml"));
        ControladorInicioSesion ct = new ControladorInicioSesion(sf);
        loader.setController(ct);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Cargamos las imágenes de los botones
    private void cargarImagenes() {
        try {
            InputStream archivoTicket = getClass().getResourceAsStream("/recibo.png");
            InputStream archivoFactura = getClass().getResourceAsStream("/factura.png");
            InputStream archivoCaja = getClass().getResourceAsStream("/magia.png");
            InputStream archivoCerrar = getClass().getResourceAsStream("/cerrar-sesion.png");

            double nuevoAncho = 200; 
            double nuevoAlto = 200; 

            Image imagenTicket = new Image(archivoTicket, 200, 200, true, true);
            Image imagenFactura = new Image(archivoFactura, nuevoAncho, nuevoAlto, true, true);
            Image imagenCaja = new Image(archivoCaja, nuevoAncho, nuevoAlto, true, true);
            Image imagenCerrar = new Image(archivoCerrar, 20, 20, true, true);
            
            ImageView a = new ImageView(imagenCerrar);
            ImageView b = new ImageView(imagenCaja);
            
            botonTicket.setGraphic(new ImageView(imagenTicket));
            botonFactura.setGraphic(new ImageView(imagenFactura));
            botonCaja.setGraphic(b);
            botonCerrar.setGraphic(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cambiamos a la escena de tickets
    public void switchToTickets(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PantallaTickets.fxml"));
        ControllerTicket ct = new ControllerTicket(sf);
        loader.setController(ct);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.001;
        double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.015;
        
        stage.setX(x);
        stage.setY(y);
        stage.setScene(scene);
        stage.setMaximized(true); 
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    // Cambiamos a la escena de facturas
    public void switchToFacturas(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Facturas.fxml"));
        ControllerFactura ct = new ControllerFactura(sf);
        loader.setController(ct);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.001;
        double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.015;
        stage.setX(x);
        stage.setY(y);
        stage.setScene(scene);
        stage.setMaximized(true); 
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    // Cambiamos a la escena de gestión de inventario
    public void switchToGestion(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Prods.fxml"));
        ControllerGI_Prods ct = new ControllerGI_Prods(sf);
        loader.setController(ct);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double x = bounds.getMinX() + (bounds.getWidth() - scene.getWidth()) * 0.001;
        double y = bounds.getMinY() + (bounds.getHeight() - scene.getHeight()) * 0.015;
        stage.setX(x);
        stage.setY(y);
        stage.setScene(scene);
        stage.setMaximized(true); 
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
}
