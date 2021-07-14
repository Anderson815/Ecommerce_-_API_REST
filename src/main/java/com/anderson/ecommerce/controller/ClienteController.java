package com.anderson.ecommerce.controller;

import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteModelResponse> getCliente(@PathVariable(value = "id") String id){
        return ResponseEntity.ok().body(this.service.getCliente(id));
    }
}
