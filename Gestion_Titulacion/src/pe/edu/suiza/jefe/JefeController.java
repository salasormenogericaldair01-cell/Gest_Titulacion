package pe.edu.suiza.jefe;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import pe.edu.suiza.dao.CatalogoDAO;
import pe.edu.suiza.dao.ProyectoDAO;
import pe.edu.suiza.dao.UsuarioDAO;
import pe.edu.suiza.modelo.Catalogo;
import pe.edu.suiza.modelo.Proyecto;
import pe.edu.suiza.modelo.Usuario;
import pe.edu.suiza.utilidades.SesionActual;

/**
 * Controlador de Jefatura de Investigación y Administración General.
 * Módulo sin paréntesis en textos, con íconos oficiales y filtro 1m-5a.
 */
public class JefeController implements Initializable {

    @FXML private Button btnSidebarToggle;
    @FXML private Label lblUsuarioLogueado;
    @FXML private VBox sidebar;
    @FXML private Button btnNavAprobacion;
    @FXML private Button btnNavUsuarios;
    @FXML private Button btnNavCatalogos;
    @FXML private Button btnNavReportes;
    @FXML private Button btnNavPerfil;
    
    @FXML private VBox panelAprobacion;
    @FXML private TextField txtBuscarProy;
    @FXML private TableView<Proyecto> tbProyectosAprobacion;
    @FXML private TableColumn<Proyecto, String> colAproCod;
    @FXML private TableColumn<Proyecto, String> colAproTit;
    @FXML private TableColumn<Proyecto, String> colAproProg;
    @FXML private TableColumn<Proyecto, String> colAproMod;
    @FXML private TableColumn<Proyecto, String> colAproEst;
    
    @FXML private VBox panelUsuarios;
    @FXML private TableView<Usuario> tbUsuarios;
    @FXML private TableColumn<Usuario, String> colUsuUser;
    @FXML private TableColumn<Usuario, String> colUsuNom;
    @FXML private TableColumn<Usuario, String> colUsuRol;
    @FXML private TableColumn<Usuario, String> colUsuPassClaro;
    @FXML private TableColumn<Usuario, String> colUsuEst;
    
    @FXML private VBox panelCatalogos;
    @FXML private ComboBox<String> cbTipoCatalogo;
    @FXML private TableView<Catalogo> tbCatalogos;
    @FXML private TableColumn<Catalogo, String> colCatTipo;
    @FXML private TableColumn<Catalogo, String> colCatCod;
    @FXML private TableColumn<Catalogo, String> colCatNom;
    @FXML private TableColumn<Catalogo, String> colCatDesc;
    
    @FXML private VBox panelReportes;
    @FXML private ComboBox<String> cbRangoTiempo;
    @FXML private ComboBox<String> cbTipoReporte;
    @FXML private DatePicker dpFechaDesde;
    @FXML private DatePicker dpFechaHasta;
    @FXML private TableView<Proyecto> tbReportes;
    @FXML private TableColumn<Proyecto, String> colRepFecha;
    @FXML private TableColumn<Proyecto, String> colRepCod;
    @FXML private TableColumn<Proyecto, String> colRepTit;
    @FXML private TableColumn<Proyecto, String> colRepProg;
    @FXML private TableColumn<Proyecto, String> colRepEst;

