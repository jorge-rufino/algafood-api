package com.algaworks.algafood.api.v1.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.ProdutoController;
import com.algaworks.algafood.api.v1.model.ProdutoDto;
import com.algaworks.algafood.domain.model.Produto;

@Component
public class ProdutoDtoAssembler extends RepresentationModelAssemblerSupport<Produto, ProdutoDto>{
	

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private AlgaLinks algaLinks;
	
	public ProdutoDtoAssembler() {
		super(ProdutoController.class, ProdutoDto.class);
	}
	
	@Override
	public ProdutoDto toModel(Produto produto) {
		ProdutoDto produtoDto = createModelWithId(produto.getId(), produto, produto.getRestaurante().getId());
		modelMapper.map(produto, produtoDto);
		
		produtoDto.add(algaLinks.linkToProdutos(produto.getRestaurante().getId(), "produtos"));
		produtoDto.add(algaLinks.linkToFotoProduto(produto.getRestaurante().getId(), produto.getId(), "foto"));
		
		return produtoDto;
	}	
}
