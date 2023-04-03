package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLink;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.model.PedidoDto;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoDtoAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoDto>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLink algaLinks;
	
	public PedidoDtoAssembler() {
		super(PedidoController.class, PedidoDto.class);
	}
	
	@Override
	public PedidoDto toModel(Pedido pedido) {
		PedidoDto pedidoDto = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoDto);
		
		pedidoDto.add(algaLinks.linkToPedidos());
				
		pedidoDto.add(algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));		
		pedidoDto.add(algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
		pedidoDto.add(algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
		
		pedidoDto.getRestaurante().add(algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		
		pedidoDto.getCliente().add(algaLinks.linkToUsuario(pedidoDto.getCliente().getId()));
		
		pedidoDto.getFormaPagamento().add(algaLinks.linkToFormaPagamento(pedidoDto.getFormaPagamento().getId()));
		
		pedidoDto.getEnderecoEntrega().getCidade().add(algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
		
//		Para cada Item, teremos um link para o Produto. 
		pedidoDto.getItens().forEach(item -> {			
			item.add(algaLinks.linkToProduto(pedido.getRestaurante().getId(), item.getProdutoId(),"produto"));
		});		
		
		return pedidoDto;
	}
	
}
