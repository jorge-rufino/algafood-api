package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.GrupoController;
import com.algaworks.algafood.api.v1.model.GrupoDto;
import com.algaworks.algafood.domain.model.Grupo;

@Component
public class GrupoDtoAssembler extends RepresentationModelAssemblerSupport<Grupo, GrupoDto> {

	@Autowired
	private ModelMapper modelMapper;
	
	public GrupoDtoAssembler() {
		super(GrupoController.class, GrupoDto.class);
	}
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Override
	public GrupoDto toModel(Grupo grupo) {
		GrupoDto grupoDto = createModelWithId(grupo.getId(), grupo);
		modelMapper.map(grupo, grupoDto);
		
		grupoDto.add(algaLinks.linkToGrupos("grupos"));
		grupoDto.add(algaLinks.linkToGrupoPermissoes(grupoDto.getId(),"permissoes"));
		
		return grupoDto;
	}
	
	@Override
    public CollectionModel<GrupoDto> toCollectionModel(Iterable<? extends Grupo> entities) {
        return super.toCollectionModel(entities).add(algaLinks.linkToGrupos());
    }      
}
