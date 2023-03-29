package com.algaworks.algafood.api.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import com.algaworks.algafood.api.assembler.CidadeDtoAssembler;
import com.algaworks.algafood.api.disassembler.CidadeInputDtoDisassembler;
import com.algaworks.algafood.api.model.CidadeDto;
import com.algaworks.algafood.api.model.input.CidadeInputDto;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.services.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private CidadeDtoAssembler cidadeDtoAssembler;
	
	@Autowired
	private CidadeInputDtoDisassembler cidadeInputDtoDisassembler;
	
	@GetMapping
	public CollectionModel<CidadeDto> listar(){
		
		List<CidadeDto> cidadesDto = cidadeDtoAssembler.toCollectionDto(cidadeService.listar()); 
		
		cidadesDto.forEach(cidadeDto -> {
			cidadeDto.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).buscarPorId(cidadeDto.getId()))
					.withSelfRel());
	
			cidadeDto.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).listar()).withRel("cidades"));
		
			cidadeDto.getEstado().add(WebMvcLinkBuilder.linkTo(methodOn(EstadoController.class).buscarId(cidadeDto.getEstado().getId()))
						.withSelfRel());
		});
		
		CollectionModel<CidadeDto> cidadesCollectionModel = CollectionModel.of(cidadesDto);
		
		cidadesCollectionModel.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel());
		
		return cidadesCollectionModel;
	}
	
	@GetMapping("{cidadeId}")
	public CidadeDto buscarPorId(@PathVariable Long cidadeId){
		CidadeDto cidadeDto = cidadeDtoAssembler.toDto(cidadeService.buscarPorId(cidadeId));
		

		cidadeDto.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).buscarPorId(cidadeDto.getId()))
						.withSelfRel());
		
		cidadeDto.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).listar()).withRel("cidades"));
		
		cidadeDto.getEstado().add(WebMvcLinkBuilder.linkTo(methodOn(EstadoController.class).buscarId(cidadeDto.getEstado().getId()))
						.withSelfRel());
		
		return cidadeDto;
	}
	
	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public CidadeDto adicionar (@RequestBody @Valid CidadeInputDto cidadeInputDto){
		try {
			Cidade cidade = cidadeInputDtoDisassembler.toDomainObject(cidadeInputDto); 
			CidadeDto cidadeDto =  cidadeDtoAssembler.toDto(cidadeService.salvar(cidade));

			ResourceUriHelper.addUriInResponseHeader(cidadeDto.getId());
			
			return cidadeDto;
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}		
	}
	
	@PutMapping("{id}")
	public CidadeDto atualizar(@PathVariable Long id, @RequestBody @Valid CidadeInputDto cidadeInput){
		
		Cidade cidadeAtual = cidadeService.buscarPorId(id);		
		cidadeInputDtoDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);
			
		try {
			return cidadeDtoAssembler.toDto(cidadeService.salvar(cidadeAtual));
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
