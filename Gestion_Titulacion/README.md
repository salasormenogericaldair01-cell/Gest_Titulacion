# 📗 MANUAL PASO A PASO DEL SISTEMA DE TITULACIÓN - IESTP SUIZA
## GUÍA OFICIAL PARA EL USUARIO, DOCENTES Y JURADO EVALUADOR
**Institución:** Instituto de Educación Superior Tecnológico Público Suiza (IESTP Suiza - Pucallpa)  
**Versión:** 2.0 (Actualizada con Gestión Integral de Expedientes, 11 Programas de Estudio UTF-8 y Filtros Temporales)  
**Entorno Técnico:** Java 8 (NetBeans Project) + JavaFX 8 + XAMPP MySQL / MariaDB  

---

## 🌟 ¿DE QUÉ TRATA ESTE SISTEMA?
Este software ha sido diseñado y programado desde cero para **automatizar todo el proceso de registro, revisión metodológica, aprobación y titulación** de los estudiantes del IESTP Suiza.

Olvídate de llevar papeles perdidos o archivos sueltos. El sistema conecta **3 roles institucionales clave** en un flujo de trabajo transparente, rápido y seguro:
1. 🌸 **Secretaría Académica (`secretaria`):** Crea los expedientes, registra a los alumnos (máximo 5 por tesis), sube los documentos originales (PDF/Word) y gestiona las rectificaciones.
2. 🔬 **Coordinación Académica (`coordinador`):** Descarga la tesis con un clic (abriéndose en pantalla para lectura), evalúa la metodología y registra observaciones o da el pase al Jefe.
3. 👑 **Jefatura de Investigación y Dirección (`jefe`):** Revisa el expediente final, otorga la **Aprobación de Titulación**, administra los usuarios/TI, gestiona los catálogos institucionales y genera **reportes gerenciales con filtros de tiempo (1 Mes a 5 Años)** para auditoría.

---

## 🎓 CATÁLOGO OFICIAL DE LOS 11 PROGRAMAS DE ESTUDIO (CARRERAS)
El sistema integra y blinda en la base de datos MySQL (`gestion_titulacion_suiza.catalogos`) y en sus respaldos offline los **11 Programas de Estudio oficiales de IESTP Suiza** con acentuación UTF-8 impecable:
1. **ADMINISTRACIÓN DE OPERACIONES TURÍSTICAS** (`AOT`)
2. **ASISTENCIA ADMINISTRATIVA** (`AA`)
3. **CONTABILIDAD** (`CONT`)
4. **CONSTRUCCIÓN CIVIL** (`CC`)
5. **GESTIÓN ADMINISTRATIVA** (`GA`)
6. **DESARROLLO DE SISTEMAS DE INFORMACIÓN** (`DSI`)
7. **ELECTRICIDAD INDUSTRIAL** (`EI`)
8. **ENFERMERÍA TÉCNICA** (`ENF`)
9. **MANEJO FORESTAL** (`MF`)
10. **MECATRÓNICA AUTOMOTRIZ** (`MA`)
11. **PRODUCCIÓN AGROPECUARIA** (`PA`)

---

## 🟢 PASO 0: LO QUE DEBES HACER ANTES DE ABRIR EL PROGRAMA (ENCENDER XAMPP)
El software almacena todos los datos (estudiantes, proyectos, observaciones, y archivos PDF/Word de las tesis) en una base de datos local rápida y segura en tu propia computadora. Para que funcione, el motor MySQL debe estar activo:
1. Abre el panel de control llamado **`XAMPP Control Panel`** en tu escritorio o menú inicio de Windows.
2. Busca la fila que dice **`MySQL`** (la segunda fila, debajo de Apache).
3. Haz clic en el botón **`Start`** (al lado derecho de MySQL).
4. Espera 2 segundos hasta que la palabra **`MySQL`** se pinte con un fondo verde y veas los números de puerto (ej. `3306`).
5. **¡Listo!** Puedes minimizar la ventana de XAMPP. Tu base de datos ya está corriendo.

---

