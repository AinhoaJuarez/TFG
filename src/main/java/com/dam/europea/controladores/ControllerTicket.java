package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.SessionFactory;

import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.Producto;

import jakarta.persistence.EntityManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ControllerTicket implements Initializable{
	@FXML
	private Button botonSalir;
	@FXML
	private Button botonMenu;
	@FXML
	private Button botonAsociar;
	@FXML
	private Button botonDesasociar;
	@FXML
	private Button botonDelLinea;
	@FXML
	private Button botonDelTicket;
	@FXML
	private Button botonBuscar;
	@FXML
	private TableColumn<Producto, Integer> codBarras;
	@FXML
	private TableColumn<Producto, String> descripcion;
	@FXML
	private TableColumn<Producto, Double> precioVenta;
	@FXML
	private TableColumn<Producto, Integer> cantidad;
	@FXML
	private TableColumn<Producto, Double> descuento;
	@FXML
	private TableColumn<Producto, Double> precioFinal;
	private ObservableList <Producto> productos;
	private EntityManager emf;
	private SessionFactory sf;
	
	public ControllerTicket(SessionFactory sf) {
		this.sf=sf;
	}

	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		
		cargarImagenes();
		botonSalir.setOnAction(arg0 -> {
			try {
				switchToInicioSesion(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		botonMenu.setOnAction(arg0 -> {
			try {
				switchToMenu(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		botonAsociar.setOnAction(arg0 -> {
            try {
                abrirDialogoSeleccionCliente(arg0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
	}
	
	public void cargarImagenes() {
		InputStream archivoCerrar = getClass().getResourceAsStream("/cerrar-sesion.png");
		Image imagenCerrar = new Image(archivoCerrar, 25, 25, true, true);
		botonSalir.setGraphic(new ImageView(imagenCerrar));
		InputStream archivoMenu = getClass().getResourceAsStream("/menu.png");
		Image imagenMenu = new Image(archivoMenu, 25, 25, true, true);
		botonMenu.setGraphic(new ImageView(imagenMenu));
		InputStream archivoAsoc = getClass().getResourceAsStream("/asociar-cliente.png");
		Image imagenAsoc = new Image(archivoAsoc, 100, 100, true, true);
		botonAsociar.setGraphic(new ImageView(imagenAsoc));
		InputStream archivoDesAsoc = getClass().getResourceAsStream("/quitar-cliente.png");
		Image imagenDesAsc = new Image(archivoDesAsoc, 100, 100, true, true);
		botonDesasociar.setGraphic(new ImageView(imagenDesAsc));
		InputStream archivoQuitLinea = getClass().getResourceAsStream("/eliminar-linea.png");
		Image imagenQuitLinea = new Image(archivoQuitLinea, 100, 100, true, true);
		botonDelLinea.setGraphic(new ImageView(imagenQuitLinea));
		InputStream archivoQuitTicket = getClass().getResourceAsStream("/eliminar-ticket.png");
		Image imagenQuitTicket = new Image(archivoQuitTicket, 100, 100, true, true);
		botonDelTicket.setGraphic(new ImageView(imagenQuitTicket));
		InputStream archivoBuscar = getClass().getResourceAsStream("/lupa.png");
		Image imagenBuscar = new Image(archivoBuscar, 15, 15, true, true);
		botonBuscar.setGraphic(new ImageView(imagenBuscar));
	}
	public void switchToMenu(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/erae.fxml"));
	    Controller ct = new Controller(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = primaryScreenBounds.getMinX() + (primaryScreenBounds.getWidth() - scene.getWidth())* 0.17;
        double centerY = primaryScreenBounds.getMinY() + (primaryScreenBounds.getHeight() - scene.getHeight()) *0.12;
        
        stage.setX(centerX);
        stage.setY(centerY);
	    stage.setScene(scene);
	    stage.show();
	}
	public void switchToInicioSesion(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/InicioSesion.fxml"));
	    ControladorInicioSesion ct = new ControladorInicioSesion(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = primaryScreenBounds.getMinX() + (primaryScreenBounds.getWidth() - scene.getWidth())* 0.17;
        double centerY = primaryScreenBounds.getMinY() + (primaryScreenBounds.getHeight() - scene.getHeight()) *0.12;
        stage.setX(centerX);
        stage.setY(centerY);
	    stage.setScene(scene);
	    stage.show();
	}
	
	private void abrirDialogoSeleccionCliente(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoSelCliente.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
    }
	
	

}
