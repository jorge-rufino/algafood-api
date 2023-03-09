package com.algaworks.algafood.api.assembler;

import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.FormaPagamentoDto;
import com.algaworks.algafood.domain.model.FormaPagamento;

@Component
public class FormaPagamentoDtoAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public FormaPagamentoDto toDto(FormaPagamento formaPagamento) {
		return modelMapper.map(formaPagamento, FormaPagamentoDto.class);
	}
	
//	Como alteramos as formas de pagamento da classe Restaurante de "List" para "Set", precisamos alterar o parametro
//	para "Collection" que é a classe Pai tanto de List quanto Set.
	public List<FormaPagamentoDto> toCollectionDto(Collection<FormaPagamento> formaPagamentos){
		return formaPagamentos.stream()
				.map(formaPagamento -> toDto(formaPagamento))
				.toList();
	}
}
