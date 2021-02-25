 
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.*;
/**
 * Esta clase modela un sencillo calendario de eventos.
 * 
 * Por simplicidad consideraremos que los eventos no se solapan
 * y no se repiten
 * 
 * El calendario guarda en un map los eventos de una serie de meses
 * Cada mes (la clave en el map, un enumerado Mes) tiene asociados 
 * en una colección ArrayList los eventos de ese mes
 * 
 * Solo aparecen los meses que incluyen algún evento
 * 
 * Las claves se recuperan en orden alfabético
 * 
 */
public class CalendarioEventos {
    private TreeMap<Mes, ArrayList<Evento>> calendario;

    /**
     * el constructor
     */
    public CalendarioEventos() {
        this.calendario = new TreeMap<>();
    }

    /**
     * añade un nuevo evento al calendario
     * Si la clave (el mes del nuevo evento) no existe en el calendario
     * se creará una nueva entrada con dicha clave y la colección formada
     * por ese único evento
     * Si la clave (el mes) ya existe se añade el nuevo evento insertándolo de forma
     * que quede ordenado por fecha y hora de inicio
     * 
     * Pista! Observa que en la clase Evento hay un método antesDe() que vendrá
     * muy bien usar aquí
     */
    public void addEvento(Evento nuevo) {
        if(calendario.keySet().contains(nuevo.getMes())){
            calendario.get(nuevo.getMes()).add(nuevo);

        }
        else{
            ArrayList<Evento> eventos = new ArrayList<>();
            calendario.put(nuevo.getMes(), eventos);
        }

    }

    /**
     * Representación textual del calendario
     * Hacer de forma eficiente 
     * Usar el conjunto de entradas  
     */
    public String toString() {
        Mes[] meses = Mes.values();
        String str = "";
        for(int i = 0; i < meses.length; i++){
            ArrayList<Evento> comparar = calendario.get(meses[i]);
            str += meses[i] + "/n";
            for(int z = 0; z < comparar.size(); z++){
                str += comparar.get(z) + "  ";
            }
            str += "/n";
        }
        return str;
    }

    /**
     * Dado un mes devolver la cantidad de eventos que hay en ese mes
     * Si el mes no existe se devuelve 0
     */
    public int totalEventosEnMes(Mes mes) {
        if(calendario.get(mes) == null){
           return 0;  
        }
        else{
            return calendario.get(mes).size();
        }
    }

    /**
     * Devuelve un conjunto (importa el orden) 
     * con los meses que tienen mayor nº de eventos
     * Hacer un solo recorrido del map con el conjunto de claves
     *  
     */
    public TreeSet<Mes> mesesConMasEventos() {
        TreeSet<Mes> ganador = new TreeSet<>();
        Mes[] meses = Mes.values();
        for (int i = 1; i < meses.length; i++) {
            Mes aux = meses[i];
            int j = i - 1;
            while (j >= 0 && calendario.get(meses[j]).size() > calendario.get(aux).size()) {
                meses[j + 1] = meses[j];
                j--;
            }
            meses[j + 1] = aux;
        }

        for(int i = 0; i < meses.length; i++){
            ganador.add(meses[0]);
        }

        return ganador;
    }

    /**
     * Devuelve el nombre del evento de mayor duración en todo el calendario
     * Se devuelve uno solo (el primero encontrado) aunque haya varios
     */
    public String eventoMasLargo() {
        Mes[] meses = Mes.values();
        String largo = "";
        int duracion = 0;
        for(int i = 0; i < meses.length; i++){
            ArrayList<Evento> comparar = calendario.get(meses[i]);
            for(int z = 0; z < comparar.size(); z++){
                if(comparar.get(z).getDuracion() > duracion){
                    largo = comparar.get(z).getNombre();
                    duracion = comparar.get(z).getDuracion();
                }
            }
        }
        return largo;
    }

    /**
     * Borrar del calendario todos los eventos de los meses indicados en el array
     * y que tengan lugar el día de la semana proporcionado (se entiende día de la
     * semana como 1 - Lunes, 2 - Martes ..  6 - Sábado, 7 - Domingo)
     * 
     * Si alguno de los meses del array no existe el el calendario no se hace nada
     * Si al borrar de un mes los eventos el mes queda con 0 eventos se borra la entrada
     * completa del map
     */
    public int cancelarEventos(Mes[] meses, int dia) {
        int cuantos = 0;
        for(int i = 0; i < meses.length; i++){
            if(calendario.containsKey(meses[i])){
                ArrayList<Evento> este = calendario.get(meses[i]);
                for(int z = 0; z < meses.length; z++){
                    if(este.get(z).getDia() == dia){
                        calendario.get(meses[i]).remove(z);
                        cuantos++;
                        if(calendario.get(meses[i]).isEmpty()){
                            calendario.remove(meses[i]);
                        }
                    }
                }
            }
        }
        return cuantos;
    }

    /**
     * Código para testear la clase CalendarioEventos
     */
    public static void main(String[] args) {
        CalendarioEventos calendario = new CalendarioEventos();
        CalendarioIO.cargarEventos(calendario);
        System.out.println(calendario);

        System.out.println();

        Mes mes = Mes.Febrero;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        mes = Mes.Marzo;
        System.out.println("Eventos en " + mes + " = "
            + calendario.totalEventosEnMes(mes));
        System.out.println("Mes/es con más eventos "
            + calendario.mesesConMasEventos());

        System.out.println();
        System.out.println("Evento de mayor duración: "
            + calendario.eventoMasLargo());

        System.out.println();
        Mes[] meses = {Mes.Febrero, Mes.Marzo, Mes.Mayo, Mes.Junio};
        int dia = 6;
        System.out.println("Cancelar eventos de " + Arrays.toString(meses));
        int cancelados = calendario.cancelarEventos(meses, dia);
        System.out.println("Se han cancelado " + cancelados +
            " eventos");
        System.out.println();
        System.out.println("Después de cancelar eventos ...");
        System.out.println(calendario);
    }

}
