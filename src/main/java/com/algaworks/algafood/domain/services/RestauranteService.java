package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {
	
	@Autowired
	private RestauranteRepository repository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private CidadeService cidadeService;
	
	public List<Restaurante> listar(){
		return repository.findAll();
	}
	
	@Transactional
	public Restaurante salvar (Restaurante restaurante) {
		Long cozinhaId = restaurante.getCozinha().getId();
		
		Cozinha cozinha = cozinhaService.buscarPorId(cozinhaId);
		Cidade cidade = cidadeService.buscarPorId(restaurante.getEndereco().getCidade().getId());
		
		restaurante.setCozinha(cozinha);
		restaurante.getEndereco().setCidade(cidade);
		
		return repository.save(restaurante);
	}
	
	public Restaurante buscarPorId(Long id) {		
		return repository.findById(id).orElseThrow(
				() -> new RestauranteNaoEncontradoException(id));		
	}
	
	
	public void deletar (Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new RestauranteNaoEncontradoException(id);
		}
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
//		Quando chamamos o metodo "buscarPorId", e este chama o "findById", o objeto "restaurante" fica sendo 
//		gerenciado por pelo Spring num contexto de persistencia, qualquer alteraçao feita nele automaticamente será sincronizada 
//		no banco de dados atraves de um "update" no banco, portanto não precisamos chamar o metodo "salvar".
		
		Restaurante restaurante = buscarPorId(restauranteId);
		
//		restaurante.setAtivo(true);
		restaurante.ativar();
	}
	
	@Transactional
	public void inativar(Long restauranteId) {		
		Restaurante restaurante = buscarPorId(restauranteId);
		
//		restaurante.setAtivo(false);
		restaurante.inativar();
	}
}
