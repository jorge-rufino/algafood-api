package com.algaworks.algafood.api.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.algaworks.algafood.api.assembler.FotoProdutoDtoAssembler;
import com.algaworks.algafood.api.model.FotoProdutoDto;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.services.FotoProdutoService;
import com.algaworks.algafood.domain.services.FotoStorageService;
import com.algaworks.algafood.domain.services.ProdutoService;

@RestController
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class FotoProdutoController {
	
	@Autowired
	private FotoProdutoService fotoProdutoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private FotoProdutoDtoAssembler fotoProdutoDtoAssembler;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@PutMapping(consumes = 	MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoDto atualizarFoto(
			@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
	
		Produto produto = produtoService.buscarPorId(restauranteId, produtoId);
		MultipartFile arquivo = fotoProdutoInput.getArquivo();
				
		FotoProduto novaFoto = new FotoProduto();		
		novaFoto.setProduto(produto);
		novaFoto.setDescricao(fotoProdutoInput.getDescricao());
		novaFoto.setNomeArquivo(arquivo.getOriginalFilename());
		novaFoto.setContentType(arquivo.getContentType());
		novaFoto.setTamanho(arquivo.getSize());
		
		return fotoProdutoDtoAssembler.toDto(fotoProdutoService.salvar(novaFoto, arquivo.getInputStream()));
	}
	
//	Na requisicao => Accept = application/json
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public FotoProdutoDto buscarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		FotoProduto fotoProduto = fotoProdutoService.buscarFoto(restauranteId, produtoId);		
		return fotoProdutoDtoAssembler.toDto(fotoProduto);
	}
	
//	Na requisicao => Accept = image/jpeg
//	Agora pegamos os valores do campo "accept" da requisição, que inclusive podem ser varios seprados por ","
	@GetMapping
	public ResponseEntity<InputStreamResource>  buscarFotoImagem(
			@PathVariable Long restauranteId, @PathVariable Long produtoId, @RequestHeader(name = "accept") String acceptHeader) 
					throws HttpMediaTypeNotAcceptableException {
		
//		Como este método retorna uma imagem e não um "json", ele acaba não mostrando as exceptions lançadas para o
//		consumidor da API, portanto precisamos do "try/catch" para captura-las e mostrar o erro 404
		
		try {
			FotoProduto fotoProduto = fotoProdutoService.buscarFoto(restauranteId, produtoId);		

//			Converte a String do metodo "getContentType" em uma "MediaType"
			MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
			
//			MediaTypes que são passadas/aceitas pelo consumidor da API
			List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
			
			verificaCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
			
			InputStream inputStream = fotoStorageService.recuperarFoto(fotoProduto.getNomeArquivo());
			
			return ResponseEntity.ok()
					.contentType(mediaTypeFoto)
					.body(new InputStreamResource(inputStream));
		} catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
		fotoProdutoService.deletarFoto(restauranteId, produtoId);
	}
	
	private void verificaCompatibilidadeMediaType(
			MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
		
//		Verifica se pelo menos uma "MediaType" aceita pelo consumidor da API é compativel com o MediaType da foto
		boolean compativel = mediaTypesAceitas.stream()
				.anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
		
		if(!compativel) {
			throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
		}
	}
	
//	@PutMapping(value = "/multiplas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public void atualizarFotoMultiplas(
//			@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid FotoProdutoMultiplaInput fotos) {
//		
//		for (FotoProdutoInput foto : fotos.getFotos()) {
//			var nomeArquivo = UUID.randomUUID().toString() + "_"+ foto.getArquivo().getOriginalFilename();
//			
//			var arquivoFoto = Path.of("/Users/Jorge Rufino/Desktop/catalogo", nomeArquivo);
//		
//			try {
//				foto.getArquivo().transferTo(arquivoFoto);
//			} catch (Exception e) {
//				throw new RuntimeException();
//			}
//		}
//		
//	}

}
