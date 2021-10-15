package com.anderson.ecommerce.service;

import com.anderson.ecommerce.exceptions.InvalidValueException;
import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.exceptions.ResourceExistsException;
import com.anderson.ecommerce.model.request.ProdutoModelRequest;
import com.anderson.ecommerce.model.resource.ProdutoModelResource;
import com.anderson.ecommerce.model.response.ProdutoModelResponse;
import com.anderson.ecommerce.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    ProdutoService produtoService;

    ProdutoModelResource produtoResource;

    @BeforeEach
    public void produtoResource(){
        this.produtoResource = new ProdutoModelResource("celular", "SAMSUNG", "XLT3", new BigDecimal("1500.00"), 50);
//        this.produtoResource.setId("a");
//        this.produtoResource.setNome("celular");
//        this.produtoResource.setMarca("SAMSUNG");
//        this.produtoResource.setModelo("XLT3");
//        this.produtoResource.setPreco(new BigDecimal("1500.00"));
//        this.produtoResource.setEstoque(50);
//        this.produtoResource.setItens(null);
    }

    //Testes do método getProduto()
    @Test
    @DisplayName("getProduto() com sucesso")
    public void testGetProdutoComSucesso(){

        //Atributos
        String id = "a";

        //Simulação
        when(produtoRepository.findById(id))
                .thenReturn(Optional.of(this.produtoResource));

        //Teste
        ProdutoModelResponse produtoAtual = this.produtoService.getProduto(id);
        assertEquals(this.produtoResource.getId(), produtoAtual.getId());
        assertEquals(this.produtoResource.getModelo(), produtoAtual.getModelo());
        assertEquals(this.produtoResource.getMarca(), produtoAtual.getMarca());
    }

    @Test
    @DisplayName("getProduto() falha, pois o id não existe")
    public void testGetProdutoFalhaIdNaoExiste(){

        //Atributos
        String id = "b";

        //Simulação
        when(produtoRepository.findById(id))
                .thenReturn(Optional.ofNullable(null));

        //Teste
        NotFoundException erro = assertThrows(NotFoundException.class, () -> produtoService.getProduto(id));
        assertEquals("Não existe produto de id: " + id, erro.getMessage());
    }

    //Teste do método getProdutos()
    @Test
    @DisplayName("getProdutos() com sucesso")
    public void testGetProdutosComSucesso(){

        //parametros
        int indice = 2;
        int tamanho = 4;

        //Esperado
        long quantidadeElementos = 11;
        boolean primeiraPage = false;
        boolean ultimaPage = true;


        //Recursos
        ProdutoModelResource produtoResource1 = new ProdutoModelResource("celular", "SAMSUNG", "XLT3", new BigDecimal("1500.00"), 50);

//        produtoResource1.setId("a");
//        produtoResource1.setNome("celular");
//        produtoResource1.setMarca("SAMSUNG");
//        produtoResource1.setModelo("XLT3");
//        produtoResource1.setPreco(new BigDecimal("1500.00"));
//        produtoResource1.setEstoque(50);
//        produtoResource1.setItens(null);
        
        ProdutoModelResource produtoResource2 = new ProdutoModelResource("balde", "bbb", "baldão", new BigDecimal("25.84"), 15);

//        produtoResource2.setId("b");
//        produtoResource2.setNome("balde");
//        produtoResource2.setMarca("bbb");
//        produtoResource2.setModelo("baldão");
//        produtoResource2.setPreco(new BigDecimal("25.84"));
//        produtoResource2.setEstoque(15);
//        produtoResource2.setItens(null);
        
        ProdutoModelResource produtoResource3 = new ProdutoModelResource("computador", "lenovo", "PC_DA_NASA", new BigDecimal("5450.99"), 4);

//        produtoResource3.setId("c");
//        produtoResource3.setNome("computador");
//        produtoResource3.setMarca("lenovo");
//        produtoResource3.setModelo("PC_DA_NASA");
//        produtoResource3.setPreco(new BigDecimal("5450.99"));
//        produtoResource3.setEstoque(4);
//        produtoResource3.setItens(null);
        
        List<ProdutoModelResource> listaResource = new ArrayList<>();
        listaResource.add(produtoResource1);
        listaResource.add(produtoResource2);
        listaResource.add(produtoResource3);
        
        //Preparao para simulação
        Pageable paginacao = PageRequest.of(indice, tamanho, Sort.Direction.ASC, "nome");
        Page<ProdutoModelResource> pageResource = new PageImpl<>(listaResource, paginacao, quantidadeElementos);
        
        //Simulação
//        doReturn(pageResource).when(produtoRepository.findAll(paginacao));
        when(produtoRepository.count())
                .thenReturn(quantidadeElementos);

        when(produtoRepository.findAll(paginacao))
                .thenReturn(pageResource);


        //Teste
        Page<ProdutoModelResponse> pageRespostaAtual = this.produtoService.getProdutos(indice, tamanho);
        assertEquals(quantidadeElementos, pageRespostaAtual.getTotalElements());
        assertFalse(pageRespostaAtual.isFirst());
        assertTrue(pageRespostaAtual.isLast());
        assertEquals(3, pageRespostaAtual.getTotalPages());
    }

    @Test
    @DisplayName("getProdutos() falha, pois o indice é menor que 0")
    public void testGetProdutosFalhaIndiceMenorQue0(){

        //parametros
        int indice = -1;
        int tamanho = 4;

        //Teste
        InvalidValueException erro = assertThrows(InvalidValueException.class, () -> this.produtoService.getProdutos(indice, tamanho));
        assertEquals("Campo inválido: número da página abaixo de 0", erro.getMessage());
    }

    @Test
    @DisplayName("getProduto() falha, pois a pagina tem um tamanho menor que 1")
    public void testGetProdutosFalhaTamanhoMenorQue1(){

        //Parâmetros
        int indice = 2;
        int tamanho = 0;

        //Teste
        InvalidValueException erro = assertThrows(InvalidValueException.class, () -> this.produtoService.getProdutos(indice, tamanho));
        assertEquals("Campo inválido: o tamanho da página é menor que 1", erro.getMessage());
    }

    @Test
    @DisplayName("getProdutos() falha, pois não existe produto na page")
    public void testGetProdutosFalhaSemElementosNaPage(){

        //Parâmetros
        int indice = 4;
        int tamanho = 3;

        //Preparação
        List<ProdutoModelResource> listaResource = new ArrayList<>();
        Pageable paginacao = PageRequest.of(indice, tamanho, Sort.Direction.ASC, "nome");
        int totalElementos = 12;

        Page<ProdutoModelResource> pageResource = new PageImpl<>(listaResource, paginacao, totalElementos);

        //Simulação
        when(produtoRepository.findAll(paginacao))
                .thenReturn(pageResource);

        //Teste
        NotFoundException erro = assertThrows(NotFoundException.class, () -> this.produtoService.getProdutos(indice, tamanho));
        assertEquals("Número da Página não encontrada", erro.getMessage());
    }
    
    //Testes do método createProduto()
    @Test
    @DisplayName("createProduto() com sucesso")
    public void testCreateProdutoComSucesso(){
        //Parâmetro
        ProdutoModelRequest produtoRequest = new ProdutoModelRequest("celular", "SAMSUNG", "XLT3", new BigDecimal("1500.00"), 50);

        //Teste
        ProdutoModelResponse produtoAtual = this.produtoService.createProduto(produtoRequest);
        assertNotNull(produtoAtual);
        assertEquals("celular", produtoAtual.getNome());
        assertEquals("SAMSUNG", produtoAtual.getMarca());
        assertEquals("XLT3", produtoAtual.getModelo());
        assertNotEquals("", produtoAtual.getId());
        assertTrue(produtoAtual.getEstoque() >= 0);
        assertEquals(1, produtoAtual.getPreco().compareTo(new BigDecimal("0")));
    }

    @Test
    @DisplayName("createProduto() falha, pois já existe um produto com o mesmo nome, marca e modelo")
    public void testCreateProdutoFalhaProdutoJaFoiCadastrado(){
        //Parâmetro
        ProdutoModelRequest produtoRequest = new ProdutoModelRequest("celular", "SAMSUNG", "XLT3", new BigDecimal("1500.00"), 50);

        //Simulação
        when(produtoRepository.existsByNomeAndModeloAndMarca(produtoRequest.getNome(), produtoRequest.getModelo(), produtoRequest.getMarca()))
                .thenReturn(true);

        //Teste
        ResourceExistsException erro = assertThrows(ResourceExistsException.class, () -> this.produtoService.createProduto(produtoRequest));
        assertEquals("Já existe esse produto no sistema", erro.getMessage());
    }
}
