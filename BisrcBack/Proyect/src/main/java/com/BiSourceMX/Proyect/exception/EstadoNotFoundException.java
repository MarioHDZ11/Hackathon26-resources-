package com.BiSourceMX.Proyect.exception;

public class EstadoNotFoundException extends RuntimeException {
    public EstadoNotFoundException(String nombre) {
        super("Estado no encontrado: " + nombre);
    }
}