## 🚀 PASO 1: CÓMO ABRIR EL PROGRAMA (UN SOLO EJECUTABLE PARA NO ROMPER NADA)
Para que no tengas que buscar entre cientos de carpetas ni preocuparte por comandos complicados de programación, hemos preparado **2 formas súper fáciles** para iniciar el sistema:

### 👉 Opción A (La más fácil - Doble clic en Windows):
1. Entra a la carpeta raíz de tu proyecto en el escritorio: `C:\Users\Geric\Desktop\WorkSpace\Gest_Titulacion`
2. Verás un archivo llamado **`INICIAR_SISTEMA_TITULACION.bat`** (o entra a `Gestion_Titulacion\dist\Gestion_Titulacion_Ejecutable.jar`).
3. Haz **doble clic** sobre él.
4. El sistema compilará y abrirá inmediatamente la pantalla de **Login del Sistema de Titulación - IESTP Suiza**.

### 👉 Opción B (Desde el NetBeans IDE):
1. Abre tu **NetBeans IDE 8.2** (o superior compatible con Java 8).
2. Abre el proyecto `Gestion_Titulacion`.
3. Haz clic en el botón verde de **Play (`Run Project - F6`)** en la barra superior.
4. El programa abrirá la ventana de inicio de sesión de inmediato.

---

## 🔐 PASO 2: CÓMO INICIAR SESIÓN (CUENTAS Y CONTRASEÑAS EXACTAS)
La pantalla principal tiene el escudo oficial del instituto y 3 casillas: **Usuario**, **Contraseña** y el botón **Ingresar al Sistema (`&#xf007;`)**.

### 📋 TABLA DE CREDENCIALES INSTITUCIONALES PARA PRUEBAS:
| Rol en el Sistema | Usuario (`username`) | Contraseña (`password`) | ¿Para qué sirve o quién lo usa? |
| :--- | :--- | :--- | :--- |
| **Secretaría Académica** | `secretaria` | `secretaria123` | Para registrar proyectos, alumnos, adjuntar o rectificar archivos Word/PDF y buscar expedientes. |
| **Coordinación Académica** | `coordinador` | `coordinador123` | Para descargar tesis en pantalla, evaluar metodología, registrar correcciones y cambiar estados. |
| **Jefatura de Investigación** | `jefe` | `jefe123` | Para aprobación final de titulación, administrar usuarios, gestionar catálogos y emitir reportes temporales. |

### 🛠️ ¿Cómo usar los botones del Login?
1. **Ingresar (`&#xf007;`):** Verifica tus credenciales contra la base de datos MySQL. Si todo está correcto, abre tu módulo y **maximiza la pantalla automáticamente (`setMaximized(true)`)** para que trabajes con comodidad visual total.
2. **Limpiar (`&#xf12d;`):** Borra lo que escribiste en las casillas para empezar de nuevo.
3. **Salir (`&#xf08b;`):** Cierra el programa por completo si ya terminaste tu jornada en el instituto.

---

## 📁 PASO 3: QUÉ HACER DENTRO DE CADA ROL (GUÍA DETALLADA BOTÓN POR BOTÓN)

---

### 🌸 ROL 1: SECRETARÍA ACADÉMICA (`secretaria`)
Al entrar con la cuenta `secretaria`, verás a la izquierda un menú azul oscuro con tus datos y 2 pestañas principales: **`Nuevo Registro`** y **`Proyectos Registrados`**.

#### ✍️ Cómo registrar una nueva tesis paso a paso (Pestaña `Nuevo Registro`):
1. **Código de Proyecto:** Escribe el código oficial del expediente. Por ejemplo: `PROY-SUIZA-2026-005`.
2. **Título del Proyecto:** Escribe el nombre completo de la tesis. Por ejemplo: *`Sistema Web para la Gestión de Citas Médicas y Triage en el Centro de Salud de Pucallpa`*.
3. **Programa de Estudio (Carrera):** Haz clic en la casilla desplegable y selecciona una de las **11 carreras oficiales del IESTP Suiza** (ej. *Desarrollo de Sistemas de Información*, *Contabilidad*, *Enfermería Técnica*, *Gestión Administrativa*, etc.).
4. **Modalidad de Titulación:** Elige cómo se titulan (*Tesis o Proyecto de Aplicación Profesional*, *Examen de Suficiencia Profesional* o *Perfil del Proyecto*).
5. **Asesor:** Selecciona al profesor que los asesoró (*Ing. Ruber Torres Arevalo*, *Lic. Carlos Mendoza*, etc.).
6. **Adjuntar Documento Inicial:** Puedes presionar **`Adjuntar Archivo (*.PDF / *.DOCX)`** para subir el documento digital del expediente en este momento.
7. **Resumen del Proyecto:** Escribe en el cuadro de texto un breve resumen de 2 o 3 líneas explicando qué resuelve el proyecto.

