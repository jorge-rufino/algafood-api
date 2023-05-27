package com.algaworks.algafood.api.v1.model.input;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProdutoInputDto {

	@Schema(example = "Espetinho de cupim")
	@NotBlank
	private String nome;
	
	@Schema(example = "Acompanha arroz, feijão e farofa")
	@NotBlank
	private String descricao;
	
	@Schema(example = "15.00")
	@NotNull
	@PositiveOrZero
	private BigDecimal preco;
	
	@Schema(example = "true")
	@NotNull
	private Boolean ativo;	
}
