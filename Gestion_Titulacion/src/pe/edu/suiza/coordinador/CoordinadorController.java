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
import pe.edu.suiza.utilidades.SesionActual;

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
            mostrarAlerta("Selección requerida", "Seleccione un proyecto para descargar sus observaciones en Excel.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Observaciones a Excel (.CSV)");
        fileChooser.setInitialFileName("Observaciones_Excel_" + codProy + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV Compatible Excel (*.csv)", "*.csv"));
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
                pw.write('\ufeff'); // BOM UTF-8 para abrir directo en Excel con acentos
                pw.println("CÓDIGO PROYECTO;FECHA OBSERVACIÓN;EVALUADOR / ROL;ESTADO SUBSANACIÓN;DETALLE O CORRECCIÓN SOLICITADA");
                if (observaciones.isEmpty()) {
                    pw.println(codProy + ";N/A;COORDINACIÓN;SIN OBSERVACIONES;No existen observaciones registradas en este contexto.");
                } else {
                    for (Observacion obs : observaciones) {
                        String cod = obs.getCodigoProyectoAux() != null ? obs.getCodigoProyectoAux() : codProy;
                        String det = obs.getDescripcion() != null ? obs.getDescripcion().replace("\"", "\"\"").replace("\n", " ").replace("\r", "") : "";
                        pw.println(String.format("%s;%s;\"%s\";%s;\"%s\"",
                            cod,
                            obs.getFechaObservacion() != null ? obs.getFechaObservacion().toString() : "-",
                            obs.getRolAutor(),
                            obs.getEstadoObservacion(),
                            det
                        ));
                    }
                }
                pw.flush();
                mostrarAlerta("Exportación Completa", "Las observaciones se guardaron en Excel:\n" + archivo.getAbsolutePath());
                try { Desktop.getDesktop().open(archivo); } catch (Exception ignored) {}
            } catch (Exception e) {
                mostrarAlerta("Error al Exportar", "Ocurrió un error al guardar el archivo Excel: " + e.getMessage());
            }
        }
    }

    @FXML
    private void descargarObservacionesPDF(ActionEvent event) {
        List<Observacion> observaciones = obtenerObservacionesLista();
        String codProy = obtenerCodigoProyectoContexto();
        if (observaciones == null || codProy == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto para generar el documento PDF/HTML de sus observaciones.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Descargar Observaciones en Documento Oficial (PDF/HTML)");
        fileChooser.setInitialFileName("Reporte_Observaciones_" + codProy + ".html");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documento Oficial HTML/PDF (*.html)", "*.html"));
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
                pw.println("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'><title>Observaciones Coordinación - IESTP Suiza</title>");
                pw.println("<style>body{font-family:'Segoe UI',sans-serif;margin:40px;color:#1e293b;} h1{color:#1e3a8a;border-bottom:3px solid #dc2626;padding-bottom:10px;} h2{color:#334155;} table{width:100%;border-collapse:collapse;margin-top:20px;} th,td{border:1px solid #cbd5e1;padding:12px;text-align:left;} th{background-color:#1e3a8a;color:white;} tr:nth-child(even){background-color:#f8fafc;} .meta{background:#eff6ff;padding:15px;border-radius:8px;margin-bottom:20px;border-left:4px solid #3b82f6;} .tag{padding:4px 8px;border-radius:4px;font-weight:bold;font-size:0.85em;} .tag-pend{background:#fef3c7;color:#92400e;} .tag-sub{background:#dcfce7;color:#166534;}</style></head><body>");
                pw.println("<h1>INSTITUTO DE EDUCACIÓN SUPERIOR TECNOLÓGICO PÚBLICO SUIZA</h1>");
                pw.println("<h2>REPORTE METODOLÓGICO Y CONTROL DE OBSERVACIONES (COORDINACIÓN)</h2>");
                pw.println("<div class='meta'><b>Referencia / Código:</b> " + codProy + "<br><b>Total de Observaciones:</b> " + observaciones.size() + "<br><b>Fecha de Emisión del Documento:</b> " + LocalDate.now() + "</div>");
                pw.println("<table><thead><tr><th>Proyecto</th><th>Fecha</th><th>Evaluador / Rol</th><th>Estado</th><th>Detalle de la Observación o Corrección</th></tr></thead><tbody>");
                if (observaciones.isEmpty()) {
                    pw.println("<tr><td colspan='5' style='text-align:center;padding:20px;color:#64748b;'>No existen observaciones registradas en este contexto.</td></tr>");
                } else {
                    for (Observacion obs : observaciones) {
                        String cod = obs.getCodigoProyectoAux() != null ? obs.getCodigoProyectoAux() : codProy;
                        String claseEstado = obs.getEstadoObservacion().contains("SUBSANADO") ? "tag-sub" : "tag-pend";
                        pw.println("<tr><td><b>" + cod + "</b></td><td>" + (obs.getFechaObservacion()!=null?obs.getFechaObservacion():"-") + "</td><td><b>" + obs.getRolAutor() + "</b></td><td><span class='tag " + claseEstado + "'>" + obs.getEstadoObservacion() + "</span></td><td>" + (obs.getDescripcion()!=null?obs.getDescripcion():"-") + "</td></tr>");
                    }
                }
                pw.println("</tbody></table></body></html>");
                pw.flush();
                mostrarAlerta("Documento Oficial PDF/HTML Generado", "El archivo de observaciones para imprimir o PDF se creó en:\n" + archivo.getAbsolutePath());
                try { Desktop.getDesktop().open(archivo); } catch (Exception ignored) {}
            } catch (Exception e) {
                mostrarAlerta("Error al Generar Documento", "Ocurrió un error al crear el archivo: " + e.getMessage());
            }
        }
    }


    @FXML
    private void exportarProyectosExcel(ActionEvent event) {
        if (tbProyectos == null || tbProyectos.getItems().isEmpty()) {
            mostrarAlerta("Tabla vacía", "No hay proyectos en la tabla para exportar a Excel.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Proyectos a Excel (.CSV)");
        fileChooser.setInitialFileName("Listado_Proyectos_Coordinacion_Suiza.csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo CSV Compatible Excel (*.csv)", "*.csv"));
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
                pw.write('\ufeff'); // BOM UTF-8
                pw.println("CÓDIGO;TÍTULO DEL PROYECTO;PROGRAMA DE ESTUDIO;MODALIDAD;ASESOR;ESTADO;FECHA REGISTRO");
                for (Proyecto p : tbProyectos.getItems()) {
                    pw.println(String.format("%s;\"%s\";\"%s\";\"%s\";\"%s\";%s;%s",
                        p.getCodigoProyecto(),
                        p.getTitulo() != null ? p.getTitulo().replace("\"", "\"\"") : "",
                        p.getProgramaEstudio() != null ? p.getProgramaEstudio().replace("\"", "\"\"") : "",
                        p.getModalidad() != null ? p.getModalidad().replace("\"", "\"\"") : "",
                        p.getAsesor() != null ? p.getAsesor().replace("\"", "\"\"") : "",
                        p.getEstado(),
                        p.getFechaRegistro() != null ? p.getFechaRegistro().toString() : "-"
                    ));
                }
                pw.flush();
                mostrarAlerta("Exportación Completa", "El reporte se guardó en:\n" + archivo.getAbsolutePath());
                try { Desktop.getDesktop().open(archivo); } catch (Exception ignored) {}
            } catch (Exception e) {
                mostrarAlerta("Error al Exportar", "Error escribiendo el archivo CSV: " + e.getMessage());
            }
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
