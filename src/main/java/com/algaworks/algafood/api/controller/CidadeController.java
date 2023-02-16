package com.algaworks.algafood.api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.services.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	CidadeService cidadeService;
	
	@GetMapping
	public List<Cidade> listar(){
		return cidadeService.listar();				
	}
	
	@GetMapping("{cidadeId}")
	public Cidade buscarPorId(@PathVariable Long cidadeId){		
		return cidadeService.buscarPorId(cidadeId);
	}
	
	@PostMapping	
	@ResponseStatus(HttpStatus.CREATED)
	public Cidade adicionar (@RequestBody @Valid Cidade cidade){
		//Caso tente salvar com um Estado que não existe, devemos retornar status "400 Bad Request" em vez de "404"
		try {
			return cidadeService.salvar(cidade);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}		
	}
	
	@PutMapping("{id}")
	public Cidade atualizar(@PathVariable Long id, @RequestBody @Valid Cidade cidade){
		
		Cidade cidadeAtual = cidadeService.buscarPorId(id);
		
		BeanUtils.copyProperties(cidade, cidadeAtual, "id");
			
		try {
			return cidadeService.salvar(cidadeAtual);
		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
		
	}
	
	@DeleteMapping("{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long cidadeId){
		cidadeService.deletar(cidadeId);
	}
	
}
