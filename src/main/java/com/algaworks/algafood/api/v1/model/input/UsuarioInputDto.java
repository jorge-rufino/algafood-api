package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioInputDto {
	
	@Schema(example = "João da Silva")
	@NotBlank	
	private String nome;
	
	@Schema(example = "joao.ger@algafood.com.br")
	@NotBlank
	@Email
	private String email;
}
