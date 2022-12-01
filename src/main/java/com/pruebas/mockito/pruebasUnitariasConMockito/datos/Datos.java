package com.pruebas.mockito.pruebasUnitariasConMockito.datos;

import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Banco;
import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Cuenta;

import java.math.BigDecimal;
import java.util.Optional;

public class Datos {
    public static Optional<Cuenta> crearCuenta001(){
        return Optional.of(new Cuenta(1L,"christian",new BigDecimal("1000")));
    }
    public static Optional<Cuenta> crearCuenta002(){
        return Optional.of(new Cuenta(2L,"jose",new BigDecimal("2000")));
    }
    public static Optional<Banco> crearBanco(){
        return Optional.of(new Banco(1L,"Banco provincia",0));
    }


}
