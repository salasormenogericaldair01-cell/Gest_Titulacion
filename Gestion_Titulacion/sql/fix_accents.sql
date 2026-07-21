SET NAMES utf8mb4;

-- Limpiamos y recreamos con los 23 catálogos oficiales de IESTP Suiza en UTF-8 perfecto
TRUNCATE TABLE catalogos;

INSERT INTO catalogos (id_catalogo, tipo_catalogo, nombre_item, codigo_item, descripcion, estado) VALUES 
(1, 'PROGRAMA_ESTUDIO', 'ADMINISTRACIÓN DE OPERACIONES TURÍSTICAS', 'AOT', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
(2, 'PROGRAMA_ESTUDIO', 'ASISTENCIA ADMINISTRATIVA', 'AA', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
(3, 'PROGRAMA_ESTUDIO', 'CONTABILIDAD', 'CONT', 'Carrera orientada a sistemas financieros y auditoría contable.', 'ACTIVO'),
(4, 'PROGRAMA_ESTUDIO', 'CONSTRUCCIÓN CIVIL', 'CC', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
(5, 'PROGRAMA_ESTUDIO', 'GESTIÓN ADMINISTRATIVA', 'GA', 'Gestión empresarial y finanzas organizacionales.', 'ACTIVO'),
(6, 'PROGRAMA_ESTUDIO', 'DESARROLLO DE SISTEMAS DE INFORMACIÓN', 'DSI', 'Carrera Tecnológica de 3 años orientada a software y bases de datos.', 'ACTIVO'),
(7, 'PROGRAMA_ESTUDIO', 'ELECTRICIDAD INDUSTRIAL', 'EI', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
(8, 'PROGRAMA_ESTUDIO', 'ENFERMERÍA TÉCNICA', 'ENF', 'Carrera de salud y asistencia clínica asistencial.', 'ACTIVO'),
(9, 'PROGRAMA_ESTUDIO', 'MANEJO FORESTAL', 'MF', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
(10, 'PROGRAMA_ESTUDIO', 'MECATRÓNICA AUTOMOTRIZ', 'MA', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
(11, 'PROGRAMA_ESTUDIO', 'PRODUCCIÓN AGROPECUARIA', 'PA', 'Carrera Técnica Profesional de 3 años.', 'ACTIVO'),
(12, 'MODALIDAD_TITULACION', 'Tesis o Proyecto de Aplicación Profesional', 'MOD-INVEST', 'Tesis o trabajo de aplicación profesional con desarrollo tecnológico.', 'ACTIVO'),
(13, 'MODALIDAD_TITULACION', 'Examen de Suficiencia Profesional', 'MOD-SUFIC', 'Evaluación teórico-práctica para egresados de la institución.', 'ACTIVO'),
(14, 'MODALIDAD_TITULACION', 'Perfil del Proyecto', 'MOD-PERFIL', 'Propuesta inicial o anteproyecto técnico sustentado ante jurado.', 'ACTIVO'),
(15, 'ASESOR', 'Ing. Ruber Torres Arevalo', 'ASE-001', 'Especialista en Inteligencia Artificial y Gestión de Sistemas.', 'ACTIVO'),
(16, 'ASESOR', 'Lic. Carlos Mendoza', 'ASE-002', 'Especialista en Bases de Datos y Arquitectura de Software.', 'ACTIVO'),
(17, 'ASESOR', 'Dra. María Gonzales', 'ASE-003', 'Especialista en Metodología de la Investigación.', 'ACTIVO'),
(18, 'ASESOR', 'Ing. Fernando Gómez López', 'ASE-004', 'Especialista en Redes, Seguridad de la Información y Cloud.', 'ACTIVO'),
(19, 'ESTADO_PROYECTO', 'EN_REVISION', 'EST-REV', 'Proyecto bajo evaluación del Coordinador Académico.', 'ACTIVO'),
(20, 'ESTADO_PROYECTO', 'APROBADO_COORDINACION', 'EST-APROB-COORD', 'Proyecto visado por Coordinación, pasa a Jefatura de Investigación.', 'ACTIVO'),
(21, 'ESTADO_PROYECTO', 'OBSERVADO', 'EST-OBS', 'Proyecto devuelto con observaciones para subsanación.', 'ACTIVO'),
(22, 'ESTADO_PROYECTO', 'APROBADO_FINAL', 'EST-APROB-JEFE', 'Proyecto aprobado finalmente para sustentar o recibir título.', 'ACTIVO'),
(23, 'ESTADO_PROYECTO', 'RECHAZADO', 'EST-RECH', 'Proyecto rechazado por incumplimiento o duplicidad.', 'ACTIVO');

-- Actualizamos proyectos existentes a carreras válidas y con tildes correctas
UPDATE proyectos SET programa_estudio = 'DESARROLLO DE SISTEMAS DE INFORMACIÓN' WHERE programa_estudio LIKE '%Desarrollo%Sistemas%';
UPDATE proyectos SET programa_estudio = 'CONTABILIDAD' WHERE programa_estudio LIKE '%Contabilidad%';
UPDATE proyectos SET programa_estudio = 'ENFERMERÍA TÉCNICA' WHERE programa_estudio LIKE '%Enfermería%';
UPDATE proyectos SET programa_estudio = 'GESTIÓN ADMINISTRATIVA' WHERE programa_estudio LIKE '%Administración%Empresas%';

-- Actualizamos tildes en usuarios y estudiantes
UPDATE usuarios SET nombre_completo = 'Noelia Bolaños (Atención y Registro)' WHERE id_usuario = 1;
UPDATE usuarios SET nombre_completo = 'Mg. Ruber Torres Arevalo (Coordinación)' WHERE id_usuario = 2;
UPDATE usuarios SET nombre_completo = 'Dr. Alejandro Barbaran (Jefatura Investigación)' WHERE id_usuario = 3;
UPDATE usuarios SET nombre_completo = 'Isak Rioja (Soporte Titulación)' WHERE id_usuario = 4;
UPDATE estudiantes SET apellidos = 'Bolaños' WHERE id_estudiante = 3;
UPDATE estudiantes SET apellidos = 'Salas Morán' WHERE id_estudiante = 4;
UPDATE estudiantes SET nombres = 'María', apellidos = 'López García' WHERE id_estudiante = 5;
