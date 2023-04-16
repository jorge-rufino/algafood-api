package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

//Classe de configuração do HttpBasic

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic()									//Permite HttpBasic			.
			.and()	
//			.formLogin()							  		  Não queremos que apareça o Formulario para Login							
			.authorizeRequests()							//Autoriza requisições
				.antMatchers("/v1/cozinhas/**").permitAll() //Permite qualque chamada que comece com "/v1/cozinhas/"
				.anyRequest().authenticated()				//Qualquer requisição que esteja autenticada
			.and()
				.sessionManagement()											//Gerenciamento de Sessão
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)	//Proibimos o uso de "session", assim não é gerado mais "cookies"
			.and()
				.csrf().disable();							//desabilita o "csrf"
	}
}
