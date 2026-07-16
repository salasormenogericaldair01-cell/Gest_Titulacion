package pe.edu.suiza.coordinador;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import pe.edu.suiza.dao.ObservacionDAO;
import pe.edu.suiza.dao.ProyectoDAO;
import pe.edu.suiza.modelo.Observacion;
import pe.edu.suiza.modelo.Proyecto;
import pe.edu.suiza.modelo.Usuario;
import pe.edu.suiza.utilidades.ExportadorInstitucional;
import pe.edu.suiza.utilidades.SesionActual;
import java.util.ArrayList;

/**
 * Controlador de Coordinación Académica.
 * Módulo sin paréntesis en textos, con íconos oficiales y evaluación en MySQL.
 */
public class CoordinadorController implements Initializable {

    @FXML private Button btnSidebarToggle;
    @FXML private Label lblUsuarioLogueado;
    @FXML private VBox sidebar;
    @FXML private Button btnNavRevision;
    @FXML private Button btnNavObservaciones;
    @FXML private Button btnNavPerfil;
    
    @FXML private VBox panelRevision;
    @FXML private TextField txtBuscar;
    @FXML private TableView<Proyecto> tbProyectos;
    @FXML private TableColumn<Proyecto, String> colCod;
    @FXML private TableColumn<Proyecto, String> colTit;
    @FXML private TableColumn<Proyecto, String> colProg;
    @FXML private TableColumn<Proyecto, String> colMod;
    @FXML private TableColumn<Proyecto, String> colEst;
    @FXML private TextArea txtObservacion;
    @FXML private ComboBox<String> cbEstadoRevision;
    
    @FXML private VBox panelObservaciones;
    @FXML private TableView<Observacion> tbObservaciones;
    @FXML private TableColumn<Observacion, String> colObsFecha;
    @FXML private TableColumn<Observacion, String> colObsProy;
    @FXML private TableColumn<Observacion, String> colObsDesc;
    @FXML private TableColumn<Observacion, String> colObsEst;

