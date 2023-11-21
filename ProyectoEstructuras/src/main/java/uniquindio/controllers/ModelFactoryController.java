package uniquindio.controllers;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import uniquindio.exceptions.ActividadException;
import uniquindio.exceptions.TareaException;
import uniquindio.hilos.HiloCorreo;
import uniquindio.listasEnlazadas.Cola;
import uniquindio.modelo.*;
import uniquindio.persistence.Persistencia;


import java.io.IOException;
import java.util.*;

public class ModelFactoryController {
    AppGestion appGestion;
    //Cliente clienteActual;
    UsuarioApp usuarioAppActual;
    boolean loginAdmin;
    boolean loginEmpleado;


    public String generarFormularioInicioSesion(String nombreUsuario) {
        // Construir el contenido HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Iniciar Sesión</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin-top: 50px; }");
        html.append(".container { background-color: #fff; width: 300px; padding: 20px; border-radius: 8px; margin: 0 auto; box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.2); }");
        html.append("h1 { color: #333; }");
        html.append("input[type='text'], input[type='password'] { width: calc(100% - 20px); padding: 8px; margin: 10px 0; border: 1px solid #ccc; border-radius: 4px; }");
        html.append("input[type='submit'] { background-color: #4caf50; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>Bienvenido, ").append(nombreUsuario).append("!</h1>");
        html.append("<h2>Iniciar Sesión</h2>");
        html.append("<form>");
        html.append("<input type='text' placeholder='Usuario' name='usuario'><br>");
        html.append("<input type='password' placeholder='Contraseña' name='contrasena'><br>");
        html.append("<input type='submit' value='Iniciar Sesión'>");
        html.append("</form>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }


    public void verificarLoginAdmin(String usuario, String contrasenia) {
        if(appGestion.getAdmin().getNombreUsuario().equals(usuario) && appGestion.getAdmin().getContrasenia().equals(contrasenia)){
            usuarioAppActual = appGestion.getAdmin();
            loginAdmin = true;
            HiloCorreo hiloCorreo = new HiloCorreo(usuario, generarFormularioInicioSesion(usuario));
            hiloCorreo.start();
        }
    }

    public boolean isLoginAdmin() {
        return loginAdmin;
    }

    public boolean verificarLoginUsuarioNormal(String nombreUsuario, String contrasenia) {
        for (UsuarioApp usuario:appGestion.getListaUsuarios()
             ) {
            if(usuario.getNombreUsuario().equals(nombreUsuario)&&usuario.getContrasenia().equals(contrasenia)){
                usuarioAppActual = usuario;
                loginEmpleado = true;
                HiloCorreo hiloCorreo = new HiloCorreo(nombreUsuario, generarFormularioInicioSesion(nombreUsuario));
                hiloCorreo.start();
            }
        }
        return loginEmpleado;
    }

    public Proceso obtenerProceso(String nombreBuscador) {
        return appGestion.buscarProceso(nombreBuscador);
    }

    public Actividad obtenerActividad(String nombreBuscador) {
        return appGestion.buscarActividad(nombreBuscador);
    }

    public Tarea obtenerTarea(String nombreBuscador) {
        return appGestion.obtenerTarea(nombreBuscador);
    }

    public int obtenerTiempoMaxProceso(Proceso procesoBuscarDetalle) {
        return appGestion.obtenerTiempoMaxProceso(procesoBuscarDetalle);
    }

    public ArrayList<Actividad> obtenerActividadesDeProceso(Proceso procesoBuscarDetalle) {
        ArrayList<Actividad> list = new ArrayList<>();
        for (Actividad a:procesoBuscarDetalle.getActividades()
             ) {
            list.add(a);
        }
        return list;
    }

    public ArrayList<Actividad> obtenerListaActividadesDeTarea(Tarea tareaBuscarDetalle) {
        return appGestion.obtenerListaActividadesDeTarea(tareaBuscarDetalle);
    }

    public ArrayList<Tarea> obtenerTareasDeProceso(Proceso procesoBuscarDetalle) {
        ArrayList<Tarea> list = new ArrayList<>();
        for (Actividad a : procesoBuscarDetalle.getActividades()) {
            Cola<Tarea> tareaCola = a.obtenerCopiaCola();
            while(!tareaCola.estaVacia()){
                Tarea tarea = tareaCola.desencolar();
                if(!verificarTareaExisteLista(tarea, list)){
                    list.add(tarea);
                }
            }
        }
        return list;

    }

    private boolean verificarTareaExisteLista(Tarea tarea, ArrayList<Tarea> list) {
        boolean r = false;
        for (Tarea t:list
             ) {
            if(t.getNombre().equals(tarea.getNombre())){
                r = true;
            }
        }
        return r;
    }

    public ArrayList<Tarea> obtenerTareasDeActividad(Actividad actividadBuscarDetalle) {
        ArrayList<Tarea> list = new ArrayList<>();
       Cola<Tarea> tareaCola = actividadBuscarDetalle.obtenerCopiaCola();
       while(!tareaCola.estaVacia()){
           list.add(tareaCola.desencolar());
       }
        return list;
    }

    public ArrayList<Proceso> obtenerListaProcesosDeActividad(Actividad actividadBuscarDetalle) {
        return appGestion.obtenerListaProcesosDeActividad(actividadBuscarDetalle);
    }

    public ArrayList<Proceso> obtenerProcesosDeTarea(Tarea tareaBuscarDetalle) {
        return appGestion.obtenerProcesosDeTarea(tareaBuscarDetalle);
    }

    public ArrayList<Proceso> obtenerListaProcesoUsuarioActual() {
        ArrayList<Proceso> procesos = new ArrayList<>();
        for (Proceso p:usuarioAppActual.getListaProcesos()
             ) {
            procesos.add(p);
        }
        return procesos;
    }

    public void generarCorreoNotificacion() {
        HiloCorreo hiloCorreo = new HiloCorreo(usuarioAppActual.getNombreUsuario(),generarMensajeVerReportes(usuarioAppActual.getNombreUsuario()) );
        hiloCorreo.start();
    }
    public String generarMensajeVerReportes(String nombreUsuario) {
        // Construir el contenido HTML
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<title>Ver Reportes de Procesos</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; background-color: #f4f4f4; text-align: center; margin-top: 50px; }");
        html.append(".container { background-color: #fff; width: 300px; padding: 20px; border-radius: 8px; margin: 0 auto; box-shadow: 0px 0px 10px 0px rgba(0,0,0,0.2); }");
        html.append("h1 { color: #333; }");
        html.append("p { color: #666; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        html.append("<div class='container'>");
        html.append("<h1>¡Hola, ").append(nombreUsuario).append("!</h1>");
        html.append("<p>Necesitas ver tus reportes de procesos.</p>");
        html.append("</div>");
        html.append("</body>");
        html.append("</html>");

        return html.toString();
    }

    public List<UsuarioApp> obtenerUsuariosApp() {
        ArrayList<UsuarioApp> list = new ArrayList<>();
        for (UsuarioApp u: appGestion.getListaUsuarios()
             ) {
            list.add(u);
        }
        return list;
    }

    public ArrayList<String> obtenerUsuarioConProceso(Proceso procesoSeleccionado) {
        ArrayList<String> list = new ArrayList<>();
        for (UsuarioApp u:appGestion.getListaUsuarios()
             ) {
            //System.out.println(u.getNombreUsuario());
            for (Proceso p:u.getListaProcesos()
                 ) {
                if(p.getNombre().equals(procesoSeleccionado.getNombre())){
                   // System.out.println(u.getNombreUsuario());
                    list.add(u.getNombreUsuario());
                }
            }
        }
        return list;
    }


    private static class SingletonHolder {
        // El constructor de Singleton puede ser llamado desde aquí al ser protected
        private final static ModelFactoryController eINSTANCE = new ModelFactoryController();
    }
    public static ModelFactoryController getInstance() {
        return SingletonHolder.eINSTANCE;
    }
    public ModelFactoryController() {

        loginAdmin = false;
        loginEmpleado = false;
        inicializarDatos();
        //cargarDatosArchivosTxt();
        //guardarArchivosTxt();




        //Siempre se debe verificar si la raiz del recurso es null

        if(appGestion == null){
            inicializarDatos();
           guardarArchivosTxt();
        }
        //registrarAccionesSistema("Datos cargados", 1, "Cargar datos");
        //inicializarDatos();
    }
    public void  cargarDatosArchivosTxt(){
        appGestion = new AppGestion();
        appGestion.setAdmin(new UsuarioApp("erikpablot28@gmail.com", "1234", TipoUsuario.ADMIN));
        try {
            ArrayList<Proceso> procesosTxt = Persistencia.cargarProcesos();
            for (Proceso p:procesosTxt
                 ) {
                appGestion.getListaPrcesos().agregarfinal(p);
            }
            ArrayList<Actividad> actividadesTxt = Persistencia.cargarActividades();
            for (Actividad p:actividadesTxt
            ) {
                appGestion.getListaActividades().agregarfinal(p);
            }
            ArrayList<Tarea> tareasTxt = Persistencia.cargarTareas();
            for (Tarea p:tareasTxt
            ) {
                appGestion.getListaTareas().agregarfinal(p);
            }
            ArrayList<UsuarioApp> usuarioAppsTxt = Persistencia.cargarUsuarios();
            for (UsuarioApp p:usuarioAppsTxt
            ) {
                appGestion.getListaUsuarios().agregarfinal(p);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void guardarArchivosTxt(){
        try {
            Persistencia.guardarProcesosTxt(appGestion.getListaPrcesos());
            Persistencia.guardarActividadesTxt(appGestion.getListaActividades());
            Persistencia.guardarTareasTxt(appGestion.getListaTareas());
            Persistencia.guardarUsuaiosTxt(appGestion.getListaUsuarios());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void inicializarDatos() {


        appGestion = new AppGestion();
        UsuarioApp usuarioApp = new UsuarioApp("erikpablot28@gmail.com", "1111", TipoUsuario.USUARIOREGULAR);
        UsuarioApp usuarioApp2 = new UsuarioApp("erikpablot@gmail.com", "2222", TipoUsuario.USUARIOREGULAR);
        UsuarioApp usuarioApp3 = new UsuarioApp("erikp.trivinog@uqvirtual.edu.co", "3333", TipoUsuario.USUARIOREGULAR);


        Tarea tarea = new Tarea("tarea1", true, 3);
        Tarea tarea2 = new Tarea("tarea2", false, 3);
        Tarea tarea3 = new Tarea("tarea3", true, 3);

        Proceso p = new Proceso ("id", "proceso1");
        Proceso p2 = new Proceso ("id2", "proces2");
        Proceso p3= new Proceso ("id3", "proces3");

        Actividad actividad = new Actividad("actividad1", "descripcion", true);
        Actividad actividad2 = new Actividad("actividad2", "descripcion", true);
        Actividad actividad3 = new Actividad("actividad3", "descripcion", true);

        actividad.getTareas().encolar(tarea);
        actividad.getTareas().encolar(tarea2);
        actividad2.getTareas().encolar(tarea3);
        actividad3.getTareas().encolar(tarea3);

        p.getActividades().agregarfinal(actividad);
        p.getActividades().agregarfinal(actividad2);
        p.getActividades().agregarfinal(actividad3);
        p2.getActividades().agregarfinal(actividad3);
        p3.getActividades().agregarfinal(actividad3);

        usuarioApp.getListaProcesos().agregarfinal(p);

        appGestion.getListaPrcesos().agregarfinal(p);
        appGestion.getListaPrcesos().agregarfinal(p2);
        appGestion.getListaPrcesos().agregarfinal(p3);
        appGestion.getListaTareas().agregarfinal(tarea);
        appGestion.getListaTareas().agregarfinal(tarea2);
        appGestion.getListaTareas().agregarfinal(tarea3);
        appGestion.getListaActividades().agregarfinal(actividad);
        appGestion.getListaActividades().agregarfinal(actividad2);
        appGestion.getListaActividades().agregarfinal(actividad3);
        appGestion.getListaUsuarios().agregarfinal(usuarioApp);
        appGestion.getListaUsuarios().agregarfinal(usuarioApp3);
        appGestion.getListaUsuarios().agregarfinal(usuarioApp2);
    }


    void mostrarMensaje(String titulo, String header, String contenido, Alert.AlertType alertType) {

        Alert alerta = new Alert(alertType);
        alerta.setTitle(titulo);
        alerta.setHeaderText(header);
        alerta.setContentText(contenido);
        alerta.showAndWait();

    }
    public void agregarProceso(String id, String nombre, ObservableList<String> listadoComboUsuarios) {
        appGestion.agregarProceso(id, nombre, listadoComboUsuarios);
    }
    public void modificarProceso(String id, String nombre, ObservableList<String> listadoComboUsuarios) {
        Proceso p = new Proceso(id, nombre);
        appGestion.actualizarProceso(p, id,listadoComboUsuarios);
    }

    public void eliminarProceso(Proceso proceso) {
        appGestion.eliminarPrceso(proceso);

    }

    public void agregarActividad(String nombre, String descripcion, String radioObligatoria, ObservableList<String> nombreProceso, String valorSeleccionActividad, String nombreActividadPrecedera) throws ActividadException {
        if(radioObligatoria.equals("Si")){
            Actividad actividad = new Actividad(nombre, descripcion,true );
            appGestion.agregarActividad(actividad, nombreProceso, valorSeleccionActividad, nombreActividadPrecedera);
        }else{
            Actividad actividad = new Actividad(nombre, descripcion,false );
            appGestion.agregarActividad(actividad, nombreProceso, valorSeleccionActividad,nombreActividadPrecedera);
        }

    }

    public void eliminarActividad(Actividad actividadSeleccionada, String nombreProceso) throws ActividadException {
        appGestion.eliminarActividadProceso(actividadSeleccionada, nombreProceso);
    }

    public void modificarActividad(String nombre, String descripcion, String radioObligatoria, String nombreProceso) {
        if(radioObligatoria.equals("Si")){
            Actividad actividad = new Actividad(nombre, descripcion,true );
            appGestion.modificarActividad(actividad, nombreProceso);
        }else{
            Actividad actividad = new Actividad(nombre, descripcion,false );
            appGestion.modificarActividad(actividad, nombreProceso);
        }

    }

    public void agregarTarea(String descripcion, String seleccionTareaObligatoria, int minutos, ObservableList<String> nombreActividad, String seleccionAgregarTarea, int posicionTarea) {
        if(seleccionTareaObligatoria.equals("Si")){
            Tarea tarea = new Tarea(descripcion,true, minutos);
            try {
                appGestion.agregarTarea(tarea, nombreActividad, seleccionAgregarTarea, posicionTarea);
            } catch (TareaException e) {
                throw new RuntimeException(e);
            }
        }else{
            Tarea tarea = new Tarea(descripcion,false, minutos);
            try {
                appGestion.agregarTarea(tarea, nombreActividad, seleccionAgregarTarea, posicionTarea);
            } catch (TareaException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Proceso> obtenerProcesos() {
        Iterator<Proceso> iterator = appGestion.obtenerProcesos().iterator();
        ArrayList<Proceso> procesos = new ArrayList<>();
        while(iterator.hasNext()){
            procesos.add(iterator.next());

        }
        return procesos;
    }

    public ArrayList<Actividad> obtenerActividades() {
        Iterator<Actividad> iterator = appGestion.obtenerActividades().iterator();
        ArrayList<Actividad> actividades = new ArrayList<>();
        while(iterator.hasNext()){
            actividades.add(iterator.next());

        }
        return actividades;
    }

    public ArrayList<Tarea> obtenerTareas() {
        Iterator<Tarea> iterator = appGestion.obtenerTareas().iterator();
        ArrayList<Tarea> tareas = new ArrayList<>();
        while(iterator.hasNext()){
            tareas.add(iterator.next());

        }
        return tareas;
    }

    public void eliminarTarea(Tarea tareaSeleccionada, String nombreActividad) {
        appGestion.eliminarTarea(tareaSeleccionada, nombreActividad);
    }

    public void modificarTarea(Tarea tareaSeleccionada, String nombreActividad) {
        appGestion.modificarTarea(tareaSeleccionada, nombreActividad);
    }

    public ArrayList<String >obtenerProcesosDeActividad(Actividad actividadSeleccionada) {
        ArrayList<String> lista = new ArrayList<>();
        for (Proceso p:appGestion.getListaPrcesos()
        ) {
            boolean bandera = true;
            //System.out.println(p.getNombre());
            for (Actividad a:p.getActividades()
            ) {
                if(actividadSeleccionada.getNombre().equals(a.getNombre()) && bandera){
                    lista.add(p.getNombre());

                    bandera = false;

                }
            }
        }
        return lista;
    }

    public ArrayList<String> obtenerActividadesDeTarea(Tarea tareaSeleccionada) {
        ArrayList<String> lista = new ArrayList<>();
        Queue<Tarea> tareasTemporales = new LinkedList<>();
        for (Actividad p:appGestion.getListaActividades()
        ) {
            boolean bandera = true;
            //System.out.println(p.getNombre());
            for (int i =0; i < p.getTareas().tamanio && bandera;i++){
                Tarea tarea = p.getTareas().desencolar();
                tareasTemporales.offer(tarea);
                if(tareaSeleccionada.equals(tarea)){
                    lista.add(p.getNombre());
                    bandera = false;
                }
            }
            while (!tareasTemporales.isEmpty()) {
                p.getTareas().encolar(tareasTemporales.poll());
            }

        }

        return lista;
    }


}
