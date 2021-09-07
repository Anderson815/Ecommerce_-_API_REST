package com.anderson.ecommerce.service;

import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.model.resource.ProdutoModelResource;
import com.anderson.ecommerce.model.response.ProdutoModelResponse;
import com.anderson.ecommerce.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository repository;

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
}
