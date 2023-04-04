package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.AlgaLinks;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.model.FormaPagamentoDto;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoDtoAssembler extends RepresentationModelAssemblerSupport<FormaPagamento, FormaPagamentoDto>{
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
    private AlgaLinks algaLinks;
	
	public FormaPagamentoDtoAssembler() {
        super(FormaPagamentoController.class, FormaPagamentoDto.class);
    }
	
	@Override
	public FormaPagamentoDto toModel(FormaPagamento formaPagamento) {
		FormaPagamentoDto formaPagamentoDto = createModelWithId(formaPagamento.getId(), formaPagamento);
		modelMapper.map(formaPagamento, formaPagamentoDto);
		
		formaPagamentoDto.add(algaLinks.linkToFormasPagamento("formasPagamento"));
		
		return formaPagamentoDto;
	}
	
	@Override
	public CollectionModel<FormaPagamentoDto> toCollectionModel(Iterable<? extends FormaPagamento> entities) {
		return super.toCollectionModel(entities).add(algaLinks.linkToFormasPagamento());
	}
}
