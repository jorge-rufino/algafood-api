package com.algaworks.algafood.domain.repository;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Produto;

@Repository
public interface ProdutoRepository extends CustomJpaRepository<Produto, Long>{

}
