package pe.edu.suiza.utilidades;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ExportadorInstitucional {

    private static String logoBase64Cache = null;

    public static synchronized String getLogoBase64() {
        if (logoBase64Cache != null) {
            return logoBase64Cache;
        }
        try (InputStream is = ExportadorInstitucional.class.getResourceAsStream("/pe/edu/suiza/login/Logo-suiza.png")) {
            if (is != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[16384];
                while ((nRead = is.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                logoBase64Cache = "data:image/png;base64," + Base64.getEncoder().encodeToString(buffer.toByteArray());
                return logoBase64Cache;
            }
        } catch (Exception e) {
            System.err.println("[Exportador] No se pudo cargar Logo-suiza.png: " + e.getMessage());
        }
        return "";
    }

    public static void exportarTablaExcelInstitucional(Stage stage, String tituloReporte, String subTitulo, String[] cabeceras, List<String[]> filas, String nombreArchivoInicial) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte Oficial en Excel (" + tituloReporte + ")");
        fileChooser.setInitialFileName(nombreArchivoInicial + ".xls");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivo Excel con Diseño Institucional (*.xls)", "*.xls"),
            new FileChooser.ExtensionFilter("Archivo CSV Plano (*.csv)", "*.csv")
        );
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try {
                if (archivo.getName().toLowerCase().endsWith(".csv")) {
                    guardarComoCSV(archivo, cabeceras, filas);
                } else {
                    guardarComoExcelHTML(archivo, tituloReporte, subTitulo, cabeceras, filas);
                }
                javax.swing.JOptionPane.showMessageDialog(null, "¡Exportación Institucional Exitosa!\nArchivo guardado en:\n" + archivo.getAbsolutePath(), "Exportación Completa", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Error al exportar: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void exportarDocumentoPdfHtmlInstitucional(Stage stage, String tituloReporte, String subTitulo, String modulo, String[] cabeceras, List<String[]> filas, String nombreArchivoInicial) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Documento Oficial para Imprimir/PDF (" + tituloReporte + ")");
        fileChooser.setInitialFileName(nombreArchivoInicial + ".html");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Documento Oficial Imprimible / Guardar como PDF (*.html)", "*.html")
        );
        File archivo = fileChooser.showSaveDialog(stage);
        if (archivo != null) {
            try {
                guardarComoPdfHtml(archivo, tituloReporte, subTitulo, modulo, cabeceras, filas);
                javax.swing.JOptionPane.showMessageDialog(null, "¡Documento Oficial Generado!\nSe abrirá en su navegador web. Para guardar en PDF presione Ctrl + P -> 'Guardar como PDF'.\n\nRuta: " + archivo.getAbsolutePath(), "Documento Oficial", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(archivo);
                }
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Error al generar documento: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void guardarComoExcelHTML(File archivo, String titulo, String subTitulo, String[] cabeceras, List<String[]> filas) throws Exception {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            pw.write('\ufeff'); // BOM para Excel UTF-8
            pw.println("<html xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:x=\"urn:schemas-microsoft-com:office:excel\" xmlns=\"http://www.w3.org/TR/REC-html40\">");
            pw.println("<head><meta charset=\"UTF-8\"><style>");
            pw.println("table { border-collapse: collapse; width: 100%; font-family: 'Segoe UI', Tahoma, Arial, sans-serif; }");
            pw.println("th { background-color: #1e3a8a; color: #ffffff; font-weight: bold; border: 1px solid #94a3b8; padding: 10px; font-size: 11pt; text-align: left; }");
            pw.println("td { border: 1px solid #cbd5e1; padding: 8px; font-size: 10pt; vertical-align: middle; mso-number-format: \"\\@\"; }");
            pw.println(".row-even { background-color: #f8fafc; }");
            pw.println(".row-odd { background-color: #ffffff; }");
            pw.println(".header-title { background-color: #1e3a8a; color: white; font-size: 16pt; font-weight: bold; padding: 12px; text-align: center; }");
            pw.println(".header-sub { background-color: #3b82f6; color: white; font-size: 12pt; font-weight: bold; padding: 8px; text-align: center; }");
            pw.println(".accent-line { background-color: #dc2626; height: 4px; }");
            pw.println("</style></head><body>");
            pw.println("<table>");
            pw.println("<tr><td colspan=\"" + cabeceras.length + "\" class=\"header-title\">INSTITUTO DE EDUCACIÓN SUPERIOR TECNOLÓGICO PÚBLICO SUIZA - PUCALLPA</td></tr>");
            pw.println("<tr><td colspan=\"" + cabeceras.length + "\" class=\"header-sub\">" + titulo.toUpperCase() + " | " + subTitulo + "</td></tr>");
            pw.println("<tr><td colspan=\"" + cabeceras.length + "\" class=\"accent-line\"></td></tr>");
            pw.println("<tr><td colspan=\"" + cabeceras.length + "\" style=\"background-color:#eff6ff; font-style:italic; padding:6px;\"><b>Fecha de Exportación:</b> " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "</td></tr>");
            pw.println("<tr><td colspan=\"" + cabeceras.length + "\"></td></tr>");
            pw.println("<tr>");
            for (String cab : cabeceras) {
                pw.println("<th>" + (cab != null ? cab : "") + "</th>");
            }
            pw.println("</tr>");
            boolean esPar = false;
            for (String[] fila : filas) {
                pw.println("<tr class=\"" + (esPar ? "row-even" : "row-odd") + "\">");
                for (String celda : fila) {
                    pw.println("<td>" + (celda != null ? celda : "") + "</td>");
                }
                pw.println("</tr>");
                esPar = !esPar;
            }
            pw.println("</table></body></html>");
        }
    }

    private static void guardarComoCSV(File archivo, String[] cabeceras, List<String[]> filas) throws Exception {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            pw.write('\ufeff');
            pw.println(String.join(";", cabeceras));
            for (String[] fila : filas) {
                for (int i = 0; i < fila.length; i++) {
                    String val = fila[i] != null ? fila[i].replace(";", ",") : "";
                    if (i > 0) pw.print(";");
                    pw.print(val);
                }
                pw.println();
            }
        }
    }

    private static void guardarComoPdfHtml(File archivo, String titulo, String subTitulo, String modulo, String[] cabeceras, List<String[]> filas) throws Exception {
        String logo = getLogoBase64();
        String fechaStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss"));
        String usr = pe.edu.suiza.utilidades.SesionActual.getUsuarioLogueado() != null ? pe.edu.suiza.utilidades.SesionActual.getUsuarioLogueado().getNombreCompleto() : "Funcionario Autorizado";
        String rol = pe.edu.suiza.utilidades.SesionActual.getUsuarioLogueado() != null ? pe.edu.suiza.utilidades.SesionActual.getUsuarioLogueado().getRol() : modulo;

        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(archivo), StandardCharsets.UTF_8))) {
            pw.write('\ufeff');
            pw.println("<!DOCTYPE html><html lang=\"es\"><head><meta charset=\"UTF-8\">");
            pw.println("<title>" + titulo + " - IESTP Suiza</title>");
            pw.println("<style>");
            pw.println("@page { size: A4 landscape; margin: 15mm; }");
            pw.println("body { font-family: 'Segoe UI', Tahoma, Arial, sans-serif; color: #1e293b; margin: 0; padding: 20px; background: #ffffff; }");
            pw.println(".header-container { display: flex; align-items: center; justify-content: space-between; border-bottom: 4px solid #1e3a8a; padding-bottom: 15px; margin-bottom: 5px; }");
            pw.println(".logo-box { display: flex; align-items: center; }");
            pw.println(".logo-img { height: 75px; margin-right: 20px; border-radius: 4px; }");
            pw.println(".institution-text { display: flex; flex-direction: column; }");
            pw.println(".institution-title { font-size: 18pt; font-weight: bold; color: #1e3a8a; letter-spacing: 0.5px; margin: 0; }");
            pw.println(".institution-sub { font-size: 11pt; color: #475569; font-weight: 600; margin-top: 3px; }");
            pw.println(".badge-doc { background: #1e3a8a; color: white; padding: 8px 16px; border-radius: 6px; font-weight: bold; font-size: 11pt; border-bottom: 3px solid #dc2626; text-align: right; }");
            pw.println(".accent-bar { height: 4px; background: #dc2626; margin-bottom: 20px; }");
            pw.println(".meta-card { background: #f8fafc; border: 1px solid #e2e8f0; border-left: 5px solid #3b82f6; padding: 12px 18px; border-radius: 6px; margin-bottom: 25px; display: flex; justify-content: space-between; font-size: 10pt; }");
            pw.println("table { width: 100%; border-collapse: collapse; margin-bottom: 40px; box-shadow: 0 1px 3px rgba(0,0,0,0.05); }");
            pw.println("th { background-color: #1e3a8a; color: white; padding: 12px 10px; font-size: 10pt; text-transform: uppercase; letter-spacing: 0.5px; border: 1px solid #1e3a8a; text-align: left; }");
            pw.println("td { padding: 10px; font-size: 10pt; border: 1px solid #cbd5e1; vertical-align: top; color: #334155; }");
            pw.println("tr:nth-child(even) { background-color: #f8fafc; }");
            pw.println("tr:hover { background-color: #eff6ff; }");
            pw.println(".status-badge { display: inline-block; padding: 3px 8px; border-radius: 4px; font-weight: bold; font-size: 9pt; }");
            pw.println(".status-ok { background: #dcfce7; color: #166534; border: 1px solid #bbf7d0; }");
            pw.println(".status-obs { background: #fee2e2; color: #991b1b; border: 1px solid #fecaca; }");
            pw.println(".status-rev { background: #fef9c3; color: #854d0e; border: 1px solid #fde047; }");
            pw.println(".signature-area { margin-top: 60px; display: flex; justify-content: space-around; text-align: center; page-break-inside: avoid; }");
            pw.println(".signature-line { border-top: 2px solid #475569; width: 260px; padding-top: 8px; font-weight: bold; font-size: 10pt; color: #1e293b; }");
            pw.println(".signature-sub { font-size: 9pt; color: #64748b; margin-top: 2px; }");
            pw.println(".footer { margin-top: 40px; border-top: 1px solid #e2e8f0; padding-top: 12px; text-align: center; font-size: 8.5pt; color: #94a3b8; }");
            pw.println("</style></head><body>");

            pw.println("<div class=\"header-container\">");
            pw.println("  <div class=\"logo-box\">");
            if (!logo.isEmpty()) {
                pw.println("    <img src=\"" + logo + "\" class=\"logo-img\" alt=\"Logo IESTP Suiza\"/>");
            }
            pw.println("    <div class=\"institution-text\">");
            pw.println("      <div class=\"institution-title\">INSTITUTO DE EDUCACIÓN SUPERIOR TECNOLÓGICO PÚBLICO SUIZA</div>");
            pw.println("      <div class=\"institution-sub\">DIRECCIÓN GENERAL | COORDINACIÓN ACADÉMICA Y DE INVESTIGACIÓN - PUCALLPA</div>");
            pw.println("    </div>");
            pw.println("  </div>");
            pw.println("  <div class=\"badge-doc\">" + titulo.toUpperCase() + "<br/><span style=\"font-size:9pt; font-weight:normal;\">" + subTitulo + "</span></div>");
            pw.println("</div>");
            pw.println("<div class=\"accent-bar\"></div>");

            pw.println("<div class=\"meta-card\">");
            pw.println("  <div><b>Módulo Específico:</b> " + modulo + "<br/><b>Emitido por:</b> " + usr + " (" + rol + ")</div>");
            pw.println("  <div><b>Fecha y Hora de Emisión:</b> " + fechaStr + "<br/><b>Total Registros:</b> " + filas.size() + "</div>");
            pw.println("</div>");

            pw.println("<table>");
            pw.println("<thead><tr>");
            for (String cab : cabeceras) {
                pw.println("<th>" + (cab != null ? cab : "") + "</th>");
            }
            pw.println("</tr></thead><tbody>");

            for (String[] fila : filas) {
                pw.println("<tr>");
                for (String celda : fila) {
                    String texto = celda != null ? celda : "";
                    if (texto.equalsIgnoreCase("APROBADO_FINAL") || texto.equalsIgnoreCase("SUBSANADA") || texto.equalsIgnoreCase("APROBADO_COORDINACION") || texto.equalsIgnoreCase("SUBSANADO")) {
                        texto = "<span class=\"status-badge status-ok\">" + texto + "</span>";
                    } else if (texto.equalsIgnoreCase("OBSERVADO") || texto.equalsIgnoreCase("PENDIENTE") || texto.equalsIgnoreCase("RECHAZADO")) {
                        texto = "<span class=\"status-badge status-obs\">" + texto + "</span>";
                    } else if (texto.equalsIgnoreCase("EN_REVISION") || texto.equalsIgnoreCase("ACTIVO") || texto.equalsIgnoreCase("TESIS")) {
                        texto = "<span class=\"status-badge status-rev\">" + texto + "</span>";
                    }
                    pw.println("<td>" + texto + "</td>");
                }
                pw.println("</tr>");
            }
            pw.println("</tbody></table>");

            pw.println("<div class=\"signature-area\">");
            pw.println("  <div>");
            pw.println("    <div class=\"signature-line\">" + usr + "</div>");
            pw.println("    <div class=\"signature-sub\">Firma y Sello (" + rol + ")<br/>IESTP Suiza - Pucallpa</div>");
            pw.println("  </div>");
            pw.println("  <div>");
            pw.println("    <div class=\"signature-line\">V° B° Jefatura de Investigación</div>");
            pw.println("    <div class=\"signature-sub\">Firma y Sello Institucional<br/>IESTP Suiza - Pucallpa</div>");
            pw.println("  </div>");
            pw.println("</div>");

            pw.println("<div class=\"footer\">");
            pw.println("  Documento Oficial Generado por el Sistema Electrónico de Gestión y Registro de Proyectos de Titulación - IESTP Suiza.<br/>");
            pw.println("  Pucallpa, Ucayali - Perú | Para guardar en PDF desde su navegador presione Ctrl + P y seleccione 'Guardar como PDF'.");
            pw.println("</div>");

            pw.println("</body></html>");
        }
    }
}
