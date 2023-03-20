package com.algaworks.algafood.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.services.EnvioEmailService.Mensagem;

@Service
public class FluxoPedidoService {
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private EnvioEmailService envioEmailService;
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarPorCodigo(codigoPedido);		
		pedido.confirmar();
				
//		Repare que estamos passando tags html para formatar a mensagem
		String corpoMensagem = "O pedido de código <strong>" + pedido.getCodigo() + "</strong>  em nome de <strong>" 
				+ pedido.getCliente().getNome() + " </strong> foi confirmado.";
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado.")
				.corpo(corpoMensagem)
				.destinatario(pedido.getCliente().getEmail())
				.build();
		
		envioEmailService.enviar(mensagem);
	}
	
	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarPorCodigo(codigoPedido);
		
		pedido.entregar();
	}
	
	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarPorCodigo(codigoPedido);
		
		pedido.cancelar();
	}
}
