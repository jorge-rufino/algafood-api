package com.algaworks.algafood.api.v1.controller;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.UsuarioDtoAssembler;
import com.algaworks.algafood.api.v1.model.UsuarioDto;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.services.RestauranteService;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private UsuarioDtoAssembler usuarioDtoAssembler;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@GetMapping
	public CollectionModel<UsuarioDto> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
		
		if (restaurante.getResponsaveis().isEmpty()) {
			throw new UsuarioNaoEncontradoException("Restaurante de ID " + restaurante.getId() + " ainda não tem responsável.");
		}
		
		CollectionModel<UsuarioDto> usuariosDto =  usuarioDtoAssembler.toCollectionModel(
				restaurante.getResponsaveis().stream()
				.sorted(Comparator.comparing(Usuario::getNome))
				.toList())
				.removeLinks()		//Remove o link do método "listar()" da classe "Usuario"
				.add(algaLinks.linkToResponsaveisRestaurante(restaurante.getId()))
				.add(algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));
		
		usuariosDto.getContent().forEach(usuarioDto -> {
			usuarioDto.add(algaLinks.linkToRestauranteResponsavelDesassociacao(restaurante.getId(), usuarioDto.getId(), "desassociar"));
		});
		
		return usuariosDto;
	}
	
	@PutMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> associarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.associarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> desassociarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.desassociarResponsavel(restauranteId, usuarioId);
		return ResponseEntity.noContent().build();
	}

}
