package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.repository.EstadoRepository;

@Service
public class CidadeService {
	
	@Autowired
	private CidadeRepository repository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	public List<Cidade> listar(){
		return repository.listar();				
	}
	
	public Cidade salvar (Cidade cidade) {
		Long estadoId = cidade.getEstado().getId();
		Estado estado = estadoRepository.buscarPorId(estadoId);
		
		if(estado == null) {
			throw new EntidadeNaoEncontradaException(String.format("Não existe cadastro de Estado com o ID: %d", estadoId));
		}
		
		cidade.setEstado(estado);
		
		return repository.salvar(cidade);
	}
	
	public Cidade buscarPorId(Long id) {
		return repository.buscarPorId(id);
	}
	
	public void deletar (Long id) {
		try {
			repository.deletar(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Cidade não existe!", id));
		}		
	}
}
