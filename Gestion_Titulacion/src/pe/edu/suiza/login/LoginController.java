package pe.edu.suiza.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import pe.edu.suiza.dao.UsuarioDAO;
import pe.edu.suiza.modelo.Usuario;
import pe.edu.suiza.utilidades.SesionActual;

/**
 * Controlador de Login en pe.edu.suiza.login.
 * Sin paréntesis en textos, con ícono oficial IESTP Suiza y validación en BD.
 */
public class LoginController implements Initializable {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private Button btnToggleVer;
    @FXML private Label lblError;
    @FXML private Button btnIngresar;
    @FXML private Button btnSalir;

    private boolean passwordVisible = false;
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/pe/edu/suiza/secretaria/fonts/fa-solid-900.ttf"), 14);
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("fa-solid-900.ttf"), 14);
        } catch (Exception ignored) {}
        lblError.setText("");
        txtPasswordVisible.textProperty().bindBidirectional(txtPassword.textProperty());
    }

    @FXML
    private void toggleVerPassword(ActionEvent event) {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            btnToggleVer.setText("\uF070");
        } else {
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPassword.setVisible(true);
            txtPassword.setManaged(true);
            btnToggleVer.setText("\uF06E");
        }
    }

    @FXML
    private void accionIngresar(ActionEvent event) {
        String usr = txtUsuario.getText().trim();
        String clave = txtPassword.getText();

        if (usr.isEmpty() || clave.isEmpty()) {
            lblError.setText("Por favor ingrese su usuario y contraseña.");
            return;
        }

        Usuario usuarioBD = usuarioDAO.validarLogin(usr, clave);
        String rutaFxml = "";
        String tituloVentana = "";

        if (usuarioBD != null) {
            SesionActual.setUsuarioLogueado(usuarioBD);
            if (usuarioBD.getRol().equalsIgnoreCase("SECRETARIA")) {
                rutaFxml = "/pe/edu/suiza/secretaria/secretaria.fxml";
                tituloVentana = "IESTP Suiza - Secretaría Académica";
            } else if (usuarioBD.getRol().equalsIgnoreCase("COORDINADOR")) {
                rutaFxml = "/pe/edu/suiza/coordinador/coordinador.fxml";
                tituloVentana = "IESTP Suiza - Coordinación Académica";
            } else if (usuarioBD.getRol().equalsIgnoreCase("JEFE_INVESTIGACION") || usuarioBD.getRol().equalsIgnoreCase("ADMINISTRADOR")) {
                rutaFxml = "/pe/edu/suiza/jefe/jefe.fxml";
                tituloVentana = "IESTP Suiza - Jefatura de Investigación y Administración";
            } else {
                lblError.setText("Rol no autorizado para acceder al sistema.");
                return;
            }
        } else {
            if (usr.equalsIgnoreCase("secretaria") && clave.equals("secretaria123")) {
                SesionActual.setUsuarioLogueado(new pe.edu.suiza.modelo.Usuario(1, "secretaria", "secretaria123", "SECRETARIA", "Noelia Bolaños (Atención y Registro)", "ACTIVO", null));
                rutaFxml = "/pe/edu/suiza/secretaria/secretaria.fxml";
                tituloVentana = "IESTP Suiza - Secretaría Académica - Modo Local";
            } else if (usr.equalsIgnoreCase("coordinador") && clave.equals("coordinador123")) {
                SesionActual.setUsuarioLogueado(new pe.edu.suiza.modelo.Usuario(2, "coordinador", "coordinador123", "COORDINADOR", "Mg. Ruber Torres Arevalo (Coordinación)", "ACTIVO", null));
                rutaFxml = "/pe/edu/suiza/coordinador/coordinador.fxml";
                tituloVentana = "IESTP Suiza - Coordinación Académica - Modo Local";
            } else if (usr.equalsIgnoreCase("jefe") && clave.equals("jefe123")) {
                SesionActual.setUsuarioLogueado(new pe.edu.suiza.modelo.Usuario(3, "jefe", "jefe123", "JEFE_INVESTIGACION", "Dr. Alejandro Barbaran (Jefatura Investigación)", "ACTIVO", null));
                rutaFxml = "/pe/edu/suiza/jefe/jefe.fxml";
                tituloVentana = "IESTP Suiza - Jefatura de Investigación - Modo Local";
            } else {
                lblError.setText("Credenciales incorrectas en BD ni locales. Verifique XAMPP.");
                return;
            }
        }

        try {
            Parent root = FXMLLoader.load(getClass().getResource(rutaFxml));
            Stage loginStage = (Stage) btnIngresar.getScene().getWindow();
            loginStage.close();

            Stage appStage = new Stage();
            appStage.setTitle(tituloVentana);
            try {
                appStage.getIcons().add(new Image(getClass().getResourceAsStream("Logo-suiza.png")));
            } catch (Exception ex) {
                System.out.println("Aviso icono: " + ex.getMessage());
            }
            appStage.setScene(new Scene(root));
            appStage.show();
            appStage.setMaximized(true);
            Platform.runLater(() -> appStage.setMaximized(true));
        } catch (IOException e) {
            lblError.setText("Error al cargar interfaz de " + usr + ".");
            System.err.println("Error cargando " + rutaFxml + ": " + e.getMessage());
        }
    }

    @FXML
    private void abrirRecuperar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/pe/edu/suiza/recuperar/recuperar.fxml"));
            Stage stage = new Stage();
            stage.setTitle("IESTP Suiza - Recuperar Contraseña y Ticket TI");
            try {
                stage.getIcons().add(new Image(getClass().getResourceAsStream("Logo-suiza.png")));
            } catch (Exception ex) {
                System.out.println("Aviso icono: " + ex.getMessage());
            }
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            lblError.setText("Módulo de recuperación en mantenimiento.");
            System.err.println("Error abriendo recuperar.fxml: " + e.getMessage());
        }
    }

    @FXML
    private void abrirCambiarClave(ActionEvent event) {
        lblError.setStyle("-fx-text-fill: #2563eb;");
        lblError.setText("Para cambiar su clave ingrese al sistema y acceda a Mi Perfil.");
    }

    @FXML
    private void accionSalir(ActionEvent event) {
        Platform.exit();
    }
}
