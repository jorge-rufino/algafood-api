package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;

@Service
public class CidadeService {
		
	private static final String MSG_CIDADE_EM_USO = "Cidade de ID %d, não pode ser removido pois está em uso!";

	@Autowired
	private CidadeRepository repository;
	
	@Autowired
	private EstadoService estadoService;
	
	public List<Cidade> listar(){
		return repository.findAll();				
	}
	
	@Transactional
	public Cidade salvar (Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoService.buscarPorId(estadoId);		
		
		cidade.setEstado(estado);
		
		return repository.save(cidade);
	}
	
	public Cidade buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(
				() -> new CidadeNaoEncontradaException(id));
	}
	
	@Transactional
	public void deletar (Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new CidadeNaoEncontradaException(id);
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_CIDADE_EM_USO, id));
		}				
	}
}