    private boolean sidebarColapsado = false;
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private CatalogoDAO catalogoDAO = new CatalogoDAO();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("/pe/edu/suiza/secretaria/fonts/fa-solid-900.ttf"), 14);
            javafx.scene.text.Font.loadFont(getClass().getResourceAsStream("fa-solid-900.ttf"), 14);
        } catch (Exception ignored) {}
        if (SesionActual.haySesionActiva()) {
            lblUsuarioLogueado.setText(SesionActual.getUsuarioLogueado().getNombreCompleto());
        } else {
            lblUsuarioLogueado.setText("Jefe de Investigación y Administración");
        }

        configurarTablas();
        configurarCombos();
        mostrarPanelAprobacion(null);

        if (txtBuscarProy != null) {
            txtBuscarProy.textProperty().addListener((obs, oldV, newV) -> {
                if (newV == null || newV.trim().isEmpty()) {
                    cargarDatosAprobacion();
                } else {
                    List<Proyecto> filtrados = proyectoDAO.buscarPorTexto(newV);
                    tbProyectosAprobacion.setItems(FXCollections.observableArrayList(filtrados));
                }
            });
        }
    }

    private void configuringTableCells() {
        colAproCod.setCellValueFactory(new PropertyValueFactory<>("codigoProyecto"));
        colAproTit.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAproProg.setCellValueFactory(new PropertyValueFactory<>("programaEstudio"));
        colAproMod.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        colAproEst.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colUsuUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        colUsuNom.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        colUsuRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
        colUsuPassClaro.setCellValueFactory(new PropertyValueFactory<>("passwordClaro"));
        colUsuEst.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configuringMoreTableCells() {
        colCatTipo.setCellValueFactory(new PropertyValueFactory<>("tipoCatalogo"));
        colCatCod.setCellValueFactory(new PropertyValueFactory<>("codigoItem"));
        colCatNom.setCellValueFactory(new PropertyValueFactory<>("nombreItem"));
        colCatDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        colRepFecha.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        colRepCod.setCellValueFactory(new PropertyValueFactory<>("codigoProyecto"));
        colRepTit.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colRepProg.setCellValueFactory(new PropertyValueFactory<>("programaEstudio"));
        colRepEst.setCellValueFactory(new PropertyValueFactory<>("estado"));
    }

    private void configurarTablas() {
        configuringTableCells();
        configuringMoreTableCells();
    }

    private void configurarCombos() {
        if (cbRangoTiempo != null) {
            cbRangoTiempo.getItems().addAll(
                "Último mes - Hace 1 mes",
                "Últimos 3 meses",
                "Últimos 6 meses",
                "Último año - Hace 1 año",
                "Últimos 3 años",
                "Últimos 5 años - Hace 5 años",
                "Rango Personalizado de Fechas"
            );
            cbRangoTiempo.getSelectionModel().selectFirst();
        }
        if (cbTipoReporte != null) {
            cbTipoReporte.getItems().addAll(
                "Proyectos Registrados y en Revisión",
                "Proyectos Aprobados para Titulación",
                "Proyectos Observados y Rechazados",
                "Consolidado por Programa de Estudio"
            );
            cbTipoReporte.getSelectionModel().selectFirst();
        }
        if (cbTipoCatalogo != null) {
            cbTipoCatalogo.getItems().addAll(
                "PROGRAMA_ESTUDIO",
                "MODALIDAD_TITULACION",
                "ASESOR",
                "ESTADO_PROYECTO"
            );
            cbTipoCatalogo.getSelectionModel().selectFirst();
            cbTipoCatalogo.setOnAction(e -> cargarDatosCatalogos());
        }
    }

    @FXML
    private void toggleSidebar(ActionEvent event) {
        sidebarColapsado = !sidebarColapsado;
        sidebar.setVisible(!sidebarColapsado);
        sidebar.setManaged(!sidebarColapsado);
    }

    @FXML
    private void mostrarPanelAprobacion(ActionEvent event) {
        ocultarTodosPaneles();
        panelAprobacion.setVisible(true);
        panelAprobacion.setManaged(true);
        actualizarEstilosNav(btnNavAprobacion);
        cargarDatosAprobacion();
    }

    @FXML
    private void mostrarPanelUsuarios(ActionEvent event) {
        ocultarTodosPaneles();
        panelUsuarios.setVisible(true);
        panelUsuarios.setManaged(true);
        actualizarEstilosNav(btnNavUsuarios);
        cargarDatosUsuarios();
    }

    @FXML
    private void mostrarPanelCatalogos(ActionEvent event) {
        ocultarTodosPaneles();
        panelCatalogos.setVisible(true);
        panelCatalogos.setManaged(true);
        actualizarEstilosNav(btnNavCatalogos);
        cargarDatosCatalogos();
    }

    @FXML
    private void mostrarPanelReportes(ActionEvent event) {
        ocultarTodosPaneles();
        panelReportes.setVisible(true);
        panelReportes.setManaged(true);
        actualizarEstilosNav(btnNavReportes);
        aplicarFiltroReporte(null);
    }

    @FXML
    private void mostrarPanelPerfil(ActionEvent event) {
        mostrarAlerta("Mi Perfil", "Para cambiar contraseña o actualizar datos consulte con soporte TI institucional.");
    }

    private void cargarDatosAprobacion() {
        List<Proyecto> lista = proyectoDAO.listarParaAprobacionFinal();
        if (lista.isEmpty()) {
            lista = proyectoDAO.listarTodos();
        }
        tbProyectosAprobacion.setItems(FXCollections.observableArrayList(lista));
    }

    private void cargarDatosUsuarios() {
        List<Usuario> lista = usuarioDAO.listarTodos();
        tbUsuarios.setItems(FXCollections.observableArrayList(lista));
    }

    private void cargarDatosCatalogos() {
        String tipoSeleccionado = cbTipoCatalogo.getValue();
        List<Catalogo> lista;
        if (tipoSeleccionado != null && !tipoSeleccionado.isEmpty()) {
            lista = catalogoDAO.listarPorTipo(tipoSeleccionado);
        } else {
            lista = catalogoDAO.listarTodos();
        }
        tbCatalogos.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void aprobarProyectoFinal(ActionEvent event) {
        Proyecto seleccionado = tbProyectosAprobacion.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor seleccione un proyecto de la tabla para aprobarlo.");
            return;
        }
        if (proyectoDAO.cambiarEstado(seleccionado.getCodigoProyecto(), "APROBADO_FINAL")) {
            mostrarAlerta("Aprobación Exitosa", "El proyecto " + seleccionado.getCodigoProyecto() + " ha sido APROBADO PARA TITULACIÓN oficialmente.");
            cargarDatosAprobacion();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el estado en la base de datos MySQL.");
        }
    }

    @FXML
    private void rechazarProyecto(ActionEvent event) {
        Proyecto seleccionado = tbProyectosAprobacion.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor seleccione un proyecto de la tabla para rechazarlo.");
            return;
        }
        if (proyectoDAO.cambiarEstado(seleccionado.getCodigoProyecto(), "RECHAZADO")) {
            mostrarAlerta("Proyecto Rechazado", "El proyecto " + seleccionado.getCodigoProyecto() + " ha sido devuelto y rechazado.");
            cargarDatosAprobacion();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el estado en la base de datos MySQL.");
        }
    }

    @FXML
    private void nuevoUsuario(ActionEvent event) {
        mostrarAlerta("Gestión de Usuarios", "En el modelo inicial de XAMPP los 3 roles oficiales del documento están listos para operar.");
    }

    @FXML
    private void nuevoCatalogo(ActionEvent event) {
        mostrarAlerta("Mantenimiento de Catálogos", "Para insertar una nueva Carrera o Modalidad agregue el registro en la tabla catalogos de XAMPP MySQL.");
    }

    @FXML
    private void aplicarFiltroReporte(ActionEvent event) {
        LocalDate hoy = LocalDate.now();
        LocalDate desde;
        
        String seleccion = cbRangoTiempo.getValue();
        if (seleccion == null) seleccion = "";
        
        if (seleccion.contains("1 mes")) {
            desde = hoy.minusMonths(1);
        } else if (seleccion.contains("3 meses")) {
            desde = hoy.minusMonths(3);
        } else if (seleccion.contains("6 meses")) {
            desde = hoy.minusMonths(6);
        } else if (seleccion.contains("1 año")) {
            desde = hoy.minusYears(1);
        } else if (seleccion.contains("3 años")) {
            desde = hoy.minusYears(3);
        } else if (seleccion.contains("5 años")) {
            desde = hoy.minusYears(5);
        } else if (dpFechaDesde.getValue() != null && dpFechaHasta.getValue() != null) {
            desde = dpFechaDesde.getValue();
            hoy = dpFechaHasta.getValue();
        } else {
            desde = hoy.minusYears(5);
        }

        List<Proyecto> resultados = proyectoDAO.listarPorRangoTiempo(desde.toString(), hoy.toString());
        if (resultados.isEmpty()) {
            resultados = proyectoDAO.listarTodos();
        }
        tbReportes.setItems(FXCollections.observableArrayList(resultados));
    }

    @FXML
    private void exportarReporteInstitucional(ActionEvent event) {
        mostrarAlerta("Exportar Reporte", "Reporte generado con membrete oficial del IESTP Suiza y rango seleccionado.");
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

    private void ocultarTodosPaneles() {
        panelAprobacion.setVisible(false);
        panelAprobacion.setManaged(false);
        panelUsuarios.setVisible(false);
        panelUsuarios.setManaged(false);
        panelCatalogos.setVisible(false);
        panelCatalogos.setManaged(false);
        panelReportes.setVisible(false);
        panelReportes.setManaged(false);
    }

    private void actualizarEstilosNav(Button btnActivo) {
        btnNavAprobacion.getStyleClass().remove("sidebar-btn-active");
        btnNavUsuarios.getStyleClass().remove("sidebar-btn-active");
        btnNavCatalogos.getStyleClass().remove("sidebar-btn-active");
        btnNavReportes.getStyleClass().remove("sidebar-btn-active");
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
