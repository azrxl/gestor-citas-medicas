package cr.ac.una.demologinspringboot.presentation;

import cr.ac.una.demologinspringboot.dto.AppointmentBlock;
import cr.ac.una.demologinspringboot.logic.entities.Cita;
import cr.ac.una.demologinspringboot.logic.entities.Usuario;
import cr.ac.una.demologinspringboot.logic.service.Service;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class UsuarioController {

    private final Service service;

    public UsuarioController(Service service) {
        this.service = service;
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid Usuario usuario, BindingResult result, Model model,
                                   @RequestParam("confirmPassword") String confirmPassword) {
        if (result.hasErrors()) {
            return "registro";  // Si hay errores, volver al formulario
        }
        if (!usuario.getPassword().equals(confirmPassword)) {
            model.addAttribute("confirmPasswordError", "Las contraseñas no coinciden.");
            return "registro";
        }
        service.registrarUsuario(usuario);

        return "redirect:/login";  // Redirigir al login después de registrarse
    }

    @GetMapping({"/", "/home", "/buscar"})
    public String home(HttpSession session, Model model,
                       @RequestParam(name = "especialidad", required = false) String especialidad,
                       @RequestParam(name = "ciudad", required = false) String ciudad) {

        // Obtener el usuario autenticado
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : "";

        // Guardar el usuario en sesión si aún no está
        if (session.getAttribute("username") == null) {
            session.setAttribute("username", username);
        }

        // Aquí determinamos si el usuario es administrador.
        Usuario usuarioLogeado = service.findByLogin(username);
        if (usuarioLogeado != null) {
            if ("ADMIN".equals(usuarioLogeado.getRol())) {
                // Si es administrador, redirige a la vista de médicos NO aprobados
                return "redirect:/admin/home";
            } else if ("MEDICO".equals(usuarioLogeado.getRol())) {
                // Si es médico, verificamos si su perfil está completo
                if (perfilIncompleto(usuarioLogeado)) {
                    // Redirigir a la página de perfil para completar los datos
                    return "redirect:/medico/completar";
                }
            }
        }
        // Guardar en la sesión los filtros ingresados
        guardarFiltrosEnSesionHome(session, especialidad, ciudad);
        // Renderizar la página home con los filtros
        renderizarHome(session, model);
        return "home";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model,
                         @RequestParam(name = "estado", required = false) String estado,
                         @RequestParam(name = "paciente", required = false) String paciente,
                         @RequestParam(name = "doctor", required = false) String doctor) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername() : principal.toString();
        Usuario usuarioLogeado = service.findByLogin(username);
        model.addAttribute("usuario", usuarioLogeado);

        // Declarar la lista de citas que se usará en ambos casos
        List<Cita> citasHistoricas;

        if ("MEDICO".equals(usuarioLogeado.getRol())) {
            // Para el médico, se muestran las citas que no están activas.
            // Además se filtra opcionalmente por paciente.
            guardarFiltrosEnSesionPerfil(session, estado, paciente);
            String filtroEstado = (String) session.getAttribute("estado");
            String filtroPaciente = (String) session.getAttribute("paciente");

            List<Cita> citas = service.findCitasByLoginMedico(usuarioLogeado.getLogin());
            citasHistoricas = citas.stream()
                    .filter(c -> !"ACTIVA".equals(c.getEstado()))
                    .collect(Collectors.toList());
            if (filtroEstado != null && !filtroEstado.isEmpty()) {
                citasHistoricas = citasHistoricas.stream()
                        .filter(c -> filtroEstado.equals(c.getEstado()))
                        .collect(Collectors.toList());
            }
            if (filtroPaciente != null && !filtroPaciente.isEmpty()) {
                citasHistoricas = citasHistoricas.stream()
                        .filter(c -> c.getLoginPaciente() != null &&
                                c.getLoginPaciente().toLowerCase().contains(filtroPaciente.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else if ("PACIENTE".equals(usuarioLogeado.getRol())) {
            // Para el paciente, se muestran todas las citas (canceladas, confirmadas, etc.),
            // pero también se filtra opcionalmente por doctor.
            guardarFiltrosEnSesionPaciente(session, estado, doctor);
            String filtroEstado = (String) session.getAttribute("estado");
            String filtroDoctor = (String) session.getAttribute("doctor");

            List<Cita> citas = service.findCitasByLoginPaciente(usuarioLogeado.getLogin());
            citasHistoricas = citas.stream()
                    .filter(c -> !"ACTIVA".equals(c.getEstado()))
                    .collect(Collectors.toList());
            if (filtroEstado != null && !filtroEstado.isEmpty()) {
                citasHistoricas = citasHistoricas.stream()
                        .filter(c -> filtroEstado.equals(c.getEstado()))
                        .collect(Collectors.toList());
            }
            if (filtroDoctor != null && !filtroDoctor.isEmpty()) {
                citasHistoricas = citasHistoricas.stream()
                        .filter(c -> c.getLoginMedico() != null &&
                                c.getLoginMedico().toLowerCase().contains(filtroDoctor.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else {
            citasHistoricas = new ArrayList<>();
        }

        // Convertir el histórico de citas en bloques agrupados por fecha
        Map<LocalDate, List<Cita>> citasPorFecha = obtenerHistoricoCitas(citasHistoricas);
        List<AppointmentBlock> blocks = convertirABlocks(citasPorFecha);
        model.addAttribute("historicoCitas", blocks);

        return "perfil";
    }

    private Map<LocalDate, List<Cita>> obtenerHistoricoCitas(List<Cita> citas) {
        return citas.stream()
                .filter(c -> !"ACTIVA".equals(c.getEstado()))
                .collect(Collectors.groupingBy(Cita::getFecha, TreeMap::new, Collectors.toList()));
    }
    private List<AppointmentBlock> convertirABlocks(Map<LocalDate, List<Cita>> citasPorFecha) {
        return citasPorFecha.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new AppointmentBlock(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    @GetMapping("/perfilMedico/{medicoId}")
    public String perfilMedico(@PathVariable Long medicoId, HttpSession session, Model model) {

        // Buscar el médico por su ID
        Optional<Usuario> medico = service.findUsuarioById(medicoId);
        // Recuperar todas las citas del médico (sin limitar)
        List<Cita> citas = List.of();
        if (medico.isPresent()) {
            citas = service.findCitasByLoginMedico(medico.get().getLogin());
        }
        // Agrupar todas las citas por fecha (ordenadas ascendentemente)
        Map<LocalDate, List<Cita>> citasPorFecha = citas.stream()
                .collect(Collectors.groupingBy(
                        Cita::getFecha,
                        TreeMap::new,  // Para mantener el orden ascendente
                        Collectors.toList()));

        // Convertir el mapa en una lista de bloques (cada bloque contiene la fecha y las citas de ese día)
        List<AppointmentBlock> blocks = citasPorFecha.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new AppointmentBlock(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        model.addAttribute("medico", medico.get());
        model.addAttribute("citasBlocks", blocks);
        model.addAttribute("username", session.getAttribute("username"));

        return "perfilMedico"; // La vista que mostrará el perfil del médico
    }

    private void guardarFiltrosEnSesionHome(HttpSession session, String especialidad, String ciudad) {
        session.setAttribute("especialidad", especialidad != null ? especialidad : session.getAttribute("especialidad"));
        session.setAttribute("ciudad", ciudad != null ? ciudad : session.getAttribute("ciudad"));
    }

    private void guardarFiltrosEnSesionPerfil(HttpSession session, String estado, String paciente) {
        session.setAttribute("estado", estado != null ? estado : session.getAttribute("estado"));
        session.setAttribute("paciente", paciente != null ? paciente : session.getAttribute("paciente"));
    }

    private void guardarFiltrosEnSesionPaciente(HttpSession session, String estado, String doctor) {
        session.setAttribute("estado", estado != null ? estado : session.getAttribute("estado"));
        session.setAttribute("doctor", doctor != null ? doctor : session.getAttribute("doctor"));
    }

    private void renderizarHome(HttpSession session, Model model) {
        String especialidad = (String) session.getAttribute("especialidad");
        String ciudad = (String) session.getAttribute("ciudad");

        List<Usuario> medicos = obtenerMedicosFiltrados(especialidad, ciudad);
        List<String> especialidades = service.findUsuarioDistinctEspecialidad();
        List<String> ciudades = service.findUsuarioDistinctLocalidades();

        // Estados permitidos para mostrar
        List<String> estadosPermitidos = List.of("ACTIVA");

        // Mapa para almacenar los bloques de citas por médico (solo las 3 fechas más cercanas)
        Map<Long, List<AppointmentBlock>> activeCitas = new HashMap<>();

        // Nueva lista para almacenar los médicos que tienen citas activas
        List<Usuario> medicosConCitas = new ArrayList<>();

        for (Usuario medico : medicos) {
            // Solo médicos con perfil completo tienen horario definido
            if (perfilIncompleto(medico)) {
                continue;
            }
            List<Cita> citas = service.findCitasByLoginMedico(medico.getLogin());

            // Filtrar las citas según el estado permitido
            List<Cita> citasFiltradas = citas.stream()
                    .filter(c -> estadosPermitidos.contains(c.getEstado()))
                    .toList();

            // Agrupar por fecha
            Map<LocalDate, List<Cita>> citasPorFecha = citasFiltradas.stream()
                    .collect(Collectors.groupingBy(Cita::getFecha));

            // Ordenar las fechas y tomar los 3 bloques más cercanos
            List<AppointmentBlock> blocks = citasPorFecha.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())  // orden ascendente por fecha
                    .map(entry -> new AppointmentBlock(entry.getKey(), entry.getValue()))
                    .limit(3)
                    .collect(Collectors.toList());

            // Si hay bloques de citas, añadir el médico a la lista y los bloques al mapa
            if (!blocks.isEmpty()) {
                medicosConCitas.add(medico);
                activeCitas.put(medico.getId(), blocks);
            }
        }

        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("medicos", medicosConCitas); // Usar la lista de médicos con citas activas
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("ciudades", ciudades);
        model.addAttribute("filtroEspecialidad", especialidad);
        model.addAttribute("filtroCiudad", ciudad);
        model.addAttribute("activeCitas", activeCitas);
    }

    private List<Usuario> obtenerMedicosFiltrados(String especialidad, String ciudad) {
        List<Usuario> medicos;
        if (especialidad != null && !especialidad.isEmpty() && ciudad != null && !ciudad.isEmpty()) {
            medicos = service.findUsuarioByRolAndEspecialidadAndLocalidad("MEDICO", especialidad, ciudad);
        } else if (especialidad != null && !especialidad.isEmpty()) {
            medicos = service.findUsuarioByRolAndEspecialidad("MEDICO", especialidad);
        } else if (ciudad != null && !ciudad.isEmpty()) {
            medicos = service.findUsuarioByRolAndLocalidad("MEDICO", ciudad);
        } else {
            medicos = service.findUsuarioByRol("MEDICO");
        }

        return medicos.stream()
                .filter(medico -> medico.getAprobado() && !perfilIncompleto(medico)) // Agregar filtro de perfil incompleto
                .collect(Collectors.toList());
    }

    private boolean perfilIncompleto(Usuario medico) {
        // Ejemplo de campos obligatorios para un médico: especialidad, costo de consulta, localidad, horario_semanal y frecuencia_cita
        return medico.getEspecialidad() == null || medico.getEspecialidad().trim().isEmpty()
                || medico.getCostoConsulta() == null
                || medico.getLocalidad() == null || medico.getLocalidad().trim().isEmpty()
                || medico.getHorarioSemanal() == null || medico.getHorarioSemanal().trim().isEmpty()
                || medico.getFrecuenciaCita() == null;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidar la sesión
        return "redirect:/login?logout";
    }


}
