package com.algaworks.algafood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.modelo.Cliente;

@Component
public class AtivacaoClienteService {
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	public void ativar(Cliente cliente) {
		cliente.ativar();
		
		//Cria um evento informando que o cliente foi ativado, se tiver algum gatilho para consumir este evento
		//ele será disparado
		eventPublisher.publishEvent(new ClienteAtivadoEvent(cliente));
	}

}
