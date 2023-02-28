package com.algaworks.algafood.api.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.model.input.RestauranteInputDTO;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDtoDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
//	Converte um "RestauranteInput" em um "Restaurante"
	public Restaurante toDomainObject(RestauranteInputDTO restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);		
	}
	
	public void copyToDomainObject(RestauranteInputDTO restauranteInput, Restaurante restaurante) {
//Para evitar esta excpetion:identifier of an instance of com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		restaurante.setCozinha(new Cozinha());
		
		modelMapper.map(restauranteInput, restaurante);
	}
}
