package com.algaworks.algafood.infrastructure.service.email;

import org.springframework.beans.factory.annotation.Autowired;

import com.algaworks.algafood.domain.services.EnvioEmailService;

import lombok.extern.slf4j.Slf4j;

//Esta classe mostra o email fake no console da IDE
@Slf4j
public class FakeEnvioEmailService implements EnvioEmailService{
	
	@Autowired
	private ProcessadorEmailTemplate processadorEmailTemplate;
	
	@Override
    public void enviar(Mensagem mensagem) {

        String corpo = processadorEmailTemplate.processarTemplate(mensagem);

        log.info("\n[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
    }
}
