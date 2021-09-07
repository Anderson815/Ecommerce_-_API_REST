package com.anderson.ecommerce.service;

import com.anderson.ecommerce.exceptions.NotFoundException;
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

import java.math.BigDecimal;
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
        this.produtoResource = new ProdutoModelResource();
        this.produtoResource.setId("a");
        this.produtoResource.setNome("celular");
        this.produtoResource.setMarca("SAMSUNG");
        this.produtoResource.setModelo("XLT3");
        this.produtoResource.setPreco(new BigDecimal("1500.00"));
        this.produtoResource.setEstoque(50);
        this.produtoResource.setItens(null);
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
}
