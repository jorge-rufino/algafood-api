package com.algaworks.algafood.api.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.ProdutoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.model.PedidoDto;
import com.algaworks.algafood.domain.model.Pedido;

@Component
public class PedidoDtoAssembler extends RepresentationModelAssemblerSupport<Pedido, PedidoDto>{

	@Autowired
	private ModelMapper modelMapper;
	
	public PedidoDtoAssembler() {
		super(PedidoController.class, PedidoDto.class);
	}
	
	@Override
	public PedidoDto toModel(Pedido pedido) {
		PedidoDto pedidoDto = createModelWithId(pedido.getCodigo(), pedido);
		modelMapper.map(pedido, pedidoDto);
		
		TemplateVariables pageVariables = new TemplateVariables(
				new TemplateVariable("page", VariableType.REQUEST_PARAM),
				new TemplateVariable("size", VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", VariableType.REQUEST_PARAM));
		
		TemplateVariables filtrosVariables = new TemplateVariables(
				new TemplateVariable("clienteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM));
				
		String pedidosUrl = WebMvcLinkBuilder.linkTo(PedidoController.class).toUri().toString();
		
		pedidoDto.add(Link.of(UriTemplate.of(pedidosUrl, pageVariables.concat(filtrosVariables)), "pedidos"));
		
//		pedidoDto.add(WebMvcLinkBuilder.linkTo(PedidoController.class).withRel("pedidos"));
		
		pedidoDto.getRestaurante().add(WebMvcLinkBuilder.linkTo(
				methodOn(RestauranteController.class).buscarId(pedido.getRestaurante().getId())).withSelfRel());
		
		pedidoDto.getCliente().add(WebMvcLinkBuilder.linkTo(
				methodOn(UsuarioController.class).buscarPorId(pedido.getCliente().getId())).withSelfRel());
		
		// Passamos null no segundo argumento, porque é indiferente para a construção da URL do recurso de forma de pagamento
		pedidoDto.getFormaPagamento().add(WebMvcLinkBuilder.linkTo(
				methodOn(FormaPagamentoController.class).buscarId(pedido.getFormaPagamento().getId(), null)).withSelfRel());
		
		pedidoDto.getEnderecoEntrega().getCidade().add(WebMvcLinkBuilder.linkTo(
				methodOn(CidadeController.class).buscarPorId(pedido.getEnderecoEntrega().getCidade().getId())).withSelfRel());
		
//		Para cada Item, teremos um link para o Produto. 
		pedidoDto.getItens().forEach(item -> {			
			item.add(WebMvcLinkBuilder.linkTo(
					methodOn(ProdutoController.class).buscarPorId(pedido.getRestaurante().getId(), item.getProdutoId())).withRel("produto"));
		});		
		
		return pedidoDto;
	}
	
}
