-- MariaDB dump 10.19  Distrib 10.4.32-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: gestion_titulacion_suiza
-- ------------------------------------------------------
-- Server version	10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `gestion_titulacion_suiza`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `gestion_titulacion_suiza` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci */;

USE `gestion_titulacion_suiza`;

--
-- Table structure for table `catalogos`
--

DROP TABLE IF EXISTS `catalogos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `catalogos` (
  `id_catalogo` int(11) NOT NULL AUTO_INCREMENT,
  `tipo_catalogo` varchar(50) NOT NULL,
  `nombre_item` varchar(150) NOT NULL,
  `codigo_item` varchar(50) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'ACTIVO',
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_catalogo`),
  KEY `idx_tipo_estado` (`tipo_catalogo`,`estado`),
  KEY `idx_nombre` (`nombre_item`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `catalogos`
--

LOCK TABLES `catalogos` WRITE;
/*!40000 ALTER TABLE `catalogos` DISABLE KEYS */;
INSERT INTO `catalogos` VALUES (1,'PROGRAMA_ESTUDIO','ADMINISTRACION DE OPERACIONES TURÍSTICAS','AOT','Carrera Técnica Profesional de 3 años.','ACTIVO','2026-07-08 18:05:38'),(2,'PROGRAMA_ESTUDIO','ASISTENCIA ADMINISTRATIVA','AA','Carrera Técnica Profesional de 3 años.','ACTIVO','2026-07-08 18:05:38'),(3,'PROGRAMA_ESTUDIO','CONTABILIDAD','CONT','Carrera orientada a sistemas financieros y auditoría contable.','ACTIVO','2026-07-08 18:05:38'),(4,'PROGRAMA_ESTUDIO','CONSTRUCCIÓN CIVIL','CC','Carrera Técnica Profesional de 3 años.','ACTIVO','2026-07-08 18:05:38'),(5,'PROGRAMA_ESTUDIO','GESTIÓN ADMINISTRATIVA','GA','Gestión empresarial y finanzas organizacionales.','ACTIVO','2026-07-08 18:05:38'),(6,'PROGRAMA_ESTUDIO','DESARROLLO DE SISTEMAS DE INFORMACIÓN','DSI','Carrera Tecnológica de 3 años orientada a software y bases de datos.','ACTIVO','2026-07-08 18:05:38'),(7,'PROGRAMA_ESTUDIO','ELECTRICIDAD INDUSTRIAL','EI','Carrera Técnica Profesional de 3 años.','ACTIVO','2026-07-08 18:05:38'),(8,'PROGRAMA_ESTUDIO','ENFERMERÍA TÉCNICA','ENF','Carrera de salud y asistencia clínica asistencial.','ACTIVO','2026-07-08 18:05:38'),(9,'PROGRAMA_ESTUDIO','MANEJO FORESTAL','MF','Carrera Técnica Profesional de 3 años.','ACTIVO','2026-07-08 18:05:38'),(10,'PROGRAMA_ESTUDIO','MECATRÓNICA AUTOMOTRIZ','MA','Carrera Técnica Profesional de 3 años.','ACTIVO','2026-07-08 18:05:38'),(11,'PROGRAMA_ESTUDIO','PRODUCCIÓN AGROPECUARIA','PA','Carrera Técnica Profesional de 3 años.','ACTIVO','2026-07-08 18:05:38'),(12,'MODALIDAD_TITULACION','Tesis o Proyecto de Aplicación Profesional','MOD-INVEST','Tesis o trabajo de aplicación profesional con desarrollo tecnológico.','ACTIVO','2026-07-08 18:05:38'),(13,'MODALIDAD_TITULACION','Examen de Suficiencia Profesional','MOD-SUFIC','Evaluación teórico-práctica para egresados de la institución.','ACTIVO','2026-07-08 18:05:38'),(14,'MODALIDAD_TITULACION','Perfil del Proyecto','MOD-PERFIL','Propuesta inicial o anteproyecto técnico sustentado ante jurado.','ACTIVO','2026-07-08 18:05:38'),(15,'ASESOR','Ing. Ruber Torres Arevalo','ASE-001','Especialista en Inteligencia Artificial y Gestión de Sistemas.','ACTIVO','2026-07-08 18:05:38'),(16,'ASESOR','Lic. Carlos Mendoza','ASE-002','Especialista en Bases de Datos y Arquitectura de Software.','ACTIVO','2026-07-08 18:05:38'),(17,'ASESOR','Dra. María Gonzales','ASE-003','Especialista en Metodología de la Investigación.','ACTIVO','2026-07-08 18:05:38'),(18,'ASESOR','Ing. Fernando Gómez López','ASE-004','Especialista en Redes, Seguridad de la Información y Cloud.','ACTIVO','2026-07-08 18:05:38'),(19,'ESTADO_PROYECTO','EN_REVISION','EST-REV','Proyecto bajo evaluación del Coordinador Académico.','ACTIVO','2026-07-08 18:05:38'),(20,'ESTADO_PROYECTO','APROBADO_COORDINACION','EST-APROB-COORD','Proyecto visado por Coordinación, pasa a Jefatura de Investigación.','ACTIVO','2026-07-08 18:05:38'),(21,'ESTADO_PROYECTO','OBSERVADO','EST-OBS','Proyecto devuelto con observaciones para subsanación.','ACTIVO','2026-07-08 18:05:38'),(22,'ESTADO_PROYECTO','APROBADO_FINAL','EST-APROB-JEFE','Proyecto aprobado finalmente para sustentar o recibir título.','ACTIVO','2026-07-08 18:05:38'),(23,'ESTADO_PROYECTO','RECHAZADO','EST-RECH','Proyecto rechazado por incumplimiento o duplicidad.','ACTIVO','2026-07-08 18:05:38');
/*!40000 ALTER TABLE `catalogos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiantes`
--

DROP TABLE IF EXISTS `estudiantes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `estudiantes` (
  `id_estudiante` int(11) NOT NULL AUTO_INCREMENT,
  `id_proyecto` int(11) NOT NULL,
  `dni_codigo` varchar(15) NOT NULL,
  `nombres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `codigo_estudiante` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id_estudiante`),
  KEY `idx_proyecto_est` (`id_proyecto`),
  KEY `idx_dni` (`dni_codigo`),
  CONSTRAINT `fk_est_proyecto` FOREIGN KEY (`id_proyecto`) REFERENCES `proyectos` (`id_proyecto`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiantes`
--

LOCK TABLES `estudiantes` WRITE;
/*!40000 ALTER TABLE `estudiantes` DISABLE KEYS */;
INSERT INTO `estudiantes` VALUES (1,1,'72345678','Angelo','Panaifo','apanaifo@est.iestpsuiza.edu.pe','955443322','EST-2023001'),(2,1,'73456789','Isak','Rioja','irioja@est.iestpsuiza.edu.pe','966554433','EST-2023002'),(3,1,'74567890','Noelia','Bolaños','nbolanos@est.iestpsuiza.edu.pe','977665544','EST-2023003'),(4,2,'71234567','Kevin','Salas Morán','ksalas@est.iestpsuiza.edu.pe','911882233','EST-2022088'),(5,2,'70123456','María','López García','mlopez@est.iestpsuiza.edu.pe','922773344','EST-2022089'),(6,3,'48901234','Jorge','Castro Vela','jcastro@est.iestpsuiza.edu.pe','933664455','EST-2021015');
/*!40000 ALTER TABLE `estudiantes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `observaciones`
--

DROP TABLE IF EXISTS `observaciones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `observaciones` (
  `id_observacion` int(11) NOT NULL AUTO_INCREMENT,
  `id_proyecto` int(11) DEFAULT NULL,
  `id_usuario` int(11) DEFAULT NULL,
  `rol_autor` varchar(50) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha_observacion` datetime DEFAULT current_timestamp(),
  `estado_observacion` varchar(50) DEFAULT 'PENDIENTE',
  PRIMARY KEY (`id_observacion`),
  KEY `idx_proy_obs` (`id_proyecto`),
  KEY `idx_usu_obs` (`id_usuario`),
  KEY `idx_fecha_obs` (`fecha_observacion`),
  CONSTRAINT `fk_obs_proyecto` FOREIGN KEY (`id_proyecto`) REFERENCES `proyectos` (`id_proyecto`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `observaciones`
--

LOCK TABLES `observaciones` WRITE;
/*!40000 ALTER TABLE `observaciones` DISABLE KEYS */;
INSERT INTO `observaciones` VALUES (1,1,2,'COORDINADOR','Falta especificar el diagrama de arquitectura y el cronograma de Gantt del desarrollo de software.','2026-06-28 13:05:38','SUBSANADO'),(2,2,2,'COORDINADOR','El planteamiento del problema debe delimitarse geogr?ficamente a la ciudad de Pucallpa.','2026-05-08 13:05:38','SUBSANADO'),(3,3,3,'JEFE_INVESTIGACION','El presupuesto presentado supera el l?mite del programa. Ajustar cotizaci?n de hardware.','2026-06-08 13:05:38','PENDIENTE'),(4,3,2,'COORDINADOR','El instrumento de recolecci?n de datos no cuenta con el certificado de validaci?n por juicio de expertos.','2025-07-08 13:05:38','PENDIENTE');
/*!40000 ALTER TABLE `observaciones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyectos`
--

DROP TABLE IF EXISTS `proyectos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proyectos` (
  `id_proyecto` int(11) NOT NULL AUTO_INCREMENT,
  `codigo_proyecto` varchar(50) NOT NULL,
  `titulo` varchar(255) NOT NULL,
  `programa_estudio` varchar(150) NOT NULL,
  `modalidad` varchar(150) NOT NULL,
  `asesor` varchar(150) DEFAULT NULL,
  `estado` varchar(100) NOT NULL,
  `fecha_registro` date NOT NULL,
  `fecha_actualizacion` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `resumen` text DEFAULT NULL,
  `id_usuario_registro` int(11) DEFAULT NULL,
  `archivo_nombre` varchar(255) DEFAULT NULL,
  `archivo_data` longblob DEFAULT NULL,
  PRIMARY KEY (`id_proyecto`),
  UNIQUE KEY `codigo_proyecto` (`codigo_proyecto`),
  KEY `idx_codigo` (`codigo_proyecto`),
  KEY `idx_programa` (`programa_estudio`),
  KEY `idx_modalidad` (`modalidad`),
  KEY `idx_asesor` (`asesor`),
  KEY `idx_estado_proj` (`estado`),
  KEY `idx_fecha` (`fecha_registro`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyectos`
--

LOCK TABLES `proyectos` WRITE;
/*!40000 ALTER TABLE `proyectos` DISABLE KEYS */;
INSERT INTO `proyectos` VALUES (1,'PROY-SUIZA-2026-001','Sistema Inteligente de Gestión Hospitalaria y Control de Triage MediConnect en IESTP Suiza','DESARROLLO DE SISTEMAS DE INFORMACIÓN','Tesis o Proyecto de Aplicación Profesional','Ing. Ruber Torres Arevalo','EN_REVISION','2026-06-23','2026-07-08 18:07:04','Desarrollo de un sistema de control hospitalario con arquitectura All-in-One y JavaFX.',1,NULL,NULL),(2,'PROY-SUIZA-2026-042','Plataforma Web para el Control de Almacén e Inventarios con Código de Barras en Pucallpa','DESARROLLO DE SISTEMAS DE INFORMACIÓN','Examen de Suficiencia Profesional','Lic. Carlos Mendoza','APROBADO_COORDINACION','2026-04-08','2026-07-08 18:07:04','Implementaci?n de sistema web para reducir p?rdidas en el almac?n central de la instituci?n.',1,NULL,NULL),(3,'PROY-SUIZA-2025-018','Evaluación de los Procesos Contables y Auditoría Digital en Mypes de la Región Ucayali','CONTABILIDAD','Tesis o Proyecto de Aplicaci?n Profesional','Dra. Mar?a Gonzales','OBSERVADO','2025-05-08','2026-07-08 18:07:04','Investigaci?n de impacto tributario y digitalizaci?n contable en empresas locales.',1,NULL,NULL),(4,'PROY-SUIZA-2023-005','Sistema Móvil para el Segregado de Residuos Hospitalarios y Protocolos de Bioseguridad','ENFERMERÍA TÉCNICA','Examen de Suficiencia Profesional','Ing. Ruber Torres Arevalo','APROBADO_FINAL','2022-07-08','2026-07-08 18:07:04','App m?vil para control de protocolos de bioseguridad en centros de salud.',1,NULL,NULL);
/*!40000 ALTER TABLE `proyectos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usuarios` (
  `id_usuario` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `password_claro` varchar(100) NOT NULL,
  `nombre_completo` varchar(150) NOT NULL,
  `rol` varchar(50) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `ticket_soporte` varchar(50) DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'ACTIVO',
  `fecha_registro` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_username` (`username`),
  KEY `idx_rol` (`rol`),
  KEY `idx_estado` (`estado`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'secretaria','secretaria123','secretaria123','Noelia Bolaños (Atención y Registro)','SECRETARIA','secretaria@iestpsuiza.edu.pe','987654321',NULL,'ACTIVO','2026-07-08 18:05:38'),(2,'coordinador','coordinador123','coordinador123','Mg. Ruber Torres Arevalo (Coordinación)','COORDINADOR','rtorres@iestpsuiza.edu.pe','988776655',NULL,'ACTIVO','2026-07-08 18:05:38'),(3,'jefe','jefe123','jefe123','Dr. Alejandro Barbaran (Jefatura Investigación)','JEFE_INVESTIGACION','jefatura@iestpsuiza.edu.pe','999888777',NULL,'ACTIVO','2026-07-08 18:05:38'),(4,'secretaria2','secretaria123','secretaria123','Isak Rioja (Soporte Titulación)','SECRETARIA','irioja@iestpsuiza.edu.pe','911223344',NULL,'ACTIVO','2026-07-08 18:05:38');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-07-09  0:29:31
