package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.assembler.ProdutoDtoAssembler;
import com.algaworks.algafood.api.v1.disassembler.ProdutoInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.ProdutoDto;
import com.algaworks.algafood.api.v1.model.input.ProdutoInputDto;
import com.algaworks.algafood.api.v1.openapi.controller.ProdutoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.services.ProdutoService;

@RestController
@RequestMapping(path = "/v1/restaurantes/{restauranteId}/produtos")
public class ProdutoController implements ProdutoControllerOpenApi {

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ProdutoDtoAssembler produtoDtoAssembler;
	
	@Autowired
	private ProdutoInputDtoDisassembler produtoInputDtoDisassembler;
	
	@Autowired
	private AlgaLinks algaLinks; 
	
//	Alteramos para "Boolean" para poder receber "null" quando formos chamar o link
//	Como precisamos do id do restaurante, adicionamos o link da coleçao aqui
	
	@Override
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping
	public CollectionModel<ProdutoDto> listar(@PathVariable Long restauranteId, 
			@RequestParam(required = false, defaultValue = "false") Boolean incluirInativos){		
		return produtoDtoAssembler.toCollectionModel(produtoService.listar(restauranteId, incluirInativos))
				.add(algaLinks.linkToProdutos(restauranteId));
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeConsultar
	@GetMapping("/{produtoId}")
	public ProdutoDto buscarPorId(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		return produtoDtoAssembler.toModel(produtoService.buscarPorId(restauranteId, produtoId));
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ProdutoDto adicionar(@PathVariable Long restauranteId, @RequestBody @Valid ProdutoInputDto produtoInput) {
		Produto produto = produtoInputDtoDisassembler.toDomainObject(produtoInput);
		return produtoDtoAssembler.toModel(produtoService.salvar(restauranteId, produto));
	}
	
	@Override
	@CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
	@PutMapping("{produtoId}")
	public ProdutoDto atualizar(
			@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestBody @Valid ProdutoInputDto produtoInput) {
		Produto produtoAtual = produtoService.buscarPorId(restauranteId, produtoId);
		produtoInputDtoDisassembler.copyToDomainObject(produtoInput, produtoAtual);
		
		return produtoDtoAssembler.toModel(produtoService.salvar(restauranteId, produtoAtual));
	}
	
}
