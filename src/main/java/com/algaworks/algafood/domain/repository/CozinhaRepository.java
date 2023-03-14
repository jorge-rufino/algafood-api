package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Cozinha;

@Repository
public interface CozinhaRepository extends CustomJpaRepository<Cozinha, Long>{
	
	//Faz o mesmo que o metodo acima. Sintaxe para o nome do método: "find"+"quaisquer palavras"+"By"+"Nome do Atributos" 
	List<Cozinha> findVariasByNome(String nome);
	
	//Faz a busca não exata, ou seja, em qualquer parte do nome
	List<Cozinha> findVariasByNomeContaining(String nome);	

	Optional<Cozinha> findByNome(String nome);
	
	//Prefixo "exists" procura pelo nome e se encontrar retornar "true" (Não é Case Sensitive). Não é aconselhavel utilizar o "Containing".
	boolean existsByNome(String nome);
	
//	Podemos especificar a paginacao diretamente na consulta assim tb caso tenhamos metodos proprios mas no caso do "findAll"
//	ele ja faz isso por padrao
	
//	Page<Cozinha> findAll(Pageable pageable);
}
