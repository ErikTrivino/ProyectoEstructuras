package uniquindio.services;

import uniquindio.modelo.Actividad;
import uniquindio.exceptions.ActividadException;

public interface IProceso {
    public void agregarActividadAlfinal(Actividad actividad) throws ActividadException;
    public void crearActividadDespuesDe(String actividadPosterior, Actividad actividadNueva) throws ActividadException;
    public void crearActividadDespuesDeUltimaActividadCreada(Actividad actividadNueva) throws ActividadException;
    public void eliminarActividad(Actividad actividad);
    public void actualizarActividad(Actividad actividad);
    public Actividad buscarActividadByNombre(String nombreActividad);

    int consultarTiempoDuracionProceso();
    void intercambiarActividades(Actividad actividad1, Actividad actividad2, boolean tareas);



}
