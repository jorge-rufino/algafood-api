package com.algaworks.algafood.domain.model;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
	
	CRIADO("Criado"),
	CONFIRMADO("Confirmado", CRIADO), //Para poder ir para o status CONFIRMADO, precisa ter como anterior o CRIADO
	ENTREGUE("Entregue", CONFIRMADO), //Para poder ir para o status ENTREGUE, precisa ter como anterior o CONFIRMADO
	CANCELADO("Cancelado", CRIADO,CONFIRMADO);	  //Para poder ir para o status CANCELADO, precisa ter como anterior o CRIADO ou CONFIRMADO	
	
	private String descricao;
	private List<StatusPedido> statusAnteriores;
	
	StatusPedido(String descricao, StatusPedido... statusAnteriores){
		this.descricao = descricao;
		this.statusAnteriores = Arrays.asList(statusAnteriores);
	}
	
	public String getDescricao() {
		return this.descricao;
	}
	
//	O "this" é o status atual
//	Por exemplo, o status atual é "Criado", e tentamos mudar para "Entregue", entao verificamos se os "statusAnteriores"
//	de "Entregue" contem o status atual ("this") que é "Criado", no caso retornará "false" porém nos negamos para retornar "true" 
//	e na classe "Pedido" disparar a Exception
	
	public boolean naoPodeAlterarPara(StatusPedido novoStatus) {
		return !novoStatus.statusAnteriores.contains(this);
	}
	
	public boolean podeAlterarPara(StatusPedido novoStatus) {
		return !naoPodeAlterarPara(novoStatus);
	}
}
