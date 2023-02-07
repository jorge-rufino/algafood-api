package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {
	
	private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Restaurante de ID %d não existe!";

	@Autowired
	private RestauranteRepository repository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	public List<Restaurante> listar(){
		return repository.findAll();
	}
	
	public Restaurante salvar (Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		Cozinha cozinha = cozinhaService.buscarPorId(cozinhaId);
		
		restaurante.setCozinha(cozinha);
		
		return repository.save(restaurante);
	}
	
	public Restaurante buscarPorId(Long id) {		
		return repository.findById(id).orElseThrow(
				() -> new EntidadeNaoEncontradaException(String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id)));		
	}
	
	public void deletar (Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format(MSG_RESTAURANTE_NAO_ENCONTRADO, id));
		}
		
	}
}
