package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

@RestController
@RequestMapping("/teste")
public class TesteController {
	
	@Autowired
	private CozinhaRepository repository;
	
//	RequesParam pega o valor que vem na URI da requisição, depois do ponto de "?"
//	@GetMapping("/cozinhas/por-nome")
//	public List<Cozinha> cozinhaPorNome (@RequestParam("nome") String nomeCozinha){
//		return repository.consultarPorNome(nomeCozinha);
//	}
}
