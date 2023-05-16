package com.algaworks.algafood.api.v1.controller;

import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import com.algaworks.algafood.api.v1.assembler.FormaPagamentoDtoAssembler;
import com.algaworks.algafood.api.v1.disassembler.FormaPagamentoInputDtoDisassembler;
import com.algaworks.algafood.api.v1.model.FormaPagamentoDto;
import com.algaworks.algafood.api.v1.model.input.FormaPagamentoInputDto;
import com.algaworks.algafood.api.v1.openapi.controller.FormaPagamentoControllerOpenApi;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.services.FormaPagamentoService;

@RestController
@RequestMapping(path = "/v1/formaPagamentos")
public class FormaPagamentoController implements FormaPagamentoControllerOpenApi {
	
	@Autowired
	private FormaPagamentoService service;
	
	@Autowired
	private FormaPagamentoDtoAssembler formaPagamentoDtoAssembler;
	
	@Autowired
	private FormaPagamentoInputDtoDisassembler formaPagamentoDisassembler;
	
//	Com DeepTags, o método só será executado completamente quando a "ETag" e "If-None-Match" forem diferentes
	@Override
	@CheckSecurity.FormasPagamento.PodeConsultar
	@GetMapping
	public ResponseEntity<CollectionModel<FormaPagamentoDto>> listar(ServletWebRequest request) {
		
//		Desativa a geraçao automatica de Etags para este método.
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = service.getDataUltimaAtualizacao();
		
		if(dataUltimaAtualizacao != null) {
//			Alteramos o eTag pegando a quantidade em segundos
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
//		Compara o "If-None-Match" com o ETag. Poderiamos somente retornar "null" que o status 304 seria retornado, porém para melhorar 
//		a legibilidade do código, fiz esta implementaçao
		
		if(request.checkNotModified(eTag)) {
//			return null;
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED) 	//Status 304
		               .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic())
		               .eTag(eTag)
		               .build();
		}
		
		CollectionModel<FormaPagamentoDto> formasPagamentosDto = formaPagamentoDtoAssembler.toCollectionModel(service.listar());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
//				Retorna o eTag
				.eTag(eTag)				
				.body(formasPagamentosDto); 
	}
	
	@Override
	@CheckSecurity.FormasPagamento.PodeConsultar
	@GetMapping("/{id}")
	public ResponseEntity<FormaPagamentoDto> buscarId(@PathVariable Long id, ServletWebRequest request) {
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = service.getDataAtualizacaoById(id);
		
		if (dataUltimaAtualizacao != null) {
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
		if (request.checkNotModified(eTag)) {
			return ResponseEntity.status(HttpStatus.NOT_MODIFIED) 	//Status 304
		               .cacheControl(CacheControl.maxAge(0, TimeUnit.SECONDS).cachePublic())
		               .eTag(eTag)
		               .build();
		}
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
				.eTag(eTag)
				.body(formaPagamentoDtoAssembler.toModel(service.buscarPorId(id)));
	}
	
	@Override
	@CheckSecurity.FormasPagamento.PodeEditar
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoDto adicionar (@RequestBody @Valid FormaPagamentoInputDto formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoDisassembler.toDomainObject(formaPagamentoInput);
		return formaPagamentoDtoAssembler.toModel(service.salvar(formaPagamento));
	}
	
	@Override
	@CheckSecurity.FormasPagamento.PodeEditar
	@PutMapping("/{id}")
	public FormaPagamentoDto atualizar (@PathVariable Long id, @RequestBody @Valid FormaPagamentoInputDto formaPagamentoInput) {
		FormaPagamento formaPagamentoAtual = service.buscarPorId(id);
		formaPagamentoDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);		
		
		return formaPagamentoDtoAssembler.toModel(service.salvar(formaPagamentoAtual));
	}
	
	@Override
	@CheckSecurity.FormasPagamento.PodeEditar
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.deletar(id);
	}
	
}
