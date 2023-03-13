package com.algaworks.algafood.domain.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.PedidoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private FormaPagamentoService formaPagamentoService;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private CidadeService cidadeService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ProdutoService produtoService;
	
	public List<Pedido> listar(Specification<Pedido> specification){
		return pedidoRepository.findAll(specification);
	}
	
//	public Pedido buscarPorId(Long pedidoId) {
//		return pedidoRepository.findById(pedidoId)
//				.orElseThrow(() -> new PedidoNaoEncontradoException(pedidoId));
//	}
	
	public Pedido buscarPorCodigo(String codigoPedido) {
		return pedidoRepository.findByCodigo(codigoPedido)
				.orElseThrow(() -> new PedidoNaoEncontradoException(codigoPedido));
	}
	
	@Transactional
	public Pedido emitir(Pedido pedido) {
		validarPedido(pedido);
		validarItens(pedido);
		
		pedido.setTaxaFrete(pedido.getRestaurante().getTaxaFrete());
		pedido.calcularValotTotal();
		
		return pedidoRepository.save(pedido);
	}
	
	private void validarPedido(Pedido pedido) {
		Restaurante restaurante = restauranteService.buscarPorId(pedido.getRestaurante().getId());
		FormaPagamento formaPagamento = formaPagamentoService.buscarPorId(pedido.getFormaPagamento().getId());		
		Cidade cidade = cidadeService.buscarPorId(pedido.getEnderecoEntrega().getCidade().getId());
		Usuario cliente = usuarioService.buscarPorId(pedido.getCliente().getId()); 
		
		pedido.setRestaurante(restaurante);
		pedido.setFormaPagamento(formaPagamento);
		pedido.getEnderecoEntrega().setCidade(cidade);
		pedido.setCliente(cliente);
		
		if (restaurante.naoAceitaFormaPagamento(formaPagamento)) {
			throw new NegocioException(String.format("Forma de pagamento '%s' não é aceita por esse restaurante.",
	                formaPagamento.getDescricao()));
		}
	}
	
	private void validarItens(Pedido pedido) {
		pedido.getItens().forEach(item -> {
			Produto produto = produtoService.buscarPorId(pedido.getRestaurante().getId(), item.getProduto().getId());
			item.setPedido(pedido);
			item.setProduto(produto);
			item.setPrecoUnitario(produto.getPreco());
		});
	}
}
