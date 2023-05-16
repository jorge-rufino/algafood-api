package com.algaworks.algafood.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

//Esta annotation inclue na representação somente as varaiveis que não forem "null"
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
@Schema(name = "Problema")
public class Problem {
	
	@Schema(example = "400")
	private Integer status;
	
	@Schema(example = "2023-05-16T00:00:00.1234567Z")
	private OffsetDateTime timestamp;
	
	@Schema(example = "https://algafood.com.br/dados-invalidos")
	private String type;
	
	@Schema(example = "Dados inválidos")
	private String title;
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String detail;	
	
	@Schema(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.")
	private String userMessage;
	
	@Schema(description = "Lista de objetos ou campos que geraram o erro")
	private List<Field> fields;	
	
	@Getter
	@Builder
	@Schema(name = "ObjetoProblema")
	public static class Field {
	
		@Schema(example = "preço")
		private String name;
		
		@Schema(example = "O preço é inválido")
		private String userMessage;
	}
}
