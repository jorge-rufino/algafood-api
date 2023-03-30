package com.algaworks.algafood.api.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.CozinhaDtoAssembler;
import com.algaworks.algafood.api.disassembler.CozinhaInputDtoDisassembler;
import com.algaworks.algafood.api.model.CozinhaDto;
import com.algaworks.algafood.api.model.input.CozinhaInputDto;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.services.CozinhaService;

//Esta annotation é mais completa e implementa as annotations "@Controller" e "@ResponseBody"
@RestController		
@RequestMapping(value = "/cozinhas") 
public class CozinhaController {
			
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private CozinhaDtoAssembler cozinhaDtoAssembler;
	
	@Autowired
	private CozinhaInputDtoDisassembler cozinhaDtoDisassembler;
	
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
//	Como alteramos o metodo para "PagedModel", não estamos mais utilizado a classe "PageJsonSerializer" do pacote "core" para serializarmos 
//	o json de resposta. O próprio PagedModel retorna por padrão os mesmos atributos que definimos no "PageJsonSerializer"
	
	@GetMapping
	public PagedModel<CozinhaDto> listar(@PageableDefault(size = 10) Pageable pageable){
		Page<Cozinha> cozinhasPage = cozinhaService.listar(pageable);
		
//		Passamos um Page de Cozinha e o objeto que converte uma Cozinha em CozinhaDto
		PagedModel<CozinhaDto> cozinhasPagedModel = pagedResourcesAssembler.toModel(cozinhasPage, cozinhaDtoAssembler);
		
		return cozinhasPagedModel;
	}
	
	@GetMapping(value = "/{cozinhaId}")
	public CozinhaDto buscarId(@PathVariable("cozinhaId") Long cozinhaId) {		
		return cozinhaDtoAssembler.toModel(cozinhaService.buscarPorId(cozinhaId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)		
	public CozinhaDto adicionar (@RequestBody @Valid CozinhaInputDto cozinhaInputDto) {
		Cozinha cozinha = cozinhaDtoDisassembler.toDomainObject(cozinhaInputDto);		
		return cozinhaDtoAssembler.toModel(cozinhaService.salvar(cozinha));
	}	
	
	@PutMapping("/{cozinhaId}")
	public CozinhaDto atualizar(@PathVariable Long cozinhaId,@RequestBody @Valid CozinhaInputDto cozinhaInput){
		Cozinha cozinhaAtual = cozinhaService.buscarPorId(cozinhaId);		
		cozinhaDtoDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		return cozinhaDtoAssembler.toModel(cozinhaService.salvar(cozinhaAtual));
	}
	
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long cozinhaId){								
		cozinhaService.deletar(cozinhaId);
	}
}
