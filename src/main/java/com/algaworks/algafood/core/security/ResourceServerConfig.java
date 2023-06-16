package com.algaworks.algafood.core.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class ResourceServerConfig {

	@Bean
	public SecurityFilterChain resourceServerFilterChain(HttpSecurity httpSecurity) throws Exception{
		
		httpSecurity
			.authorizeRequests()
			.antMatchers("/oauth2/**")
				.authenticated()
			.and()
			.csrf()
				.disable()
			.cors()
			.and()
			.oauth2ResourceServer()
				.jwt()
				.jwtAuthenticationConverter(jwtAuthenticationConverter());
		
		return httpSecurity
				.formLogin(customizer -> customizer.loginPage("/login"))	//Habilita o login
				.build();
	}
	
//	Converte as permissoes e scopes que vem no token JWT
	private JwtAuthenticationConverter jwtAuthenticationConverter () {
		
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		
		converter.setJwtGrantedAuthoritiesConverter(jwt -> {
			
//			Lista das permissoes			
			List<String> authorities = jwt.getClaimAsStringList("authorities");
			
			if(authorities == null) {
				return Collections.emptyList();
			}
			
			JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
			
//			Lista dos Scopes
			Collection<GrantedAuthority> grantedAuthorities = authoritiesConverter.convert(jwt);
			
//			Junta as 2 listas
			grantedAuthorities.addAll(authorities
					.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList()));
				
			return grantedAuthorities;	
		});
		
		return converter;
	}
}
