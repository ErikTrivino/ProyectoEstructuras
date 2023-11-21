package uniquindio.exceptions;

public class PermisoDenegadoException extends Exception{
    public PermisoDenegadoException(String el_acceso_no_fue_autorizado) {
        super(el_acceso_no_fue_autorizado);
    }
}
