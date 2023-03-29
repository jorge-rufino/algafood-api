package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.model.CidadeDto;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeDtoAssembler extends RepresentationModelAssemblerSupport<Cidade, CidadeDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CidadeDtoAssembler() {
		super(CidadeController.class, CidadeDto.class);
	}
	
//	Alterei o método "toDto" para "toModel" para poder sobrescreve-lo pois ele faz parte de uma "Interface" da classe extendida.
//	Agora a adiçao dos Links será feita aqui no momento da conversão dos objetos para seus DTOs
	
	@Override
	public CidadeDto toModel(Cidade cidade) {
		CidadeDto cidadeDto = modelMapper.map(cidade, CidadeDto.class);
		
		cidadeDto.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).buscarPorId(cidadeDto.getId()))
				.withSelfRel());

		cidadeDto.add(WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class).listar()).withRel("cidades"));
	
		cidadeDto.getEstado().add(WebMvcLinkBuilder.linkTo(methodOn(EstadoController.class).buscarId(cidadeDto.getEstado().getId()))
					.withSelfRel());
		
		return cidadeDto;
	}
	
//	Utilizaremos este método no lugar do "toCollectionDto"
	
	@Override
	public CollectionModel<CidadeDto> toCollectionModel(Iterable<? extends Cidade> entities) {
		return super.toCollectionModel(entities)
				.add(WebMvcLinkBuilder.linkTo(CidadeController.class).withSelfRel()); 	//Acrescenta o Link da coleção
	}
	
//	public List<CidadeDto> toCollectionDto(List<Cidade> cidades){
//		return cidades.stream()
//				.map(cidade -> toModel(cidade))
//				.toList();
//	}

}
