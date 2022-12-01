package com.pruebas.mockito.pruebasUnitariasConMockito.repository;

import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepositorio extends JpaRepository<Banco,Long> {
}
