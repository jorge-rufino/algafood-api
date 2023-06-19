package com.algaworks.algafood.core.security.authorizationserver;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

public class JdbcOauth2AuthorizationQueryService implements Oauth2AuthorizationQueryService{

	private final JdbcOperations jdbcOperations;
	private final RowMapper<RegisteredClient> rowMapperRegisteredClient;
	private final RowMapper<OAuth2Authorization> rowMapperAuthorization;

	private final String LIST_AUTHORIZED_CLIENTS_QUERY = "select RC.* from oauth2_authorization_consent C inner join oauth2_registered_client RC"
			+ " on RC.id = C.registered_client_id where C.principal_name = ? ";
	
	private final String LIST_AUTHORIZATIONS_QUERY = "select A.* from oauth2_authorization A "
			+ "inner join oauth2_registered_client C "
			+ "on C.id = A.registered_client_id "
			+ "where A.principal_name = ? "
			+ "and A.registered_client_id = ? ";
	
	public JdbcOauth2AuthorizationQueryService(JdbcOperations jdbcOperations, RegisteredClientRepository repository) {
		this.jdbcOperations = jdbcOperations;
		this.rowMapperRegisteredClient = new JdbcRegisteredClientRepository.RegisteredClientRowMapper();
		this.rowMapperAuthorization = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(repository);
	}

	@Override
	public List<RegisteredClient> listClientsWithConsent(String principalName) {

		return this.jdbcOperations.query(LIST_AUTHORIZED_CLIENTS_QUERY, rowMapperRegisteredClient, principalName);
	}

	@Override
	public List<OAuth2Authorization> listAuthorization(String principalName, String clientId) {

		return this.jdbcOperations.query(LIST_AUTHORIZATIONS_QUERY, rowMapperAuthorization, principalName, clientId);
	}

}
