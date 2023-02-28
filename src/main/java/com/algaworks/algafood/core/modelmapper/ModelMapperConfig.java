package com.algaworks.algafood.core.modelmapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.algaworks.algafood.api.model.RestauranteDto;
import com.algaworks.algafood.domain.model.Restaurante;

//Esta classe cria uma instancia de "ModelMapper" dentro do contexto do Spring

@Configuration
public class ModelMapperConfig {

	@Bean
	public ModelMapper modelMapper() {
		var modelMapper = new ModelMapper();
		
//		Aqui fazemos a correspondencia da "taxaFrete" do Restaurante para "precoFrete" do RestauranteDto
//		Repare que pegamos o metodo de "get" de Restaurante e o metodo "set" de RestauranteDto
		modelMapper.createTypeMap(Restaurante.class, RestauranteDto.class)
			.addMapping(Restaurante::getTaxaFrete, RestauranteDto::setPrecoFrete);
		
		return modelMapper;
	}
}
