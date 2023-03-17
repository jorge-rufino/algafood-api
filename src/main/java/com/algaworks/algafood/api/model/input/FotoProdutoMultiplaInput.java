package com.algaworks.algafood.api.model.input;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FotoProdutoMultiplaInput {

	@NotNull
	@Valid
	private List<FotoProdutoInput> fotos;
}
