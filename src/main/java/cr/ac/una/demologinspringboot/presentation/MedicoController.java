package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.MonthlyCitasGenerator;
import cr.ac.una.demologinspringboot.logic.service.Service;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/medico")
public class MedicoController {

    private final Service service;
    private final MonthlyCitasGenerator citasGenerator;

    public MedicoController(Service service, MonthlyCitasGenerator citasGenerator) {
        this.service = service;
        this.citasGenerator = citasGenerator;
    }

    @GetMapping("/completar")
    public String perfil(HttpSession session, Model model) {
        // Obtener el usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : "";

        // Buscar el usuario (médico) en la base de datos
        Usuario medico = service.findByLogin(username);

        // Agregarlo al modelo para pre-poblar el formulario
        model.addAttribute("usuario", medico);
        List<String> diasSemana = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        model.addAttribute("diasSemana", diasSemana);
        return "medico/completar"; // Devuelve la vista completar.html
    }



    @PostMapping("completar")
    public String actualizarPerfil(Usuario usuario, HttpServletRequest request, Model model) {
        Usuario usuarioLogeado = service.findByLogin(usuario.getLogin());
        StringBuilder scheduleBuilder = horarioFormatParser(request);

        usuarioLogeado.setEspecialidad(usuario.getEspecialidad());
        usuarioLogeado.setCostoConsulta(usuario.getCostoConsulta());
        usuarioLogeado.setLocalidad(usuario.getLocalidad());
        usuarioLogeado.setHorarioSemanal(scheduleBuilder.toString());
        usuarioLogeado.setFrecuenciaCita(usuario.getFrecuenciaCita());
        service.actualizarUsuario(usuarioLogeado);

        return "redirect:/medico/citas";
    }

    @GetMapping("/citas")
    public String citas(HttpSession session, Model model) {
        // Obtener el usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : "";

        // Agregar el nombre de usuario al modelo (si es necesario)
        model.addAttribute("username", username);

        // Obtener las citas activas para el médico
        List<Cita> citasActivas = service.findCitasActivasByMedico(username);
        model.addAttribute("citasActivas", citasActivas);

        return "medico/citas"; // La vista que mostrará las citas
    }

    public StringBuilder horarioFormatParser(HttpServletRequest request) {
        // Convertir los horarios de atención a una cadena (horarioSemanal)
        List<String> diasSemana = Arrays.asList("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo");
        StringBuilder scheduleBuilder = new StringBuilder();

        for (String dia : diasSemana) {
            // Leer los inputs del día actual
            String mananaInicio = request.getParameter(dia + "MananaInicio");
            String mananaFin = request.getParameter(dia + "MananaFin");
            String tardeInicio = request.getParameter(dia + "TardeInicio");
            String tardeFin = request.getParameter(dia + "TardeFin");

            List<String> intervals = new ArrayList<>();

            // Si existen datos para el turno mañana, se formatea el intervalo
            if (mananaInicio != null && !mananaInicio.isEmpty() && mananaFin != null && !mananaFin.isEmpty()) {
                intervals.add(formatInterval(mananaInicio, mananaFin));
            }
            // Si existen datos para el turno tarde, se formatea el intervalo
            if (tardeInicio != null && !tardeInicio.isEmpty() && tardeFin != null && !tardeFin.isEmpty()) {
                intervals.add(formatInterval(tardeInicio, tardeFin));
            }

            // Se unen los intervalos con coma y se añade un punto y coma al final (incluso si el día no tiene horario)
            String daySchedule = String.join(",", intervals);
            scheduleBuilder.append(daySchedule).append(";");
        }
        return scheduleBuilder;
    }

    // Métdo auxiliar para formatear un intervalo de tiempo
    private String formatInterval(String inicio, String fin) {
        // Se asume que el input es del tipo "HH:mm", por ejemplo, "08:00" y "12:00"
        // Extraemos solo la hora y eliminamos ceros a la izquierda si se desea
        String hInicio = inicio.split(":")[0];
        String hFin = fin.split(":")[0];
        return hInicio + "-" + hFin;
    }
}