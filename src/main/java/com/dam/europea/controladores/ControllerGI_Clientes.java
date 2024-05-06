package com.dam.europea.controladores;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ControllerGI_Clientes implements Initializable{
	@FXML
	private Button btnProductos;
	@FXML
	private Button btnFamilia;
	@FXML
	private Button btnClientes;
	@FXML
	private Button btnProovedores;
	@FXML
	private Button btnUsers;
	@FXML
	private Button btnTickets;
	@FXML
	private Button btnFacturas;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cargarImagenes();
		
	}
	
	public void cargarImagenes() {
		InputStream archivoProd = getClass().getResourceAsStream("/inventario.png");
		Image imagenProd= new Image(archivoProd, 75, 75, true, true);
		btnProductos.setGraphic(new ImageView(imagenProd));
		InputStream archivoMenu = getClass().getResourceAsStream("/menu.png");
//		Image imagenMenu = new Image(archivoMenu, 25, 25, true, true);
//		botonMenu.setGraphic(new ImageView(imagenMenu));
//		InputStream archivoAsoc = getClass().getResourceAsStream("/asociar-cliente.png");
//		Image imagenAsoc = new Image(archivoAsoc, 100, 100, true, true);
//		botonAsociar.setGraphic(new ImageView(imagenAsoc));
//		InputStream archivoDesAsoc = getClass().getResourceAsStream("/quitar-cliente.png");
//		Image imagenDesAsc = new Image(archivoDesAsoc, 100, 100, true, true);
//		botonDesasociar.setGraphic(new ImageView(imagenDesAsc));
//		InputStream archivoQuitLinea = getClass().getResourceAsStream("/ticketAsoc.png");
//		Image imagenQuitLinea = new Image(archivoQuitLinea, 100, 100, true, true);
//		botonTicket.setGraphic(new ImageView(imagenQuitLinea));
//		InputStream archivoQuitTicket = getClass().getResourceAsStream("/eliminar-ticket.png");
//		Image imagenQuitTicket = new Image(archivoQuitTicket, 100, 100, true, true);
//		botonDelTicket.setGraphic(new ImageView(imagenQuitTicket));
//		InputStream archivoBuscar = getClass().getResourceAsStream("/lupa.png");
//		Image imagenBuscar = new Image(archivoBuscar, 15, 15, true, true);
//		botonBuscar.setGraphic(new ImageView(imagenBuscar));
//		InputStream archivoFecha = getClass().getResourceAsStream("/calendario.png");
//		Image imagenFecha = new Image(archivoFecha, 25, 25, true, true);
//		botonFecha.setGraphic(new ImageView(imagenFecha));
	}
}
