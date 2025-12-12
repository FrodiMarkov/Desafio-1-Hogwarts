-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 12-12-2025 a las 08:00:45
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `howarts`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumno_asignatura`
--

CREATE TABLE `alumno_asignatura` (
  `id` int(200) NOT NULL,
  `id_alumno` int(10) NOT NULL,
  `id_asignatura` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `alumno_asignatura`
--

INSERT INTO `alumno_asignatura` (`id`, `id_alumno`, `id_asignatura`) VALUES
(1, 8, 4),
(2, 4, 4),
(3, 6, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alumno_hechizos`
--

CREATE TABLE `alumno_hechizos` (
  `id` int(9) NOT NULL,
  `usuario_id` int(9) NOT NULL,
  `id_hechizo` int(9) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `asignatura`
--

CREATE TABLE `asignatura` (
  `id` int(10) NOT NULL,
  `nombre` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `asignatura`
--

INSERT INTO `asignatura` (`id`, `nombre`) VALUES
(1, 'Hechizos'),
(2, 'Pociones'),
(4, 'Bestias');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `casa`
--

CREATE TABLE `casa` (
  `id` int(1) NOT NULL,
  `Nombre` varchar(20) NOT NULL,
  `Puntos` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `casa`
--

INSERT INTO `casa` (`id`, `Nombre`, `Puntos`) VALUES
(1, 'Gryffindor', 0),
(2, 'Slytherin', 0),
(3, 'Ravenclaw', 0),
(4, 'Hufflepuff', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hechizos`
--

CREATE TABLE `hechizos` (
  `id` int(9) NOT NULL,
  `nombre` text NOT NULL,
  `experiencia` int(20) NOT NULL,
  `descripcion` text NOT NULL,
  `url_icono` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `hechizos`
--

INSERT INTO `hechizos` (`id`, `nombre`, `experiencia`, `descripcion`, `url_icono`) VALUES
(2, 'Accio', 50, 'Descripcion Accio', 'pruebaImagen'),
(3, 'Prueba', 100, 'Prueba1234', 'pruebaImagen'),
(4, 'Prueba2', 1231, 'Prueba2', 'pruebaImagen');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ingredientes`
--

CREATE TABLE `ingredientes` (
  `id` int(9) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `propiedad1` varchar(20) DEFAULT NULL,
  `propiedad2` varchar(20) DEFAULT NULL,
  `propiedad3` varchar(20) DEFAULT NULL,
  `propiedad4` varchar(20) DEFAULT NULL,
  `propiedad5` varchar(20) DEFAULT NULL,
  `propiedad6` varchar(20) DEFAULT NULL,
  `propiedad7` varchar(20) DEFAULT NULL,
  `propiedad8` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `matricula`
--

CREATE TABLE `matricula` (
  `id` int(9) NOT NULL,
  `alumno_id` int(9) NOT NULL,
  `asignatura_id` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pociones`
--

CREATE TABLE `pociones` (
  `id` int(9) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `fecha_creacion` date NOT NULL,
  `re-editada` tinyint(1) NOT NULL,
  `creador` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `profesor_asignatura`
--

CREATE TABLE `profesor_asignatura` (
  `id` int(20) NOT NULL,
  `asignatura_id` int(20) NOT NULL,
  `id_profesor` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `profesor_asignatura`
--

INSERT INTO `profesor_asignatura` (`id`, `asignatura_id`, `id_profesor`) VALUES
(1, 1, 2),
(2, 1, 18);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `nombre` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`) VALUES
(1, 'Alumno'),
(2, 'Profesor'),
(3, 'Admin'),
(4, 'Dumbledore');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles_usuario`
--

CREATE TABLE `roles_usuario` (
  `id` int(20) NOT NULL,
  `usuario_id` int(9) NOT NULL,
  `rol_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `roles_usuario`
--

INSERT INTO `roles_usuario` (`id`, `usuario_id`, `rol_id`) VALUES
(1, 1, 2),
(4, 1, 3),
(5, 1, 4),
(6, 2, 2),
(7, 3, 1),
(8, 4, 1),
(9, 5, 1),
(10, 6, 1),
(11, 7, 1),
(12, 8, 1),
(13, 9, 1),
(14, 10, 1),
(15, 11, 1),
(16, 12, 1),
(17, 13, 1),
(18, 15, 1),
(19, 16, 1),
(21, 2, 3),
(22, 17, 2),
(23, 17, 1),
(24, 18, 3),
(25, 18, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id` int(10) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `contraseña` varchar(30) NOT NULL,
  `experiencia` int(30) NOT NULL,
  `id_casa` int(1) NOT NULL,
  `nivel` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id`, `nombre`, `email`, `contraseña`, `experiencia`, `id_casa`, `nivel`) VALUES
(1, 'Dumbledor', 'Dumbli@dumbledor', 'Dumbi1234', 500, 0, 999),
(2, 'Snape', 'snape@snape', 'snape1234', 400, 0, 3),
(4, 'Pepe2', 'Pepe23@pe', 'pepe1234', 0, 2, 0),
(5, 'Pepe5', 'pe32@pep2', 'pepe123', 0, 3, 0),
(6, 'Harry Potter', 'Harry@harry', 'harry1234', 200, 1, 3),
(7, 'Pepe', 'pepe@mail.com', '1234', 0, 1, 0),
(8, 'Pruebap', 'prueba@', 'prueba1234', 0, 2, 0),
(9, 'Pruebap', 'prueba@', 'prueba1234', 0, 2, 0),
(10, 'Prueba2', 'prueba@', 'prueba124', 0, 2, 0),
(11, 'sadjk', 'ashdlkasjhd', 'hdaslkjh', 0, 1, 0),
(12, 'Pepe', 'pepe@mail.com', '1234', 0, 1, 0),
(13, 'Pepe', '124', '41', 0, 1, 0),
(15, 'Pepe134209', '43312', '43214123', 0, 4, 1),
(16, 'Pepe1342', '43312', '43214123', 0, 3, 1),
(17, 'pepe123', 'pepe1243', '1234', 0, 1, 1),
(18, 'Pepe123', 'Pepe1209387', '1234', 0, 2, 1);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alumno_asignatura`
--
ALTER TABLE `alumno_asignatura`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `asignatura`
--
ALTER TABLE `asignatura`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `casa`
--
ALTER TABLE `casa`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `hechizos`
--
ALTER TABLE `hechizos`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `ingredientes`
--
ALTER TABLE `ingredientes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `matricula`
--
ALTER TABLE `matricula`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `pociones`
--
ALTER TABLE `pociones`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `profesor_asignatura`
--
ALTER TABLE `profesor_asignatura`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `roles_usuario`
--
ALTER TABLE `roles_usuario`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alumno_asignatura`
--
ALTER TABLE `alumno_asignatura`
  MODIFY `id` int(200) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `asignatura`
--
ALTER TABLE `asignatura`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `casa`
--
ALTER TABLE `casa`
  MODIFY `id` int(1) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `hechizos`
--
ALTER TABLE `hechizos`
  MODIFY `id` int(9) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `ingredientes`
--
ALTER TABLE `ingredientes`
  MODIFY `id` int(9) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `matricula`
--
ALTER TABLE `matricula`
  MODIFY `id` int(9) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `pociones`
--
ALTER TABLE `pociones`
  MODIFY `id` int(9) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `profesor_asignatura`
--
ALTER TABLE `profesor_asignatura`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `roles_usuario`
--
ALTER TABLE `roles_usuario`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
