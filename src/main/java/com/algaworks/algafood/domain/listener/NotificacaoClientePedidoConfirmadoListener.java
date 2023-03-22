package com.algaworks.algafood.domain.listener;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.services.EnvioEmailService;
import com.algaworks.algafood.domain.services.EnvioEmailService.Mensagem;

//Classe que fica ouvindo/listen o evento "PedidoCondimadoEvent" ser disparado para entrar em ação
@Component
public class NotificacaoClientePedidoConfirmadoListener {
	
	@Autowired
	private EnvioEmailService envioEmailService;
	
	@EventListener
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		
		Pedido pedido = event.getPedido();
		
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
}
