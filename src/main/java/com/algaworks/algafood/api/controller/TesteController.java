package com.algaworks.algafood.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {
	
	@Autowired
	private CozinhaRepository repository;
	
	@Autowired
	private RestauranteRepository restauranteRepository;
	
//	RequesParam pega o valor que vem na URI da requisição, depois do ponto de "?"
//	como os estamos utilizando a palavra "nome" como variavel e parametro, podemos excluir a annotation "@RequestParam"
	@GetMapping("/cozinhas/por-nome")
	public List<Cozinha> cozinhasPorNome (String nome){
		return repository.findVariasByNomeContaining(nome);
	}
	
	@GetMapping("/cozinhas/unica-por-nome")
	public Optional<Cozinha> cozinhaPorNome (String nome){
		return repository.findByNome(nome);
	}
	
	@GetMapping("/cozinhas/exists")
	public Boolean existePorNome (String nome){
		return repository.existsByNome(nome);
	}
	
	@GetMapping("/restaurantes/por-taxa-frete")
	public List<Restaurante> restaurantesPorTaxaFrete(BigDecimal taxaInicial, BigDecimal taxaFinal){
		return restauranteRepository.findByTaxaFreteBetween(taxaInicial, taxaFinal);
	}
	
	@GetMapping("/restaurantes/por-nome-e-cozinha")
	public List<Restaurante> restaurantesPorNomeAndCozinha(String nome, Long cozinhaId){
		return restauranteRepository.findByNomeContainingAndCozinhaId(nome, cozinhaId);
	}
	
	@GetMapping("/restaurantes/primeiro-por-nome")
	public Optional<Restaurante> restaurantePrimeiroPorNome(String nome){
		return restauranteRepository.findFirstRestauranteByNomeContaining(nome);
	}
	
	@GetMapping("/restaurantes/top-2-por-nome")
	public List<Restaurante> restaurantesTop2PorNome(String nome){
		return restauranteRepository.findTop2ByNomeContaining(nome);
	}
	
	@GetMapping("/restaurantes/quantidade-cozinha")
	public int restaurantesQuantidadeCozinha(Long idCozinha){
		return restauranteRepository.countByCozinhaId(idCozinha);
	}
}
