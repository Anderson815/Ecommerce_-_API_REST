package com.anderson.ecommerce.repository;

import com.anderson.ecommerce.model.resource.ProdutoModelResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModelResource, String> {
    List<ProdutoModelResource> findByMarca (String marca);
    List<ProdutoModelResource> findByNomeStartingWith (String nome);
    boolean existsByNomeAndModeloAndMarca(String nome, String modelo, String Marca);
}
