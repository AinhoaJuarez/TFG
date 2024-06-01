package com.dam.europea.controladores;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.dam.europea.entidades.FamiliaProducto;

import jakarta.persistence.TypedQuery;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ControllerGI_Fam implements Initializable{
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
	private TextField txtDescripcion;
	@FXML
	private TextField txtCod;
	@FXML
	private ComboBox<Integer> comboIVA;
	
	//Botones sin fotos
	@FXML
	private Button btnDescripcion;
	@FXML
	private Button btnCodPostal;
	@FXML
	private Button btnLocal;
	
	private SessionFactory sf;
	
	@FXML
	private TableView<FamiliaProducto> tableViewFam;
	@FXML
	private TableColumn<FamiliaProducto, String> codFamiliaColumn;

	@FXML
	private TableColumn<FamiliaProducto, String> descripcionFamiliaColumn;
	@FXML
	private TableColumn<FamiliaProducto, String> ivaColumn;
	
	public ControllerGI_Fam(SessionFactory sf) {
		this.sf=sf;
	}
	
	//En el código, definimos un controlador JavaFX que gestiona la visualización y navegación de familia de productos en nuestra aplicación de inventario,
	//interactuando con una base de datos a través de Hibernate.
	@Override
	public void initialize(URL url, ResourceBundle arg1) {
		cargarImagenes();
		cargarTabla();
		ArrayList<Integer> ivas = new ArrayList<Integer>();
		ivas.add(null);
		ivas.add(4);
		ivas.add(21);
		comboIVA.getItems().addAll(ivas);
		tableViewFam.setRowFactory(tv -> {
	        TableRow<FamiliaProducto> row = new TableRow<>();
	        row.setOnMouseClicked(event -> {
	            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2 && (!row.isEmpty())) {
	            	FamiliaProducto rowData = row.getItem();
	                try {
						abrirDialogoCrearFam(event, rowData.getCodFamilia());
					} catch (IOException e) {
						e.printStackTrace();
					}
	            }
	        });
	        return row;
	    });
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
		btnProductos.setOnAction(arg0 -> {
			try {
				switchToProds(arg0);
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
		btnProveedores.setOnAction(arg0 -> {
			try {
				switchToProv(arg0);
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
		btnNew.setOnAction(arg0 -> {
			try {
				abrirDialogoCrearFam(arg0, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		txtCod.textProperty().addListener((observable, oldValue, newValue) -> searchFamilias());
		txtDescripcion.textProperty().addListener((observable, oldValue, newValue) -> searchFamilias());
		comboIVA.valueProperty().addListener((observable, oldValue, newValue) -> searchFamilias());
	}
	
	private void searchFamilias() {
	    String cod = txtCod.getText().trim();
	    String descripcion = txtDescripcion.getText().trim();
	    Integer iva = comboIVA.getValue();

	    StringBuilder hql = new StringBuilder("SELECT f FROM FamiliaProducto f WHERE 1=1");

	    if (!cod.isEmpty()) {
	        hql.append(" AND STR(f.codFamilia) LIKE :cod");
	    }
	    if (!descripcion.isEmpty()) {
	        hql.append(" AND f.familiaProducto LIKE :descripcion");
	    }
	    if (iva != null) {
	        hql.append(" AND f.IVA = :iva");
	    }

	    Session session = sf.openSession();
	    TypedQuery<FamiliaProducto> query = session.createQuery(hql.toString(), FamiliaProducto.class);

	    if (!cod.isEmpty()) {
	        query.setParameter("cod", "%" + cod + "%");
	    }
	    if (!descripcion.isEmpty()) {
	        query.setParameter("descripcion", "%" + descripcion + "%");
	    }
	    if (iva != null) {
	        query.setParameter("iva", iva);
	    }

	    List<FamiliaProducto> results = query.getResultList();
	    tableViewFam.getItems().clear();
	    tableViewFam.getItems().addAll(results);
	    session.close();
	}
	
	private void abrirDialogoCrearFam(Event event, String codFamilia) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/DialogoFamiliaProducto.fxml"));
        ControllerDialogoFamiliaProducto ct = new ControllerDialogoFamiliaProducto(sf, codFamilia, this);
        loader.setController(ct);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());
        stage.setScene(scene);
        stage.show();
		
	}

	public void cargarTabla() {
		tableViewFam.getItems().clear();
		codFamiliaColumn.setCellValueFactory(new PropertyValueFactory<>("codFamilia"));
		descripcionFamiliaColumn.setCellValueFactory(new PropertyValueFactory<>("familiaProducto"));
		ivaColumn.setCellValueFactory(new PropertyValueFactory<>("IVA"));

	    Session session = sf.openSession();
	    TypedQuery<FamiliaProducto> query = session.createQuery("SELECT e FROM FamiliaProducto e", FamiliaProducto.class);
	    ArrayList<FamiliaProducto> entityData = (ArrayList<FamiliaProducto>) query.getResultList();
	    if(entityData!=null) {
	    	tableViewFam.getItems().addAll(entityData);
	    }
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
	public void switchToProds(ActionEvent event) throws IOException {
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
