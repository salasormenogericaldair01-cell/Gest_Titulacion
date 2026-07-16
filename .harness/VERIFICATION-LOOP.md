# BUCLE DE VALIDACIÓN AUTOMÁTICO (VERIFICATION LOOP)
## PROTOCOLO DE AUDITORÍA TÉCNICA - IESTP SUIZA

Este documento establece el **checklist de verificación obligatoria** que todo agente debe ejecutar antes de presentar cualquier módulo o refactorización al usuario.

---

### 🔍 1. AUDITORÍA DE CONECTIVIDAD Y BASE DE DATOS (XAMPP / MYSQL)
- [ ] **Estado del Servidor XAMPP:** Verificar que XAMPP MySQL esté ejecutándose en el puerto `3306`.
- [ ] **Reconexión Automática:** Validar que `ConexionDB.getInstancia().getConexion()` maneje correctamente conexiones cerradas, reintentando la conexión sin arrojar crashes no controlados.
- [ ] **Integridad de Claves Foráneas:** Asegurar que toda inserción en `proyectos`, `estudiantes` u `observaciones` respete las restricciones FK de la tabla `catalogos` y `usuarios`.
- [ ] **Validación de Límites:** Probar el caso extremo donde se intenta registrar un sexto estudiante en un mismo proyecto (el sistema debe bloquearlo con una alerta amable).

---

### 🛡️ 2. AUDITORÍA DE INTERFAZ GRÁFICA (JAVAFX / FXML)
- [ ] **Cero Errores de FXML Loader:** Verificar que todos los `fx:id` declarados en las vistas `.fxml` coincidan exactamente (en tipo y nombre) con los campos anotados con `@FXML` en los controladores Java.
- [ ] **Maximizacón Dinámica:** Comprobar que en toda transición de escena se invoque `stage.setMaximized(true)`.
- [ ] **Resiliencia Visual sin Emojis:** Verificar que ningún botón o texto contenga caracteres de emojis que puedan causar mojibake o errores de renderizado en Windows/NetBeans.
- [ ] **Ajuste de Columnas en Tablas:** Auditar que todas las tablas tengan `<columnResizePolicy><TableView fx:constant="CONSTRAINED_RESIZE_POLICY"/></columnResizePolicy>`.

---

### 📊 3. AUDITORÍA DEL MÓDULO DE REPORTES (DOCENTE RUBER TORRES)
- [ ] **Filtro Temporal Estricto:** Verificar que al consultar reportes en el panel de Jefatura de Investigación, las consultas SQL (`WHERE fecha_registro BETWEEN ? AND ?`) funcionen con precisión matemática para los rangos de:
  * Hace 1 mes (Mínimo).
  * Hace 3 meses / 6 meses / 1 año / 3 años.
  * Hace 5 años (Máximo).
- [ ] **Membrete e Identidad:** Auditar que la exportación de reportes cargue correctamente la imagen institucional `Logo-suiza.png` del directorio raíz.

---

### 🚀 4. REGLA DE CIERRE DE TAREA
Ninguna tarea de código se da por concluida hasta haber verificado que los controladores, modelos y DAOs compilan limpiamente en NetBeans sin generar advertencias de tipos incompatibles o referencias nulas.
