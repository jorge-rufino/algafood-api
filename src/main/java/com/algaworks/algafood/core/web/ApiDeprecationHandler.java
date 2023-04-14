package com.algaworks.algafood.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

//Classe responsável por informar que a determinada versão está Depreciada
//É chamada no WebConfig
//A mensagem irá aparecer no "Header" de resposta da requisição

@Component
public class ApiDeprecationHandler implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("chegou aqui");
		if(request.getRequestURI().startsWith("/v1/")) {
			response.addHeader("X-AlgaFood-Deprecated", "Essa versão da API está depreciada e deixará de existir a partir de 01/06/2023."
					+ "Use a versão mais atual da API.");
		}
		
		return true;
	}
}
