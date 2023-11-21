package uniquindio.modelo;


import javafx.collections.ObservableList;
import uniquindio.exceptions.ActividadException;
import uniquindio.exceptions.TareaException;
import uniquindio.listasEnlazadas.Cola;
import uniquindio.listasEnlazadas.listas.ListaDoble;
import uniquindio.listasEnlazadas.listas.ListaSimple;

import java.util.ArrayList;

public class AppGestion  {
    ListaDoble<Proceso> listaPrcesos;
    ListaDoble<Actividad> listaActividades;
    ListaDoble<Tarea> listaTareas;
    ListaSimple<UsuarioApp> listaUsuarios;
    UsuarioApp admin;

    public AppGestion() {
        this.listaPrcesos = new ListaDoble<>();
        this.listaUsuarios = new ListaSimple<>();
        this.listaTareas = new ListaDoble<>();
        this.listaActividades = new ListaDoble<>();
        this.admin = new UsuarioApp("erikpablot28@gmail.com", "1234", TipoUsuario.ADMIN);

    }


    public ListaDoble<Proceso> getListaPrcesos() {
        return listaPrcesos;
    }

    public void setListaPrcesos(ListaDoble<Proceso> listaPrcesos) {
        this.listaPrcesos = listaPrcesos;
    }

    public ListaDoble<Actividad> getListaActividades() {
        return listaActividades;
    }

    public void setListaActividades(ListaDoble<Actividad> listaActividades) {
        this.listaActividades = listaActividades;
    }

    public ListaDoble<Tarea> getListaTareas() {
        return listaTareas;
    }

    public void setListaTareas(ListaDoble<Tarea> listaTareas) {
        this.listaTareas = listaTareas;
    }

