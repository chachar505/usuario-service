-- Insertar usuarios de prueba
INSERT INTO usuario (
    nombre_apellido,
    nombre_pantalla,
    email,
    password,
    billetera,
    cuenta_bloqueada,
    fecha_registro
) VALUES
      (
          'Juan Perez',
          'JuanitoGamer',
          'juan.perez@gmail.com',
          'password123',
          150.00,
          0,
          2024
      ),
      (
          'Maria Garcia',
          'MariaPro',
          'm.garcia@gmail.com',
          'segura456',
          50.00,
          0,
          2023
      ),
      (
          'Lucas Gomez',
          'LucasSky',
          'lucas.gomez@gmail.com',
          'admin789',
          0.00,
          1,
          2024
      ),
      (
          'Admin Sistema',
          'SuperAdmin',
          'admin.gameup@gmail.com',
          'root2026',
          1000.00,
          0,
          2026
      );