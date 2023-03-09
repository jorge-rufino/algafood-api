package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

//Esta annotation faz com que o Spring não instancie uma implementação para está interface
@NoRepositoryBean
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID>{

	Optional<T> buscarPrimeiro();
	
	void detach(T entity);
}
