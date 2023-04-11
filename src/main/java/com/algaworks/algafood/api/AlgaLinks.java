package com.algaworks.algafood.api;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariable.VariableType;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.controller.CidadeController;
import com.algaworks.algafood.api.controller.CozinhaController;
import com.algaworks.algafood.api.controller.EstadoController;
import com.algaworks.algafood.api.controller.FluxoPedidoController;
import com.algaworks.algafood.api.controller.FormaPagamentoController;
import com.algaworks.algafood.api.controller.FotoProdutoController;
import com.algaworks.algafood.api.controller.GrupoController;
import com.algaworks.algafood.api.controller.GrupoPermissaoController;
import com.algaworks.algafood.api.controller.PedidoController;
import com.algaworks.algafood.api.controller.PermissaoController;
import com.algaworks.algafood.api.controller.ProdutoController;
import com.algaworks.algafood.api.controller.RestauranteController;
import com.algaworks.algafood.api.controller.RestauranteFormaPagamentoController;
import com.algaworks.algafood.api.controller.RestauranteUsuarioController;
import com.algaworks.algafood.api.controller.UsuarioController;
import com.algaworks.algafood.api.controller.UsuarioGrupoController;

@Component
public class AlgaLinks {
	
	public static final	TemplateVariables PAGINACAO_VARIABLES = new TemplateVariables(
				new TemplateVariable("page", VariableType.REQUEST_PARAM),
				new TemplateVariable("size", VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", VariableType.REQUEST_PARAM));
	
	public static final TemplateVariables PROJECAO_VARIABLES = new TemplateVariables(
				new TemplateVariable("projecao", VariableType.REQUEST_PARAM)); 
	
	public Link linkToPedidos(String rel) {		
		TemplateVariables filtrosVariables = new TemplateVariables(
				new TemplateVariable("clienteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("restauranteId", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoInicio", VariableType.REQUEST_PARAM),
				new TemplateVariable("dataCriacaoFim", VariableType.REQUEST_PARAM));
				
		String pedidosUrl = WebMvcLinkBuilder.linkTo(PedidoController.class).toUri().toString();
		
		return Link.of(UriTemplate.of(pedidosUrl, PAGINACAO_VARIABLES.concat(filtrosVariables)), rel);
	}
	
	public Link linkToConfirmacaoPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(methodOn(FluxoPedidoController.class).confirmar(codigoPedido)).withRel(rel).withType("Put");
	}
	
	public Link linkToEntregaPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(methodOn(FluxoPedidoController.class).entregar(codigoPedido)).withRel(rel).withType("Put");
	}
	
	public Link linkToCancelamentoPedido(String codigoPedido, String rel) {
		return WebMvcLinkBuilder.linkTo(methodOn(FluxoPedidoController.class).cancelar(codigoPedido)).withRel(rel).withType("Put");
	}
	
	public Link linkToRestauranteAtivacao(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(methodOn(RestauranteController.class).ativar(restauranteId)).withRel(rel).withType("Put");
	}
	
	public Link linkToRestauranteInativacao(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(methodOn(RestauranteController.class).inativar(restauranteId)).withRel(rel).withType("Delete");
	}
	
	public Link linkToRestauranteAbertura(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(methodOn(RestauranteController.class).abrirRestaurante(restauranteId)).withRel(rel).withType("Put");
	}
	
	public Link linkToRestauranteFechamento(Long restauranteId, String rel) {
		return WebMvcLinkBuilder.linkTo(methodOn(RestauranteController.class).fecharRestaurante(restauranteId)).withRel(rel).withType("Put");
	}
	
