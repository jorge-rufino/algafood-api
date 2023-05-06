package com.algaworks.algafood.domain.listener;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.services.EnvioEmailService;
import com.algaworks.algafood.domain.services.EnvioEmailService.Mensagem;

//Classe que fica ouvindo/listen o evento "PedidoCondimadoEvent" ser disparado para entrar em ação
@Component
public class NotificacaoClientePedidoCanceladoListener {
	
	@Autowired
	private EnvioEmailService envioEmailService;
	
	@TransactionalEventListener
	public void aoConfirmarPedido(PedidoCanceladoEvent event) {
		
		Pedido pedido = event.getPedido();
		
//		Template que será usado para montar o corpo da mensagem. "src/main/resources/pedido-confirmado.html"
		String template = "emails/pedido-cancelado.html";
				
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm:ss");
		
//		Converte as horas para nosso horario pois no BD está UTC
		String dataCancelamento= pedido.getDataCancelamento().format(formatterDate)
						+" as " + pedido.getDataCancelamento().atZoneSameInstant(ZoneId.of("America/Sao_Paulo")).format(formatterHour);
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido Cancelado.")
				.corpo(template)
				.variavel("pedido", pedido)
				.variavel("dataCancelamento", dataCancelamento)
				.destinatario(pedido.getCliente().getEmail())
				.build();
		
		envioEmailService.enviar(mensagem);
	}
}
