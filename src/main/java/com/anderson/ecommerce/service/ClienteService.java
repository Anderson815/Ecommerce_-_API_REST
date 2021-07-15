package com.anderson.ecommerce.service;

import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.model.resource.ClienteModelResource;
import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repository;

    public ClienteModelResponse getCliente(String id) {
        return this.clienteParaResposta(this.obterCliente(id));
    }

    //Métodos auxiliares
    private ClienteModelResource obterCliente(String id){
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("cliente", id)); //substituire o get
    }

    private ClienteModelResponse clienteParaResposta(ClienteModelResource clienteResource){

        ClienteModelResponse clienteResponse = new ClienteModelResponse();

        clienteResponse.setId(clienteResource.getId());
        clienteResponse.setNome(clienteResource.getNome());
        clienteResponse.setEmail(clienteResource.getEmail());
        clienteResponse.setSenha(clienteResource.getSenha());
        clienteResponse.setTelefone(clienteResource.getTelefone());
        clienteResponse.setCompras(clienteResource.getCompras());

        return clienteResponse;
    }

    public List<ClienteModelResponse> getClientes() {
        return null;
    }
}
