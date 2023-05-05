package com.algaworks.algafood.core.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

@Retention(RUNTIME)
@Target(METHOD)
public @interface CheckSecurity {
	
	public @interface Cozinhas{
		
//		Precisa estar autenticado e ter o scopo de leitura
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar{ }
		
//		Precisa da permissao para editar e ter o scopo de escrita
		@PreAuthorize("hasAuthority('SCOPE_WRITE') AND hasAuthority('EDITAR_COZINHAS')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar{ }
	}

	public @interface Restaurantes {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') AND hasAuthority('EDITAR_RESTAURANTES')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarCadastro { }
		
//		Permite acesso do "Responsavel do Restaurante" mesmo que ele não tenha nenhuma permissão 
//		Usando "@" podemos chamar métodos de um bean. Os beans do spring tem sempre a inicial do nome minuscula,e o restante igual.
		@PreAuthorize("hasAuthority('SCOPE_WRITE') AND (hasAuthority('EDITAR_RESTAURANTES') OR @algaSecurity.gerenciaRestaurante(#restauranteId))")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarFuncionamento { }

		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
		
	public @interface Pedidos {

//	PostAuthorize permite a execução do método porém antes do resultado ser serializado, ele faz as checagems que forem configuradas
//	O "returnObject" é o objeto de resposta do controlador, no caso do metodo "buscarPorCodigo", é o um objeto do tipo "PedidoDto"
		
		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@PostAuthorize("hasAuthority('CONSULTAR_PEDIDOS') OR "
					+ "@algaSecurity.getUsuarioId() == returnObject.cliente.id OR"		//Se o usuario for o cliente que fez o pedido
					+ "@algaSecurity.gerenciaRestaurante(returnObject.restaurante.id)")	//Se o usuário for o responsavel pelo restaurante
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeBuscar { }
		
//		Usando "#" podemos pegar o valor do "pathvariable" como parametro, desde que tenham os nomes iguais
		@PreAuthorize("hasAuthority('SCOPE_READ') and (hasAuthority('CONSULTAR_PEDIDOS') or " //Usuario com permissao pode pesquisar tudo
				+ "@algaSecurity.getUsuarioId() == #filtro.clienteId or"				//Cliente pesquisa somente os pedidos dele
				+ "@algaSecurity.gerenciaRestaurante(#filtro.restauranteId))")			//Responsavel pelo restaurante pesquisa todos os pedidos do restaurante dele
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodePesquisar { }
		
//		Qualquer usuario autenticado e com Scopo WRITE pode criar um pedido
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeCriar { }

//		Para gerenciar os pedidos: Scopo Write e (permissao GERENCIAR_PEDIDOS ou ser o responsavel pelo restaurante do pedido)
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and (hasAuthority('GERENCIAR_PEDIDOS') or "
				+ "@algaSecurity.gerenciaRestauranteDoPedido(#codigoPedido))")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarPedidos {
		}		
	}
	
	public @interface FormasPagamento {

	    @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeEditar { }

	    @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeConsultar { }
	    
	}
	
	public @interface Cidades {

	    @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_CIDADES')")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeEditar { }

	    @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeConsultar { }
	    
	}

	public @interface Estados {
	    
	    @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_ESTADOS')")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeEditar { }

	    @PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeConsultar { }
	    
	}
	
	public @interface UsuariosGruposPermissoes {

	    @PreAuthorize("hasAuthority('SCOPE_WRITE') and "
	            + "@algaSecurity.getUsuarioId() == #usuarioId")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeAlterarPropriaSenha { }
	    
	    @PreAuthorize("hasAuthority('SCOPE_WRITE') and (hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') or "
	            + "@algaSecurity.getUsuarioId() == #usuarioId)")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeAlterarUsuario { }

	    @PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES')")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeEditar { }
	    

	    @PreAuthorize("hasAuthority('SCOPE_READ') and hasAuthority('CONSULTAR_USUARIOS_GRUPOS_PERMISSOES')")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeConsultar { }
	    
	    @PreAuthorize("hasAuthority('SCOPE_READ') and (hasAuthority('CONSULTAR_USUARIOS_GRUPOS_PERMISSOES')) or @algaSecurity.getUsuarioId() == #usuarioId")
	    @Retention(RUNTIME)
	    @Target(METHOD)
	    public @interface PodeConsultarUsuario {  }
	    
	}
}
