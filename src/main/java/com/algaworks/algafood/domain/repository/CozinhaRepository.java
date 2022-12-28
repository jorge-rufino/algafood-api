package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends JpaRepository<Cozinha, Long>{
//	Este metodo faz a busca por "Nome", para funcionar ele deve ter mesmo nome do atributo que queremos fazer a busca
//	Ele faz uma busca EXATA
	List<Cozinha> nome(String nome);
	
	//Faz o mesmo que o metodo acima. Sintaxe para o nome do método: "find"+"quaisquer palavras"+"By"+"Nome do Atributos" 
	List<Cozinha> findVariasByNome(String nome);	
	
	//Aqui ele busca somente um elemento, caso o resultado seja mais de 1, vai dar erro
	Optional<Cozinha> findByNome(String nome);
	
	
}
