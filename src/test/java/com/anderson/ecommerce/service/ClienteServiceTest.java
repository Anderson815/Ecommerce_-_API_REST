package com.anderson.ecommerce.service;


import com.anderson.ecommerce.exceptions.InvalidValueException;
import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.exceptions.UpdateException;
import com.anderson.ecommerce.model.request.ClienteModelRequest;
import com.anderson.ecommerce.model.resource.ClienteModelResource;
import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


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
        assertEquals("Não existe cliente de id: " + id, erro.getMessage());
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

    //Testes do método createCliente()
    @Test
    @DisplayName("createCliente() com sucesso")
    public void testCreateClienteComSucesso(){
        //Parâmetro
        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setNome("Anderson");
        clienteRequest.setEmail("anderson@email.com");
        clienteRequest.setSenha("123456");
        clienteRequest.setTelefone("15987654321");

        //Simulação
        when(clienteRepository.existsByEmail(clienteRequest.getEmail()))
                .thenReturn(false);

        //Teste
        ClienteModelResponse clienteResponse = clienteService.createCliente(clienteRequest);
        assertNotNull(clienteResponse);
        assertNotEquals("", clienteResponse.getId());
        verify(clienteRepository).save(any());
    }

    @Test
    @DisplayName("createCliente() falha, pois já existe cliente com o e-mail informado")
    public void testCreateClienteFalhaEmail(){
        //Parâmetro
        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setNome("Anderson");
        clienteRequest.setEmail("anderson@email.com");
        clienteRequest.setSenha("123456");
        clienteRequest.setTelefone("15987654321");

        //Simulação
        when(clienteRepository.existsByEmail(clienteRequest.getEmail()))
                .thenReturn(true);

        //Teste
        InvalidValueException erro = assertThrows(InvalidValueException.class, () -> clienteService.createCliente(clienteRequest));
        assertEquals("Campo inválido: Um cliente com o e-mail informado já foi cadastrado", erro.getMessage());
    }

    //Testes do método updateCliente()
    @Test
    @DisplayName("updateCliente() com sucesso")
    public void testUpdateClienteComSucesso(){
        //parâmetro
        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setNome("Anderson Correia");
        clienteRequest.setEmail("anderson@email.com"); //o e-mail é o único não alterado
        clienteRequest.setSenha("123");
        clienteRequest.setTelefone("15123456789");

        String id = "a";

        //Simulação
        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(this.clienteResource));

        //Teste
        ClienteModelResponse clienteAtual = clienteService.updateCliente(id, clienteRequest);
        assertEquals(clienteRequest.getNome(), clienteAtual.getNome());
        assertEquals(clienteRequest.getSenha(), clienteAtual.getSenha());
        assertEquals(clienteRequest.getTelefone(), clienteAtual.getTelefone());
        assertEquals(clienteResource.getEmail(), clienteAtual.getEmail()); //Não pode alterar o e-mail
        assertEquals(clienteResource.getId(), clienteAtual.getId()); // Não pode alterar o ID
    }

    @Test
    @DisplayName("updateCliente() falha, pois não existe o cliente")
    public void testUpdateClienteFalhaClienteInexistente(){
        //Parâmetro
        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setNome("Anderson Correia");
        clienteRequest.setEmail("anderson@email.com");
        clienteRequest.setSenha("123");
        clienteRequest.setTelefone("15123456789");

        String id = "b";

        //Simulação
        when(clienteRepository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        //Teste
        NotFoundException erro = assertThrows(NotFoundException.class, () -> clienteService.updateCliente(id, clienteRequest));
        assertEquals("Não existe cliente de id: " + id, erro.getMessage());
    }

    @Test
    @DisplayName("updateCliente() falha, pois não pode alterar o e-mail")
    public void testUpdateClienteFalhaEmail(){
        //Parâmetro
        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setNome("Anderson Correia");
        clienteRequest.setEmail("anderson123@email.com");
        clienteRequest.setSenha("123");
        clienteRequest.setTelefone("15123456789");

        String id = "a";

        //Simulação
        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(clienteResource));

        //Teste
        UpdateException erro = assertThrows(UpdateException.class, () -> clienteService.updateCliente(id, clienteRequest));
        assertEquals("Cliente não pode ser atualizado: o e-mail não pode ser alterado", erro.getMessage());
    }

    @Test
    @DisplayName("deleteClinte() com sucesso")
    public void testDeleteClienteComSucesso(){

        //Parâmetro
        String id = "a";

        //Simulação
        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(clienteResource));

        //Teste
        clienteService.deleteCliente(id);
        verify(clienteRepository).delete(clienteResource);
    }

    @Test
    @DisplayName("deleteCliente() falha, pois não existe cliente com o id informado")
    public void testDeleteClienteFalhaClienteInexistente(){
        //Parâmetro
        String id = "b";

        //Simulação
        when(clienteRepository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        //Teste
        NotFoundException erro = assertThrows(NotFoundException.class, () -> clienteService.deleteCliente(id));
        assertEquals("Não existe cliente de id: " + id, erro.getMessage());
    }
}
