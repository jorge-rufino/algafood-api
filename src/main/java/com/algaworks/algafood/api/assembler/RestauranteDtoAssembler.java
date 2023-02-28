package com.algaworks.algafood.api.assembler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.RestauranteDto;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteDtoAssembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
//	Converte um "Restaurante" em um "RestauranteDto"
	public RestauranteDto toDTO(Restaurante restaurante) {
		return modelMapper.map(restaurante, RestauranteDto.class);
	}
	
	public List<RestauranteDto> toCollectionDTO(List<Restaurante> restaurantes){
		return restaurantes.stream()
			.map(restaurante -> toDTO(restaurante))	
			.sorted(Comparator.comparing(RestauranteDto::getId))	//Tive que adicinar para organizar a lista por ID
			.collect(Collectors.toList());
	}

}
