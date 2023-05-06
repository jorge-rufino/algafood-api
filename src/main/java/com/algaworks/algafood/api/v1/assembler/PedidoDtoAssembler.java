package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.PedidoController;
import com.algaworks.algafood.api.v1.model.PedidoDto;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoDtoAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoDto> {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AlgaLinks algaLinks;

	@Autowired
	private AlgaSecurity algaSecurity;

	public PedidoDtoAssembler() {
		super(PedidoController.class, PedidoDto.class);
	}

	@Override
	public PedidoDto toModel(Pedido pedido) {
		PedidoDto pedidoDto = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoDto);

		// Não usei o método algaSecurity.podePesquisarPedidos(clienteId, restauranteId)
		// aqui,
		// porque na geração do link, não temos o id do cliente e do restaurante,
		// então precisamos saber apenas se a requisição está autenticada e tem o escopo
		// de leitura
		if (algaSecurity.podePesquisarPedidos()) {
			pedidoDto.add(algaLinks.linkToPedidos("pedidos"));
		}

//		Irá mostrar estes links de acordo com as permissoes de usuario
		if (algaSecurity.podeGerenciarPedidos(pedido.getCodigo())) {
			if (pedido.podeSerConfirmado()) {
				pedidoDto.add(algaLinks.linkToConfirmacaoPedido(pedido.getCodigo(), "confirmar"));
			}

			if (pedido.podeSerCancelado()) {
				pedidoDto.add(algaLinks.linkToCancelamentoPedido(pedido.getCodigo(), "cancelar"));
			}

			if (pedido.podeSerEntregue()) {
				pedidoDto.add(algaLinks.linkToEntregaPedido(pedido.getCodigo(), "entregar"));
			}
		}

		if (algaSecurity.podeConsultarRestaurantes()) {
			pedidoDto.getRestaurante().add(algaLinks.linkToRestaurante(pedido.getRestaurante().getId()));
		}

		if (algaSecurity.podeConsultarUsuariosGruposPermissoes()) {
			pedidoDto.getCliente().add(algaLinks.linkToUsuario(pedidoDto.getCliente().getId()));
		}

		if (algaSecurity.podeConsultarFormasPagamento()) {
			pedidoDto.getFormaPagamento().add(algaLinks.linkToFormaPagamento(pedidoDto.getFormaPagamento().getId()));
		}

		if (algaSecurity.podeConsultarCidades()) {
			pedidoDto.getEnderecoEntrega().getCidade()
					.add(algaLinks.linkToCidade(pedido.getEnderecoEntrega().getCidade().getId()));
		}

		if (algaSecurity.podeConsultarRestaurantes()) {
//		Para cada Item, teremos um link para o Produto. 
			pedidoDto.getItens().forEach(item -> {
				item.add(algaLinks.linkToProduto(pedido.getRestaurante().getId(), item.getProdutoId(), "produto"));
			});
		}

		return pedidoDto;
	}

}
