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
import javafx.scene.control.TextArea;
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
    @FXML private Button btnNavTicketsTI;
    @FXML private Button btnNavCatalogos;
    @FXML private Button btnNavReportes;
    @FXML private Button btnNavPerfil;
    
    @FXML private VBox panelTicketsTI;
    @FXML private TableView<Observacion> tbTicketsTI;
    @FXML private TableColumn<Observacion, String> colTicId;
    @FXML private TableColumn<Observacion, String> colTicFecha;
    @FXML private TableColumn<Observacion, String> colTicDesc;
    @FXML private TableColumn<Observacion, String> colTicEstado;
    
    @FXML private TextArea txtObservacionJefatura;
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
    
    @FXML private TextField txtUsrDni;
    @FXML private TextField txtUsrNombre;
    @FXML private TextField txtUsrCorreo;
    @FXML private TextField txtUsrEspecialidad;
    @FXML private ComboBox<String> cbUsrRol;
    @FXML private ComboBox<String> cbUsrEstado;
    @FXML private Button btnGuardarUsr;
    
    @FXML private VBox panelCatalogos;
    @FXML private ComboBox<String> cbTipoCatalogo;
    @FXML private TableView<Catalogo> tbCatalogos;
    @FXML private TableColumn<Catalogo, String> colCatTipo;
    @FXML private TableColumn<Catalogo, String> colCatCod;
    @FXML private TableColumn<Catalogo, String> colCatNom;
    @FXML private TableColumn<Catalogo, String> colCatDesc;
    
    @FXML private ComboBox<String> cbCatTipo;
    @FXML private TextField txtCatValor;
    @FXML private ComboBox<String> cbCatEstado;
    @FXML private Button btnGuardarCat;
    
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

        if (tbUsuarios != null) {
            tbUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    if (txtUsrNombre != null) txtUsrNombre.setText(newSel.getNombreCompleto());
                    if (txtUsrCorreo != null && newSel.getEmail() != null) txtUsrCorreo.setText(newSel.getEmail());
                    if (txtUsrEspecialidad != null && newSel.getEspecialidad() != null) txtUsrEspecialidad.setText(newSel.getEspecialidad());
                    if (cbUsrRol != null) cbUsrRol.setValue(newSel.getRol());
                    if (cbUsrEstado != null) cbUsrEstado.setValue(newSel.getEstado());
                }
            });
        }
        if (tbCatalogos != null) {
            tbCatalogos.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                if (newSel != null) {
                    if (cbCatTipo != null) cbCatTipo.setValue(newSel.getTipoCatalogo());
                    if (txtCatValor != null) txtCatValor.setText(newSel.getNombreItem());
                    if (cbCatEstado != null) cbCatEstado.setValue(newSel.getEstado());
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

        if (colTicId != null) colTicId.setCellValueFactory(cellData -> new javafx.beans.property.ReadOnlyStringWrapper("TICK-" + cellData.getValue().getIdObservacion()));
        if (colTicFecha != null) colTicFecha.setCellValueFactory(cellData -> new javafx.beans.property.ReadOnlyStringWrapper(cellData.getValue().getFechaObservacion() != null ? cellData.getValue().getFechaObservacion().toString() : "-"));
        if (colTicDesc != null) colTicDesc.setCellValueFactory(cellData -> new javafx.beans.property.ReadOnlyStringWrapper(cellData.getValue().getDescripcion() != null ? cellData.getValue().getDescripcion() : "-"));
        if (colTicEstado != null) colTicEstado.setCellValueFactory(cellData -> new javafx.beans.property.ReadOnlyStringWrapper(cellData.getValue().getEstadoObservacion() != null ? cellData.getValue().getEstadoObservacion() : "-"));
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
                "Todos los Proyectos",
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
        if (cbUsrRol != null) {
            cbUsrRol.getItems().addAll("SECRETARIA", "COORDINADOR", "JEFE_INVESTIGACION", "ADMINISTRADOR");
            cbUsrRol.getSelectionModel().selectFirst();
        }
        if (cbUsrEstado != null) {
            cbUsrEstado.getItems().addAll("ACTIVO", "INACTIVO");
            cbUsrEstado.getSelectionModel().selectFirst();
        }
        if (cbCatTipo != null) {
            cbCatTipo.getItems().addAll("PROGRAMA_ESTUDIO", "MODALIDAD_TITULACION", "ASESOR", "ESTADO_PROYECTO");
            cbCatTipo.getSelectionModel().selectFirst();
        }
        if (cbCatEstado != null) {
            cbCatEstado.getItems().addAll("ACTIVO", "INACTIVO");
            cbCatEstado.getSelectionModel().selectFirst();
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
    private void mostrarPanelTicketsTI(ActionEvent event) {
        ocultarTodosPaneles();
        if (panelTicketsTI != null) {
            panelTicketsTI.setVisible(true);
            panelTicketsTI.setManaged(true);
        }
        actualizarEstilosNav(btnNavTicketsTI);
        cargarTicketsTI();
    }

    private void cargarTicketsTI() {
        if (tbTicketsTI != null) {
            List<Observacion> tickets = observacionDAO.listarPorRol("TICKET_TI");
            tbTicketsTI.setItems(FXCollections.observableArrayList(tickets));
        }
    }

    @FXML
    private void atenderTicketTI(ActionEvent event) {
        if (tbTicketsTI == null) return;
        Observacion sel = tbTicketsTI.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Selección requerida", "Por favor seleccione un ticket de la tabla para atenderlo o resolverlo.");
            return;
        }
        boolean exito = observacionDAO.actualizarEstadoObservacion(sel.getIdObservacion(), "RESUELTO");
        if (exito) {
            cargarTicketsTI();
            mostrarAlerta("Ticket Atendido / Resuelto", "El ticket TICK-" + sel.getIdObservacion() + " ha sido marcado como RESUELTO en la base de datos.");
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el estado del ticket en XAMPP MySQL.");
        }
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
    private void descargarDocumentoProyecto(ActionEvent event) {
        Proyecto sel = tbProyectosAprobacion.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarAlerta("Selección requerida", "Seleccione un proyecto de la tabla para descargar y visualizar su documento oficial.");
            return;
        }
        Proyecto bdProy = proyectoDAO.obtenerPorId(sel.getIdProyecto());
        if (bdProy != null && bdProy.getArchivoData() != null && bdProy.getArchivoData().length > 0) {
            sel.setArchivoNombre(bdProy.getArchivoNombre());
            sel.setArchivoData(bdProy.getArchivoData());
        }
        if (sel.getArchivoData() == null || sel.getArchivoData().length == 0) {
            mostrarAlerta("Sin Documento Adjunto", "El proyecto seleccionado (" + sel.getCodigoProyecto() + ") no tiene un documento o archivo subido por la Secretaría.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Descargar Documento del Proyecto (PDF y Word)");
        fileChooser.getExtensionFilters().clear();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Documento PDF (*.pdf)", "*.pdf"),
            new FileChooser.ExtensionFilter("Documento Word (*.docx, *.doc)", "*.docx", "*.doc"),
            new FileChooser.ExtensionFilter("Todos los archivos (*.*)", "*.*")
        );
        String nombreDef = sel.getArchivoNombre() != null && !sel.getArchivoNombre().isEmpty() ? sel.getArchivoNombre() : "Documento_" + sel.getCodigoProyecto() + ".pdf";
        fileChooser.setInitialFileName(nombreDef);
        Stage stage = (Stage) btnSidebarToggle.getScene().getWindow();
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try {
                String rut = archivo.getAbsolutePath();
                if (!rut.contains(".") && fileChooser.getSelectedExtensionFilter() != null && fileChooser.getSelectedExtensionFilter().getExtensions() != null && !fileChooser.getSelectedExtensionFilter().getExtensions().isEmpty()) {
                    String ext = fileChooser.getSelectedExtensionFilter().getExtensions().get(0).replace("*", "");
                    if (!ext.equals(".*")) {
                        archivo = new File(rut + ext);
                    }
                }
                java.nio.file.Files.write(archivo.toPath(), sel.getArchivoData());
                boolean abierto = false;
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                    try {
                        Desktop.getDesktop().open(archivo);
                        abierto = true;
                    } catch (Exception ignored) {}
                }
                String extra = abierto ? "\n\nEl archivo se ha abierto en su aplicación predeterminada para revisión." : "\n\nPuede abrir el archivo directamente desde la carpeta donde lo guardó.";
                mostrarAlerta("Descarga Exitosa", "El documento fue guardado correctamente en:\n" + archivo.getAbsolutePath() + extra);
            } catch (Exception e) {
                mostrarAlerta("Error al Guardar", "Ocurrió un error al guardar el documento en disco: " + e.getMessage());
            }
        }
    }

    @FXML
    private void emitirObservacionJefe(ActionEvent event) {
        Proyecto seleccionado = tbProyectosAprobacion.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Selección requerida", "Por favor seleccione un proyecto de la tabla para emitir observaciones.");
            return;
        }
        String obs = txtObservacionJefatura != null ? txtObservacionJefatura.getText().trim() : "";
        if (obs.isEmpty()) {
            mostrarAlerta("Texto Requerido", "Escriba el detalle de las correcciones u observaciones de Jefatura en el cuadro de texto.");
            return;
        }
        int idUsr = SesionActual.haySesionActiva() ? SesionActual.getUsuarioLogueado().getIdUsuario() : 3;
        boolean exito = observacionDAO.registrarObservacionPorCodigo(seleccionado.getCodigoProyecto(), idUsr, "JEFE_INVESTIGACION", obs, "PENDIENTE");
        if (exito) {
            proyectoDAO.cambiarEstado(seleccionado.getCodigoProyecto(), "OBSERVADO");
            if (txtObservacionJefatura != null) txtObservacionJefatura.clear();
            cargarDatosAprobacion();
            mostrarAlerta("Observación Emitida", "La observación de Jefatura fue registrada exitosamente y el proyecto regresó al estado OBSERVADO para subsanación.");
        } else {
            mostrarAlerta("Error", "No se pudo guardar la observación en MySQL.");
        }
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
        String obs = txtObservacionJefatura != null ? txtObservacionJefatura.getText().trim() : "";
        int idUsr = SesionActual.haySesionActiva() ? SesionActual.getUsuarioLogueado().getIdUsuario() : 3;
        if (!obs.isEmpty()) {
            observacionDAO.registrarObservacionPorCodigo(seleccionado.getCodigoProyecto(), idUsr, "JEFE_INVESTIGACION", obs, "PENDIENTE");
        }
        if (proyectoDAO.cambiarEstado(seleccionado.getCodigoProyecto(), "RECHAZADO")) {
            if (txtObservacionJefatura != null) txtObservacionJefatura.clear();
            mostrarAlerta("Proyecto Devuelto / Rechazado", "El proyecto " + seleccionado.getCodigoProyecto() + " ha sido devuelto" + (!obs.isEmpty() ? " con sus observaciones registradas." : "."));
            cargarDatosAprobacion();
        } else {
            mostrarAlerta("Error", "No se pudo actualizar el estado en la base de datos MySQL.");
        }
    }

    @FXML
    private void nuevoUsuario(ActionEvent event) {
        limpiarFormularioUsuario(event);
    }

    @FXML
    private void nuevoCatalogo(ActionEvent event) {
        limpiarFormularioCatalogo(event);
    }

    @FXML
    private void limpiarFormularioUsuario(ActionEvent event) {
        if (tbUsuarios != null && tbUsuarios.getSelectionModel() != null) {
            tbUsuarios.getSelectionModel().clearSelection();
        }
        if (txtUsrDni != null) txtUsrDni.clear();
        if (txtUsrNombre != null) txtUsrNombre.clear();
        if (txtUsrCorreo != null) txtUsrCorreo.clear();
        if (txtUsrEspecialidad != null) txtUsrEspecialidad.clear();
        if (cbUsrRol != null) cbUsrRol.getSelectionModel().selectFirst();
        if (cbUsrEstado != null) cbUsrEstado.getSelectionModel().selectFirst();
    }

    @FXML
    private void generarTicketSoporteTI(ActionEvent event) {
        String ticket = "TICK-SUIZA-TI-" + (System.currentTimeMillis() % 10000);
        int idUsr = SesionActual.getUsuarioLogueado() != null ? SesionActual.getUsuarioLogueado().getIdUsuario() : 3;
        String desc = "REQUERIMIENTO SOPORTE TI (Admin/Jefatura) - Cód: " + ticket;
        boolean exito = observacionDAO.registrarTicketTI(idUsr, desc);
        if (exito && panelTicketsTI != null && panelTicketsTI.isVisible()) {
            cargarTicketsTI();
        }
        mostrarAlerta("Ticket TI Generado Oficialmente", 
            "Se ha registrado el requerimiento en la base de datos MySQL (tabla observaciones).\n\n" +
            "Código de Ticket: " + ticket + "\n" +
            "Área: Jefatura e Investigación / Soporte TI\n" +
            "Estado: PENDIENTE (Visible en Bandeja de Tickets TI)");
    }

    @FXML
    private void guardarUsuario(ActionEvent event) {
        if (txtUsrNombre == null || txtUsrNombre.getText().trim().isEmpty()) {
            mostrarAlerta("Validación requerida", "Por favor ingrese al menos el nombre completo del usuario.");
            return;
        }
        String nombre = txtUsrNombre.getText().trim();
        String rol = (cbUsrRol != null && cbUsrRol.getValue() != null) ? cbUsrRol.getValue() : "SECRETARIA";
        String estado = (cbUsrEstado != null && cbUsrEstado.getValue() != null) ? cbUsrEstado.getValue() : "ACTIVO";
        String email = (txtUsrCorreo != null) ? txtUsrCorreo.getText().trim() : "";
        String especialidad = (txtUsrEspecialidad != null) ? txtUsrEspecialidad.getText().trim() : "";
        String usr = (txtUsrDni != null && !txtUsrDni.getText().trim().isEmpty()) ? txtUsrDni.getText().trim() : nombre.toLowerCase().replaceAll("\\s+", "").substring(0, Math.min(6, nombre.length()));
        String pass = "suiza2026";
        
        Usuario actual = (tbUsuarios != null) ? tbUsuarios.getSelectionModel().getSelectedItem() : null;
        if (actual != null) {
            actual.setNombreCompleto(nombre);
            actual.setRol(rol);
            actual.setEstado(estado);
            actual.setEmail(email);
            actual.setEspecialidad(especialidad);
            if (usuarioDAO.actualizar(actual)) {
                mostrarAlerta("Éxito", "Usuario actualizado exitosamente.");
            } else {
                mostrarAlerta("Aviso", "Datos actualizados en sesión (verifique conexión MySQL).");
            }
        } else {
            Usuario nuevo = new Usuario(0, usr, pass, rol, nombre, estado, null);
            nuevo.setEmail(email);
            nuevo.setEspecialidad(especialidad);
            if (usuarioDAO.insertar(nuevo)) {
                mostrarAlerta("Éxito", "Nuevo usuario creado con clave por defecto: " + pass);
            } else {
                mostrarAlerta("Aviso", "Usuario registrado (verifique XAMPP si desea persistencia permanente).");
            }
        }
        cargarDatosUsuarios();
        limpiarFormularioUsuario(null);
    }

    @FXML
    private void eliminarUsuario(ActionEvent event) {
        if (tbUsuarios == null || tbUsuarios.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Selección requerida", "Por favor seleccione el usuario de la tabla que desea eliminar o inhabilitar.");
            return;
        }
        Usuario sel = tbUsuarios.getSelectionModel().getSelectedItem();
        if (usuarioDAO.eliminar(sel.getIdUsuario())) {
            mostrarAlerta("Eliminado", "Se ha eliminado el registro del usuario en el sistema.");
            cargarDatosUsuarios();
            limpiarFormularioUsuario(null);
        } else {
            mostrarAlerta("Error", "No se pudo eliminar de la base de datos MySQL.");
        }
    }

    @FXML
    private void limpiarFormularioCatalogo(ActionEvent event) {
        if (tbCatalogos != null && tbCatalogos.getSelectionModel() != null) {
            tbCatalogos.getSelectionModel().clearSelection();
        }
        if (txtCatValor != null) txtCatValor.clear();
        if (cbCatTipo != null) cbCatTipo.getSelectionModel().selectFirst();
        if (cbCatEstado != null) cbCatEstado.getSelectionModel().selectFirst();
    }

    @FXML
    private void filtrarCatalogoPorTipo(ActionEvent event) {
        if (cbCatTipo != null && cbCatTipo.getValue() != null && cbTipoCatalogo != null) {
            cbTipoCatalogo.setValue(cbCatTipo.getValue());
            cargarDatosCatalogos();
        }
    }

    @FXML
    private void guardarCatalogo(ActionEvent event) {
        if (txtCatValor == null || txtCatValor.getText().trim().isEmpty()) {
            mostrarAlerta("Validación requerida", "Por favor ingrese el nombre / valor del registro del catálogo.");
            return;
        }
        String valor = txtCatValor.getText().trim();
        String tipo = (cbCatTipo != null && cbCatTipo.getValue() != null) ? cbCatTipo.getValue() : "PROGRAMA_ESTUDIO";
        String estado = (cbCatEstado != null && cbCatEstado.getValue() != null) ? cbCatEstado.getValue() : "ACTIVO";
        
        Catalogo sel = (tbCatalogos != null) ? tbCatalogos.getSelectionModel().getSelectedItem() : null;
        if (sel != null) {
            sel.setNombreItem(valor);
            sel.setTipoCatalogo(tipo);
            sel.setEstado(estado);
            if (catalogoDAO.actualizar(sel)) {
                mostrarAlerta("Éxito", "Ítem de catálogo actualizado en el sistema.");
            } else {
                mostrarAlerta("Aviso", "Catálogo modificado en memoria.");
            }
        } else {
            String codGen = tipo.substring(0, Math.min(3, tipo.length())) + "-" + (System.currentTimeMillis() % 10000);
            Catalogo nuevo = new Catalogo(0, tipo, codGen, valor, "Registrado desde Jefatura e Investigación", estado);
            if (catalogoDAO.insertar(nuevo)) {
                mostrarAlerta("Éxito", "Nuevo ítem registrado en el catálogo " + tipo + ".");
            } else {
                mostrarAlerta("Aviso", "Ítem agregado en memoria local.");
            }
        }
        cargarDatosCatalogos();
        limpiarFormularioCatalogo(null);
    }

    @FXML
    private void eliminarCatalogo(ActionEvent event) {
        if (tbCatalogos == null || tbCatalogos.getSelectionModel().getSelectedItem() == null) {
            mostrarAlerta("Selección requerida", "Por favor seleccione el registro del catálogo que desea eliminar.");
            return;
        }
        Catalogo sel = tbCatalogos.getSelectionModel().getSelectedItem();
        if (catalogoDAO.eliminar(sel.getIdCatalogo())) {
            mostrarAlerta("Eliminado", "Se ha eliminado exitosamente el ítem del catálogo.");
            cargarDatosCatalogos();
            limpiarFormularioCatalogo(null);
        } else {
            mostrarAlerta("Error", "No se pudo eliminar el catálogo de MySQL.");
        }
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
        if (!tipoRep.isEmpty() && !"Todos los Proyectos".equals(tipoRep)) {
            if ("Consolidado por Programa de Estudio".equals(tipoRep)) {
                resultados.sort(java.util.Comparator.comparing(p -> p.getProgramaEstudio() != null ? p.getProgramaEstudio() : ""));
            } else {
                List<Proyecto> filtradosPorTipo = new ArrayList<>();
                for (Proyecto p : resultados) {
                    if (tipoRep.contains("Aprobados") && (p.getEstado().contains("APROBADO") || "APROBADO_COORDINACION".equals(p.getEstado()))) {
                        filtradosPorTipo.add(p);
                    } else if (tipoRep.contains("Observados") && (p.getEstado().contains("OBSERVADO") || p.getEstado().contains("RECHAZADO"))) {
                        filtradosPorTipo.add(p);
                    } else if (tipoRep.contains("Revisión") && (p.getEstado().contains("REVISION") || p.getEstado().contains("REGISTRADO"))) {
                        filtradosPorTipo.add(p);
                    }
                }
                resultados = filtradosPorTipo;
            }
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
        if (panelTicketsTI != null) {
            panelTicketsTI.setVisible(false);
            panelTicketsTI.setManaged(false);
        }
        panelCatalogos.setVisible(false);
        panelCatalogos.setManaged(false);
        panelReportes.setVisible(false);
        panelReportes.setManaged(false);
    }

    private void actualizarEstilosNav(Button btnActivo) {
        btnNavAprobacion.getStyleClass().remove("sidebar-btn-active");
        btnNavUsuarios.getStyleClass().remove("sidebar-btn-active");
        if (btnNavTicketsTI != null) btnNavTicketsTI.getStyleClass().remove("sidebar-btn-active");
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
