package com.algaworks.algafood.domain.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import com.algaworks.algafood.domain.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.exception.NegocioException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)	//Para nao chamar o "@EqualsAndHashCode" da superclasse
public class Pedido extends AbstractAggregateRoot<Pedido>{				//Extendo para poder registrar um "evento"

	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String codigo;
	
	private BigDecimal subtotal;
	private BigDecimal taxaFrete;
	private BigDecimal valorTotal;
	
	@CreationTimestamp	
	private OffsetDateTime dataCriacao;	
	
	private OffsetDateTime dataConfirmacao;
	private OffsetDateTime dataCancelamento;
	private OffsetDateTime dataEntrega;
	
	@Enumerated(EnumType.STRING)
	private StatusPedido status = StatusPedido.CRIADO;
	
	@Embedded
	private Endereco enderecoEntrega;
	
	@ManyToOne
	@JoinColumn(nullable = false)
	private Restaurante restaurante;
	
	@ManyToOne
	@JoinColumn(name = "usuario_cliente_id", nullable = false)
	private Usuario cliente;
	
//	"Lazy" só faz a busca no banco caso a variavel seja chamada diretamente.
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private FormaPagamento formaPagamento;
	
//	"Cascade" faz com que o os objetos de ItemPedidos sejam salvos tb se nao somente os objetos de Pedido serao salvos
	@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
	private List<ItemPedido> itens = new ArrayList<>();

	public void calcularValotTotal() {
		getItens().forEach(ItemPedido::calcularPrecoTotal);
	    
	    this.subtotal = getItens().stream()
	        .map(item -> item.getPrecoTotal())
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	    
	    this.valorTotal = this.subtotal.add(this.taxaFrete);
	}
	
	public void confirmar() {
		setStatus(StatusPedido.CONFIRMADO);
		setDataConfirmacao(OffsetDateTime.now());
		
		//Registrando o evento que será disparado somente após o pedido ser salvo no banco
		registerEvent(new PedidoConfirmadoEvent(this));
	}
	
	public void entregar() {
		setStatus(StatusPedido.ENTREGUE);
		setDataEntrega(OffsetDateTime.now());
	}
	
	public void cancelar() {
		setStatus(StatusPedido.CANCELADO);
		setDataCancelamento(OffsetDateTime.now());
		
		registerEvent(new PedidoCanceladoEvent(this));
	}
	
	private void setStatus(StatusPedido novoStatus) {
		if(getStatus().naoPodeAlterarPara(novoStatus)) {
			final String mensagem;
			
			if(getStatus().equals(novoStatus)) {
				mensagem = String.format("Pedido %s já foi %s.", getCodigo(), novoStatus.getDescricao());
			} else {
				mensagem = String.format("Status do pedido %s não pode ser alterado de %s para %s"
						, getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao());
			}
			
			throw new NegocioException(mensagem);
		}
		
		this.status = novoStatus;
	}
	
//	Antes de inserir um novo registro de Pedido no banco de dados, ele executa este método
	@PrePersist
	private void gerarCodigo() {
		setCodigo(UUID.randomUUID().toString());
	}
}
