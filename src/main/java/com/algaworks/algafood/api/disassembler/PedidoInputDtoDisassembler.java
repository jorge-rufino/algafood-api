package com.algaworks.algafood.api.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.ItemPedidoInputDto;
import com.algaworks.algafood.api.model.input.PedidoInputDto;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoInputDtoDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
	public Pedido toDomainObject(PedidoInputDto pedidoInput) {
		return modelMapper.map(pedidoInput, Pedido.class);
	}
	
	public void copyToDomainObject(ItemPedidoInputDto pedidoInput, Pedido pedido) {
		modelMapper.map(pedidoInput, pedido);
	}
}
