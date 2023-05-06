package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.FormaPagamentoController;
import com.algaworks.algafood.api.v1.model.FormaPagamentoDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoDtoAssembler extends RepresentationModelAssemblerSupport<FormaPagamento, FormaPagamentoDto> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;

	@Autowired
	private AlgaSecurity algaSecurity;

	public FormaPagamentoDtoAssembler() {
		super(FormaPagamentoController.class, FormaPagamentoDto.class);
	}

	@Override
	public FormaPagamentoDto toModel(FormaPagamento formaPagamento) {
		FormaPagamentoDto formaPagamentoDto = createModelWithId(formaPagamento.getId(), formaPagamento);
		modelMapper.map(formaPagamento, formaPagamentoDto);

		if (algaSecurity.podeConsultarFormasPagamento()) {
			formaPagamentoDto.add(algaLinks.linkToFormasPagamento("formasPagamento"));
		}

		return formaPagamentoDto;
	}

	@Override
	public CollectionModel<FormaPagamentoDto> toCollectionModel(Iterable<? extends FormaPagamento> entities) {
		CollectionModel<FormaPagamentoDto> collectionModel = super.toCollectionModel(entities);
	    
	    if (algaSecurity.podeConsultarFormasPagamento()) {
	        collectionModel.add(algaLinks.linkToFormasPagamento());
	    }
	    return collectionModel;
	}
}
