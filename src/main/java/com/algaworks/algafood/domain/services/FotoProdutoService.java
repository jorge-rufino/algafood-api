package com.algaworks.algafood.domain.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class FotoProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Transactional
	public FotoProduto salvar(FotoProduto foto) {
//		Busca a foto de produto e caso exista, exclui ela e salva uma nova
		
		Long restauranteId = foto.getProduto().getRestaurante().getId();
		Long produtoId = foto.getProduto().getId();
		
		Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);
		
		if(fotoExistente.isPresent()) {
			produtoRepository.deletar(fotoExistente.get());
		}
		
		return produtoRepository.salvar(foto);
	}
}