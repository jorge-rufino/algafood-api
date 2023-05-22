package com.algaworks.algafood.api.v1.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnderecoDto {
		
	@Schema(example = "68795-000")
	private String cep;		
	
	@Schema(example = "Av. João Fanjas")
	private String logradouro;		
	
	@Schema(example = "205")
	private String numero;
	
	@Schema(example = "Fundos")
	private String complemento;
	
	@Schema(example = "Centro")
	private String bairro;
	
	private CidadeResumoDto cidade;	
}
