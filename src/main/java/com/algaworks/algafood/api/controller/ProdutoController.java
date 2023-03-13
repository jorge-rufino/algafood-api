package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.ProdutoDtoAssembler;
import com.algaworks.algafood.api.disassembler.ProdutoInputDtoDisassembler;
import com.algaworks.algafood.api.model.ProdutoDto;
import com.algaworks.algafood.api.model.input.ProdutoInputDto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.services.ProdutoService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoDtoAssembler produtoDtoAssembler;
	
	@Autowired
	private ProdutoInputDtoDisassembler produtoInputDtoDisassembler;
	
	@GetMapping
	public List<ProdutoDto> listar(@PathVariable Long restauranteId, @RequestParam(required = false) boolean incluirInativos){		
		return produtoDtoAssembler.toCollectDto(produtoService.listar(restauranteId, incluirInativos));
	}
	
	@GetMapping("/{produtoId}")
	public ProdutoDto buscarPorId(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		return produtoDtoAssembler.toDto(produtoService.buscarPorId(restauranteId, produtoId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoDto adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInputDto produtoInput) {
		Produto produto = produtoInputDtoDisassembler.toDomainObject(produtoInput);
		return produtoDtoAssembler.toDto(produtoService.salvar(restauranteId, produto));
	}
	
	@PutMapping("{produtoId}")
	public ProdutoDto atualizar(
			@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInputDto produtoInput) {
		Produto produtoAtual = produtoService.buscarPorId(restauranteId, produtoId);
		produtoInputDtoDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		
		return produtoDtoAssembler.toDto(produtoService.salvar(restauranteId, produtoAtual));
	}
	
}
