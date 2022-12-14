package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {
	
	@Autowired
	private RestauranteRepository repository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	public List<Restaurante> listar(){
		return repository.listar();
	}
	
	public Restaurante salvar (Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaService.buscarPorId(cozinhaId);
		
		if (cozinha == null) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe cadastro de cozinha com o ID: %d", cozinhaId));
		}
		
		return repository.salvar(restaurante);
	}
	
}
