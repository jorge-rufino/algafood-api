package com.algaworks.algafood.api;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.experimental.UtilityClass;

//Transforma a classe em "final" e não pode ser extendida. NÃO É UMA ANOTTATION OBRIGATORIA
@UtilityClass
public class ResourceUriHelper {

	public static void addUriInResponseHeader(Object resourceId) {
//		Pega a URI da requisição("api.algafood.local:8080/cidades") e acrescenta o "/{id} da cidade que acabou de ser criada"
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
			.path("/{id}")
			.buildAndExpand(resourceId)
			.toUri();
				
//		Captura a resposta		
		HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getResponse();

		response.setHeader("Location", uri.toString());
	}
}
