package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.GrupoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Permissao;
import com.algaworks.algafood.domain.repository.GrupoRepository;

@Service
public class GrupoService {
	
	private static final String MSG_GRUPO_EM_USO = "Grupo de ID %d, não pode ser removido pois está em uso!";
	
	@Autowired
	private GrupoRepository repository;
	
	@Autowired
	private PermissaoService permissaoService;
	
	public List<Grupo> listar(){
		return repository.findAll();
	}
	
	public Grupo buscarPorId(Long id) {
		return repository.findById(id).
				orElseThrow(() -> new GrupoNaoEncontradoException(id));
	}
	
	@Transactional
	public Grupo adicionar (Grupo grupo) {
		return repository.save(grupo);
	}
	
	@Transactional
	public void deletar(Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new GrupoNaoEncontradoException(id);
		}catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_GRUPO_EM_USO, id));
		}		
	}
	
	@Transactional
	public void associarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarPorId(grupoId);
		Permissao permissao = permissaoService.buscarPorId(permissaoId);
		
		grupo.associarPermissao(permissao);
	}
	
	@Transactional
	public void desassociarPermissao(Long grupoId, Long permissaoId) {
		Grupo grupo = buscarPorId(grupoId);
		Permissao permissao = permissaoService.buscarPorId(permissaoId);
		
		grupo.desassociarPermissao(permissao);
	}
}
