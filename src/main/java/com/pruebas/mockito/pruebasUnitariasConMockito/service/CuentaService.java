package com.pruebas.mockito.pruebasUnitariasConMockito.service;

import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    public List<Cuenta> listAll();
    public Cuenta findById(Long id);
    public Cuenta save(Cuenta cuenta);
    public int revisarTotalTransferencias(Long bancoId);
    public BigDecimal revisarSaldo(Long cuentaId);
    public void transferirDiner(Long numeroCuentaOrigen,
                                Long numeroCuentaDestino,
                                BigDecimal monto,Long bancoId);
}
