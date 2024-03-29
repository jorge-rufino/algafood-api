package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.CidadeController;
import com.algaworks.algafood.api.v1.model.CidadeDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeDtoAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeDto> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;

	@Autowired
	private AlgaSecurity algaSecurity;

	public CidadeDtoAssembler() {
		super(CidadeController.class, CidadeDto.class);
	}

//	Alterei o método "toDto" para "toModel" para poder sobrescreve-lo pois ele faz parte de uma "Interface" da classe extendida.
//	Agora a adiçao dos Links será feita aqui no momento da conversão dos objetos para seus DTOs

	@Override
	public CidadeDto toModel(Cidade cidade) {
		CidadeDto cidadeDto = createModelWithId(cidade.getId(), cidade);
		modelMapper.map(cidade, cidadeDto);

		if (algaSecurity.podeConsultarCidades()) {
			cidadeDto.add(algaLinks.linkToCidades("cidades"));
		}

		if (algaSecurity.podeConsultarEstados()) {
			cidadeDto.getEstado().add(algaLinks.linkToEstado(cidade.getEstado().getId()));
		}

		return cidadeDto;
	}

//	Utilizaremos este método no lugar do "toCollectionDto"

	@Override
	public CollectionModel<CidadeDto> toCollectionModel(Iterable<? extends Cidade> entities) {
		CollectionModel<CidadeDto> collectionModel = super.toCollectionModel(entities);

		if (algaSecurity.podeConsultarCidades()) {
			collectionModel.add(algaLinks.linkToCidades());
		}

		return collectionModel;
	}

}
