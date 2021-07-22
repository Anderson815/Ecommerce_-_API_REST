package com.anderson.ecommerce.controller;

import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.service.ClienteService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    private ClienteModelResponse clienteResponse;

    @BeforeEach
    public void clienteResponse(){
        this.clienteResponse = new ClienteModelResponse();

        this.clienteResponse.setId("a");
        this.clienteResponse.setNome("Anderson");
        this.clienteResponse.setEmail("anderson@email.com");
        this.clienteResponse.setSenha("123456");
        this.clienteResponse.setTelefone("15987654321");
    }

    //Testes do método getCliente()
    @Test
    @DisplayName("getCliente() com sucesso")
    public void testGetClienteComSucesso() throws Exception{

        //Parâmetro
        String id = "a";

        //Simulação
        when(clienteService.getCliente(id))
                .thenReturn(this.clienteResponse);

        //Teste
        mockMvc.perform(get("/cliente/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("getCliente() falha, pois não existe o cliente com o ID informado")
    public void testGetClienteFalhaClienteNaoExiste() throws Exception{

        //Parâmetro
        String id = "b";

        //Simulação
        when(clienteService.getCliente(id))
                .thenThrow(new NotFoundException("cliente", id));

        //Teste
        mockMvc.perform(get("/cliente/{id}", id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Não existe cliente de id: " + id)));

    }

    //Teste do método getContas()
    @Test
    @DisplayName("getClientes() com sucesso")
    public void testGetClientesComSucesso() throws Exception{
        mockMvc.perform(get("/cliente").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
