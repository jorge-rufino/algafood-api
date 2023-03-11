package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.PedidoDtoAssembler;
import com.algaworks.algafood.api.model.PedidoDto;
import com.algaworks.algafood.domain.services.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	@Autowired
	private PedidoService pedidoService;

	@Autowired
	private PedidoDtoAssembler pedidoDtoAssembler;
	
	@GetMapping
	public List<PedidoDto> listar(){
		return pedidoDtoAssembler.toCollectDto(pedidoService.listar());
	}
	
	@GetMapping("{pedidoId}")
	public PedidoDto buscarPorId(@PathVariable Long pedidoId) {
		return pedidoDtoAssembler.toDto(pedidoService.buscarPorId(pedidoId));
	}
}
