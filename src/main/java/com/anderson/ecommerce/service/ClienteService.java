package com.anderson.ecommerce.service;

import com.anderson.ecommerce.exceptions.CreateException;
import com.anderson.ecommerce.exceptions.NotFoundException;
import com.anderson.ecommerce.exceptions.UpdateException;
import com.anderson.ecommerce.model.request.ClienteModelRequest;
import com.anderson.ecommerce.model.resource.ClienteModelResource;
import com.anderson.ecommerce.model.response.ClienteModelResponse;
import com.anderson.ecommerce.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository repository;

    public ClienteModelResponse getCliente(String id) {
        return this.clienteParaResposta(this.obterCliente(id));
    }

    public List<ClienteModelResponse> getClientes() {

        List<ClienteModelResponse> listaClienteResponse = new ArrayList<>();

        for(ClienteModelResource clienteResource : repository.findAll()){
            listaClienteResponse.add(this.clienteParaResposta(clienteResource));
        }

        return listaClienteResponse;
    }

    public ClienteModelResponse createCliente(ClienteModelRequest clienteRequest) {

        if(this.repository.existsByEmail(clienteRequest.getEmail())) throw new CreateException("Cliente", "uma conta com o e-mail informado já foi cadastrado");

        ClienteModelResource cliente = new ClienteModelResource();
        cliente.setNome(clienteRequest.getNome());
        cliente.setEmail(clienteRequest.getEmail());
        cliente.setSenha(clienteRequest.getSenha());
        cliente.setTelefone(clienteRequest.getTelefone());
        cliente.setId(UUID.randomUUID().toString());

        repository.save(cliente);

        return this.clienteParaResposta(cliente);
    }

    public ClienteModelResponse updateCliente(String id, ClienteModelRequest clienteRequest) {

        ClienteModelResource cliente = this.obterCliente(id);
        if(!clienteRequest.getEmail().equals(cliente.getEmail())) throw new UpdateException("Cliente","o e-mail não pode ser alterado");

        cliente.setNome(clienteRequest.getNome());
        cliente.setSenha(clienteRequest.getSenha());
        cliente.setTelefone(clienteRequest.getTelefone());

        return this.clienteParaResposta(cliente);
    }

    public void deleteCliente(String id) {
        repository.delete(this.obterCliente(id));
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


}
