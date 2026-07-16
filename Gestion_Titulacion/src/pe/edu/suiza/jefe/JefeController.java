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

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;

import pe.edu.suiza.dao.CatalogoDAO;
import pe.edu.suiza.dao.ObservacionDAO;
import pe.edu.suiza.dao.ProyectoDAO;
import pe.edu.suiza.dao.UsuarioDAO;
import pe.edu.suiza.modelo.Catalogo;
import pe.edu.suiza.modelo.Observacion;
import pe.edu.suiza.modelo.Proyecto;
import pe.edu.suiza.modelo.Usuario;
import pe.edu.suiza.utilidades.ExportadorInstitucional;
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
        String tipoSel = cbTipoCatalogo.getValue();
        if (tipoSel == null || tipoSel.isEmpty()) {
            mostrarAlerta("Tipo de Catálogo requerido", "Seleccione primero el Tipo de Catálogo en la lista desplegable (Ej: PROGRAMA_ESTUDIO).");
            return;
        }
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuevo ítem para " + tipoSel);
        dialog.setHeaderText("Registro de nuevo ítem en " + tipoSel);
        dialog.setContentText("Ingrese el nombre del nuevo ítem (Ej: Mecatrónica Automotriz):");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nombre -> {
            if (!nombre.trim().isEmpty()) {
                String codigoGen = tipoSel.substring(0, Math.min(3, tipoSel.length())) + "-" + (System.currentTimeMillis() % 10000);
                Catalogo nuevo = new Catalogo(0, tipoSel, codigoGen, nombre.trim(), "Ítem registrado desde Jefatura", "ACTIVO");
                if (catalogoDAO.insertar(nuevo)) {
                    mostrarAlerta("Catálogo Actualizado", "Se registró exitosamente el ítem '" + nombre + "' en " + tipoSel + ".");
                    cargarDatosCatalogos();
                } else {
                    mostrarAlerta("Error", "No se pudo registrar en la base de datos MySQL.");
                }
            }
        });
    }

    @FXML
    private void aplicarFiltroReporte(ActionEvent event) {
        LocalDate hoy = LocalDate.now();
        LocalDate desde = hoy.minusYears(5);
        
        String seleccion = cbRangoTiempo != null && cbRangoTiempo.getValue() != null ? cbRangoTiempo.getValue() : "";
        
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
        } else if (dpFechaDesde != null && dpFechaHasta != null && dpFechaDesde.getValue() != null && dpFechaHasta.getValue() != null) {
            desde = dpFechaDesde.getValue();
            hoy = dpFechaHasta.getValue();
        }

        List<Proyecto> resultados = proyectoDAO.listarPorRangoTiempo(desde.toString(), hoy.toString());
        
        String tipoRep = cbTipoReporte != null && cbTipoReporte.getValue() != null ? cbTipoReporte.getValue() : "";
        if (!tipoRep.isEmpty() && !"Todos los Proyectos".equals(tipoRep) && !"Consolidado por Programa de Estudio".equals(tipoRep)) {
            List<Proyecto> filtradosPorTipo = new ArrayList<>();
            for (Proyecto p : resultados) {
                if (tipoRep.contains("Aprobados") && p.getEstado().contains("APROBADO")) {
                    filtradosPorTipo.add(p);
                } else if (tipoRep.contains("Observados") && (p.getEstado().contains("OBSERVADO") || p.getEstado().contains("RECHAZADO"))) {
                    filtradosPorTipo.add(p);
                } else if (tipoRep.contains("Revisión") && p.getEstado().contains("REVISION")) {
                    filtradosPorTipo.add(p);
                } else {
                    filtradosPorTipo.add(p);
                }
            }
            resultados = filtradosPorTipo;
        }
        if (tbReportes != null) {
            tbReportes.setItems(FXCollections.observableArrayList(resultados));
        }
    }

    @FXML
    private void exportarReporteInstitucional(ActionEvent event) {
        descargarReporteExcel(event);
    }

    @FXML
    private void descargarReporteExcel(ActionEvent event) {
        if (tbReportes == null || tbReportes.getItems().isEmpty()) {
            mostrarAlerta("Tabla vacía", "No hay registros en la tabla para exportar a Excel. Aplique un filtro primero.");
            return;
        }
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        String[] cabeceras = {"FECHA REGISTRO", "CÓDIGO PROYECTO", "TÍTULO DEL PROYECTO", "PROGRAMA DE ESTUDIO", "MODALIDAD", "ASESOR", "ESTADO ACTUAL"};
        List<String[]> filas = new ArrayList<>();
        for (Proyecto p : tbReportes.getItems()) {
            filas.add(new String[]{
                p.getFechaRegistro() != null ? p.getFechaRegistro().toString() : "-",
                p.getCodigoProyecto(),
                p.getTitulo() != null ? p.getTitulo() : "",
                p.getProgramaEstudio() != null ? p.getProgramaEstudio() : "",
                p.getModalidad() != null ? p.getModalidad() : "",
                p.getAsesor() != null ? p.getAsesor() : "",
                p.getEstado()
            });
        }
        ExportadorInstitucional.exportarTablaExcelInstitucional(stage, "REPORTE OFICIAL Y CONSOLIDADO DE PROYECTOS", "MÓDULO DE JEFATURA DE INVESTIGACIÓN", cabeceras, filas, "Reporte_Oficial_Jefatura_Suiza");
    }

    @FXML
    private void descargarReportePDF(ActionEvent event) {
        if (tbReportes == null || tbReportes.getItems().isEmpty()) {
            mostrarAlerta("Tabla vacía", "No hay registros en la tabla para generar el documento oficial en PDF/HTML.");
            return;
        }
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        String[] cabeceras = {"FECHA REGISTRO", "CÓDIGO PROYECTO", "TÍTULO DEL PROYECTO", "PROGRAMA DE ESTUDIO", "MODALIDAD", "ASESOR", "ESTADO ACTUAL"};
        List<String[]> filas = new ArrayList<>();
        for (Proyecto p : tbReportes.getItems()) {
            filas.add(new String[]{
                p.getFechaRegistro() != null ? p.getFechaRegistro().toString() : "-",
                p.getCodigoProyecto(),
                p.getTitulo() != null ? p.getTitulo() : "",
                p.getProgramaEstudio() != null ? p.getProgramaEstudio() : "",
                p.getModalidad() != null ? p.getModalidad() : "",
                p.getAsesor() != null ? p.getAsesor() : "",
                p.getEstado()
            });
        }
        ExportadorInstitucional.exportarDocumentoPdfHtmlInstitucional(stage, "REPORTE INSTITUCIONAL DE PROYECTOS DE TITULACIÓN", "CONSOLIDADO OFICIAL - FILTRO TEMPORAL", "JEFATURA DE INVESTIGACIÓN Y DIRECCIÓN", cabeceras, filas, "Reporte_Oficial_Jefatura_Suiza");
    }

    @FXML
    private void descargarObservaciones(ActionEvent event) {
        descargarObservacionesExcel(event);
    }

    @FXML
    private void descargarObservacionesExcel(ActionEvent event) {
        Proyecto sel = tbProyectosAprobacion != null ? tbProyectosAprobacion.getSelectionModel().getSelectedItem() : null;
        if (sel == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto para descargar su historial de observaciones en Excel.");
            return;
        }
        List<Observacion> observaciones = observacionDAO.listarPorCodigoProyecto(sel.getCodigoProyecto());
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        String[] cabeceras = {"FECHA OBSERVACIÓN", "EVALUADOR / ROL", "ESTADO SUBSANACIÓN", "DETALLE O CORRECCIÓN SOLICITADA"};
        List<String[]> filas = new ArrayList<>();
        if (observaciones.isEmpty()) {
            filas.add(new String[]{"-", "SISTEMA", "SIN OBSERVACIONES", "No existen observaciones registradas para este proyecto."});
        } else {
            for (Observacion obs : observaciones) {
                filas.add(new String[]{
                    obs.getFechaObservacion() != null ? obs.getFechaObservacion().toString() : "-",
                    obs.getRolAutor(),
                    obs.getEstadoObservacion(),
                    obs.getDescripcion() != null ? obs.getDescripcion() : "-"
                });
            }
        }
        ExportadorInstitucional.exportarTablaExcelInstitucional(stage, "HISTORIAL Y EVALUACIÓN DE TITULACIÓN", "CÓDIGO: " + sel.getCodigoProyecto() + " | TÍTULO: " + sel.getTitulo(), cabeceras, filas, "Observaciones_Jefatura_" + sel.getCodigoProyecto());
    }

    @FXML
    private void descargarObservacionesPDF(ActionEvent event) {
        Proyecto sel = tbProyectosAprobacion != null ? tbProyectosAprobacion.getSelectionModel().getSelectedItem() : null;
        if (sel == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto para generar el documento PDF/HTML de sus observaciones.");
            return;
        }
        List<Observacion> observaciones = observacionDAO.listarPorCodigoProyecto(sel.getCodigoProyecto());
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        String[] cabeceras = {"FECHA", "EVALUADOR / ROL", "ESTADO", "DETALLE DE LA OBSERVACIÓN O CORRECCIÓN"};
        List<String[]> filas = new ArrayList<>();
        if (observaciones.isEmpty()) {
            filas.add(new String[]{"-", "SISTEMA", "SIN OBSERVACIONES", "No existen observaciones registradas para este proyecto."});
        } else {
            for (Observacion obs : observaciones) {
                filas.add(new String[]{
                    obs.getFechaObservacion() != null ? obs.getFechaObservacion().toString() : "-",
                    obs.getRolAutor(),
                    obs.getEstadoObservacion(),
                    obs.getDescripcion() != null ? obs.getDescripcion() : "-"
                });
            }
        }
        ExportadorInstitucional.exportarDocumentoPdfHtmlInstitucional(stage, "EVALUACIÓN Y DICTAMEN DE TITULACIÓN", "EXPEDIENTE: " + sel.getCodigoProyecto() + " | " + sel.getTitulo(), "JEFATURA DE INVESTIGACIÓN", cabeceras, filas, "Reporte_Obs_Jefatura_" + sel.getCodigoProyecto());
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
