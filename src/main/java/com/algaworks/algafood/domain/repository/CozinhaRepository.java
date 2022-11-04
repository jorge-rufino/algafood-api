package com.algaworks.algafood.domain.repository;

import java.util.List;

import com.algaworks.algafood.domain.model.Cozinha;

public interface CozinhaRepository {
	
	List<Cozinha> listar();
	Cozinha buscarPorId(Long id);
	Cozinha salvar(Cozinha cozinha);
	void deletar(Cozinha cozinha);
	
}
