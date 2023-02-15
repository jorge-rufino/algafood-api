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

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.services.CozinhaService;

//Esta annotation é mais completa e implementa as annotations "@Controller" e "@ResponseBody"
@RestController		
@RequestMapping(value = "/cozinhas") 
public class CozinhaController {
			
	@Autowired
	private CozinhaService cozinhaService;
	
	@GetMapping
	public List<Cozinha> listar(){
		return cozinhaService.listar();
	}
	
	@GetMapping(value = "/{cozinhaId}")
	public Cozinha buscarId(@PathVariable("cozinhaId") Long cozinhaId) {		
		return cozinhaService.buscarPorId(cozinhaId);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)		
	public Cozinha adicionar (@RequestBody @Valid Cozinha cozinha) {
		return cozinhaService.salvar(cozinha);
	}	
	
	@PutMapping("/{cozinhaId}")
	public Cozinha atualizar(@PathVariable Long cozinhaId,@RequestBody Cozinha cozinha){
		Cozinha cozinhaAtual = cozinhaService.buscarPorId(cozinhaId);

		BeanUtils.copyProperties(cozinha, cozinhaAtual, "id");
		
		return cozinhaService.salvar(cozinhaAtual);
	}
	
	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletar(@PathVariable Long cozinhaId){								
		cozinhaService.deletar(cozinhaId);
	}
}
