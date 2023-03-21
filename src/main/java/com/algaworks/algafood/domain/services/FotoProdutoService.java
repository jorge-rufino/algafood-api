package com.algaworks.algafood.domain.services;

import java.io.InputStream;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.services.FotoStorageService.NovaFoto;

@Service
public class FotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private FotoStorageService fotoStorageService;
	
	@Autowired
	private RestauranteService restauranteService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Transactional
	public FotoProduto salvar(FotoProduto fotoProduto, InputStream dadosArquivo) {
//		Busca a foto de produto e caso exista, exclui ela e salva uma nova
		
		Long restauranteId = fotoProduto.getProduto().getRestaurante().getId();
		Long produtoId = fotoProduto.getProduto().getId();
		String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(fotoProduto.getNomeArquivo());
		String nomeArquivoExistente = null;
		
		Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);
		
		if(fotoExistente.isPresent()) {
			nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
			produtoRepository.deletar(fotoExistente.get());
		}
		
		//Se der algum problema para salvar no banco de dados, vai disparar exception e nao vai executar o trecho abaixo
		fotoProduto.setNomeArquivo(nomeNovoArquivo);
		fotoProduto = produtoRepository.salvar(fotoProduto);
		produtoRepository.flush();
		
		NovaFoto novaFoto = NovaFoto.builder()
				.nomeArquivo(fotoProduto.getNomeArquivo())
				.contentType(fotoProduto.getContentType())
				.inputStream(dadosArquivo)
				.build();
		
//		if (nomeArquivoExistente != null) {
//			fotoStorageService.removerFoto(nomeArquivoExistente);
//		}
//		
//		fotoStorageService.armazenarFoto(novaFoto);
		
		fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
		
		return fotoProduto;
	}
	
	public FotoProduto buscarFoto(Long restauranteId, Long produtoId) {
//		Estamos fazendo a busca para retornar o erro correto de quando nao exisitr restaurante ou produto
//		se nao irá sempre retornar "FotoProdutoNaoEncontradoException"
		restauranteService.buscarPorId(restauranteId);
		produtoService.buscarPorId(restauranteId, produtoId);
		
		return produtoRepository.findFotoById(restauranteId, produtoId)
				.orElseThrow(() -> new FotoProdutoNaoEncontradoException(restauranteId, produtoId));
	}
	
	@Transactional
	public void deletarFoto(Long restauranteId, Long produtoId) {
		FotoProduto fotoProduto = buscarFoto(restauranteId, produtoId);
		
		produtoRepository.deletar(fotoProduto);
		produtoRepository.flush();
		
		fotoStorageService.removerFoto(fotoProduto.getNomeArquivo());
	}
}