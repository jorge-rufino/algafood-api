package com.algaworks.algafood.core.security;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

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
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_COZINHAS')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeEditar{ }
	}

	public @interface Restaurantes {
		
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and hasAuthority('EDITAR_RESTAURANTES')")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarCadastro { }
		
//		Permite acesso do "Responsavel do Restaurante" mesmo que ele não tenha nenhuma permissão 
//		Usando "@" podemos chamar métodos de um bean. Os beans do spring tem sempre a inicial do nome minuscula,e o restante igual. 
//		Usando "#" podemos pegar o valor do "pathvariable" como parametro, desde que tenham os nomes iguais
		@PreAuthorize("hasAuthority('SCOPE_WRITE') and (hasAuthority('EDITAR_RESTAURANTES') or @algaSecurity.gerenciaRestaurante(#restauranteId))")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeGerenciarFuncionamento { }

		@PreAuthorize("hasAuthority('SCOPE_READ') and isAuthenticated()")
		@Retention(RUNTIME)
		@Target(METHOD)
		public @interface PodeConsultar { }
		
	}
}
