package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioComSenhaInputDto extends UsuarioInputDto{
	
	@Schema(example = "123", type = "string")
	@NotBlank
	private String senha;
}
