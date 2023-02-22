package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class EstadoService {
	
	private static final String MSG_ESTADO_EM_USO = "Estado de ID %d, não pode ser removido pois está em uso!";	
	
	@Autowired
	private EstadoRepository repository;
	
	public List<Estado> listar(){
		return repository.findAll();
	}
	
	public Estado buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(
				() -> new EstadoNaoEncontradoException(id));
	}
	
	@Transactional
	public Estado salvar (Estado estado) {
		return repository.save(estado);
	}
	
	@Transactional
	public void deletar (Long id) {		
		try {
			repository.deleteById(id);
			repository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new EstadoNaoEncontradoException(id);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_ESTADO_EM_USO, id));
		}		
	}
}
