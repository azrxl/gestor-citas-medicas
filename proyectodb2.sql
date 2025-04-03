-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS `proyectodb2`
    /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

-- Usar la base de datos
USE `proyectodb2`;

-- Eliminar tablas existentes para recrearlas (opcional, para un script completo y limpio)
DROP TABLE IF EXISTS citas;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS permisos;

-- Tabla de roles (sin descripción)
CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       nombre VARCHAR(20) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabla de permisos (sin descripción)
CREATE TABLE permisos (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabla de usuarios (se agregó la columna 'horario_semanal' para el horario del médico y 'frecuencia_cita' para la duración de cada citaLogic)
CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          login VARCHAR(50) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          nombre VARCHAR(50) NOT NULL,
                          apellido VARCHAR(50) NOT NULL,
                          cedula VARCHAR(20) NOT NULL UNIQUE,
                          rol VARCHAR(20) NOT NULL,
                          aprobado BOOLEAN NOT NULL DEFAULT (false),
                          especialidad VARCHAR(100),
                          costo_consulta DECIMAL(10, 2),
                          localidad VARCHAR(100),
                          horario_semanal VARCHAR(255), -- Ej. "8-12,13-17;8-12,13-17;..."
                          frecuencia_cita INT        -- Frecuencia de citasLogic en minutos (definida por el médico)
) ENGINE=InnoDB;

CREATE TABLE citas (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       login_medico VARCHAR(50) NOT NULL,
                       login_paciente VARCHAR(50),
                       fecha DATE NOT NULL,
                       hora_inicio TIME NOT NULL,
                       hora_fin TIME NOT NULL,
                       estado VARCHAR(20) NOT NULL
) ENGINE=InnoDB;

-- Insertar roles de prueba
INSERT INTO roles (nombre) VALUES
                               ('ADMIN'),
                               ('MEDICO'),
                               ('PACIENTE'),
                               ('ANONIMO');

-- Insertar permisos de prueba
INSERT INTO permisos (nombre) VALUES
                                  ('ACCESO_ADMIN'),
                                  ('GESTION_MEDICOS'),
                                  ('GESTION_CITAS'),
                                  ('BUSCAR_MEDICOS');

-- Insertar usuarios de prueba (las contraseñas deben estar encriptadas; se usan dummy hashes)
-- Nota: Para usuarios que no son médicos, 'especialidad', 'costo_consulta', 'horario_semanal' y 'frecuencia_cita' serán NULL.
INSERT INTO usuarios (id, login, password, nombre, apellido, cedula, rol, aprobado, especialidad, costo_consulta, localidad, horario_semanal, frecuencia_cita)
VALUES
    (1, 'admin', '$2a$10$e1qZUgdgrP4.gQNtP8N6ceM/Yxo6V6NF1xCHg.3L3CGYI1BIY3aJi', 'Admin', 'System', '00000000', 'ADMIN', 1, NULL, NULL, NULL, NULL, NULL),
    (2, 'medico1', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Juan', 'Pérez', '11111111', 'MEDICO', 1, 'Cardiología', 150.00, 'San José', '8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;;', 45),
    (3, 'paciente1', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'María', 'González', '22222222', 'PACIENTE', 1, NULL, NULL, NULL, NULL, NULL),
    (4, 'medico2', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Carlos', 'López', '33333333', 'MEDICO', 1, 'Pediatría', 100.00, 'Heredia', '8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;;8-12,13-17;', 30),
    (5, 'paciente2', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Ana', 'Martínez', '44444444', 'PACIENTE', 1, NULL, NULL, NULL, NULL, NULL),
    (6, 'paciente3', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Luis', 'Rodríguez', '55555555', 'PACIENTE', 1, NULL, NULL, NULL, NULL, NULL),
    (7, 'medico3', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Elena', 'Sánchez', '66666666', 'MEDICO', 1, 'Dermatología', 120.00, 'Alajuela', '8-12,13-17;;8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;', 60),
    (8, 'MedSinRegistrar', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Medico', 'Sin Registrar', '77777777', 'MEDICO', 0, NULL, NULL, NULL, NULL, NULL),
    (9, 'medico4', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Medico', 'Aprobado', '888888888', 'MEDICO', 1, NULL, NULL, NULL, NULL, NULL);

INSERT INTO citas (id, login_medico, login_paciente, fecha, hora_inicio, hora_fin, estado)
VALUES
    (1, 'medico1', 'paciente1', '2024-03-20', '10:00:00', '10:45:00', 'ACTIVA'),
    (2, 'medico1', 'paciente1', '2024-03-20', '11:00:00', '11:45:00', 'ACTIVA'),
    (3, 'medico1', 'paciente1', '2024-04-05', '14:00:00', '14:30:00', 'COMPLETADA'),
    (4, 'medico1', 'paciente2', '2024-04-05', '15:00:00', '15:30:00', 'CANCELADA'),
    (5, 'medico2', 'paciente3', '2024-04-10', '09:00:00', '09:30:00', 'PENDIENTE'),
    (6, 'medico3', 'paciente1', '2024-04-12', '16:00:00', '17:00:00', 'CANCELADA'),
    (7, 'medico3', NULL, '2024-04-15', '10:00:00', '11:00:00', 'ACTIVA'),
    (8, 'medico3', 'paciente2', '2024-04-15', '14:00:00', '15:00:00', 'PENDIENTE'),
    (9, 'medico1', 'DISPONIBLE', '2025-04-07', '08:00:00', '08:30:00', 'Disponible'),
    (10, 'medico1', 'DISPONIBLE', '2025-04-07', '08:30:00', '09:00:00', 'Disponible'),
    (11, 'medico1', 'DISPONIBLE', '2025-04-07', '09:00:00', '09:30:00', 'Disponible'),
    (12, 'medico1', 'DISPONIBLE', '2025-04-07', '09:30:00', '10:00:00', 'Disponible'),
    (13, 'medico1', 'DISPONIBLE', '2025-04-07', '13:00:00', '13:30:00', 'Disponible'),
    (14, 'medico1', 'DISPONIBLE', '2025-04-07', '13:30:00', '14:00:00', 'Disponible'),
    (15, 'medico1', 'DISPONIBLE', '2025-04-07', '14:00:00', '14:30:00', 'Disponible'),
    (16, 'medico1', 'DISPONIBLE', '2025-04-07', '14:30:00', '15:00:00', 'Disponible'),
    (17, 'medico1', 'DISPONIBLE', '2025-04-08', '08:00:00', '08:30:00', 'Disponible'),
    (18, 'medico1', 'DISPONIBLE', '2025-04-08', '08:30:00', '09:00:00', 'Disponible'),
    (19, 'medico1', 'DISPONIBLE', '2025-04-08', '09:00:00', '09:30:00', 'Disponible'),
    (20, 'medico1', 'DISPONIBLE', '2025-04-08', '09:30:00', '10:00:00', 'Disponible');