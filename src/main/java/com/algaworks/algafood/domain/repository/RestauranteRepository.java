package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository  extends JpaRepository<Restaurante, Long>{
	
	//Busca os restaurantes que tenham taxa frente entre os valores
	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
	
	//Busca por nome e pelo id da cozinha
	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long id); 
	
//	Busca pelo primeiro nome que encontrar
	Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome);
	
//	Busca os 2 primeiros nomes
	List<Restaurante> findTop2ByNomeContaining(String nome);
}
