package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Restaurante;

@Repository
public interface RestauranteRepository  extends JpaRepository<Restaurante, Long>{
	
//			Metodos - Exemplos de Prefixo e palavras chaves de Query Methods
	
	//Busca os restaurantes que tenham taxa frente entre os valores
	List<Restaurante> findByTaxaFreteBetween(BigDecimal taxaInicial, BigDecimal taxaFinal);
	
	//Busca por nome e pelo id da cozinha
	List<Restaurante> findByNomeContainingAndCozinhaId(String nome, Long id);
	
	//Busca pelo primeiro nome que encontrar
	Optional<Restaurante> findFirstRestauranteByNomeContaining(String nome);
	
	//Busca os 2 primeiros nomes
	List<Restaurante> findTop2ByNomeContaining(String nome);
	
	//Mostra a quantidade de cozinhas 
	int countByCozinhaId(Long id);
	
//						Exemplo de Metodos utilizando JPQL
	
	//Perceba que se usassemos o mesmo nome de variavel para o "ID", não precisariamos da annotation "@Param".
//	@Query("from Restaurante where nome like %:nome% and cozinha.id = :cozinhaId")
//	List<Restaurante> consultarPorNomeAndCozinhaId(String nome,@Param("cozinhaId") Long id); 
	
//	A query está no arquivo "orm.xml" dentro da pasta "META-INF", dentro da pasta "resources"
	List<Restaurante> consultarPorNomeAndCozinhaId(String nome,@Param("cozinhaId") Long id);
	
//	A implementação deste método, está na classe "RestauranteRepositoryImpl"
	List<Restaurante> find (String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);
}
