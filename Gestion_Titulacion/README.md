# 📗 MANUAL PASO A PASO DEL SISTEMA DE TITULACIÓN - IESTP SUIZA
*(Guía ultra detallada explicada paso a paso, botón por botón, para cualquier usuario, alumno, secretario, docente o administrador)*

---

## 🌟 ¿DE QUÉ TRATA ESTE SISTEMA?
Este programa sirve para registrar, revisar, observar y aprobar todas las tesis y proyectos de titulación de los estudiantes del **Instituto de Educación Superior Tecnológico Público Suiza (IESTP Suiza)**.

El sistema funciona con **3 roles (tipos de cuenta)** diferentes:
1. 📁 **Secretaría Académica:** Es la persona encargada de recibir las carpetas de los alumnos y registrar los datos del proyecto y de los integrantes.
2. 🔬 **Coordinación Académica:** Es el profesor o coordinador que lee las tesis, las evalúa, les pone observaciones si hay errores o las aprueba.
3. 👑 **Jefatura de Investigación y Administración:** Es el jefe supremo que da la aprobación final para que los alumnos se titulen, administra los reportes por años, gestiona usuarios y administra las carreras.

---

## 🟢 PASO 0: LO QUE DEBES HACER ANTES DE ABRIR EL PROGRAMA (ENCENDER XAMPP)
Para que el programa guarde los datos permanentemente, necesita que la base de datos **MySQL** esté encendida en tu computadora.

1. Busca en tu Windows el programa llamado **`XAMPP Control Panel`** (tiene un icono de un huesito o una letra **X** naranja) y hazle doble clic para abrirlo.
2. Verás una ventana con una lista de nombres (*Apache, MySQL, FileZilla, etc.*).
3. Haz clic en el botón verde que dice **`Start`** (o **Iniciar**) justo a la derecha de **`Apache`**. Espera 2 segundos hasta que la palabra *Apache* se ponga de color **verde**.
4. Haz clic en el botón verde que dice **`Start`** (o **Iniciar**) justo a la derecha de **`MySQL`**. Espera 2 segundos hasta que la palabra *MySQL* se ponga de color **verde** y aparezca el número `3306` en la columna de puertos.
5. ¡Listo! Ya puedes minimizar (ocultar) la ventana de XAMPP. Tu base de datos está activa.

> [!NOTE]
> **💡 ¿QUÉ PASA SI SE ME OLVIDA ENCENDER XAMPP O ESTOY SIN INTERNET/RED?**
> ¡No te preocupes! El programa cuenta con un **"Salvavidas de Emergencia (Modo Local / Offline)"**. Si intentas entrar y XAMPP está apagado, el sistema no se quedará congelado ni te dará un error fatal; te permitirá ingresar en modo local para que puedas revisar las pantallas y continuar trabajando.

---

## 🚀 PASO 1: CÓMO ABRIR EL PROGRAMA (UN SOLO EJECUTABLE PARA NO ROMPER NADA)
Para no confundirse con tantos archivos técnicos ni romper el flujo, hemos preparado **una sola puerta de entrada fácil para todos**:

