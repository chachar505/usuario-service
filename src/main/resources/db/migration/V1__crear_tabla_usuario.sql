DROP TABLE IF EXISTS usuario;
CREATE TABLE usuario (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         nombre_apellido VARCHAR(100) NOT NULL,
                         nombre_pantalla VARCHAR(100) NOT NULL UNIQUE,
                         email VARCHAR(150) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         billetera DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
                         cuenta_bloqueada BOOLEAN DEFAULT FALSE,
                         anio_registro BIGINT
);