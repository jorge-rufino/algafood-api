package com.algaworks.algafood.core.security.authorizationserver;

import java.security.Principal;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthorizedClientsController {
	
	private final Oauth2AuthorizationQueryService oauth2AuthorizationQueryService; 

	private final RegisteredClientRepository clientRepository;
	private final OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService;
	private final OAuth2AuthorizationService auth2AuthorizationService;
	
	@GetMapping("oauth2/authorized-clients")
	public String clientsList(Principal principal, Model model) {
		
		List<RegisteredClient> clients = oauth2AuthorizationQueryService.listClientsWithConsent(principal.getName());
		model.addAttribute("clients",clients);
		return "pages/authorized-clients";
	}
	
	@PostMapping("oauth2/authorized-clients/revoke")
	public String revoke(Principal principal, Model model, @RequestParam(OAuth2ParameterNames.CLIENT_ID) String clientId) {
		
		RegisteredClient registeredClient = clientRepository.findByClientId(clientId);
		
		if(registeredClient == null) {
			throw new AccessDeniedException(String.format("Cliente %s não encontrado.", clientId));			
		}
		
		OAuth2AuthorizationConsent consent = oAuth2AuthorizationConsentService.findById(registeredClient.getId(), principal.getName());

		List<OAuth2Authorization> authorizations = oauth2AuthorizationQueryService.listAuthorization(principal.getName(), registeredClient.getId());
		
		if (consent != null) {
			oAuth2AuthorizationConsentService.remove(consent);
		}
		
		for (OAuth2Authorization authorization : authorizations) {
			auth2AuthorizationService.remove(authorization);			
		}
		
		return "redirect:/oauth2/authorized-clients";
	}
}
