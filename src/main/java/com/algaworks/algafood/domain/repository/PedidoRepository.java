package com.algaworks.algafood.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.Pedido;

@Repository
public interface PedidoRepository extends CustomJpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido>{
	
//	"Fetch" faz com que o JPA faça a busca das tabelas em um unico select
	@Query("from Pedido p join fetch p.cliente join fetch p.restaurante r join fetch r.cozinha")
	List<Pedido> findAll();

	Optional<Pedido> findByCodigo(String codigo);
	
//	Query no arquivo "orm.mxl" em "resources/meta-inf/orm.xml
	boolean isPedidoGerenciadoPor(String codigoPedido, Long usuarioId);
}
