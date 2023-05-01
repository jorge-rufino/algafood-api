package com.algaworks.algafood.core.security;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

//Classe de configuração do HttpBasic
//Para manter a didática do curso, continuarei estendo esta classe mesmo estando depreciada

@Configuration
@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter{
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()							//Autoriza requisições				
				.anyRequest().authenticated()				//Qualquer requisição que esteja autenticada
			.and()
			.cors().and()
//			.oauth2ResourceServer().jwt();			//Transforma o projeto em um ResourceServer com "OpaqueTokens"
			.oauth2ResourceServer().jwt();			//Transforma o projeto em um ResourceServer com tokens JWT
	}

}
