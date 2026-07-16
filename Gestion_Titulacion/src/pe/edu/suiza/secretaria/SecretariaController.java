package pe.edu.suiza.secretaria;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import pe.edu.suiza.dao.CatalogoDAO;
import pe.edu.suiza.dao.EstudianteDAO;
import pe.edu.suiza.dao.ObservacionDAO;
import pe.edu.suiza.dao.ProyectoDAO;
import pe.edu.suiza.modelo.Catalogo;
import pe.edu.suiza.modelo.Estudiante;
import pe.edu.suiza.modelo.Observacion;
import pe.edu.suiza.modelo.Proyecto;
import pe.edu.suiza.utilidades.SesionActual;

/**
 * Controlador de Secretaría Académica.
 * Sin paréntesis en textos, con íconos oficiales y límite de 5 integrantes.
 */
public class SecretariaController implements Initializable {

    @FXML private Button btnSidebarToggle;
    @FXML private Label lblUsuarioLogueado;
    @FXML private VBox sidebar;
    @FXML private Button btnNavRegistro;
    @FXML private Button btnNavListado;
    @FXML private Button btnNavPerfil;
    
    @FXML private VBox panelRegistro;
    @FXML private TextField txtTitulo;
    @FXML private ComboBox<String> cbPrograma;
    @FXML private ComboBox<String> cbModalidad;
    @FXML private ComboBox<String> cbAsesor;
    @FXML private DatePicker dpFechaRegistro;
    @FXML private Label lblContadorEstudiantes;
    @FXML private TextField txtDniEst;
    @FXML private TextField txtNombresEst;
    @FXML private TextField txtApellidosEst;
    
    @FXML private TableView<Estudiante> tbEstudiantes;
    @FXML private TableColumn<Estudiante, String> colDniEst;
    @FXML private TableColumn<Estudiante, String> colNombresEst;
    @FXML private TableColumn<Estudiante, String> colApellidosEst;
    @FXML private TableColumn<Estudiante, Void> colAccionEst;
    
    @FXML private VBox panelListado;
    @FXML private TextField txtBuscarProyecto;
    @FXML private TableView<Proyecto> tbProyectos;
    @FXML private TableColumn<Proyecto, String> colCodProy;
    @FXML private TableColumn<Proyecto, String> colTitProy;
    @FXML private TableColumn<Proyecto, String> colProgProy;
    @FXML private TableColumn<Proyecto, String> colModProy;
    @FXML private TableColumn<Proyecto, String> colAseProy;
    @FXML private TableColumn<Proyecto, String> colEstProy;

