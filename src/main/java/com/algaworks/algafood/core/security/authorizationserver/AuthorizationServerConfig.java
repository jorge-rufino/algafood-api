package com.algaworks.algafood.core.security.authorizationserver;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@Configuration
public class AuthorizationServerConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)	//Para executar antes do resource server
	public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		
		return http
				.formLogin(Customizer.withDefaults())	//Habilita o login
				.build();
	}
	
	@Bean
	public ProviderSettings providerSettings(AlgaFoodSecurityProperties properties) {
		return ProviderSettings.builder()
				.issuer(properties.getProviderUrl())
				.build();
	}
	
	@Bean
	public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
		
		RegisteredClient algafoodBackend = RegisteredClient
				.withId("1")
				.clientId("algafood-backend")
				.clientSecret(passwordEncoder.encode("backend123"))	//Senha
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
				.scope("READ")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)	//Toke JWT
						.accessTokenTimeToLive(Duration.ofMinutes(30))	//30 minutos de validade
						.build())
				.build();
		
		RegisteredClient algafoodWeb = RegisteredClient
				.withId("2")
				.clientId("algafood-web")
				.clientSecret(passwordEncoder.encode("web123"))	//Senha
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.scope("READ")
				.scope("WRITE")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)	//Token JWT
						.accessTokenTimeToLive(Duration.ofMinutes(15))	//30 minutos de validade
						.build())
				.redirectUri("http://127.0.0.1:8080/authorized")						//Esta url nao existe
				.redirectUri("http://127.0.0.1:8080/swagger-ui/oauth2-redirect.html")	//Esta url existe
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(true) 		//Obrigatorio a tela para conceder permissoes
						.build())
				.build();
		
		RegisteredClient foodanalytics = RegisteredClient
				.withId("3")
				.clientId("foodanalytics")
				.clientSecret(passwordEncoder.encode("web123"))	//Senha
				.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
				.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
				.scope("READ")
				.scope("WRITE")
				.tokenSettings(TokenSettings.builder()
						.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)	//Token JWT
						.accessTokenTimeToLive(Duration.ofMinutes(30))	//30 minutos de validade
						.build())
				.redirectUri("http://www.foodanalytics.local:8082")
				.clientSettings(ClientSettings.builder()
						.requireAuthorizationConsent(false) 		//Obrigatorio a tela para conceder permissoes
						.build())
				.build();
		
		return new InMemoryRegisteredClientRepository(Arrays.asList(algafoodBackend, algafoodWeb, foodanalytics));
	}
	
	@Bean
	public OAuth2AuthorizationService oAuth2AuthorizationService (JdbcOperations jdbcOperations, RegisteredClientRepository registeredClientRepository) {
		
		return new JdbcOAuth2AuthorizationService(jdbcOperations, registeredClientRepository);
	}
	
	@Bean
	public JWKSource<SecurityContext> jwkSource(JwtKeyStoreProperties properties) throws Exception{
		
		char[] keyStorePass = properties.getPassword().toCharArray();
		String keyParAlias = properties.getKeypairAlias();
		
		Resource jksLocation = properties.getJksLocation();
		InputStream inputStream = jksLocation.getInputStream();
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(inputStream, keyStorePass);
		
		RSAKey rsaKey = RSAKey.load(keyStore, keyParAlias, keyStorePass);
		
		return new ImmutableJWKSet<>(new JWKSet(rsaKey));		
	}
}
