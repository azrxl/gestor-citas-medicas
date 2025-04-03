package cr.ac.una.demologinspringboot.logic.entities.horario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CitaLogic {
    private int inicio;
    private int fin;

    private String horainicio;
    private String horafin;

    public CitaLogic()
    {
    }

    public CitaLogic(int inicio, int fin)
    {
        this.inicio = inicio;
        this.fin = fin;

        horainicio = convertMinutesHour(inicio);
        horafin = convertMinutesHour(fin);

    }
    public String convertMinutesHour(String minutes)
    {
        return  convertMinutesHour(Integer.parseInt(minutes));
    }

    public String convertMinutesHour(int minutes)
    {
        String formatted;
        int hour, minute;

        hour = minutes / 60;
        minute = minutes % 60;

        formatted = Integer.toString(hour);
        formatted +=":";

        if(minute==0)
        {
            formatted +="00";
        }else {
            formatted +=Integer.toString(minute);
        }
        return formatted;
    }

}
