-- ==============================================================================
-- SISTEMA DE REGISTRO DE PROYECTOS DE TITULACIÓN - IESTP SUIZA
-- SCRIPT DE BASE DE DATOS E INYECCIÓN DE DATOS INICIALES (XAMPP / MySQL)
-- MIGRACIÓN 100% ALINEADA A LOS DAOs DE JAVA Y BUENAS PRÁCTICAS
-- ==============================================================================

CREATE DATABASE IF NOT EXISTS gestion_titulacion_suiza 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_spanish_ci;

USE gestion_titulacion_suiza;

-- ------------------------------------------------------------------------------
-- 1. TABLA DE USUARIOS (Con ticket_soporte para recuperación y password_claro)
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS observaciones;
DROP TABLE IF EXISTS estudiantes;
DROP TABLE IF EXISTS proyectos;
DROP TABLE IF EXISTS catalogos;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    password_claro VARCHAR(100) NOT NULL,
    nombre_completo VARCHAR(150) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    email VARCHAR(100) NULL,
    telefono VARCHAR(20) NULL,
    ticket_soporte VARCHAR(50) NULL,
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_rol (rol),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ------------------------------------------------------------------------------
-- 2. TABLA DE CATÁLOGOS UNIVERSAL (Programas, Modalidades, Asesores y Estados)
-- ------------------------------------------------------------------------------
CREATE TABLE catalogos (
    id_catalogo INT AUTO_INCREMENT PRIMARY KEY,
    tipo_catalogo VARCHAR(50) NOT NULL,
    nombre_item VARCHAR(150) NOT NULL,
    codigo_item VARCHAR(50) NULL,
    descripcion TEXT NULL,
    estado VARCHAR(20) DEFAULT 'ACTIVO',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_tipo_estado (tipo_catalogo, estado),
    INDEX idx_nombre (nombre_item)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ------------------------------------------------------------------------------
-- 3. TABLA DE PROYECTOS DE TITULACIÓN
-- ------------------------------------------------------------------------------
CREATE TABLE proyectos (
    id_proyecto INT AUTO_INCREMENT PRIMARY KEY,
    codigo_proyecto VARCHAR(50) UNIQUE NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    programa_estudio VARCHAR(150) NOT NULL,
    modalidad VARCHAR(150) NOT NULL,
    asesor VARCHAR(150) NULL,
    estado VARCHAR(100) NOT NULL,
    fecha_registro DATE NOT NULL,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    resumen TEXT NULL,
    id_usuario_registro INT NULL,
    INDEX idx_codigo (codigo_proyecto),
    INDEX idx_programa (programa_estudio),
    INDEX idx_modalidad (modalidad),
    INDEX idx_asesor (asesor),
    INDEX idx_estado_proj (estado),
    INDEX idx_fecha (fecha_registro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ------------------------------------------------------------------------------
-- 4. TABLA DE ESTUDIANTES (Hasta 5 estudiantes por proyecto)
-- ------------------------------------------------------------------------------
CREATE TABLE estudiantes (
    id_estudiante INT AUTO_INCREMENT PRIMARY KEY,
    id_proyecto INT NOT NULL,
    dni_codigo VARCHAR(15) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    email VARCHAR(100) NULL,
    telefono VARCHAR(20) NULL,
    codigo_estudiante VARCHAR(30) NULL,
    INDEX idx_proyecto_est (id_proyecto),
    INDEX idx_dni (dni_codigo),
    CONSTRAINT fk_est_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyectos(id_proyecto) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ------------------------------------------------------------------------------
-- 5. TABLA DE OBSERVACIONES (Emitidas por Coordinador o Jefe de Investigación)
-- ------------------------------------------------------------------------------
CREATE TABLE observaciones (
    id_observacion INT AUTO_INCREMENT PRIMARY KEY,
    id_proyecto INT NULL,
    id_usuario INT NULL,
    rol_autor VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_observacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado_observacion VARCHAR(50) DEFAULT 'PENDIENTE',
    INDEX idx_proy_obs (id_proyecto),
    INDEX idx_usu_obs (id_usuario),
    INDEX idx_fecha_obs (fecha_observacion),
    CONSTRAINT fk_obs_proyecto FOREIGN KEY (id_proyecto) REFERENCES proyectos(id_proyecto) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;

-- ==============================================================================
-- SIEMBRA DE DATOS INICIALES (SEED DATA PARA PRESENTACIÓN DOCENTE)
-- ==============================================================================

-- 1. Insertar Usuarios del Sistema (Clave en claro para demostración académica)
INSERT INTO usuarios (username, password, password_claro, nombre_completo, rol, email, telefono, estado) VALUES
('secretaria', 'secretaria123', 'secretaria123', 'Noelia Bolaños (Atención y Registro)', 'SECRETARIA', 'secretaria@iestpsuiza.edu.pe', '987654321', 'ACTIVO'),
('coordinador', 'coordinador123', 'coordinador123', 'Mg. Ruber Torres Arevalo (Coordinación)', 'COORDINADOR', 'rtorres@iestpsuiza.edu.pe', '988776655', 'ACTIVO'),
('jefe', 'jefe123', 'jefe123', 'Dr. Alejandro Barbaran (Jefatura Investigación)', 'JEFE_INVESTIGACION', 'jefatura@iestpsuiza.edu.pe', '999888777', 'ACTIVO'),
('secretaria2', 'secretaria123', 'secretaria123', 'Isak Rioja (Soporte Titulación)', 'SECRETARIA', 'irioja@iestpsuiza.edu.pe', '911223344', 'ACTIVO');

-- 2. Insertar Catálogos del Sistema
-- Programas de Estudio
INSERT INTO catalogos (tipo_catalogo, nombre_item, codigo_item, descripcion, estado) VALUES
('PROGRAMA_ESTUDIO', 'ADMINISTRACION DE OPERACIONES TURÍSTICAS', 'AOT', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'ASISTENCIA ADMINISTRATIVA', 'AA', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'CONTABILIDAD', 'CONT', 'Carrera orientada a sistemas financieros y auditoría contable.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'CONSTRUCCIÓN CIVIL', 'CC', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'GESTIÓN ADMINISTRATIVA', 'GA', 'Gestión empresarial y finanzas organizacionales.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'DESARROLLO DE SISTEMAS DE INFORMACIÓN', 'DSI', 'Carrera Tecnológica de 3 años orientada a software y bases de datos.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'ELECTRICIDAD INDUSTRIAL', 'EI', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'ENFERMERÍA TÉCNICA', 'ENF', 'Carrera de salud y asistencia clínica asistencial.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'MANEJO FORESTAL', 'MF', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'MECATRÓNICA AUTOMOTRIZ', 'MA', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
('PROGRAMA_ESTUDIO', 'PRODUCCIÓN AGROPECUARIA', 'PA', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO');

-- Modalidades de Titulación
INSERT INTO catalogos (tipo_catalogo, nombre_item, codigo_item, descripcion, estado) VALUES
('MODALIDAD_TITULACION', 'Tesis o Proyecto de Aplicación Profesional', 'MOD-INVEST', 'Tesis o trabajo de aplicación profesional con desarrollo tecnológico.', 'ACTIVO'),
('MODALIDAD_TITULACION', 'Examen de Suficiencia Profesional', 'MOD-SUFIC', 'Evaluación teórico-práctica para egresados de la institución.', 'ACTIVO'),
('MODALIDAD_TITULACION', 'Perfil del Proyecto', 'MOD-PERFIL', 'Propuesta inicial o anteproyecto técnico sustentado ante jurado.', 'ACTIVO');

-- Asesores Académicos
INSERT INTO catalogos (tipo_catalogo, nombre_item, codigo_item, descripcion, estado) VALUES
('ASESOR', 'Ing. Ruber Torres Arevalo', 'ASE-001', 'Especialista en Inteligencia Artificial y Gestión de Sistemas.', 'ACTIVO'),
('ASESOR', 'Lic. Carlos Mendoza', 'ASE-002', 'Especialista en Bases de Datos y Arquitectura de Software.', 'ACTIVO'),
('ASESOR', 'Dra. María Gonzales', 'ASE-003', 'Especialista en Metodología de la Investigación.', 'ACTIVO'),
('ASESOR', 'Ing. Fernando Gómez López', 'ASE-004', 'Especialista en Redes, Seguridad de la Información y Cloud.', 'ACTIVO');

-- Estados del Proyecto
INSERT INTO catalogos (tipo_catalogo, nombre_item, codigo_item, descripcion, estado) VALUES
('ESTADO_PROYECTO', 'EN_REVISION', 'EST-REV', 'Proyecto bajo evaluación del Coordinador Académico.', 'ACTIVO'),
('ESTADO_PROYECTO', 'APROBADO_COORDINACION', 'EST-APROB-COORD', 'Proyecto visado por Coordinación, pasa a Jefatura de Investigación.', 'ACTIVO'),
('ESTADO_PROYECTO', 'OBSERVADO', 'EST-OBS', 'Proyecto devuelto con observaciones para subsanación.', 'ACTIVO'),
('ESTADO_PROYECTO', 'APROBADO_FINAL', 'EST-APROB-JEFE', 'Proyecto aprobado finalmente para sustentar o recibir título.', 'ACTIVO'),
('ESTADO_PROYECTO', 'RECHAZADO', 'EST-RECH', 'Proyecto rechazado por incumplimiento o duplicidad.', 'ACTIVO');

-- 3. Insertar Proyectos de Ejemplo (Con fechas variadas para probar los filtros de 1 mes a 5 años)
INSERT INTO proyectos (codigo_proyecto, titulo, programa_estudio, modalidad, asesor, estado, fecha_registro, resumen, id_usuario_registro) VALUES
('PROY-SUIZA-2026-001', 'Sistema Inteligente de Gestión Hospitalaria y Control de Triage MediConnect en IESTP Suiza', 'DESARROLLO DE SISTEMAS DE INFORMACIÓN', 'Tesis o Proyecto de Aplicación Profesional', 'Ing. Ruber Torres Arevalo', 'EN_REVISION', CURDATE() - INTERVAL 15 DAY, 'Desarrollo de un sistema de control hospitalario con arquitectura All-in-One y JavaFX.', 1),
('PROY-SUIZA-2026-042', 'Plataforma Web para el Control de Almacén e Inventarios con Código de Barras en Pucallpa', 'DESARROLLO DE SISTEMAS DE INFORMACIÓN', 'Examen de Suficiencia Profesional', 'Lic. Carlos Mendoza', 'APROBADO_COORDINACION', CURDATE() - INTERVAL 3 MONTH, 'Implementación de sistema web para reducir pérdidas en el almacén central de la institución.', 1),
('PROY-SUIZA-2025-018', 'Evaluación de los Procesos Contables y Auditoría Digital en Mypes de la Región Ucayali', 'CONTABILIDAD', 'Tesis o Proyecto de Aplicación Profesional', 'Dra. María Gonzales', 'OBSERVADO', CURDATE() - INTERVAL 14 MONTH, 'Investigación de impacto tributario y digitalización contable en empresas locales.', 1),
('PROY-SUIZA-2023-005', 'Sistema Móvil para el Segregado de Residuos Hospitalarios y Protocolos de Bioseguridad', 'ENFERMERÍA TÉCNICA', 'Examen de Suficiencia Profesional', 'Ing. Ruber Torres Arevalo', 'APROBADO_FINAL', CURDATE() - INTERVAL 4 YEAR, 'App móvil para control de protocolos de bioseguridad en centros de salud.', 1);

-- 4. Insertar Estudiantes (Integrantes asignados a proyectos)
INSERT INTO estudiantes (id_proyecto, dni_codigo, nombres, apellidos, email, telefono, codigo_estudiante) VALUES
(1, '72345678', 'Angelo', 'Panaifo', 'apanaifo@est.iestpsuiza.edu.pe', '955443322', 'EST-2023001'),
(1, '73456789', 'Isak', 'Rioja', 'irioja@est.iestpsuiza.edu.pe', '966554433', 'EST-2023002'),
(1, '74567890', 'Noelia', 'Bolaños', 'nbolanos@est.iestpsuiza.edu.pe', '977665544', 'EST-2023003'),
(2, '71234567', 'Kevin', 'Salas Morán', 'ksalas@est.iestpsuiza.edu.pe', '911882233', 'EST-2022088'),
(2, '70123456', 'María', 'López García', 'mlopez@est.iestpsuiza.edu.pe', '922773344', 'EST-2022089'),
(3, '48901234', 'Jorge', 'Castro Vela', 'jcastro@est.iestpsuiza.edu.pe', '933664455', 'EST-2021015');

-- 5. Insertar Observaciones de Prueba
INSERT INTO observaciones (id_proyecto, id_usuario, rol_autor, fecha_observacion, descripcion, estado_observacion) VALUES
(1, 2, 'COORDINADOR', NOW() - INTERVAL 10 DAY, 'Falta especificar el diagrama de arquitectura y el cronograma de Gantt del desarrollo de software.', 'SUBSANADO'),
(2, 2, 'COORDINADOR', NOW() - INTERVAL 2 MONTH, 'El planteamiento del problema debe delimitarse geográficamente a la ciudad de Pucallpa.', 'SUBSANADO'),
(3, 3, 'JEFE_INVESTIGACION', NOW() - INTERVAL 1 MONTH, 'El presupuesto presentado supera el límite del programa. Ajustar cotización de hardware.', 'PENDIENTE'),
(3, 2, 'COORDINADOR', NOW() - INTERVAL 12 MONTH, 'El instrumento de recolección de datos no cuenta con el certificado de validación por juicio de expertos.', 'PENDIENTE');

SELECT 'BASE DE DATOS GESTION_TITULACION_SUIZA CREADA Y SEMBRADA EXITOSAMENTE' AS Resultado;
