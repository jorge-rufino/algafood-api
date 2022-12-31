package com.algaworks.algafood.infrastructure.repository;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.algaworks.algafood.domain.repository.CustomJpaRepository;

public class CustomJpaRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> 
						implements CustomJpaRepository<T, ID>{

	private EntityManager entityManager;
	
	public CustomJpaRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
	}

	@Override
	public Optional<T> buscarPrimeiro() {
		
		//getDomainClass busca um objeto referente a classe que a estiver utilizando
		var jpql = "from " + getDomainClass().getName();
		
//		traz somente o primeiro da busca
		T entity = entityManager.createQuery(jpql, getDomainClass()).setMaxResults(1).getSingleResult();
		
//		retorna "Null" ou retorna a entidade
		return Optional.ofNullable(entity);
	}
	
}
