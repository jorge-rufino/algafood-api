package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	@GetMapping
	public List<CozinhaDto> listar(){
		return cozinhaDtoAssembler.toCollectionDto(cozinhaService.listar());
	}
	
	@GetMapping(value = "/{cozinhaId}")
	public CozinhaDto buscarId(@PathVariable("cozinhaId") Long cozinhaId) {		
		return cozinhaDtoAssembler.toDto(cozinhaService.buscarPorId(cozinhaId));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)		
	public CozinhaDto adicionar (@RequestBody @Valid CozinhaInputDto cozinhaInputDto) {
		Cozinha cozinha = cozinhaDtoDisassembler.toDomainObject(cozinhaInputDto);		
		return cozinhaDtoAssembler.toDto(cozinhaService.salvar(cozinha));
	}	
	
	@PutMapping("/{cozinhaId}")
	public CozinhaDto atualizar(@PathVariable Long cozinhaId,@RequestBody @Valid CozinhaInputDto cozinhaInput){
		Cozinha cozinhaAtual = cozinhaService.buscarPorId(cozinhaId);		
		cozinhaDtoDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
		
		return cozinhaDtoAssembler.toDto(cozinhaService.salvar(cozinhaAtual));
	}
	
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long cozinhaId){								
		cozinhaService.deletar(cozinhaId);
	}
}
