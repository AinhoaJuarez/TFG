package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import com.dam.europea.entidades.Ticket;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

// Controlador para seleccionar tickets
public class ControladorSelTicket implements Initializable {

    @FXML
    private TableView<Ticket> tablaTickets;
    private Ticket ticketSeleccionado;

    // Inicializamos el controlador y configuramos la tabla
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Manejamos el clic en la tabla para seleccionar un ticket
        tablaTickets.setOnMouseClicked(event -> {
            ticketSeleccionado = tablaTickets.getSelectionModel().getSelectedItem();
            System.out.println("Ticket seleccionado: " + ticketSeleccionado);
        });
    }

    // Método para obtener el ticket seleccionado
    public Ticket getTicketSeleccionado() {
        return ticketSeleccionado;
    }
}
