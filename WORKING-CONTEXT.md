# IESTP SUIZA - SISTEMA DE REGISTRO DE PROYECTOS DE TITULACIÓN
## WORKING-CONTEXT & ROUTER DE MEMORIA (SECOND BRAIN & HARNESS)

---

### 🚨 ENRUTADOR PRINCIPAL DE MEMORIA PARA AGENTES (SECOND BRAIN)
Este espacio de trabajo está estructurado bajo una arquitectura de **Arnés de Control (Harness)** y **Segundo Cerebro (Second Brain)** para garantizar un rendimiento óptimo, autónomo y libre de alucinaciones.

Todo agente de IA que opere en este workspace debe consultar y obedecer estrictamente los siguientes archivos clave:
1. **[Cerebro Central (.agents/AGENTS.md)](file:///C:/Users/Geric/Desktop/Gest_Titulacion/.agents/AGENTS.md):** Mapa completo del sistema, arquitectura técnico-visual, activos institucionales (`Logo-suiza.png`) y árbol modular por carpetas (`login`, `recuperar`, `secretaria`, `coordinador`, `jefe`, `dao`, `modelo`, `utilidades`).
2. **[Protocolos del Arnés (.harness/ECC-PROTOCOLS.md)](file:///C:/Users/Geric/Desktop/Gest_Titulacion/.harness/ECC-PROTOCOLS.md):** Reglas duras de ingeniería (Patrón DAO, Singleton MySQL, prohibición de emojis, pantallas maximizadas, filtro de 1 mes a 5 años y límite de 5 estudiantes por proyecto).
3. **[Bucle de Validación (.harness/VERIFICATION-LOOP.md)](file:///C:/Users/Geric/Desktop/Gest_Titulacion/.harness/VERIFICATION-LOOP.md):** Checklist de pruebas para auditar conectividad MySQL, nulidad de variables, resoluciones JavaFX y precisión del módulo de reportes.

---

### 📌 INFORMACIÓN GENERAL DEL PROYECTO
- **Institución:** Instituto de Educación Superior Tecnológico Público (IESTP) Suiza.
- **Carrera:** Desarrollo de Sistemas de Información.
- **Docente Evaluador:** Ruber Torres Arevalo.
- **Estructura de Carpetas en `src/pe/edu/suiza/`:** Exactamente idéntica y modular como en `Gestion_De_Pacientes` (`login`, `recuperar`, `secretaria`, `coordinador`, `jefe`, `dao`, `modelo`, `utilidades`).

---

### 🛠️ ESTADO DEL DESARROLLO (100% COMPLETADO Y CONECTADO A MYSQL)
- **Front-End y Login:**
  * Migración 1:1 de `code.html` a `login/login.fxml` y `login/login.css` con ojo de contraseña y validación contra MySQL XAMPP.
  * Módulo independiente de recuperación por Ticket TI (`recuperar/`).
- **Base de Datos y Capa DAO Completa:**
  * Script SQL sembrado en `Gestion_Titulacion/sql/01_schema_y_datos_suiza.sql`.
  * DAOs implementados en `pe.edu.suiza.dao`: `ConexionDB`, `UsuarioDAO`, `ProyectoDAO`, `EstudianteDAO`, `CatalogoDAO` y `ObservacionDAO`.
- **Los 3 Roles Oficiales Operativos en Vivo:**
  1. **Jefe de Investigación (`jefe/`):** Aprobación final para titulación, gestión de usuarios, catálogos y reportes con filtro de 1 mes a 5 años.
  2. **Coordinador Académico (`coordinador/`):** Evaluación metodológica, registro de observaciones y cambio de estado.
  3. **Secretaria Académica (`secretaria/`):** Registro de proyectos con tope innegociable de **máximo 5 estudiantes** guardados en XAMPP MySQL.
- **Optimizaciones Finales de Interfaz, Cero Emojis y Flujo de Datos (Julio 2026):**
  * **Regla 5 (Cero Emojis en FXML/Java):** Eliminados todos los emojis que causaban problemas de renderizado o fallos visuales en JavaFX. Se implementaron códigos Unicode de FontAwesome 6 (`&#xf007;`, `&#xf0c9;`, `&#xf023;`) con carga dual de fuente (`fa-solid-900.ttf`) en los inicializadores, y texto en español limpio en todos los controladores y FXMLs (`login`, `recuperar`, `secretaria`, `coordinador`, `jefe`).
  * **Búsqueda en Tiempo Real y Flujo de Datos:** Añadido filtrado dinámico (`buscarPorTexto`) en tablas de Proyectos de Secretaría, Coordinación y Jefatura, además de vincular el historial de observaciones (`ObservacionDAO.listarPorCodigoProyecto`) al seleccionar proyectos.
  * **Exportación Inteligente de Observaciones (Excel & PDF/HTML):** Se clarificaron y agregaron botones específicos tanto en **Secretaría** (`SecretariaController.java`) como en **Coordinación** (`CoordinadorController.java`) para que la descarga de observaciones en Excel (`.CSV` en UTF-8 con BOM para tildes y eñes) o en documento oficial (`.HTML/.PDF`) sea exacta para el proyecto seleccionado o el historial general, evitando cruces con la exportación del listado general de proyectos.
  * **Tipografía Universal:** Todos los CSS utilizan `"Segoe UI", "Tahoma", "Verdana", sans-serif` para garantizar escalado compacto en Windows.
  * **Codificación JDBC & MySQL:** `ConexionDB.java` incluye explícitamente `&characterEncoding=UTF-8&useUnicode=true` con auto-reconexión y validación activa (`isValid(2)`), operando en `UTF8MB4` puro para correcta persistencia y visualización de tildes y eñes.
  * **Unificación del Punto de Entrada (*App.java):** Todas las clases ejecutables (`LoginApp`, `SecretariaApp`, `CoordinadorApp`, `JefeApp`, `RecuperarApp`) redirigen unificadamente a `/pe/edu/suiza/login/login.fxml`. De esta manera, sin importar qué archivo `.java` o clase principal ejecute el desarrollador en NetBeans, el sistema siempre inicia por el acceso autenticado (`Login`), evitando by-pass de seguridad o errores de sesión nula (`SesionActual == null`).
  * **Acoplamiento y Maximización Adaptativa de Ventanas:** Corregido el problema donde `setMaximized(true)` no surtía efecto al provenir de un Login con `setResizable(false)`. Ahora, al ingresar a los módulos (`Secretaria`, `Coordinador`, `Jefe`), el controlador activa explícitamente `stage.setResizable(true); stage.setMaximized(false); stage.setMaximized(true); stage.centerOnScreen();`, asegurando que todas las páginas se abran en grande y se acoplen perfectamente a cualquier resolución de monitor. Asimismo, al cerrar sesión, las ventanas restauran limpiamente a las dimensiones exactas y centradas del Login (`660x720`).
  * **Un Solo Ejecutable Standalone (Fat-JAR & Launcher):** En `build.xml` se configuró el target `-post-jfx-jar` que empaqueta automáticamente las librerías (`mysql-connector-j` y `atlantafx`) dentro del ejecutable único `dist/Gestion_Titulacion_Ejecutable.jar`. Además se incluyó el iniciador nativo `INICIAR_SISTEMA_TITULACION.bat` para ejecución inmediata con doble clic en Windows sin romper el flujo de NetBeans.
