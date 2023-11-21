package uniquindio.services;

import uniquindio.listasEnlazadas.listas.ListaDobleCircular;
import uniquindio.modelo.Actividad;
import uniquindio.modelo.Proceso;
import uniquindio.modelo.Tarea;
import uniquindio.modelo.UsuarioApp;

public interface IAppGestion {
    public void agregarProceso(String id, String nombre);

    public void actualizarProceso(Proceso proceso, String idProceso);
    public void eliminarPrceso(Proceso proceso);
    public ListaDobleCircular<Proceso> obtenerProcesos();
    public Proceso buscarProcesoById(String idProceso);

    public void agregarUsuario(UsuarioApp usuarioApp);
    public void eliminarUsuario(UsuarioApp usuarioApp);
    public void actualizararUsuario(UsuarioApp usuarioApp);
    public UsuarioApp buscarUsuarioById(String idUsuario);

    public  void buscarActividadByNombre(String nombre);

    public void buscarDatosTareaActividadActual(Tarea tarea);
    public void buscarDatosTareaActividadByNombre(Tarea tarea,  String nombreActividad);

    void consultarTiempoDuracionProceso(Proceso proceso);
    void intercambiarActividades(Actividad actividad1, Actividad actividad2, boolean tareas);


}
