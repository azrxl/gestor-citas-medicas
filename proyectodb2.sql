-- Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS `proyectodb`
    /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;

USE `proyectodb`;

DROP TABLE IF EXISTS citas;
DROP TABLE IF EXISTS usuarios;

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
                          horario_semanal VARCHAR(255),
                          frecuencia_cita INT
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

INSERT INTO usuarios (id, login, password, nombre, apellido, cedula, rol, aprobado, especialidad, costo_consulta, localidad, horario_semanal, frecuencia_cita)
VALUES
    (1, 'admin', '$2a$10$cHzCm9Y/ost2IWN3unUEZ.abod.avA1uRon2qTDYXfBjNNgseiplm', 'Admin', 'System', '00000000', 'ADMIN', 1, NULL, NULL, NULL, NULL, NULL),
    (2, 'medico1', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Juan', 'Pérez', '11111111', 'MEDICO', 1, 'Cardiología', 150.00, 'San José', '8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;;', 45),
    (3, 'paciente1', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'María', 'González', '22222222', 'PACIENTE', 1, NULL, NULL, NULL, NULL, NULL),
    (4, 'medico2', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Carlos', 'López', '33333333', 'MEDICO', 1, 'Pediatría', 100.00, 'Heredia', '8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;;8-12,13-17;', 30),
    (5, 'paciente2', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Ana', 'Martínez', '44444444', 'PACIENTE', 1, NULL, NULL, NULL, NULL, NULL),
    (6, 'paciente3', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Luis', 'Rodríguez', '55555555', 'PACIENTE', 1, NULL, NULL, NULL, NULL, NULL),
    (7, 'medico3', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Elena', 'Sánchez', '66666666', 'MEDICO', 1, 'Dermatología', 120.00, 'Alajuela', '8-12,13-17;;8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;', 60),
    (8, 'MedSinRegistrar', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Medico', 'Sin Registrar', '77777777', 'MEDICO', 0, NULL, NULL, NULL, NULL, NULL),
    (9, 'medico4', '$2a$10$zSVtlQv14GAfeKX7cFzm4Oj/SstfcFRYFFPVq8ER4YmnbGMJmQ/8.', 'Medico', 'Aprobado', '888888888', 'MEDICO', 1, NULL, NULL, NULL, NULL, NULL),
    (10, 'password', '$2a$10$1LTnNflwLMSvJeQcaKeJDu/T53nWO5EirGqZzozlY5tp8rrdvALEi', 'CEO', 'Sexo', '1234567890', 'MEDICO', 0, NULL, NULL, NULL, NULL, NULL),
    (11, 'a', '$2a$10$cHzCm9Y/ost2IWN3unUEZ.abod.avA1uRon2qTDYXfBjNNgseiplm', 'a', 'a', '111', 'MEDICO', 1, 'Memes', 12500.00, 'San Carlos', '10-00,13-15;;;;;;;', 20),
    (12, 'b', '$2a$10$2AgfJRy1ULAhT1kfZs9eYOywq3yr9xaENEzP3SrcwIyTScAIYcYoy', 'b', 'b', '222', 'PACIENTE', 1, NULL, NULL, NULL, NULL, NULL);

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
    (20, 'medico1', 'DISPONIBLE', '2025-04-08', '09:30:00', '10:00:00', 'Disponible'),
    (21, 'a', 'a', '2025-06-09', '13:00:00', '13:20:00', 'COMPLETADA'),
    (22, 'a', 'b', '2025-06-09', '13:20:00', '13:40:00', 'CANCELADA'),
    (23, 'a', 'DISPONIBLE', '2025-06-09', '13:40:00', '14:00:00', 'ACTIVA'),
    (24, 'a', 'DISPONIBLE', '2025-06-09', '14:00:00', '14:20:00', 'ACTIVA'),
    (25, 'a', 'DISPONIBLE', '2025-06-09', '14:20:00', '14:40:00', 'ACTIVA'),
    (26, 'a', 'b', '2025-06-09', '14:40:00', '15:00:00', 'COMPLETADA'),
    (27, 'a', 'DISPONIBLE', '2025-06-16', '13:00:00', '13:20:00', 'ACTIVA'),
    (28, 'a', 'DISPONIBLE', '2025-06-16', '13:20:00', '13:40:00', 'ACTIVA'),
    (29, 'a', 'DISPONIBLE', '2025-06-16', '13:40:00', '14:00:00', 'ACTIVA'),
    (30, 'a', 'DISPONIBLE', '2025-06-16', '14:00:00', '14:20:00', 'ACTIVA'),
    (31, 'a', 'DISPONIBLE', '2025-06-16', '14:20:00', '14:40:00', 'ACTIVA'),
    (32, 'a', 'DISPONIBLE', '2025-06-16', '14:40:00', '15:00:00', 'ACTIVA'),
    (33, 'a', 'DISPONIBLE', '2025-06-23', '13:00:00', '13:20:00', 'ACTIVA'),
    (34, 'a', 'DISPONIBLE', '2025-06-23', '13:20:00', '13:40:00', 'ACTIVA'),
    (35, 'a', 'DISPONIBLE', '2025-06-23', '13:40:00', '14:00:00', 'ACTIVA'),
    (36, 'a', 'DISPONIBLE', '2025-06-23', '14:00:00', '14:20:00', 'ACTIVA'),
    (37, 'a', 'DISPONIBLE', '2025-06-23', '14:20:00', '14:40:00', 'ACTIVA'),
    (38, 'a', 'DISPONIBLE', '2025-06-23', '14:40:00', '15:00:00', 'ACTIVA'),
    (39, 'a', 'DISPONIBLE', '2025-06-30', '13:00:00', '13:20:00', 'ACTIVA'),
    (40, 'a', 'DISPONIBLE', '2025-06-30', '13:20:00', '13:40:00', 'ACTIVA'),
    (41, 'a', 'DISPONIBLE', '2025-06-30', '13:40:00', '14:00:00', 'ACTIVA'),
    (42, 'a', 'DISPONIBLE', '2025-06-30', '14:00:00', '14:20:00', 'ACTIVA'),
    (43, 'a', 'DISPONIBLE', '2025-06-30', '14:20:00', '14:40:00', 'ACTIVA'),
    (44, 'a', 'DISPONIBLE', '2025-06-30', '14:40:00', '15:00:00', 'ACTIVA'),
    (45, 'a', 'DISPONIBLE', '2025-07-07', '13:00:00', '13:20:00', 'ACTIVA'),
    (46, 'a', 'DISPONIBLE', '2025-07-07', '13:20:00', '13:40:00', 'ACTIVA'),
    (47, 'a', 'DISPONIBLE', '2025-07-07', '13:40:00', '14:00:00', 'ACTIVA'),
    (48, 'a', 'DISPONIBLE', '2025-07-07', '14:00:00', '14:20:00', 'ACTIVA'),
    (49, 'a', 'DISPONIBLE', '2025-07-07', '14:20:00', '14:40:00', 'ACTIVA'),
    (50, 'a', 'DISPONIBLE', '2025-07-07', '14:40:00', '15:00:00', 'ACTIVA');