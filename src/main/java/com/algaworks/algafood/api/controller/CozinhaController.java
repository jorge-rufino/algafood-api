package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.model.CozinhasXmlWrapper;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;

//Esta annotation é mais completa e implementa as annotations "@Controller" e "@ResponseBody"
@RestController		
@RequestMapping(value = "/cozinhas") 
public class CozinhaController {
	
	@Autowired
	private CozinhaRepository repository;
	
	@GetMapping
	public List<Cozinha> listar(){
		return repository.listar();
	}
	
	@GetMapping(value = "/{cozinhaId}")
	//Como a "PathVariable" e a variavel long tem o mesmo nome, poderiamos deixar a annotation "@PathVariable" sem o parametro
	public ResponseEntity<Cozinha> buscarId(@PathVariable("cozinhaId") Long cozinhaId) {
		Cozinha cozinha = repository.buscarPorId(cozinhaId);

		if (cozinha != null) {
			return ResponseEntity.ok(cozinha);
		}

//		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();	-> Faz o mesmo que a linha de baixo
		return ResponseEntity.notFound().build();

	}

	//Se for solicitado uma resposta em XML, vai usar este método
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml(){
		return new CozinhasXmlWrapper(repository.listar());
	}
}
