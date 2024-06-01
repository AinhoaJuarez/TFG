package com.dam.europea.controladores;

import java.net.URL;
import java.util.ResourceBundle;

import com.dam.europea.entidades.Cliente;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

public class ControladorSelCliente implements Initializable{

    @FXML
    private TableView<Cliente> tablaClientes;

    private Cliente clienteSeleccionado;

    // Inicializamos el controlador y configuramos el comportamiento al hacer clic en la tabla
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tablaClientes.setOnMouseClicked(event -> {
            clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
            
            System.out.println("Cliente seleccionado: " + clienteSeleccionado);
        });
    }

    // Metodo para obtener el cliente seleccionado
    public Cliente getClienteSeleccionado() {
        return clienteSeleccionado;
    }
}
