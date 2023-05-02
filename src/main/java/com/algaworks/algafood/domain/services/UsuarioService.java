package com.algaworks.algafood.domain.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private GrupoService grupoService;
	
	@Autowired
	private PasswordEncoder passwordEncoder; 
	
	public List<Usuario> listar(){
		return repository.findAll();
	}
	
	public Usuario buscarPorId(Long id) {
		return repository.findById(id)
				.orElseThrow(() -> new UsuarioNaoEncontradoException(id));
	}
	
	@Transactional
	public Usuario salvar(Usuario usuario) {
		
//Faz com que o "usuario" saia do contexto de persistencia do JPA evitando assim que aconteça a duplicação de usuarios
//no momento do "findByEmail"
		
		repository.detach(usuario);
		
		Optional<Usuario> usuarioExistente = repository.findByEmail(usuario.getEmail());
		
//Como este metodo serve tanto para salvar quanto para atualizar, devemos verificar se o usuario(usuarioExistente) que ele buscou é o mesmo
//que está sendo recebido como parametro(usuario). Se forem iguais então é um update portanto não irá disparar a exception
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
			throw new NegocioException(String.format("Já existe um usuário cadastrado com o e-mail: '%s'", usuario.getEmail()));
		}
				
		if(usuario.isNovo()) {
			usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
		}
		
		return repository.save(usuario);
	}
	
	@Transactional
	public void deletar(Long id) {
		try {
			repository.deleteById(id);
			repository.flush();
			
		} catch (EmptyResultDataAccessException e) {
			throw new UsuarioNaoEncontradoException(id);
		}		
	}
	
	@Transactional
	public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
		Usuario usuario = buscarPorId(id);
		
		if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }
        
		usuario.setSenha(passwordEncoder.encode(novaSenha));
	}
	
	@Transactional
	public void associarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarPorId(usuarioId);
		Grupo grupo = grupoService.buscarPorId(grupoId);
		
		usuario.associarGrupo(grupo);
	}
	
	@Transactional
	public void desassociarGrupo(Long usuarioId, Long grupoId) {
		Usuario usuario = buscarPorId(usuarioId);
		Grupo grupo = grupoService.buscarPorId(grupoId);
		
		usuario.desassociarGrupo(grupo);
	}
}
