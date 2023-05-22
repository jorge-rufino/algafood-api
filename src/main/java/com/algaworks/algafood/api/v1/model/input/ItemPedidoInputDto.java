package com.algaworks.algafood.api.v1.model.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemPedidoInputDto {
	
	@Schema(example = "1")
	@NotNull
	private Long produtoId;
	
	@Schema(example = "2")
	@NotNull
	@Positive
	private Integer quantidade;
	
	@Schema(example = "Sem salada, por favor")
	private String observacao;
}
