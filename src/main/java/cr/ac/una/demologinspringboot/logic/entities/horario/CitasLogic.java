package cr.ac.una.demologinspringboot.logic.entities.horario;

import java.util.ArrayList;
import java.util.List;

public class CitasLogic {

   private List<CitaLogic> EstimarCitasDia(String rango, int frecuencia)
   {
       int inicio =0, fin =0;

       List<CitaLogic> citaLogics = new ArrayList<>();

       String regex = ",";
       String[] rangos = rango.split(regex);
       for(String nodo: rangos)
       {
           regex = "-";
           String[] dias = nodo.split(regex);
           inicio = Integer.parseInt(dias[0]);
           fin = Integer.parseInt(dias[1]);
           citaLogics.addAll(EstimarCitasDia(inicio, fin, frecuencia));
       }
       return citaLogics;
   }

    private List<CitaLogic> EstimarCitasDia(int inicio, int fin, int frecuencia)
   {
       List<CitaLogic> citaLogics = new ArrayList<>();
       inicio *= 60; // se convierte a minutos
       fin *=60;// se convierte a minutos
       int totalCitas = (fin - inicio) / frecuencia;
       CitaLogic citaLogic = new CitaLogic();

       while (totalCitas > 0)
       {
            citaLogic = new CitaLogic(inicio,(inicio + frecuencia));
            citaLogics.add(citaLogic);
           totalCitas--;
           inicio += frecuencia;
       }

       return citaLogics;
   }

   public List<Dia> EstimarSemanaCitas(String horario, int frecuencia)
   {
       String regex = ";";
       List<Dia> agenda = new ArrayList<>();
       String[] dias = horario.split(regex);
       int diacont = 1;

       Dia dia;
        for(String nodo : dias)
       {
           dia = new Dia();
           dia.setNombre(diacont++);
           agenda.add(dia);
           if (!nodo.isEmpty())
            dia.setCitaLogics(EstimarCitasDia(nodo, frecuencia));
           ;
       }
       return agenda;
   }
}