	public Link linkToRestaurante(Long restauranteId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(RestauranteController.class)
	            .buscarId(restauranteId)).withRel(rel);
	}

	public Link linkToRestaurante(Long restauranteId) {
	    return linkToRestaurante(restauranteId, IanaLinkRelations.SELF.value());
	}
	
	public Link linkToRestaurantes(String rel) {
		String restaurantesUrl = WebMvcLinkBuilder.linkTo(RestauranteController.class).toUri().toString();
		
		return Link.of(UriTemplate.of(restaurantesUrl, PROJECAO_VARIABLES), rel);
	}
	
	public Link linkToRestaurantes() {
	    return linkToRestaurantes(IanaLinkRelations.SELF.value());
	}

	public Link linkToUsuario(Long usuarioId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(UsuarioController.class)
	            .buscarPorId(usuarioId)).withRel(rel);
	}

	public Link linkToUsuario(Long usuarioId) {
	    return linkToUsuario(usuarioId, IanaLinkRelations.SELF.value());
	}

	public Link linkToUsuarios(String rel) {
	    return WebMvcLinkBuilder.linkTo(UsuarioController.class).withRel(rel);
	}

	public Link linkToUsuarios() {
	    return linkToUsuarios(IanaLinkRelations.SELF.value());
	}

	public Link linkToGruposUsuario(Long usuarioId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(UsuarioGrupoController.class)
	            .listar(usuarioId)).withRel(rel);
	}

	public Link linkToGruposUsuario(Long usuarioId) {
	    return linkToGruposUsuario(usuarioId, IanaLinkRelations.SELF.value());
	}

	public Link linkToUsuarioGrupoAssociacao(Long usuarioId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(UsuarioGrupoController.class)
	            .associarGrupo(usuarioId, null)).withRel(rel).withType("Put");
	}
	
	public Link linkToUsuarioGrupoDesassociacao(Long usuarioId, Long grupoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(UsuarioGrupoController.class)
	            .desassociarGrupo(usuarioId, grupoId)).withRel(rel).withType("Delete");
	}
	
	public Link linkToGrupos(String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(GrupoController.class)
	            .listar()).withRel(rel);
	}
	
	public Link linkToGrupos() {
	    return linkToGrupos(IanaLinkRelations.SELF.value());
	}
	
	public Link linkToGrupoPermissoes(Long grupoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(GrupoPermissaoController.class)
	            .listar(grupoId)).withRel(rel);
	}
	
	public Link linkToGrupoPermissoes(Long grupoId) {
	    return WebMvcLinkBuilder.linkTo(methodOn(GrupoPermissaoController.class)
	            .listar(grupoId)).withRel(IanaLinkRelations.SELF.value());
	}
	
	public Link linkToPermissoes(String rel) {
	    return WebMvcLinkBuilder.linkTo(PermissaoController.class).withRel(rel);
	}
	
	public Link linkToPermissoes() {
	    return linkToPermissoes(IanaLinkRelations.SELF.value());
	}
	
	public Link linkToGrupoPermissaoAssociacao(Long grupoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(GrupoPermissaoController.class)
	            .associarPermissao(grupoId, null)).withRel(rel).withType("Put");
	}
	
	public Link linkToGrupoPermissaoDesassociacao(Long grupoId, Long permissaoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(GrupoPermissaoController.class)
	            .desassociarPermissao(grupoId, permissaoId)).withRel(rel).withType("Delete");
	}
	
	public Link linkToResponsaveisRestaurante(Long restauranteId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(RestauranteUsuarioController.class)
	            .listar(restauranteId)).withRel(rel);
	}

	public Link linkToResponsaveisRestaurante(Long restauranteId) {
	    return linkToResponsaveisRestaurante(restauranteId, IanaLinkRelations.SELF.value());
	}

	public Link linkToRestauranteResponsavelDesassociacao(Long restauranteId, Long usuarioId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(RestauranteUsuarioController.class)
	            .desassociarResponsavel(restauranteId, usuarioId)).withRel(rel).withType("Delete");
	}

	public Link linkToRestauranteResponsavelAssociacao(Long restauranteId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(RestauranteUsuarioController.class)
	            .associarResponsavel(restauranteId, null)).withRel(rel).withType("Put");
	}

	public Link linkToFormaPagamento(Long formaPagamentoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(FormaPagamentoController.class)
	            .buscarId(formaPagamentoId, null)).withRel(rel);
	}

	public Link linkToFormaPagamento(Long formaPagamentoId) {
	    return linkToFormaPagamento(formaPagamentoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToFormasPagamento(String rel) {
	    return WebMvcLinkBuilder.linkTo(FormaPagamentoController.class).withRel(rel);
	}

	public Link linkToFormasPagamento() {
	    return linkToFormasPagamento(IanaLinkRelations.SELF.value());
	}
	
	public Link linkToRestauranteFormasPagamento(Long restauranteId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(RestauranteFormaPagamentoController.class)
	            .listar(restauranteId)).withRel(rel);
	}
	
	public Link linkToRestauranteFormasPagamento(Long restauranteId) {
	    return linkToRestauranteFormasPagamento(restauranteId, IanaLinkRelations.SELF.value());
	}
	
	public Link linkToRestauranteFormaPagamentoDesassociacao(Long restauranteId, Long formaPagamentoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(RestauranteFormaPagamentoController.class)
	            .desassociarFormaPagamento(restauranteId, formaPagamentoId)).withRel(rel).withType("Delete");
	}

