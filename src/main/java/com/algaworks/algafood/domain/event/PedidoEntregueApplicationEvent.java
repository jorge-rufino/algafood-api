package com.algaworks.algafood.domain.event;

import org.springframework.context.ApplicationEvent;

import com.algaworks.algafood.domain.model.Pedido;

import lombok.Getter;

@Getter
public class PedidoEntregueApplicationEvent extends ApplicationEvent{
	private static final long serialVersionUID = 1L;

	private Pedido pedido;
	
	public PedidoEntregueApplicationEvent(Object source, Pedido pedido) {
		super(source);
		this.pedido = pedido;
	}

}
