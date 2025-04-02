Author Jose Alberto Vindas Quiros

Explicación

Este proyecto es una aplicación web basada en Spring Boot, Spring Security, Thymeleaf y H2 Database,
que implementa un sistema de autenticación y registro de usuarios. La aplicación permite a los usuarios
registrarse, iniciar sesión y acceder a una página protegida solo si están autenticados. Además,
utiliza sesiones del lado del servidor para mantener el estado del usuario.

Los usuarios se almacenan en una base de datos en memoria H2, con contraseñas encriptadas usando BCrypt.
Se han implementado validaciones en los formularios utilizando Thymeleaf para asegurar que los datos ingresados
sean correctos antes de enviarlos.

La seguridad está gestionada por Spring Security, configurando rutas públicas y protegidas. La página de login y
registro son accesibles para cualquier usuario, mientras que la página principal (`/home`) solo es visible para
usuarios autenticados. También se ha habilitado la consola H2 (`/h2-console`) para inspeccionar la base de datos
directamente.

Adicionalmente, el proyecto incluye una funcionalidad para insertar usuarios "hardcoded" al iniciar la aplicación,
asegurando que existan credenciales por defecto para pruebas. Toda la aplicación sigue una arquitectura basada en
MVC (Model-View-Controller) y usa Jakarta EE en lugar de javax.

Se omite la estructura a 3 capas por motivos de simplicidad únicamente.


Usuarios hardcoded

Intenta iniciar sesión en http://localhost:8080/login con:
Usuario: admin / Contraseña: admin123
Usuario: usuario / Contraseña: user123

Páginas
http://localhost:8080/login
http://localhost:8080/home
http://localhost:8080/registro


Acceso a H2
http://localhost:8080/h2-console

URL         jdbc:h2:mem:testdb
Usuario     sa
Password    password

Consulta
select * from usuarios










