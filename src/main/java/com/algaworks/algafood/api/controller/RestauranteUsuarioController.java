package com.algaworks.algafood.api.controller;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.UsuarioDtoAssembler;
import com.algaworks.algafood.api.model.UsuarioDto;
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
	
	@GetMapping
	public List<UsuarioDto> listar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
		
//		Passando a lista de Responsaveis e ordenando por nome
		return  usuarioDtoAssembler.toCollectDto(
				restaurante.getResponsaveis().stream()
				.sorted(Comparator.comparing(Usuario::getNome))
				.toList());
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
