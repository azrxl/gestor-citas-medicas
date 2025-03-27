package cr.ac.una.presentation;

import cr.ac.una.data.MedicoRepository;
import cr.ac.una.logic.Service;
import cr.ac.una.logic.entities.Medico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MedicoController {
    @Autowired
    private Service service;

    @GetMapping("/medicos")
    public String listarMedicos(Model model) {
        List<Medico> medicos = (List<Medico>) service.medicosFindAll();
        model.addAttribute("medicos", medicos);
        return "medicos";
    }
}
