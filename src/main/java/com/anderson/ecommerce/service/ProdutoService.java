package com.anderson.ecommerce.service;

import com.anderson.ecommerce.exceptions.InvalidValueException;
import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.model.request.ProdutoModelRequest;
import com.anderson.ecommerce.model.resource.ProdutoModelResource;
import com.anderson.ecommerce.model.response.ProdutoModelResponse;
import com.anderson.ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository repository;

    public Page<ProdutoModelResponse> getProdutos(int indice, int tamanho){

        if(indice < 0) throw new InvalidValueException("número da página abaixo de 0");
        if(tamanho < 1) throw new InvalidValueException("o tamanho da página é menor que 1");

        //Pageable é uma intrface de informações gerais da Pagina, Cabeçalho
        Pageable paginacao = PageRequest.of(indice, tamanho, Sort.Direction.ASC, "nome");

        //O page é como o ResponseExceptionDetails que só INFORMA sobre os itens da pagina, ele não faz NADA, Corpo
        Page<ProdutoModelResource> paginaProdutos = repository.findAll(paginacao);
        List<ProdutoModelResponse> listaProdutosResponse = new ArrayList<>();

        if(paginaProdutos.isEmpty()) throw new NotFoundException("Número da Página não encontrada");

        for(ProdutoModelResource produtoResource : paginaProdutos){
            listaProdutosResponse.add(this.produtoParaResposta(produtoResource));
        }

        Page<ProdutoModelResponse> paginaProdutosResposta = new PageImpl<>(listaProdutosResponse, paginacao, repository.count());

        return paginaProdutosResposta;
    }

    public ProdutoModelResponse getProduto(String id) {
        return this.produtoParaResposta(this.obterProduto(id));
    }

    //Métodos Auxiliares

    private ProdutoModelResource obterProduto(String id){
        return repository.findById(id).orElseThrow(() -> new NotFoundException("produto", id));
    }

    private ProdutoModelResponse produtoParaResposta(ProdutoModelResource produtoResource){
        return new ProdutoModelResponse(produtoResource.getId(),
                produtoResource.getNome(),
                produtoResource.getMarca(),
                produtoResource.getModelo(),
                produtoResource.getPreco(),
                produtoResource.getEstoque());
    }

    public ProdutoModelResponse createProduto(ProdutoModelRequest produtoRequest) {
        return null;
    }
}
