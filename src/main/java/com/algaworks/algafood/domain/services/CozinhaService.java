package com.algaworks.algafood.domain.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@Service
public class CozinhaService {
	
	@Autowired
	private CozinhaRepository cozinhaRepository;
	
	public List<Cozinha> listar(){
		return cozinhaRepository.findAll();
	}
	
	public Cozinha salvar(Cozinha cozinha) {
		return cozinhaRepository.save(cozinha);
	}
	
	public Cozinha buscarPorId(Long id) {
		Cozinha cozinha = null;
		
//		Metodo "findById" retorna por padrão um Optional
		Optional<Cozinha> cozinhaOptional = cozinhaRepository.findById(id);
		
//		Para pegarmos o "Objeto" de um "Optional", usamos o metodo "get()"
		if (cozinhaOptional.isPresent()) {
			cozinha = cozinhaOptional.get();
		}
		
		return cozinha;
	}
	
	public void deletar (Long id) {
		try {
			cozinhaRepository.deleteById(id);
			
		}
//		Caso a "Cozinha" a ser deletada esteja vinculada com algum restaurante, dispara "exception" de integridade
		catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format("Cozinha de ID %d, não pode ser removida pois está em uso!", id));
		}		
		catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(String.format("Cozinha não existe!", id));
		}
	}
}
