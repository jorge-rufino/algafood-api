package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.EstadoController;
import com.algaworks.algafood.api.v1.model.EstadoDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoDtoAssembler extends RepresentationModelAssemblerSupport<Estado, EstadoDto>{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	@Autowired
	private AlgaSecurity algaSecurity; 
	
	public EstadoDtoAssembler() {
		super(EstadoController.class, EstadoDto.class);
	}
	
	@Override
	public EstadoDto toModel(Estado estado) {
		EstadoDto estadoDto = createModelWithId(estado.getId(), estado);
		modelMapper.map(estado, estadoDto);
		
		if (algaSecurity.podeConsultarEstados()) {
			estadoDto.add(algaLinks.linkToEstados("estados"));
		}
		
		return estadoDto;
	}
	
	@Override
	public CollectionModel<EstadoDto> toCollectionModel(Iterable<? extends Estado> entities) {
		CollectionModel<EstadoDto> collectionModel = super.toCollectionModel(entities);
	    
	    if (algaSecurity.podeConsultarEstados()) {
	        collectionModel.add(algaLinks.linkToEstados());
	    }
	    
	    return collectionModel;
	}
}
