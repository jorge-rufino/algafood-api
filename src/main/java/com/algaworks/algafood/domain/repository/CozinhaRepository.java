package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{
	
	//Faz o mesmo que o metodo acima. Sintaxe para o nome do método: "find"+"quaisquer palavras"+"By"+"Nome do Atributos" 
	List<Cozinha> findVariasByNome(String nome);
	
	//Faz a busca não exata, ou seja, em qualquer parte do nome
	List<Cozinha> findVariasByNomeContaining(String nome);	

	Optional<Cozinha> findByNome(String nome);
}
