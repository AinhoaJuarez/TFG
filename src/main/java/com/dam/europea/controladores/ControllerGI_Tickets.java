package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.Ticket;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerGI_Tickets implements Initializable{
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
	@FXML
    private TextField txtCod;
	@FXML
	private Button btnDescripcion;
	@FXML
	private Button btnCodPostal;
	@FXML
	private Button btnLocal;
	@FXML
    private TableView<Ticket> tableViewTickets;
	private SessionFactory sf;
	@FXML
    private TableColumn<Ticket, Integer> codigoTicketColumn;
    @FXML
    private TableColumn<Ticket, String> fechaTicketColumn;
    @FXML
    private TableColumn<Ticket, Cliente> clienteAsociadoColumn;
    @FXML
    private TableColumn<Ticket, Double> totalTicketColumn;
	
	public ControllerGI_Tickets(SessionFactory sf) {
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
		btnClientes.setOnAction(arg0 -> {
			try {
				switchToClientes(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		btnProveedores.setOnAction(arg0 -> {
			try {
				switchToProv(arg0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		codigoTicketColumn.setCellValueFactory(cellData -> cellData.getValue().numTicketProperty().asObject());
        fechaTicketColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        clienteAsociadoColumn.setCellValueFactory(cellData -> cellData.getValue().clienteProperty());
        totalTicketColumn.setCellValueFactory(cellData -> cellData.getValue().importeTotalProperty().asObject());

        // Load data into TableView
        loadTickets();
	}
	private void loadTickets() {
		Session session = sf.openSession();
		Query<Ticket> query = session.createQuery("SELECT * FROM Ticket", Ticket.class);
        ObservableList<Ticket> ticketList = FXCollections.observableArrayList(query.list());
        tableViewTickets.setItems(ticketList);
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
	public void switchToProv(ActionEvent event) throws IOException {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionInventario_Prov.fxml"));
	    ControllerGI_Prov ct = new ControllerGI_Prov(sf);
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
	    ControllerGI_Facturas s = new ControllerGI_Facturas(sf);
	    loader.setController(s);
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
