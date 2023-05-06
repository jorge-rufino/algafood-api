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
	
//	Quando utilizamos ClientCredentials, a claim nao vem com o id do usuario, então no caso de uma consulta em Pedidos sem passar o filtro contento
//	o id do cliente, acabaria comparando o "null" da claim e o "null" do filtro e acabaria fazendo a consulta que não deveria ser possível.
//	Portanto, os "ids" não podem ser nulos e tem que ser iguais
	public boolean usuarioAutenticadoIgual(Long usuarioId) {
		return getUsuarioId() != null && usuarioId != null && getUsuarioId().equals(usuarioId);
	}
	
	public boolean hasAuthority(String authorityName) {		
		return getAuthentication().getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals(authorityName));
	}
	public boolean podeGerenciarPedidos(String codigoPedido) {
		return hasAuthority("SCOPE_WRITE") && (hasAuthority("GERENCIAR_PEDIDOS") || gerenciaRestauranteDoPedido(codigoPedido));
	}
}
