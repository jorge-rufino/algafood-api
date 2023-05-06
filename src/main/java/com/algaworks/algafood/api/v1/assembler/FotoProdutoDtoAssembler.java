package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.FotoProdutoDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.FotoProduto;

@Component
public class FotoProdutoDtoAssembler {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;

	@Autowired
	private AlgaSecurity algaSecurity;

	public FotoProdutoDto toDto(FotoProduto foto) {
		FotoProdutoDto fotoProdutoDto = modelMapper.map(foto, FotoProdutoDto.class);

		// Quem pode consultar restaurantes, também pode consultar os produtos e fotos
		if (algaSecurity.podeConsultarRestaurantes()) {
			fotoProdutoDto.add(algaLinks.linkToFotoProduto(foto.getProduto().getRestaurante().getId(), foto.getProduto().getId()));
			fotoProdutoDto.add(algaLinks.linkToProduto(foto.getProduto().getRestaurante().getId(),foto.getProduto().getId(), "produto"));
		}

		return fotoProdutoDto;
	}

}