#### 👥 Cómo agregar a los alumnos (Integrantes):
Justo debajo del formulario principal, está la sección para registrar a los estudiantes:
1. Llena los campos: **DNI**, **Nombres**, **Apellidos**, **Email Institucional**, **Teléfono** y **Código de Estudiante**.
2. Haz clic en el botón verde **`+ Añadir Estudiante`**. El alumno aparecerá inmediatamente en la tabla inferior.
3. **¡REGLA INNEGOCIABLE DEL INSTITUTO (`máximo 5 estudiantes`)!** Un proyecto puede tener **desde 1 hasta 5 estudiantes**. Si intentas añadir un 6to estudiante, el sistema bloqueará la acción automáticamente con una alerta de advertencia.
4. Si te equivocaste con un alumno, selecciónalo en la tabla y presiona **`Eliminar Seleccionado`**.
5. Al finalizar, haz clic en **`💾 Registrar Proyecto en Base de Datos`**. Te confirmará: *"¡Registro Exitoso!"*.

#### 🔍 Pestaña `Proyectos Registrados` (Gestión de Documentos y Observaciones):
Aquí verás la tabla central con todos los proyectos registrados. Arriba tienes un buscador en tiempo real por código, título o modalidad.

##### 📂 Gestión de Archivos Digitales de Tesis (Lo pedido por el flujo oficial):
Justo debajo de la tabla de proyectos, la Secretaría cuenta con botones especializados para el control documental del expediente:
* 📤 **`Subir Documento (Rectificado)`:** Si el coordinador o el jefe devolvieron un proyecto con observaciones (`OBSERVADO` o `RECHAZADO`), la secretaria selecciona la fila del proyecto, presiona este botón y elige el nuevo archivo Word o PDF corregido por el alumno.  
  **⚡ ¡AUTOMATIZACIÓN INTELIGENTE!** Al subir exitosamente el archivo rectificado, el sistema **cambia automáticamente el estado del proyecto de regreso a `EN_REVISION`** y actualiza la tabla para que el Coordinador sepa que el documento ya fue corregido y está listo para una nueva evaluación.
* 🗑️ **`Eliminar Documento`:** Si se subió un archivo erróneo al expediente, selecciona el proyecto y presiona este botón para eliminar el documento binario (`LONGBLOB`) de MySQL, permitiendo adjuntar uno limpio.
* 📥 **`Descargar Documento`:** Descarga a tu computadora y visualiza el archivo PDF o Word actual asociado a la tesis.
* 👁️ **`Ver en Pantalla`:** Abre una ventana emergente rápida con el historial detallado de observaciones y correcciones que hicieron el Coordinador o Jefe.
* 📊 **`Descargar Obs. Excel (.XLS)` y `Descargar Obs. PDF/HTML`:** Exporta el historial formal de correcciones en hoja de cálculo o documento institucional imprimible.

---

### 🔬 ROL 2: COORDINACIÓN ACADÉMICA (`coordinador`)
El Coordinador Académico es el evaluador metodológico. Su interfaz principal (`Revisión de Proyectos`) le permite revisar tesis a máxima velocidad sin complicaciones documentales.