//	Quando o método espera um parametro e este parametro é um @PathVariable, o Hateos automaticamente o adiciona a URL.
//	Basta passarmos "null" para ele entender
	
//	Exemplo: "http://api.algafood.local:8080/restaurantes/1/formas-pagamento/{formaPagamentoId}"
	
	public Link linkToRestauranteFormaPagamentoAssociacao(Long restauranteId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(RestauranteFormaPagamentoController.class)
	            .associarFormaPagamento(restauranteId, null)).withRel(rel).withType("Put");
	}
	
	public Link linkToCidade(Long cidadeId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(CidadeController.class)
	            .buscarPorId(cidadeId)).withRel(rel);
	}

	public Link linkToCidade(Long cidadeId) {
	    return linkToCidade(cidadeId, IanaLinkRelations.SELF.value());
	}

	public Link linkToCidades(String rel) {
	    return WebMvcLinkBuilder.linkTo(CidadeController.class).withRel(rel);
	}

	public Link linkToCidades() {
	    return linkToCidades(IanaLinkRelations.SELF.value());
	}

	public Link linkToEstado(Long estadoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(EstadoController.class)
	            .buscarId(estadoId)).withRel(rel);
	}

	public Link linkToEstado(Long estadoId) {
	    return linkToEstado(estadoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToEstados(String rel) {
	    return WebMvcLinkBuilder.linkTo(EstadoController.class).withRel(rel);
	}

	public Link linkToEstados() {
	    return linkToEstados(IanaLinkRelations.SELF.value());
	}

	public Link linkToProduto(Long restauranteId, Long produtoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(ProdutoController.class)
	            .buscarPorId(restauranteId, produtoId))
	            .withRel(rel);
	}

	public Link linkToProduto(Long restauranteId, Long produtoId) {
	    return linkToProduto(restauranteId, produtoId, IanaLinkRelations.SELF.value());
	}

	public Link linkToProdutos(Long restauranteId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(ProdutoController.class).listar(restauranteId, null)).withRel(rel);
	}
	
	public Link linkToFotoProduto(Long restauranteId, Long produtoId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(FotoProdutoController.class)
	            .buscarFoto(restauranteId, produtoId)).withRel(rel);
	}

	public Link linkToFotoProduto(Long restauranteId, Long produtoId) {
	    return linkToFotoProduto(restauranteId, produtoId, IanaLinkRelations.SELF.value());
	}
	
	public Link linkToProdutos(Long restauranteId) {
	    return linkToProdutos(restauranteId, IanaLinkRelations.SELF.value());
	}
	
	public Link linkToCozinhas(String rel) {
	    return WebMvcLinkBuilder.linkTo(CozinhaController.class).withRel(rel);
	}

	public Link linkToCozinhas() {
	    return linkToCozinhas(IanaLinkRelations.SELF.value());
	}
	
	public Link linkToCozinha(Long cozinhaId, String rel) {
	    return WebMvcLinkBuilder.linkTo(methodOn(CozinhaController.class)
	            .buscarId(cozinhaId)).withRel(rel);
	}
	
	public Link linkToCozinha(Long cozinhaId) {
	    return linkToCozinha(cozinhaId, IanaLinkRelations.SELF.value());
	} 

}
