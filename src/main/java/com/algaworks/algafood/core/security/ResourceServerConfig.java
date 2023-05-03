package com.algaworks.algafood.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

//Classe de configuração do HttpBasic
//Para manter a didática do curso, continuarei estendo esta classe mesmo estando depreciada

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)	//Permite com que as regras das permissoes estejam direto nos controladores
public class ResourceServerConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http		
			.csrf().disable()			//Para evitar erros com os metodos Post e Put
			.cors().and()
			.oauth2ResourceServer()
				.jwt()
				.jwtAuthenticationConverter(jwtAuthenticationConverter());
	}

//	Quando configuramos para o jwt usar este metodo, ele perde a configuração que carregava os SCOPES, portanto precisamos
//	reconfigurar
	private JwtAuthenticationConverter jwtAuthenticationConverter() {		
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		
//		Este metodo recebe como parametro uma lista de SimpleGrantedAuthority
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			
//			Passamos como parametro o nome da Claim dentro do payload do toke Jwt
//			Cria um lista de String com as permissoes
			var authorities = jwt.getClaimAsStringList("authorities");
			
			var scopes = jwt.getClaimAsStringList("scope");
			
			if(authorities == null) {
				authorities = Collections.emptyList();
			}
			
//			Convertemos a lista de Scopes em String em uma lista de "GrantedAuthoritys"
			Collection<GrantedAuthority> grantedAuthorities = scopes.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());			

//			Transformamos a lista de Permissoes string em GrantedAutoritys e a adicionamos a coleção 
			grantedAuthorities.addAll(authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));

			return grantedAuthorities;
		});
		
		return jwtAuthenticationConverter;
	}
}
