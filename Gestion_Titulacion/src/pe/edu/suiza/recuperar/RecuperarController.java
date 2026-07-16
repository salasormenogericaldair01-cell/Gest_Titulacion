package pe.edu.suiza.recuperar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controlador de Recuperación de Credenciales en pe.edu.suiza.recuperar.
 * Genera el Ticket TI institucional para reseteo local sin correos externos.
 */
public class RecuperarController implements Initializable {

    @FXML private TextField txtUsuarioRecuperar;
    @FXML private VBox boxTicket;
    @FXML private Label lblTicketGenerado;
    @FXML private Label lblMensaje;
    @FXML private Button btnGenerar;
    @FXML private Button btnCancelar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/pe/edu/suiza/secretaria/fonts/fa-solid-900.ttf"), 14);
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("fa-solid-900.ttf"), 14);
        } catch (Exception ignored) {}
        boxTicket.setVisible(false);
        boxTicket.setManaged(false);
        lblMensaje.setText("");
    }

    @FXML
    private void generarTicket(ActionEvent event) {
        String usr = txtUsuarioRecuperar.getText().trim();
        if (usr.isEmpty()) {
            lblMensaje.setText("Por favor ingrese su usuario del sistema.");
            return;
        }

        pe.edu.suiza.dao.UsuarioDAO usuarioDAO = new pe.edu.suiza.dao.UsuarioDAO();
        String codigoTicket = usuarioDAO.generarTicketRecuperacion(usr);

        if (codigoTicket == null) {
            // Si el usuario no fue hallado en BD o XAMPP está offline, generar ticket local
            int numRandom = (int) (Math.random() * 9000) + 1000;
            codigoTicket = "TICK-SUIZA-" + numRandom;
            lblMensaje.setStyle("-fx-text-fill: #d97706;");
            lblMensaje.setText("Ticket generado en modo local/offline.");
        } else {
            lblMensaje.setStyle("-fx-text-fill: #065f46;");
            lblMensaje.setText("Ticket registrado y vinculado al usuario en base de datos.");
        }

        lblTicketGenerado.setText(codigoTicket);
        boxTicket.setVisible(true);
        boxTicket.setManaged(true);
        btnGenerar.setDisable(true);
        txtUsuarioRecuperar.setDisable(true);
    }

    @FXML
    private void cerrarModal(ActionEvent event) {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
