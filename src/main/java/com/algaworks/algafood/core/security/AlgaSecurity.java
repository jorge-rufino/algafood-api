package com.algaworks.algafood.core.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.domain.services.PedidoService;
import com.algaworks.algafood.domain.services.RestauranteService;

@Component
public class AlgaSecurity {

	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private PedidoService pedidoService;
	
//	Contexto de Autenticacao
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	public Long getUsuarioId() {
		
//		Captura o token Jwt
		Jwt jwt = (Jwt) getAuthentication().getPrincipal();
		
		return jwt.getClaim("usuario-id");
	}
	
//	Verifica se o usuario logado é o dono/responsavel pelo restaurante
	public boolean gerenciaRestaurante(Long restauranteId) {
		if (restauranteId == null) {
	        return false;
	    }
		
		return restauranteService.existsResponsavel(restauranteId, getUsuarioId());
	}
	
	public boolean gerenciaRestauranteDoPedido(String codigoPedido) {
	    return pedidoService.isPedidoGerenciadoPor(codigoPedido, getUsuarioId());
	} 
}