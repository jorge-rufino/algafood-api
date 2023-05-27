package com.algaworks.algafood.api.v1.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v1.assembler.CidadeDtoAssembler;
import com.algaworks.algafood.api.v1.disassembler.CidadeInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.CidadeDto;
import com.algaworks.algafood.api.v1.model.input.CidadeInputDto;
import com.algaworks.algafood.api.v1.openapi.controller.CidadeControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.services.CidadeService;

@RestController
@RequestMapping(path = "/v1/cidades" , produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController implements CidadeControllerOpenApi {
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private CidadeDtoAssembler cidadeDtoAssembler;
	
	@Autowired
	private CidadeInputDtoDisassembler cidadeInputDtoDisassembler;
	
	@Override
	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping
	public CollectionModel<CidadeDto> listar(){
		return cidadeDtoAssembler.toCollectionModel(cidadeService.listar());
	}
	
	@Override
	@CheckSecurity.Cidades.PodeConsultar
	@GetMapping("{cidadeId}")
	public CidadeDto buscarPorId(@PathVariable Long cidadeId){
		return cidadeDtoAssembler.toModel(cidadeService.buscarPorId(cidadeId));
	}
	
	@Override
	@CheckSecurity.Cidades.PodeEditar
	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDto adicionar (@RequestBody @Valid CidadeInputDto cidadeInputDto){
		try {
			Cidade cidade = cidadeInputDtoDisassembler.toDomainObject(cidadeInputDto); 
			CidadeDto cidadeDto =  cidadeDtoAssembler.toModel(cidadeService.salvar(cidade));

			ResourceUriHelper.addUriInResponseHeader(cidadeDto.getId());
			
			return cidadeDto;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}		
	}
	
	@Override
	@CheckSecurity.Cidades.PodeEditar
	@PutMapping("{cidadeId}")
	public CidadeDto atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInputDto cidadeInput){
		
		Cidade cidadeAtual = cidadeService.buscarPorId(cidadeId);		
		cidadeInputDtoDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
		try {
			return cidadeDtoAssembler.toModel(cidadeService.salvar(cidadeAtual));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}
	
	@Override
	@CheckSecurity.Cidades.PodeEditar
	@DeleteMapping("{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long cidadeId){
		cidadeService.deletar(cidadeId);
	}
	
}