#### 🔎 Cómo evaluar y hacer observaciones a una tesis paso a paso:
1. En la tabla principal, selecciona la fila del proyecto que deseas evaluar (ej. `PROY-SUIZA-2026-001`).
2. **📥 Botón `Descargar Documento (Secretaría)`:** Haz clic en este botón azul grande debajo de la tabla. El sistema descargará al instante el archivo original (Word o PDF) subido por la Secretaría y **lo abrirá automáticamente en la pantalla de tu computadora** (con Adobe Reader o Microsoft Word) para tu cómoda lectura.
3. **✍️ Registro de Correcciones (`txtObservacion`):** Mientras lees el documento abierto, ve a la caja de texto inferior en el programa (`Registro de Observaciones Metodológicas / Calificación`) y escribe las correcciones que debe enmendar el alumno (ej: *"Falta corregir planteamiento del problema, objetivos específicos y anexos"*).
4. **🔄 Cambiar Estado (`cbEstadoRevision`):** Selecciona el nuevo estado de tu evaluación en la lista desglosable:
   * **`EN_REVISION`**: Si aún está en proceso de lectura.
   * **`APROBADO_COORDINACION`**: Si el proyecto cumple toda la metodología y le das el pase para la Jefatura de Investigación.
   * **`OBSERVADO`**: Si el alumno debe corregir el archivo (al guardarlo, la secretaria verá la observación en su panel).
   * **`RECHAZADO`**: Si el proyecto incumple normas o es duplicado.
5. Presiona el botón azul **`Guardar Evaluación`**. La observación quedará inmortalizada con fecha, hora y nombre del coordinador en la base de datos MySQL.
6. **Botones de Reportes del Coordinador:** También cuentas con los botones **`Descargar Obs. Excel (.XLS)`**, **`Descargar Obs. PDF/HTML`** y **`Exportar Proyectos (.XLS)`** para emitir consolidados para reuniones de comisión.

---

### 👑 ROL 3: JEFATURA DE INVESTIGACIÓN Y DIRECCIÓN (`jefe`)
El Jefe de Investigación administra el sistema y emite el dictamen definitivo que autoriza la titulación. Su menú lateral izquierdo cuenta con **4 pestañas directivas**:

#### 1️⃣ Pestaña `Aprobación` (Visto Bueno Final de Titulación):
* Muestra las tesis que ya superaron la revisión del coordinador (`APROBADO_COORDINACION`).
* **📥 Botón `Descargar Documento (Secretaría)`:** Al igual que en coordinación, el Jefe selecciona la fila y presiona este botón para abrir de inmediato el documento Word/PDF en su pantalla y verificar que todo esté en orden.
* **⚡ Botón `Emitir Observación Jefatura`:** Escribe tus notas en el cuadro de texto y presiona este botón si deseas agregar observaciones directivas al expediente sin cambiar su estado aún.
* **⚡ Botón `Devolver o Rechazar`:** Devuelve el proyecto a estado `OBSERVADO` o `RECHAZADO` si detectas fallas formales finales.
* **🌟 Botón `Aprobar para Titulación`:** Si la tesis cumple al 100%, selecciónala y haz clic en este botón primario. El estado cambia formalmente a **`APROBADO_FINAL`**, autorizando la sustentación oficial ante jurado para obtención de título profesional.

#### 2️⃣ Pestaña `Usuarios & TI` (Administración de Personal):
* Tabla completa con todas las cuentas del sistema (`secretaria`, `coordinador`, `jefe`, etc.).
* **Crear Nuevos Usuarios:** Llena las casillas (*Username, Password, Nombre Completo, Rol y Correo*) y presiona **`+ Crear Usuario`**.
* **Gestión de Tickets TI:** Aquí verás y podrás resolver los tickets de restablecimiento de contraseña solicitados por el personal (`TICK-SUIZA-XXXX`).

#### 3️⃣ Pestaña `Catálogos` (Configurar Carreras, Modalidades y Asesores):
* Control total sin depender de programadores. Puedes seleccionar el tipo de catálogo (`PROGRAMA_ESTUDIO`, `MODALIDAD_TITULACION`, `ASESOR` o `ESTADO_PROYECTO`).
* Escribe el nombre del nuevo ítem (ej. *Ingeniería de Software* o un nuevo asesor docente), su código y presiona **`+ Agregar Ítem al Catálogo`**. Estará disponible al segundo en los selectores de la secretaría.

