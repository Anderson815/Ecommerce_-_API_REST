package com.anderson.ecommerce.controller;

import com.anderson.ecommerce.exceptions.InvalidValueException;
import com.anderson.ecommerce.model.request.ClienteModelRequest;
import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteModelResponse> getCliente(@PathVariable(value = "id") String id){
        return ResponseEntity.ok().body(this.service.getCliente(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteModelResponse>> getClintes(){
        return ResponseEntity.ok().body(service.getClientes());
    }

    @PostMapping
    public ResponseEntity<ClienteModelResponse> createCliente(@Valid @RequestBody ClienteModelRequest cliente, BindingResult erro){
        if(erro.hasErrors()) throw new InvalidValueException("Cliente", erro.getAllErrors().get(0).getDefaultMessage());
        return new ResponseEntity<>(service.createCliente(cliente), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCliente(@PathVariable(value = "id") String id){
        service.deleteCliente(id);
        return ResponseEntity.noContent().build();
    }
}
