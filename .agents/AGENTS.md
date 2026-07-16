# CEREBRO CENTRAL DE AGENTES (SECOND BRAIN - AGENTS.MD)
## PROYECTO: SISTEMA DE REGISTRO DE PROYECTOS DE TITULACIÓN - IESTP SUIZA
**Ubicación del Workspace:** `C:\Users\Geric\Desktop\Gestion de Titulacion`  
**Estado:** Activo y Sincronizado | **Última actualización:** Julio 2026

---

## 🧠 1. PROPÓSITO DEL SEGUNDO CEREBRO (.agents/AGENTS.md)
Este archivo es la **fuente de verdad indiscutible** para cualquier agente de Inteligencia Artificial (Agente Principal o Subagentes) que opere en este espacio de trabajo. Al iniciar cualquier tarea, el agente tiene la obligación absoluta de consultar este documento para asimilar la arquitectura, el estado de desarrollo, los estándares visuales y los protocolos del sistema antes de proponer o modificar código.

---

## 🏗️ 2. ESTADO GENERAL DEL SISTEMA Y MAPA DE ARCHIVOS (MODULAR POR CARPETAS)
El proyecto se desarrolla en **Java 8 (NetBeans Project)** con **JavaFX (FXML + CSS)** y base de datos **MySQL (XAMPP)** en la carpeta `Gestion_Titulacion/`. La estructura de paquetes en `src/pe/edu/suiza/` está organizada modularmente por carpetas:

### ESTRUCTURA DE PAQUETES VERIFICADA EN `src/pe/edu/suiza/`:
- **`login/`**: Contiene `login.css`, `login.fxml`, `LoginApp.java`, `LoginController.java` y `Logo-suiza.png`. Conectado a BD MySQL con salvavidas offline.
- **`recuperar/`**: Contiene `recuperar.css`, `recuperar.fxml`, `RecuperarApp.java`, `RecuperarController.java`. Genera el **Ticket TI (`TICK-SUIZA-XXXX`)** persistido en BD sin requerir SMTP externo.
- **`secretaria/`**: Contiene `secretaria.css`, `secretaria.fxml`, `SecretariaApp.java`, `SecretariaController.java` y subcarpeta `fonts/fa-solid-900.ttf`. Controla el registro de proyectos en MySQL y hace cumplir el límite innegociable de **máximo 5 estudiantes por proyecto**.
- **`coordinador/`**: Contiene `coordinador.css`, `coordinador.fxml`, `CoordinadorApp.java`, `CoordinadorController.java`. Gestiona revisión metodológica, cambio de estado e historial de observaciones en BD.
- **`jefe/`**: Contiene `jefe.css`, `jefe.fxml`, `JefeApp.java`, `JefeController.java`. Aprobación final, administración de usuarios/TI, catálogos y el **Módulo de Reportes con filtro de 1 mes a 5 años** conectado a MySQL.
- **`dao/`**: Contiene `ConexionDB.java` (Singleton JDBC a XAMPP MySQL `gestion_titulacion_suiza`), `UsuarioDAO.java`, `ProyectoDAO.java`, `EstudianteDAO.java`, `CatalogoDAO.java` y `ObservacionDAO.java`.
- **`modelo/`**: Contiene `Usuario.java`, `Proyecto.java`, `Estudiante.java`, `Catalogo.java` y `Observacion.java`.
- **`utilidades/`**: Contiene `SesionActual.java` y `AnimacionesUI.java`.

---

## 🎨 3. IDENTIDAD INSTITUCIONAL Y RECURSOS DE MARCA (LOGOS SUIZA)
En el directorio raíz y en la carpeta `login/` se dispone del activo oficial del instituto `Logo-suiza.png`. Todo reporte y pantalla de ingreso hace uso visual prominente de este emblema.

---

## ⚙️ 4. DIRECTRICES TÉCNICAS INNEGOCIABLES PARA AGENTES
1. **Prohibido el uso de Emojis en Código / UI:** Usar exclusivamente iconos de FontAwesome (`&#xf19d;`, `&#xf007;`, `&#xf023;`, `&#xf0c9;`) o texto en español limpio.
2. **Pantallas Maximizadas:** Toda navegación entre escenas debe invocar `stage.setMaximized(true)`.
3. **Tablas sin Columna Vacía:** Uso obligatorio de `<columnResizePolicy><TableView fx:constant="CONSTRAINED_RESIZE_POLICY"/></columnResizePolicy>`.
4. **Patrón DAO Estricto:** Toda consulta o alteración en XAMPP MySQL se ejecuta exclusivamente desde el paquete `pe.edu.suiza.dao`.

---

## 🚀 5. ESTADO OPERATIVO FINAL (100% COMPLETADO)
Los tres roles del sistema (Jefatura, Coordinación y Secretaría) están completos, conectados a XAMPP MySQL y respetan estrictamente los lineamientos del documento del docente Ruber Torres.
