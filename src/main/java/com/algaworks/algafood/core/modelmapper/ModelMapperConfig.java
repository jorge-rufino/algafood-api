package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.EnderecoDto;
import com.algaworks.algafood.api.model.input.ItemPedidoInputDto;
import com.algaworks.algafood.domain.model.Endereco;
import com.algaworks.algafood.domain.model.ItemPedido;

//Esta classe cria uma instancia de "ModelMapper" dentro do contexto do Spring

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		
		var modelMapper = new ModelMapper();
		
//		Criamos um novo mapeamento entre "Endereco" e "EnderecoDto"
		var enderecoToEnderecoDtoTypeMap = modelMapper.createTypeMap(
				Endereco.class, EnderecoDto.class);
		
//		Definimos que o "nome" do "Estado" é valor do atributo "estado" da classe "CidadeResumoDto"
		enderecoToEnderecoDtoTypeMap.<String>addMapping(
				enderecoSrc -> enderecoSrc.getCidade().getEstado().getNome(), 
				(enderecoDtoDestino, valor) -> enderecoDtoDestino.getCidade().setEstado(valor));
		
		modelMapper.createTypeMap(ItemPedidoInputDto.class, ItemPedido.class)
	    .addMappings(mapper -> mapper.skip(ItemPedido::setId));
		
		return modelMapper;
	}
}
