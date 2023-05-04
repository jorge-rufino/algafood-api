package com.algaworks.algafood.domain.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@Service
public class RestauranteService {
	
	@Autowired
	private RestauranteRepository repository;
	
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
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
	public void desassociarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarPorId(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoService.buscarPorId(formaPagamentoId);
				
		restaurante.removerFormaPagamento(formaPagamento);
		//Lembrando que não precisamos chamar o metodo "salvar" pois está no contexto de persistencia do JPA e o update será feito.
	}
	
	@Transactional
	public void associarFormaPagamento(Long restauranteId, Long formaPagamentoId) {
		Restaurante restaurante = buscarPorId(restauranteId);
		FormaPagamento formaPagamento = formaPagamentoService.buscarPorId(formaPagamentoId);
				
		restaurante.associarFormaPagamento(formaPagamento);
		//Lembrando que não precisamos chamar o metodo "salvar" pois está no contexto de persistencia do JPA e o update será feito.
	}
	
	@Transactional
	public void ativar(Long restauranteId) {
//		Quando chamamos o metodo "buscarPorId", e este chama o "findById", o objeto "restaurante" fica sendo 
//		gerenciado por pelo Spring num contexto de persistencia, qualquer alteraçao feita nele automaticamente será sincronizada 
//		no banco de dados atraves de um "update" no banco, portanto não precisamos chamar o metodo "salvar".
		
		Restaurante restaurante = buscarPorId(restauranteId);
		restaurante.ativar();
	}
	
	@Transactional
	public void ativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::ativar);
	}
	
	@Transactional
	public void inativar(Long restauranteId) {		
		Restaurante restaurante = buscarPorId(restauranteId);
		restaurante.inativar();
	}
	
	@Transactional
	public void inativar(List<Long> restauranteIds) {
		restauranteIds.forEach(this::inativar);
	}
	
	@Transactional
	public void abrirRestaurante(Long restauranteId) {
		Restaurante restaurante = buscarPorId(restauranteId);
		restaurante.abrirRestaurante();
	}
	
	@Transactional
	public void fecharRestaurante(Long restauranteId) {
		Restaurante restaurante = buscarPorId(restauranteId);
		restaurante.fecharRestaurante();
	}
	
	@Transactional
	public void associarResponsavel(Long restauranteId, Long usuarioId) {
		Usuario usuario = usuarioService.buscarPorId(usuarioId);
		Restaurante restaurante = buscarPorId(restauranteId);
		
		restaurante.associarResponsavel(usuario);
	}
	
	@Transactional
	public void desassociarResponsavel(Long restauranteId, Long usuarioId) {
		Usuario usuario = usuarioService.buscarPorId(usuarioId);
		Restaurante restaurante = buscarPorId(restauranteId);
		
		restaurante.desassociarResponsavel(usuario);
	}
	
	public OffsetDateTime getDataUltimaAtualizacao() {
		return repository.getUltimaDataAtualizacao();
	}
	
	public boolean existsResponsavel(Long restauranteId, Long usuarioId) {
		return repository.existResponsavel(restauranteId, usuarioId);
	}
}

