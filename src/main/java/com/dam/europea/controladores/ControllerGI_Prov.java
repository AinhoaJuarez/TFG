package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;
import com.dam.europea.entidades.Proveedor;

import jakarta.persistence.TypedQuery;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerGI_Prov implements Initializable{
	//Botones fotos
	@FXML
	private Button btnProductos;
	@FXML
	private Button btnFamilia;
	@FXML
	private Button btnClientes;
	@FXML
	private Button btnProveedores;
	@FXML
	private Button btnUsers;
	@FXML
	private Button btnTickets;
	@FXML
	private Button btnFacturas;
	@FXML
	private Button botonMenu;
	@FXML
	private Button botonSalir;
	@FXML
	private Button btnNew;
	@FXML
	private Button btnDel;
	
	//Botones sin fotos
	@FXML
	private Button btnDescripcion;
	@FXML
	private Button btnCodPostal;
	@FXML
	private Button btnLocal;
	
	private SessionFactory sf;
	
	@FXML
	private TableView<Proveedor> tableView;
	@FXML
	private TableColumn<Proveedor, String> codProveedorColumn;

	@FXML
	private TableColumn<Proveedor, String> nombreProveedorColumn;

	
	public ControllerGI_Prov(SessionFactory sf) {
		this.sf=sf;
	}
	public void cargarTabla() {
		tableView.getItems().clear();
		codProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("codigo"));
		nombreProveedorColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));

	    Session session = sf.openSession();
	    TypedQuery<Proveedor> query = session.createQuery("SELECT e FROM Proveedor e", Proveedor.class);
	    ArrayList<Proveedor> entityData = (ArrayList<Proveedor>) query.getResultList();
	    if(entityData!=null) {
	    	tableView.getItems().addAll(entityData);
	    }
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
		btnFamilia.setOnAction(arg0 -> {
			try {
				switchToFamilia(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnFacturas.setOnAction(arg0 -> {
			try {
				switchToFacturas(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnProductos.setOnAction(arg0 -> {
			try {
				switchToProductos(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnUsers.setOnAction(arg0 -> {
			try {
				switchToUsers(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnTickets.setOnAction(arg0 -> {
			try {
				switchToTickets(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnClientes.setOnAction(arg0 -> {
			try {
				switchToClientes(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public void cargarImagenes() {
		InputStream archivoProd = getClass().getResourceAsStream("/inventario.png");
		Image imagenProd= new Image(archivoProd, 75, 75, true, true);
		btnProductos.setGraphic(new ImageView(imagenProd));
		InputStream archivoFam = getClass().getResourceAsStream("/famProd.png");
		Image imagenFam= new Image(archivoFam, 75, 75, true, true);
		btnFamilia.setGraphic(new ImageView(imagenFam));
		InputStream archivoCli = getClass().getResourceAsStream("/cliente.png");
		Image imagenCli= new Image(archivoCli, 75, 75, true, true);
		btnClientes.setGraphic(new ImageView(imagenCli));
		InputStream archivoProv = getClass().getResourceAsStream("/prov.png");
		Image imagenProv= new Image(archivoProv, 75, 75, true, true);
		btnProveedores.setGraphic(new ImageView(imagenProv));
		InputStream archivoUsr= getClass().getResourceAsStream("/users.png");
		Image imagenUsr= new Image(archivoUsr, 75, 75, true, true);
		btnUsers.setGraphic(new ImageView(imagenUsr));
		InputStream archivoTick= getClass().getResourceAsStream("/recibo.png");
		Image imagenTick= new Image(archivoTick, 75, 75, true, true);
		btnTickets.setGraphic(new ImageView(imagenTick));
		InputStream archivoFac= getClass().getResourceAsStream("/factura.png");
		Image imagenFac= new Image(archivoFac, 75, 75, true, true);
		btnFacturas.setGraphic(new ImageView(imagenFac));
		InputStream archivoNew= getClass().getResourceAsStream("/plus.png");
		Image imagenNew= new Image(archivoNew, 35, 35, true, true);
		btnNew.setGraphic(new ImageView(imagenNew));
		InputStream archivoMin= getClass().getResourceAsStream("/minus.png");
		Image imagenMin= new Image(archivoMin, 35, 35, true, true);
		btnDel.setGraphic(new ImageView(imagenMin));
		InputStream archivoMenu = getClass().getResourceAsStream("/menu.png");
		Image imagenMenu = new Image(archivoMenu, 25, 25, true, true);
		botonMenu.setGraphic(new ImageView(imagenMenu));
		InputStream archivoCerrar = getClass().getResourceAsStream("/cerrar-sesion.png");
		Image imagenCerrar = new Image(archivoCerrar, 25, 25, true, true);
		botonSalir.setGraphic(new ImageView(imagenCerrar));
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
	public void switchToClientes(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Clientes.fxml"));
	    ControllerGI_Clientes ct = new ControllerGI_Clientes(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.setMaximized(true); 
	    stage.initStyle(StageStyle.UNDECORATED);
	   
	    stage.show();
	}
	public void switchToTickets(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Tickets.fxml"));
	    ControllerGI_Tickets ct = new ControllerGI_Tickets(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.setMaximized(true); 
	    stage.initStyle(StageStyle.UNDECORATED);
	    
	    stage.show();
	}
	public void switchToFacturas(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Facturas.fxml"));
	    ControllerGI_Facturas ct = new ControllerGI_Facturas(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.setMaximized(true); 
	    stage.initStyle(StageStyle.UNDECORATED);
	   
	    stage.show();
	}
	public void switchToFamilia(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Fam.fxml"));
	    ControllerGI_Fam ct = new ControllerGI_Fam(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.setMaximized(true); 
	    stage.initStyle(StageStyle.UNDECORATED);
	    
	    stage.show();
	}
	public void switchToProductos(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Prods.fxml"));
	    ControllerGI_Prods ct = new ControllerGI_Prods(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.setMaximized(true); 
	    stage.initStyle(StageStyle.UNDECORATED);
	    
	    stage.show();
	}
	public void switchToUsers(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Users.fxml"));
	    ControllerGI_Users ct = new ControllerGI_Users(sf);
	    loader.setController(ct);
	    Parent root = loader.load();
	    Scene scene = new Scene(root);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.setMaximized(true); 
	    stage.initStyle(StageStyle.UNDECORATED);
	    
	    stage.show();
	}
}
