package com.anderson.ecommerce.controller;

import com.anderson.ecommerce.exceptions.CreateException;
import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.model.request.ClienteModelRequest;
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
        String body = "{\"nome\":\"Ana\", \"email\": \"ana@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(notNull())));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O nome não foi informado")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O nome não foi informado")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O nome deve possuir de 3 a 35 caracteres")));

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O nome deve possuir de 3 a 35 caracteres")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O e-mail não foi informado")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O e-mail não foi informado")));

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O campo e-mail não está no padrão de e-mail")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O campo e-mail não está no padrão de e-mail")));
    }

    @Test
    @DisplayName("creteCliente() falha, pois o e-mail está fora de padrão (.)")
    public void testCreateClienteFalhaEmailForaPadraoPonto() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\":\"anderson@emailcom\", \"senha\": \"123456\", \"telefone\":\"15987654321\"}";

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is(400)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O campo e-mail não está no padrão de e-mail")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O e-mail deve possuir no máximo 35 caracteres")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: A senha não foi informada")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: A senha não foi informada")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: A senha deve possuir de 6 a 20 caracteres")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: A senha deve possuir de 6 a 20 caracteres")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O telefone não foi informado")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O telefone não foi informado")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O telefone deve possuir 11 números (ddd mais o número)")));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O telefone deve possuir 11 números (ddd mais o número)")));
    }

    // --> Análise no BD
    @Test
    @DisplayName("createCliente() falha, pois já existe um cliente com o e-mail informado")
    public void testCreateClienteFalhaEmailJaExiste() throws Exception{

        //Parâmetro
        String body = "{\"nome\":\"Anderson\", \"email\": \"anderson@email.com\", \"senha\": \"123456\", \"telefone\": \"15987654321\" }";

        //Simulação
        ClienteModelRequest clienteRequest = new ClienteModelRequest();

        clienteRequest.setNome("Anderson");
        clienteRequest.setEmail("anderson@email.com");
        clienteRequest.setSenha("123456");
        clienteRequest.setTelefone("15987654321");

        when(clienteService.createCliente(clienteRequest))
                .thenThrow(new CreateException("Cliente", "uma conta com o e-mail informado já foi cadastrado"));

        //Teste
        mockMvc.perform(post("/cliente").content(body).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.cod", Matchers.is("400")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.mensagem", Matchers.is("Cliente não pode ser criado: O telefone deve possuir 11 números (ddd mais o número)")));
    }
}
