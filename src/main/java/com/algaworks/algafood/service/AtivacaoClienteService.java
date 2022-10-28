package com.algaworks.algafood.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.modelo.Cliente;
import com.algaworks.algafood.notificacao.NivelUrgencia;
import com.algaworks.algafood.notificacao.Notificador;
import com.algaworks.algafood.notificacao.TipoDoNotificador;

@Component
public class AtivacaoClienteService {

	@TipoDoNotificador(NivelUrgencia.NORMAL)
	@Autowired
	private Notificador notificador;
	
	//Como existe somente um Bean/Classe que implementa a interface "Notificador", o spring entende que deve utilizar ele
	//Porém se tivessemos outra classe (NotificadorSMS por exemplo), teriamos que escolher qual notificador seria passado como
	//parametro no construtor, seria um "NotificadorEmail" ou se seria um "NotificadorSMS".
	
//	@Autowired
//	public AtivacaoClienteService(Notificador notificador) {
//		this.notificador = notificador;
//	}
	
//	public AtivacaoClienteService(String test) {
//		
//	}
	
//	Esta annotation faz com que o metodo seja executado na inicializaçao do Bean, logo depois do Construtor
	@PostConstruct
	public void init() {
		System.out.println("INIT");
	}
	
//	Esta annotation faz com que o metodo seja executado antes da destruição do Bean
	@PreDestroy
	public void destroy() {
		System.out.println("DESTROY");
	}
	
	public void ativar(Cliente cliente) {
		cliente.ativar();
				
		
		this.notificador.notificar(cliente, "Seu cadastro foi ativado!");
	}

//	@Autowired
//	public void setNotificador(Notificador notificador) {
//		this.notificador = notificador;
//	}
	
}
