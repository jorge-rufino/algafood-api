package com.algaworks.algafood.core.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

//Classe responsável por desligar determinada versão que não irá mais funcionar
//É chamada no WebConfig

//@Component
public class ApiRetirementHandler implements HandlerInterceptor{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		if(request.getRequestURI().startsWith("/v1/")) {
			response.setStatus(HttpStatus.GONE.value());	//Retorna codigo "410"
			
			return false;	//Precisamos retornar "false" para parar a execução do método que foi chamado pela requisição
		}
		
		return true;
	}
}
