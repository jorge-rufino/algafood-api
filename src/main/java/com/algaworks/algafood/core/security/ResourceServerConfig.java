package com.algaworks.algafood.core.security;

import java.util.Collections;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

//Classe de configuração do HttpBasic
//Para manter a didática do curso, continuarei estendo esta classe mesmo estando depreciada

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()							//Autoriza requisições				
				.antMatchers(HttpMethod.POST, "/v1/cozinhas/**").hasAuthority("EDITAR_COZINHAS")	//So pode fazer um Post se tiver a permissao
				.antMatchers(HttpMethod.PUT, "/v1/cozinhas/**").hasAuthority("EDITAR_COZINHAS")	//So pode fazer um Put se tiver a permissao
				.antMatchers(HttpMethod.GET, "/v1/cozinhas/**").authenticated()					//Basta está autenticado para fazer um Get
				.anyRequest().denyAll()															//Se não estiver nas regras acima, será negado
			.and()
			.cors().and()
			.oauth2ResourceServer()
				.jwt()
				.jwtAuthenticationConverter(jwtAuthenticationConverter());
	}

	private JwtAuthenticationConverter jwtAuthenticationConverter() {		
		var jwtAuthenticationConverter = new JwtAuthenticationConverter();
		
//		Este metodo recebe como parametro uma lista de SimpleGrantedAuthority
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
			
//			Passamos como parametro o nome da Claim dentro do payload do toke Jwt
//			Cria um lista de String com as permissoes
			var authorities = jwt.getClaimAsStringList("authorities");
			
			if(authorities == null) {
				authorities = Collections.emptyList();
			}
			
//			Convertemos a lista de Permissoes String em uma lista de "GrantedAuthoritys"
			return authorities.stream()
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		});
		
		return jwtAuthenticationConverter;
	}
}
