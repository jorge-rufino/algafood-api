package com.algaworks.algafood.core.springdoc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.SpringDocUtils;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.exceptionhandler.Problem;
import com.algaworks.algafood.api.v1.openapi.controller.LinksModel;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
@SecurityScheme(name = "security_oauth", 
	type = SecuritySchemeType.OAUTH2, 
	flows = @OAuthFlows(authorizationCode = @OAuthFlow(
			authorizationUrl = "${springdoc.oAuthFlow.authorizationUrl}",
			tokenUrl = "${springdoc.oAuthFlow.tokenUrl}",
			scopes = {
					@OAuthScope(name = "READ", description = "read scope"),
					@OAuthScope(name = "WRITE", description = "write scope")
			}
	)))
public class SpringDocConfig {
	
	private static final String INTERNAL_SERVER_ERROR_RESPONSE = "InternalServerErrorResponse";
	private static final String NOT_ACCEPTABLE_RESPONSE = "NotAcceptableResponse";
	private static final String NOT_FOUND_RESPONSE = "NotFoundResponse";
	private static final String BAD_REQUEST_RESPONSE = "BadRequestResponse";

	@Bean
	public OpenAPI openAPI() {
		
		SpringDocUtils.getConfig().replaceWithClass(
		           org.springframework.hateoas.Links.class, LinksModel.class);
		
		return new OpenAPI()
				.info(new Info().title("AlgaFood API")
						.version("v1")
						.description("REST API do sistema AlgaFood")
						.license(new License()
								.name("SpringDoc")
								.url("https://springdoc.org")
								)
					 ) 
				.externalDocs(new ExternalDocumentation()
						.description("AlgaWorks")
						.url("https://algaworks.com"))
				.tags(Arrays.asList(
						new Tag().name("Cidades").description("Gerencia as cidades"),
						new Tag().name("Estados").description("Gerencia os estados"),						
						new Tag().name("Cozinhas").description("Gerencia as cozinhas"),						
						new Tag().name("Formas de pagamento").description("Gerencia as formas de pagamento"),
						new Tag().name("Pedidos").description("Gerencia os pedidos"),
						new Tag().name("Restaurantes").description("Gerencia os restaurantes"),					
						new Tag().name("Produtos").description("Gerencia os produtos"),
						new Tag().name("Grupos").description("Gerencia os grupos"),
						new Tag().name("Usuários").description("Gerencia os usuários"),
						new Tag().name("Permissões").description("Gerencia as permissões"),
						new Tag().name("Estatísticas").description("Estatísticas da AlgaFood")
					  ))
				.components(new Components()
						.schemas(gerarSchemas())
						.responses(gerarResponses())
						);
	}

	//Representaçoes dos problemas de forma global
	//Os "responses" estão comentandos para que os "responses" definidos nas interfaces apareçam, pois os "responses" globais tem prioridade 
	@Bean
	public OpenApiCustomiser openApiCustomiser(){
		return openApi -> {
			openApi.getPaths()
					.values()
					.forEach(pathItem -> pathItem.readOperationsMap()
						.forEach((httpMethod, operation) -> {
							
							ApiResponses responses = operation.getResponses();
							
							switch (httpMethod) {
							case GET:
//								responses.addApiResponse("404", new ApiResponse().$ref(NOT_FOUND_RESPONSE));
								responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
								responses.addApiResponse("406", new ApiResponse().$ref(NOT_ACCEPTABLE_RESPONSE));
								break;
							case POST:
								responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
								responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
								break;
							case PUT:
//								responses.addApiResponse("404", new ApiResponse().$ref(NOT_FOUND_RESPONSE));
//								responses.addApiResponse("400", new ApiResponse().$ref(BAD_REQUEST_RESPONSE));
								responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
								break;
							case DELETE:
//								responses.addApiResponse("404", new ApiResponse().$ref(NOT_FOUND_RESPONSE));
								responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
								break;
							default:
								responses.addApiResponse("500", new ApiResponse().$ref(INTERNAL_SERVER_ERROR_RESPONSE));
								break;
							}
						})
							
					);
		};		
	}
	
	private Map<String, Schema> gerarSchemas(){
		final Map<String, Schema> schemaMap = new HashMap<>();
		
		Map<String, Schema> problemSchema = ModelConverters.getInstance().read(Problem.class);
		Map<String, Schema> problemFieldSchema = ModelConverters.getInstance().read(Problem.Field.class);
		
		schemaMap.putAll(problemSchema);
		schemaMap.putAll(problemFieldSchema);
		
		return schemaMap;
	}
	
	private Map<String, ApiResponse> gerarResponses() {

		Map<String, ApiResponse> apiResponseMap = new HashMap<>();
		
		Content content = new Content()
				.addMediaType(APPLICATION_JSON_VALUE, new MediaType().schema(new Schema<Problem>().$ref("Problema")));
		
		apiResponseMap.put(BAD_REQUEST_RESPONSE, new ApiResponse()
				.description("Requisição inválida.")
				.content(content));
		
		apiResponseMap.put(NOT_FOUND_RESPONSE, new ApiResponse()
				.description("Recurso não encontrado")
				.content(content));
		
		apiResponseMap.put(NOT_ACCEPTABLE_RESPONSE, new ApiResponse()
				.description("Recurso não possui representação que poderia ser aceita pela consumidor")
				.content(content));
		
		apiResponseMap.put(INTERNAL_SERVER_ERROR_RESPONSE, new ApiResponse()
				.description("Erro interno no servidor")
				.content(content));
		
		return apiResponseMap;
	}
}
