package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import com.dam.europea.entidades.Ticket;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class ControladorSelTicket implements Initializable{

	    @FXML
	    private TableView<Ticket> tablaTickets;
	    private Ticket ticketSeleccionado;

	    @Override
	    public void initialize(URL url, ResourceBundle resourceBundle) {
	    	tablaTickets.setOnMouseClicked(event -> {
	            ticketSeleccionado = tablaTickets.getSelectionModel().getSelectedItem();
	            
	            System.out.println("Cliente seleccionado: " + ticketSeleccionado);
	        });
	    }

	    public Ticket getTicketSeleccionado() {
	        return ticketSeleccionado;
	    }

	}

