package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;

public interface ProdutoRepositoryQueries {
	
	FotoProduto salvar(FotoProduto foto);
	
	void deletar(FotoProduto foto);
}
