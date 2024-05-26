package com.dam.europea;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.dam.europea.controladores.ControladorInicioSesion;
import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.Factura;
import com.dam.europea.entidades.FamiliaProducto;
import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Proveedor;
import com.dam.europea.entidades.Ticket;
import com.dam.europea.entidades.TicketProductos;
import com.dam.europea.entidades.Usuario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
    public static SessionFactory sf;

    public static void main(String[] args) {
        sf = new Configuration().configure().buildSessionFactory();
        Session session = sf.openSession();
        launch(args);
        if (sf != null) {
            sf.close();
        }
        
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        try (Session session = sf.openSession()) {
            session.beginTransaction();
            Producto p = new Producto();
            p.setCodigoBarras("");
            session.persist(p);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InicioSesion.fxml"));
        ControladorInicioSesion controller = new ControladorInicioSesion(sf);
        loader.setController(controller);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Image icon = new Image("iconoApp.jpeg");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(scene);
        primaryStage.setTitle("PapeLeo");
        primaryStage.show();
        
       
    }
}
