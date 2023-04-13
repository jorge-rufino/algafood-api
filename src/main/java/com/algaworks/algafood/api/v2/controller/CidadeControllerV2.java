package com.algaworks.algafood.api.v2.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
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

import com.algaworks.algafood.api.ResourceUriHelper;
import com.algaworks.algafood.api.v2.assembler.CidadeDtoAssemblerV2;
import com.algaworks.algafood.api.v2.disassembler.CidadeInputDtoDisassemblerV2;
import com.algaworks.algafood.api.v2.model.CidadeDtoV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputDtoV2;
import com.algaworks.algafood.core.web.AlgaMediaTypes;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.services.CidadeService;

@RestController
@RequestMapping(path = "/cidades" , produces = AlgaMediaTypes.V2_APPLICATION_JSON_VALUE)
public class CidadeControllerV2 {
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private CidadeDtoAssemblerV2 cidadeDtoAssembler;
	
	@Autowired
	private CidadeInputDtoDisassemblerV2 cidadeInputDtoDisassembler;
	
	@GetMapping
	public CollectionModel<CidadeDtoV2> listar(){
		return cidadeDtoAssembler.toCollectionModel(cidadeService.listar());
	}
	
	@GetMapping("{cidadeId}")
	public CidadeDtoV2 buscarPorId(@PathVariable Long cidadeId){
		return cidadeDtoAssembler.toModel(cidadeService.buscarPorId(cidadeId));
	}
	
	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDtoV2 adicionar (@RequestBody @Valid CidadeInputDtoV2 cidadeInputDto){
		try {
			Cidade cidade = cidadeInputDtoDisassembler.toDomainObject(cidadeInputDto); 
			CidadeDtoV2 cidadeDto =  cidadeDtoAssembler.toModel(cidadeService.salvar(cidade));

			ResourceUriHelper.addUriInResponseHeader(cidadeDto.getIdCidade());
			
			return cidadeDto;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}		
	}
	
	@PutMapping("{id}")
	public CidadeDtoV2 atualizar(@PathVariable Long id, @RequestBody @Valid CidadeInputDtoV2 cidadeInput){
		
		Cidade cidadeAtual = cidadeService.buscarPorId(id);		
		cidadeInputDtoDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
		try {
			return cidadeDtoAssembler.toModel(cidadeService.salvar(cidadeAtual));
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}
	
	@DeleteMapping("{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long cidadeId){
		cidadeService.deletar(cidadeId);
	}
	
}