    private boolean sidebarColapsado = false;
    private ObservableList<Estudiante> listaEstudiantesTemporal = FXCollections.observableArrayList();
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private EstudianteDAO estudianteDAO = new EstudianteDAO();
    private CatalogoDAO catalogoDAO = new CatalogoDAO();
    private ObservacionDAO observacionDAO = new ObservacionDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/pe/edu/suiza/secretaria/fonts/fa-solid-900.ttf"), 14);
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("fa-solid-900.ttf"), 14);
        } catch (Exception ignored) {}
        if (SesionActual.haySesionActiva()) {
            lblUsuarioLogueado.setText(SesionActual.getUsuarioLogueado().getNombreCompleto());
        } else {
            lblUsuarioLogueado.setText("Secretaría Académica");
        }

        dpFechaRegistro.setValue(LocalDate.now());
        configurarTablas();
        configurarCombos();
        mostrarPanelRegistro(null);

        if (txtBuscarProyecto != null) {
            txtBuscarProyecto.textProperty().addListener((obs, oldV, newV) -> {
                if (newV == null || newV.trim().isEmpty()) {
                    cargarCatalogoProyectos();
                } else {
                    List<Proyecto> filtrados = proyectoDAO.buscarPorTexto(newV);
                    tbProyectos.setItems(FXCollections.observableArrayList(filtrados));
                }
            });
        }
    }

    private void configuringTableCells() {
        colDniEst.setCellValueFactory(new PropertyValueFactory<>("dniCodigo"));
        colNombresEst.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidosEst.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        
        colAccionEst.setCellFactory(param -> new TableCell<Estudiante, Void>() {
            private final Button btnEliminar = new Button("Eliminar");
            {
                btnEliminar.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-font-family: 'Segoe UI', 'Tahoma', sans-serif; -fx-font-weight: bold;");
                btnEliminar.setOnAction(event -> {
                    Estudiante est = getTableView().getItems().get(getIndex());
                    listaEstudiantesTemporal.remove(est);
                    actualizarContadorEstudiantes();
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnEliminar);
                }
            }
        });

        tbEstudiantes.setItems(listaEstudiantesTemporal);
    }

    private void configurarTablas() {
        configuringTableCells();

        colCodProy.setCellValueFactory(new PropertyValueFactory<>("codigoProyecto"));
        colTitProy.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colProgProy.setCellValueFactory(new PropertyValueFactory<>("programaEstudio"));
        colModProy.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        colAseProy.setCellValueFactory(new PropertyValueFactory<>("asesor"));
        colEstProy.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarCombos() {
        List<Catalogo> carreras = catalogoDAO.listarPorTipo("PROGRAMA_ESTUDIO");
        if (!carreras.isEmpty()) {
            for (Catalogo c : carreras) cbPrograma.getItems().add(c.getNombreItem());
        } else {
            cbPrograma.getItems().addAll("Desarrollo de Sistemas de Información", "Enfermería Técnica", "Contabilidad");
        }

        List<Catalogo> modalidades = catalogoDAO.listarPorTipo("MODALIDAD_TITULACION");
        if (!modalidades.isEmpty()) {
            for (Catalogo c : modalidades) cbModalidad.getItems().add(c.getNombreItem());
        } else {
            cbModalidad.getItems().addAll("Tesis o Proyecto de Aplicación Profesional", "Examen de Suficiencia Profesional");
        }

        List<Catalogo> asesores = catalogoDAO.listarPorTipo("ASESOR");
        if (!asesores.isEmpty()) {
            for (Catalogo c : asesores) cbAsesor.getItems().add(c.getNombreItem());
        } else {
            cbAsesor.getItems().addAll("Ing. Ruber Torres Arevalo", "Lic. Carlos Mendoza", "Dra. María Gonzales");
        }
    }

    @FXML
    private void toggleSidebar(ActionEvent event) {
        sidebarColapsado = !sidebarColapsado;
        sidebar.setVisible(!sidebarColapsado);
        sidebar.setManaged(!sidebarColapsado);
    }

    @FXML
    private void mostrarPanelRegistro(ActionEvent event) {
        panelRegistro.setVisible(true);
        panelRegistro.setManaged(true);
        panelListado.setVisible(false);
        panelListado.setManaged(false);
        actualizarEstilosNav(btnNavRegistro);
    }

    @FXML
    private void mostrarPanelListado(ActionEvent event) {
        panelRegistro.setVisible(false);
        panelRegistro.setManaged(false);
        panelListado.setVisible(true);
        panelListado.setManaged(true);
        actualizarEstilosNav(btnNavListado);
        cargarCatalogoProyectos();
    }

    @FXML
    private void mostrarPanelPerfil(ActionEvent event) {
        mostrarAlerta("Mi Perfil", "Para cambio de credenciales de secretaría, contacte al módulo de Jefatura y soporte TI.");
    }

    @FXML
    private void agregarEstudiante(ActionEvent event) {
        if (listaEstudiantesTemporal.size() >= 5) {
            mostrarAlerta("Límite Excedido - Regla Institucional", 
                "El reglamento oficial del IESTP Suiza prohíbe inscribir más de 5 estudiantes por proyecto de titulación.");
            return;
        }

        String dni = txtDniEst.getText().trim();
        String nom = txtNombresEst.getText().trim();
        String ape = txtApellidosEst.getText().trim();

        if (dni.isEmpty() || nom.isEmpty() || ape.isEmpty()) {
            mostrarAlerta("Datos incompletos", "Por favor ingrese DNI, Nombres y Apellidos del integrante.");
            return;
        }

        for (Estudiante e : listaEstudiantesTemporal) {
            if (e.getDniCodigo().equalsIgnoreCase(dni)) {
                mostrarAlerta("Estudiante Duplicado", "El DNI o Código '" + dni + "' ya está registrado en este proyecto.");
                return;
            }
        }

        listaEstudiantesTemporal.add(new Estudiante(0, 0, dni, nom, ape));
        actualizarContadorEstudiantes();

        txtDniEst.clear();
        txtNombresEst.clear();
        txtApellidosEst.clear();
        txtDniEst.requestFocus();
    }

    private void actualizarContadorEstudiantes() {
        lblContadorEstudiantes.setText(listaEstudiantesTemporal.size() + " de 5 Registrados");
        if (listaEstudiantesTemporal.size() == 5) {
            lblContadorEstudiantes.setStyle("-fx-font-weight: bold; -fx-text-fill: #dc2626;");
        } else {
            lblContadorEstudiantes.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e3a8a;");
        }
    }

    @FXML
    private void limpiarFormulario(ActionEvent event) {
        txtTitulo.clear();
        cbPrograma.getSelectionModel().clearSelection();
        cbModalidad.getSelectionModel().clearSelection();
        cbAsesor.getSelectionModel().clearSelection();
        dpFechaRegistro.setValue(LocalDate.now());
        listaEstudiantesTemporal.clear();
        actualizarContadorEstudiantes();
    }

    @FXML
    private void guardarProyecto(ActionEvent event) {
        String tit = txtTitulo.getText().trim();
        String prog = cbPrograma.getValue();
        String mod = cbModalidad.getValue();
        String ase = cbAsesor.getValue();
        LocalDate fecha = dpFechaRegistro.getValue();

        if (tit.isEmpty() || prog == null || mod == null || ase == null || fecha == null) {
            mostrarAlerta("Formulario incompleto", "Debe completar Título, Carrera, Modalidad y Asesor para el registro.");
            return;
        }

        if (listaEstudiantesTemporal.isEmpty()) {
            mostrarAlerta("Sin Integrantes", "Debe registrar al menos 1 estudiante para crear un proyecto de titulación.");
            return;
        }

        int numRandom = (int) (Math.random() * 900) + 100;
        String codigoOficial = "PROY-SUIZA-" + LocalDate.now().getYear() + "-" + numRandom;

        Proyecto proy = new Proyecto(
            0,
            codigoOficial,
            tit,
            prog,
            mod,
            ase,
            "EN_REVISION",
            Date.valueOf(fecha),
            null
        );

        int idProyectoGenerado = proyectoDAO.insertarProyecto(proy);

        if (idProyectoGenerado > 0) {
            int countEst = 0;
            for (Estudiante est : listaEstudiantesTemporal) {
                est.setIdProyecto(idProyectoGenerado);
                if (estudianteDAO.insertarEstudiante(est)) {
                    countEst++;
                }
            }

            mostrarAlerta("Registro Exitoso", 
                "El proyecto de titulación se ha guardado oficialmente en XAMPP MySQL.\n\n" +
                "Código: " + codigoOficial + "\n" +
                "Integrantes guardados: " + countEst + " estudiantes.\n" +
                "Estado Inicial: EN_REVISION - Listo para el Coordinador.");

            limpiarFormulario(null);
            cargarCatalogoProyectos();
        } else {
            mostrarAlerta("Error BD", "Hubo un error al guardar en la base de datos MySQL. Verifique conexión en XAMPP.");
        }
    }

    private void cargarCatalogoProyectos() {
        List<Proyecto> lista = proyectoDAO.listarTodos();
        tbProyectos.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void descargarObservaciones(ActionEvent event) {
        Proyecto sel = tbProyectos.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto para ver sus observaciones.");
            return;
        }
        List<Observacion> observaciones = observacionDAO.listarPorCodigoProyecto(sel.getCodigoProyecto());
        if (observaciones.isEmpty()) {
            mostrarAlerta("Observaciones del Proyecto", "No existen observaciones registradas para el proyecto: " + sel.getCodigoProyecto() + " (Estado: " + sel.getEstado() + ").");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Historial de correcciones para: ").append(sel.getCodigoProyecto()).append("\n\n");
            for (Observacion obs : observaciones) {
                sb.append("• [").append(obs.getRolAutor()).append(" - ").append(obs.getEstadoObservacion()).append("]\n");
                sb.append("  Fecha: ").append(obs.getFechaObservacion()).append("\n");
                sb.append("  Detalle: ").append(obs.getDescripcion()).append("\n\n");
            }
            mostrarAlerta("Observaciones Metodológicas", sb.toString());
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        SesionActual.limpiarSesion();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/pe/edu/suiza/login/login.fxml"));
            Stage currentStage = (Stage) btnSidebarToggle.getScene().getWindow();
            currentStage.close();

            Stage loginStage = new Stage();
            loginStage.setTitle("IESTP Suiza - Sistema de Registro de Proyectos de Titulación - Acceso");
            try {
                loginStage.getIcons().add(new Image(getClass().getResourceAsStream("/pe/edu/suiza/login/Logo-suiza.png")));
            } catch (Exception ex) {
                System.out.println("Aviso icono: " + ex.getMessage());
            }
            loginStage.setScene(new Scene(root));
            loginStage.setResizable(false);
            loginStage.show();
            loginStage.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Error al cerrar sesión: " + e.getMessage());
            Platform.exit();
        }
    }

    private void actualizarEstilosNav(Button btnActivo) {
        btnNavRegistro.getStyleClass().remove("sidebar-btn-active");
        btnNavListado.getStyleClass().remove("sidebar-btn-active");
        if (btnActivo != null) {
            if (!btnActivo.getStyleClass().contains("sidebar-btn-active")) {
                btnActivo.getStyleClass().add("sidebar-btn-active");
            }
        }
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
