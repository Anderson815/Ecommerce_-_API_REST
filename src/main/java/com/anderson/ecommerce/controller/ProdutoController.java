package com.anderson.ecommerce.controller;

import com.anderson.ecommerce.model.response.ProdutoModelResponse;
import com.anderson.ecommerce.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    ProdutoService service;

    @GetMapping
    public ResponseEntity<Page<ProdutoModelResponse>> getProdutos(@RequestParam(value = "numeroPagina", defaultValue = "0") int numeroPagina, @RequestParam(value = "tamanhoPagina", defaultValue = "5") int tamanhoPagina){
        return ResponseEntity.ok(service.getProdutos(numeroPagina, tamanhoPagina));
    }
}
