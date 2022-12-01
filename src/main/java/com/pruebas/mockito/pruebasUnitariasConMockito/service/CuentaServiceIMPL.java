package com.pruebas.mockito.pruebasUnitariasConMockito.service;

import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Banco;
import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Cuenta;
import com.pruebas.mockito.pruebasUnitariasConMockito.repository.BancoRepositorio;
import com.pruebas.mockito.pruebasUnitariasConMockito.repository.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CuentaServiceIMPL implements CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private BancoRepositorio bancoRepositorio;

    @Override
    @Transactional(readOnly = true)//con readOnly va a ser solo de lectura
    public List<Cuenta> listAll() {
        return cuentaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)//con readOnly va a ser solo de lectura
    public Cuenta findById(Long id) {
        return cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional
    public Cuenta save(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional(readOnly = true)//con readOnly va a ser solo de lectura
    public int revisarTotalTransferencias(Long bancoId) {
        Banco banco=bancoRepositorio.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)//con readOnly va a ser solo de lectura

    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta=cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    public void transferirDiner(Long numeroCuentaOrigen, Long numeroCuentaDestino, BigDecimal monto, Long bancoId) {
        Cuenta cuentaOrigen=cuentaRepository.findById(numeroCuentaOrigen).orElseThrow();
        cuentaOrigen.realizarDebito(monto);
        cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino=cuentaRepository.findById(numeroCuentaDestino).orElseThrow();
        cuentaDestino.realizarCredito(monto);
        cuentaRepository.save(cuentaDestino);

        Banco banco=bancoRepositorio.findById(bancoId).orElseThrow();
        int totalTransferencias = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencias);
        bancoRepositorio.save(banco);
    }
}
