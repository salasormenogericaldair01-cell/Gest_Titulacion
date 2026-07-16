# PROTOCOLOS DEL ARNÉS DE INGENIERÍA (ENGINEERING CONTROL CENTER - ECC)
## SISTEMA DE REGISTRO DE PROYECTOS DE TITULACIÓN - IESTP SUIZA

Este documento define el **Arnés de Control (Harness)** que rige de manera obligatoria e innegociable el comportamiento de los agentes de IA al escribir, refactorizar o auditar código en este espacio de trabajo.

---

### 🛡️ REGLA 1: PROTOCOLO DE MEMORIA PERSISTENTE (LOCAL MEMORY PERSISTENCE)
- **Lectura Obligatoria al Inicio:** Al comenzar cualquier sesión o tarea, el agente DEBE leer `WORKING-CONTEXT.md` y `.agents/AGENTS.md` para sincronizar el estado técnico del sistema.
- **Actualización Sincrónica:** Si se descubre un bug crítico, se cambia la estructura de la base de datos o se toma una decisión arquitectónica, el agente debe actualizar inmediatamente el Cerebro Central (`.agents/AGENTS.md`) antes de finalizar su turno.

---

### 🛑 REGLA 2: PROTOCOLO ANTI-ASUNCIÓN Y RIGOR ABSOLUTO (ANTI-ASSUMPTION PROTOCOL)
- **JAMÁS ASUMIR LÓGICA:** No rellenar métodos con código genérico o aproximado.
- **JAMÁS DEJAR COMENTARIOS DE MARCADA:** Queda estrictamente prohibido escribir `// implement later`, `// TODO` o dejar métodos vacíos sin una implementación funcional o justificación explícita en el diseño temporal.
- **Honestidad Radical:** Si un requerimiento es ambiguo o falta una tabla en XAMPP, el agente debe detener la generación de código, reportar el vacío y consultar al usuario.

---

### 🎯 REGLA 3: DESARROLLO IMPULSADO POR INTENCIÓN (INTENT-DRIVEN DEVELOPMENT - IDD)
- **Resumen de Intención:** Antes de modificar un archivo Java o FXML, el agente debe declarar internamente o en su reporte qué problema va a solucionar y qué arquitectura aplicará.
- **Validación de Entradas/Salidas:** Verificar estrictamente qué tipos de datos entran desde los formularios JavaFX (`TextField`, `ComboBox`, `DatePicker`) y cómo se mapean a los tipos SQL de XAMPP MySQL (`VARCHAR`, `INT`, `DATE`, `ENUM`).

---

### 🏛️ REGLA 4: ESTÁNDARES JAVA / JAVAFX Y PATRÓN DAO
- **Separación Estricta DAO / UI:** Prohibido escribir sentencias SQL (`SELECT`, `INSERT`, `UPDATE`, `DELETE`) en las clases controladoras de FXML (`*Controller.java`). Toda comunicación con XAMPP MySQL se realiza a través de objetos DAO en `pe.edu.suiza.dao`.
- **Manejo Específico de Excepciones:** No capturar excepciones de forma genérica (`catch (Exception e)`) para silenciarlas. Capturar `SQLException`, `IOException` o `NullPointerException` e imprimir o registrar mensajes de diagnóstico claros.
- **Singleton `ConexionDB`:** Utilizar siempre la única instancia de conexión para evitar fugas de memoria o saturación de puertos en XAMPP.

---

### 🎨 REGLA 5: EXCELENCIA VISUAL INSTITUCIONAL (BRANDING SUIZA)
- **Cero Emojis:** Prohibición total de caracteres Unicode/emojis. Usar iconos FontAwesome (`&#xf19d;`, `&#xf007;`, `&#xf023;`, `&#xf0c9;`) o texto profesional en español.
- **Ventanas Maximizadas:** Toda transición con `FXMLLoader` debe aplicar `stage.setMaximized(true)`.
- **Tablas sin Columna Gris:** Aplicar `CONSTRAINED_RESIZE_POLICY` en todas las tablas JavaFX.
- **Uso de Logotipo:** Referenciar los archivos `Logo-suiza.png` / `.svg` / `.webp` del directorio raíz para encabezados y membretes oficiales.

---

### 📊 REGLA 6: REGLAS DE NEGOCIO ACADÉMICAS DEL DOCENTE (RUBER TORRES)
1. **Límite de Integrantes por Proyecto:** En el módulo de Secretaría Académica, el DAO de estudiantes debe validar que ningún proyecto exceda el límite estricto de **5 estudiantes**.
2. **Filtro Temporal de Reportes:** En el módulo del Jefe de Investigación, los reportes deben soportar obligatoriamente la consulta en un rango temporal estricto: **Mínimo desde hace 1 mes, y máximo hasta hace 5 años**.
3. **Recuperación de Clave Local:** El sistema de recuperación por olvido de clave no debe exigir correos electrónicos externos (SMTP); debe generar un **Ticket TI de Soporte institucional (`TICK-SUIZA-XXXX`)** persistido en MySQL y auditable desde el panel del Jefe de Investigación.
