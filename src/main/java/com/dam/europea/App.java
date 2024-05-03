package com.dam.europea;

import java.io.IOException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.dam.europea.entidades.Cliente;
import com.dam.europea.entidades.Factura;
import com.dam.europea.entidades.FamiliaProducto;
import com.dam.europea.entidades.Producto;
import com.dam.europea.entidades.Ticket;
import com.dam.europea.entidades.Usuario;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class App extends Application{
	public static SessionFactory sf;
  public static void main(String[] args) {
	sf = null;
	Session session = null;
	try {
		sf = new Configuration().configure().buildSessionFactory();
		session = sf.openSession();
		session.beginTransaction();
		FamiliaProducto f1 = new FamiliaProducto();
	    Producto p = new Producto();
	    Cliente c = new Cliente();
	    Ticket t = new Ticket();
	    Factura f = new Factura();
	    Usuario u = new Usuario();
		session.persist(f1);
		session.persist(p);
		session.persist(c);
		session.persist(f);
		session.persist(t);
		session.persist(u);
		session.getTransaction().commit();
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		if (session != null)
			session.close();
		if (sf != null) sf.close();
	}
    
    
  }
  public void start(Stage primaryStage) throws IOException {
      Parent root = FXMLLoader.load(getClass().getResource("/InicioSesion.fxml"));
      Scene scene = new Scene(root);
      Image icon = new Image("iconoApp.jpeg");
      primaryStage.getIcons().add(icon);
      primaryStage.setScene(scene);
      primaryStage.setTitle("PapeLeo");
      primaryStage.show();
	}
}