### 👉 Opción A (La más fácil - Doble clic en Windows):
1. Entra a tu carpeta del proyecto: `C:\Users\Geric\Desktop\Gest_Titulacion\Gestion_Titulacion\`
2. Busca el archivo llamado **`INICIAR_SISTEMA_TITULACION.bat`** (tiene el icono de dos tuercas o engranajes).
3. Haz **doble clic** sobre él.
4. Se abrirá una pequeña ventanita negra por un segundo y luego aparecerá mágicamente en el centro de tu pantalla la ventana blanca de **Acceso al Sistema (Login)**.

### 👉 Opción B (Desde el NetBeans IDE):
1. Abre tu programa **NetBeans**.
2. Haz clic en el proyecto **`Gestion_Titulacion`**.
3. Presiona la tecla **`F6`** en tu teclado (o haz clic en el triángulo verde de Play en la barra superior que dice *Run Project*).
4. Inmediatamente se abrirá la ventana de **Acceso (Login)** en el centro exacto de tu monitor.

---

## 🔐 PASO 2: CÓMO INICIAR SESIÓN (CUENTAS Y CONTRASEÑAS EXACTAS)
En la pantalla de bienvenida verás arriba un círculo blanco con el logotipo oficial del **IESTP Suiza** y debajo dos casillas vacías:
* **Casilla 1 (`Usuario`):** Aquí escribes el nombre de tu cuenta.
* **Casilla 2 (`Contraseña`):** Aquí escribes tu clave secreta.

### 📋 TABLA DE CREDENCIALES PARA COPIAR Y PEGAR:
Elige la cuenta a la que quieras entrar según lo que quieras hacer:

| ¿Qué quieres probar hoy? | Escribe en Usuario (`username`) | Escribe en Contraseña (`password`) | ¿Quién entrará? |
| :--- | :--- | :--- | :--- |
| **Registrar proyectos nuevos y alumnos** | `secretaria` | `secretaria123` | **Noelia Bolaños** *(Secretaría Académica)* |
| **Revisar tesis y poner observaciones** | `coordinador` | `coordinador123` | **Mg. Ruber Torres Arevalo** *(Coordinación)* |
| **Aprobar al final, ver Reportes de 1 a 5 años** | `jefe` | `jefe123` | **Dr. Alejandro Barbaran** *(Jefatura / Admin)* |
| **Cuenta extra de soporte secretarial** | `secretaria2` | `secretaria123` | **Isak Rioja** *(Soporte Titulación)* |

### 🛠️ ¿Cómo usar los botones del Login?
1. Escribe tu usuario (por ejemplo: `secretaria`).
2. Escribe tu contraseña (`secretaria123`).
3. **¿Tienes dudas de si escribiste bien tu contraseña?** Haz clic en el botón del **Ojito (`👁`)** que está dentro de la casilla a la derecha. Tu contraseña oculta con puntos se convertirá en texto normal para que la puedas leer. Si le vuelves a hacer clic, se volverá a ocultar.
4. Haz clic en el botón azul grande que dice **`Ingresar`**.
5. **¡Atención a la magia!** La ventanita pequeña desaparecerá y se abrirá tu módulo de trabajo en **Pantalla Completa verdadera (Maximizada)** abarcando todo tu monitor, sin espacios en blanco ni barras cortadas.

---

## 📁 PASO 3: QUÉ HACER DENTRO DE CADA ROL (GUÍA DETALLADA BOTÓN POR BOTÓN)

---

### 🌸 ROL 1: SECRETARÍA ACADÉMICA (`secretaria`)
Al entrar con la cuenta `secretaria`, verás a la izquierda un menú azul oscuro con tus datos y 2 opciones principales: **`Nuevo Registro`** y **`Proyectos Registrados`**.

#### ✍️ Cómo registrar una nueva tesis paso a paso (Pestaña `Nuevo Registro`):
Imagina que llegaron 3 alumnos a tu escritorio con su empastado para titularse. Sigue estos pasos exactos:
1. **Código de Proyecto:** Escribe el código oficial del expediente. Por ejemplo: `PROY-SUIZA-2026-005`.
2. **Título del Proyecto:** Escribe el nombre completo de la tesis. Por ejemplo: *`Sistema Web para la Gestión de Citas Médicas y Triage en el Centro de Salud de Pucallpa`*.
3. **Programa de Estudio (Carrera):** Haz clic en la casilla con la flechita hacia abajo y selecciona la carrera de los alumnos (*Desarrollo de Sistemas de Información*, *Contabilidad*, *Enfermería Técnica* o *Administración de Empresas*).
4. **Modalidad de Titulación:** Haz clic en la flechita y elige cómo se titulan (*Tesis o Proyecto de Aplicación Profesional*, *Examen de Suficiencia Profesional* o *Perfil del Proyecto*).
5. **Asesor:** Haz clic y selecciona al profesor que los asesoró (por ejemplo: *Ing. Ruber Torres Arevalo* o *Lic. Carlos Mendoza*).
6. **Resumen del Proyecto:** Escribe en el cuadro grande de abajo un breve resumen de 2 o 3 líneas explicando qué hace el proyecto.

#### 👥 Cómo agregar a los alumnos (Integrantes):
Justo debajo del formulario del proyecto, está la sección para registrar a los estudiantes:
1. **DNI:** Escribe los 8 números del DNI del alumno (ej. `72345678`).
2. **Nombres:** Escribe sus nombres (ej. `Kevin Aldair`).
3. **Apellidos:** Escribe sus apellidos (ej. `Salas Ormeño`).
4. **Email Institucional:** Escribe su correo (ej. `ksalas@est.iestpsuiza.edu.pe`).
5. **Teléfono:** Escribe su celular (ej. `955443322`).
6. **Código:** Escribe su código de estudiante (ej. `EST-2023088`).
7. Haz clic en el botón verde con el signo más **`+ Añadir Estudiante`**.
8. Verás cómo el alumno aparece inmediatamente en la tabla inferior.
9. **¡REGLA INNEGOCIABLE DEL INSTITUTO!** Un proyecto puede tener **desde 1 hasta un máximo de 5 estudiantes**. Si intentas añadir a un 6to estudiante, el sistema bloqueará la acción y te mostrará una alerta advirtiendo que ya se alcanzó el límite máximo permitido.
10. Si te equivocaste al escribir el nombre de un alumno en la tabla, haz clic sobre esa fila y presiona el botón rojo **`Eliminar Seleccionado`**.
11. Cuando ya hayas llenado todo, haz clic en el botón azul grande del fondo: **`💾 Registrar Proyecto en Base de Datos`**. Te saldrá un mensaje diciendo *"¡Registro Exitoso!"*.

#### 🔍 Cómo buscar lo que ya registraste (Pestaña `Proyectos Registrados`):
1. Haz clic en el menú izquierdo donde dice **`Proyectos Registrados`**.
2. Verás una tabla grande con todas las tesis guardadas.
3. Arriba verás una casilla que dice **`Buscar por código, título, modalidad o estado...`**.
4. Simplemente empieza a escribir ahí dentro (por ejemplo escribe la palabra `Sistemas` o el apellido `Panaifo`). Verás que en tiempo real, mientras tecleas letra por letra, la tabla se filtra al instante mostrándote solo lo que coincide.

---

### 🔬 ROL 2: COORDINACIÓN ACADÉMICA (`coordinador`)
Al entrar con la cuenta `coordinador`, tú eres el evaluador metodológico. Tu menú izquierdo tiene 2 pestañas: **`Revisión de Proyectos`** e **`Historial de Observaciones`**.

#### 🔎 Cómo evaluar y cambiar el estado de un proyecto (Pestaña `Revisión de Proyectos`):
1. Verás la lista de todos los proyectos que registró la secretaria en la tabla central.
2. Haz **un clic** sobre cualquiera de las filas de la tabla para seleccionar esa tesis.
3. Al hacer clic, verás que en el panel derecho superior (donde dice *Acción de Coordinación*) se iluminará el título del proyecto que elegiste.
4. Haz clic en la lista desplegable que dice **`-- Seleccionar Nuevo Estado --`**. Tienes estas opciones exactas según tu evaluación:
   * **`EN_REVISION`**: Si recién estás empezando a leer el documento.
   * **`APROBADO_COORDINACION`**: Si el proyecto está perfecto y quieres pasarlo a Jefatura de Investigación para su aprobación final.
   * **`OBSERVADO`**: Si encontraste errores (falta marco teórico, mala redacción, presupuesto incompleto).
   * **`RECHAZADO`**: Si el proyecto es plagio o no cumple en absoluto.
5. **Si elegiste `OBSERVADO`:** Ve al cuadro de texto de abajo que dice *Descripción o motivo de la observación* y escribe claramente qué debe corregir el alumno (ejemplo: *`Falta incluir el diagrama de base de datos y la carta de aceptación de la empresa.`*).
6. Haz clic en el botón azul **`⚡ Actualizar Estado / Guardar Observación`**. El estado cambiará en la base de datos inmediatamente.

---

### 👑 ROL 3: JEFATURA DE INVESTIGACIÓN Y ADMINISTRACIÓN (`jefe`)
Tú tienes el control total del sistema. Tu barra lateral izquierda tiene **4 botones** poderosos:

#### 1️⃣ Pestaña `Aprobación` (Visto Bueno Final):
* Aquí verás los proyectos que ya pasaron por Coordinación.
* Haz clic en una fila de la tabla, selecciona el estado **`APROBADO_FINAL`** y haz clic en guardar. Con esto, el expediente queda formalmente expedito para sustentación de título.

#### 2️⃣ Pestaña `Usuarios & TI` (Administración de Personal):
* En esta pestaña puedes ver la tabla con todas las cuentas del sistema (*secretaria, coordinador, jefe*).
* Puedes crear nuevas cuentas llenando las casillas (*Username, Password en claro, Nombre Completo, Rol y Correo*) y presionando **`+ Crear Usuario`**.
* También puedes ver aquí si algún funcionario solicitó un reseteo de clave mediante un **Ticket de Soporte TI**.

#### 3️⃣ Pestaña `Catálogos` (Configurar Carreras, Modalidades y Asesores):
* ¿El instituto abrió una nueva carrera en 2026? ¡No necesitas llamar a un programador!
* Haz clic en la pestaña **`Catálogos`**.
* Elige el tipo en la lista: **`PROGRAMA_ESTUDIO`** (para carreras), **`MODALIDAD_TITULACION`** (para modalidades), **`ASESOR`** (para profesores) o **`ESTADO_PROYECTO`**.
* Escribe el nombre (ej. *`Mecatrónica Automotriz`*), ponle un código (ej. *`MEC-2026`*) y una descripción.
* Haz clic en **`+ Agregar Ítem al Catálogo`**. Inmediatamente aparecerá disponible para que la secretaria lo elija en sus listas desplegables al registrar proyectos.

#### 4️⃣ Pestaña `Reportes` (El Módulo Gerencial Inteligente con Filtros de Tiempo):
* Haz clic en la pestaña **`Reportes`**.
* Aquí puedes generar estadísticas oficiales para el Ministerio o la Dirección General.
* **Filtro Rápido por Tiempo (De 1 mes a 5 años):**
  En la parte superior verás 6 botones estilizados. Haz clic en el que necesites:
  * **`[ 1 Mes ]`**: Muestra solo los proyectos registrados en los últimos 30 días.
  * **`[ 3 Meses ]`**: Muestra el trimestre actual.
  * **`[ 6 Meses ]`**: Muestra el semestre actual.
  * **`[ 1 Año ]`**: Muestra todo el año académico en curso.
  * **`[ 2 Años ]`**: Muestra el bienio reciente.
  * **`[ 5 Años ]`**: Muestra el histórico quinquenal para evaluación institucional.
* **Filtros por Catálogo:** Si quieres ser aún más específico, además del tiempo puedes elegir en las casillas desplegables:
  * Filtrar solo por una carrera (ej. *Desarrollo de Sistemas de Información*).
  * Filtrar solo por una modalidad (ej. *Tesis*).
  * Filtrar solo por un estado (ej. *APROBADO_FINAL*).
* Haz clic en el botón verde principal **`📊 Generar Reporte`**.
* **¿Qué verás en pantalla?**
  1. Arriba a la derecha se calcularán las tarjetas de resumen: **Total de Proyectos encontrados** y el **Total de Estudiantes involucrados**.
  2. En la tabla se mostrarán exactamente los expedientes que cumplen tu filtro con sus fechas reales de registro para auditoría.

---

## 🆘 PASO 4: ¿QUÉ HAGO SI OLVIDÉ MI CONTRASEÑA? (SISTEMA DE TICKETS TI)
Si un día intentas entrar y tu contraseña no funciona, no te preocupes ni necesitas configurar un correo web complicado:
1. En la pantalla de **Login**, debajo de los botones, haz clic en el texto azul que dice **`¿Olvidó su contraseña?`**.
2. Se abrirá una pequeña ventana llamada **"Recuperación de Credenciales"**.
3. Escribe en la casilla el nombre de tu usuario que olvidaste (por ejemplo: `secretaria` o `coordinador`).
4. Haz clic en el botón azul **`Generar Ticket TI`**.
5. Si tu usuario existe en la base de datos, aparecerá un recuadro verde brillante con un código único como: **`TICK-SUIZA-8492`**.
6. Anota ese código en un papelito o tómale foto con tu celular.
7. Acércate a la oficina del Administrador / Jefe de Investigación con ese código y dile: *"Hola, se me bloqueó mi clave, mi ticket es el TICK-SUIZA-8492"*. El administrador te restablecerá tu clave local al instante en su panel de Usuarios.
8. Haz clic en **`Cerrar`** para volver al Login.

---

## 🚪 PASO 5: CÓMO SALIR O CERRAR SESIÓN CORRECTAMENTE
Cuando termines tu turno de trabajo en el instituto, nunca apagues la computadora de golpe. Hazlo así para proteger los datos:

* **Opción 1 (`Cerrar Sesión` para cambiar de usuario):**
  En la parte inferior izquierda del menú oscuro de tu módulo, verás el botón **`🚪 Cerrar Sesión`**.
  Hazle clic. Tu pantalla completa se cerrará y la ventana regresará suavemente al centro de la pantalla con el tamaño exacto del **Login (`660x720`)**, lista para que otro compañero ingrese con su cuenta.

* **Opción 2 (`Salir` del programa por completo):**
  Si estás en el Login y ya quieres cerrar todo el software para irte a casa, haz clic en el botón gris que dice **`Salir`**. El programa se cerrará completamente de tu computadora.

---

## ✅ RESUMEN DE GARANTÍA TÉCNICA
* ✔️ **Todas las pantallas abren maximizadas** al entrar y se restauran al cerrar sesión.
* ✔️ **Todos los iconos** son de *FontAwesome 6 Solid* puro en español (`&#xf007;`, `&#xf023;`, `&#xf19d;`), garantizando **Cero Emojis** y cero errores gráficos.
* ✔️ **Un solo archivo ejecutable (`INICIAR_SISTEMA_TITULACION.bat`)** centraliza la ejecución sin romper el flujo de trabajo ni las sesiones de JavaFX.
* ✔️ **Conectado 100% a XAMPP MySQL** con salvavidas de emergencia local en caso de corte de red.
