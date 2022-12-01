package com.pruebas.mockito.pruebasUnitariasConMockito;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebas.mockito.pruebasUnitariasConMockito.controlador.CuentaControlador;
import com.pruebas.mockito.pruebasUnitariasConMockito.datos.Datos;
import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.Cuenta;
import com.pruebas.mockito.pruebasUnitariasConMockito.entidades.TransaccionDTO;
import com.pruebas.mockito.pruebasUnitariasConMockito.service.CuentaService;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

//Probamos la capa de controlador y le proporsiona las dependecias requeridas
//mediante mocks, mvc Test
@WebMvcTest(CuentaControlador.class)
public class CuentaControllerTest {

    ObjectMapper objectMapper; //sirve para mapear un json a objeto
    @Autowired
    private MockMvc mockMvc; //mockmvc ofrece una api fluida, permite hacer una llamada
                            //web con todos los parametros necesarios

    @MockBean//indicamos que el atributo va a ser un objeto falso(simulacro=simulacion)
    private CuentaService cuentaService; //Sirve para crear un simulacro del servicio

    @BeforeEach
    void configurar(){
        objectMapper = new ObjectMapper();
    }
    @Test
    void  testVerDetalles()throws Exception{
        when(cuentaService.findById(2L)).thenReturn(Datos.crearCuenta002().orElseThrow());
        //lo que hace es retornar una peticion(PERFORM)
        mockMvc.perform(get("/api/cuentas/2").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona").value("jose"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo").value("2000"));
        verify(cuentaService).findById(2L);
    }
    @Test
    void testTransferirDinero() throws Exception {
        TransaccionDTO transaccionDTO=new TransaccionDTO();
        transaccionDTO.setCuentaOrigenId(1L);
        transaccionDTO.setCuentaDestinoId(2L);
        transaccionDTO.setMonto(new BigDecimal("100"));
        transaccionDTO.setBancoId(1L);

        System.out.println(objectMapper.writeValueAsString(transaccionDTO));

        Map<String,Object> respuesta=new HashMap<>();
        respuesta.put("date", LocalDate.now().toString());
        respuesta.put("status","ok");
        respuesta.put("mensaje","transeferncia realizada con exito");
        respuesta.put("transaccionDTO",transaccionDTO);
        System.out.println(objectMapper.writeValueAsString(respuesta));
        mockMvc.perform(post("/api/cuentas/transferir").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transaccionDTO)))
                        .andExpect(status().isOk())
                        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.date").value(LocalDate.now().toString()))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.mensaje").
                                value("transeferncia realizada con exito"))
                        .andExpect(MockMvcResultMatchers.jsonPath("$.transaccionDTO.cuentaOrigenId")
                                .value(transaccionDTO.getCuentaOrigenId()))
                        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(respuesta)));
    }
    @Test
    void listarCuentas() throws Exception {
        List<Cuenta>cuentas= Arrays.
                        asList(Datos.crearCuenta001().
                        orElseThrow(),
                        Datos.crearCuenta002()
                        .orElseThrow());
        when(cuentaService.listAll()).thenReturn(cuentas);

        mockMvc.perform(get("/api/cuentas").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].persona").value("christian"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].persona").value("jose"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].saldo").value("1000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].saldo").value("2000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$",hasSize(2)))

                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cuentas)));

        verify(cuentaService).listAll();
    }

    @Test
    void guardarCuenta() throws Exception {
        Cuenta cuenta = new Cuenta(1,"matias",new BigDecimal("3000"));//el id es nulo pq la base de datos lo incrementa
        when(cuentaService.save(any())).then(invocation ->{
            Cuenta c = invocation.getArgument(0);
            c.setId(3L);
            return c;
        });
        mockMvc.perform(post("/api/cuentas").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id",is(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.persona",is("matias")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.saldo",is(3000)));
        verify(cuentaService).save(any());







    }



}
