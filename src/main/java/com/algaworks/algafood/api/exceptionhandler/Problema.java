package com.algaworks.algafood.api.exceptionhandler;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

//Annotation "Builder" muda a forma como construir e declarar os valores dos objetos
@Getter
@Builder
public class Problema {
	
	private LocalDateTime dataHora;
	private String mensagem;
}
