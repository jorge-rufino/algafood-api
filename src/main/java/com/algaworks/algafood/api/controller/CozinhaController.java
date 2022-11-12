package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

//Esta annotation é mais completa e implementa as annotations "@Controller" e "@ResponseBody"
@RestController		
@RequestMapping(value = "/cozinhas") // , produces = MediaType.APPLICATION_JSON_VALUE) -- Para definir para todos os metodos
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository repository;
	
//	Chama este medodo quando o "Accept" da requisição solicitar JSON
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Cozinha> listar(){
		return repository.listar();
	}
	
//	Chama este medodo quando o "Accept" da requisição solicitar XML
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public List<Cozinha> listar2(){
		return repository.listar();
	}
}
