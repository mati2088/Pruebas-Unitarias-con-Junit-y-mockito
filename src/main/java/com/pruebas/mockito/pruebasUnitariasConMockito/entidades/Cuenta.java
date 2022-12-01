package com.pruebas.mockito.pruebasUnitariasConMockito.entidades;


import com.pruebas.mockito.pruebasUnitariasConMockito.exceptions.DineroInsuficienteException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "cuentas")
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String persona;
    private BigDecimal saldo;
    //genero constructores
    public Cuenta(){
        super();
    }

    public Cuenta(long id, String persona, BigDecimal saldo) {
        this.id = id;
        this.persona = persona;
        this.saldo = saldo;
    }


    //genero getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
    //metodo para retirar dinero de la cuenta
    public void realizarDebito(BigDecimal monto){
        BigDecimal nuevoSaldo=this.saldo.subtract(monto);
        if (nuevoSaldo.compareTo(BigDecimal.ZERO)<0){
            throw new DineroInsuficienteException("Dinero insuficiente en la cuenta");
        }
        this.saldo=nuevoSaldo;

    }
    //metodo para agregar dinero a la cuenta
    public void realizarCredito(BigDecimal monto){

        this.saldo=saldo.add(monto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cuenta cuenta = (Cuenta) o;
        return id == cuenta.id && persona.equals(cuenta.persona) && saldo.equals(cuenta.saldo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, persona, saldo);
    }
}
