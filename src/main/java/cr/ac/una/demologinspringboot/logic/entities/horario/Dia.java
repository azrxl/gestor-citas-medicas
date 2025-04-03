package cr.ac.una.demologinspringboot.logic.entities.horario;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class Dia {

    String Nombre;
    @Setter
    List<CitaLogic> citaLogics;

    public Dia()
    {}

    public Dia(String nombre, List<CitaLogic> citaLogics)
    {
        this.Nombre = nombre;
        this.citaLogics = citaLogics;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setNombre(int dia) {
        Nombre = GetDayName(dia);
    }

    private String GetDayName(int day)
    {
        return switch (day) {
            case 1 -> "Lunes";
            case 2 -> "Martes";
            case 3 -> "Miercoles";
            case 4 -> "Jueves";
            case 5 -> "Viernes";
            case 6 -> "Sabado";
            case 7 -> "Domingo";
            default -> "";
        };

    }

}
