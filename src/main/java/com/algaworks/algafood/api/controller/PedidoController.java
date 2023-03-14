package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoDtoAssembler;
import com.algaworks.algafood.api.assembler.PedidoResumoDtoAssembler;
import com.algaworks.algafood.api.disassembler.PedidoInputDtoDisassembler;
import com.algaworks.algafood.api.model.PedidoDto;
import com.algaworks.algafood.api.model.PedidoResumoDto;
import com.algaworks.algafood.api.model.input.PedidoInputDto;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.filter.PedidoFilter;
import com.algaworks.algafood.domain.services.PedidoService;
import com.algaworks.algafood.infrastructure.repository.spec.PedidosSpecs;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private PedidoDtoAssembler pedidoDtoAssembler;
	
	@Autowired
	private PedidoResumoDtoAssembler pedidoResumoDtoAssembler;
	
	@Autowired
	private PedidoInputDtoDisassembler pedidoInputDtoDisassembler;
	
//	@GetMapping
//	public MappingJacksonValue listar(@RequestParam(required = false) String campos){
//		List<PedidoResumoDto> pedidosResumoDto = pedidoResumoDtoAssembler.toCollectDto(pedidoService.listar());
//		
////		Envelopa a lista
//		MappingJacksonValue pedidosWrapper = new MappingJacksonValue(pedidosResumoDto);
//		
////		Criamos o filtro passando o nome do filtro e os campos que queremos mostrar na representaçao
//		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//		filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());	//Mostra todos os campos
//		
////		Filtra os campos caso sejam passados na requisiçao separados por ","
//			StringUtils do pacote "CommonsLang3"
//		if(StringUtils.isNotBlank(campos)) {
//			filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
//		}
//		
//		pedidosWrapper.setFilters(filterProvider);
//		
//		return pedidosWrapper;
//	}
	
//	@GetMapping("{pedidoId}")
//	public PedidoDto buscarPorId(@PathVariable Long pedidoId) {
//		return pedidoDtoAssembler.toDto(pedidoService.buscarPorId(pedidoId));
//	}
	
//	Mesmo sem o @RequestParam o spring consegue fazer o databind corretamente dos filtros
	@GetMapping
	public Page<PedidoResumoDto> pesquisar(PedidoFilter filtro, Pageable pageable){
		
		Page<Pedido> pedidosPage = pedidoService.listar(PedidosSpecs.usandoFiltro(filtro), pageable);
		
		List<PedidoResumoDto> pedidosResumoDto = pedidoResumoDtoAssembler.toCollectDto(pedidosPage.getContent());
		
		Page<PedidoResumoDto> pedidosResumoDtoPage = new PageImpl<>(pedidosResumoDto, pageable, pedidosPage.getTotalElements());
		
		return pedidosResumoDtoPage;
	}
	
	@GetMapping("{codigoPedido}")
	public PedidoDto buscarPorCodigo(@PathVariable String codigoPedido) {
		return pedidoDtoAssembler.toDto(pedidoService.buscarPorCodigo(codigoPedido));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoDto emitir(@RequestBody @Valid PedidoInputDto pedidoInput) {
		try {
			Pedido novoPedido = pedidoInputDtoDisassembler.toDomainObject(pedidoInput);
			novoPedido.setCliente(new Usuario());
			novoPedido.getCliente().setId(1L);
			
			return pedidoDtoAssembler.toDto(pedidoService.emitir(novoPedido));
//	Convertermos os erros de "EntidadeNaoEncontrada" de "404" para BadRequest "400"
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
}
