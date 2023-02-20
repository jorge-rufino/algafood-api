package com.algaworks.algafood;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.services.CozinhaService;

@SpringBootTest
class CadastroCozinhaIntegrationTests {

	@Autowired
	private CozinhaService cozinhaService;
	
	@Test
	public void testaCadastroCozinhaComSucesso() {
		//Cenário
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome("Chinesa");		
		
		//Ação
		novaCozinha = cozinhaService.salvar(novaCozinha);
		
		//Validação
		assertThat(novaCozinha).isNotNull();
		assertThat(novaCozinha.getId()).isNotNull();
	}
	
	@Test
	public void testarCadastroCozinhaSemNome() {
		
		Cozinha novaCozinha = new Cozinha();
		novaCozinha.setNome(null);		

		ConstraintViolationException erroEsperado =
			      Assertions.assertThrows(ConstraintViolationException.class, () -> {
			    	  cozinhaService.salvar(novaCozinha);
			      });
			   
			   assertThat(erroEsperado).isNotNull();
	}
	
	@Test
	public void deveFalhar_QuandoExcluirCozinhaEmUso() {
		
		EntidadeEmUsoException erroEsperado =
			      Assertions.assertThrows(EntidadeEmUsoException.class, () -> {
			    	  cozinhaService.deletar(1L);
			      });
			   
			   assertThat(erroEsperado).isNotNull();
	}
	
	@Test
	public void deveFalhar_QuandoExcluirCozinhaInexistente() {
		
		CozinhaNaoEncontradaException erroEsperado =
			      Assertions.assertThrows(CozinhaNaoEncontradaException.class, () -> {
			    	  cozinhaService.deletar(10L);
			      });
			   
			   assertThat(erroEsperado).isNotNull();
	}
}
