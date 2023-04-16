package com.algaworks.algafood.core.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//Classe de configuração do HttpBasic
//Para manter a didática do curso, continuarei estendo esta classe mesmo estando depreciada

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication()
		.withUser("jorge")
			.password(passwordEncoder().encode("123"))
			.roles("ADMIN")
		.and()	
		.withUser("joao")
			.password(passwordEncoder().encode("123"))
			.roles("ADMIN");	
	}
	
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
		
//	Criptografa e discriptografa as senhas
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
