package com.pruebas.mockito.pruebasUnitariasConMockito.controlador;

import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Cuenta;
import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.TransaccionDTO;
import com.pruebas.mockito.pruebasUnitariasConMockito.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaControlador {
    @Autowired
    private CuentaService cuentaService;
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<Cuenta> listarCuentas(){
        return cuentaService.listAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public Cuenta verDetalles(@PathVariable long id){
        return cuentaService.findById(id);
    }
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Cuenta guardarCuenta(@RequestBody Cuenta cuenta){
        return cuentaService.save(cuenta);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?>transferirDinero(@RequestBody TransaccionDTO transaccionDTO){
        cuentaService.transferirDiner(transaccionDTO.getCuentaDestinoId()
                ,transaccionDTO.getCuentaOrigenId(),
                transaccionDTO.getMonto(),
                transaccionDTO.getBancoId());
        Map<String,Object> respuesta=new HashMap<>();
        respuesta.put("date", LocalDate.now().toString());
        respuesta.put("status","ok");
        respuesta.put("mensaje","transeferncia realizada con exito");
        respuesta.put("transaccionDTO",transaccionDTO);
        return ResponseEntity.ok(respuesta);


    }

}
