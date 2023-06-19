package com.algaworks.algafood.core.security.authorizationserver;

import java.util.List;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

public class JdbcOauth2AuthorizationQueryService implements Oauth2AuthorizationQueryService{

	private final JdbcOperations jdbcOperations;
	private final RowMapper<RegisteredClient> rowMapper;

	private final String LIST_AUTHORIZED_CLIENTS = "select RC.* from oauth2_authorization_consent C inner join oauth2_registered_client RC"
			+ " on RC.id = C.registered_client_id where C.principal_name = ? ";
	
	public JdbcOauth2AuthorizationQueryService(JdbcOperations jdbcOperations) {
		this.jdbcOperations = jdbcOperations;
		this.rowMapper = new JdbcRegisteredClientRepository.RegisteredClientRowMapper();
	}



	@Override
	public List<RegisteredClient> listClientsWithConsent(String principalName) {

		return this.jdbcOperations.query(LIST_AUTHORIZED_CLIENTS, rowMapper, principalName);
	}

}
