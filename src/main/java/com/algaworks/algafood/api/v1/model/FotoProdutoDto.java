package com.algaworks.algafood.api.v1.model;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FotoProdutoDto extends RepresentationModel<FotoProdutoDto>{

	@Schema(example = "b8bbd21a-4dd3-4954-835c-3493af2ba6a0_Prime-Rib.jpg")
	private String nomeArquivo;
	
	@Schema(example = "Prime Rib ao ponto")
	private String descricao;
	
	@Schema(example = "image/jpeg")
	private String contentType;
	
	@Schema(example = "201950")
	private Long tamanho;
}
