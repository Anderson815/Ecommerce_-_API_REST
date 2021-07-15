package com.anderson.ecommerce.service;


import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.model.resource.ClienteModelResource;
import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;


import javax.annotation.security.RunAs;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private ClienteModelResource clienteResource;

    @BeforeEach
    public void clienteResource(){
        this.clienteResource = new ClienteModelResource();
        this.clienteResource.setId("a");
        this.clienteResource.setNome("Anderson");
        this.clienteResource.setEmail("anderson@email.com");
        this.clienteResource.setSenha("123456");
        this.clienteResource.setTelefone("15987654321");
    }

    //Testes do método getCliente()
    @Test
    @DisplayName("getConta() com sucesso")
    public void testGetClienteComSucesso(){
        //Parâmetro
        String id = "a";

        //Simulação do repositório
        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(this.clienteResource));

        //teste
        ClienteModelResponse clienteResponse = clienteService.getCliente(id);
        assertNotNull(clienteResponse);
        assertEquals(this.clienteResource.getNome(), clienteResponse.getNome());
        assertEquals(this.clienteResource.getEmail(), clienteResponse.getEmail());
        assertEquals(this.clienteResource.getSenha(), clienteResponse.getSenha());
    }

    @Test
    @DisplayName("getConta() falha, pois não existe o cliente com o ID informado")
    public void testGetClienteFalhaClienteNaoExiste(){
        //Parâmetro
        String id = "b";

        //Simulação
        when(clienteRepository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        //Teste
        NotFoundException erro = assertThrows(NotFoundException.class, () -> clienteService.getCliente(id));
        assertEquals(erro.getMessage(), "Não existe cliente de id: " + id);
    }

    //Testes do método getClientes()
    @Test
    @DisplayName("getContas() com sucesso")
    public void testGetClientesComSucesso(){

        //Preparo para a simulação
        ClienteModelResource clienteResource2 = new ClienteModelResource();
        clienteResource2.setId("b");
        clienteResource2.setNome("Bruna");
        clienteResource2.setEmail("bruna@email.com");
        clienteResource2.setSenha("654321");
        clienteResource2.setTelefone("15123456789");

        List<ClienteModelResource> listaAtual = new ArrayList<>();
        listaAtual.add(clienteResource);
        listaAtual.add(clienteResource2);

        //Simulação
        when(clienteRepository.findById(clienteResource.getId()))
                .thenReturn(Optional.of(clienteResource));

        when(clienteRepository.findById(clienteResource2.getId()))
                .thenReturn(Optional.of(clienteResource2));

        when(clienteRepository.findAll())
                .thenReturn(listaAtual);

        //Testes
        List <ClienteModelResponse> listaClienteAtual = clienteService.getClientes();
        assertNotNull(listaAtual.get(0));
        assertNotNull(listaAtual.get(1));
        assertEquals(clienteResource.getEmail(), listaClienteAtual.get(0).getEmail());
        assertEquals(clienteResource2.getEmail(), listaClienteAtual.get(1).getEmail());
        assertEquals(2, listaAtual.size());
    }

}
