package com.algaworks.algafood.api.v2.controller;

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

import com.algaworks.algafood.api.v2.assembler.CozinhaDtoAssemblerV2;
import com.algaworks.algafood.api.v2.disassembler.CozinhaInputDtoDisassemblerV2;
import com.algaworks.algafood.api.v2.model.CozinhaDtoV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputDtoV2;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.services.CozinhaService;

//Esta annotation é mais completa e implementa as annotations "@Controller" e "@ResponseBody"
@RestController		
@RequestMapping(path = "/v2/cozinhas") 
public class CozinhaControllerV2 {
			
	@Autowired
	private CozinhaService cozinhaService;
	
	@Autowired
	private CozinhaDtoAssemblerV2 cozinhaDtoAssembler;
	
	@Autowired
	private CozinhaInputDtoDisassemblerV2 cozinhaDtoDisassembler;
	
	@Autowired
	private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
	
//	Como alteramos o metodo para "PagedModel", não estamos mais utilizado a classe "PageJsonSerializer" do pacote "core" para serializarmos 
//	o json de resposta. O próprio PagedModel retorna por padrão os mesmos atributos que definimos no "PageJsonSerializer"
	
	@GetMapping
	public PagedModel<CozinhaDtoV2> listar(@PageableDefault(size = 10) Pageable pageable){
		Page<Cozinha> cozinhasPage = cozinhaService.listar(pageable);
		
//		Passamos um Page de Cozinha e o objeto que converte uma Cozinha em CozinhaDto
		PagedModel<CozinhaDtoV2> cozinhasPagedModel = pagedResourcesAssembler.toModel(cozinhasPage, cozinhaDtoAssembler);
		
		return cozinhasPagedModel;
	}
	
	@GetMapping(value = "/{cozinhaId}")
	public CozinhaDtoV2 buscarId(@PathVariable("cozinhaId") Long cozinhaId) {		
		return cozinhaDtoAssembler.toModel(cozinhaService.buscarPorId(cozinhaId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)		
	public CozinhaDtoV2 adicionar (@RequestBody @Valid CozinhaInputDtoV2 cozinhaInputDto) {
		Cozinha cozinha = cozinhaDtoDisassembler.toDomainObject(cozinhaInputDto);		
		return cozinhaDtoAssembler.toModel(cozinhaService.salvar(cozinha));
	}	
	
	@PutMapping("/{cozinhaId}")
	public CozinhaDtoV2 atualizar(@PathVariable Long cozinhaId,@RequestBody @Valid CozinhaInputDtoV2 cozinhaInput){
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
