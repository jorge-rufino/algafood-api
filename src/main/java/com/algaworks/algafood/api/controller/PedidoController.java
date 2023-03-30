package com.algaworks.algafood.api.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
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
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PageableTranslate;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
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

	@Autowired
	private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;
	
//	Mesmo sem o @RequestParam o spring consegue fazer o databind corretamente dos filtros
	@GetMapping
	public PagedModel<PedidoResumoDto> pesquisar(PedidoFilter filtro, Pageable pageable){
		
		Pageable pageableTraduzido = traduzirPageable(pageable);
		
		Page<Pedido> pedidosPage = pedidoService.listar(PedidosSpecs.usandoFiltro(filtro), pageableTraduzido);
		
		pedidosPage = new PageWrapper<>(pedidosPage, pageable);
		
		PagedModel<PedidoResumoDto> pedidoPagedModel = pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoDtoAssembler);
		
		return pedidoPagedModel;
	}
	
	@GetMapping("{codigoPedido}")
	public PedidoDto buscarPorCodigo(@PathVariable String codigoPedido) {
		return pedidoDtoAssembler.toModel(pedidoService.buscarPorCodigo(codigoPedido));
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoDto emitir(@RequestBody @Valid PedidoInputDto pedidoInput) {
		try {
			Pedido novoPedido = pedidoInputDtoDisassembler.toDomainObject(pedidoInput);
			novoPedido.setCliente(new Usuario());
			novoPedido.getCliente().setId(1L);
			
			return pedidoDtoAssembler.toModel(pedidoService.emitir(novoPedido));
//	Convertermos os erros de "EntidadeNaoEncontrada" de "404" para BadRequest "400"
		} catch (EntidadeNaoEncontradaException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}
	
//	Converte/traduz os nomes dos filtros/parametros da requisição para os nomes corretos que o Spring utiliza para filtrar
	private Pageable traduzirPageable(Pageable apiPageable) {
		Map<String, String> mapeamento = new HashMap<>();
		mapeamento.put("codigo", "codigo");
		mapeamento.put("restaurante.nome", "restaurante.nome");
		mapeamento.put("restauranteNome", "restaurante.nome");
		mapeamento.put("nomeCliente", "cliente.nome");
		mapeamento.put("nome.cliente", "cliente.nome");
		mapeamento.put("cliente.nome", "cliente.nome");
		mapeamento.put("clienteEmail", "cliente.email");
		mapeamento.put("cliente.email", "cliente.email");
		mapeamento.put("valorTotal", "valorTotal");
		mapeamento.put("taxaFrete", "taxaFrete");
		mapeamento.put("status", "status");
		
		return PageableTranslate.translate(apiPageable, mapeamento);
	}
}
