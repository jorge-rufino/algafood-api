package com.algaworks.algafood.api.v1.disassembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.model.input.RestauranteInputDto;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDtoDisassembler {
	
	@Autowired
	private ModelMapper modelMapper;
	
//	Converte um "RestauranteInput" em um "Restaurante"
	public Restaurante toDomainObject(RestauranteInputDto restauranteInput) {
		return modelMapper.map(restauranteInput, Restaurante.class);		
	}
	
	public void copyToDomainObject(RestauranteInputDto restauranteInput, Restaurante restaurante) {
//Para evitar esta excpetion:identifier of an instance of com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		restaurante.setCozinha(new Cozinha());
		
//Para evitar esta excpetion:identifier of an instance of com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		if(restaurante.getEndereco() != null) {
			restaurante.getEndereco().setCidade(new Cidade());
		}
		
		modelMapper.map(restauranteInput, restaurante);
	}
}
