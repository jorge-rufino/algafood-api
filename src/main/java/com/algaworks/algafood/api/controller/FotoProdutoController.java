package com.algaworks.algafood.api.controller;

import java.nio.file.Path;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.FotoProdutoDtoAssembler;
import com.algaworks.algafood.api.model.FotoProdutoDto;
import com.algaworks.algafood.api.model.input.FotoProdutoInput;
import com.algaworks.algafood.api.model.input.FotoProdutoMultiplaInput;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.services.FotoProdutoService;
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
	
	@PutMapping(consumes = 	MediaType.MULTIPART_FORM_DATA_VALUE)
	public FotoProdutoDto atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) {
	
		Produto produto = produtoService.buscarPorId(restauranteId, produtoId);
		
		FotoProduto novaFoto = new FotoProduto();		
		novaFoto.setProduto(produto);
		novaFoto.setDescricao(fotoProdutoInput.getDescricao());
		novaFoto.setNomeArquivo(fotoProdutoInput.getArquivo().getOriginalFilename());
		novaFoto.setContentType(fotoProdutoInput.getArquivo().getContentType());
		novaFoto.setTamanho(fotoProdutoInput.getArquivo().getSize());
		
		return fotoProdutoDtoAssembler.toDto(fotoProdutoService.salvar(novaFoto));
	}
	
	@PutMapping(value = "/multiplas", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void atualizarFotoMultiplas(
			@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid FotoProdutoMultiplaInput fotos) {
		
		for (FotoProdutoInput foto : fotos.getFotos()) {
			var nomeArquivo = UUID.randomUUID().toString() + "_"+ foto.getArquivo().getOriginalFilename();
			
			var arquivoFoto = Path.of("/Users/Jorge Rufino/Desktop/catalogo", nomeArquivo);
		
			try {
				foto.getArquivo().transferTo(arquivoFoto);
			} catch (Exception e) {
				throw new RuntimeException();
			}
		}
		
	}

}
