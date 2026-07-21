package pe.edu.suiza.utilidades;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class TestFXMLLoader extends Application {
    @Override
    public void start(Stage primaryStage) {
        String[] fxmls = {
            "/pe/edu/suiza/login/login.fxml",
            "/pe/edu/suiza/recuperar/recuperar.fxml",
            "/pe/edu/suiza/secretaria/secretaria.fxml",
            "/pe/edu/suiza/coordinador/coordinador.fxml",
            "/pe/edu/suiza/jefe/jefe.fxml"
        };
        boolean todoOk = true;
        for (String fxml : fxmls) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
                loader.load();
                System.out.println("[VERIFICADO OK] Carga FXML exitosa: " + fxml);
            } catch (Exception e) {
                System.err.println("[ERROR FATAL EN FXML] Fallo al cargar: " + fxml);
                e.printStackTrace();
                todoOk = false;
            }
        }
        if (todoOk) {
            System.out.println("=================================================");
            System.out.println("¡TODOS LOS FXML FUERON AUDITADOS Y CARGAN AL 100%!");
            System.out.println("=================================================");
            System.exit(0);
        } else {
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