    private boolean sidebarColapsado = false;
    private ProyectoDAO proyectoDAO = new ProyectoDAO();
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
            lblUsuarioLogueado.setText("Coordinador Académico");
        }

        configurarTablas();
        configurarCombos();
        mostrarPanelRevision(null);

        if (txtBuscar != null) {
            txtBuscar.textProperty().addListener((obs, oldV, newV) -> {
                if (newV == null || newV.trim().isEmpty()) {
                    cargarProyectosParaRevision();
                } else {
                    List<Proyecto> filtrados = proyectoDAO.buscarPorTexto(newV);
                    tbProyectos.setItems(FXCollections.observableArrayList(filtrados));
                }
            });
        }
    }

    private void configuringTableCells() {
        colCod.setCellValueFactory(new PropertyValueFactory<>("codigoProyecto"));
        colTit.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colProg.setCellValueFactory(new PropertyValueFactory<>("programaEstudio"));
        colMod.setCellValueFactory(new PropertyValueFactory<>("modalidad"));
        colEst.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colObsFecha.setCellValueFactory(new PropertyValueFactory<>("fechaObservacion"));
        colObsProy.setCellValueFactory(new PropertyValueFactory<>("codigoProyectoAux"));
        colObsDesc.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colObsEst.setCellValueFactory(new PropertyValueFactory<>("estadoObservacion"));
    }

    private void configurarTablas() {
        configuringTableCells();
        
        tbProyectos.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                txtObservacion.clear();
                if (newVal.getEstado().equals("APROBADO_COORDINACION")) {
                    cbEstadoRevision.setValue("APROBADO_COORDINACION");
                } else if (newVal.getEstado().equals("OBSERVADO")) {
                    cbEstadoRevision.setValue("OBSERVADO");
                } else {
                    cbEstadoRevision.setValue("APROBADO_COORDINACION");
                }
            }
        });
    }

    private void configurarCombos() {
        if (cbEstadoRevision != null) {
            cbEstadoRevision.getItems().addAll(
                "APROBADO_COORDINACION",
                "OBSERVADO"
            );
        }
    }

    @FXML
    private void toggleSidebar(ActionEvent event) {
        sidebarColapsado = !sidebarColapsado;
        sidebar.setVisible(!sidebarColapsado);
        sidebar.setManaged(!sidebarColapsado);
    }

    @FXML
    private void mostrarPanelRevision(ActionEvent event) {
        panelRevision.setVisible(true);
        panelRevision.setManaged(true);
        panelObservaciones.setVisible(false);
        panelObservaciones.setManaged(false);
        actualizarEstilosNav(btnNavRevision);
        cargarProyectosParaRevision();
    }

    @FXML
    private void mostrarPanelObservaciones(ActionEvent event) {
        panelRevision.setVisible(false);
        panelRevision.setManaged(false);
        panelObservaciones.setVisible(true);
        panelObservaciones.setManaged(true);
        actualizarEstilosNav(btnNavObservaciones);
        cargarHistorialObservaciones();
    }

    @FXML
    private void mostrarPanelPerfil(ActionEvent event) {
        mostrarAlerta("Mi Perfil", "Para actualizar su clave de acceso de coordinación, consulte con Jefatura y soporte TI.");
    }

    private void cargarProyectosParaRevision() {
        List<Proyecto> lista = proyectoDAO.listarTodos();
        tbProyectos.setItems(FXCollections.observableArrayList(lista));
    }

    private void cargarHistorialObservaciones() {
        List<Observacion> lista = observacionDAO.listarPorRol("COORDINADOR");
        if (lista.isEmpty()) {
            lista = observacionDAO.listarTodas();
        }
        tbObservaciones.setItems(FXCollections.observableArrayList(lista));
    }

    @FXML
    private void guardarRevision(ActionEvent event) {
        Proyecto seleccionado = tbProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor seleccione un proyecto de la tabla para calificarlo.");
            return;
        }

        String nuevoEstado = cbEstadoRevision.getValue();
        if (nuevoEstado == null || nuevoEstado.isEmpty()) {
            mostrarAlerta("Estado requerido", "Por favor seleccione un estado de calificación.");
            return;
        }

        String textoObs = txtObservacion.getText().trim();
        int idUsr = SesionActual.haySesionActiva() ? SesionActual.getUsuarioLogueado().getIdUsuario() : 0;

        if (!textoObs.isEmpty()) {
            String estObs = nuevoEstado.equals("OBSERVADO") ? "PENDIENTE" : "SUBSANADO";
            observacionDAO.registrarObservacionPorCodigo(seleccionado.getCodigoProyecto(), idUsr, "COORDINADOR", textoObs, estObs);
        } else if (nuevoEstado.equals("OBSERVADO")) {
            mostrarAlerta("Detalle Requerido", "Para marcar un proyecto como OBSERVADO debe detallar las correcciones en el cuadro de texto.");
            return;
        }

        if (proyectoDAO.cambiarEstado(seleccionado.getCodigoProyecto(), nuevoEstado)) {
            mostrarAlerta("Evaluación Guardada", "El proyecto " + seleccionado.getCodigoProyecto() + " ahora está en estado: " + nuevoEstado);
            txtObservacion.clear();
            cargarProyectosParaRevision();
        } else {
            mostrarAlerta("Error BD", "No se pudo actualizar el estado del proyecto en la base de datos MySQL.");
        }
    }

    private List<Observacion> obtenerObservacionesLista() {
        if (panelObservaciones != null && panelObservaciones.isVisible()) {
            Observacion obsSel = tbObservaciones != null ? tbObservaciones.getSelectionModel().getSelectedItem() : null;
            if (obsSel != null && obsSel.getCodigoProyectoAux() != null && !obsSel.getCodigoProyectoAux().isEmpty()) {
                return observacionDAO.listarPorCodigoProyecto(obsSel.getCodigoProyectoAux());
            }
            if (tbObservaciones != null && tbObservaciones.getItems() != null && !tbObservaciones.getItems().isEmpty()) {
                return tbObservaciones.getItems();
            }
            return observacionDAO.listarTodas();
        } else {
            Proyecto sel = tbProyectos != null ? tbProyectos.getSelectionModel().getSelectedItem() : null;
            if (sel != null) {
                return observacionDAO.listarPorCodigoProyecto(sel.getCodigoProyecto());
            }
        }
        return null;
    }

    private String obtenerCodigoProyectoContexto() {
        if (panelObservaciones != null && panelObservaciones.isVisible()) {
            Observacion obsSel = tbObservaciones != null ? tbObservaciones.getSelectionModel().getSelectedItem() : null;
            if (obsSel != null && obsSel.getCodigoProyectoAux() != null) return obsSel.getCodigoProyectoAux();
            return "GENERAL_COORDINACION";
        } else {
            Proyecto sel = tbProyectos != null ? tbProyectos.getSelectionModel().getSelectedItem() : null;
            if (sel != null) return sel.getCodigoProyecto();
        }
        return null;
    }

    @FXML
    private void descargarObservaciones(ActionEvent event) {
        List<Observacion> observaciones = obtenerObservacionesLista();
        String codProy = obtenerCodigoProyectoContexto();
        if (observaciones == null || codProy == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto en la tabla para descargar su historial de observaciones.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Descargar Observaciones Metodológicas (.TXT)");
        fileChooser.setInitialFileName("Observaciones_Coordinacion_" + codProy + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo de Texto (*.txt)", "*.txt"));
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
                pw.println("======================================================================");
                pw.println("IESTP SUIZA - EVALUACIÓN Y OBSERVACIONES METODOLÓGICAS (COORDINACIÓN)");
                pw.println("======================================================================");
                pw.println("Referencia / Código: " + codProy);
                pw.println("----------------------------------------------------------------------\n");
                if (observaciones.isEmpty()) {
                    pw.println("No se han registrado observaciones metodológicas en este contexto.");
                } else {
                    for (Observacion obs : observaciones) {
                        String cod = obs.getCodigoProyectoAux() != null ? "[" + obs.getCodigoProyectoAux() + "] " : "";
                        pw.println(cod + "[" + obs.getFechaObservacion() + "] Evaluador: " + obs.getRolAutor() + " (" + obs.getEstadoObservacion() + ")");
                        pw.println("Detalle: " + obs.getDescripcion());
                        pw.println("----------------------------------------------------------------------");
                    }
                }
                pw.flush();
                mostrarAlerta("Descarga Exitosa", "Las observaciones se guardaron en:\n" + archivo.getAbsolutePath());
                try { Desktop.getDesktop().open(archivo); } catch (Exception ignored) {}
            } catch (Exception e) {
                mostrarAlerta("Error al Descargar", "Ocurrió un error al guardar las observaciones: " + e.getMessage());
            }
        }
    }

    @FXML
    private void descargarObservacionesExcel(ActionEvent event) {
        List<Observacion> observaciones = obtenerObservacionesLista();
        String codProy = obtenerCodigoProyectoContexto();
        if (observaciones == null || codProy == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto para descargar sus observaciones con diseño institucional.");
            return;
        }
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        String[] cabeceras = {"CÓDIGO / PROYECTO", "FECHA OBSERVACIÓN", "EVALUADOR / ROL", "ESTADO SUBSANACIÓN", "DETALLE O CORRECCIÓN SOLICITADA"};
        List<String[]> filas = new ArrayList<>();
        if (observaciones.isEmpty()) {
            filas.add(new String[]{codProy, "-", "COORDINACIÓN", "SIN OBSERVACIONES", "No existen observaciones registradas en este contexto."});
        } else {
            for (Observacion obs : observaciones) {
                String cod = obs.getCodigoProyectoAux() != null ? obs.getCodigoProyectoAux() : codProy;
                filas.add(new String[]{
                    cod,
                    obs.getFechaObservacion() != null ? obs.getFechaObservacion().toString() : "-",
                    obs.getRolAutor(),
                    obs.getEstadoObservacion(),
                    obs.getDescripcion() != null ? obs.getDescripcion() : "-"
                });
            }
        }
        ExportadorInstitucional.exportarTablaExcelInstitucional(stage, "OBSERVACIONES METODOLÓGICAS - COORDINACIÓN", "REFERENCIA: " + codProy, cabeceras, filas, "Observaciones_Coordinacion_" + codProy);
    }

    @FXML
    private void descargarObservacionesPDF(ActionEvent event) {
        List<Observacion> observaciones = obtenerObservacionesLista();
        String codProy = obtenerCodigoProyectoContexto();
        if (observaciones == null || codProy == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto para generar el documento PDF/HTML con diseño institucional.");
            return;
        }
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        String[] cabeceras = {"PROYECTO", "FECHA", "EVALUADOR / ROL", "ESTADO", "DETALLE DE LA OBSERVACIÓN O CORRECCIÓN"};
        List<String[]> filas = new ArrayList<>();
        if (observaciones.isEmpty()) {
            filas.add(new String[]{codProy, "-", "COORDINACIÓN", "SIN OBSERVACIONES", "No existen observaciones registradas en este contexto."});
        } else {
            for (Observacion obs : observaciones) {
                String cod = obs.getCodigoProyectoAux() != null ? obs.getCodigoProyectoAux() : codProy;
                filas.add(new String[]{
                    cod,
                    obs.getFechaObservacion() != null ? obs.getFechaObservacion().toString() : "-",
                    obs.getRolAutor(),
                    obs.getEstadoObservacion(),
                    obs.getDescripcion() != null ? obs.getDescripcion() : "-"
                });
            }
        }
        ExportadorInstitucional.exportarDocumentoPdfHtmlInstitucional(stage, "REPORTE E HISTORIAL METODOLÓGICO", "EXPEDIENTE / REFERENCIA: " + codProy, "COORDINACIÓN ACADÉMICA Y METODOLÓGICA", cabeceras, filas, "Reporte_Obs_Coordinacion_" + codProy);
    }


    @FXML
    private void exportarProyectosExcel(ActionEvent event) {
        if (tbProyectos == null || tbProyectos.getItems().isEmpty()) {
            mostrarAlerta("Tabla vacía", "No hay proyectos en la tabla para exportar a Excel.");
            return;
        }
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        String[] cabeceras = {"CÓDIGO", "TÍTULO DEL PROYECTO", "PROGRAMA DE ESTUDIO", "MODALIDAD", "ASESOR", "ESTADO", "FECHA REGISTRO"};
        List<String[]> filas = new ArrayList<>();
        for (Proyecto p : tbProyectos.getItems()) {
            filas.add(new String[]{
                p.getCodigoProyecto(),
                p.getTitulo() != null ? p.getTitulo() : "",
                p.getProgramaEstudio() != null ? p.getProgramaEstudio() : "",
                p.getModalidad() != null ? p.getModalidad() : "",
                p.getAsesor() != null ? p.getAsesor() : "",
                p.getEstado(),
                p.getFechaRegistro() != null ? p.getFechaRegistro().toString() : "-"
            });
        }
        ExportadorInstitucional.exportarTablaExcelInstitucional(stage, "CONSOLIDADO DE EVALUACIÓN DE PROYECTOS", "MÓDULO DE COORDINACIÓN ACADÉMICA", cabeceras, filas, "Listado_Proyectos_Coordinacion_Suiza");
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
        btnNavRevision.getStyleClass().remove("sidebar-btn-active");
        btnNavObservaciones.getStyleClass().remove("sidebar-btn-active");
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