    public ListaSimple<UsuarioApp> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(ListaSimple<UsuarioApp> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public UsuarioApp getAdmin() {
        return admin;
    }

    public void setAdmin(UsuarioApp admin) {
        this.admin = admin;
    }


    public void agregarProceso(String id, String nombre, ObservableList<String> listadoComboUsuarios) {
        Proceso nuevoProceso = new Proceso(id, nombre);
        for (UsuarioApp u:listaUsuarios
             ) {
            for (String s:listadoComboUsuarios
                 ) {
                if(u.getNombreUsuario().equals(s)){
                    u.getListaProcesos().agregarfinal(nuevoProceso);
                }
            }
        }
        listaPrcesos.agregarfinal(nuevoProceso);
    }


    public void actualizarProceso(Proceso proceso, String idProceso, ObservableList<String> listadoComboUsuarios) {

        // Buscar el proceso por ID
        Proceso procesoEncontrado = buscarProcesoById(idProceso);

        // Verificar si se encontró el proceso
        if (procesoEncontrado != null) {
            // Actualizar la información del proceso
            procesoEncontrado.setId(proceso.getId());

            procesoEncontrado.setNombre(proceso.getNombre());
            procesoEncontrado.setActividades(proceso.getActividades());
            procesoEncontrado.setUltimaActividadCreada(proceso.getUltimaActividadCreada());

            for (UsuarioApp u:listaUsuarios
                 ) {
                for (Proceso p:u.getListaProcesos()
                     ) {
                    if(p.getId().equals(idProceso)){
                        p.setId(proceso.getId());
                        p.setNombre(proceso.getNombre());
                        p.setActividades(proceso.getActividades());
                        p.setUltimaActividadCreada(proceso.getUltimaActividadCreada());
                    }

                }
            }


        } else {
            System.out.println("El proceso con ID " + idProceso + " no se encontró en la lista.");
        }
    }


    public void eliminarPrceso(Proceso proceso) {
        int pos = listaPrcesos.obtenerPosicionNodo(proceso);


        if (pos != -1) {
            listaPrcesos.eliminar(proceso);
            eliminarProcesoUsuario(proceso);
            System.out.println("Proceso eliminado correctamente.");
        } else {
            System.out.println("El proceso no se encontró en la lista.");
        }
    }

    private void eliminarProcesoUsuario(Proceso proceso) {
        for (UsuarioApp u:listaUsuarios
             ) {
            u.getListaProcesos().eliminar(proceso);
        }
    }


    public ListaDoble<Proceso> obtenerProcesos() {
        return listaPrcesos;
    }


    public Proceso buscarProcesoById(String idProceso) {
        for (Proceso proceso : listaPrcesos) {
            if (proceso.getId().equals(idProceso)) {
                return proceso;
            }
        }
        return null;
    }


    public void agregarUsuario(UsuarioApp usuarioApp) {

    }


    public void eliminarUsuario(UsuarioApp usuarioApp) {

    }


    public void actualizararUsuario(UsuarioApp usuarioApp) {

    }


    public UsuarioApp buscarUsuarioById(String idUsuario) {
        return null;
    }


    public void buscarActividadByNombre(String nombre) {
        boolean actividadEncontrada = false;

        // Itera sobre todos los procesos en busca de la actividad por nombre
        for (Proceso proceso : listaPrcesos) {
            // Itera sobre todas las actividades del proceso actual
            for (Actividad actividad : proceso.getActividades()) {
                if (actividad.getNombre().equals(nombre)) {

                    System.out.println("Actividad encontrada");

                    actividadEncontrada = true;
                }
            }
        }

        if (!actividadEncontrada) {
            System.out.println("La actividad no se encontró en ningún proceso.");
        }

    }


    public void buscarDatosTareaActividadActual(Tarea tarea) {
        boolean tareaEncontrada = false;

        for (Proceso proceso : listaPrcesos) {
            for (Actividad actividad : proceso.getActividades()) {
                Cola<Tarea> tareas = actividad.getTareas();

                while (!tareas.estaVacia()) {
                    Tarea tareaActual = tareas.desencolar();

                    if (tareaActual.equals(tarea)) {
                        // Realiza alguna acción con la tarea encontrada
                        System.out.println("Tarea encontrada:");
                        tareaEncontrada = true;
                        break;
                    }

                    // Vuelve a encolar la tarea si no es la buscada
                    tareas.encolar(tareaActual);
                }

                if (tareaEncontrada) {
                    break;
                }
            }

            if (tareaEncontrada) {
                break;
            }
        }

        if (!tareaEncontrada) {
            System.out.println("La tarea no se encontró en ninguna actividad.");
        }
    }


    public void buscarDatosTareaActividadByNombre(Tarea tarea, String nombreActividad) {
        boolean tareaEncontrada = false;

        for (Proceso proceso : listaPrcesos) {
            for (Actividad actividad : proceso.getActividades()) {
                if (actividad.getNombre().equals(nombreActividad)) {
                    Cola<Tarea> tareas = actividad.getTareas();

                    while (!tareas.estaVacia()) {
                        Tarea tareaActual = tareas.desencolar();

                        if (tareaActual.getDescripcion().equals(tarea.getDescripcion())) {
                            // Realiza alguna acción con la tarea encontrada
                            System.out.println("Tarea encontrada en la actividad '" + nombreActividad + "':");
                            System.out.println("Descripción: " + tareaActual.getDescripcion());
                            System.out.println("Obligatoria: " + tareaActual.isObligatoria());
                            System.out.println("Duración en minutos: " + tareaActual.getDuracionMinutos());
                            System.out.println("Proceso ID: " + proceso.getId());
                            tareaEncontrada = true;
                            break;
                        }

                        // Vuelve a encolar la tarea si no es la buscada
                        tareas.encolar(tareaActual);
                    }
                }

                if (tareaEncontrada) {
                    break;
                }
            }

            if (tareaEncontrada) {
                break;
            }
        }

        if (!tareaEncontrada) {
            System.out.println("La tarea no se encontró en la actividad '" + nombreActividad + "'.");
        }

    }


    public void consultarTiempoDuracionProceso(Proceso proceso) {
        int duracionTotal = proceso.consultarTiempoDuracionProceso();

        if (duracionTotal > 0) {
            System.out.println("La duración total del proceso con ID " + proceso.getId() + " es de " + duracionTotal + " minutos.");
        } else {
            System.out.println("El proceso con ID " + proceso.getId() + " no tiene actividades con duración.");
        }

    }





    private Proceso buscarProcesoPorActividad(Actividad actividad) {
        for (Proceso proceso : listaPrcesos) {
            for (Actividad a : proceso.getActividades()) {
                if (a.equals(actividad)) {
                    return proceso;
                }
            }
        }
        return null;
    }
    private void intercambiarTareas(Actividad actividad1, Actividad actividad2) {
        // Intercambiar tareas entre las actividades
        Cola<Tarea> tareas1 = actividad1.getTareas();
        Cola<Tarea> tareas2 = actividad2.getTareas();

        Cola<Tarea> tempTareas = new Cola<>();
        tempTareas = tareas1;
        tareas1 = tareas2;
        tareas2 = tempTareas;

        actividad1.setTareas(tareas1);
        actividad2.setTareas(tareas2);
    }

    public void agregarActividad(Actividad actividad, ObservableList<String> nombreProcesos, String valorSeleccionActividad, String nombreActividadPrecedera) throws ActividadException {
        for (String nombreP:nombreProcesos
             ) {

            int proceso = buscarProcesoByNombre(nombreP);
            Proceso p = listaPrcesos.obtenerValorNodo(proceso);


            switch (valorSeleccionActividad){
                case "Al final":
                    p.agregarActividadAlfinal(actividad);
                    break;
                case "Despues de ultima actividad":
                    p.crearActividadDespuesDeUltimaActividadCreada(actividad);
                    break;
                case "Actividad precedera":
                    p.crearActividadDespuesDe(nombreActividadPrecedera, actividad);
                    break;
            }
        }

        listaActividades.agregarfinal(actividad);

    }

    private int buscarProcesoByNombre(String nombreProceso) {
        int cont = 0;
        int contFinal = 0;
        for (Proceso p: listaPrcesos
             ) {
            if(p.getNombre().equals(nombreProceso)){
                contFinal = cont;
            }
            cont++;
        }
        if(cont!=0){
            return contFinal;
        }else{
            return -1;
        }

    }

    public void eliminarActividadProceso(Actividad actividadSeleccionada, String nombreProceso) throws ActividadException {
        int p = buscarProcesoByNombre(nombreProceso);
        Proceso proceso = listaPrcesos.obtenerValorNodo(p);
        proceso.eliminarActividad(actividadSeleccionada);
    }

    public void modificarActividad(Actividad actividad, String nombreProceso) {
        int p = buscarProcesoByNombre(nombreProceso);
        Proceso proceso = listaPrcesos.obtenerValorNodo(p);
        proceso.actualizarActividad(actividad);
    }

    public void agregarTarea(Tarea tarea, ObservableList<String> nombreActividades, String seleccionAgregarTarea, int posicionTarea) throws TareaException {
        String nombreTarea = "Tarea"+listaTareas.getTamanio()+1;
        tarea.setNombre(nombreTarea);
        for (String nombreActividad:nombreActividades
              ) {
            int pos = obtenerActividadByNombre(nombreActividad);
            Actividad actividad = listaActividades.obtener(pos);
            switch (seleccionAgregarTarea){
                case "Al final":
                    actividad.agregarTareaAlFinal(tarea);
                    break;

                case "Por posicion":
                    actividad.agregarTareaPorPosicion(posicionTarea, tarea);
                    break;
            }
        }
        listaTareas.agregarfinal(tarea);


    }

    private int obtenerActividadByNombre(String nombreActividad) {
        return 0;
    }

    public ListaDoble<Actividad >obtenerActividades() {
        return listaActividades;
    }

    public ListaDoble<Tarea> obtenerTareas() {
        return listaTareas;
    }

    public void eliminarTarea(Tarea tareaSeleccionada, String nombreActividad) {
        int pos = obtenerActividadByNombre(nombreActividad);
        Actividad actividad = listaActividades.obtener(pos);
        actividad.eliminarTarea(tareaSeleccionada);
    }

    public void modificarTarea(Tarea tareaSeleccionada, String nombreActividad) {
        int pos = obtenerActividadByNombre(nombreActividad);
        Actividad actividad = listaActividades.obtener(pos);
        actividad.actualizarTarea(tareaSeleccionada);
    }

    public Proceso buscarProceso(String nombreBuscador) {
        Proceso proceso = new Proceso();

        for (Proceso p: listaPrcesos){
            if(p.getNombre().equals(nombreBuscador)){
                proceso = p;
            }
        }
        return proceso;
    }

    public Actividad buscarActividad(String nombreBuscador) {
        Actividad actividad = new Actividad();
        for (Actividad ac : listaActividades) {
            if(ac.getNombre().equals(nombreBuscador)){
                actividad = ac;
            }
        }{

        }
        return actividad;
    }

    public Tarea obtenerTarea(String nombreBuscador) {
        Tarea tarea = new Tarea();

        for (Tarea t:listaTareas
             ) {
            if(t.getNombre().equals(nombreBuscador)){
                tarea = t;
            }
        }{

        }
        return tarea;
    }

    public int obtenerTiempoMaxProceso(Proceso procesoBuscarDetalle) {
        return 0;
    }

    public ArrayList<Actividad> obtenerListaActividadesDeTarea(Tarea tareaBuscarDetalle) {
        ArrayList<Actividad> list = new ArrayList<>();
        for (Actividad a:listaActividades
             ) {
            for (Tarea t:listaTareas
                 ) {
                if(t.getNombre().equals(tareaBuscarDetalle.getNombre())){
                    list.add(a);
                }
            }
        }
        return list;
    }

    public ArrayList<Proceso> obtenerListaProcesosDeActividad(Actividad actividadBuscarDetalle) {
        ArrayList<Proceso> list = new ArrayList<>();
        for (Proceso p:listaPrcesos
             ) {
            for (Actividad a:p.getActividades()
                 ) {
                if(a.getNombre().equals(actividadBuscarDetalle.getNombre())){
                    if(!verificarSiExisteProcesoList(p, list)){
                        list.add(p);
                    }
                    
                }
            }
        }
        return list;
    }

    private boolean verificarSiExisteProcesoList(Proceso p, ArrayList<Proceso> list) {
        boolean r = false;
        for (Proceso pro:list
             ) {
            if(pro.getNombre().equals(p.getNombre())){
                r = true;
            }
        }
        return r;
    }

    public ArrayList<Proceso> obtenerProcesosDeTarea(Tarea tareaBuscarDetalle) {
        ArrayList<Proceso> list = new ArrayList<>();
        for (Proceso p:listaPrcesos
             ) {
            for (Actividad a:p.getActividades()
                 ) {
                Cola<Tarea> cola = a.obtenerCopiaCola();
                while(!cola.estaVacia()){
                    Tarea tarea = cola.desencolar();
                    if(tarea.getNombre().equals(tareaBuscarDetalle.getNombre())){
                        if(!verificarSiExisteProcesoList(p, list)){
                            list.add(p);
                        }

                    }
                }

            }
        }

        return list;
    }
}
