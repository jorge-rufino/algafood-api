package com.algaworks.algafood.domain.services;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
				
//		Template que será usado para montar o corpo da mensagem. "src/main/resources/pedido-confirmado.html"
		String template = "pedido-confirmado.html";
				
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm:ss");
		
//		Converte as horas para nosso horario pois no BD está UTC
		String dataConfirmacao = pedido.getDataConfirmacao().format(formatterDate)
						+" as " + pedido.getDataConfirmacao().atZoneSameInstant(ZoneId.of("America/Sao_Paulo")).format(formatterHour);
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado.")
				.corpo(template)
				.variavel("pedido", pedido)
				.variavel("dataConfirmacao", dataConfirmacao)
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
