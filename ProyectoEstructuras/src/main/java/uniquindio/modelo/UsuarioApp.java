package uniquindio.modelo;

import uniquindio.listasEnlazadas.listas.ListaDoble;
import uniquindio.listasEnlazadas.listas.ListaDobleCircular;

public class UsuarioApp {
    private String nombreUsuario;
    private String contrasenia;

    private TipoUsuario tipoUsuario;
    private ListaDoble<Proceso> listaProcesos;

    public UsuarioApp(String nombreUsuario, String contrasenia, TipoUsuario tipoUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.tipoUsuario = tipoUsuario;
        this.listaProcesos = new ListaDoble<>();
    }

    public UsuarioApp() {
        this.listaProcesos = new ListaDoble<>();
    }

    public ListaDoble<Proceso> getListaProcesos() {
        return listaProcesos;
    }

    public void setListaProcesos(ListaDoble<Proceso> listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
