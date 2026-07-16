package pe.edu.suiza.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Clase principal lanzadora (Main) de la aplicación IESTP Suiza.
 * Carga la interfaz de Login y la hoja de estilos institucional.
 */
public class LoginApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Font.loadFont(getClass().getResourceAsStream("fa-solid-900.ttf"), 14);
        } catch (Exception e) {
            System.out.println("Aviso: No se pudo cargar fuente programáticamente: " + e.getMessage());
        }
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("IESTP Suiza - Sistema de Registro de Proyectos de Titulación - Acceso");
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("Logo-suiza.png")));
        } catch (Exception e) {
            System.out.println("Aviso: No se pudo cargar icono en barra de título: " + e.getMessage());
        }
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
