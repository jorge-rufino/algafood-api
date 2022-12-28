package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadoRepository repository;
	
	public List<Estado> listar(){
		return repository.findAll();
	}
	
	public Estado buscarPorId(Long id) {
		return repository.findById(id).orElse(null);
	}
	
	public Estado salvar (Estado estado) {
		return repository.save(estado);
	}
	
	public void deletar (Long id) {		
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Estado não existe!", id));
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Estado de ID %d, não pode ser removido pois está em uso!", id));
		}		
	}
}
