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

-- Tabla de usuarios (se agregó la columna 'horario_semanal' para el horario del médico y 'frecuencia_cita' para la duración de cada cita)
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
                          frecuencia_cita INT        -- Frecuencia de citas en minutos (definida por el médico)
) ENGINE=InnoDB;

-- Tabla de citas para registrar las citas agendadas
-- Tabla de citas para registrar las citas agendadas (sin claves foráneas)
CREATE TABLE citas (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       login_medico VARCHAR(50) NOT NULL,
                       login_paciente VARCHAR(50) NOT NULL,
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
INSERT INTO usuarios (login, password, nombre, apellido, cedula, rol, aprobado, especialidad, costo_consulta, localidad, horario_semanal, frecuencia_cita)
VALUES
    ('admin', '$2a$10$dummyhashadmin', 'Admin', 'System', '00000000', 'ADMIN', true, NULL, NULL, NULL, NULL, NULL),
    ('medico1', '$2a$10$dummyhashmedico', 'Juan', 'Pérez', '11111111', 'MEDICO', true,'Cardiología', 150.00, 'San José', '8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;;', 45),
    ('paciente1', '$2a$10$dummyhashpaciente', 'María', 'González', '22222222', 'PACIENTE', true, NULL, NULL, NULL, NULL, NULL),
    ('medico2', '$2a$10$anotherdummyhash', 'Carlos', 'López', '33333333', 'MEDICO', true, 'Pediatría', 100.00, 'Heredia', '8-12,13-17;8-12,13-17;8-12,13-17;8-12,13-17;;8-12,13-17;', 30);

-- Insertar citas de prueba (sin claves foráneas)
INSERT INTO citas (login_medico, login_paciente, fecha, hora_inicio, hora_fin, estado)
VALUES
    ('medico1', 'paciente1', '2024-03-20', '10:00:00', '10:45:00', 'PENDIENTE'),
    ('medico1', 'paciente1', '2024-03-20', '11:00:00', '11:45:00', 'PENDIENTE'),
    ('medico2', 'paciente1', '2024-04-05', '14:00:00', '14:30:00', 'COMPLETADA'),
    ('medico2', 'paciente1', '2024-04-05', '15:00:00', '15:30:00', 'CANCELADA');