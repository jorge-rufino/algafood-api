package com.algaworks.algafood.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
		
//		Exemplo de respostas com sucesso (As 2 linhas fazem a mesma coisa)
		
//		return ResponseEntity.status(HttpStatus.OK).body(cozinha);
//		return ResponseEntity.ok(cozinha);
		
//		Exemplo de resposta caso o URI tivesse mudado
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.LOCATION, "http://api.algafood.local:8080/cozinhas");
		
		return ResponseEntity
				.status(HttpStatus.FOUND)
				.headers(headers)
				.build();
	}

	//Se for solicitado uma resposta em XML, vai usar este método
	@GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
	public CozinhasXmlWrapper listarXml(){
		return new CozinhasXmlWrapper(repository.listar());
	}
}
