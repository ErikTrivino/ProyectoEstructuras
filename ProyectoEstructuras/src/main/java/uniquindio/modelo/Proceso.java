package uniquindio.modelo;



import uniquindio.exceptions.ActividadException;
import uniquindio.listasEnlazadas.Cola;
import uniquindio.listasEnlazadas.listas.ListaDoble;

import java.util.Iterator;

public class Proceso  {
    private String id;
    private String nombre;
    private ListaDoble<Actividad> actividades;
    private Actividad ultimaActividadCreada;

    public Proceso(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.actividades = new ListaDoble<>();

    }

    public Proceso() {

        this.actividades = new ListaDoble<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public ListaDoble<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(ListaDoble<Actividad> actividades) {
        this.actividades = actividades;
    }

    public Actividad getUltimaActividadCreada() {
        return ultimaActividadCreada;
    }

    public void setUltimaActividadCreada(Actividad ultimaActividadCreada) {
        this.ultimaActividadCreada = ultimaActividadCreada;
    }


    public boolean verificarNombre(String nombre) {
        for (int i = 0; i < getActividades().getTamanio(); i++) {
            if(getActividades().obtenerValorNodo(i).getNombre().equals(nombre)){
                return true;
            }
        }
        return false;
    }
    public void agregarActividadAlfinal(Actividad actividad) throws ActividadException {
        if(!verificarNombre(actividad.getNombre())){
            getActividades().agregarfinal(actividad);
            this.ultimaActividadCreada = actividad;
        }else{
            throw new ActividadException("La actividad ya existe");
        }



    }
    public int obtenerPosicion( String nombreActividad) {
        int valor = 0;
        for (int i = 0; i < getActividades().getTamanio(); i++) {
            String nombre = getActividades().obtenerValorNodo(i).getNombre();
            if(nombre.equals(nombreActividad)){
                valor = i;
                return valor;
            }
        }
        return valor;
    }
    public void crearActividadDespuesDe(String actividadPosterior, Actividad actividadNueva) throws ActividadException {
        if(!verificarNombre(actividadNueva.getNombre())){
            int posicion = obtenerPosicion(actividadPosterior);
            if(posicion+1== getActividades().getTamanio()){
                getActividades().agregarfinal(actividadNueva);
                this.ultimaActividadCreada = actividadNueva;
            }else {
                getActividades().agregar(actividadNueva, posicion+1);
                this.ultimaActividadCreada = actividadNueva;
            }
        }else{
            throw new ActividadException("La actividad ya existe");
        }
    }
    public void crearActividadDespuesDeUltimaActividadCreada(Actividad actividadNueva) throws ActividadException {
        if(ultimaActividadCreada != null){
            int pos = actividades.obtenerPosicionNodo(ultimaActividadCreada);
            actividades.agregar(actividadNueva, pos);
            this.ultimaActividadCreada = actividadNueva;
        }else{
            throw new ActividadException("La actividad ya existe");
        }

    }

    public void eliminarActividad(Actividad actividad) throws ActividadException {
        if(verificarNombre(actividad.getDescripcion())){
            Actividad actividad1 = buscarActividad(actividad.getNombre());
            getActividades().eliminar(actividad1);
        }else{
            throw new ActividadException("La actividad ya existe");
        }
    }

    public void actualizarActividad(Actividad actividad) {
        int pos = buscarActividad(actividad);
        if (pos != -1) {
            // Actualiza la actividad en la lista
            actividades.agregar(actividad, pos);
        } else {
            System.out.println("La actividad no se encontró en la lista.");
        }
    }

    private int buscarActividad(Actividad actividad) {
        Iterator<Actividad> iterable = actividades.iterator();
        int pos = -1;
        int cont = 0;
        while(iterable.hasNext()){
            if(iterable.next().getNombre().equals(actividad.getNombre())){
                pos = cont;
            }
            cont++;
        }
        if(pos != -1){
            return cont;
        }else {
            return pos;
        }
    }
    public Actividad buscarActividad(String nombre) {
        Actividad actividad = new Actividad();
        for (int i = 0; i < getActividades().getTamanio(); i++) {
            Actividad actividad1 = getActividades().obtenerValorNodo(i);
            if(actividad1.getNombre().equals(nombre)){
                return actividad1;
            }
        }
        return actividad;
    }


    public Actividad buscarActividadByNombre(String nombreActividad) {
        for (Actividad actividad : actividades) {
            if (actividad.getNombre().equals(nombreActividad)) {
                // Realiza alguna acción con la actividad encontrada
                // Puedes imprimir la información de la actividad o hacer lo que necesites
                return actividad; // Sale del método una vez que se encuentra la primera coincidencia
            }
        }
        return null;
    }
    public int calcularTiempoMax(){
        int tiempoMax = 0;

        for (int i = 0; i < getActividades().getTamanio(); i++) {
            Actividad actividad = getActividades().obtenerValorNodo(i);
            tiempoMax += actividad.calcularTiempoMax();
        }

        return tiempoMax;
    }
    public int calcularTiempoMin(){
        int tiempoMin = 0;

        for (int i = 0; i < getActividades().getTamanio(); i++) {
            Actividad actividad = getActividades().obtenerValorNodo(i);
            if(actividad.isObligatoria()==false){
                tiempoMin += actividad.calcularTiempoMin();
            }
        }

        return tiempoMin;
    }


    public int consultarTiempoDuracionProceso() {
        int duracionTotal = 0;
        for (Actividad actividad : actividades) {
            duracionTotal += actividad.obtenerDuracionActividad();
        }
        return duracionTotal;
    }

    public void intercambiarActividades(String nombreActividad1, String nombreActividad2) throws ActividadException{
        Actividad actividad1 = buscarActividad(nombreActividad1);
        Actividad actividad2 = buscarActividad(nombreActividad2);
        getActividades().intercambiarNodosPorValores(actividad1,actividad2);

    }

    public void intercambiarContenido(String nombreActividad1, String nombreActividad2) throws ActividadException {
        Actividad actividad1 = buscarActividad(nombreActividad1);
        Actividad actividad2 = buscarActividad(nombreActividad2);
        Cola<Tarea> tareasActividad1 = actividad1.getTareas();
        Cola<Tarea> tareasActividad2 = actividad2.getTareas();
        actividad1.setTareas(tareasActividad2);
        actividad2.setTareas(tareasActividad1);
    }

    public void intercambiarActividadesUnicamente(String nombreActividad1, String nombreActividad2) throws ActividadException {
        Actividad actividad1 = buscarActividad(nombreActividad1);
        Actividad actividad2 = buscarActividad(nombreActividad2);
        Cola<Tarea> tareasActividad1 = actividad1.getTareas();
        Cola<Tarea> tareasActividad2 = actividad2.getTareas();
        actividad1.setTareas(tareasActividad2);
        actividad2.setTareas(tareasActividad1);
        getActividades().intercambiarNodosPorValores(actividad1,actividad2);
    }



}
