package com.algaworks.algafood.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;

@Service
public class FluxoPedidoService {
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private PedidoRepository pedidoRepository;	//Precisamos do repositorio para poder disparar os eventos
	
	@Transactional
	public void confirmar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarPorCodigo(codigoPedido);		
		pedido.confirmar();
		
//		Precisamos fazer o "save" do objeto/pedido agora pois os eventos registrados só serão disparados após salvar no banco 
		pedidoRepository.save(pedido);
	}
	
	@Transactional
	public void entregar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarPorCodigo(codigoPedido);
		pedido.entregar();
	}
	
	@Transactional
	public void cancelar(String codigoPedido) {
		Pedido pedido = pedidoService.buscarPorCodigo(codigoPedido);		
		pedido.cancelar();
	}
}
