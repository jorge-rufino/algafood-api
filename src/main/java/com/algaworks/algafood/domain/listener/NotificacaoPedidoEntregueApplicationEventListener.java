package com.algaworks.algafood.domain.listener;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.event.PedidoEntregueApplicationEvent;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.services.EnvioEmailService;
import com.algaworks.algafood.domain.services.EnvioEmailService.Mensagem;

@Component
public class NotificacaoPedidoEntregueApplicationEventListener {
	
	@Autowired
	private EnvioEmailService envioEmailService;
	
	@TransactionalEventListener
	private void aoCancelarPedido(PedidoEntregueApplicationEvent event) {
		Pedido pedido = event.getPedido();
		
		String template = "emails/pedido-entregue.html";
				
		DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		String dataEntrega= pedido.getDataEntrega().format(formatterDate)
						+" as " + pedido.getDataEntrega().atZoneSameInstant(ZoneId.of("America/Sao_Paulo")).format(formatterHour);
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido Entregue.")
				.corpo(template)
				.variavel("pedido", pedido)
				.variavel("dataEntrega", dataEntrega)
				.destinatario(pedido.getCliente().getEmail())
				.build();
		
		envioEmailService.enviar(mensagem);
	}
}
