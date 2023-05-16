package com.algaworks.algafood.api.v1.openapi.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;

import com.algaworks.algafood.api.v1.model.FotoProdutoDto;
import com.algaworks.algafood.api.v1.model.input.FotoProdutoInput;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@SecurityRequirement(name = "security_oauth")
public interface FotoProdutoControllerOpenApi {

	FotoProdutoDto atualizarFoto(Long restauranteId, Long produtoId, FotoProdutoInput fotoProdutoInput)
			throws IOException;

	FotoProdutoDto buscarFoto(Long restauranteId, Long produtoId);

	//	Quando o serviço de storage de imagens for local, vamos mostrar a imagem atraves do InputStream, mas quando for remoto ou em nuvem
	//	vamos mostrar a URL da imagem
	// As fotos dos produtos ficarão públicas (não precisa de autorização para acessá-las)
	ResponseEntity<?> buscarFotoImagem(Long restauranteId, Long produtoId, String acceptHeader)
			throws HttpMediaTypeNotAcceptableException;

	void deletarFoto(Long restauranteId, Long produtoId);

}