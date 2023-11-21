package uniquindio.modelo;

public class Tarea {

    private String nombre;
    private String descripcion;
    private boolean obligatoria;
    private int duracionMinutos;

    public Tarea(String descripcion, boolean obligatoria, int duracionMinutos) {
        this.descripcion = descripcion;
        this.obligatoria = obligatoria;
        this.duracionMinutos = duracionMinutos;
    }

    public Tarea() {
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

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
}
