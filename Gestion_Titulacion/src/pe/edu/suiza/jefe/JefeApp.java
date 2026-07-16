package pe.edu.suiza.jefe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * App lanzador de Jefatura de Investigación en pe.edu.suiza.jefe.
 * Redirige de forma segura al Login principal para garantizar la autenticación de sesión y no romper el flujo.
 */
public class JefeApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            Font.loadFont(getClass().getResourceAsStream("/pe/edu/suiza/login/fa-solid-900.ttf"), 14);
            Font.loadFont(getClass().getResourceAsStream("fa-solid-900.ttf"), 14);
        } catch (Exception ignored) {}
        Parent root = FXMLLoader.load(getClass().getResource("/pe/edu/suiza/login/login.fxml"));
        primaryStage.setTitle("IESTP Suiza - Sistema de Registro de Proyectos de Titulación - Acceso");
        try {
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/pe/edu/suiza/login/Logo-suiza.png")));
        } catch (Exception ignored) {}
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
