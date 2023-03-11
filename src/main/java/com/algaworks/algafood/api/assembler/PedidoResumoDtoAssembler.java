package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.PedidoResumoDto;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoResumoDtoAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public PedidoResumoDto toDto(Pedido pedido) {
		return modelMapper.map(pedido, PedidoResumoDto.class);
	}
	
	public List<PedidoResumoDto> toCollectDto(Collection<Pedido> pedidos){
		return pedidos.stream()
				.map(pedido -> toDto(pedido))
				.toList();
	}
}
