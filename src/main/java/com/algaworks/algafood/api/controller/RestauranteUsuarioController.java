package com.algaworks.algafood.api.controller;

import java.util.Comparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.AlgaLink;
import com.algaworks.algafood.api.assembler.UsuarioDtoAssembler;
import com.algaworks.algafood.api.model.UsuarioDto;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.services.RestauranteService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/responsaveis")
public class RestauranteUsuarioController {
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private UsuarioDtoAssembler usuarioDtoAssembler;
	
	@Autowired
	private AlgaLink algaLinks;
	
	@GetMapping
	public CollectionModel<UsuarioDto> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
		
		if (restaurante.getResponsaveis().isEmpty()) {
			throw new UsuarioNaoEncontradoException("Restaurante de ID " + restaurante.getId() + " ainda não tem responsável.");
		}
		
//		Passando a lista de Responsaveis e ordenando por nome
		return  usuarioDtoAssembler.toCollectionModel(
				restaurante.getResponsaveis().stream()
				.sorted(Comparator.comparing(Usuario::getNome))
				.toList())
				.removeLinks()		//Remove o link do método "listar()" da classe "Usuario"
				.add(algaLinks.linkToResponsaveisRestaurante(restaurante.getId()));
	}
	
	@PutMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void associarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.associarResponsavel(restauranteId, usuarioId);
	}
	
	@DeleteMapping("/{usuarioId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void desassociarResponsavel(@PathVariable Long restauranteId, @PathVariable Long usuarioId) {
		restauranteService.desassociarResponsavel(restauranteId, usuarioId);
	}

}
