package com.algaworks.algafood.api.controller;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.algaworks.algafood.api.assembler.FormaPagamentoDtoAssembler;
import com.algaworks.algafood.api.disassembler.FormaPagamentoInputDtoDisassembler;
import com.algaworks.algafood.api.model.FormaPagamentoDto;
import com.algaworks.algafood.api.model.input.FormaPagamentoInputDto;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.services.FormaPagamentoService;

@RestController
@RequestMapping("/formaPagamentos")
public class FormaPagamentoController {
	
	@Autowired
	private FormaPagamentoService service;
	
	@Autowired
	private FormaPagamentoDtoAssembler formaPagamentoDtoAssembler;
	
	@Autowired
	private FormaPagamentoInputDtoDisassembler formaPagamentoDisassembler;
	
//	Com DeepTags, o método só será executado completamente quando a "ETag" e "If-None-Match" forem diferentes
	@GetMapping
	public ResponseEntity<List<FormaPagamentoDto>> listar(ServletWebRequest request) {
		
//		Desativa a geraçao automatica de Etags para este método.
		ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());
		
		String eTag = "0";
		
		OffsetDateTime dataUltimaAtualizacao = service.getDataUltimaAtualizacao();
		
		if(dataUltimaAtualizacao != null) {
//			Alteramos o eTag pegando a quantidade em segundos
			eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
		}
		
//		Compara o "If-None-Match" com o ETag. Se forem iguais retorna "Null", se não continua a execução metodo
		if(request.checkNotModified(eTag)) {
			return null;
		}
		
		List<FormaPagamentoDto> formasPagamentosDto = formaPagamentoDtoAssembler.toCollectionDto(service.listar());
		
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
//				Retorna o eTag
				.eTag(eTag)				
				.body(formasPagamentosDto); 
	}
	
	@GetMapping("/{id}")
	public FormaPagamentoDto buscarId(@PathVariable Long id) {
		return formaPagamentoDtoAssembler.toDto(service.buscarPorId(id));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormaPagamentoDto adicionar (@RequestBody @Valid FormaPagamentoInputDto formaPagamentoInput) {
		FormaPagamento formaPagamento = formaPagamentoDisassembler.toDomainObject(formaPagamentoInput);
		return formaPagamentoDtoAssembler.toDto(service.salvar(formaPagamento));
	}
	
	@PutMapping("/{id}")
	public FormaPagamentoDto atualizar (@PathVariable Long id, @RequestBody @Valid FormaPagamentoInputDto formaPagamentoInput) {
		FormaPagamento formaPagamentoAtual = service.buscarPorId(id);
		formaPagamentoDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);		
		
		return formaPagamentoDtoAssembler.toDto(service.salvar(formaPagamentoAtual));
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		service.deletar(id);
	}
	
}
