package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CozinhaService {
	
	private static final String MSG_COZINHA_EM_USO = "Cozinha de ID %d, não pode ser removida pois está em uso!";	
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	public List<Cozinha> listar(){
		return cozinhaRepository.findAll();
	}
	
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}
	
	public Cozinha buscarPorId(Long id) {
		return cozinhaRepository.findById(id).orElseThrow(
				() -> new CozinhaNaoEncontradaException(id));
	}
	
	public void deletar (Long id) {
		try {
			cozinhaRepository.deleteById(id);			
		}
//		Caso a "Cozinha" a ser deletada esteja vinculada com algum restaurante, dispara "exception" de integridade
		catch (DataIntegrityViolationException e) {			
			throw new EntidadeEmUsoException(String.format(MSG_COZINHA_EM_USO, id));
		}		
		catch (EmptyResultDataAccessException e) {
			throw new CozinhaNaoEncontradaException(id);
		}
	}
}
