package com.pruebas.mockito.pruebasUnitariasConMockito.exceptions;

public class DineroInsuficienteException extends RuntimeException{

    private static final long serialVersionUID=1L;


    public DineroInsuficienteException(String mensaje){
        super(mensaje);

    }
}
