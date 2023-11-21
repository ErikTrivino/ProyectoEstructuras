package uniquindio.modelo;



import uniquindio.exceptions.TareaException;
import uniquindio.listasEnlazadas.Pila;
import uniquindio.listasEnlazadas.Cola;


public class Actividad  {

    private String nombre;
    private String descripcion;
    private boolean obligatoria;
    private Cola<Tarea> tareas;

    public Actividad(String nombre, String descripcion, boolean obligatoria) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.obligatoria = obligatoria;
        this.tareas = new Cola<>();
    }

    public Actividad() {
        this.tareas = new Cola<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isObligatoria() {
        return obligatoria;
    }

    public void setObligatoria(boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    public Cola<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Cola<Tarea> tareas) {
        this.tareas = tareas;
    }

    public void agregarTareaAlFinal(Tarea tarea) throws TareaException {
        if(!verificarExisteTarea(tarea.getNombre())){
            if(verificarOpcional(tarea.isObligatoria())){
                getTareas().encolar(tarea);
            }else {
                throw new TareaException("Hay dos tareas opcionales seguidas");
            }
        }else {
            throw new TareaException("La tarea existe");
        }
    }
    private boolean verificarOpcional(boolean obligatorio) {
        int tamano = getTareas().getTamano();
        boolean isPosible = true;
        for (int i = 0; i < tamano; i++) {
            Tarea tarea = getTareas().desencolar();
            getTareas().encolar(tarea);
            if(i == tamano-1){
                if(obligatorio == true && tarea.isObligatoria() == true){
                    isPosible = false;
                }
            }
        }
        return isPosible;
    }
    private boolean verificarExisteTarea(String nombre) {
        int tamano = getTareas().getTamano();
        boolean existe = false;
        for (int i = 0; i <tamano ; i++) {
            Tarea tarea = getTareas().desencolar();
            getTareas().encolar(tarea);
            if(tarea.getNombre().equals(nombre)){
                existe = true;
            }
        }
        return existe;
    }
    public Tarea buscarTarea(String nombre) {
        Tarea tarea = new Tarea();
        for (int i = 0; i < getTareas().getTamano(); i++) {
            Tarea tarea1 = getTareas().desencolar();
            getTareas().encolar(tarea1);
            if(tarea1.getNombre().equals(nombre)){
                tarea = tarea1;
            }
        }
        return tarea;
    }

    public void agregarTareaPorPosicion(int posicionTarea, Tarea nuevaTarea){
        if(posicionTarea > tareas.getTamano()){
            System.out.println("Posicion no valida");
        }else{
            if (posicionTarea == tareas.getTamano()) {
                // Si la posición es igual al tamaño de la cola, simplemente agrega la tarea al final.
                tareas.encolar(nuevaTarea);
            } else {
                // Si la posición es distinta del tamaño de la cola, necesitas desplazar las tareas existentes hacia adelante.
                Cola<Tarea> copiaCola = new Cola<>();

                // Desencola y encola tareas desde la cola original a la copia hasta llegar a la posición deseada.
                while (!tareas.estaVacia() && posicionTarea > 0) {
                    copiaCola.encolar(tareas.desencolar());
                    posicionTarea--;
                }
                // Agrega la nueva tarea en la posición especificada.
                copiaCola.encolar(nuevaTarea);

                // Luego, encola las tareas restantes de la cola original a la copia.
                while (!tareas.estaVacia()) {
                    copiaCola.encolar(tareas.desencolar());
                }

                // Ahora, la copiaCola contiene todas las tareas en el orden correcto.
                // Puedes copiar las tareas de la copia de nuevo a la cola original si lo deseas.
                tareas = copiaCola;
            }
        }

        }


    public void actualizarTarea(Tarea tarea) {

        Cola<Tarea> tareasTemporales = new Cola<>();

        // Buscar y actualizar la tarea en la cola de tareas
        while (!tareas.estaVacia()) {
            Tarea tareaActual = tareas.desencolar();
            if (tareaActual.equals(tarea)) {
                // Actualizar la tarea
                tareaActual.setDescripcion(tarea.getDescripcion());
                tareaActual.setObligatoria(tarea.isObligatoria());
                tareaActual.setDuracionMinutos(tarea.getDuracionMinutos());
            }
            // Encolar la tarea (ya sea la actualizada o la no modificada)
            tareasTemporales.encolar(tareaActual);
        }

        // Volver a encolar las tareas en el orden original
        while (!tareasTemporales.estaVacia()) {
            tareas.encolar(tareasTemporales.desencolar());
        }

    }


    public void eliminarTarea(Tarea tarea) {

        Cola<Tarea> tareasTemporales = new Cola<>();

        // Buscar y eliminar la tarea de la cola de tareas
        while (!tareas.estaVacia()) {
            Tarea tareaActual = tareas.desencolar();
            if (!tareaActual.equals(tarea)) {
                // Encolar la tarea (si no es la que se va a eliminar)
                tareasTemporales.encolar(tareaActual);
            }
        }

        // Volver a encolar las tareas restantes
        while (!tareasTemporales.estaVacia()) {
            tareas.encolar(tareasTemporales.desencolar());
        }

    }
    public int calcularTiempoMin(){
        int tiempoMin = 0;

        for (int i = 0; i < getTareas().getTamano(); i++) {
            Tarea tarea = getTareas().desencolar();
            getTareas().encolar(tarea);
            if(tarea.isObligatoria() == false){
                tiempoMin += tarea.getDuracionMinutos();
            }
        }

        return tiempoMin;
    }
    public int calcularTiempoMax(){
        int tiempoMax = 0;

        for (int i = 0; i < getTareas().getTamano(); i++) {
            Tarea tarea = getTareas().desencolar();
            getTareas().encolar(tarea);
            tiempoMax += tarea.getDuracionMinutos();
        }

        return tiempoMax;
    }


    public int obtenerDuracionActividad() {
        Pila<Tarea> tareaPila = new Pila<>();
        int suma = 0;
        while(!tareas.estaVacia()){
            Tarea tarea = tareas.desencolar();

            suma+=tarea.getDuracionMinutos();
            tareaPila.push(tarea);
        }
        while(!tareaPila.estaVacia()){
            tareas.encolar(tareaPila.pop());
        }

        return suma;

    }

    public Cola<Tarea> obtenerCopiaCola() {
        Cola<Tarea> colaNueva = new Cola<>();
        Cola<Tarea> colaNuevaCopia = new Cola<>();

        // Obtener una copia de la cola original
        while (!tareas.estaVacia()) {
            Tarea tarea = tareas.desencolar();
            colaNueva.encolar(tarea);
            colaNuevaCopia.encolar(tarea); // Crear una copia de la cola
        }

        // Restaurar la cola original
        while (!colaNueva.estaVacia()) {
            tareas.encolar(colaNueva.desencolar());
        }

        return colaNuevaCopia;
    }

}


