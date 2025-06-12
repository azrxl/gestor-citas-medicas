package cr.ac.una.demologinspringboot.security; // O tu paquete de configuración

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Aplica esta configuración a todas las rutas bajo /api/
                .allowedOrigins("http://localhost:5174", "http://localhost:5173") // Permite peticiones desde tu frontend
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite todos los métodos necesarios
                .allowedHeaders("*") // Permite todas las cabeceras (incluyendo Authorization)
                .allowCredentials(true); // Permite enviar cookies o tokens de autorización
    }
}