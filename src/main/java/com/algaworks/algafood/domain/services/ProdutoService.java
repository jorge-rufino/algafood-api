package com.algaworks.algafood.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private RestauranteService restauranteService;
	
	public List<Produto> listar(Long restauranteId, Boolean incluirInativos){
		Restaurante restaurante = restauranteService.buscarPorId(restauranteId);
		
		if(incluirInativos) {
			return produtoRepository.findTodosByRestaurante(restaurante);
			
		}else {
			return produtoRepository.findAtivosByRestaurante(restaurante);	
		}
	}
	
	public Produto buscarPorId(Long restauranteId, Long produtoId) {
//		Buscando restaurante somente para disparar a exception caso ele nao exista
		restauranteService.buscarPorId(restauranteId);
		
		return produtoRepository.findById(restauranteId, produtoId)
				.orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
	}

	@Transactional
	public Produto salvar(Long id, Produto produto) {
		Restaurante restaurante = restauranteService.buscarPorId(id);
		produto.setRestaurante(restaurante);
		
		return produtoRepository.save(produto);
	}
}
