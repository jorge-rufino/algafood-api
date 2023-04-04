package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLink;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.model.PedidoResumoDto;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoDtoAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoResumoDto>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLink algaLinks;
	
	public PedidoResumoDtoAssembler() {
		super(PedidoController.class, PedidoResumoDto.class);
	}

	@Override
	public PedidoResumoDto toModel(Pedido pedido) {
		PedidoResumoDto pedidoDto = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoDto);
		
		pedidoDto.add(algaLinks.linkToPedidos("pedidos"));
		
		pedidoDto.getRestaurante().add(algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		
		pedidoDto.getCliente().add(algaLinks.linkToUsuario(pedido.getCliente().getId()));
		
		return pedidoDto;
	}
}
