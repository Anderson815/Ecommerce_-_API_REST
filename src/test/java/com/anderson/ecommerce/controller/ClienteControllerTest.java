package com.anderson.ecommerce.controller;

import com.anderson.ecommerce.exceptions.InvalidValueException;
import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.exceptions.UpdateException;
import com.anderson.ecommerce.model.request.ClienteModelRequest;
import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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

    //Teste do método getClientes()
    @Test
    @DisplayName("getClientes() com sucesso")
    public void testGetClientesComSucesso() throws Exception{
        mockMvc.perform(get("/cliente").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    //Testes do método createCliente()
    @Test
    @DisplayName("createCliente() com sucesso")
    public void testCreateClienteComSucesso() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\": \"anderson@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Simulação
        when(clienteService.createCliente(any()))
                .thenReturn(this.clienteResponse);

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Anderson")));
    }

    // --> Problemas com o nome
    @Test
    @DisplayName("creteCliente() falha, pois não é informado o nome")
    public void testCreateClienteFalhaFaltaNome() throws Exception{

        //Parâmetro
        String body = "{\"email\":\"anderson@email.com\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome não foi informado")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o nome está vazio")
    public void testCreateClienteFalhaNomeVazio() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"\", \"email\":\"anderson@email.com\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome deve possuir de 3 a 35 caracteres")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o nome tem menos do que o minímo de caracteres exigido (3)")
    public void testCreateClienteFalhaNomeMinCaracter() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"An\", \"email\": \"ana@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome deve possuir de 3 a 35 caracteres")));

    }

    @Test
    @DisplayName("creteCliente() falha, pois o nome tem mais do que o máximo de caracteres exigido (35)")
    public void testCreateClienteFalhaNomeMaxCaracter() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson Correia de Souza dos Santos Silva\", \"email\": \"ana@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome deve possuir de 3 a 35 caracteres")));
    }

    // --> Problemas com o e-mail
    @Test
    @DisplayName("creteCliente() falha, pois não é informado o e-mail")
    public void testCreateClienteFalhaFaltaEmail() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O e-mail não foi informado")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o e-mail está vazio")
    public void testCreateClienteFalhaEmailVazio() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O e-mail não foi informado")));

    }

    @Test
    @DisplayName("creteCliente() falha, pois o e-mail está fora de padrão (@, .com)")
    public void testCreateClienteFalhaEmailForaPadrao() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"andersonemailcom\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O campo e-mail não está no padrão de e-mail")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o e-mail está fora de padrão (@)")
    public void testCreateClienteFalhaEmailForaPadraoArroba() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"andersonemail.com\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O campo e-mail não está no padrão de e-mail")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o e-mail tem mais do que o máximo de caracteres exigido (35)")
    public void testCreateClienteFalhaEmailMaxCaracter() throws Exception{
        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson_correia_de_souza@grandeemail.com\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O e-mail deve possuir no máximo 35 caracteres")));
    }

    // --> Problemas com a senha

    @Test
    @DisplayName("creteCliente() falha, pois não é informado a senha")
    public void testCreateClienteFalhaFaltaSenha() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha não foi informada")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois a senha está vazia")
    public void testCreateClienteFalhaSenhaVazio() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"senha\":\"\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha deve possuir de 6 a 20 caracteres")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois a senha tem menos do que o minímo de caracteres exigido (6)")
    public void testCreateClienteFalhaSenhaMinCaracter() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"senha\":\"12345\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha deve possuir de 6 a 20 caracteres")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois a senha tem mais do que o máximo de caracteres exigido (20)")
    public void testCreateClienteFalhaSenhaMaxCaracter() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"senha\":\"012345678901234567890\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha deve possuir de 6 a 20 caracteres")));
    }

    // --> Problemas com o telefone

    @Test
    @DisplayName("creteCliente() falha, pois não é informado o telefone")
    public void testCreateClienteFalhaFaltaTelefone() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone não foi informado")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o telefone está vazio")
    public void testCreateClienteFalhaTelefoneVazio() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\", \"telefone\":\"\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone deve possuir 11 números (ddd mais o número)")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o telefone tem menos do que o minímo de caracteres exigido (11)")
    public void testCreateClienteFalhaTelefoneMinCaracter() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\", \"telefone\":\"1598765432\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone deve possuir 11 números (ddd mais o número)")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o telefone tem mais do que o máximo de caracteres exigido (11)")
    public void testCreateClienteFalhaTelefoneMaxCaracter() throws Exception{
        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\", \"telefone\":\"159876543210\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone deve possuir 11 números (ddd mais o número)")));
    }

    // --> Análise no BD
    @Test
    @DisplayName("createCliente() falha, pois já existe um cliente com o e-mail informado")
    public void testCreateClienteFalhaEmailJaExiste() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\": \"anderson@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Simulação
        when(clienteService.createCliente(any()))
                .thenThrow(new InvalidValueException("Um cliente com o e-mail informado já foi cadastrado"));

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: Um cliente com o e-mail informado já foi cadastrado")));
    }

    //Testes do método updateCliente()
    @Test
    @DisplayName("updateCliente() com sucesso")
    public void testUpdateClienteComSucesso() throws Exception{

        //Parâmetros
        ObjectMapper json = new ObjectMapper();
        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setSenha("654321");
        clienteRequest.setNome("Anderson CS");
        clienteRequest.setTelefone("15123456789");
        clienteRequest.setEmail(clienteResponse.getEmail());

        String id = clienteResponse.getId();
        String clienteBodyRequest = json.writeValueAsString(clienteRequest);

        //Simulação
        ClienteModelResponse clienteResponse = new ClienteModelResponse();
        clienteResponse.setId(this.clienteResponse.getId());
        clienteResponse.setNome("Anderson CS");
        clienteResponse.setSenha("654321");
        clienteResponse.setTelefone("15123456789");
        clienteResponse.setEmail(this.clienteResponse.getEmail());

        when(clienteService.updateCliente(eq(id), any(ClienteModelRequest.class)))
                .thenReturn(clienteResponse);

        //Teste
        mockMvc.perform(put("/cliente/{id}", id)
                .content(clienteBodyRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.nome", Matchers.is("Anderson CS")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.senha", Matchers.is("654321")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.telefone", Matchers.is("15123456789")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", Matchers.is(clienteResponse.getEmail())));
    }

    @Test
    @DisplayName("updateCliente() falha, pois o id não existe")
    public void testUpdateClienteFalhaIdNaoExiste() throws Exception{

        //Parâmetros

        ObjectMapper json = new ObjectMapper();

        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setSenha("654321");
        clienteRequest.setNome("Anderson CS");
        clienteRequest.setTelefone("15123456789");
        clienteRequest.setEmail(clienteResponse.getEmail());

        String id = "b";
        String clienteBodyRequest = json.writeValueAsString(clienteRequest);

        //Simulação
        when(clienteService.updateCliente(eq(id), any(ClienteModelRequest.class)))
                .thenThrow(new NotFoundException("cliente", id));

        //Teste
        mockMvc.perform(put("/cliente/{id}", id)
                .content(clienteBodyRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Não existe cliente de id: " + id)));
    }

    // --> Problemas com o nome
    @Test
    @DisplayName("updateCliente() falha, pois não é informado o nome")
    public void testUpdateClienteFalhaFaltaNome() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"email\":\"anderson@email.com\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome não foi informado")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois o nome está vazio")
    public void testUpdateClienteFalhaNomeVazio() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"\", \"email\":\"anderson@email.com\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome deve possuir de 3 a 35 caracteres")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois o nome tem menos do que o minímo de caracteres exigido (3)")
    public void testUpdateClienteFalhaNomeMinCaracter() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"An\", \"email\": \"ana@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome deve possuir de 3 a 35 caracteres")));

    }

    @Test
    @DisplayName("updateCliente() falha, pois o nome tem mais do que o máximo de caracteres exigido (35)")
    public void testUpdateClienteFalhaNomeMaxCaracter() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson Correia de Souza dos Santos Silva\", \"email\": \"ana@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O nome deve possuir de 3 a 35 caracteres")));
    }

    // --> Problemas com o e-mail
    @Test
    @DisplayName("updateCliente() falha, pois o e-mail não pode ser alterado")
    public void testUpdateClienteFalhaEmailNaoAltera() throws Exception{

        //Parâmetro
        ObjectMapper jason = new ObjectMapper();
        ClienteModelRequest clienteRequest = new ClienteModelRequest();
        clienteRequest.setNome(clienteResponse.getNome());
        clienteRequest.setSenha(clienteResponse.getSenha());
        clienteRequest.setTelefone(clienteResponse.getTelefone());
        clienteRequest.setEmail("andersoncs@email.com");

        String id = clienteResponse.getId();
        String clienteBodyRequest = jason.writeValueAsString(clienteRequest);

        // Simulação
        when(clienteService.updateCliente(eq(id), any(ClienteModelRequest.class)))
                .thenThrow(new UpdateException("Cliente","o e-mail não pode ser alterado"));

        //Teste
        mockMvc.perform(put("/cliente/{id}", id)
                .content(clienteBodyRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser atualizado: o e-mail não pode ser alterado")));
    }

    // --> Problemas com a senha

    @Test
    @DisplayName("updateCliente() falha, pois não é informado a senha")
    public void testUpdateClienteFalhaFaltaSenha() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha não foi informada")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois a senha está vazia")
    public void testUpdateClienteFalhaSenhaVazio() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"senha\":\"\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha deve possuir de 6 a 20 caracteres")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois a senha tem menos do que o minímo de caracteres exigido (6)")
    public void testUpdateClienteFalhaSenhaMinCaracter() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"senha\":\"12345\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha deve possuir de 6 a 20 caracteres")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois a senha tem mais do que o máximo de caracteres exigido (20)")
    public void testUpdateClienteFalhaSenhaMaxCaracter() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@email.com\", \"senha\":\"012345678901234567890\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: A senha deve possuir de 6 a 20 caracteres")));
    }

    // --> Problemas com o telefone

    @Test
    @DisplayName("updateCliente() falha, pois não é informado o telefone")
    public void testUpdateClienteFalhaFaltaTelefone() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone não foi informado")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois o telefone está vazio")
    public void testUpdateClienteFalhaTelefoneVazio() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\", \"telefone\":\"\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone deve possuir 11 números (ddd mais o número)")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois o telefone tem menos do que o minímo de caracteres exigido (11)")
    public void testUpdateClienteFalhaTelefoneMinCaracter() throws Exception{

        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\", \"telefone\":\"1598765432\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone deve possuir 11 números (ddd mais o número)")));
    }

    @Test
    @DisplayName("updateCliente() falha, pois o telefone tem mais do que o máximo de caracteres exigido (11)")
    public void testUpdateClienteFalhaTelefoneMaxCaracter() throws Exception{
        //Parâmetro
        String id = clienteResponse.getId();
        String body = "{\"nome\":\"Anderson\", \"email\":\"andeson@email.com\", \"senha\": \"123456\", \"telefone\":\"159876543210\"}";

        //Teste
        mockMvc.perform(put("/cliente/{id}", id).content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Campo inválido: O telefone deve possuir 11 números (ddd mais o número)")));
    }

    //Testes do método deleteCliente()
    @Test
    @DisplayName("deleteCliente() com sucesso")
    public void testDeleteClienteComSucesso() throws Exception{

        //Parâmetros
        String id = "a";

        //Teste
        mockMvc.perform(delete("/cliente/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("deleteCliente() falha, pois o id não existe")
    public void testDeleteCliente() throws Exception{

        //Parâmetros
        String id = "b";

        //Simulação
        Mockito.doThrow(new NotFoundException("cliente", id))
                .when(clienteService)
                .deleteCliente(id);

        //Teste
        mockMvc.perform(delete("/cliente/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(404)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Não existe cliente de id: " + id)));
    }
}
