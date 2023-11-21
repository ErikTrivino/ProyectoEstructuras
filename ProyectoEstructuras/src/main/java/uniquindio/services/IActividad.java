package uniquindio.services;

import uniquindio.modelo.Tarea;

public interface IActividad {

    public void agregarTareaAlFinal(Tarea tareaNueva);
    public void agregarTareaPorPosicion(int posicionTarea, Tarea nuevaTarea);
    public void actualizarTarea(Tarea tarea);
    public void eliminarTarea(Tarea tarea);

    int obtenerDuracionActividad();
}
