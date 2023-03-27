package com.algaworks.algafood.domain.services;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;

@Service
public class FormaPagamentoService {

	private static final String MSG_FORMA_PAGAMENTO_EM_USO = "Forma de Pagamento de ID %d, não pode ser removido pois está em uso!";
	
	@Autowired
	private FormaPagamentoRepository repository;
	
	public List<FormaPagamento> listar(){
		return repository.findAll();
	}
	
	public FormaPagamento buscarPorId(Long id) {
		return repository.findById(id).orElseThrow(
				() -> new FormaPagamentoNaoEncontradoException(id));
	}
	
	@Transactional
	public FormaPagamento salvar (FormaPagamento formaPagamento) {
		return repository.save(formaPagamento);
	}
	
	public void deletar (Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
		} catch (EmptyResultDataAccessException e) {
			throw new FormaPagamentoNaoEncontradoException(id);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_FORMA_PAGAMENTO_EM_USO, id));
		}				
	}
	
	public OffsetDateTime getDataUltimaAtualizacao() {
		return repository.getDataUltimaAtualizacao();
	}
	
	public OffsetDateTime getDataAtualizacaoById(Long id) {
		return repository.getDataAtualizacaoById(id);
	}
}