#### 4️⃣ Pestaña `Reportes` (El Módulo Gerencial Inteligente con Filtros de Tiempo):
Este módulo es una herramienta de auditoría avanzada que permite generar reportes estadísticos e institucionales con **filtros de tiempo, carrera, modalidad y estado**:
1. **⏳ Filtro Rápido por Tiempo (De 1 mes a 5 años):** En la barra superior tienes 6 casillas temporales dinámicas:
   * **`[ 1 Mes ]`**: Proyectos registrados en los últimos 30 días.
   * **`[ 3 Meses ]`**: Proyectos del trimestre actual.
   * **`[ 6 Meses ]`**: Proyectos del semestre actual.
   * **`[ 1 Año ]`**: Proyectos de los últimos 365 días.
   * **`[ 3 Años ]`**: Proyectos del trienio reciente.
   * **`[ 5 Años ]`**: Histórico del último quinquenio.
2. **Filtros por Catálogo:** Puedes combinar el tiempo con los selectores desglosables para filtrar por una carrera específica (ej. *Contabilidad*), una modalidad (*Tesis*) o un estado (*APROBADO_FINAL*).
3. Haz clic en **`📊 Generar Reporte`**.
4. **¿Qué verás en pantalla?** Las tarjetas de resumen superiores se recalcularán showing el **Total de Proyectos Encontrados** y el **Total de Estudiantes Involucrados**. La tabla te mostrará el detalle exacto de los registros con sus fechas para exportación a **Excel (`.XLS`)** o **PDF/HTML**.

---

## 🆘 PASO 4: ¿QUÉ HAGO SI OLVIDÉ MI CONTRASEÑA? (SISTEMA DE TICKETS TI)
Si un usuario olvida su contraseña, el sistema cuenta con un generador de tickets de soporte integrado que **no requiere internet ni configuración de servidores SMTP externos**:
1. En la pantalla de **Login**, haz clic en el enlace azul **`¿Olvidó su contraseña?`**.
2. Se abrirá la ventana de **"Recuperación de Credenciales"**.
3. Escribe tu usuario (ej. `secretaria`) y haz clic en **`Generar Ticket TI`**.
4. El sistema verificará tu existencia en MySQL y te entregará en un recuadro verde brillante un código único: **`TICK-SUIZA-XXXX`** (ej. `TICK-SUIZA-8492`).
5. Indícale ese número de ticket al Administrador / Jefe de Investigación para que te restablezca tu contraseña de forma segura desde su pestaña `Usuarios & TI`.

---

## 🚪 PASO 5: CÓMO SALIR O CERRAR SESIÓN CORRECTAMENTE
* **Para cambiar de usuario (`Cerrar Sesión`):** Presiona el botón **`🚪 Cerrar Sesión`** en el menú inferior izquierdo. La ventana se restaurará suavemente a su tamaño normal del Login (`660x720`) lista para otro ingreso.
* **Para cerrar el software (`Salir`):** En la pantalla de Login, haz clic en el botón gris **`Salir`** (`&#xf08b;`).

---

## ✅ RESUMEN DE BLINDAJE Y GARANTÍA TÉCNICA
* ✔️ **Gestión Documental Completa (`LONGBLOB`):** Subida, rectificación con retorno automático a `EN_REVISION`, eliminación y descarga directa con apertura automática en pantalla para revisión en tiempo real.
* ✔️ **11 Programas de Estudio UTF-8:** Base de datos y respaldos offline sincronizados carácter por carácter sin recortes ni pérdidas de tildes.
* ✔️ **Cero Emojis / FontAwesome 6 Solid:** Interfaz limpia sin íconos incompatibles ni advertencias de renderizado.
* ✔️ **Auditoría FXML Automatizada:** Integridad estructural y de controladores probada con la clase de verificación en tiempo de ejecución **`TestFXMLLoader`**.
* ✔️ **Ejecutable FAT-JAR Unificado:** Todo empaquetado en `dist/Gestion_Titulacion_Ejecutable.jar` con arranque centralizado en `INICIAR_SISTEMA_TITULACION.bat`.
