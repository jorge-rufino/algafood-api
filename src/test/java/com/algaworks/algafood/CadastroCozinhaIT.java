package com.algaworks.algafood;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroCozinhaIT {

	@LocalServerPort
	private int port;
	
	@Test
	public void deveRetornarStatus200_QuandoConsultarCozinhas() {
//		Mostra a requisição feita quando acontecer erro
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
//	Dado que uma requisição em "/cozinhas" na porta "port" com retorno em JSON, quando for um "GET", esperando Status 200 (OK)
		RestAssured.given()
			.basePath("/cozinhas")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(200);
	}
	
	@Test
	public void deveConter4Cozinhas_QuandoConsultarCozinhas() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		RestAssured.given()
			.basePath("/cozinhas")
			.port(port)
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", Matchers.hasSize(4))							//verifica se existem 4 objetos na resposta
			.body("nome", Matchers.hasItems("Indiana", "Tailandesa"));	//Verifica se existem "nomes" com os valores indicados
	}
}
