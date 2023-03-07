package com.algaworks.algafood.api.model.input;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UsuarioComSenhaInputDto extends UsuarioInputDto{
	
	@NotBlank
	private String senha;
}
